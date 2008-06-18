package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.ui.FDEditorWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.LdbnDragCotroller;

import com.google.gwt.user.client.ui.Widget;

public final class FDEditorDialog extends CloseDialog {
	private FDEditorWidget fdew;

	private static FDEditorDialog inst;
	
	private FDEditorDialog() {
		super("FD Editor","Create an FD by giving the left and right hand side attributes.", false);
	}
	
	public static FDEditorDialog get() {
		if (inst == null) {
			inst = new FDEditorDialog();
		}
		return inst;
	}
	
	public void setCurrentFDHolderPanel(FDHolderPanel fdHP) {
		fdew.setCurrentFDHolderPanel(fdHP);
	}
	
	@Override
	public void show() {
		super.show();
		LdbnDragCotroller.get().registerDropController(fdew.getRightTextArea());
		LdbnDragCotroller.get().registerDropController(fdew.getLeftTextArea());
	}
	
	@Override
	public void hide() {
		super.hide();
		fdew.clearText();

		LdbnDragCotroller.get().unregisterDropController(fdew.getRightTextArea());
		LdbnDragCotroller.get().unregisterDropController(fdew.getLeftTextArea());
		fdew.setCurrentFDHolderPanel(null); //TODO Is this needed?
	}
	
	public FDEditorWidget getFDEditorWidget() {
		return fdew;
	}

	protected Widget getContentWidget() {
		if (fdew == null)
			fdew = new FDEditorWidget();
		return fdew;
	}
}