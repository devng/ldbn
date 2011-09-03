package se.umu.cs.ldbn.client.ui.dialog;

import java.util.Collection;

import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.utils.Common;

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
		super(I18N.constants().renameDialogTitle(), true);
		caller = null;
	}
	
	public void rename(RenameDialogCallback rdc) {
		if(rdc == null) return;
		caller = rdc;
		nameBox.setText(rdc.getOldName());
		center();
	}

	protected Widget getDialogContentWidget() {
		if (nameBox == null) {
			nameBox = new TextBox();
			nameBox.setWidth("220px");;
		}
		return nameBox;
	}
	
	@Override
	protected void onCancelClick() {
		super.onCancelClick();
		if(caller != null) {
			caller.onRenameCanceled();
		}
	}
	
	@SuppressWarnings("rawtypes")
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
							setErrorMsg(I18N.constants().renameDialogNameTaken());
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
			setErrorMsg(I18N.constants().invalidName());
		}
	}
	
	@Override
	protected String getHelpMessage() {
		return I18N.constants().renameDialogSubTitle();
	}
}
