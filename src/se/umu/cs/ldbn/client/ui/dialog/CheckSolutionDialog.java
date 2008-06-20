package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.CommonFunctions;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
//TODO DO it properly
public final class CheckSolutionDialog extends DialogBox 
	implements ClickListener {

	private Panel mainPanel;
	
	private static CheckSolutionDialog inst;
	
	private CheckSolutionDialog() {
		super(false, false);
		setText("Check Solution Dialog");
		Button closeButton = new Button("Close", this);
		CommonFunctions.setCursorPointer(closeButton);
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("csd-innerPanel");
	}
	
	public static CheckSolutionDialog get() {
		if (inst == null) {
			inst = new CheckSolutionDialog();
		}
		return inst;
	}
	
	
	public void onClick(Widget sender) {
		hide();
	}
	
	public void clearMsgs() {
		mainPanel.clear();
	}
	
	public void msgOK(String msg) {
		HorizontalPanel hp = new HorizontalPanel();
		Image okMsg   = new Image("img/okMsg.png");
		hp.add(okMsg);
		hp.add(new Label(msg));
	}
	
	public void msgErr(String msg) {
		HorizontalPanel hp = new HorizontalPanel();
		Image errMsg  = new Image("img/warnMsg.png");
		hp.add(errMsg);
		hp.add(new Label(msg));
	}
	
	public void msgWarn(String msg) {
		HorizontalPanel hp = new HorizontalPanel();
		Image warnMsg = new Image("img/errMsg.png");
		hp.add(warnMsg);
		hp.add(new Label(msg));
	}

}
