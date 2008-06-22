package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.HasAdditionalControlls;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MinimalCoverWidget extends Composite implements ClickListener,
	HasAdditionalControlls {

	
	private FDHolderPanel mainPanel;
	private Button minCovAddFD;
	
	public MinimalCoverWidget() {
		mainPanel = new FDHolderPanel();
		
		minCovAddFD = new Button("Add new FD");
		minCovAddFD.setStyleName("min-cov-but");
		minCovAddFD.addClickListener(this);
		CommonFunctions.setCursorPointer(minCovAddFD);
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttons.add(minCovAddFD);
		buttons.add(new InfoButton("example"));
		
		mainPanel.add(buttons);
		initWidget(mainPanel);
	}
	
	public Widget[] getAdditionalControlls() {
		return mainPanel.getAdditionalControlls();
	}
	
	public FDHolderPanel getFDHolderPanel() {
		return mainPanel;
	}
	
	public void onClick(Widget sender) {
		if (sender == minCovAddFD) {
			FDEditorDialog.get().center(); //always center first
			FDEditorDialog.get().setCurrentFDHolderPanel(mainPanel);
			FDEditorDialog.get().setCurrentDomain(SolveAssignmentWidget.get().getDomainTable());
		}
	}
}
