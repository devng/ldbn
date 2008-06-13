package se.umu.cs.ldbn.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.AttributeSetIterator;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.core.Relation;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
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
public final class KeyEditorWidget extends Composite
	implements ClickListener {
	
	private HorizontalPanel mainPanel;
	private KeyTextArea textArea;
	private Image infoButton;
	private Button clearBtn;
	private Button addBtn;
	private RelationAttributesWidget raw = null;
	
	private final class KeyTextArea extends AttributeTextArea {

		public void onDrop(DragContext context) {
			Widget w = context.draggable;
			if(w == null) return;
			
			if(w instanceof SingleAttributeWidget) {
				this.appendAttributes(((SingleAttributeWidget)w).getText());
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
			} else if (w instanceof RelationAttributesWidget) {
				RelationAttributesWidget rw = (RelationAttributesWidget) w;
				List<String> names =  rw.getAttributes().getAttributeNames();
				for (String str : names) {
					this.appendAttributes(str);
				}
			}
		}
		
		public void setAttributeSet(AttributeSet att) {
			AttributeSetIterator asi = att.iterator();
			for (; asi.hasNext();) {
				String name = asi.next();
				this.appendAttributes(name);
			}
		}
	}
	
	public KeyEditorWidget() {
		super();
		mainPanel = new HorizontalPanel();
		
		mainPanel.setSpacing(4);
		
		textArea = new KeyTextArea();
		textArea.setSize("200px", "70px");
		PickupDragController dc = Main.get().getDragController();
		dc.registerDropController(textArea);
		mainPanel.add(textArea);
		
		VerticalPanel vp = new VerticalPanel();
		infoButton = new Image("img/info.png");
		CommonFunctions.setCursorPointer(infoButton);
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
		mainPanel.add(vp);
		
		initWidget(mainPanel);
	}

	void setCurrentRelationAttributesWidget(RelationAttributesWidget raw) {
		this.raw = raw;
		textArea.setAttributeSet(raw.getKey());
	}
	
	public void clearText() {
		textArea.setText("");
	}
	
	public void onClick(Widget sender) {
		if (sender.equals(infoButton)) {
			DialogBox dlg = new HelpDialog();
		    dlg.center();
		} else if (sender.equals(addBtn)) {
			if(raw == null) {
				textArea.setText("");
				return;
			}
			String[] atts = textArea.parseAttributes();
			AttributeSet as = new AttributeSet(Main.get().getAttributeNameTable());
			for (String str : atts) {
				as.addAtt(str);
			}
			raw.setKey(as);
			textArea.setText("");
		} else if (sender.equals(clearBtn)) {
			textArea.setText("");
		}
	}
	
	public void registerAsDropController() {
		Main.get().getDragController().registerDropController(textArea);
	}
	
	public void unregisterAsDropController() {
		Main.get().getDragController().unregisterDropController(textArea);
	}
}
