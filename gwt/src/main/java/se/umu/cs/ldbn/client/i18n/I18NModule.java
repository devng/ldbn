package se.umu.cs.ldbn.client.i18n;

import com.google.gwt.inject.client.AbstractGinModule;

import javax.inject.Singleton;

public class I18NModule extends AbstractGinModule {

    @Override
        protected void configure() {
        bind(I18NConstants.class).in(Singleton.class);
        bind(I18NMessages.class).in(Singleton.class);
    }
}
