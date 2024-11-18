package edu.upc.dsa;

import edu.upc.dsa.exceptions.EmptyListException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class MapaManagerTest {
    MapaManager tm;

    @Before
    public void setUp() {
        this.tm = MapaManagerImpl.getInstance();

    }

    @Test
    public void testAddUser1() {
        // Funcionamiento de AddUser1
        User user = tm.AddUser1("carla", "Sureda", "carla@hotmail.com", "02/02/1995");
        Assert.assertNotNull("El usuario creado no deberia ser nulo",user);
        Assert.assertEquals(1, tm.NumberUsers());
        Assert.assertEquals("carla", user.getNombre());
    }

    @Test
    public void testAddPuntoInteres() {
        // Verificamos que solo se puede añadir un punto con una coordenada en concreto
        PuntoInteres punto = tm.addPuntoInteres(10.43,75.86,"DOOR");
        Assert.assertEquals(punto, tm.existePuntoInteres(10.43,75.86));
        PuntoInteres punto1 = tm.addPuntoInteres(10.43,75.86,"WALL");
        Assert.assertNotEquals(punto1, tm.existePuntoInteres(10.43,75.86));

    }

    @Test
    public void testOrdenarUsuariosAlfaEmptyList() {
        // Excepcion de la lista vacia
        Exception exception = Assert.assertThrows(EmptyListException.class, () -> {tm.OrdenarUsuariosAlfa();});
        Assert.assertEquals("La lista de usuarios esta vacia y no se puede ordenar", exception.getMessage());
    }

    @Test
    public void testRegistro(){
        PuntoInteres punto2 = tm.addPuntoInteres(12.34,56.78,"BRIDGE");
        Assert.assertEquals(punto2, tm.existePuntoInteres(12.34,56.78));
        User user1 = tm.AddUser1("Encarna", "Flores", "enca@hotmail.com", "03/08/1999");
        Assert.assertEquals("Encarna", user1.getNombre());
        Assert.assertEquals(1, tm.RegistrarUsuarioenPuntoInteres(user1.getIdentificador(), 12.34,56.78));
    }

    @Test
    public void testRegistrar(){
        PuntoInteres punto3 = tm.addPuntoInteres(78.90,90.67,"BRIDGE");
        Assert.assertEquals(punto3, tm.existePuntoInteres(78.90,90.67));
        User user2 = tm.AddUser1("Manu", "Santiago", "manuu@hotmail.com", "12/05/1997");
        Assert.assertEquals("Manu", user2.getNombre());
        Assert.assertEquals(1, tm.RegistrarUsuarioenPuntoInteres(user2.getIdentificador(), 78.90,90.67));
    }

}
