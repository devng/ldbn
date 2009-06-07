package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.widgetideas.graphics.client.Color;

public interface ColorPalette {

	public String getName();
	
	public Color nextColor();
	public void reset();
	
	public Color[] getAllColors();
	
}
