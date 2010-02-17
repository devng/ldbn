package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.widgetideas.graphics.client.Color;

public class PastelPalette implements ColorPalette {
	
	private final String name;
	private final Color[] colors;
	private int curColor; 
	
	public PastelPalette() {
		this.name = "Pastel Colors";
		//colors taken from http://www.hitmill.com/html/rgbcolorvalues.html
		this.colors = new Color[]{new Color("#F70000"),
				new Color("#74138C"),
				new Color("#0000CE"),
				new Color("#1F88A7"),
				new Color("#4A9586"),
				new Color("#D1D17A"),
				new Color("#C27E3A"),
				new Color("#B05F3C"),
				new Color("#B96F6F"),
				new Color("#DFA800"),
				new Color("#3DE4FC"),
				new Color("#FFB428"),
				new Color("#FF800D"),
				new Color("#F7DE00"),
				new Color("#B9264F"),
				new Color("#3923D6"),
				new Color("#2966B8"),
				new Color("#23819C"),
				new Color("#03EBA6"),
				new Color("#2DC800"),
				new Color("#66FF00")};
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
