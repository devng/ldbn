package se.umu.cs.ldbn.client.utils;

import java.util.ArrayList;
import java.util.Collection;

import se.umu.cs.ldbn.client.core.Relation;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.io.LdbnParser;
import se.umu.cs.ldbn.client.io.LdbnParser.LDBN_TYPE;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class with static methods for common function such as 
 * style properties to Widgets, checking the response of a server, regular
 * expression strings and others.
 */
public final class Common {
	
	public final static int MIN_PASSWORD_LENGTH = 4; 
	/*
	 * regular expressions for validating user data, peace not if you change
	 * the expressions here you have to change them in the PHP scripts as well.
	 */
	public final static String NAME_REGEX = "([\\w]|\\-){1,20}";
	
	public final static String EMAIL_REGEX = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,10}"; 
	
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
	
	public static Label createCursorLabel(String labelText) {
		Label l = new Label(labelText);
		setCursorPointer(l);
		return l;
	}
	
	public static HTML createCursorHTML(String html) {
		HTML l = new HTML(html);
		setCursorPointer(l);
		return l;
	}
	
	/**
	 * Check a response, returned by the server. The following checks are 
	 * being performed:
	 * If the status code return by the server is ok (200).
	 * If the returned xml is a valid LDBN xml.
	 * If the returned LDBN xml is with type message, if so the message is 
	 * displayed, unless the message has type = OK.
	 * Please note that u don't have to parse the response with the LdbnParser
	 * after calling this method. Just call LdbnParser.get() and the method you
	 * desire. 
	 * @param response a request response.
	 * @return true if no errors are returned by the server.
	 */
	public static boolean checkResponse(Response response) {
		if(response.getStatusCode() != 200) {
			Window.alert(I18N.messages().serverErrorCode(String.
					valueOf(response.getStatusCode())));
			return false;
		}
		String responce = response.getText();
		return checkResponceTextOnly(responce);
	}
	
	public static boolean checkResponceTextOnly(String responce) {
		System.out.println("-----------------------Server response-----------------------------");
		System.out.println(responce);
		System.out.println("---------------------End server response---------------------------");
		LdbnParser p = LdbnParser.get();
		LdbnParser.LDBN_TYPE type = p.parse(responce);
		if (type == LDBN_TYPE.unknown) {
			Window.alert(I18N.constants().serverErrorUnknownXML());
			return false;
		}
		if (type == LDBN_TYPE.msg && p.getMsgType() != LdbnParser.MSG_TYPE.ok) {
			Window.alert(p.getMsgText());
			return false;
		}
		
		return true;
	}
	
	public static Collection<Relation> deepCopyDecomposition(Collection<Relation> decomposition) {
		ArrayList<Relation> dc = new ArrayList<Relation>(decomposition.size());
		for (Relation r : decomposition) {
			if(r != null) {
				dc.add(r.clone());
			}
		}
		return dc;
	}
	
	public static String escapeHTMLCharacters(String str) {
		final StringBuilder result = new StringBuilder();
		char[] chars = new char[str.length()];
		str.getChars(0, str.length(), chars, 0);
		for (char character : chars) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '&') {
				result.append("&amp;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '(') {
				result.append("&#040;");
			} else if (character == ')') {
				result.append("&#041;");
			} else if (character == '#') {
				result.append("&#035;");
			} else if (character == '%') {
				result.append("&#037;");
			} else if (character == ';') {
				result.append("&#059;");
			} else if (character == '+') {
				result.append("&#043;");
			} else if (character == '-') {
				result.append("&#045;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
		}
		return result.toString();
	}
	
	//TODO do it more efficiently 
	public static String reconvertHTMLToChar(String str) {
		//all HTML escaped characters should start with "&" 
		if(!str.contains("&")) {
			return str;
		}
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&quot;", "\"");
		str = str.replaceAll("&#039;", "\'");
		str = str.replaceAll("&#040;", "(");
		str = str.replaceAll("&#041;", ")");
		str = str.replaceAll("&#035;", "#");
		str = str.replaceAll("&#037;", "%");
		str = str.replaceAll("&#059;", ";");
		str = str.replaceAll("&#043;", "+");
		str = str.replaceAll("&#045;", "-");
		return str;
	}
	
	public static native int getScreenWidth () /*-{
		return screen.width;
	}-*/;
	
	public static native String base64decode(final String data) /*-{
		return window.atob(data);
	}-*/;
	
	
	public static native String base64encode(final String data) /*-{
		return window.btoa(data);
	}-*/;
}
