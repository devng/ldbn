package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.widgetideas.graphics.client.Color;


public final class PastelPalette1 implements ColorPalette {
	
	private final String name;
	private final Color[] colors;
	private int curColor; 
	
	public PastelPalette1() {
		this.name = "Pastel Colors 1";
		//colors taken from http://www.hitmill.com/html/pastels.html Table 3
		colors = new Color[] {
				new Color("#5757FF"),
				new Color("#62A9FF"),
				new Color("#62D0FF"),
				//new Color("#06DCFB"),
				//new Color("#01FCEF"),
				new Color("#03EBA6"),
				new Color("#01F33E"),
				
				new Color("#7979FF"),
				new Color("#86BCFF"),
				new Color("#8ADCFF"),
				//new Color("#3DE4FC"),
				//new Color("#5FFEF7"),
				new Color("#33FDC0"),
				new Color("#4BFE78"),
				
				new Color("#9999FF"),
				new Color("#99C7FF"),
				new Color("#A8E4FF"),
				//new Color("#75ECFD"),
				//new Color("#92FEF9"),
				new Color("#7DFDD7"),
				new Color("#8BFEA8")
		};
		curColor = 0;
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
