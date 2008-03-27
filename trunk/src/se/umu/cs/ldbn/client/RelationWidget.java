package se.umu.cs.ldbn.client;

import com.google.gwt.user.client.ui.HTML;

public class RelationWidget extends HTML {
	
	private String[] attributes;
	
	public RelationWidget(String[] attributes) {
		this.attributes = attributes;
		StringBuffer sb = new StringBuffer(); 
		sb.append("<table border='0' cellpadding='2' cellspacing='2'>");
		sb.append("<tr>");
		for (int i = 0; i < attributes.length; i++) {
			sb.append("<td>");
			sb.append(attributes[i]);
			sb.append("</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		setHTML(sb.toString());
		setStyleName("fdw");
	}

	public String[] getAttributes() {
		return attributes;
	}
}
