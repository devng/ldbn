package se.umu.cs.ldbn.server;

import se.umu.cs.ldbn.server.rest.LdbnRestModule;

import com.squarespace.jersey2.guice.JerseyModule;

public class LdbnMainModule extends JerseyModule {

    @Override
    protected void configure() {
        install(new LdbnRestModule());
    }
}
