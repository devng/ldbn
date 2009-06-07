package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class OkCancelDialog extends BaseDialog {
	
	protected Label errorLabel;
	
	public OkCancelDialog(String title) {
		this(title, false);
	}
	
	public OkCancelDialog(String title, boolean modal) {
		super(title,  modal);
		//setText(title);
		closeButton.setText("Cancel");
	}
	
	
	public void onClick(Widget sender) {
		if(sender == closeButton) {
			onCancelClick();
		} else if (sender == okButton) {
			onOkClick();
		}
	}
	
	protected void onCancelClick() {
		hide();
	}
	
	protected abstract void onOkClick();
}
