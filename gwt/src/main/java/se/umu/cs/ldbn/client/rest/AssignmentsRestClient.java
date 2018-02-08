package se.umu.cs.ldbn.client.rest;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Attribute;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/api/v1/assignments")
public interface AssignmentsRestClient extends RestService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    void indexAssignments(MethodCallback<List<AssignmentDto>> callback);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getAssignment(@PathParam("id") Integer id, MethodCallback<AssignmentDto> callback);

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    void createAssignment(AssignmentDto dto, MethodCallback<Void> callback);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAssignment(@PathParam("id") Integer id, AssignmentDto dto, MethodCallback<Void> callback);
}
