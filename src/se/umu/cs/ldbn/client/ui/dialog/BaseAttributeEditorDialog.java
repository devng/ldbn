package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.AttributeTextArea;
import se.umu.cs.ldbn.client.ui.InfoButton;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

abstract class BaseAttributeEditorDialog extends CloseDialog {

	protected HorizontalPanel mainPanel;
	protected Button clearButton;
	protected Button setButton;
	
	protected BaseAttributeEditorDialog(String title, String msg) {
		super(title, msg+"<BR>You can use Drag'n'Drop.", false);
	}
	
	protected abstract AttributeTextArea getAttributeTextArea();
	protected abstract void onInfoButClicked();
	protected abstract void onAddButClicked();
	
	protected void onClearButClicked() {
		getAttributeTextArea().setText("");
	}
	
	public void onClick(Widget sender) {
		super.onClick(sender);
		if (sender == clearButton) {
			onClearButClicked();
		} else if (sender == setButton) {
			onAddButClicked();
		}
	}
	
	protected Widget getContentWidget() {
		if(mainPanel != null) {
			return mainPanel;
		}
		mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(4);
		
		AttributeTextArea textArea = getAttributeTextArea();
		textArea.setSize("220px", "70px");
		mainPanel.add(textArea);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(new InfoButton("example"));
		setButton = new Button("Set");
		setButton.setStyleName("dew-btn");
		setButton.addClickListener(this);
		CommonFunctions.setCursorPointer(setButton);
		vp.add(setButton);
		clearButton = new Button("Clear");
		clearButton.setStyleName("dew-btn");
		clearButton.addClickListener(this);
		CommonFunctions.setCursorPointer(clearButton);
		vp.add(clearButton);
		mainPanel.add(vp);
	
		return mainPanel;
	}
}
