package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.user.client.ui.Widget;

public abstract class CloseDialog extends BaseDialog {

	
	public CloseDialog(String title) {
		this(title, false);
	}
	
	
	public CloseDialog(String title, boolean modal) {
		this(title, null, modal);
	}
	
	public CloseDialog(String title, String msg ,boolean modal) {
		super(title, msg, modal);
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
