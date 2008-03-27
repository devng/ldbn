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

public abstract class CheckBoxWidget extends Composite implements ClickListener{
	
	protected Map checkBoxes;
	protected Image[] checkControlls;

	public CheckBoxWidget() {
		super();
		checkBoxes = new HashMap();
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
	
	public void onClick(Widget sender) {
		if (sender.equals(checkControlls[0])) {
			selectAll();
		} else if (sender.equals(checkControlls[1])) {
			selectNone();
		} else if (sender.equals(checkControlls[2])) {
			deleteSelected();
		}
	}
	
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
	
	protected void selectAll() {
		Set keys = checkBoxes.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = (CheckBox) iter.next();
			chBox.setChecked(true);
		}
	}
	
	protected void selectNone() {
		Set keys = checkBoxes.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = (CheckBox) iter.next();
			chBox.setChecked(false);
		}
	}
	
	protected void deleteSelected() {
		Set keys = checkBoxes.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			CheckBox chBox = (CheckBox) iter.next();
			if(chBox.isChecked()) {
				chBox.getParent().removeFromParent();
				iter.remove();
			}
		}
	}
	
	protected Widget[] getCheckBoxControlls() {
		return checkControlls;
	}

}
