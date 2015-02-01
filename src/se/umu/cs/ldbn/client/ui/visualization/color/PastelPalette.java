package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.canvas.dom.client.CssColor;

public class PastelPalette implements ColorPalette {
	
	private final String name;
	private final CssColor[] colors;
	private int curColor; 
	
	public PastelPalette() {
		this.name = "Pastel Colors";
		//colors taken from http://www.hitmill.com/html/rgbcolorvalues.html
		this.colors = new CssColor[]{CssColor.make("#F70000"),
				CssColor.make("#74138C"),
				CssColor.make("#0000CE"),
				CssColor.make("#1F88A7"),
				CssColor.make("#4A9586"),
				CssColor.make("#D1D17A"),
				CssColor.make("#C27E3A"),
				CssColor.make("#B05F3C"),
				CssColor.make("#B96F6F"),
				CssColor.make("#DFA800"),
				CssColor.make("#3DE4FC"),
				CssColor.make("#FFB428"),
				CssColor.make("#FF800D"),
				CssColor.make("#F7DE00"),
				CssColor.make("#B9264F"),
				CssColor.make("#3923D6"),
				CssColor.make("#2966B8"),
				CssColor.make("#23819C"),
				CssColor.make("#03EBA6"),
				CssColor.make("#2DC800"),
				CssColor.make("#66FF00")};
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
