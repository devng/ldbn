package se.umu.cs.ldbn.client.rest;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import se.umu.cs.ldbn.shared.dto.UserDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/api/v1/users")
public interface UserRestClient extends RestService {

    UserRestClient INSTANCE = GWT.create(UserRestClient.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    void indexUsers(@QueryParam("activeOnly") boolean activeOnly, MethodCallback<List<UserDto>> callback);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getUser(@PathParam("id") Integer id, MethodCallback<List<UserDto>> callback);
}
