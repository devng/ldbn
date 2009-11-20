package se.umu.cs.ldbn.client.ui.visualization;

import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;

import com.google.gwt.widgetideas.graphics.client.Color;

final class VisualAttribute extends SingleAttributeWidget {

	private boolean hasIncommingArrow;
	
	private boolean hasOutgoingArrow;
	
	private int positionArrowIn;
	
	private int positionArrowOut;
	
	private int attIndex;
	
	private String name;
	
	private Color lastIncommingColor;
	
	private Color lastOutcommingColor;

	public VisualAttribute(String name, int attIndex) {
		this(name, attIndex, Color.BLACK);
	}
	
	public VisualAttribute(String name, int attIndex, Color c) {
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

	public Color getLastIncommingColor() {
		return lastIncommingColor;
	}

	public void setLastIncommingColor(Color lastIncommingColor) {
		this.lastIncommingColor = lastIncommingColor;
	}

	public Color getLastOutcommingColor() {
		return lastOutcommingColor;
	}

	public void setLastOutcommingColor(Color lastOutcommingColor) {
		this.lastOutcommingColor = lastOutcommingColor;
	}
}
