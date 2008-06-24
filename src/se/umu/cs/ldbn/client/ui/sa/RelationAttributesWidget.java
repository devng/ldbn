package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.ui.FDHolderPanelListener;
import se.umu.cs.ldbn.client.ui.FDWidget;

import com.google.gwt.user.client.ui.HTML;

public final class RelationAttributesWidget extends HTML {
	
	private AttributeSet attributes;
	private AttributeSet key;
	private List<RelationAttributesWidgetListener> listeners;
	
	public RelationAttributesWidget() {
		this(new AttributeSet(SolveAssignmentWidget.get().getDomainTable())
				, new AttributeSet(SolveAssignmentWidget.get().getDomainTable()));
	}
	
	public RelationAttributesWidget(AttributeSet attributes) {
		this(attributes, new AttributeSet(SolveAssignmentWidget.get().getDomainTable()));
	}
	
	public RelationAttributesWidget(AttributeSet attributes, 
			AttributeSet key) {
		this.attributes = attributes;
		this.key = key;
		listeners = new ArrayList<RelationAttributesWidgetListener>();
		setHTML(generateAttributeHTML());
		setStyleName("fdw");
		Main.get().getDragController().makeDraggable(this);
	}
	
	public void addListener(RelationAttributesWidgetListener l) {
		listeners.add(l);
	}
	
	public void removeListener(RelationAttributesWidgetListener l) {
		listeners.remove(l);
	}
	
	public AttributeSet getAttributes() {
		return attributes;
	}
	
	public AttributeSet getKey() {
		return key;
	}

	public void setAttributes(AttributeSet atts) {
		this.attributes = atts;
		setHTML(generateAttributeHTML());
		for (RelationAttributesWidgetListener l : listeners) {
			l.onAttributesChange();
		}
	}
	
	public void setKey(AttributeSet key) {
		this.key = key;
		setHTML(generateAttributeHTML());
	}
	
	String generateAttributeHTML() {
		StringBuffer sb = new StringBuffer(); 
		sb.append("<table border='0' cellpadding='2' cellspacing='2'>");
		sb.append("<tr>");
		List<String> names = attributes.getAttributeNames();
		for (String str : names) {	
			sb.append("<td>");
			if(key.containsAtt(str)) {
				sb.append("<b>");
				sb.append(str);
				sb.append("</b>");
			} else {
				sb.append(str);
			}
			sb.append("</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		return sb.toString();
	}
}
