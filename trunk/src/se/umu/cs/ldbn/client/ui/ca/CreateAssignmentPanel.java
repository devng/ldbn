package se.umu.cs.ldbn.client.ui.ca;


import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.dialog.AttributeEditorDialog;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class CreateAssignmentPanel extends Composite 
	implements ClickListener {
	
	private AbsolutePanel mainPanel;
	private EditableGivenAttributesWidget egas;
	private FDHolderPanel fdh;
	private Button addAtts;
	
	private static CreateAssignmentPanel inst;
	
	private CreateAssignmentPanel() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		
		VerticalPanel vp = new VerticalPanel();
		addAtts = new Button ("Add/Remove Attributes");
		addAtts.addClickListener(this);
		vp.add(addAtts);
		egas = EditableGivenAttributesWidget.get();
		egas.getAttributeNameTable().addAtt("A");
		egas.getAttributeNameTable().addAtt("B");
		vp.add(egas);
		
		DisclosureWidget dw = new DisclosureWidget("Given attributes", vp);
		mainPanel.add(dw);
		
		initWidget(mainPanel);
	}
	
	public static CreateAssignmentPanel get() {
		if(inst == null) {
			inst = new CreateAssignmentPanel();
		}
		return inst;
	}

	public void onClick(Widget sender) {
		if(sender == addAtts) {
			AttributeEditorDialog atd = AttributeEditorDialog.get();
			atd.center();
		}
		
	}
}
