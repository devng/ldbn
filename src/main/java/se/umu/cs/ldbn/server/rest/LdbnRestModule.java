package se.umu.cs.ldbn.server.rest;


import com.google.inject.AbstractModule;

public class LdbnRestModule extends AbstractModule {

    @Override
    protected void configure() {
    	bind(LdbnRestExceptionMapper.class);
        bind(LdbnRestService.class).to(LdbnRestServiceImpl.class);
    }
}
