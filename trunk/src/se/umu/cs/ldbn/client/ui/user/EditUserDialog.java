package se.umu.cs.ldbn.client.ui.user;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.UserManagement;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.Window;

public final class EditUserDialog extends RegisterUserDialog {
	
	private static EditUserDialog inst_;
	
	public static EditUserDialog get() {
		if(inst_ == null) {
			inst_ = new EditUserDialog();
		}
		return inst_;
	}
	
	private EditUserDialog() {
		super("Change Account");
		//setText("Change Account");
		checkPassLenght = false;
		useMD5 = false;
	}
	
	protected void oKAction(String userName, String pass, String email) {
		if(!pass.equals("")) {
			if(pass.length() < Common.MIN_PASSWORD_LENGTH) {
				setErrorMsg("Password is too short.");
				return;
			}
			pass = Common.md5(pass);
		} else {
			pass = null;
		}
		if(email.equals(UserData.get().getEmail())) {
			email = null;
		}
		if(userName.equals(UserData.get().getName())) {
			userName = null;
		}
		UserManagement.get().sendUserChange(userName, pass, email);
	}

	public void center() {
		super.center();
		updateTextBoxes();
	}
	
	private void updateTextBoxes() {
		UserData ud = UserData.get();
		userTB.setText(ud.getName());
		emailTB.setText(ud.getEmail());
		passTB1.setText("");
		passTB2.setText("");
	}
	
	public void setOKStatus() {
		super.setOKStatus();
		if(UserData.get().isLoggedIn()) {
			hide();
			Window.alert("You have to login again.");
			Login.get().sendKillSession(UserData.get().getSession());
		}
	}
}
