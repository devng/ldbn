package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.user.client.ui.Widget;

public abstract class CloseDialog extends BaseDialog {

	
	public CloseDialog(String title) {
		this(title, false);
	}
	
	public CloseDialog(String title, boolean modal) {
		super(title, modal);
		okButton.removeClickListener(this);
		okButton.removeFromParent();
	}


	
	public void onClick(Widget sender) {
		if (sender == closeButton) {
			onCloseClick();
		}
		
	}
	
	protected void onCloseClick() {
		hide();
	}

}
