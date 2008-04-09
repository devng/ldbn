package se.umu.cs.ldbn.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

	
	private static Main instance = null;
	private PickupDragController dragController;
	private FDEditorWidget fdEditor;
	
	public static Main get() {
		if (instance == null) {
			throw new IllegalArgumentException("onModuleLoad method must bu " +
					"called first.");
		}
		return instance;
	}
	
	private Main() {
		
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		if (instance == null) {
			instance = this;
		}
		if(instance != this) { // should not occur
			Window.alert("Main.java : instance != this");
		}
		
		AbsolutePanel all = new AbsolutePanel();
		dragController = new PickupDragController(all, false);
		dragController.setBehaviorDragProxy(true);
		//Attributes
		RelationAttributeWidget raw1 = new RelationAttributeWidget("A1");
		dragController.makeDraggable(raw1);
		RelationAttributeWidget raw2 = new RelationAttributeWidget("A2");
		dragController.makeDraggable(raw2);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(raw1);
		hp.add(raw2);
		Button[] attUpBut = new Button[2];
		attUpBut[0] = new Button("New Assigment");
		attUpBut[0].setStyleName("att-but");
		CommonStyle.setCursorPointer(attUpBut[0]);
		attUpBut[1] = new Button("Check Solution");
		attUpBut[1].setStyleName("att-but");
		CommonStyle.setCursorPointer(attUpBut[1]);
		DisclosureWidget dw1 = new DisclosureWidget("Given attributes", hp, attUpBut);
		//FDs
		String[] left1 = {"A1", "A2", "A3"};
		String[] right1 = {"B1", "B2", "B3"};
		FDWidget fdw1 = new FDWidget(false, left1, right1);
		dragController.makeDraggable(fdw1);
		String[] left2 = {"C1", "C2", "C3"};
		String[] right2 = {"D1", "D2", "D3"};
		FDWidget fdw2 = new FDWidget(false, left2, right2);
		dragController.makeDraggable(fdw2);
		VerticalPanel vp = new VerticalPanel();
		vp.add(fdw1);
		vp.add(fdw2);
		DisclosureWidget dw2 = new DisclosureWidget("Given FDs", vp);
		
		fdEditor = new FDEditorWidget();
		
		String[] left3 = {"C1", "C2", "C3"};
		String[] right3 = {"D1", "D2", "D3"};
		FDWidget fdw3 = new FDWidget(true, left3, right3);
		fdEditor.addFDWidget(fdw3);
		DisclosureWidget dw3 = new DisclosureWidget("Find the minimal cover of FDs", fdEditor);
		
		//Decomposition
		DecompositionEditorWidget dew2nf = new DecompositionEditorWidget();
		DisclosureWidget dw4 = new DisclosureWidget("Decompose in 2 NF", dew2nf); 
		
		DecompositionEditorWidget dew3nf = new DecompositionEditorWidget();
		DisclosureWidget dw5 = new DisclosureWidget("Decompose in 3 NF", dew3nf); 
		
		//add to all
		all.add(dw1);
		all.add(dw2);
		all.add(dw3);
		all.add(dw4);
		all.add(dw5);
		RootPanel.get().add(all);
	}

	public PickupDragController getDragController() {
		return dragController;
	}

	public FDEditorWidget getFdEditor() {
		return fdEditor;
	}
}
