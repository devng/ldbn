package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public final class FDEditorDialog extends DialogBox implements ClickListener {
	private FDEditorWidget fdew;

	public FDEditorDialog() {
		super(false, false);
		setText("FD Editor");
		Button closeButton = new Button("Close", this);
		CommonStyle.setCursorPointer(closeButton);
		HTML msg = new HTML(
				"<center>Create an FD by giving the left and right hand side attributes.</center>", true);

		DockPanel dock = new DockPanel();
		dock.setSpacing(4);

		dock.add(closeButton, DockPanel.SOUTH);
		dock.add(msg, DockPanel.NORTH);
		fdew = new FDEditorWidget();
		dock.add(fdew, DockPanel.CENTER);

		dock.setCellHorizontalAlignment(closeButton, DockPanel.ALIGN_RIGHT);
		dock.setWidth("100%");
		setWidget(dock);
		
	}
	
	public void setCurrentFDHolderPanel(FDHolderPanel fdHP) {
		fdew.setCurrentFDHolderPanel(fdHP);
	}

	public void onClick(Widget sender) {
		hide();
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
		Main.get().getFDEditorWidget().setCurrentFDHolderPanel(null); //TODO Is this needed?
	}
	
	public FDEditorWidget getFDEditorWidget() {
		return fdew;
	}
	
	
}