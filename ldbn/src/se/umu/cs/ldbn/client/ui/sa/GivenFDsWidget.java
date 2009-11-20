package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.AlignmentButton;
import se.umu.cs.ldbn.client.ui.AlignmentButtonListener;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HasAdditionalControlls;
import se.umu.cs.ldbn.client.ui.visualization.VisualizationWindow;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class GivenFDsWidget extends Composite 
	implements AlignmentButtonListener, HasAdditionalControlls, ClickHandler {
	
	private Grid mainPanel;
	private Panel mainPanelChild;
	private AlignmentButton ab;
	private Image visual;
	private Widget[] additionalControlls;
	private List<Widget> currentContent;
	
	public GivenFDsWidget() {
		mainPanel = new Grid(1,1);
		mainPanelChild = new VerticalPanel();
		mainPanel.setWidget(0, 0,mainPanelChild);
		initWidget(mainPanel);
		
		ab = new AlignmentButton(false);
		ab.setTitle("Align horizontal/vertical");
		ab.addListener(this);
		
		visual = new Image("img/eye.png");
		visual.setTitle("FD Visualization");
		visual.addClickHandler(this);
		Common.setCursorPointer(visual);
		
		additionalControlls = new Widget[2];
		additionalControlls[0] = visual;
		additionalControlls[1] = ab;
		
		currentContent = new ArrayList<Widget>();
	}
	
	public void clearData() {
		mainPanelChild.clear();
		currentContent.clear();
	}
	
	public void setFDs(List<FD> fds) {
		mainPanelChild.clear();
		for (FD fd : fds) {
			HorizontalPanel hp = new HorizontalPanel();
			FDWidget tmp = new FDWidget(false, fd);
			hp.add(tmp);
			currentContent.add(hp);
			mainPanelChild.add(hp);
		}
	}
	
	public void onAlignmentChanged(boolean isHorizontal) {
		Panel newP = isHorizontal ? new HorizontalPanel() : new VerticalPanel();
		for (Widget w : currentContent) {
			newP.add(w);
		}
		SolveAssignmentWidget.get().dwFDs.resetHeightToDefault();
		mainPanel.setWidget(0, 0, newP);
		mainPanelChild = newP;
	}
	
	public Widget[] getAdditionalControlls() {
		return additionalControlls;
	}

	@Override
	public void onClick(ClickEvent event) {
		VisualizationWindow vw = VisualizationWindow.get();
		SolveAssignmentWidget sa = SolveAssignmentWidget.get();
		vw.setData(sa.domainAsAttSet, sa.fds);
		vw.center();
	}
}
