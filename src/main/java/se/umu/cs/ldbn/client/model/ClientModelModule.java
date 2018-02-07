package se.umu.cs.ldbn.client.model;

import com.google.gwt.inject.client.AbstractGinModule;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;

import javax.inject.Singleton;

public class ClientModelModule extends AbstractGinModule {
	@Override
	protected void configure() {
		bind(UserModel.class).in(Singleton.class);
		bind(SolveAssignmentWidget.class).in(Singleton.class);
	}
}
