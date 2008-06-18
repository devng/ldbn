package se.umu.cs.ldbn.client.ui;

import java.util.List;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;
import se.umu.cs.ldbn.client.ui.ca.EditableGivenAttributesWidget;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.HelpDialog;
import se.umu.cs.ldbn.client.ui.sa.RelationAttributesWidget;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class FDEditorWidget extends Composite
	implements ClickListener {
	
	//TODO debug
	private HelpDialog dlg;
	
	private FDEditorTextArea leftTA;
	private FDEditorTextArea rightTA;
	private Image arrowImg;
	private Image infoButton;
	private HorizontalPanel mainPanel;
	private Button clearButton;
	private Button addButton;
	private FDHolderPanel currnetFDHP;
	
	private final class FDEditorTextArea extends AttributeTextArea  {
		
		public void onDrop (DragContext context) {
			Widget w = context.draggable;
			if(w == null) return;
			
			if(w instanceof SingleAttributeWidget) {
				this.appendAttributes(((SingleAttributeWidget)w).getText());
			} else if (w instanceof FDWidget) {
				FDWidget fdw = (FDWidget) w;
				setFDWidget(fdw);
			} else if (w instanceof RelationAttributesWidget) {
				RelationAttributesWidget rw = (RelationAttributesWidget) w;
				List<String> names =  rw.getAttributes().getAttributeNames();
				for (String str : names) {
					this.appendAttributes(str);
				}
			}
		}
		
		public void setFDWidget(FDWidget fdw) {
			FDEditorWidget fdEdit = FDEditorDialog.get().getFDEditorWidget();
			AttributeTextArea ata = fdEdit.getLeftTextArea();
			List<String> atts = fdw.getFD().getLHS()
					.getAttributeNames();
			for (String str : atts) {
				ata.appendAttributes(str);
			}
			ata = fdEdit.getRightTextArea();
			atts = fdw.getFD().getRHS().getAttributeNames();
			for (String str : atts) {
				ata.appendAttributes(str);
			}
			if (fdw.isEditable()) {
				if(currnetFDHP != null) { 
					//TODO Do it better
					//so that the widget is not removed, but rather edited
					currnetFDHP.removeFDWidget(fdw);
				}
			}
		}
	}
	
	public FDEditorWidget() {
		super();
		mainPanel = new HorizontalPanel();
		dlg = new HelpDialog();
		
		mainPanel.setSpacing(4);
		
		mainPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

		leftTA = new FDEditorTextArea();
		leftTA.setSize("160px", "70px");
		mainPanel.add(leftTA);

		arrowImg = new Image("img/arrow-right-large.png");
		mainPanel.add(arrowImg);
		
		rightTA = new FDEditorTextArea();
		rightTA.setSize("160px", "70px");
		mainPanel.add(rightTA);
		
		mainPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		
		VerticalPanel vp = new VerticalPanel();
		infoButton = new Image("img/info.png");
		CommonFunctions.setCursorPointer(infoButton);
		infoButton.addClickListener(this);
		vp.add(infoButton);
		addButton = new Button("Set/Add");
		addButton.addClickListener(this);
		CommonFunctions.setCursorPointer(addButton);
		addButton.setStyleName("fdew-btn");
		vp.add(addButton);
		clearButton = new Button("Clear");
		clearButton.addClickListener(this);
		CommonFunctions.setCursorPointer(clearButton);
		clearButton.setStyleName("fdew-btn");
		vp.add(clearButton);
		mainPanel.add(vp);
		
		initWidget(mainPanel);
	}
	
	public AttributeTextArea getLeftTextArea() {
		return leftTA;
	}
	
	public AttributeTextArea getRightTextArea() {
		return rightTA;
	}

	public void onClick(Widget sender) {
		if (sender.equals(clearButton)) {
			clearTextAreas();
		} else if (sender.equals(addButton)) {
			FDWidget fd = createFD();
			if(fd == null) {
				FDEditorDialog.get().setErrorMsg("LHS or RHS of the FD has no attributes.");
				return;
			}
			addFDWidget(fd);
		} else if (sender.equals(infoButton)) {
		    dlg.center();
		}
	}

	public void setCurrentFDHolderPanel(FDHolderPanel fdHP) {
		currnetFDHP = fdHP;
	}
	
	public void setFDWidtet(FDWidget fdw) {
		leftTA.setFDWidget(fdw);
	}
	
	public void clearText() {
		leftTA.setText("");
		rightTA.setText("");
	}
	
	/**
	 * use setCurrentFDHolderPanel before you use this method
	 * @param fd
	 */
	private void addFDWidget(FDWidget fd) {
		if (currnetFDHP != null) {
			currnetFDHP.addFDWidget(fd);
		}
	}
	
	//returns null if the size of LHS or RHS is < 1
	private FDWidget createFD() {
		AttributeNameTable domain = SolveAssignmentWidget.get().getAttributeNameTable();
		FD fd = new FD(domain);
		List<String> l = leftTA.parseAttributes();
		if(l.size() < 1) {
			return null;
		}
		boolean isAnythinInserted = false;
		for (int i = 0; i < l.size(); i++) {
			if(fd.getLHS().addAtt(l.get(i))) {
				isAnythinInserted = true;
			}
		}
		if(!isAnythinInserted) {
			return null;
		}
		List<String> r = rightTA.parseAttributes();
		if(l.size() < 1) {
			return null;
		}
		isAnythinInserted = false;
		for (int i = 0; i < r.size(); i++) {
			if(fd.getRHS().addAtt(r.get(i))) {
				isAnythinInserted = true;
			}
		}
		if(!isAnythinInserted) {
			return null;
		}
		FDWidget fdw = new FDWidget(true, fd);
		clearTextAreas();
		return fdw;
	}
	
	private void clearTextAreas() {
		leftTA.setText("");
		rightTA.setText("");
	}
}
