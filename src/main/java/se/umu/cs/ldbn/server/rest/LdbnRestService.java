package se.umu.cs.ldbn.server.rest;


import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;
import se.umu.cs.ldbn.shared.dto.HelloDto;

@Path("/")
public interface LdbnRestService {


    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    HelloDto sayHello();
    
    @GET
    @Path("/assignments")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
	List<AssignmentDto> getAllAssignments();
    
    @GET
    @Path("/assignments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    AssignmentDto getAssignment(@PathParam("id") Integer id);
}
