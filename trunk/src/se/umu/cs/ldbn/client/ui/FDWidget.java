package se.umu.cs.ldbn.client.ui;

import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.FD;

import com.google.gwt.user.client.ui.HTML;

public final class FDWidget extends HTML {
	
	private boolean isEditable;
	private FD fd;
	
	public FDWidget(boolean isEditable, FD fd) {
		this.isEditable = isEditable;
		this.fd = fd;
		
		StringBuffer sb = new StringBuffer(); 
		sb.append("<table border='0' cellpadding='2' cellspacing='2'>");
		sb.append("<tr>");
		List<String> atts = fd.getLHS().getAttributeNames();
		for (String str : atts) {
			sb.append("<td>");
			sb.append(str);
			sb.append("</td>");
		}
		sb.append("<td>");
		sb.append("<img src='img/arrow-right.png'/>");
		sb.append("</td>");
		atts = fd.getRHS().getAttributeNames();
		for (String str : atts) {
			sb.append("<td>");
			sb.append(str);
			sb.append("</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		setHTML(sb.toString());
		setStyleName("fdw");
		Main.get().getDragController().makeDraggable(this);
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public FD getFD() {
		return fd;
	}
}
