package se.umu.cs.ldbn.client.ui.licence;

import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Hidden;

public class LicenceWidget extends Composite {
	private static LicenceWidget inst;
	
	public static LicenceWidget get() {
		if(inst == null) {
			inst = new LicenceWidget();
		} 
		return inst;
	}
	
	private AbsolutePanel mainPanel;
	private HeaderWidget hw;
	
	private LicenceWidget() {
		mainPanel = new AbsolutePanel();
		initWidget(mainPanel);
		hw = new HeaderWidget();
		Hidden hidden = new Hidden();
		hidden.setHeight("10px");
		hw.add(hidden);
		mainPanel.add(hw);
		Frame viewer = new Frame("info/licence.html");
		viewer.setWidth("100%");
		viewer.setHeight("400px");
		DisclosureWidget dw = new DisclosureWidget("Copyright", viewer);
		mainPanel.add(dw);
		
	}
}
