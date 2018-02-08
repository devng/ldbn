package se.umu.cs.ldbn.server.rest;

import io.swagger.annotations.Api;
import se.umu.cs.ldbn.shared.dto.CommentDto;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/assignments/{assignmentId}/comments")
@Api(value = "Assignment Comments Service")
public interface CommentsRestService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    List<CommentDto> getAssignmentComments(@PathParam("assignmentId") Integer assignmentId);

}
