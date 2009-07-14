package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class UserLogoutWidget extends Composite implements ClickListener {

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
		logout.addClickListener(this);
		mainPanel.add(logout);
		Common.setCursorPointer(logout);
		//edit
		edit = new Button("Edit");
		edit.addClickListener(this);
		mainPanel.add(edit);
		//info button
		mainPanel.add(new InfoButton("logout"));
		Common.setCursorPointer(edit);
	}
	
	public void onClick(Widget sender) {
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
