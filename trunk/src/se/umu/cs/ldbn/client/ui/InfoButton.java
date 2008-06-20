package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.dialog.HelpDialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class InfoButton extends Image implements ClickListener {
	
	private String fileBase;
	
	public InfoButton (String fileBase) {
		super("img/info.png");
		CommonFunctions.setCursorPointer(this);
		this.fileBase = fileBase;
		this.addClickListener(this);
	}
	
	public String getFileBase() {
		return fileBase;
	}
	
	public void onClick(Widget sender) {
		HelpDialog.get().showInfo(fileBase+".html");
	}

}
