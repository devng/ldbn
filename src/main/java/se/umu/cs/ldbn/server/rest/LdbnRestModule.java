package se.umu.cs.ldbn.server.rest;

import com.google.inject.AbstractModule;

public class LdbnRestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapperProvider.class);
        bind(RestExceptionMapper.class);
        bind(AssignmentsRestService.class).to(AssignmentsRestServiceImpl.class);
        bind(CommentsRestService.class).to(CommentsRestServiceImpl.class);
        bind(UserRestService.class).to(UserRestServiceImpl.class);
    }
}
