package se.umu.cs.ldbn.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.core.Relation;
import se.umu.cs.ldbn.client.ui.CommonStyle;
import se.umu.cs.ldbn.client.ui.DecompositionEditorWidget;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.FDEditorWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.GivenAttributesWidget;
import se.umu.cs.ldbn.client.ui.GivenFDsWidget;
import se.umu.cs.ldbn.client.ui.RelationHolderPanel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint, ClickListener {

	private static Main instance = null;
	
	private AbsolutePanel mainPanel;
	private PickupDragController dragController;
	
	private AttributeNameTable domain;
	private List<FD> fds;
	
	private Button checkSolution;
	private Button newAssignment;
	
	private GivenAttributesWidget givenAttributesWidget;
	private GivenFDsWidget givenFDsWidget;
	private FDEditorWidget fdEditorWidget;
	private FDHolderPanel minimalCoverFDs;
	private List<RelationHolderPanel> NF2Relations;
	//private DecompositionEditorWidget NF2DecompositionEditorWidget;
	private DecompositionEditorWidget NF3DecompositionEditorWidget;

	private FDEditorDialog fdEditorDialog;

	private Button minCovAddFD;

	private HorizontalPanel relationsPanel;

	private Button addRelation2NF;

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
		if(instance != this) { // should not occur
			Window.alert("Main.java : instance != this");
		}
		mainPanel = RootPanel.get();
		dragController = new PickupDragController(mainPanel, false);
		dragController.setBehaviorDragProxy(true);
		
		//Given Attributes
		givenAttributesWidget = new GivenAttributesWidget();
		newAssignment = new Button("New Assignment");
		newAssignment.setStyleName("att-but");
		CommonStyle.setCursorPointer(newAssignment);
		newAssignment.addClickListener(this);
		checkSolution = new Button("Check Solution");
		checkSolution.setStyleName("att-but");
		CommonStyle.setCursorPointer(checkSolution);
		checkSolution.addClickListener(this);
		Button[] attUpBut = {newAssignment, checkSolution};
		DisclosureWidget dw = new DisclosureWidget("Given attributes", 
				givenAttributesWidget, attUpBut);
		mainPanel.add(dw);
		//Given FDs
		givenFDsWidget = new GivenFDsWidget();
		dw = new DisclosureWidget("Given FDs", 
				givenFDsWidget);
		mainPanel.add(dw);
		//Minimal Cover
		minimalCoverFDs = new FDHolderPanel();
		HorizontalPanel minCoverBut = new HorizontalPanel();
		minCovAddFD = new Button("Add new FD");
		minCovAddFD.addStyleName("min-cov-but");
		minCovAddFD.addClickListener(this);
		minCoverBut.add(minCovAddFD);
		minimalCoverFDs.add(minCovAddFD);
		dw = new DisclosureWidget("Find the minimal cover of FDs", 
				minimalCoverFDs, minimalCoverFDs.getCheckBoxControlls());
		mainPanel.add(dw); 
		//Decomposition 2NF
		
		VerticalPanel vp2NF = new VerticalPanel();
		NF2Relations = new ArrayList<RelationHolderPanel>();
		addRelation2NF = new Button("Add new Relation");
		addRelation2NF.setStyleName("min-cov-but");
		addRelation2NF.addClickListener(this);
		HorizontalPanel addRelation2NFPanel = new HorizontalPanel();
		addRelation2NFPanel.add(addRelation2NF);
		vp2NF.add(addRelation2NFPanel);
		relationsPanel = new HorizontalPanel();
		vp2NF.add(relationsPanel);
		//NF2DecompositionEditorWidget = new DecompositionEditorWidget();
		dw = new DisclosureWidget("Decompose in 2 NF", 
				vp2NF); 
		mainPanel.add(dw);
		
		//Decomposition 3NF
		NF3DecompositionEditorWidget = new DecompositionEditorWidget();
		dw = new DisclosureWidget("Decompose in 3 NF", 
				NF3DecompositionEditorWidget); 
		mainPanel.add(dw);
		
		fdEditorDialog = new FDEditorDialog();
		fdEditorWidget = fdEditorDialog.getFDEditorWidget();
		
		//generate new assignment
		newAssignment();
	}
	
	private void newAssignment() {
		minimalCoverFDs.clearAll();
		//NF2DecompositionEditorWidget.clearAll();
		NF3DecompositionEditorWidget.clearAll();
		Assignment a = RandomAssignmentGenerator.generate();
		this.domain = a.getDomain();
		this.fds = a.getFDs();
		givenAttributesWidget.setAttributeNames(domain);
		givenFDsWidget.setFDs(fds);
	}

	public PickupDragController getDragController() {
		return dragController;
	}

	public FDEditorWidget getFDEditorWidget() {
		return fdEditorWidget;
	}
	
	public AttributeNameTable getAttributeNameTable() {
		return domain;
	}

	public GivenAttributesWidget getGivenAttributesWidget() {
		return givenAttributesWidget;
	}

	public GivenFDsWidget getGivenFDsWidget() {
		return givenFDsWidget;
	}

	/*
	public DecompositionEditorWidget getNF2DecompositionEditorWidget() {
		return NF2DecompositionEditorWidget;
	}
	*/
	public DecompositionEditorWidget getNF3DecompositionEditorWidget() {
		return NF3DecompositionEditorWidget;
	}

	public List<FD> getFds() {
		return fds;
	}

	public void onClick(Widget sender) {
		
		if(sender == checkSolution) {
			checkSolution();
		} else if (sender == newAssignment) {
			newAssignment();
		} else  if (sender == minCovAddFD) {
			fdEditorDialog.center();
			fdEditorDialog.setCurrentFDHolderPanel(minimalCoverFDs);
		} else if (sender == addRelation2NF) {
			RelationHolderPanel rhp = new RelationHolderPanel();
			NF2Relations.add(rhp);
			relationsPanel.add(rhp);
			fdEditorDialog.center();
			fdEditorDialog.setCurrentFDHolderPanel(rhp.fetFDHolderPanel());
		}
	}
	
	public FDHolderPanel getMinCoverHolderPanel() {
		return minimalCoverFDs;
	}
	
	private void checkSolution() {
		//check minimal cover
		List<FD> minCovFDs = minimalCoverFDs.getFDs();
		List<FD> deepCopy = new ArrayList<FD>(minCovFDs.size());
		for (FD fd : minCovFDs) {
			deepCopy.add(fd.clone());
		}
		boolean isMinCoverRight = false;
		Algorithms.minimalCover(deepCopy);
		if (!Algorithms.equivalence(fds, minCovFDs)) {
			Log.info("minimal cover - wrong - fds are not equivalent to the given fds.");
		} else if(deepCopy.size() != minCovFDs.size()) {
			Log.info("minimal cover - wrong - too much fds.");
		} else if (!deepCopy.containsAll(minCovFDs)) {
			Log.info("minimal cover - wrong - some fds are not redused.");
		} else {
			Log.info("minimal cover - right.");
			isMinCoverRight = true;
		}
		//check 2nf
		if(!isMinCoverRight) {
			Log.info("Minimal cover was wrong, program will compute it, but not show the solution, in order to check the decomposition solutions. Note that different minimal covers may give different solutions.");
			deepCopy = new ArrayList<FD>();
			for (FD fd2 : fds) {
				deepCopy.add(fd2);
			}
			Algorithms.minimalCover(deepCopy);
		}
		Log.info("Checking 2nf...");
		/*
		List<Relation> relations = NF2DecompositionEditorWidget.getRelations();
		boolean is2NF = Algorithms.isIn2NF(relations, deepCopy); 
		if(is2NF) {
			Log.info("2 NF Decomposition - right");
		} else {
			Log.info("2 NF Decomposition - wrong");
		}
		
		Log.info("Checking 3nf...");
		relations = NF3DecompositionEditorWidget.getRelations();
		boolean is3NF = Algorithms.isIn3NF(relations, deepCopy); 
		if(is3NF) {
			Log.info("3 NF Decomposition - right");
		} else {
			Log.info("3 NF Decomposition - wrong");
		}
		*/
	}

	public FDEditorDialog getFdEditorDialog() {
		return fdEditorDialog;
	}
}
