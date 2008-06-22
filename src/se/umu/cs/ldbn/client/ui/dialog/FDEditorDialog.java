package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.ui.FDEditorWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;

import com.google.gwt.user.client.ui.Widget;

public final class FDEditorDialog extends CloseDialog {
	private FDEditorWidget fdew;

	private static FDEditorDialog inst;
	
	private FDEditorDialog() {
		super("FD Editor","Create an FD by giving the left and right hand side " +
				"attributes.<BR>You can use Drag'n'Drop.", false);
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
	
	public void setCurrentDomain(DomainTable domain) {
		fdew.setCurrentDomain(domain);
	}
	
	@Override
	public void show() {
		super.show();
		Main.get().getDragController().registerDropController(fdew.getRightTextArea());
		Main.get().getDragController().registerDropController(fdew.getLeftTextArea());
	}
	
	@Override
	public void hide() {
		super.hide();
		fdew.clearText();

		Main.get().getDragController().unregisterDropController(fdew.getRightTextArea());
		Main.get().getDragController().unregisterDropController(fdew.getLeftTextArea());
		fdew.setCurrentFDHolderPanel(null); //TODO Is this needed?
		fdew.setCurrentDomain(null);
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