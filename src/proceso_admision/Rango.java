/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proceso_admision;

/**
 *
 * @author Keler
 */
public class Rango {

    private int indiceMinimo;
    private int indiceMaximo;
    private int posicion;

    public Rango(int indiceMinimo, int indiceMaximo, int posicion) {
        this.indiceMinimo = indiceMinimo;
        this.indiceMaximo = indiceMaximo;
        this.posicion = posicion;
    }

    public int getIndiceMinimo() {
        return indiceMinimo;
    }

    public void setIndiceMinimo(int indiceMinimo) {
        this.indiceMinimo = indiceMinimo;
    }

    public int getIndiceMaximo() {
        return indiceMaximo;
    }

    public void setIndiceMaximo(int indiceMaximo) {
        this.indiceMaximo = indiceMaximo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

}
