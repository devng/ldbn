package se.umu.cs.ldbn.client.ui;

import java.util.Collection;
import java.util.HashSet;

import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;


public class AlignmentButton extends Image implements MouseOverHandler, 
		MouseOutHandler, ClickHandler {
	
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
		addMouseOutHandler(this);
		addMouseOverHandler(this);
		addClickHandler(this);
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
	
	@Override
	public void onClick(ClickEvent event) {
		if (isHorizontal) {
			isHorizontal = false;
			setVisibleRect(15, 15, 15, 15);
		} else {
			isHorizontal = true;
			setVisibleRect(15, 0, 15, 15);
		}
		notifyListeners(isHorizontal);
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		if(isHorizontal) {
			setVisibleRect(15, 0, 15, 15);
		} else {
			setVisibleRect(15, 15, 15, 15);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		if(isHorizontal) {
			setVisibleRect(0, 0, 15, 15);
		} else {
			setVisibleRect(0, 15, 15, 15);
		}
	}
	
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
