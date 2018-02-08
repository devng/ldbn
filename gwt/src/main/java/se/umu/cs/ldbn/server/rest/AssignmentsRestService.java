package se.umu.cs.ldbn.server.rest;

import io.swagger.annotations.Api;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/api/v1/assignments")
@Api(value = "Assignment Service")
public interface AssignmentsRestService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    List<AssignmentDto> getAllAssignments(@QueryParam("includeXml") @DefaultValue("false") boolean includeXml);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    AssignmentDto getAssignment(@PathParam("id") Integer id);

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Nonnull
    Response createAssignment(AssignmentDto dto, @Context UriInfo uriInfo);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAssignment(AssignmentDto dto, @PathParam("id") Integer id);
}
