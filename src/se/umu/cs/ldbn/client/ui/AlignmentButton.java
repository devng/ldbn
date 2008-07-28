package se.umu.cs.ldbn.client.ui;

import java.util.Collection;
import java.util.HashSet;

import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class AlignmentButton extends Image implements MouseListener, 
	ClickListener{
	
	private boolean isHorizontal;
	private Collection<AlignmentButtonListener> listeners;
	
	public AlignmentButton(boolean isHorizontal) {
		super("img/alingment.png");
		setTitle("Align horizontal / vertical");
		this.isHorizontal = isHorizontal;
		if(isHorizontal) {
			setVisibleRect(0, 0, 15, 15);
		} else {
			setVisibleRect(0, 15, 15, 15);
		}
		addMouseListener(this);
		addClickListener(this);
		Common.setCursorPointer(this);
		listeners = new HashSet<AlignmentButtonListener>();
	}
	
	public void addListener(AlignmentButtonListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}
	
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public void onClick(Widget sender) {
		if (isHorizontal) {
			isHorizontal = false;
			setVisibleRect(15, 15, 15, 15);
		} else {
			isHorizontal = true;
			setVisibleRect(15, 0, 15, 15);
		}
		notifyListeners(isHorizontal);
		
	}

	public void onMouseDown(Widget sender, int x, int y) {}

	
	public void onMouseEnter(Widget sender) {
		if(isHorizontal) {
			setVisibleRect(15, 0, 15, 15);
		} else {
			setVisibleRect(15, 15, 15, 15);
		}
	}
	
	public void onMouseLeave(Widget sender) {
		if(isHorizontal) {
			setVisibleRect(0, 0, 15, 15);
		} else {
			setVisibleRect(0, 15, 15, 15);
		}
	}

	public void onMouseMove(Widget sender, int x, int y) {}

	
	public void onMouseUp(Widget sender, int x, int y) {}

	
	public void removeListener(AlignmentButtonListener l) {
		if (l != null) {
			listeners.remove(l);
		}
	}
	
	private void notifyListeners(boolean isHorizontal) {
		for (AlignmentButtonListener abl : listeners) {
			abl.onAlignmentChanged(isHorizontal);
		}
	}

}
