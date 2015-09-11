package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.ui.dialog.HelpDialog;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;

public class InfoButton extends Image implements ClickHandler {
	
	private String fileBase;
	
	public InfoButton (String fileBase) {
		super(Common.getResourceUrl("img/info.png"));
		Common.setCursorPointer(this);
		this.fileBase = fileBase;
		this.addClickHandler(this);
	}
	
	public String getFileBase() {
		return fileBase;
	}
	
	public void setFileBase(String fileBase) {
		this.fileBase = fileBase;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		HelpDialog.get().showInfo(fileBase+".html");
	}

}
