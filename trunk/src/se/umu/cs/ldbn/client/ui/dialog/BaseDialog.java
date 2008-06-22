package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

abstract class BaseDialog extends DialogBox implements ClickListener {
	
	protected Button okButton;
	protected Button closeButton;
	protected Label errorLabel;
	protected boolean isModal;
	
	public BaseDialog(String title, String msg ,boolean modal) {
		super(false, modal);
		isModal = modal;
		
		setText(title);
		DockPanel dock = new DockPanel();
		dock.setSpacing(4);
		
		if(msg != null) {
			HTML msgHTML = new HTML(
					"<center>"+msg+"</center>", true);
			dock.add(msgHTML, DockPanel.NORTH);
		}
		dock.add(getContentWidget(), DockPanel.CENTER);
		okButton = new Button("OK", this);
		closeButton = new Button("Close", this);
		
		CommonFunctions.setCursorPointer(okButton);
		CommonFunctions.setCursorPointer(closeButton);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(4);
		hp.add(okButton);
		hp.add(closeButton);
		
		
		errorLabel = new Label("");
		Grid g = new Grid(1,2);
		g.setWidget(0, 0, errorLabel);
		g.setWidget(0, 1, hp);
		g.setWidth("100%");
		g.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
		g.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		dock.add(g, DockPanel.SOUTH);
		dock.setWidth("100%");
		
		DOM.setStyleAttribute(errorLabel.getElement(), "color", "red");
		setWidget(dock);
	}
	
	public void center() {
		super.center();
		if (isModal) {
			Main.get().showGlassPanel();
		}
	}
	
	public void hide() {
		super.hide();
		setErrorMsg("");
		if (isModal) {
			Main.get().hideGlassPanel();
		}
	}
	
	public void setErrorMsg(String msg) {
		errorLabel.setText(msg);
	}
	
	protected abstract Widget getContentWidget();
}
