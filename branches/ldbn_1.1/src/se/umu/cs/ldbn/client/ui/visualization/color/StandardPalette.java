package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.canvas.dom.client.CssColor;

public class StandardPalette implements ColorPalette {

	private final String name;
	private final CssColor[] colors;
	private int curColor; 
	
	public StandardPalette() {
		this.name = "Standard Colors";
		this.colors = new CssColor[]{CommonColors.BLUE,
			CommonColors.RED, 
			CommonColors.GREEN, 
			CommonColors.YELLOW, 
			CommonColors.SKY_BLUE, 
			CommonColors.PINK,
			CommonColors.ORANGE, 
			CommonColors.DARK_BLUE, 
			CommonColors.DARK_ORANGE, 
			CommonColors.GREY,
			CommonColors.CYAN, 
			CommonColors.PEACH, 
			CommonColors.BLUEVIOLET, 
			CommonColors.BLACK};
	}
	
	public CssColor[] getAllColors() {
		return colors;
	}

	public String getName() {
		return name;
	}

	public CssColor nextColor() {
		curColor %= colors.length;
		CssColor r = colors[curColor];
		curColor++;
		return r;
	}

	public void reset() {
		curColor = 0;
	}

}
