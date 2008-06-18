package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class GivenAttributesWidget extends Composite {
	
	protected HorizontalPanel mainPanel;
	protected DomainTable att;
	
	public GivenAttributesWidget () {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
	}
	
	public void setDomain(DomainTable att) {
		this.att = att;
		recalculateMainPanel();
	}
	
	public DomainTable getDomain() {
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
