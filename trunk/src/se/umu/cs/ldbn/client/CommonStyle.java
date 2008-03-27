package se.umu.cs.ldbn.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class CommonStyle {
	
	public static void setBGTransparent(Widget w) {
		DOM.setStyleAttribute(w.getElement(), "background", "transparent");
	}
	
	public static void setCursorPointer(Widget w) {
		DOM.setStyleAttribute(w.getElement(), "cursor", "pointer");
	}
}
