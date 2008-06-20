package se.umu.cs.ldbn.client;

import se.umu.cs.ldbn.client.io.LdbnParser;
import se.umu.cs.ldbn.client.io.LdbnParser.LDBN_TYPE;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class with static methods for adding common style properties to Widgets,
 * and checking the response of a server.
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
	
	/**
	 * Check a response, returned by the server. The following checks are 
	 * being performed:
	 * If the status code return by the server is ok (200).
	 * If the returned xml is a valid LDBN xml.
	 * If the returned LDBN xml is with type message, if so the message is 
	 * displayed, unless the message has type = OK.
	 * Please note that u don't have to parse the response with the LdbnParser
	 * arter calling this method. Just call LdbnParser.get() and the method you
	 * desire. 
	 * @param response a request response.
	 * @return true if no errors are returned by the server.
	 */
	public static boolean checkResponse(Response response) {
		if(response.getStatusCode() != 200) {
			Window.alert("The server returned a " + 
					response.getStatusCode() + " error code.");
			return false;
		}
		LdbnParser p = LdbnParser.get();
		LdbnParser.LDBN_TYPE type = p.parse(response.getText());
		if (type == LDBN_TYPE.unknown) {
			Window.alert("The server returned an unknown XML responce.");
			return false;
		}
		if (type == LDBN_TYPE.msg && p.getMsgType() != LdbnParser.MSG_TYPE.ok) {
			Window.alert(p.getMsgText());
			return false;
		}
		
		return true;
	}
}
