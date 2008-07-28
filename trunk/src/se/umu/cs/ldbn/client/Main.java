package se.umu.cs.ldbn.client;

import se.umu.cs.ldbn.client.ui.GlassPanel;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;
import se.umu.cs.ldbn.client.ui.home.HomeWidget;
import se.umu.cs.ldbn.client.ui.licence.LicenceWidget;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class Main implements EntryPoint {
	
	public static int WIDTH_PX = 850;
	
	private static Main instance;

	private PickupDragController dragControll;

	private AbsolutePanel mainPanel;

	private GlassPanel glass;
	
	public static Main get() {
		if (instance == null) {
			throw new IllegalArgumentException("onModuleLoad method must bu " +
					"called first.");
		}
		return instance;
	}
	
	private Main() {}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Log.setUncaughtExceptionHandler();
		//used by the logger!
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				onModuleLoad2();
			}
		});
	}
	
	private void onModuleLoad2() {
	    if (Log.isDebugEnabled()) {
	    	Log.debug("Debug log started.");
	    }
		if (instance == null) {
			instance = this;
		}
		if(instance != this) { // should not occur, but how knows?!
			Window.alert("Main.java : instance != this");
		}
		
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth(WIDTH_PX+"px");
		dragControll = new PickupDragController(mainPanel, false);
		dragControll.setBehaviorDragProxy(true);
		
		TabPanel tabs = new TabPanel();
		tabs.add(HomeWidget.get(), "Home");
		tabs.add(SolveAssignmentWidget.get(), "Solve assignments");
		tabs.add(CreateAssignmentWidget.get(), "Create assignments");
		tabs.add(LicenceWidget.get(), "Licence");
		
		tabs.setWidth("100%");
		tabs.selectTab(0);
		mainPanel.add(tabs);
		glass = new GlassPanel(mainPanel);
		DOM.setInnerHTML(RootPanel.get("loading").getElement(), "");
		Panel rp = RootPanel.get("gwtapp");
		rp.add(mainPanel);
	}
	
	public PickupDragController getDragController() {
		return dragControll;
	}
	
	public void showGlassPanelLoading() {
		glass.setLoadingAnimationNextTime(true);
		glass.show();
	}
	
	public void showGlassPanel() {
		glass.show();
	}
	
	public void hideGlassPanel() {
		glass.hide();
	}
}
