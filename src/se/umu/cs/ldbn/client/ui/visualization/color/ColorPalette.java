package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.canvas.dom.client.CssColor;

public interface ColorPalette {

	public String getName();
	
	public CssColor nextColor();
	public void reset();
	
	public CssColor[] getAllColors();
	
}
