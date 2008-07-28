package se.umu.cs.ldbn.client.ui.dialog;

import java.util.Collection;

import se.umu.cs.ldbn.client.util.Common;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public final class RenameDialog extends OkCancelDialog {

	private static RenameDialog inst;
	
	public static RenameDialog get() {
		if (inst == null) {
			inst = new RenameDialog();
		}
		return inst;
	}
	private RenameDialogCallback caller;
	
	private TextBox nameBox;
	
	private RenameDialog() {
		super("Enter a name", "Use only <i>word</i> characters.<BR>Maximal 20 characters.", true);
		caller = null;
	}
	
	public void rename(RenameDialogCallback rdc) {
		if(rdc == null) return;
		caller = rdc;
		nameBox.setText(rdc.getOldName());
		center();
	}

	protected Widget getContentWidget() {
		if (nameBox == null) {
			nameBox = new TextBox();
			nameBox.setWidth("220px");;
		}
		return nameBox;
	}
	
	protected void onCancelClick() {
		super.onCancelClick();
		if(caller != null) {
			caller.onRenameCanceled();
		}
	}
	
	protected void onOkClick() {
		String s = nameBox.getText();
		if(s.matches(Common.NAME_REGEX)) {
			Collection names = caller.getTakenNames();
			if(names != null) {
				String sLow = s.toLowerCase();
				for (Object obj : names) {
					if (obj instanceof HasName) {
						String name = ((HasName) obj).getName();
						if(name.toLowerCase().equals(sLow)) {
							setErrorMsg("Name is taken.");
							return;
						}
					}
				}
			}
			caller.setNewName(s);
			caller = null;
			nameBox.setText("");
			setErrorMsg("");
			hide();
		} else {
			setErrorMsg("Invalid name.");
		}
	}
}
