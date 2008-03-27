package se.umu.cs.ldbn.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VetoDropException;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public abstract class AttributeTextArea extends TextArea 
	implements DropController {

	
	public AttributeTextArea() {
		super();
		
	}
	
	public void appendAttributes(String text) {
		String s = this.getText();
		s = s.trim();
		if (s.length() == 0) {
			this.setText(text);
			return;
		} else if (s.charAt(s.length() - 1) != ',') {
			s += ", " + text;
		} else {
			s += " " + text;
		}
		this.setText(s);
	}

	protected String[] parseAttributes() {
		String s = this.getText();
		String[] r = s.split(",");
		for (int i = 0; i < r.length; i++) {
			r[i].trim();
		}
		return r;
	}

	public Widget getDropTarget() {
		return this;
	}

	public abstract void onDrop(DragContext context);
	

	public void onEnter(DragContext context) {
		getDropTarget().addStyleName("fdew-dc-on-enter");
	}
	
	public void onLeave(DragContext context) {
		getDropTarget().removeStyleName("fdew-dc-on-enter");
	}

	/* NOT USED METHODS*/
	
	public void onMove(DragContext context) {
	}
	
	public void onPreviewDrop(DragContext context) throws VetoDragException {
	}

	
	/* DEPRECATED METHODS */
	
	/**
	 * @deprecated Use {@link #onDrop(DragContext)} and {@link #onLeave(DragContext)} instead.
	 */
	public DragEndEvent onDrop(Widget reference, Widget draggable,
			DragController dragController) {
		return null;
	}

	/**
	 * @deprecated Use {@link #onEnter(DragContext)} instead.
	 */
	public void onEnter(Widget reference, Widget draggable,
			DragController dragController) {
	}

	/**
	 * @deprecated Used {@link #onLeave(DragContext)} instead.
	 */
	public void onLeave(Widget draggable, DragController dragController) {
	}

	/**
	 * @deprecated Used {@link #onLeave(DragContext)} instead.
	 */
	public void onLeave(Widget reference, Widget draggable,
			DragController dragController) {
	}

	/**
	 * @deprecated Use {@link #onMove(DragContext)} instead.
	 */
	public void onMove(Widget reference, Widget draggable,
			DragController dragController) {
	}

	/**
	 * @deprecated Use {@link #onMove(DragContext)} instead.
	 */
	public void onMove(int x, int y, Widget reference, Widget draggable,
			DragController dragController) {
	}

	/**
	 * @deprecated Use {@link #onPreviewDrop(DragContext)} instead.
	 */
	public void onPreviewDrop(Widget reference, Widget draggable,
			DragController dragController) throws VetoDropException {
	}
}
