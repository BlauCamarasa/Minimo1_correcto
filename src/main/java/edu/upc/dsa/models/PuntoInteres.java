package edu.upc.dsa.models;

public class PuntoInteres {

    public enum ElementType {
        DOOR, WALL, BRIDGE, POTION, SWORD, COIN, GRASS, TREE
    }
    Double coordHorizontal;
    Double coordVertical;
    ElementType tipo;

    public PuntoInteres(Double corH, Double corV, String tipo) {
        this.setCoordHorizontal(corH);
        this.setCoordVertical(corV);
        this.setTipo(tipo);
    }

    public PuntoInteres(){

    }

    public Double getCoordHorizontal() {
        return coordHorizontal;
    }

    public void setCoordHorizontal(Double coordHorizontal) {
        this.coordHorizontal = coordHorizontal;
    }

    public Double getCoordVertical() {
        return coordVertical;
    }

    public void setCoordVertical(Double coordVertical) {
        this.coordVertical = coordVertical;
    }

    public ElementType getTipo() {
        return tipo;
    }


    // Setter para el tipo con String que convierte a ElementType
    public void setTipo(String tipo) {
        try {
            this.tipo = ElementType.valueOf(tipo.toUpperCase()); // Convierte el String a ElementType
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo inválido: " + tipo);
            this.tipo = null; // Manejar el error asignando null
        }
    }

    //Imprimir info
    @Override
    public String toString() {
        return "PuntoInteres{" + "coordHorizontal=" + coordHorizontal + ", coordVertical=" + coordVertical + ", tipo=" + tipo + '}';
    }

}
