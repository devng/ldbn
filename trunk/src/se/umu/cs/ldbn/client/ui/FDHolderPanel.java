package se.umu.cs.ldbn.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.DomainTableListener;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class FDHolderPanel extends VerticalPanel 
	implements ClickListener, MouseListener, HasAdditionalControlls, 
		DomainTableListener {
	
	private class EditButton extends Image {
		public EditButton() {
			super("img/edit-big.png", 0, 0, 20, 20);
			CommonFunctions.setCursorPointer(this);
		}
	}

	private HashSet<FDWidget> fds;
	/**
	 * Maps all the check boxes to their corresponding widgets. The keys are
	 * instances of the CheckBox class and values are instances of the widget 
	 * class.
	 */
	private Map<CheckBox, FDWidget> checkBoxes;
	private Map<EditButton, CheckBox> buttons;
	/**
	 * Array of images used as buttons, which are used as controls for 
	 * selection and deletion of all the check boxes.
	 */
	private Image[] checkControlls;
	
	private List<FDHolderPanelListener> listeners;
	
	public FDHolderPanel() {
		super();
		listeners = new ArrayList<FDHolderPanelListener>();
		fds = new HashSet<FDWidget>();
		checkBoxes = new HashMap<CheckBox, FDWidget>();
		buttons = new HashMap<EditButton, CheckBox>();
		checkControlls = new Image[3];
		checkControlls = new Image[3];
		checkControlls[0] = new Image("img/check-box.png", 0, 0, 15, 15);
		CommonFunctions.setCursorPointer(checkControlls[0]);
		checkControlls[0].setTitle("Select all");
		checkControlls[0].addClickListener(this);
		checkControlls[1] = new Image("img/check-box.png", 0, 15, 15, 15);
		CommonFunctions.setCursorPointer(checkControlls[1]);
		checkControlls[1].setTitle("Select none");
		checkControlls[1].addClickListener(this);
		checkControlls[2] = new Image("img/bin.png", 0, 0, 15, 15);
		checkControlls[2].setTitle("Delete selected");
		checkControlls[2].addMouseListener(new MouseAdapter() {
			public void onMouseEnter(Widget sender) {
				checkControlls[2].setVisibleRect(15, 0, 15, 15);
			}

			public void onMouseLeave(Widget sender) {
				checkControlls[2].setVisibleRect(0, 0, 15, 15);
			}
		});
		CommonFunctions.setCursorPointer(checkControlls[2]);
		checkControlls[2].addClickListener(this);
	}
	
	public void addListener(FDHolderPanelListener l) {
		if(l == null) return;
		listeners.add(l);
	}
	
	public void removeListener(FDHolderPanelListener l) {
		if(l == null) return;
		listeners.remove(l);
	}
	
	public void clearData() {
		for (Iterator<FDWidget> iter = fds.iterator(); iter.hasNext();) {
			FDWidget fdw = iter.next();
			fdw.getParent().removeFromParent();
			iter.remove();
		}
		for (FDHolderPanelListener l : listeners) {
			l.allFDsRemoved();
		}
	}
	
	public List<FD> getFDs() {
		ArrayList<FD> r = new ArrayList<FD>();
		for (FDWidget fdw : fds) {
			r.add(fdw.getFD());
		}
		return r;
	} 
	
	
	
	public boolean removeFDWidget(FDWidget fdw) {
		if(fds.remove(fdw)) {
			fdw.getParent().removeFromParent();
			for (FDHolderPanelListener l : listeners) {
				l.fdRemoved(fds); 
			}
			return true;
		}
		return false;
	}
	
	public void addFDWidget(FDWidget fd) {
		add(getCheckBoxPanel(fd));
		fds.add(fd);
		for (FDHolderPanelListener l : listeners) {
			l.fdAdded(fds);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
	 */
	public void onClick(Widget sender) {
		if (sender.equals(checkControlls[0])) {
			selectAll();
		} else if (sender.equals(checkControlls[1])) {
			selectNone();
		} else if (sender.equals(checkControlls[2])) {
			deleteSelected();
		} else if (sender instanceof EditButton){
			EditButton but = (EditButton) sender;
			CheckBox chBox = buttons.get(but);
			if (chBox != null) {
				FDWidget fdw = checkBoxes.get(chBox);
				if (fdw != null) {
					FDEditorDialog fded = FDEditorDialog.get();
					fded.center(); //always center first
					fded.getFDEditorWidget().clearText();
					fded.getFDEditorWidget().setCurrentFDHolderPanel(this);
					fded.setCurrentDomain(fdw.getFD().getLHS().domain());
					fded.getFDEditorWidget().setFDWidtet(fdw);
				} else {
					Log.error("FDHolderPanel : FDwidget is null");
				}
			} else {
				Log.error("FDHolderPanel : check box is null");
			}
		}
	}
	
	/**
	 * Select all the check boxes.
	 */
	public void selectAll() {
		Set<CheckBox> keys = checkBoxes.keySet();
		for (Iterator<CheckBox> iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = iter.next();
			chBox.setChecked(true);
		}
	}
	
	/**
	 * Removes any selection from the check boxes.
	 */
	public void selectNone() {
		Set<CheckBox> keys = checkBoxes.keySet();
		for (Iterator<CheckBox> iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = iter.next();
			chBox.setChecked(false);
		}
	}
	
	/**
	 * Removes the check boxes and the widgets associated with them.
	 */
	public void deleteSelected() {
		Set<CheckBox> keys = checkBoxes.keySet();
		for (Iterator<CheckBox> iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = iter.next();
			if(chBox.isChecked()) {
				chBox.getParent().removeFromParent();
				fds.remove(checkBoxes.get(chBox));
				iter.remove();
			}
		}
		for (FDHolderPanelListener l : listeners) {
			l.fdRemoved(fds); 
		}
	}
	
	/**
	 * Returns the buttons for manipulating the check boxes, such as 'select 
	 * all', 'select none\ and 'delete all'. This controlls are added to the 
	 * DisclosureWidget.
	 *  
	 * @return widgets for manipulating the check boxes.
	 */
	public Widget[] getCheckBoxControlls() {
		return checkControlls;
	}

	public void onMouseEnter(Widget sender) {
		EditButton edit = (EditButton) sender;
		edit.setVisibleRect(20, 0, 20, 20);
	}

	public void onMouseLeave(Widget sender) {
		EditButton edit = (EditButton) sender;
		edit.setVisibleRect(0, 0, 20, 20);
	}

	public void onMouseDown(Widget sender, int x, int y) {}
	
	public void onMouseMove(Widget sender, int x, int y) {}

	public void onMouseUp(Widget sender, int x, int y) {}
	
	/**
	 * Returns the buttons for manipulating the check boxes, such as 'select 
	 * all', 'select none\ and 'delete all'. This controlls are added to the 
	 * DisclosureWidget.
	 *  
	 * @return widgets for manipulating the check boxes.
	 */
	public Widget[] getAdditionalControlls() {
		return checkControlls;
	}
	
	public void onDomainChange() {
		for (FDWidget fdw : fds) {
			fdw.recalculateMask();
		}
	}
	
	/**
	 * Creates a panel, which consists of a check box and a widget and then adds
	 * both to the map. 
	 * 
	 * @param w a widget which will be mapped to a check box 
	 * @return a panel containing the widget and a check box.
	 */
	private Panel getCheckBoxPanel(FDWidget w) {
		PickupDragController dc = Main.get().getDragController();
		dc.makeDraggable(w);
		CheckBox chBox = new CheckBox();
		CommonFunctions.setCursorPointer(chBox);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		hp.add(chBox);
		hp.add(w);
		EditButton edit = new EditButton();
		edit.addClickListener(this);
		edit.addMouseListener(this);
		hp.add(edit);
		checkBoxes.put(chBox, w);
		buttons.put(edit, chBox);
		return hp;
	}
}
