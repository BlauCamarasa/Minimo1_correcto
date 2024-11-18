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

@Api(value = "/PuntoInteres", description = "Endpoint to PuntoInteres Service")
@Path("/PuntoInteres")
public class PuntoInteresService {
    private MapaManager tm;
    public PuntoInteresService() {
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
    // Añadir un nuevo punto de interes
    @POST
    @ApiOperation(value = "Crear un nuevo punto de interes en el mapa", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= PuntoInteres.class),
            @ApiResponse(code = 409, message = "Incorrect type"),
            @ApiResponse(code = 500, message = "Error")

    })
    @Path("/PostNewPuntoInteres/{Horiz}/{Vert}/{Tipo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newPunto(@PathParam("Horiz") String horiz, @PathParam("Vert") String vert, @PathParam("Tipo") String tipo ) {

        if (tipo==null){
            return Response.status(500).build();
        }
        else{
            PuntoInteres puntoInteres = this.tm.addPuntoInteres(Double.parseDouble(horiz), Double.parseDouble(vert), tipo);
            if (puntoInteres==null){
                return Response.status(409).build();
            }
            else{
                return Response.status(201).entity(puntoInteres).build();
            }

        }

    }

    //Consultar los puntos de interes por los que un usuario ha pasado en el orden registrado
    @GET
    @ApiOperation(value = "Get de todos los puntos de interes por los que un usuario ha pasado en el orden registrado", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = PuntoInteres.class, responseContainer="List"),
            @ApiResponse(code = 400, message = "Error"),
            @ApiResponse(code = 406, message = "La lista de puntos de interes del usuario esta vacia")
    })
    @Path("/GetPuntosInteresUsuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPuntosInteresDeUser(@PathParam("id") String identificador) {
        try{
            List<PuntoInteres> ptsinteresuser = this.tm.consultarPuntosIntUsuario(identificador);
            if(ptsinteresuser.isEmpty()){
                return Response.status(404).build();
            }
            else{
                GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(ptsinteresuser) {};
                return Response.status(201).entity(entity).build();
            }
        }
        catch (Exception e){
            return Response.status(400).build();
        }
    }


    //Obtener lista de puntos de interes de x tipo
    @GET
    @ApiOperation(value = "Obtener lista de puntos de interes de x tipo", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = PuntoInteres.class, responseContainer="List"),
            @ApiResponse(code = 400, message = "Error")
    })
    @Path("/GetListaSegunTipo/{tipo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getpuntosinteres(@PathParam("tipo") String tipo) {
        try{
            List<PuntoInteres> puntoInteresX = this.tm.consultarPuntosPorTipoString(tipo);
            if (puntoInteresX.isEmpty()){
                return Response.status(400).build();
            }
            else{
                GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(puntoInteresX) {};
                return Response.status(201).entity(entity).build();
            }

        }
        catch (Exception e){
            return Response.status(400).build();
        }

    }
}
