package se.umu.cs.ldbn.client.ui;

import com.google.gwt.inject.client.AbstractGinModule;
import se.umu.cs.ldbn.client.ui.licence.LicenceWidget;

import javax.inject.Singleton;

public class UiModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(LicenceWidget.class).in(Singleton.class);
    }
}
