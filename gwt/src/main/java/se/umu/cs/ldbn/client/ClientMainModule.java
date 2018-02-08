package se.umu.cs.ldbn.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import se.umu.cs.ldbn.client.i18n.I18NModule;
import se.umu.cs.ldbn.client.model.ClientModelModule;
import se.umu.cs.ldbn.client.rest.RestModule;
import se.umu.cs.ldbn.client.ui.UiModule;

import javax.inject.Singleton;

public class ClientMainModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

        install(new RestModule());
        install(new ClientModelModule());
        install(new I18NModule());
        install(new UiModule());
    }
}
