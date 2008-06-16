package se.umu.cs.ldbn.client.ui;

import java.util.List;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.FD;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
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
	private Button clearBtn;
	private Button addBtn;
	private FDHolderPanel currnetFDHP = null;
	
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
			FDEditorWidget fdEdit = Main.get()
				.getFDEditorWidget();
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
		addBtn = new Button("Add");
		addBtn.addClickListener(this);
		CommonFunctions.setCursorPointer(addBtn);
		addBtn.setStyleName("fdew-btn");
		vp.add(addBtn);
		clearBtn = new Button("Clear");
		clearBtn.addClickListener(this);
		CommonFunctions.setCursorPointer(clearBtn);
		clearBtn.setStyleName("fdew-btn");
		vp.add(clearBtn);
		mainPanel.add(vp);
		
		PickupDragController dc = Main.get().getDragController();
		dc.registerDropController(leftTA);
		dc.registerDropController(rightTA);
		
		initWidget(mainPanel);
	}
	
	public AttributeTextArea getLeftTextArea() {
		return leftTA;
	}
	
	public AttributeTextArea getRightTextArea() {
		return rightTA;
	}

	public void onClick(Widget sender) {
		if (sender.equals(clearBtn)) {
			clearTextAreas();
		} else if (sender.equals(addBtn)) {
			FDWidget fd = createFD();
			addFDWidget(fd);
		} else if (sender.equals(infoButton)) {
		    dlg.center();
		}
	}

	void setCurrentFDHolderPanel(FDHolderPanel fdHP) {
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
	
	private FDWidget createFD() {
		FD fd = new FD(Main.get().getAttributeNameTable());
		String[] l = leftTA.parseAttributes();
		for (int i = 0; i < l.length; i++) {
			fd.getLHS().addAtt(l[i]);
		}
		String[] r = rightTA.parseAttributes();
		for (int i = 0; i < r.length; i++) {
			fd.getRHS().addAtt(r[i]);
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
