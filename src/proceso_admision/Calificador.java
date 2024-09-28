/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proceso_admision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Keler
 */
public class Calificador {

    private final int threshold;
    private final int numParts;

    public Calificador(int threshold, int numParts) {
        this.threshold = threshold;
        this.numParts = numParts;
    }

    private List<Respuesta> obtenerRespuestas() throws SQLException {
        List<Respuesta> respuestas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ide_iIndice, res_vcRespuesta FROM respuesta"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Respuesta respuesta = new Respuesta(rs.getInt("ide_iIndice"), rs.getString("res_vcRespuesta").replaceAll(" ", "*"));
                respuestas.add(respuesta);
            }
        }
        return respuestas;
    }

    private List<Clave> obtenerClaves() throws SQLException {
        List<Clave> claves = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clave"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clave clave = new Clave(rs.getInt("cla_iIndice"), rs.getInt("cla_iPosicion"), rs.getString("cla_vcRespuesta"));
                claves.add(clave);
            }
        }
        return claves;
    }

    private List<Rango> obtenerRangos() throws SQLException {
        List<Rango> rangos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rango"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Rango rango = new Rango(rs.getInt("ran_iIndiceMinimo"), rs.getInt("ran_iIndiceMaximo"), rs.getInt("cla_iPosicion"));
                rangos.add(rango);
            }
        }
        return rangos;
    }

    private Clave obtenerClaveCorrespondiente(int indice, List<Clave> claves, List<Rango> rangos) {
        for (Rango rango : rangos) {
            if (indice >= rango.getIndiceMinimo() && indice <= rango.getIndiceMaximo()) {
                for (Clave clave : claves) {
                    if (clave.getPosicion() == rango.getPosicion()) {
                        return clave;
                    }
                }
            }
        }
        return null; // Si no se encuentra ninguna clave correspondiente
    }

    public void calificarRespuestas() throws SQLException {
        List<Respuesta> respuestas = obtenerRespuestas();
        List<Clave> claves = obtenerClaves();
        List<Rango> rangos = obtenerRangos();
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new CalificarTask(respuestas, claves, rangos, threshold, numParts));
    }

    private class CalificarTask extends RecursiveAction {

        private List<Respuesta> respuestas;
        private List<Clave> claves;
        private List<Rango> rangos;
        private int threshold;
        private int numParts;

        public CalificarTask(List<Respuesta> respuestas, List<Clave> claves, List<Rango> rangos, int threshold, int numParts) {
            this.respuestas = respuestas;
            this.claves = claves;
            this.rangos = rangos;
            this.threshold = threshold;
            this.numParts = numParts;
        }

        @Override
        protected void compute() {
            if (respuestas.size() <= threshold) {
                calificar(respuestas, claves, rangos);
            } else {
                int partSize = respuestas.size() / numParts;
                List<CalificarTask> tasks = new ArrayList<>();
                for (int i = 0; i < numParts; i++) {
                    int start = i * partSize;
                    int end = (i == numParts - 1) ? respuestas.size() : (i + 1) * partSize;
                    tasks.add(new CalificarTask(respuestas.subList(start, end), claves, rangos, threshold, numParts));
                }
                invokeAll(tasks);
            }
        }
    }

    private void calificar(List<Respuesta> respuestas, List<Clave> claves, List<Rango> rangos) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);  // Desactivar el auto-commit

            for (Respuesta respuesta : respuestas) {
                Clave clave = obtenerClaveCorrespondiente(respuesta.getIndice(), claves, rangos);
                if (clave == null) {
                    System.out.println("No se encontro clave para el indice: " + respuesta.getIndice());
                    continue;
                }

                double notaHabilidades = 0.0;
                double notaConocimientos = 0.0;

                for (int i = 0; i < respuesta.getRespuesta().length(); i++) {
                    char respuestaChar = respuesta.getRespuesta().charAt(i);
                    char claveChar = clave.getClaveRespuestas().charAt(i);

                    if (respuestaChar == claveChar) {
                        if (i < 30) {
                            notaHabilidades += 20;
                        } else {
                            notaConocimientos += 20;
                        }
                    } else if (respuestaChar != '*' && respuestaChar != ' ') {
                        if (i < 30) {
                            notaHabilidades -= 1.125;
                        } else {
                            notaConocimientos -= 1.125;
                        }
                    }
                }

                System.out.println("Calificacion para indice " + respuesta.getIndice() + ": Habilidades = " + notaHabilidades + ", Conocimientos = " + notaConocimientos);

                // Verificar si el índice ya existe en la tabla calificacion
                String checkQuery = "SELECT COUNT(*) FROM calificacion WHERE ide_iIndice = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setInt(1, respuesta.getIndice());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        // Si no existe, insertarlo
                        String insertQuery = "INSERT INTO calificacion (ide_iIndice, cal_fNotaHabilidades, cal_fNotaConocimientos, cal_fNotaFinal) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, respuesta.getIndice());
                            insertStmt.setDouble(2, notaHabilidades);
                            insertStmt.setDouble(3, notaConocimientos);
                            insertStmt.setDouble(4, notaHabilidades + notaConocimientos);
                            insertStmt.executeUpdate();
                            //System.out.println("Insercion exitosa para indice " + respuesta.getIndice());
                        }
                    } else {
                        // Si existe, actualizarlo
                        String updateQuery = "UPDATE calificacion SET cal_fNotaHabilidades = ?, cal_fNotaConocimientos = ?, cal_fNotaFinal = ? WHERE ide_iIndice = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setDouble(1, notaHabilidades);
                            updateStmt.setDouble(2, notaConocimientos);
                            updateStmt.setDouble(3, notaHabilidades + notaConocimientos);
                            updateStmt.setInt(4, respuesta.getIndice());
                            updateStmt.executeUpdate();
                            //System.out.println("Actualización exitosa para indice " + respuesta.getIndice());
                        }
                    }
                }
            }
            conn.commit();  // Confirmar la transacción
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
