/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proceso_admision;

import java.io.IOException;
import java.sql.SQLException;

/**
 GRUPO 7- SOFTWARE- PROGRAMACIÓN PARALELA Y CONCURRENTE-2024-I 
 - Integrantes:
    * Díaz Pillaca, Sebastián Alexis
    * Lopez Terrones, Xiomy Ximena
    * Martinez Santos, Luz Cristina
    * Modesto Calixto, Keler   
 */
public class Proceso_admision {

    public static void main(String[] args) throws IOException {

        try {
            int threshold = 500; // Tamaño de carga del trabajo
            int numParts = 4; // Numero de tareas 

            long startTime = System.currentTimeMillis(); // Tiempo inicial

            Calificador calificador = new Calificador(threshold, numParts);
            calificador.calificarRespuestas();

            long endTime = System.currentTimeMillis();
            System.out.println("Tiempo total de ejecucion: " + (endTime - startTime) + " ms");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
