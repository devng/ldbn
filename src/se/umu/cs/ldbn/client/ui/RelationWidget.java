package se.umu.cs.ldbn.client.ui;

import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.Relation;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

public final class RelationWidget extends HTML {
	
	private Relation relation;
	
	public RelationWidget(Relation relation) {
		this.relation = relation;
		StringBuffer sb = new StringBuffer(); 
		sb.append("<table border='0' cellpadding='2' cellspacing='2'>");
		sb.append("<tr>");
		AttributeSet atts = relation.getAttrbutes();
		List<String> names = atts.getAttributeNames();
		for (String str : names) {
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
	
	public Relation getRelation() {
		return relation;
	}
}
