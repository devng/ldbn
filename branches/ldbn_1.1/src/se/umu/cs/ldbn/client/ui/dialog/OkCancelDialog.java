package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;

public abstract class OkCancelDialog extends BaseDialog {
	
	protected Label errorLabel;
	
	public OkCancelDialog(String title) {
		this(title, false);
	}
	
	public OkCancelDialog(String title, boolean modal) {
		super(title,  modal);
		closeButton.setText("Cancel");
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
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
