package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.KeyEditorDialog;

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

public final class RelationWidget extends Composite implements ClickListener,
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
	private RelationAttributesWidget raw;
	private Image keyButton;
	
	public RelationWidget() {
		this("R1");
	}
	
	public RelationWidget(String name) {
		isOpen = true;
		raw = new RelationAttributesWidget();
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("relW");
		
		header = new Grid(1, 5);
		header.setStyleName("relW-header");
		collapseButton = new Image("img/dw-collapse-but.png", 0, 15, 15, 15);
		collapseButton.addClickListener(this);
		collapseButton.addMouseListener(this);
		CommonFunctions.setCursorPointer(collapseButton);
		collapseButton.setStyleName("relW-collapseBut");
		
		keyButton = new Image("img/key-but-big.png", 0, 0, 20, 20);
		keyButton.addClickListener(this);
		keyButton.addMouseListener(this);
		CommonFunctions.setCursorPointer(keyButton);
		
		checkBox = new CheckBox();
		CommonFunctions.setCursorPointer(checkBox);
		header.setWidget(0, 0, checkBox);
		
		relName = new Label(name);
		header.setWidget(0, 1, relName);
		
		editBut = new Image("img/edit-name.png", 0, 0, 15, 15);
		editBut.addMouseListener(this);
		editBut.addClickListener(this);
		CommonFunctions.setCursorPointer(editBut);
		//header.setWidget(0, 2, editBut);
		
		addBut = new Image("img/add.png", 0, 15, 15, 15);
		addBut.addClickListener(this);
		addBut.addMouseListener(this);
		CommonFunctions.setCursorPointer(addBut);
		header.setWidget(0, 3, addBut);
		
		fdHP = new FDHolderPanel();
		HorizontalPanel headerControlsPanel = new HorizontalPanel();
		for (int i = 0; i < fdHP.getCheckBoxControlls().length; i++) {
			headerControlsPanel.add(fdHP.getCheckBoxControlls()[i]);
			fdHP.getCheckBoxControlls()[i].addStyleName("dw-heder-controls");
			CommonFunctions.setCursorPointer(fdHP.getCheckBoxControlls()[i]);
		}
		header.setWidget(0, 4, headerControlsPanel);
		CellFormatter cf = header.getCellFormatter();
		cf.setAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label attsTitle = new Label("Atributes:");
		Label fdTitle = new Label("FDs:");
		fdTitle.addStyleName("relW-attLabel");
		attsTitle.addStyleName("relW-attLabel");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		hp.add(raw);
		hp.add(keyButton);
		
		fdHP.add(attsTitle);
		
		fdHP.add(hp);
		fdHP.add(fdTitle);
		fdHP.addListener(raw);
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
			FDEditorDialog fded = FDEditorDialog.get();
			fded.center(); //always center first
			fded.getFDEditorWidget().clearText();
			fded.getFDEditorWidget().setCurrentFDHolderPanel(fdHP);
			fded.setCurrentDomain(SolveAssignmentWidget.get().getDomainTable());
		} else if (sender == collapseButton) {
			if(isOpen) {
				mainPanelWrapper.setWidget(0, 1, relName);
				collapseButton.setVisibleRect(15, 0, 15, 15);
				relName.addStyleName("relW-collapsedName");
				isOpen = false;
			} else {
				mainPanelWrapper.setWidget(0, 1, mainPanel);
				header.setWidget(0, 1, relName);
				collapseButton.setVisibleRect(15, 15, 15, 15);
				relName.removeStyleName("relW-collapsedName");
				isOpen = true;
			}
		} else if (sender == keyButton) {
			KeyEditorDialog ked = KeyEditorDialog.get();
			ked.center();
			ked.setCurrentRelationAttributesWidget(raw);
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
		} else if (sender == keyButton) {
			keyButton.setVisibleRect(20, 0, 20, 20);
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
		} else if (sender == keyButton) {
			keyButton.setVisibleRect(0, 0, 20, 20);
		}
		
	}

	public void onMouseMove(Widget sender, int x, int y) {}

	public void onMouseUp(Widget sender, int x, int y) {}
	
	public void setChecked(boolean isChecked) {
		checkBox.setChecked(isChecked);
	}
	
	public boolean isChecked() {
		return checkBox.isChecked();
	}
}