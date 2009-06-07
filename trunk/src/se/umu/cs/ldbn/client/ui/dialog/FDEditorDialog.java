package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;

import com.google.gwt.user.client.ui.Widget;

public final class FDEditorDialog extends CloseDialog {
	
	private static FDEditorDialog inst;

	public static FDEditorDialog get() {
		if (inst == null) {
			inst = new FDEditorDialog();
		}
		return inst;
	}
	
	private FDEditorWidget fdew;
	
	private FDEditorDialog() {
		super("FD Editor", false);
	}
	
	public FDEditorWidget getFDEditorWidget() {
		return fdew;
	}
	
	public void hide() {
		super.hide();
		fdew.clearText();
		
		Main.get().getDragController().unregisterDropController(fdew.getRightTextArea());
		Main.get().getDragController().unregisterDropController(fdew.getLeftTextArea());
		fdew.handleCurrentFDWidget();
		fdew.setCurrentFDHolderPanel(null); //TODO Is this needed?
		fdew.setCurrentDomain(null);
	}
	
	public void setCurrentDomain(DomainTable domain) {
		fdew.setCurrentDomain(domain);
	}
	
	public void setCurrentFDHolderPanel(FDHolderPanel fdHP) {
		fdew.setCurrentFDHolderPanel(fdHP);
	}
	
	public void show() {
		super.show();
		Main.get().getDragController().registerDropController(fdew.getRightTextArea());
		Main.get().getDragController().registerDropController(fdew.getLeftTextArea());
	}

	protected Widget getDialogContentWidget() {
		if (fdew == null)
			fdew = new FDEditorWidget();
		return fdew;
	}
	
	protected String getHelpMessage() {
		return "Create a FD by giving the left-hand and right-hand side " +
		"attributes.<BR>You can use Drag'n'Drop.";
	}
}