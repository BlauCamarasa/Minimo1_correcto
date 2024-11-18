package edu.upc.dsa;

import edu.upc.dsa.exceptions.EmptyListException;
import edu.upc.dsa.models.PuntoInteres.ElementType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;

public class MapaManagerImpl implements MapaManager {
    private static MapaManager instance;
    protected HashMap<String,User> userHashMap;
    protected HashMap<PuntoInteres, List<User>> viewersPuntoInteres;
    protected List<PuntoInteres> MapaDePuntos ;
    final static Logger logger = Logger.getLogger(MapaManagerImpl.class);


    private MapaManagerImpl() {
        this.userHashMap = new HashMap<>();
        this.MapaDePuntos = new LinkedList<>();
        this.viewersPuntoInteres = new HashMap<>();
    }

    public static MapaManager getInstance() {
        if (instance==null) instance = new MapaManagerImpl();
        return instance;
    }



    //Para consultar el numero de usuarios que hay en el sistema
    public int NumberUsers(){
        int numerousuarios = userHashMap.size();
        logger.info("Se ha consultado y hay " + numerousuarios + " usuarios en el sistema");
        return numerousuarios;
    }

    //Añade un usuario en userHashMap
    public void AddUser(User usuario){
        NumberUsers(); // Para ver que aun no se ha añadido el usuario
        logger.info("new user: " + usuario.toString());
        userHashMap.put(usuario.getIdentificador(),usuario);
        logger.info("new user added");
        NumberUsers(); // Para ver que se ha añadido el usuario correctamente
    }

    //Crea usuario i añade usuario a la lista
    public User AddUser1(String nombre, String apellidos, String correoelec, String fechanacimiento){
        User usuario = new User(null,nombre,apellidos,correoelec,fechanacimiento);
        this.AddUser(usuario);
        return usuario;

    }

    //Crea lista usuarios ordenada (apellidos,nombre)
    public List<User> OrdenarUsuariosAlfa()throws EmptyListException {
        List<User> OrderedUsers = new ArrayList<>(userHashMap.values()); // Convertir HashMap a ArrayList
        if (OrderedUsers.isEmpty()){
            logger.info("La lista de usuarios esta vacia y no se puede ordenar");
            throw new EmptyListException("La lista de usuarios esta vacia y no se puede ordenar");
        }
        else{
            logger.info("La lista de usuarios antes de ser ordenada es: "+ OrderedUsers);
            // Ordenar la lista con Comparator
            OrderedUsers.sort(Comparator
                    .comparing(User::getApellidos) // Primero ordenar por apellido
                    .thenComparing(User::getNombre)); // Luego ordenar por nombre en caso de empate
            logger.info("La lista de usuarios despues de ser ordenada es: "+ OrderedUsers);
        }
        return OrderedUsers;
    }

