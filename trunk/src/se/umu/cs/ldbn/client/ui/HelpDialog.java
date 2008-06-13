package se.umu.cs.ldbn.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public final class HelpDialog extends DialogBox implements ClickListener {

	public HelpDialog() {
		super(false, false);
		setText("Help DialogBox");

		Button closeButton = new Button("Close", this);
		HTML msg = new HTML(
				"<center>This is an example of help dialog.</center>", true);

		DockPanel dock = new DockPanel();
		dock.setSpacing(4);

		dock.add(closeButton, DockPanel.SOUTH);
		dock.add(msg, DockPanel.NORTH);
		dock.add(new Frame("http://www.google.com"), DockPanel.CENTER);

		dock.setCellHorizontalAlignment(closeButton, DockPanel.ALIGN_RIGHT);
		dock.setWidth("100%");
		setWidget(dock);
		
	}

	public void onClick(Widget sender) {
		hide();
	}
}