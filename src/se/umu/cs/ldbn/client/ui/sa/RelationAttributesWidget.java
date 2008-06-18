package se.umu.cs.ldbn.client.ui.sa;

import java.util.Collection;
import java.util.List;

import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.ui.FDHolderPanelListener;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.LdbnDragCotroller;

import com.google.gwt.user.client.ui.HTML;

public final class RelationAttributesWidget extends HTML 
	implements FDHolderPanelListener{
	
	private AttributeSet attributes;
	private AttributeSet key;
	
	public RelationAttributesWidget() {
		this(new AttributeSet(SolveAssignmentWidget.get().getAttributeNameTable())
				, new AttributeSet(SolveAssignmentWidget.get().getAttributeNameTable()));
	}
	
	public RelationAttributesWidget(AttributeSet attributes) {
		this(attributes, new AttributeSet(SolveAssignmentWidget.get().getAttributeNameTable()));
	}
	
	public RelationAttributesWidget(AttributeSet attributes, 
			AttributeSet key) {
		this.attributes = attributes;
		this.key = key;
		
		setHTML(generateAttributeHTML());
		setStyleName("fdw");
		LdbnDragCotroller.get().makeDraggable(this);
	}
	
	private String generateAttributeHTML() {
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
	
	public void setAttributes(AttributeSet atts) {
		this.attributes = atts;
		setHTML(generateAttributeHTML());
	}
	
	public void setKey(AttributeSet key) {
		this.key = key;
		setHTML(generateAttributeHTML());
	}
	
	public AttributeSet getAttributes() {
		return attributes;
	}
	
	public AttributeSet getKey() {
		return key;
	}

	public void allFDsRemoved() {
		attributes.clearAllAttributes();
		setHTML(generateAttributeHTML());
		
	}
	
	public void fdAdded(Collection<FDWidget> currentFDs) {
		for (FDWidget fdw : currentFDs) {
			attributes.union(fdw.getFD().getLHS());
			attributes.union(fdw.getFD().getRHS());
		}
		setHTML(generateAttributeHTML());
	}
	
	public void fdRemoved(Collection<FDWidget> currentFDs) {
		attributes.clearAllAttributes();
		for (FDWidget fdw : currentFDs) {
			attributes.union(fdw.getFD().getLHS());
			attributes.union(fdw.getFD().getRHS());
			
		}
		key.andOperator(attributes);
		setHTML(generateAttributeHTML());
	}
}