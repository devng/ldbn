package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.canvas.dom.client.CssColor;

public class BlackPalette implements ColorPalette {
	
	private final String name;
	private final CssColor[] colors;
	
	public BlackPalette() {
		this.name = "Black Only";
		colors = new CssColor[] {CssColor.make("#000000")};
	}

	public CssColor[] getAllColors() {
		return colors;
	}

	public String getName() {
		return name;
	}
	
	public CssColor nextColor() {
		return colors[0];
	}
	
	public void reset() {
	}

}
