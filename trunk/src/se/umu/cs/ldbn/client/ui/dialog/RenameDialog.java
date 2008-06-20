package se.umu.cs.ldbn.client.ui.dialog;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public final class RenameDialog extends OkCancelDialog {

	public static String REGEX = "([\\w]|\\-){1,20}";
	private static RenameDialog inst;
	
	private RenameDialogCallback caller;
	private TextBox nameBox;
	
	private RenameDialog() {
		super("Enter a name", "Use only <i>word</i> characters.", true);
		caller = null;
	}
	
	public static RenameDialog get() {
		if (inst == null) {
			inst = new RenameDialog();
		}
		return inst;
	}
	
	protected Widget getContentWidget() {
		if (nameBox == null) {
			nameBox = new TextBox();
			nameBox.setWidth("220px");;
		}
		return nameBox;
	}

	protected void onOkClick() {
		String s = nameBox.getText();
		if(s.matches(REGEX)) {
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
	
	protected void onCancelClick() {
		super.onCancelClick();
		if(caller != null) {
			caller.onRenameCanceled();
		}
	}
	
	public void rename(RenameDialogCallback rdc) {
		if(rdc == null) return;
		caller = rdc;
		nameBox.setText(rdc.getOldName());
		center();
	}
}
