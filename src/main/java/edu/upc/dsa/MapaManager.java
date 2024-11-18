package edu.upc.dsa;

import edu.upc.dsa.exceptions.EmptyListException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;

import java.util.List;

public interface MapaManager {

    public int NumberUsers();
    public void AddUser(User usuario);
    public User AddUser1(String nombre, String apellidos, String correoelec, String fechanacimiento);
    public List<User> OrdenarUsuariosAlfa()throws EmptyListException;
    public User ConsultInfoUser(String id)throws UserNotFoundException;
    public PuntoInteres addPuntoInteres(Double coordHorizontal, Double coordVertical, String tipo);
    public PuntoInteres existePuntoInteres(double coordHoriz, double coordVert);
    public int RegistrarUsuarioenPuntoInteres(String id, Double horiz, Double vert);
    public List<PuntoInteres> consultarPuntosIntUsuario(String id)throws EmptyListException;
    public List<User> consultaviewersdepunto(Double horiz,Double verti)throws EmptyListException;
    public List<PuntoInteres> consultarPuntosPorTipoString(String tipoStr);
}
