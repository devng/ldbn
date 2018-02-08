package se.umu.cs.ldbn.client.rest;

import com.google.gwt.inject.client.AbstractGinModule;

import javax.inject.Singleton;

public class RestModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(AssignmentsRestClient.class).in(Singleton.class);
        bind(CommentsRestClient.class).in(Singleton.class);
        bind(UsersRestClient.class).in(Singleton.class);
    }
}
