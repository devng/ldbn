package se.umu.cs.ldbn.client.ui;

import java.util.List;

import se.umu.cs.ldbn.client.core.FD;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class GivenFDsWidget extends Composite {
	
	private VerticalPanel mainPanel;
	
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
