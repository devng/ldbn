package se.umu.cs.ldbn.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 */
public class DecompositionEditorWidget extends CheckBoxWidget 
	implements HasUpControlls {
	
	private VerticalPanel mainPanel;
	private DecompositionTextArea textArea;
	private Image collapseButton;
	private Image infoButton;
	private Button clearBtn;
	private Button addBtn;
	private boolean isOpen = true;
	private Label expandLabel;
	
	private class DecompositionTextArea extends AttributeTextArea {

		public void onDrop(DragContext context) {
			Widget w = context.draggable;
			if(w == null) return;
			
			if(w instanceof RelationAttributeWidget) {
				this.appendAttributes(((RelationAttributeWidget)w).getText());
			} else if (w instanceof FDWidget) {
				FDWidget fdw = (FDWidget) w;
				String[] fdAtt = fdw.getLeftSide();
				for (int i = 0; i < fdAtt.length; i++) {
					this.appendAttributes(fdAtt[i]);
				}
				
				fdAtt = fdw.getRightSide();
				for (int i = 0; i < fdAtt.length; i++) {
					this.appendAttributes(fdAtt[i]);
				}
			} else if (w instanceof RelationWidget) {
				RelationWidget rw = (RelationWidget) w;
				String[] att =  rw.getAttributes();
				for (int i = 0; i < att.length; i++) {
					this.appendAttributes(att[i]);
				}
				if(checkBoxes.containsValue(rw)) {
					rw.getParent().removeFromParent();
				}
			}
		}
	}
	
	public DecompositionEditorWidget() {
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
		
		textArea = new DecompositionTextArea();
		textArea.setSize("300px", "70px");
		PickupDragController dc = Main.get().getDragController();
		dc.registerDropController(textArea);
		hp.add(textArea);
		
		VerticalPanel vp = new VerticalPanel();
		infoButton = new Image("img/info.png");
		CommonStyle.setCursorPointer(infoButton);
		infoButton.addClickListener(this);
		vp.add(infoButton);
		addBtn = new Button("Add");
		addBtn.setStyleName("dew-btn");
		addBtn.addClickListener(this);
		vp.add(addBtn);
		clearBtn = new Button("Clear");
		clearBtn.setStyleName("dew-btn");
		clearBtn.addClickListener(this);
		vp.add(clearBtn);
		hp.add(vp);
		
		expandLabel = new Label("Expand the decomposition editor");
		expandLabel.setStyleName("dew-label");
		expandLabel.setVisible(false);
		hp.add(expandLabel);
		
		mainPanel.add(hp);
	}

	public void onClick(Widget sender) {
		super.onClick(sender);
		if (sender.equals(collapseButton)) {
			openClose();
		} else if (sender.equals(infoButton)) {
			DialogBox dlg = new HelpDialog();
		    dlg.center();
		} else if (sender.equals(addBtn)) {
			addRelation();
			textArea.setText("");
		} else if (sender.equals(clearBtn)) {
			textArea.setText("");
		}
	}
	
	private void openClose() {
		if (isOpen) {
			collapseButton.setVisibleRect(15, 0, 15, 15);
			textArea.setVisible(false);
			infoButton.setVisible(false);
			clearBtn.setVisible(false);
			addBtn.setVisible(false);
			expandLabel.setVisible(true);
		} else {
			collapseButton.setVisibleRect(15, 15, 15, 15);
			textArea.setVisible(true);
			infoButton.setVisible(true);
			clearBtn.setVisible(true);
			addBtn.setVisible(true);
			expandLabel.setVisible(false);
		}
		isOpen = !isOpen;
	}
	
	private void addRelation() {
		String[] att = textArea.parseAttributes();
		RelationWidget rw = new RelationWidget(att);
		mainPanel.add(getCheckBoxPanel(rw));
	}

	public Widget[] getUpControlls() {
		return checkControlls;
	}
}
