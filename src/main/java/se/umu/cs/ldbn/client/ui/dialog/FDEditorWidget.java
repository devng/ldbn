package se.umu.cs.ldbn.client.ui.dialog;

import java.util.List;

import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.AttributeTextArea;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;
import se.umu.cs.ldbn.client.ui.sa.RelationAttributesWidget;
import se.umu.cs.ldbn.client.utils.Common;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class FDEditorWidget extends Composite
	implements ClickHandler {

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
			if (fdw.isEditable() && currnetFDHP != null &&
					currnetFDHP.containsFDWidget(fdw) && currentFDW == null) {
					currentFDW = fdw;
					fdw.getParent().setVisible(false);

			}
		}
	}
	private FDEditorTextArea leftTA;
	private FDEditorTextArea rightTA;
	private FDWidget currentFDW;
	private Image arrowImg;
	private HorizontalPanel mainPanel;
	private Button clearButton;
	private Button addButton;
	private FDHolderPanel currnetFDHP;

	private DomainTable currentDomain;

	public FDEditorWidget() {
		super();
		mainPanel = new HorizontalPanel();

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
		vp.add(new InfoButton("fdeditor"));
		addButton = new Button("Set/Add");
		addButton.addClickHandler(this);
		Common.setCursorPointer(addButton);
		addButton.setStyleName("fdew-btn");
		vp.add(addButton);
		clearButton = new Button("Clear");
		clearButton.addClickHandler(this);
		Common.setCursorPointer(clearButton);
		clearButton.setStyleName("fdew-btn");
		vp.add(clearButton);
		mainPanel.add(vp);

		initWidget(mainPanel);
	}

	public void clearText() {
		leftTA.setText("");
		rightTA.setText("");
	}

	public DomainTable getCurrentDomain() {
		return currentDomain;
	}

	public AttributeTextArea getLeftTextArea() {
		return leftTA;
	}

	public AttributeTextArea getRightTextArea() {
		return rightTA;
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		FDEditorDialog.get().setErrorMsg("");
		if (sender.equals(clearButton)) {
			clearTextAreas();
		} else if (sender.equals(addButton)) {
			FDWidget fdw = createFD();
			if(fdw == null) {
				FDEditorDialog.get().setErrorMsg("LHS or RHS of the FD has no valid attributes.");
				return;
			}
			if (leftTA.hasOmittedAttributes() || rightTA.hasOmittedAttributes()) {
				FDEditorDialog.get().setErrorMsg("Some attributes had invalid names.");
			}

			if(currentFDW != null && fdw.getFD().equals(currentFDW.getFD())) {
				handleCurrentFDWidget();
				return;
			}

			if(currentFDW != null && currnetFDHP != null) {
				currnetFDHP.removeFDWidget(currentFDW);
			}
			currentFDW = null;

			addFDWidget(fdw);
		}
	}

	public void setCurrentDomain(DomainTable currentDomain) {
		this.currentDomain = currentDomain;
	}

	public void setCurrentFDHolderPanel(FDHolderPanel fdHP) {
		currnetFDHP = fdHP;
	}

	public void setFDWidtet(FDWidget fdw) {
		leftTA.setFDWidget(fdw);
	}

	/**
	 * This is used by FDEditorDialog, in order to restore the initial
	 * FDWidget if the "Close" Button is hit without setting or modifying the
	 * initial widget.
	 */
	void handleCurrentFDWidget() {
		if(currentFDW != null) {
			currentFDW.getParent().setVisible(true);
		}
		currentFDW = null;
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

	private void clearTextAreas() {
		leftTA.setText("");
		rightTA.setText("");
	}

	/** returns null if the size of LHS or RHS is < 1 */
	private FDWidget createFD() {
		FD fd = new FD(currentDomain);
		List<String> l = leftTA.parseAttributes();
		if(l.size() < 1) {
			return null;
		}
		boolean isAnythinInserted = false;
		for (String aL : l) {
			if (fd.getLHS().addAtt(aL)) {
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
		for (String aR : r) {
			if (fd.getRHS().addAtt(aR)) {
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
}
