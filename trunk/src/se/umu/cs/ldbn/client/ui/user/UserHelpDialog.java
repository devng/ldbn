package se.umu.cs.ldbn.client.ui.user;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import se.umu.cs.ldbn.client.io.UserHelpSender;
import se.umu.cs.ldbn.client.ui.dialog.OkCancelDialog;
import se.umu.cs.ldbn.client.util.Common;

public class UserHelpDialog extends OkCancelDialog{
	
	private static UserHelpDialog inst;
	
	public static UserHelpDialog get() {
		if (inst == null) {
			inst = new UserHelpDialog();
		}
		return inst;
	}
	
	private VerticalPanel mainPanel;
	private RadioButton forgotPassword;
	private RadioButton resendEmail;
	private TextBox emaiTB;
	private HTML label;

	private UserHelpDialog() {
		super("User Account Help", true);
	}
	
	protected Widget getContentWidget() {
		mainPanel = new VerticalPanel();
		forgotPassword = new RadioButton("group0", "I forgot my password.");
		resendEmail = new RadioButton("group0", "Resend my activation email.");
		emaiTB = new TextBox();
		label = new HTML("<center>This help dialog will help You in case <BR>" +
				"You have not yet received an activation email, or<BR> " +
				"You forgot Your password.<center><BR>");
		mainPanel.add(label);
		mainPanel.add(resendEmail);
		mainPanel.add(forgotPassword);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Label("Your email: "));
		hp.setSpacing(4);
		hp.add(emaiTB);
		mainPanel.add(hp);
		return mainPanel;
	}

	protected void onOkClick() {
		setErrorMsg("");
		String email = emaiTB.getText().trim().toLowerCase();
		if(!email.matches(Common.EMAIL_REGEX)) {
			setErrorMsg("Invalid email.");
			return;
		}
		if(forgotPassword.isChecked()) {
			UserHelpSender.get().sendNewPassword(email);
		} else {
			UserHelpSender.get().resendActivationEmail(email);
		}
	}

}
