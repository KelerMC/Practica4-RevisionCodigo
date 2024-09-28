/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proceso_admision;

/**
 *
 * @author Keler
 */
public class Calificacion {
    private int indice;
    private String codigoPostulante;
    private double notaAptitud;
    private double notaConocimientos;
    private double notaFinal;

    public Calificacion(int indice, String codigoPostulante, double notaAptitud, double notaConocimientos, double notaFinal) {
        this.indice = indice;
        this.codigoPostulante = codigoPostulante;
        this.notaAptitud = notaAptitud;
        this.notaConocimientos = notaConocimientos;
        this.notaFinal = notaFinal;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getCodigoPostulante() {
        return codigoPostulante;
    }

    public void setCodigoPostulante(String codigoPostulante) {
        this.codigoPostulante = codigoPostulante;
    }

    public double getNotaAptitud() {
        return notaAptitud;
    }

    public void setNotaAptitud(double notaAptitud) {
        this.notaAptitud = notaAptitud;
    }

    public double getNotaConocimientos() {
        return notaConocimientos;
    }

    public void setNotaConocimientos(double notaConocimientos) {
        this.notaConocimientos = notaConocimientos;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }
    
}
