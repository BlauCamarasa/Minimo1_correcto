package edu.upc.dsa.services;


import edu.upc.dsa.MapaManager;
import edu.upc.dsa.MapaManagerImpl;
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

@Api(value = "/usuarios", description = "Endpoint to Track Service")
@Path("/usuarios")
public class UserService {

    private MapaManager tm;

    public UserService() {
        this.tm = MapaManagerImpl.getInstance();
        if (tm.NumberUsers()==0) {
            User user1 = this.tm.AddUser1("Maria","Camarasa","Mary@gmail.com","23/7/89");
            User user2 =this.tm.AddUser1("Jon","Trabolta","Trabo@gmail.com","2/9/90");
            User user3 =this.tm.AddUser1("Monica","Naranjo","MonNa@gmail.com","16/12/02");
            User user4 =this.tm.AddUser1("Roger","Fernandez","RugerFEr@gmail.com","9/3/89");
        }

    }

    // Añadir un nuevo usuario
    @POST
    @ApiOperation(value = "create a new User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User usuari) {
        if (usuari.getNombre()==null || usuari.getApellidos()==null || usuari.getCorreoelec()==null || usuari.getFechanacimiento()==null ){
            return Response.status(500).build();
        }
        else{
            User usuario = this.tm.AddUser1(usuari.getNombre(), usuari.getApellidos(),usuari.getCorreoelec(),usuari.getFechanacimiento());
            return Response.status(201).entity(usuario).build();
        }

    }

    //Obtener toda la lista de users alfabeticamente
    @GET
    @ApiOperation(value = "get all Users ordenados alfabeticamente", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 400, message = "Error")
    })
    @Path("/GetOrderedUsers")
    @Produces(MediaType.APPLICATION_JSON)
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
    @Path("/{id}")
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




}