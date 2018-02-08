package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.UserManagement;
import se.umu.cs.ldbn.client.model.UserModel;
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
		useBase64 = false;
	}

	protected void oKAction(String userName, String pass, String email) {
		if(!pass.equals("")) {
			if(pass.length() < Common.MIN_PASSWORD_LENGTH) {
				setErrorMsg("Password is too short.");
				return;
			}
			pass = Common.base64encode(pass);
		} else {
			pass = null;
		}
		if(email.equals(UserModel.get().getEmail())) {
			email = null;
		}
		if(userName.equals(UserModel.get().getName())) {
			userName = null;
		}
		UserManagement.get().sendUserChange(userName, pass, email);
	}

	public void center() {
		super.center();
		updateTextBoxes();
	}

	private void updateTextBoxes() {
		UserModel ud = UserModel.get();
		userTB.setText(ud.getName());
		emailTB.setText(ud.getEmail());
		passTB1.setText("");
		passTB2.setText("");
	}

	public void setOKStatus() {
		super.setOKStatus();
		if(UserModel.get().isLoggedIn()) {
			hide();
			Window.alert("You have to login again.");
			Login.get().sendKillSession(UserModel.get().getSession());
		}
	}
}
