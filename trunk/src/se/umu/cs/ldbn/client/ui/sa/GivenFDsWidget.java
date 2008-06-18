package se.umu.cs.ldbn.client.ui.sa;

import java.util.List;

import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.FDWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class GivenFDsWidget extends Composite {
	
	private Panel mainPanel;
	
	public GivenFDsWidget() {
		mainPanel = new VerticalPanel();
		initWidget(mainPanel);
	}
	
	public void setFDs(List<FD> fds) {
		mainPanel.clear();
		for (FD fd : fds) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(new FDWidget(false, fd));
			mainPanel.add(hp);
		}
	}
}
