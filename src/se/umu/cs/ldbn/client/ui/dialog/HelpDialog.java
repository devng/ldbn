package se.umu.cs.ldbn.client.ui.dialog;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public final class HelpDialog extends CloseDialog {

	private static HelpDialog inst;
	private Frame viewer;
	
	private HelpDialog() {
		super("Help Dialog", "", false);
		DOM.setStyleAttribute(viewer.getElement(), "background", "white");
	}
	
	public static HelpDialog get() {
		if (inst == null) {
			inst = new HelpDialog();
		}
		return inst;
	}
	
	protected Widget getContentWidget() {
		viewer = new Frame();
		viewer.setSize("400px", "250px");
		return viewer;
	}
	
	/**
	 * Only a file name not the URL. Files are to be located in the info
	 * folder.
	 * @param fileName
	 */
	public void showInfo(String fileName) {
		viewer.setUrl("info/"+fileName);
		center();
	}
}