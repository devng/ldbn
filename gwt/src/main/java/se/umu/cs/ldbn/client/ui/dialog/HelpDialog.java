package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public final class HelpDialog extends CloseDialog {

	private static HelpDialog inst;
	
	public static HelpDialog get() {
		if (inst == null) {
			inst = new HelpDialog();
		}
		return inst;
	}
	
	private Frame viewer;
	
	private HelpDialog() {
		super("Help Dialog", false);
		DOM.setStyleAttribute(viewer.getElement(), "background", "white");
	}
	
	/**
	 * Only a file name not the URL. Files are to be located in the info
	 * folder.
	 * @param fileName
	 */
	public void showInfo(String fileName) {
		viewer.setUrl(Common.getResourceUrl("info/" + fileName));
		center();
	}
	
	protected Widget getDialogContentWidget() {
		viewer = new Frame();
		viewer.setSize("420px", "250px");
		return viewer;
	}
}