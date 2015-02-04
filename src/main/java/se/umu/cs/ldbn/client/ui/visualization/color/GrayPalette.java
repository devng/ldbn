package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.canvas.dom.client.CssColor;

//Gray Shades
public class GrayPalette implements ColorPalette {

	private final String name;
	private final CssColor[] colors;
	private int curColor; 
	
	public GrayPalette() {
		this.name = "Gray Shades";
		//http://www.w3schools.com/Html/html_colors.asp
		this.colors = new CssColor[]{CssColor.make("#D0D0D0"),
				CssColor.make("#C0C0C0"),
				CssColor.make("#B0B0B0"),
				CssColor.make("#A0A0A0"),
				CssColor.make("#909090"),
				CssColor.make("#888888"),
				CssColor.make("#808080"),
				CssColor.make("#787878"),
				CssColor.make("#707070"),
				CssColor.make("#606060"),
				CssColor.make("#505050"),
				CssColor.make("#404040"),
				CssColor.make("#303030"),
				CssColor.make("#202020"),
				CssColor.make("#101010")};
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
