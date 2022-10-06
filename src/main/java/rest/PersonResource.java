package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDto;
import dtos.PhoneDto;
import entities.Person;
import facades.PersonFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/person")
public class PersonResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE = PersonFacade.getInstance(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("{id}")
    @GET
    @Produces("text/plain")
    public Response getPersonById(@PathParam("id") int id) {
        return Response.ok().entity(GSON.toJson(FACADE.getById(id))).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response update(@PathParam("id") int id, String jsonInput) {
        PersonDto person = GSON.fromJson(jsonInput, PersonDto.class);
        person.setId(id);
        person = FACADE.update(person);
        return Response.ok().entity(GSON.toJson(person)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String jsonInput) {
        PersonDto personDto = GSON.fromJson(jsonInput, PersonDto.class);
        personDto = FACADE.create(personDto);
        return Response.ok().entity(GSON.toJson(personDto)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) {
        PersonDto deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @GET

    @Path("hobby/{hobby}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByHobby(@PathParam("hobby") String hobbyName) {
        return Response.ok().entity(GSON.toJson(FACADE.getAllPersonsWithHobby(hobbyName))).build();
    }

    @GET
    @Path("hobby/amount/{hobby}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByHobbyAmount(@PathParam("hobby") String hobbyName) {
        return Response.ok().entity(GSON.toJson(FACADE.getAmountOfPersonsWithHobby(hobbyName))).build();
    }

    @GET
    @Path("city/{zip}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByCity(@PathParam("zip") String zip) {
        return Response.ok().entity(GSON.toJson(FACADE.getAllPersonsInCity(zip))).build();

    }


    @GET
    @Path("/{phone}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByPhone (@PathParam("phone") Integer phone)
    {
        PersonDto p = FACADE.getByPhone(phone);
        return Response.ok().entity(GSON.toJson(p)).build();
    }
    }


