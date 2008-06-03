package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

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
	private Grid mainPanelWrapper;
	private Image collapseButton;
	private boolean isOpen;
	private Grid header;
	private CheckBox checkBox;
	private FDHolderPanel fdHP;
	private Label relName;
	private Image editBut;
	private Image addBut;
	
	public RelationHolderPanel() {
		isOpen = true;
		
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("relHP");
		
		header = new Grid(1, 5);
		header.setStyleName("relHP-header");
		collapseButton = new Image("img/dw-collapse-but.png", 0, 15, 15, 15);
		collapseButton.addClickListener(this);
		collapseButton.addMouseListener(this);
		CommonStyle.setCursorPointer(collapseButton);
		collapseButton.setStyleName("relHP-collapseBut");
		
		checkBox = new CheckBox();
		CommonStyle.setCursorPointer(checkBox);
		header.setWidget(0, 0, checkBox);
		
		relName = new Label("R1");
		header.setWidget(0, 1, relName);
		
		editBut = new Image("img/edit-name.png", 0, 0, 15, 15);
		editBut.addMouseListener(this);
		editBut.addClickListener(this);
		CommonStyle.setCursorPointer(editBut);
		header.setWidget(0, 2, editBut);
		
		addBut = new Image("img/add.png", 0, 15, 15, 15);
		addBut.addClickListener(this);
		addBut.addMouseListener(this);
		CommonStyle.setCursorPointer(addBut);
		header.setWidget(0, 3, addBut);
		
		fdHP = new FDHolderPanel();
		HorizontalPanel headerControlsPanel = new HorizontalPanel();
		for (int i = 0; i < fdHP.getCheckBoxControlls().length; i++) {
			headerControlsPanel.add(fdHP.getCheckBoxControlls()[i]);
			fdHP.getCheckBoxControlls()[i].addStyleName("dw-heder-controls");
			CommonStyle.setCursorPointer(fdHP.getCheckBoxControlls()[i]);
		}
		header.setWidget(0, 4, headerControlsPanel);
		CellFormatter cf = header.getCellFormatter();
		cf.setAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		mainPanel.add(header);
		mainPanel.add(fdHP);
		
		mainPanelWrapper = new Grid(1,2);
		mainPanelWrapper.setWidget(0, 0, collapseButton);
		cf = mainPanelWrapper.getCellFormatter();
		cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP);
		mainPanelWrapper.setWidget(0, 1, mainPanel);
		initWidget(mainPanelWrapper);
	}
	
	public FDHolderPanel fetFDHolderPanel() {
		return fdHP;
	}
	
	public void onClick(Widget sender) { 
		if (sender == addBut) {
			Main.get().getFdEditorDialog().center();
			Main.get().getFDEditorWidget().clearText();
			Main.get().getFDEditorWidget().setCurrentFDHolderPanel(fdHP);
		} else if (sender == collapseButton) {
			if(isOpen) {
				mainPanelWrapper.setWidget(0, 1, relName);
				collapseButton.setVisibleRect(15, 0, 15, 15);
				relName.addStyleName("relHP-collapsedName");
				isOpen = false;
			} else {
				mainPanelWrapper.setWidget(0, 1, mainPanel);
				header.setWidget(0, 1, relName);
				collapseButton.setVisibleRect(15, 15, 15, 15);
				relName.removeStyleName("relHP-collapsedName");
				isOpen = true;
			}
			
			
		}
		
	}

	public void onMouseDown(Widget sender, int x, int y) {}

	public void onMouseEnter(Widget sender) {
		if(sender == collapseButton) {
			if (isOpen) {
				collapseButton.setVisibleRect(15, 15, 15, 15);
			} else {
				collapseButton.setVisibleRect(15, 0, 15, 15);
			}
		} else if (sender == addBut) {
			addBut.setVisibleRect(15, 15, 15, 15);
		} else if (sender == editBut) {
			editBut.setVisibleRect(15, 0, 15, 15);
		}
		
	}

	public void onMouseLeave(Widget sender) {
		if (sender == collapseButton) {
			if (isOpen) {
				collapseButton.setVisibleRect(0, 15, 15, 15);
			} else {
				collapseButton.setVisibleRect(0, 0, 15, 15);
			}
		} else if (sender == addBut) {
			addBut.setVisibleRect(0, 15, 15, 15);
		} else if (sender == editBut) {
			editBut.setVisibleRect(0, 0, 15, 15);
		}
		
	}

	public void onMouseMove(Widget sender, int x, int y) {}

	public void onMouseUp(Widget sender, int x, int y) {}
}
