package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public final class HelpDialog extends CloseDialog {

	public HelpDialog() {
		super("Help Dialog", "This is an example of help dialog.", false);
		
	}
	
	protected Widget getContentWidget() {
		//TODO
		return new Frame("http://www.google.com");
	}
}