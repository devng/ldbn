package se.umu.cs.ldbn.client.ui.user;

import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.io.Login;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public final class UserLoginWidget extends Composite implements ClickListener {
	
	private HorizontalPanel mainPanel;
	//user
	private TextBox userTB;
	private Label userLabel;
	//pass
	private PasswordTextBox passTB;
	private Label passLabel;
	// login
	private Button login;
	// register
	private Button register;
	//help
	private Button help;
	
	public UserLoginWidget() {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setSpacing(5);
		
		//user
		userTB = new TextBox();
		userLabel = new Label("User: ");
		mainPanel.add(userLabel);
		mainPanel.add(userTB);
		//pass
		passTB = new PasswordTextBox();
		passLabel = new Label("Password: ");
		mainPanel.add(passLabel);
		mainPanel.add(passTB);
		//login
		login = new Button("Login");
		login.addClickListener(this);
		mainPanel.add(login);
		Common.setCursorPointer(login);
		//register
		register = new Button("Register");
		register.addClickListener(this);
		mainPanel.add(register);
		Common.setCursorPointer(register);
		//help
		help = new Button("Help");
		help.addClickListener(this);
		mainPanel.add(help);
		Common.setCursorPointer(help);
	}
	
	public void onClick(Widget sender) {
		if(sender == login) {
			String username = userTB.getText().trim();
			if(username.matches(Common.NAME_REGEX)) {
				
			} else {
				
			}
			String pass = Common.md5(passTB.getText().trim());
			UserData.get().setName(username);
			UserData.get().setPass(pass);
			Login.get().sendLogin(username, pass);
			
		} else if (sender == register) {
			RegisterUserDialog.get().center();
		} else if (sender == help) {
			UserHelpDialog.get().center();
		}
	}
	
	public void clear() {
		userTB.setText("");
		passTB.setText("");
	}
}
