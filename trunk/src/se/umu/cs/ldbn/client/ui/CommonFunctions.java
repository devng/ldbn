package se.umu.cs.ldbn.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class with static methods for adding common style properties to Widgets.
 * 
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 */
public final class CommonFunctions {
	
	/**
	 * Set the transparent background colour of a widget
	 * 
	 * @param w Widget which will have a transparent background colour.
	 */
	public static void setBGTransparent(Widget w) {
		DOM.setStyleAttribute(w.getElement(), "background", "transparent");
	}
	
	/**
	 * Set a cursor of a widget to be a pointer, so when a mouse is over the 
	 * widget a pinter cursor is used. This is mainly used to indicate buttons.
	 * 
	 * @param w a Widget which will have a pinter as a cursor.
	 */
	public static void setCursorPointer(Widget w) {
		DOM.setStyleAttribute(w.getElement(), "cursor", "pointer");
	}
}
