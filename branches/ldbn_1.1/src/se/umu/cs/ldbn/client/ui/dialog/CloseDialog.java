package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.event.dom.client.ClickEvent;

public abstract class CloseDialog extends BaseDialog {

	
	public CloseDialog(String title) {
		this(title, false);
	}
	
	public CloseDialog(String title, boolean modal) {
		super(title, modal);
		//TODO
		//okButton.removeClickListener(this);
		okButton.removeFromParent();
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender == closeButton) {
			onCloseClick();
		}
		
	}
	
	protected void onCloseClick() {
		hide();
	}

}
