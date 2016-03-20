package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.DomainTableListener;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class GivenAttributesWidget extends Composite 
	implements DomainTableListener{
	
	protected HorizontalPanel mainPanel;
	protected DomainTable att;
	
	public GivenAttributesWidget () {
		mainPanel = new HorizontalPanel();
		initWidget(mainPanel);
	}
	
	public void setDomain(DomainTable att) {
		if(att == null) {
			Log.warn("GivenAttributesWidget: DomainTable cannot be null");
			return;
		}
		this.att = att;
		att.addListener(this);
		recalculateMainPanel();
	}
	
	public DomainTable getDomain() {
		return att;
	}
	
	public void onDomainChange() {
		recalculateMainPanel();
		
	}
	
	protected void recalculateMainPanel() {
		mainPanel.clear();
		for (String attName : att.getAttNames()) {
			SingleAttributeWidget saw = new SingleAttributeWidget(attName);
			mainPanel.add(saw);
		}
	}


}
