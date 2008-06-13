package se.umu.cs.ldbn.client.ui;

import java.util.List;

import se.umu.cs.ldbn.client.core.FD;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class GivenFDsWidget extends Composite {
	
	private Panel mainPanel;
	
	public GivenFDsWidget() {
		mainPanel = new VerticalPanel();
		initWidget(mainPanel);
	}
	
	public void setFDs(List<FD> fds) {
		mainPanel.clear();
		for (FD fd : fds) {
			mainPanel.add(new FDWidget(false, fd));
		}
	}
}
