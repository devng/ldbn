package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.widgetideas.graphics.client.Color;

public class StandardPalette implements ColorPalette {

	private final String name;
	private final Color[] colors;
	private int curColor; 
	
	public StandardPalette() {
		this.name = "Standard Colors";
		this.colors = new Color[]{Color.BLUE,
				Color.RED,
				Color.GREEN,
				Color.YELLOW,
				Color.SKY_BLUE,
				Color.PINK,
				Color.ORANGE,
				Color.DARK_BLUE,
				Color.DARK_ORANGE,
				Color.GREY,
				Color.CYAN,
				Color.PEACH,
				Color.BLUEVIOLET,
				Color.BLACK};
	}
	
	public Color[] getAllColors() {
		
		return colors;
	}

	public String getName() {
		return name;
	}

	public Color nextColor() {
		curColor %= colors.length;
		Color r = colors[curColor];
		curColor++;
		return r;
	}

	public void reset() {
		curColor = 0;
	}

}
