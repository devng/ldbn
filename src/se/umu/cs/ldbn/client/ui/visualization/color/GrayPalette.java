package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.widgetideas.graphics.client.Color;

//Gray Shades
public class GrayPalette implements ColorPalette {

	private final String name;
	private final Color[] colors;
	private int curColor; 
	
	public GrayPalette() {
		this.name = "Gray Shades";
		//http://www.w3schools.com/Html/html_colors.asp
		this.colors = new Color[]{new Color("#D0D0D0"),
				new Color("#C0C0C0"),
				new Color("#B0B0B0"),
				new Color("#A0A0A0"),
				new Color("#909090"),
				new Color("#888888"),
				new Color("#808080"),
				new Color("#787878"),
				new Color("#707070"),
				new Color("#606060"),
				new Color("#505050"),
				new Color("#404040"),
				new Color("#303030"),
				new Color("#202020"),
				new Color("#101010")};
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
