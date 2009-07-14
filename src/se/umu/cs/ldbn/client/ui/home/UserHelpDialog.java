package se.umu.cs.ldbn.client.ui.home;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import se.umu.cs.ldbn.client.io.UserHelpSender;
import se.umu.cs.ldbn.client.ui.dialog.OkCancelDialog;
import se.umu.cs.ldbn.client.utils.Common;

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
	
	protected Widget getDialogContentWidget() {
		mainPanel = new VerticalPanel();
		forgotPassword = new RadioButton("group0", "I forgot my password. Send me a new one.");
		resendEmail = new RadioButton("group0", "Resend my activation email.");
		emaiTB = new TextBox();
		label = new HTML("<P ALIGN=LEFT>This dialog will help you in case: <BR>" +
				"1. You have NOT yet received an activation email.<BR> " +
				"2. You forgot your password.<P>");
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