    //Consultar información de un usuario a partir del id
    public User ConsultInfoUser(String id)throws UserNotFoundException {
        User usu = userHashMap.get(id);
        if (usu != null){
            logger.info("El usuario que se consulta es el siguiente: " + usu.toString());
        }
        else{
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return usu;
    }

    // Añadir punto de interes a la lista de MapaDePuntos
    public PuntoInteres addPuntoInteres(Double coordHorizontal, Double coordVertical, String tipo) {

        PuntoInteres punto = existePuntoInteres(coordHorizontal,coordVertical);
        if (punto==null){
            // Crear el nuevo punto de interés ya que no existe ningun punto con estas coordenadas
            PuntoInteres puntoInteres = new PuntoInteres(coordHorizontal, coordVertical, tipo);
            if (puntoInteres.getTipo()==null){
                logger.info("Punto de interés no se ha podido añadir al mapa porque el tipo no es correcto");
                return null;
            }
            else {
                // Añadir el punto a la lista global ya que no existe ninguno con estas coordenadas y el tipo es correcto
                MapaDePuntos.add(puntoInteres);
                logger.info("Punto de interés añadido al mapa: " + puntoInteres.toString());

                // Crear una lista vacía de usuarios para linkearlos con el PuntoInteres cuando estos lo visiten
                List<User> usuarios = new LinkedList<>();
                // Añadir el PuntoInteres con la lista vacía al HashMap
                viewersPuntoInteres.put(puntoInteres, usuarios);

                return puntoInteres;
            }
        }
        else{
            logger.info("Coordenadas del punto de interés que se quiere crear ya existian en el mapa y no se ha podido añadir nuevo punto");
            return null;
        }

    }

    //A partir de las coordenadas devuelve el punto de interes si existe y null si no existe
    public PuntoInteres existePuntoInteres(double coordHoriz, double coordVert) {

        for (PuntoInteres punto : MapaDePuntos) {
            // Comparar las coordenadas con el punto actual
            if (punto.getCoordHorizontal().equals(coordHoriz) && punto.getCoordVertical().equals(coordVert)) {
                logger.info("Punto de interés con coordenadas H: " + coordHoriz + " V: " + coordVert + " ha sido encontrado en el mapa: " + punto.toString());
                return punto;  // Si encontramos el punto, lo devolvemos
            }
        }
        logger.info("Punto de interés con coordenadas H: " + coordHoriz + " V: " + coordVert + " no ha sido encontrado en el mapa");
        // Si no encontramos el punto de interés, devolvemos null
        return null;
    }

    //Registra un usuario que pasa por un punto de interés
    public int RegistrarUsuarioenPuntoInteres(String id, Double horiz, Double vert){
        int controlerror;
        PuntoInteres punto = existePuntoInteres(horiz,vert);
        User usuario = ConsultInfoUser(id);
        if (punto!=null && usuario!=null){
            //Se añade el punto de interes a la lista de puntos del usuario
            usuario.AnadirPuntoInteresVisitado(punto);
            logger.info("Punto de interés " + punto.toString() + " ha sido añadido a " + usuario.toString());
            //Se tiene que añadir el usuario a la lista de usuarios que han visitado ese punto
            List<User> usuarios = viewersPuntoInteres.get(punto);
            usuarios.add(usuario);
            controlerror=1;
        }
        else{
            controlerror=0; // No se ha introducido un punto o un usuario válido
        }
        return controlerror;

    }

    //Consultar puntos interes por los que un usuario ha pasado
    public List<PuntoInteres> consultarPuntosIntUsuario(String id)throws EmptyListException{
        User usuario = ConsultInfoUser(id);
        if (usuario.getPuntosInteresusuario().isEmpty()){
            logger.info("La lista de Puntos de Interes del usuario "+usuario.getNombre()+" esta vacia");
            throw new EmptyListException("La lista de Puntos de Interes del usuario esta vacia");
        }
        else{
            logger.info("La lista de puntos de interes visitados por "+usuario.getNombre()+" es:");
            for (PuntoInteres punto : usuario.getPuntosInteresusuario()) {
                logger.info(punto.toString());
            }
            return usuario.getPuntosInteresusuario();
        }
    }

    //Listado de usuario que han pasado por un punto de interes a partir de coordenadas
    public List<User> consultaviewersdepunto(Double horiz,Double verti)throws EmptyListException{
        PuntoInteres pinteres = existePuntoInteres(horiz,verti);
        if (pinteres!=null){
            List<User> usuarios = viewersPuntoInteres.get(pinteres);
            if (usuarios.isEmpty()){
                logger.info("La lista de usuarios que han visitado el punto "+ pinteres.toString() +"esta vacia.");
                throw new EmptyListException("La lista de usuarios esta vacia");
            }
            else{
                return usuarios;
            }
        }
        else{
            return null; //no se ha encontrado el punto de interes
        }
    }



    //Consultar todos los puntos de interes de un tipo en formato string
    public List<PuntoInteres> consultarPuntosPorTipoString(String tipoStr) {
        List<PuntoInteres> puntosPorTipo = new LinkedList<>();

        // Convertir el String al tipo ElementType
        ElementType tipo;
        try {
            tipo = ElementType.valueOf(tipoStr.toUpperCase()); // Convierte el String a ElementType (asegurándote de que esté en mayúsculas)
        } catch (IllegalArgumentException e) {

            logger.info("Tipo de punto de interés inválido: " + tipoStr);
            return puntosPorTipo;  // Devuelvo lista vacía si el tipo no es válido
        }

        // Recorrer la lista de puntos de interés y filtrar por el tipo
        for (PuntoInteres punto : MapaDePuntos) {
            if (punto.getTipo() == tipo) {
                puntosPorTipo.add(punto);
            }
        }

        // Imprimir el resultado si es necesario
        if (puntosPorTipo.isEmpty()) {
            logger.info("No se encontraron puntos de interés de tipo: " + tipoStr);
        } else {
            logger.info("Puntos de interés de tipo " + tipoStr + ":");
            for (PuntoInteres punto : puntosPorTipo) {
                logger.info(punto.toString());
            }
        }

        return puntosPorTipo;
    }


}