package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class GivenAttributesWidget extends Composite {
	
	protected HorizontalPanel mainPanel;
	protected AttributeNameTable att;
	
	public GivenAttributesWidget () {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
	}
	
	public void setAttributeNames(AttributeNameTable att) {
		this.att = att;
		recalculateMainPanel();
	}
	
	public AttributeNameTable getAttributeNames() {
		return att;
	}
	
	protected void recalculateMainPanel() {
		mainPanel.clear();
		for (String attName : att.getAttNames()) {
			SingleAttributeWidget saw = new SingleAttributeWidget(attName);
			mainPanel.add(saw);
		}
	}
}
