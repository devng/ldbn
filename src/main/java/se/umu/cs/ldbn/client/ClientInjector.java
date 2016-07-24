package se.umu.cs.ldbn.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import se.umu.cs.ldbn.client.rest.AssignmentsRestClient;

@GinModules(LdbnClientMainModule.class)
public interface ClientInjector extends Ginjector {

    ClientInjector INSTANCE = GWT.create(ClientInjector.class);

    EventBus getEventBus();

    AssignmentsRestClient getAssignmentsRestClient();


}
