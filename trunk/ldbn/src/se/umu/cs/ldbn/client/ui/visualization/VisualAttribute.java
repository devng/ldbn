package se.umu.cs.ldbn.client.ui.visualization;

import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;
import se.umu.cs.ldbn.client.ui.visualization.color.CommonColors;

import com.google.gwt.canvas.dom.client.CssColor;

final class VisualAttribute extends SingleAttributeWidget {

	private boolean hasIncommingArrow;
	
	private boolean hasOutgoingArrow;
	
	private int positionArrowIn;
	
	private int positionArrowOut;
	
	private int attIndex;
	
	private String name;
	
	private CssColor lastIncommingColor;
	
	private CssColor lastOutcommingColor;

	public VisualAttribute(String name, int attIndex) {
		this(name, attIndex, CommonColors.BLACK);
	}
	
	public VisualAttribute(String name, int attIndex, CssColor c) {
		super(name);
		positionArrowIn  = 0;
		positionArrowOut = 0;
		hasIncommingArrow = false;
		hasOutgoingArrow  = false;
		this.name = name;
		this.attIndex = attIndex;
		removeStyleName("saw");
//		Element e = this.getElement();
//		DOM.setStyleAttribute(e, "border", "1px solid " + c.toString());
//		DOM.setStyleAttribute(e, "color", c.toString());
	}
	
	public void setZoomLevel(int zoomLevel) {
		setStyleName("vaw-zoom-"+zoomLevel);
//		if(zoomLevel > 2) {
//			Element e = this.getElement();
//			DOM.setStyleAttribute(e, "border", "2px solid " + c.toString());
//		}
	}
	
	public boolean hasIncommingArrow() {
		return hasIncommingArrow;
	}

	public void setHasIncommingArrow(boolean hasIncommingArrow) {
		this.hasIncommingArrow = hasIncommingArrow;
	}

	public boolean hasOutgoingArrow() {
		return hasOutgoingArrow;
	}

	public void setHasOutgoingArrow(boolean hasOutgoingArrow) {
		this.hasOutgoingArrow = hasOutgoingArrow;
	}

	public int getPositionArrowIn() {
		return positionArrowIn;
	}

	public void setPositionArrowIn(int positionArrowIn) {
		this.positionArrowIn = positionArrowIn;
	}

	public int getPositionArrowOut() {
		return positionArrowOut;
	}

	public void setPositionArrowOut(int positionArrowOut) {
		this.positionArrowOut = positionArrowOut;
	}

	public int getAttIndex() {
		return attIndex;
	}

	public String getName() {
		return name;
	}

	public CssColor getLastIncommingColor() {
		return lastIncommingColor;
	}

	public void setLastIncommingColor(CssColor lastIncommingColor) {
		this.lastIncommingColor = lastIncommingColor;
	}

	public CssColor getLastOutcommingColor() {
		return lastOutcommingColor;
	}

	public void setLastOutcommingColor(CssColor lastOutcommingColor) {
		this.lastOutcommingColor = lastOutcommingColor;
	}
}
