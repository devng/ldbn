package se.umu.cs.ldbn.client.rest;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.MethodCallback;
import se.umu.cs.ldbn.shared.dto.CommentDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/api/v1/assignments/{assignmentId}/comments")
public interface CommentsRestClient {

    CommentsRestClient INSTANCE = GWT.create(CommentsRestClient.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    void getAssignmentComments(@PathParam("assignmentId") Integer assignmentId, MethodCallback<List<CommentDto>> callback);
}
