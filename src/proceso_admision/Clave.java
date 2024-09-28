/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proceso_admision;

/**
 *
 * @author Keler
 */
public class Clave {

    private int indice;
    private int posicion;
    private String claveRespuestas;

    public Clave(int indice, int posicion, String claveRespuestas) {
        this.indice = indice;
        this.posicion = posicion;
        this.claveRespuestas = claveRespuestas;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getClaveRespuestas() {
        return claveRespuestas;
    }

    public void setClaveRespuestas(String claveRespuestas) {
        this.claveRespuestas = claveRespuestas;
    }

}
