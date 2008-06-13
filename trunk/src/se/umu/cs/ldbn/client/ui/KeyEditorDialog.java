package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class KeyEditorDialog  extends DialogBox implements ClickListener {
	private KeyEditorWidget kew;

	public KeyEditorDialog() {
		super(false, false);
		setText("Key Editor");
		Button closeButton = new Button("Close", this);
		CommonFunctions.setCursorPointer(closeButton);
		HTML msg = new HTML(
				"<center>Give a valid candidate key for the relation.</center>", true);

		DockPanel dock = new DockPanel();
		dock.setSpacing(4);

		dock.add(closeButton, DockPanel.SOUTH);
		dock.add(msg, DockPanel.NORTH);
		kew = new KeyEditorWidget();
		dock.add(kew, DockPanel.CENTER);

		dock.setCellHorizontalAlignment(closeButton, DockPanel.ALIGN_RIGHT);
		dock.setWidth("100%");
		setWidget(dock);
		
	}
	
	public void setCurrentRelationAttributesWidget(RelationAttributesWidget raw) {
		kew.setCurrentRelationAttributesWidget(raw);
	}

	public void onClick(Widget sender) {
		hide();
	}
	
	@Override
	public void show() {
		super.show();
		kew.registerAsDropController();
	}
	
	@Override
	public void hide() {
		super.hide();
		kew.clearText();
		kew.unregisterAsDropController();
		
	}
	
	public KeyEditorWidget getKeyEditorWidget() {
		return kew;
	}
}
