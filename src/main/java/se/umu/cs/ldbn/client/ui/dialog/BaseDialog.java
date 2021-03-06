package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.ui.window.WindowPanel;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

abstract public class BaseDialog extends WindowPanel implements ClickHandler {
	
	protected Button okButton;
	protected Button closeButton;
	protected Label errorLabel;
	protected boolean isModal;

	public BaseDialog(String title, boolean modal) {
		super(title, false);
		isModal = modal;
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

	protected boolean useScrollPanel() {
		return false;
	}
	
	protected Widget getContentWidget() {
		DockPanel dock = new DockPanel();
		dock.setSpacing(4);
		String msg = getHelpMessage();
		if(msg != null) {
			HTML msgHTML = new HTML(
					"<center>"+msg+"</center>", true);
			dock.add(msgHTML, DockPanel.NORTH);
		}
		dock.add(getDialogContentWidget(), DockPanel.CENTER);
		okButton = new Button(I18N.constants().okBut());
		closeButton = new Button(I18N.constants().closeBut());
		okButton.addClickHandler(this);
		closeButton.addClickHandler(this);
		
		Common.setCursorPointer(okButton);
		Common.setCursorPointer(closeButton);
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
		return dock;
	}
	
	protected abstract Widget getDialogContentWidget();
	
	protected String getHelpMessage() {
		return null;
	}
	
}
