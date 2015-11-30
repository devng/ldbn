package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public final class UserLoginWidget extends Composite implements ClickHandler,
	KeyPressHandler {

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
		userTB.addKeyPressHandler(this);
		userLabel = new Label("User: ");
		mainPanel.add(userLabel);
		mainPanel.add(userTB);
		//pass
		passTB = new PasswordTextBox();
		passTB.addKeyPressHandler(this);
		passLabel = new Label("Password: ");
		mainPanel.add(passLabel);
		mainPanel.add(passTB);
		//login
		login = new Button("Login");
		login.addClickHandler(this);
		mainPanel.add(login);
		Common.setCursorPointer(login);
		//register
		register = new Button("Register");
		register.addClickHandler(this);
		mainPanel.add(register);
		Common.setCursorPointer(register);
		//help
		help = new Button("Help");
		help.addClickHandler(this);
		mainPanel.add(help);
		//info button
		mainPanel.add(new InfoButton("login"));
		Common.setCursorPointer(help);
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
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
		String pass = Common.base64encode(passTB.getText().trim());
		UserData.get().setName(username);
		UserData.get().setPass(pass);
		Login.get().sendLogin(username, pass);
	}

	public void clear() {
		userTB.setText("");
		passTB.setText("");
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		char keyCode = event.getCharCode();
		if(keyCode == KeyCodes.KEY_ENTER) {
			doLogin();
		}
	}
}
