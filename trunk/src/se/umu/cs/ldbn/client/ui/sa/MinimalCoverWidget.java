package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.HasAdditionalControlls;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.visualization.VisualizationWindow;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MinimalCoverWidget extends Composite implements ClickListener,
	HasAdditionalControlls {

	
	private FDHolderPanel mainPanel;
	private Button minCovAddFD;
	private Image visual;
	
	public MinimalCoverWidget() {
		mainPanel = new FDHolderPanel();
		
		minCovAddFD = new Button("Add new FD");
		minCovAddFD.setStyleName("min-cov-but");
		minCovAddFD.addClickListener(this);
		Common.setCursorPointer(minCovAddFD);
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttons.add(minCovAddFD);
		buttons.add(new InfoButton("find-mincov"));
		
		visual = new Image("img/eye.png");
		visual.setTitle("FD Visualization");
		visual.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				VisualizationWindow vw = VisualizationWindow.get();
				SolveAssignmentWidget sa = SolveAssignmentWidget.get();
				vw.setData(sa.domainAsAttSet, mainPanel.getFDs());
				vw.center();
				
			}
		});
		Common.setCursorPointer(visual);
		
		mainPanel.add(buttons);
		initWidget(mainPanel);
	}
	
	public Widget[] getAdditionalControlls() {
		Widget[] superAdditionalControl = mainPanel.getAdditionalControlls();
		Widget[] additionalContorll = 
			new Widget[superAdditionalControl.length+1];
		additionalContorll[0] = visual;
		for (int i = 0; i < superAdditionalControl.length; i++) {
			Widget widget = superAdditionalControl[i];
			additionalContorll[i+1] = widget;
		}
		return additionalContorll;
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
