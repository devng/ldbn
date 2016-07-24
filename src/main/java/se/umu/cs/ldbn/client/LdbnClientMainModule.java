package se.umu.cs.ldbn.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import se.umu.cs.ldbn.client.rest.AssignmentsRestClient;

import javax.inject.Singleton;

public class LdbnClientMainModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(AssignmentsRestClient.class).in(Singleton.class);
    }
}
