package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MinimalCoverWidget extends Composite implements ClickListener {

	
	private FDHolderPanel mainPanel;
	private Button minCovAddFD;
	
	public MinimalCoverWidget() {
		mainPanel = new FDHolderPanel();
		
		minCovAddFD = new Button("Add new FD");
		minCovAddFD.addStyleName("min-cov-but");
		minCovAddFD.addClickListener(this);
		CommonFunctions.setBGTransparent(minCovAddFD);
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttons.add(minCovAddFD);
		buttons.add(new InfoButton("example"));
		
		mainPanel.add(buttons);
		initWidget(mainPanel);
	}
	
	public void onClick(Widget sender) {
		if (sender == minCovAddFD) {
			FDEditorDialog.get().center(); //always center first
			FDEditorDialog.get().setCurrentFDHolderPanel(mainPanel);
			FDEditorDialog.get().setCurrentDomain(SolveAssignmentWidget.get().getDomainTable());
		}
	}
	
	public FDHolderPanel getFDHolderPanel() {
		return mainPanel;
	}
	
	

}
