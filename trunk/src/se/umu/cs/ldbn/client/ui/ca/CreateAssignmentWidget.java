package se.umu.cs.ldbn.client.ui.ca;


import java.util.Collection;

import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.FDHolderPanelListener;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.dialog.AttributeEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class CreateAssignmentWidget extends Composite 
	implements ClickListener, FDHolderPanelListener {
	
	private AbsolutePanel mainPanel;
	private EditableGivenAttributesWidget egas;
	private Button addAtts;
	
	private static CreateAssignmentWidget inst;
	private FDHolderPanel givenFDs;
	private Button addFDs;
	
	private CreateAssignmentWidget() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		
		VerticalPanel vp = new VerticalPanel();
		addAtts = new Button ("Add/Remove Attributes");
		addAtts.addClickListener(this);
		vp.add(addAtts);
		egas = new EditableGivenAttributesWidget();
		egas.getDomain().addAtt("A");
		egas.getDomain().addAtt("B");
		vp.add(egas);
		
		DisclosureWidget dw = new DisclosureWidget("Given attributes", vp);
		mainPanel.add(dw);
		
		givenFDs = new FDHolderPanel();
		HorizontalPanel hp = new HorizontalPanel();
		addFDs = new Button("Add FDs");
		addFDs.addClickListener(this);
		hp.add(addFDs);
		givenFDs.add(hp);
		dw = new DisclosureWidget("Given FDs", givenFDs);
		mainPanel.add(dw);
		
		initWidget(mainPanel);
	}
	
	public static CreateAssignmentWidget get() {
		if (inst == null) {
			inst = new CreateAssignmentWidget();
		}
		return inst;
	}
	
	public AttributeNameTable getDomain() {
		return egas.getDomain();
	}

	public void onClick(Widget sender) {
		if(sender == addAtts) {
			AttributeEditorDialog atd = AttributeEditorDialog.get();
			atd.center();
		} else if (sender == addFDs) {
			FDEditorDialog fded = FDEditorDialog.get();
			fded.setCurrentFDHolderPanel(givenFDs);
			fded.center();
		}
		
	}

	
	public void allFDsRemoved() {
		// TODO Auto-generated method stub
		
	}

	
	public void fdAdded(Collection<FDWidget> currentFDs) {
		// TODO Auto-generated method stub
		
	}

	
	public void fdRemoved(Collection<FDWidget> currentFDs) {
		// TODO Auto-generated method stub
		
	}
}
