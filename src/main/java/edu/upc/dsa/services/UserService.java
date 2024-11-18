package edu.upc.dsa.services;


import edu.upc.dsa.MapaManager;
import edu.upc.dsa.MapaManagerImpl;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/usuarios", description = "Endpoint to User Service")
@Path("/usuarios")
public class UserService {

    private MapaManager tm;

    public UserService() {
        this.tm = MapaManagerImpl.getInstance();
        if (tm.NumberUsers()==0) {
            User user1 = this.tm.AddUser1("Maria","Camarasa","Mary@gmail.com","23/1/2002");
            User user2 =this.tm.AddUser1("Jon","Trabolta","Trabo@gmail.com","2/9/1994");
            User user3 =this.tm.AddUser1("Monica","Naranjo","MonNa@gmail.com","16/12/2003");
            User user4 =this.tm.AddUser1("Roger","Fernandez","RugerFEr@gmail.com","9/3/1975");
            PuntoInteres punto1 =this.tm.addPuntoInteres(23.45,78.97,"DOOR");
            PuntoInteres punto2 =this.tm.addPuntoInteres(56.90,234.32,"WALL");
            PuntoInteres punto3 =this.tm.addPuntoInteres(234.567,38.75,"WALL");

        }

    }

    // Añadir un nuevo usuario
    @POST
    @ApiOperation(value = "Crear un nuevo usuario", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/PostUserNuevo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User usuari) {
        try {
            if (usuari.getNombre()==null || usuari.getApellidos()==null || usuari.getCorreoelec()==null || usuari.getFechanacimiento()==null ){
                return Response.status(500).build();
            }
            else{
                User usuario = this.tm.AddUser1(usuari.getNombre(), usuari.getApellidos(),usuari.getCorreoelec(),usuari.getFechanacimiento());
                return Response.status(201).entity(usuario).build();
            }
        }catch (Exception e){
            return Response.status(500).build();
        }


    }

    //Obtener toda la lista de users alfabeticamente
    @GET
    @ApiOperation(value = "Get de todos los users ordenados alfabeticamente", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 400, message = "Error")
    })
    @Path("/GetOrderedUsers")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public Response getUsersOrdered() {
        try{
            List<User> users = this.tm.OrdenarUsuariosAlfa();
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
            return Response.status(201).entity(entity).build();
        }
        catch (Exception e){
            return Response.status(400).build();
        }

    }

    //Consultar un usuario a partir de identificador
    @GET
    @ApiOperation(value = "Consultar Usuario a partir de identificador", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code = 400, message = "Error"),
            @ApiResponse(code = 404, message = "Usuario no existe")
    })
    @Path("/GetUsuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFromId(@PathParam("id") String identificator) {

        try{
            User usuario = this.tm.ConsultInfoUser(identificator);
            if (usuario!=null){
                GenericEntity<User> entity = new GenericEntity<User>(usuario) {};
                return Response.status(201).entity(entity).build();
            }
            else {
                return Response.status(404).build();
            }

        }
        catch (Exception e){
            return Response.status(400).build();
        }

    }

    //Registrar que usuario ha pasado por punto interes
    @POST
    @ApiOperation(value = "Registrar que usuario ha pasado por punto interes", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 400, message = "Error"),
            @ApiResponse(code = 404, message = "Usuario/punto interés no válido")

    })
    @Path("/PostPuntoEnUsuario/{Horiz}/{Vert}/{id}")
    public Response RegistrarPuntoInteresToUsuario(@PathParam("Horiz") String horiz, @PathParam("Vert") String vert, @PathParam("id") String identificador) {
        try {
            int codigo = tm.RegistrarUsuarioenPuntoInteres(identificador,Double.parseDouble(horiz), Double.parseDouble(vert));
            if (codigo == 1){
                return Response.status(201).build();
            }
            else{
                return Response.status(404).build();
            }
        }
        catch (Exception e){
            return Response.status(400).build();
        }

    }

    //Consultar los usuarios que han visitado un punto de interes
    @GET
    @ApiOperation(value = "Get de todos los usuarios que han pasado por un punto de interes", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 400, message = "Error"),
            @ApiResponse(code = 404, message = "Punto Interés no existe/La lista de usuario que han pasado por el punto de interes esta vacia")

    })
    @Path("/GetViewersPuntoInteres/{Hor}/{Ver}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersDePuntoInteres(@PathParam("Hor") String hor, @PathParam("Ver") String ver) {
        try{
            List<User> vieweresPtInteres = this.tm.consultaviewersdepunto(Double.parseDouble(hor), Double.parseDouble(ver));
            if(vieweresPtInteres == null || vieweresPtInteres.isEmpty()){
                return Response.status(404).build();
            } else{
                GenericEntity<List<User>> entiti = new GenericEntity<List<User>>(vieweresPtInteres) {};
                return Response.status(201).entity(entiti).build();
            }
        }
        catch (Exception e){
            return Response.status(400).build();
        }
    }



}