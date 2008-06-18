package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class OkCancelDialog extends BaseDialog {
	
	protected Label errorLabel;
	
	public OkCancelDialog(String title) {
		this(title, false);
	}
	
	
	public OkCancelDialog(String title, boolean modal) {
		this(title, null, modal);
	}
	
	public OkCancelDialog(String title, String msg ,boolean modal) {
		super(title, msg, modal);
		setText(title);
		closeButton.setText("Cancel");
	}
	
	
	protected abstract Widget getContentWidget();
	
	protected abstract void onOkClick();
	
	protected void onCancelClick() {
		hide();
	}
	
	public void onClick(Widget sender) {
		if(sender == closeButton) {
			onCancelClick();
		} else if (sender == okButton) {
			onOkClick();
		}
	}
}
