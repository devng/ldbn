package se.umu.cs.ldbn.client;

import com.google.gwt.user.client.ui.HTML;

public class FDWidget extends HTML {
	
	private boolean isEditable;
	private String[] leftSide;
	private String[] rightSide;
	
	public FDWidget(boolean isEditable, String[] left, String[] right) {
		this.isEditable = isEditable;
		leftSide = left;
		rightSide = right;
		
		StringBuffer sb = new StringBuffer(); 
		sb.append("<table border='0' cellpadding='2' cellspacing='2'>");
		sb.append("<tr>");
		for (int i = 0; i < left.length; i++) {
			sb.append("<td>");
			sb.append(left[i]);
			sb.append("</td>");
		}
		sb.append("<td>");
		sb.append("<img src='img/arrow-right.png'/>");
		sb.append("</td>");
		for (int i = 0; i < right.length; i++) {
			sb.append("<td>");
			sb.append(right[i]);
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

	public String[] getLeftSide() {
		return leftSide;
	}

	public String[] getRightSide() {
		return rightSide;
	}
}
