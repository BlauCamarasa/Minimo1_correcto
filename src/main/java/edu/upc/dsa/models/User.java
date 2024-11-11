package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

import java.util.LinkedList;
import java.util.List;

public class User {
    String identificador;
    String nombre;
    String apellidos;
    String correoelec;
    String fechanacimiento;
    List<PuntoInteres> puntosInteresusuario = new LinkedList<>();

    public User(String identificador, String nombre, String apellidos, String correoelec, String fechanacimiento) {

        this(); //Crida al constructor que no te parametres d'entrada
        if (identificador!=null)this.setIdentificador(identificador);
        this.setNombre(nombre);
        this.setApellidos(apellidos);
        this.setCorreoelec(correoelec);
        this.setFechanacimiento(fechanacimiento);
    }

    public User() {
        this.setIdentificador(RandomUtils.getId());
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoelec() {
        return correoelec;
    }

    public void setCorreoelec(String correoelec) {
        this.correoelec = correoelec;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public List<PuntoInteres> getPuntosInteresusuario() {
        return puntosInteresusuario;
    }

    public void setPuntosInteresusuario(List<PuntoInteres> puntosInteresusuario) {
        this.puntosInteresusuario = puntosInteresusuario;
    }

    @Override
    public String toString() {
        StringBuilder puntosInteresStr = new StringBuilder();
        for (PuntoInteres punto : puntosInteresusuario) {
            puntosInteresStr.append(punto.toString()).append("\n");
        }

        return "User{" +
                "identificador='" + identificador + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correoelec='" + correoelec + '\'' +
                ", fechanacimiento='" + fechanacimiento + '\'' +
                ", puntosInteresusuario=\n" + puntosInteresStr.toString() +
                '}';
    }



}
