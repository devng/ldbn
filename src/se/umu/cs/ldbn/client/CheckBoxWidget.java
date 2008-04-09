package se.umu.cs.ldbn.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget which consists of many check boxes, which on the other hand are 
 * linked to other widgets. It provides means to select all the check boxes and 
 * to remove them from their parent widget. This class is used by the 
 * FDEditorWidget and by the DecompositionEditorWidget class. 
 * to access the 
 * 
 * @author @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 */
public class CheckBoxWidget extends Composite implements ClickListener{
	
	/**
	 * Maps all the check boxes to their corresponding widgets. The keys are
	 * instances of the CheckBox class and values are instances of the widget 
	 * class.
	 */
	protected Map<CheckBox, Widget> checkBoxes;
	/**
	 * Array of images used as buttons, which are used as controls for 
	 * selection and deletion of all the check boxes.
	 */
	protected Image[] checkControlls;

	/**
	 * Constructor which instantiates the map, and the control buttons.
	 */
	public CheckBoxWidget() {
		super();
		checkBoxes = new HashMap<CheckBox, Widget>();
		checkControlls = new Image[3];
		checkControlls = new Image[3];
		checkControlls[0] = new Image("img/check-box.png", 0, 0, 15, 15);
		CommonStyle.setCursorPointer(checkControlls[0]);
		checkControlls[0].setTitle("Select all");
		checkControlls[0].addClickListener(this);
		checkControlls[1] = new Image("img/check-box.png", 0, 15, 15, 15);
		CommonStyle.setCursorPointer(checkControlls[1]);
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
		CommonStyle.setCursorPointer(checkControlls[2]);
		checkControlls[2].addClickListener(this);
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
		}
	}
	
	/**
	 * Creates a panel, which consists of a check box and a widget and then adds
	 * both to the map. 
	 * 
	 * @param w a widget which will be mapped to a check box 
	 * @return a panel containing the widget and a check box.
	 */
	protected Panel getCheckBoxPanel(Widget w) {
		PickupDragController dc = Main.get().getDragController();
		dc.makeDraggable(w);
		CheckBox chBox = new CheckBox();
		CommonStyle.setCursorPointer(chBox);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		hp.add(chBox);
		hp.add(w);
		checkBoxes.put(chBox, w);
		return hp;
	}
	
	/**
	 * Select all the check boxes.
	 */
	protected void selectAll() {
		Set<CheckBox> keys = checkBoxes.keySet();
		for (Iterator<CheckBox> iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = iter.next();
			chBox.setChecked(true);
		}
	}
	
	/**
	 * Removes any selection from the check boxes.
	 */
	protected void selectNone() {
		Set<CheckBox> keys = checkBoxes.keySet();
		for (Iterator<CheckBox> iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = iter.next();
			chBox.setChecked(false);
		}
	}
	
	/**
	 * Removes the check boxes and the widgets associated with them.
	 */
	protected void deleteSelected() {
		Set<CheckBox> keys = checkBoxes.keySet();
		for (Iterator<CheckBox> iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = iter.next();
			if(chBox.isChecked()) {
				chBox.getParent().removeFromParent();
				iter.remove();
			}
		}
	}
	
	/**
	 * Returns the buttons for manipulating the check boxes, such as 'select 
	 * all', 'select none\ and 'delete all'. This controlls are added to the 
	 * DisclosureWidget.
	 *  
	 * @return widgets for manipulating the check boxes.
	 */
	protected Widget[] getCheckBoxControlls() {
		return checkControlls;
	}

}
