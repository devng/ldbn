package se.umu.cs.ldbn.client.ui.visualization.color;

import com.google.gwt.canvas.dom.client.CssColor;

public final class OpenOfficePalette implements ColorPalette {
	
	private final String name;
	private final CssColor[] colors;
	private int curColor; 
	
	public OpenOfficePalette() {
		this.name = "OpenOffice Style";
		
		colors = new CssColor[] {
				//Open Office Chart Colors
				CssColor.make("#004586"),
				CssColor.make("#FF420E"),
				CssColor.make("#FFD320"),
				CssColor.make("#579D1C"),
				CssColor.make("#7E0021"),
				CssColor.make("#83CAFF"),
				CssColor.make("#314004"),
				CssColor.make("#AECF00"),
				CssColor.make("#4B1F6F"),
				CssColor.make("#FF950E"),
				CssColor.make("#C5000B"),
				CssColor.make("#0084D1"),
				
				/*
				 * //colors taken from http://www.hitmill.com/html/pastels.html Table 3
				CssColor.make("#5757FF"),
				CssColor.make("#62A9FF"),
				CssColor.make("#62D0FF"),
				//CssColor.make("#06DCFB"),
				//CssColor.make("#01FCEF"),
				CssColor.make("#03EBA6"),
				CssColor.make("#01F33E"),
				
				CssColor.make("#7979FF"),
				CssColor.make("#86BCFF"),
				CssColor.make("#8ADCFF"),
				//CssColor.make("#3DE4FC"),
				//CssColor.make("#5FFEF7"),
				CssColor.make("#33FDC0"),
				CssColor.make("#4BFE78"),
				
				CssColor.make("#9999FF"),
				CssColor.make("#99C7FF"),
				CssColor.make("#A8E4FF"),
				//CssColor.make("#75ECFD"),
				//CssColor.make("#92FEF9"),
				CssColor.make("#7DFDD7"),
				CssColor.make("#8BFEA8")
				*/
		};
		curColor = 0;
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
