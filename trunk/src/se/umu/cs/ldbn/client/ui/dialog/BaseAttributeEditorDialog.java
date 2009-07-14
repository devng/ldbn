package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.ui.AttributeTextArea;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

abstract class BaseAttributeEditorDialog extends CloseDialog {

	protected HorizontalPanel mainPanel;
	protected Button clearButton;
	protected Button setButton;
	protected InfoButton infoButton;
	
	protected BaseAttributeEditorDialog(String title) {
		super(title, false);
	}
	
	@Override
	public void onClick(Widget sender) {
		super.onClick(sender);
		if (sender == clearButton) {
			onClearButClicked();
		} else if (sender == setButton) {
			onAddButClicked();
		}
	}
	protected abstract AttributeTextArea getAttributeTextArea();
	
	protected Widget getDialogContentWidget() {
		if(mainPanel != null) {
			return mainPanel;
		}
		mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(4);
		
		AttributeTextArea textArea = getAttributeTextArea();
		textArea.setSize("220px", "70px");
		mainPanel.add(textArea);
		
		VerticalPanel vp = new VerticalPanel();
		infoButton = new InfoButton("example");
		vp.add(infoButton);
		setButton = new Button(I18N.constants().setBut());
		setButton.setStyleName("dew-btn");
		setButton.addClickListener(this);
		Common.setCursorPointer(setButton);
		vp.add(setButton);
		clearButton = new Button(I18N.constants().clearBut());
		clearButton.setStyleName("dew-btn");
		clearButton.addClickListener(this);
		Common.setCursorPointer(clearButton);
		vp.add(clearButton);
		mainPanel.add(vp);
	
		return mainPanel;
	}
	
	protected abstract void onAddButClicked();
	
	protected void onClearButClicked() {
		getAttributeTextArea().setText("");
	}
}
