package se.umu.cs.ldbn.server.rest;

import io.swagger.annotations.Api;
import se.umu.cs.ldbn.shared.dto.UserDto;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/users")
@Api(value = "User Service")
public interface UserRestService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    List<UserDto> indexUsers(@QueryParam("activeOnly") @DefaultValue("false") boolean activeOnly);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    UserDto getUser(@PathParam("id") Integer id);

}
