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
    List<PuntoInteres> MapaDePuntos ;
    final static Logger logger = Logger.getLogger(MapaManagerImpl.class);


    private MapaManagerImpl() {
        this.userHashMap = new HashMap<>();
        this.MapaDePuntos = new LinkedList<>();
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
    public List<User> OrdenarUsuariosAlfa() throws EmptyListException {
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
        // Crear el nuevo punto de interés
        PuntoInteres puntoInteres = new PuntoInteres(coordHorizontal, coordVertical, tipo);
        if (puntoInteres.getTipo()==null){
            logger.info("Punto de interés no se ha podido añadir al mapa porque el tipo no es correcto");
        }
        else {
            // Añadir el punto a la lista global
            MapaDePuntos.add(puntoInteres);
            logger.info("Punto de interés añadido al mapa: " + puntoInteres.toString());
        }
        return puntoInteres;

    }

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


















    /*
    public int size() {
        int ret = this.tracks.size();
        logger.info("size " + ret);

        return ret;
    }

    public Track addTrack(Track t) {
        logger.info("new Track " + t);

        this.tracks.add (t);
        logger.info("new Track added");
        return t;
    }

    public Track addTrack(String title, String singer){
        return this.addTrack(null, title, singer);
    }

    public Track addTrack(String id, String title, String singer) {
        return this.addTrack(new Track(id, title, singer));
    }

    public Track getTrack(String id) {
        logger.info("getTrack("+id+")");

        for (Track t: this.tracks) {
            if (t.getId().equals(id)) {
                logger.info("getTrack("+id+"): "+t);

                return t;
            }
        }

        logger.warn("not found " + id);
        return null;
    }

    public Track getTrack2(String id) throws TrackNotFoundException {
        Track t = getTrack(id);
        if (t == null) throw new TrackNotFoundException();
        return t;
    }


    public List<Track> findAll() {
        return this.tracks;
    }

    @Override
    public void deleteTrack(String id) {

        Track t = this.getTrack(id);
        if (t==null) {
            logger.warn("not found " + t);
        }
        else logger.info(t+" deleted ");

        this.tracks.remove(t);

    }

    @Override
    public Track updateTrack(Track p) {
        Track t = this.getTrack(p.getId());

        if (t!=null) {
            logger.info(p+" rebut!!!! ");

            t.setSinger(p.getSinger());
            t.setTitle(p.getTitle());

            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+p);
        }

        return t;
    }

    public void clear() {
        this.tracks.clear();
    }

     */
}