package se.umu.cs.ldbn.client.ui.user;

import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.io.UserManagement;
import se.umu.cs.ldbn.client.io.UserManagementListener;
import se.umu.cs.ldbn.client.ui.dialog.OkCancelDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RegisterUserDialog extends OkCancelDialog implements UserManagementListener {

	private static RegisterUserDialog inst;
	
	public static RegisterUserDialog get() {
		if(inst == null) {
			inst = new RegisterUserDialog();
		}
		return inst;
	}
	
	protected Grid mainGrid;
	protected TextBox userTB;
	protected PasswordTextBox passTB1;
	protected PasswordTextBox passTB2;
	protected TextBox emailTB;
	protected boolean checkPassLenght;
	protected boolean useMD5;
	
	
	protected RegisterUserDialog() {
		super("New User", true);
		checkPassLenght = true;
		useMD5 = true;
		UserManagement.get().addListener(this);
	}
	
	protected Widget getContentWidget() {
		mainGrid = new Grid(4, 2);
		mainGrid.setWidget(0, 0, new Label("Username: "));
		mainGrid.setWidget(0, 1, userTB = new TextBox());
		mainGrid.setWidget(1, 0, new Label("Pasword: "));
		mainGrid.setWidget(1, 1, passTB1 = new PasswordTextBox());
		mainGrid.setWidget(2, 0, new Label("Re-type Password: "));
		mainGrid.setWidget(2, 1, passTB2 = new PasswordTextBox());
		mainGrid.setWidget(3, 0, new Label("Email: "));
		mainGrid.setWidget(3, 1, emailTB = new TextBox());
		return mainGrid;
	}
	
	protected void onOkClick() {
		setErrorMsg("");
		//username
		String userName =  userTB.getText().trim().toLowerCase();
		if(!userName.matches(Common.NAME_REGEX)) {
			setErrorMsg("Invalid username.");
			return;
		}
		//password
		if(!passTB1.getText().trim().equals(passTB2.getText().trim())) {
			setErrorMsg("Passwords do not match.");
			return;
		}
		
		String pass = passTB1.getText().trim();
		if(checkPassLenght) {
			if(pass.length() < Common.MIN_PASSWORD_LENGTH) {
				setErrorMsg("Password is too short.");
				return;
			}
		}
		if(useMD5) {
			pass = Common.md5(pass);
		}
		
		//email
		String email = emailTB.getText().trim().toLowerCase();
		if(!email.matches(Common.EMAIL_REGEX)) {
			setErrorMsg("Invalid email.");
			return;
		}
		oKAction(userName, pass, email);
	}
	
	protected void oKAction(String userName, String pass, String email) {
		UserManagement.get().sendUserRegistration(userName, pass, email);
	}
	
	public void setOKStatus() {
		userTB.setText("");
		passTB1.setText("");
		passTB2.setText("");
		emailTB.setText("");
		hide();
	}
}
