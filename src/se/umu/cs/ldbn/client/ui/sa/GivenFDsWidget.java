package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.AlignmentButton;
import se.umu.cs.ldbn.client.ui.AlignmentButtonListener;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HasAdditionalControlls;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class GivenFDsWidget extends Composite 
	implements AlignmentButtonListener, HasAdditionalControlls {
	
	private Grid mainPanel;
	private Panel mainPanelChild;
	private AlignmentButton ab;
	private Widget[] additionalControlls;
	private List<Widget> currentContent;
	
	public GivenFDsWidget() {
		mainPanel = new Grid(1,1);
		mainPanelChild = new VerticalPanel();
		mainPanel.setWidget(0, 0,mainPanelChild);
		initWidget(mainPanel);
		
		ab = new AlignmentButton(false);
		additionalControlls = new Widget[1];
		additionalControlls[0] = ab;
		ab.addListener(this);
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
}
