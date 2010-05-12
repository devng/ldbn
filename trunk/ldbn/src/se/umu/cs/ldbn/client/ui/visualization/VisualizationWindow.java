package se.umu.cs.ldbn.client.ui.visualization;

import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.ui.visualization.color.BlackPalette;
import se.umu.cs.ldbn.client.ui.visualization.color.ColorPalette;
import se.umu.cs.ldbn.client.ui.visualization.color.GrayPalette;
import se.umu.cs.ldbn.client.ui.visualization.color.OpenOfficePalette;
import se.umu.cs.ldbn.client.ui.visualization.color.PastelPalette;
import se.umu.cs.ldbn.client.ui.visualization.color.StandardPalette;
import se.umu.cs.ldbn.client.ui.window.WindowPanel;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.IncrementalCommand;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class VisualizationWindow extends WindowPanel 
	implements ChangeHandler {
	
	private final static int MENU_PX_HEIGHT = 60;
	
	private static VisualizationWindow inst;
	
	public static VisualizationWindow get() {
		if (inst == null) {
			inst = new VisualizationWindow();
		}
		return inst;
	}
	
	private boolean isCentering;
	
	private FDDrawer drawer;
	
	private Panel contentPanel;
	private Grid menuPanel;
	
	private ListBox diagramBox;
	private ListBox colorBox;
	private ListBox zoomBox;
	private ListBox fdRenderBox;
	
	private String[] diagramTypes; 
	private ColorPalette[] palettes;
	private int[] zoomLevels;
	private String[] fdRenderOrder; 
	
	private AttributeSet curSet;
	private List<FD> curFDs;
	private int curPaletteIndex;
	private int curDiagramTypeIndex;
	private int curZoomLevelIndex;
	private int curFDRenderIndex;

	private SimplePanel menuWrapper;
	
	private int ieCanvasPasses;
	
	private VisualizationWindow() {
		super("FD Visualization");
		ieCanvasPasses = 0;
	}
	
	public void setData(AttributeSet attributes, List<FD> fds) {
		curFDs = fds;
		curSet = attributes;
	}
	
	public void reDrawCanvas() {
		drawer.reDrawCanvas();
	}
	
	@Override
	public void center() {
		if (isCentering) {
			return;
		}
		isCentering = true;
		if (curFDs == null || curSet == null) {
			throw new IllegalArgumentException(
					"VisualizationWindow: Attributes or FDs are null.");
		}
		//setAnimationEnabled(false);
		//setVisible(false);
		palettes[curPaletteIndex].reset();
		drawer.drawFDs(curSet, curFDs, palettes[curPaletteIndex],
				zoomLevels[curZoomLevelIndex], 
				curDiagramTypeIndex == 0 ? false : true,
				curFDRenderIndex == 0 ? true : false);
		show();
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				int w = Math.max(drawer.getOffsetWidth(), 
						menuWrapper.getOffsetWidth() + 8);
				setContentSize(w, drawer.getOffsetHeight() + MENU_PX_HEIGHT);
				//hide();
				//setAnimationEnabled(true);
				//setVisible(true);
				//VisualizationWindow.super.center();
				//IE BUG canvas must be drawn twice 
				if(Common.isAgentIE()) {
					DeferredCommand.addCommand(new Command() {
						public void execute() {
							setVisible(false);
							setVisible(true);
						}
					});
				}
				isCentering = false;
			}
		});
	}

	private void reDrawFDs() {
		if (curFDs == null || curSet == null) {
			throw new IllegalArgumentException(
					"VisualizationWindow: Attributes or FDs are null.");
		}
		palettes[curPaletteIndex].reset();
		drawer.drawFDs(curSet, curFDs, palettes[curPaletteIndex],
				zoomLevels[curZoomLevelIndex], 
				curDiagramTypeIndex == 0 ? false : true,
				curFDRenderIndex == 0 ? true : false);
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				int w = Math.max(drawer.getOffsetWidth(), 
						menuWrapper.getOffsetWidth() + 8);
				setContentSize(w, drawer.getOffsetHeight() + MENU_PX_HEIGHT);
			}
		});

	}
	
	protected Widget getContentWidget() {
		diagramTypes = new String[]{"Type 1: LDBN", "Type 2: Elmasri"};
		palettes = new ColorPalette[]{ 
				new BlackPalette(), new GrayPalette(), new StandardPalette(),
				new OpenOfficePalette(), new PastelPalette()};
		zoomLevels = new int[]{1, 2, 3};
		fdRenderOrder = new String[]{"Forward", "Reverse"};
		
		curPaletteIndex = 3;
		curDiagramTypeIndex = 1;
		curZoomLevelIndex = 1;
		
		menuPanel = new Grid(2,4);
		
		menuPanel.setWidget(0, 0, new HTML("<NOBR>Diagram Type</NOBR>"));
		diagramBox = new ListBox(false);
		for (int i = 0; i < diagramTypes.length; i++) {
			diagramBox.addItem(diagramTypes[i]);
	    }
		diagramBox.setSelectedIndex(curDiagramTypeIndex);
		diagramBox.addChangeHandler(this);
		menuPanel.setWidget(1, 0, diagramBox);
		
		menuPanel.setWidget(0, 1, new HTML("<NOBR>Color Palette</NOBR>"));
		colorBox = new ListBox(false);
		for (int i = 0; i < palettes.length; i++) {
			colorBox.addItem(palettes[i].getName());
	    }
		colorBox.setSelectedIndex(curPaletteIndex);
		colorBox.addChangeHandler(this);
		menuPanel.setWidget(1, 1, colorBox);
		
		menuPanel.setWidget(0, 2, new Label("Zoom"));
		zoomBox = new ListBox(false);
		for (int i = 0; i < zoomLevels.length; i++) {
			zoomBox.addItem(" "+zoomLevels[i]+" x ");
	    }
		zoomBox.setSelectedIndex(curZoomLevelIndex);
		zoomBox.addChangeHandler(this);
		menuPanel.setWidget(1, 2, zoomBox);
		
		menuPanel.setWidget(0, 3, new HTML("<NOBR>FDs Order</NOBR>"));
		fdRenderBox = new ListBox(false);
		for (int i = 0; i < fdRenderOrder.length; i++) {
			fdRenderBox.addItem(fdRenderOrder[i]);
	    }
		fdRenderBox.setSelectedIndex(curFDRenderIndex);
		fdRenderBox.addChangeHandler(this);
		menuPanel.setWidget(1, 3, fdRenderBox);
		
		menuWrapper = new SimplePanel();
		menuWrapper.setWidth("99%");
		menuWrapper.setStyleName("fdw");
		menuWrapper.add(menuPanel);
		
		contentPanel = new VerticalPanel();
		contentPanel.add(menuWrapper);
		drawer = new FDDrawer();
		contentPanel.add(drawer);
		
		return contentPanel;
	}

	protected boolean useScrollPanel() {
		return true;
	}

	@Override
	public void onChange(ChangeEvent event) {
		Object sender = event.getSource();
		if (sender == diagramBox) {
			curDiagramTypeIndex = diagramBox.getSelectedIndex();
		} else if (sender == colorBox) {
			curPaletteIndex = colorBox.getSelectedIndex();
		} else if (sender == zoomBox) {
			curZoomLevelIndex = zoomBox.getSelectedIndex();
		} else if (sender == fdRenderBox) {
			curFDRenderIndex = fdRenderBox.getSelectedIndex();
		}
		reDrawFDs();
	}
}
