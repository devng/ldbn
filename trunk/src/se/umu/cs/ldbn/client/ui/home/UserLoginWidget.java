package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public final class UserLoginWidget extends Composite implements ClickListener,
	KeyboardListener {
	
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
		userTB.addKeyboardListener(this);
		userLabel = new Label("User: ");
		mainPanel.add(userLabel);
		mainPanel.add(userTB);
		//pass
		passTB = new PasswordTextBox();
		passTB.addKeyboardListener(this);
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
		//info button
		mainPanel.add(new InfoButton("login"));
		Common.setCursorPointer(help);
	}
	
	public void onClick(Widget sender) {
		if(sender == login) {
			doLogin();
		} else if (sender == register) {
			RegisterUserDialog.get().center();
		} else if (sender == help) {
			UserHelpDialog.get().center();
		}
	}
	
	private void doLogin() {
		String username = userTB.getText().trim();
		if(username.matches(Common.NAME_REGEX)) {
			
		} else {
			
		}
		String pass = Common.md5(passTB.getText().trim());
		UserData.get().setName(username);
		UserData.get().setPass(pass);
		Login.get().sendLogin(username, pass);
	}
	
	public void clear() {
		userTB.setText("");
		passTB.setText("");
	}
	
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		if (keyCode == KeyboardListener.KEY_ENTER) {
			doLogin();
		}
	}
	
	public void onKeyUp(Widget sender, char keyCode, int modifiers) {}
	
	public void onKeyDown(Widget sender, char keyCode, int modifiers) {}
}
