package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.core.AttributeNameTable;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public final class GivenAttributesWidget extends Composite {
	
	private HorizontalPanel mainPanel;
	
	public GivenAttributesWidget () {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
	}
	
	public void setAttributeNames(AttributeNameTable att) {
		mainPanel.clear();
		for (String attName : att.getAttNames()) {
			RelationAttributeWidget raw = new RelationAttributeWidget(attName);
			mainPanel.add(raw);
		}
	}
}
