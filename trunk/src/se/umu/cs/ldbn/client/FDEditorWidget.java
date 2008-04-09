package se.umu.cs.ldbn.client;

import java.util.HashSet;

import se.umu.cs.ldbn.client.core.FD;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class FDEditorWidget extends  CheckBoxWidget 
	implements HasUpControlls {
	
	protected FDEditorTextArea leftTA;
	protected FDEditorTextArea rightTA;
	private Image arrowImg;
	private Image collapseButton;
	private Image infoButton;
	private boolean isOpen = true;
	private VerticalPanel mainPanel;
	private Button clearBtn;
	private Button addBtn;
	private Label expandLabel;
	//private HashSet<FD> fds;
	
	private class FDEditorTextArea extends AttributeTextArea  {
		
		public void onDrop (DragContext context) {
			Widget w = context.draggable;
			if(w == null) return;
			
			if(w instanceof RelationAttributeWidget) {
				this.appendAttributes(((RelationAttributeWidget)w).getText());
			} else if (w instanceof FDWidget) {
				FDEditorWidget fdEdit = Main.get().getFdEditor();
				FDWidget fdw = (FDWidget) w;
				AttributeTextArea ata = fdEdit.getLeftTextArea();
				String[] fdAtt = fdw.getLeftSide();
				for (int i = 0; i < fdAtt.length; i++) {
					ata.appendAttributes(fdAtt[i]);
				}
				
				ata = fdEdit.getRightTextArea();
				fdAtt = fdw.getRightSide();
				for (int i = 0; i < fdAtt.length; i++) {
					ata.appendAttributes(fdAtt[i]);
				}
				
				if(fdw.isEditable()) {
					fdw.getParent().removeFromParent();
				}
			} else if (w instanceof RelationWidget) {
				RelationWidget rw = (RelationWidget) w;
				String[] att =  rw.getAttributes();
				for (int i = 0; i < att.length; i++) {
					this.appendAttributes(att[i]);
				}
			}
		}
	}
	
	public FDEditorWidget() {
		super();
		mainPanel = new VerticalPanel();
		initWidget(mainPanel);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(4);
		
		collapseButton = new Image("img/dw-collapse-but.png", 0, 15, 15, 15);
		CommonStyle.setCursorPointer(collapseButton);
		collapseButton.addClickListener(this);
		collapseButton.addMouseListener(new MouseAdapter(){
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
		});
		hp.add(collapseButton);
		
		hp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

		leftTA = new FDEditorTextArea();
		leftTA.setSize("160px", "70px");
		hp.add(leftTA);

		arrowImg = new Image("img/arrow-right-large.png");
		hp.add(arrowImg);
		
		rightTA = new FDEditorTextArea();
		rightTA.setSize("160px", "70px");
		hp.add(rightTA);
		
		hp.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		
		VerticalPanel vp = new VerticalPanel();
		infoButton = new Image("img/info.png");
		CommonStyle.setCursorPointer(infoButton);
		infoButton.addClickListener(this);
		vp.add(infoButton);
		addBtn = new Button("Add");
		addBtn.addClickListener(this);
		CommonStyle.setCursorPointer(addBtn);
		addBtn.setStyleName("fdew-btn");
		vp.add(addBtn);
		clearBtn = new Button("Clear");
		clearBtn.addClickListener(this);
		CommonStyle.setCursorPointer(clearBtn);
		clearBtn.setStyleName("fdew-btn");
		vp.add(clearBtn);
		
		hp.add(vp);
		
		expandLabel = new Label("Expand the FD editor");
		expandLabel.setStyleName("fdew-label");
		expandLabel.setVisible(false);
		hp.add(expandLabel);
		
		mainPanel.add(hp);
		
		PickupDragController dc = Main.get().getDragController();
		dc.registerDropController(leftTA);
		dc.registerDropController(rightTA);
	}
	
	public AttributeTextArea getLeftTextArea() {
		return leftTA;
	}
	
	public AttributeTextArea getRightTextArea() {
		return rightTA;
	}

	public void onClick(Widget sender) {
		super.onClick(sender);
		if (sender.equals(collapseButton)) {
			openClose();
		} else if (sender.equals(clearBtn)) {
			clearTextAreas();
		} else if (sender.equals(addBtn)) {
			FDWidget fd = createFD();
			addFDWidget(fd);
		} else if (sender.equals(infoButton)) {
			DialogBox dlg = new HelpDialog();
		    dlg.center();
		}
	}
	
	public Widget[] getUpControlls() {
		return checkControlls;
	}
	
	//TODO make to private after the demo
	void addFDWidget(FDWidget fd) {
		mainPanel.add(getCheckBoxPanel(fd));
	}
	
	private void openClose() {
		if (isOpen) {
			arrowImg.setVisible(false);
			leftTA.setVisible(false);
			rightTA.setVisible(false);
			collapseButton.setVisibleRect(15, 0, 15, 15);
			infoButton.setVisible(false);
			clearBtn.setVisible(false);
			addBtn.setVisible(false);
			expandLabel.setVisible(true);
		} else {
			arrowImg.setVisible(true);
			leftTA.setVisible(true);
			rightTA.setVisible(true);
			collapseButton.setVisibleRect(15, 15, 15, 15);
			infoButton.setVisible(true);
			clearBtn.setVisible(true);
			addBtn.setVisible(true);
			expandLabel.setVisible(false);
		}
		isOpen = !isOpen;
	}
	
	public void clearAll() {
		
	}
	
	private FDWidget createFD() {
		String[] l = leftTA.parseAttributes();
		String[] r = rightTA.parseAttributes();
		FDWidget fd = new FDWidget(true, l, r);
		clearTextAreas();
		return fd;
	}
	
	private void clearTextAreas() {
		leftTA.setText("");
		rightTA.setText("");
	}
}
