package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class RelationHolderPanel extends Composite implements ClickListener,
	MouseListener {
	
	private VerticalPanel mainPanel;
	private Image collapseButton;
	private boolean isOpen;
	private Grid header;
	private CheckBox checkBox;
	private FDHolderPanel fdHP;
	private Button addFDBut;
	
	public RelationHolderPanel() {
		mainPanel = new VerticalPanel();
		collapseButton = new Image("img/dw-collapse-but.png", 0, 15, 15, 15);
		collapseButton.addClickListener(this);
		collapseButton.addMouseListener(this);
		header = new Grid(1, 4);
		header.setWidget(0, 0, collapseButton);
		checkBox = new CheckBox();
		header.setWidget(0, 1, checkBox);
		header.setWidget(0, 2, new Label("Relation"));
		fdHP = new FDHolderPanel();
		HorizontalPanel headerControlsPanel = new HorizontalPanel();
		for (int i = 0; i < fdHP.getCheckBoxControlls().length; i++) {
			headerControlsPanel.add(fdHP.getCheckBoxControlls()[i]);
			//headerContorls[i].addStyleName("dw-heder-controls");
			//Common.setCursorPointer(headerContorls[i]);

		}
		
		header.setWidget(0, 3, headerControlsPanel);
		CellFormatter cf = header.getCellFormatter();
		cf.setWidth(0, 0, "20px");
		//IE Bug - must set BG to transparent
		DOM.setStyleAttribute(cf.getElement(0, 0), "background", "transparent");
		cf.setAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		addFDBut = new Button("Add FD");
		fdHP.add(addFDBut);
		mainPanel.add(fdHP);
		initWidget(mainPanel);
	}
	
	public FDHolderPanel fetFDHolderPanel() {
		return fdHP;
	}
	
	public void onClick(Widget sender) { 
		if (sender == addFDBut) {
			Main.get().getFdEditorDialog().center();
			Main.get().getFDEditorWidget().clearText();
			Main.get().getFDEditorWidget().setCurrentFDHolderPanel(fdHP);
		}
		
	}

	public void onMouseDown(Widget sender, int x, int y) {}

	public void onMouseEnter(Widget sender) {
		if (isOpen) {
			collapseButton.setVisibleRect(15, 15, 15, 15);
		} else {
			collapseButton.setVisibleRect(15, 0, 15, 15);
		}
	}

	public void onMouseLeave(Widget sender) {
		if (isOpen) {
			collapseButton.setVisibleRect(0, 15, 15, 15);
		} else {
			collapseButton.setVisibleRect(0, 0, 15, 15);
		}
	}

	public void onMouseMove(Widget sender, int x, int y) {}

	public void onMouseUp(Widget sender, int x, int y) {}
}
