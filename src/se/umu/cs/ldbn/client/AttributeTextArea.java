package se.umu.cs.ldbn.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VetoDropException;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is an abstract class for a text area which is also a drop target. 
 * The classes which inherit this class must implement the onDrop() method, to
 * provide meaningful behaviour for the widget. 
 * The text area should only be used to manipulate relational attributes, so
 * there a methods for retrieving, adding and validating user input. 
 * 
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 *
 */
public abstract class AttributeTextArea extends TextArea 
	implements DropController {

	/**
	 * A Default constructor - it only calls the super constructor.
	 */
	public AttributeTextArea() {
		super();
	}
	
	/**
	 * Append an attribute, which is represented as a string, to the text area.
	 * All attributes should be divided by comma, so if it necessary a comma is
	 * also added before the attribute.
	 * 
	 * @param text a relational attribute.
	 */
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

	/**
	 * Parse the text in the text area and returns all the attributes as a 
	 * string array. 
	 *  
	 * @return  returns all the attributes contained in the text area.
	 */
	protected String[] parseAttributes() {
		String s = this.getText();
		String[] r = s.split(",");
		for (int i = 0; i < r.length; i++) {
			r[i].trim();
		}
		return r;
	}

	/**
	 * Retrieve our drop target widget.
	 * 
	 * @return this instance since the class implements the DropController 
	 * interface, and it is a drop target.
	 * 
	 * @see DropController
	 */
	public Widget getDropTarget() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.DropController#onDrop(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	public abstract void onDrop(DragContext context);
	

	/*
	 * (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.DropController#onEnter(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	public void onEnter(DragContext context) {
		getDropTarget().addStyleName("fdew-dc-on-enter");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.DropController#onLeave(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	public void onLeave(DragContext context) {
		getDropTarget().removeStyleName("fdew-dc-on-enter");
	}

	/* NOT USED METHODS*/
	
	/** not used. */
	public void onMove(DragContext context) {
	}
	/** not used. */
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
