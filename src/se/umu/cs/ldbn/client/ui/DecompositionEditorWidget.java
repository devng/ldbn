package se.umu.cs.ldbn.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.Relation;

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
public final class DecompositionEditorWidget extends CheckBoxWidget 
	implements HasUpControlls {
	
	private VerticalPanel mainPanel;
	private DecompositionTextArea textArea;
	private Image collapseButton;
	private Image infoButton;
	private Button clearBtn;
	private Button addBtn;
	private boolean isOpen;
	private Label expandLabel;
	private HashSet<RelationWidget> relations;
	
	private final class DecompositionTextArea extends AttributeTextArea {

		public void onDrop(DragContext context) {
			Widget w = context.draggable;
			if(w == null) return;
			
			if(w instanceof RelationAttributeWidget) {
				this.appendAttributes(((RelationAttributeWidget)w).getText());
			} else if (w instanceof FDWidget) {
				FDWidget fdw = (FDWidget) w;
				List<String> att = fdw.getFD().getLHS().getAttributeNames();
				for (String str : att) {
					this.appendAttributes(str);
				}
				att = fdw.getFD().getRHS().getAttributeNames();
				for (String str : att) {
					this.appendAttributes(str);
				}
			} else if (w instanceof RelationWidget) {
				RelationWidget rw = (RelationWidget) w;
				List<String> names =  rw.getRelation().getAttrbutes().getAttributeNames();
				for (String str : names) {
					this.appendAttributes(str);
				}
				if(checkBoxes.containsValue(rw)) {
					rw.getParent().removeFromParent();
					relations.remove(rw);
				}
			}
		}
	}
	
	public DecompositionEditorWidget() {
		super();
		isOpen = true;
		relations = new HashSet<RelationWidget>();
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
		AttributeSet atts = new AttributeSet(Main.get().getAttributeNameTable(),
				textArea.parseAttributes());
		Relation r = new Relation(atts);
		RelationWidget rw = new RelationWidget(r);
		relations.add(rw);
		mainPanel.add(getCheckBoxPanel(rw));
	}

	public Widget[] getUpControlls() {
		return checkControlls;
	}
	
	public void clearAll() {
		for (Iterator<RelationWidget> iter = relations.iterator(); iter.hasNext();) {
			RelationWidget rw = iter.next();
			rw.getParent().removeFromParent();
			iter.remove();
		}
		textArea.setText("");
	}
	
	public List<Relation> getRelations() {
		ArrayList<Relation> r = new ArrayList<Relation>(relations.size());
		for (RelationWidget rw : relations) {
			r.add(rw.getRelation());
		}
		return r;
	}
}
