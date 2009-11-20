package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.widgetideas.graphics.client.Color;

public class BlackPalette implements ColorPalette {
	
	private final String name;
	private final Color[] colors;
	
	public BlackPalette() {
		this.name = "Black Only";
		colors = new Color[] {Color.BLACK};
	}

	public Color[] getAllColors() {
		return colors;
	}

	public String getName() {
		return name;
	}
	
	public Color nextColor() {
		return Color.BLACK;
	}
	
	public void reset() {
	}

}
