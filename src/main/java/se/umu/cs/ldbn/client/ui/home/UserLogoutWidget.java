package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public final class UserLogoutWidget extends Composite implements ClickHandler {

	private HorizontalPanel mainPanel;
	private HTML label;
	private Button logout;
	private Button edit;
	
	public UserLogoutWidget() {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setSpacing(5);
		//label;
		label = new HTML();
		
		mainPanel.add(label);
		//logout
		logout = new Button("Logout");
		logout.addClickHandler(this);
		mainPanel.add(logout);
		Common.setCursorPointer(logout);
		//edit
		edit = new Button("Edit");
		edit.addClickHandler(this);
		mainPanel.add(edit);
		//info button
		mainPanel.add(new InfoButton("logout"));
		Common.setCursorPointer(edit);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender == logout) {
			Login.get().sendKillSession(UserData.get().getSession());
		} else if (sender == edit) {
			EditUserDialog.get().center();
		}
	}
	
	protected void onAttach() {
		super.onAttach();
		label.setHTML("Logged in as <b>"+UserData.get().getName()+"</b>");
	}

}
