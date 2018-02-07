package se.umu.cs.ldbn.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import se.umu.cs.ldbn.client.i18n.I18NConstants;
import se.umu.cs.ldbn.client.i18n.I18NMessages;
import se.umu.cs.ldbn.client.model.SolveAssignmentModel;
import se.umu.cs.ldbn.client.model.UserModel;
import se.umu.cs.ldbn.client.rest.AssignmentsRestClient;
import se.umu.cs.ldbn.client.rest.CommentsRestClient;
import se.umu.cs.ldbn.client.rest.UsersRestClient;
import se.umu.cs.ldbn.client.ui.licence.LicenceWidget;

@GinModules(ClientMainModule.class)
public interface ClientInjector extends Ginjector {

    ClientInjector INSTANCE = GWT.create(ClientInjector.class);

    EventBus getEventBus();

    AssignmentsRestClient getAssignmentsRestClient();

    CommentsRestClient getCommentsRestClient();

    UsersRestClient getUsersRestClient();

    I18NConstants getI18NConstants();

    I18NMessages getI18NMessages();

    LicenceWidget getLicenceWidget();

    SolveAssignmentModel getSolveAssignmentModel();

    UserModel getUserModel();
}
