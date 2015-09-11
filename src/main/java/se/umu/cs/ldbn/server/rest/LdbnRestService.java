package se.umu.cs.ldbn.server.rest;


import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.umu.cs.ldbn.shared.dto.HelloDto;

public interface LdbnRestService {


    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    @Nonnull
    HelloDto sayHello();
}
