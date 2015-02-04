package se.umu.cs.ldbn.client.ui.sa;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.core.Relation;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.FDHolderPanelListener;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.dialog.AttributesEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.KeyEditorDialog;
import se.umu.cs.ldbn.client.ui.visualization.VisualizationWindow;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class RelationWidget extends Composite implements ClickHandler,
	FDHolderPanelListener, RelationAttributesWidgetListener,
	MouseOutHandler, MouseOverHandler {
	
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
		raw.addListener(this);
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("relW");
		
		header = new Grid(1, 6);
		header.setStyleName("relW-header");
		collapseButton = new Image("img/dw-collapse-but.png", 0, 15, 15, 15);
		collapseButton.addClickHandler(this);
		collapseButton.addMouseOutHandler(this);
		collapseButton.addMouseOverHandler(this);
		
		Common.setCursorPointer(collapseButton);
		collapseButton.setStyleName("relW-collapseBut");
		
		editBut = new Image("img/edit-big.png", 0, 0, 20, 20);
		editBut.addMouseOutHandler(this);
		editBut.addMouseOverHandler(this);
		editBut.addClickHandler(this);
		editBut.setTitle("Edit attributes");
		Common.setCursorPointer(editBut);
		
		keyButton = new Image("img/key-but-big.png", 0, 0, 20, 20);
		keyButton.addClickHandler(this);
		keyButton.addMouseOverHandler(this);
		keyButton.addMouseOutHandler(this);
		keyButton.setTitle("Define a key");
		Common.setCursorPointer(keyButton);
		
		checkBox = new CheckBox();
		Common.setCursorPointer(checkBox);
		header.setWidget(0, 0, checkBox);
		
		relName = new Label(name);
		header.setWidget(0, 1, relName);
		
		addBut = new Image("img/add.png", 0, 15, 15, 15);
		addBut.addClickHandler(this);
		addBut.addMouseOutHandler(this);
		addBut.addMouseOverHandler(this);
		Common.setCursorPointer(addBut);
		addBut.setTitle("Add FDs");
		header.setWidget(0, 3, addBut);
		
		Image visual = new Image("img/eye.png");
		visual.setTitle("FD Visualization");
		Common.setCursorPointer(visual);
		visual.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				VisualizationWindow vw = VisualizationWindow.get();
				vw.setData(raw.getAttributes(), fdHP.getFDs());
				vw.center();
				
			}
		});
		header.setWidget(0, 4, visual);
		
		fdHP = new FDHolderPanel();
		HorizontalPanel headerControlsPanel = new HorizontalPanel();
		for (int i = 0; i < fdHP.getCheckBoxControlls().length; i++) {
			headerControlsPanel.add(fdHP.getCheckBoxControlls()[i]);
			fdHP.getCheckBoxControlls()[i].addStyleName("dw-heder-controls");
			Common.setCursorPointer(fdHP.getCheckBoxControlls()[i]);
		}
		header.setWidget(0, 5, headerControlsPanel);
		CellFormatter cf = header.getCellFormatter();
		cf.setAlignment(0, 5, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label attsTitle = new Label("Attributes:");
		Label fdTitle = new Label("FDs:");
		fdTitle.addStyleName("relW-attLabel");
		attsTitle.addStyleName("relW-attLabel");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		hp.add(raw);
		hp.add(editBut);
		hp.setSpacing(4);
		hp.add(keyButton);
		
		fdHP.add(attsTitle);
		
		fdHP.add(hp);
		fdHP.add(fdTitle);
		fdHP.addListener(this);
		mainPanel.add(header);
		mainPanel.add(fdHP);
		
		mainPanelWrapper = new Grid(1,2);
		mainPanelWrapper.setWidget(0, 0, collapseButton);
		cf = mainPanelWrapper.getCellFormatter();
		cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP);
		mainPanelWrapper.setWidget(0, 1, mainPanel);
		initWidget(mainPanelWrapper);
	}
	
	public FDHolderPanel getFDHolderPanel() {
		return fdHP;
	}
	
	public RelationAttributesWidget getRelationAttributesWidget() {
		return raw;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
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
		} else if (sender == editBut) {
			AttributesEditorDialog dialog = AttributesEditorDialog.get();
			dialog.center();
			dialog.setCurrentRelationAttributesWidget(raw);
		}
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
		Object sender = event.getSource();
		if (sender == collapseButton) {
			if (isOpen) {
				collapseButton.setVisibleRect(0, 15, 15, 15);
			} else {
				collapseButton.setVisibleRect(0, 0, 15, 15);
			}
		} else if (sender == addBut) {
			addBut.setVisibleRect(0, 15, 15, 15);
		} else if (sender == editBut) {
			editBut.setVisibleRect(0, 0, 20, 20);
		} else if (sender == keyButton) {
			keyButton.setVisibleRect(0, 0, 20, 20);
		}
		
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		Object sender = event.getSource();
		if(sender == collapseButton) {
			if (isOpen) {
				collapseButton.setVisibleRect(15, 15, 15, 15);
			} else {
				collapseButton.setVisibleRect(15, 0, 15, 15);
			}
		} else if (sender == addBut) {
			addBut.setVisibleRect(15, 15, 15, 15);
		} else if (sender == editBut) {
			editBut.setVisibleRect(20, 0, 20, 20);
		} else if (sender == keyButton) {
			keyButton.setVisibleRect(20, 0, 20, 20);
		}
		
	}

	@Override
	public void allFDsRemoved() {
		raw.getAttributes().clearAllAttributes();
		raw.setHTML(raw.generateAttributeHTML());
		
	}
	
	@Override
	public void fdAdded(Collection<FDWidget> currentFDs) {
		for (FDWidget fdw : currentFDs) {
			raw.getAttributes().union(fdw.getFD().getLHS());
			raw.getAttributes().union(fdw.getFD().getRHS());
		}
		raw.setHTML(raw.generateAttributeHTML());
	}
	
	public void fdRemoved(Collection<FDWidget> currentFDs) {
//		attributes.clearAllAttributes();
//		for (FDWidget fdw : currentFDs) {
//			attributes.union(fdw.getFD().getLHS());
//			attributes.union(fdw.getFD().getRHS());
//			
//		}
//		key.andOperator(attributes);
//		setHTML(generateAttributeHTML());
	}

	@Override
	public void onAttributesChange() {
		//DO NOT USE FDHolderPanel.removeFDWidget(), or you will notify the listener
		//and there will be an endless loop.
		Collection<FDWidget> widgets = getFDHolderPanel().getFDWidgets();
		AttributeSet att = raw.getAttributes();
		for (Iterator<FDWidget> i = widgets.iterator(); i.hasNext();) {
			FDWidget fdw = i.next();
			fdw.getFD().getLHS().andOperator(att);
			if(fdw.getFD().getLHS().isEmpty()) {
				fdw.getParent().removeFromParent();
				i.remove();
				continue;
			}
			fdw.getFD().getRHS().andOperator(att);
			if(fdw.getFD().getRHS().isEmpty()) {
				fdw.getParent().removeFromParent();
				i.remove();
				continue;
			}
			fdw.rebuildHTML();
		}
	}
	
	
	public Relation getRelation() {
		Relation r = new Relation();
		AttributeSet atts = raw.getAttributes();
		List<FD> fds = fdHP.getFDs();
		r.setAttributes(atts);
		r.setPrimaryKey(raw.getKey());
		r.setFDs(fds);
		r.setName(relName.getText());
		return r;
	}
	
	public void setChecked(boolean isChecked) {
		checkBox.setValue(isChecked);
	}
	
	public boolean isChecked() {
		return checkBox.getValue();
	}
}
