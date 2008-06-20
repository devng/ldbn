package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.AssignmentGenerator;
import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.io.AssignmentLoaderCallback;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public final class SolveAssignmentWidget extends AbsolutePanel 
	implements ClickListener, LoadAssignmentDialogCallback, AssignmentLoaderCallback {

	private static SolveAssignmentWidget inst;
	//assignment variables  
	private DomainTable domain;
	private List<FD> fds;
	//given attributes
	private Button checkSolution;
	private Button newAssignment;
	private Button showSolution;
	private GivenAttributesWidget givenAttributesWidget;
	//given fds
	private GivenFDsWidget givenFDsWidget;
	//minimal cover
	private MinimalCoverWidget minimalCoverWidget;
	//2nf decomposition
	private DecompositionWidget decomposition2NF;
	
	private SolveAssignmentWidget() {
		super();
		setWidth("100%");
		//this are not used, before an assigment has been loaded,  but
		//if they are not set a NullPointer or IllegalArgument exception
		//can be thrown
		domain = new DomainTable();
		fds = new ArrayList<FD>();
		
		//Given Attributes
		givenAttributesWidget = new GivenAttributesWidget();
		newAssignment = new Button("New Assignment");
		newAssignment.setStyleName("att-but");
		CommonFunctions.setCursorPointer(newAssignment);
		newAssignment.addClickListener(this);
		checkSolution = new Button("Check Solution");
		checkSolution.setStyleName("att-but");
		CommonFunctions.setCursorPointer(checkSolution);
		checkSolution.addClickListener(this);
		showSolution = new Button("Show Solution");
		showSolution.setStyleName("att-but");
		CommonFunctions.setCursorPointer(showSolution);
		showSolution.addClickListener(this);
		InfoButton info = new InfoButton("example");
		info.setStyleName("att-img");
		HeaderWidget hw = new HeaderWidget();
		hw.add(newAssignment);
		hw.add(checkSolution);
		hw.add(showSolution);
		hw.add(info);
		add(hw);
		//Button[] attUpBut = {newAssignment, checkSolution};
		DisclosureWidget dw = new DisclosureWidget("Given attributes", 
				givenAttributesWidget);
		add(dw);
		//Given FDs
		givenFDsWidget = new GivenFDsWidget();
		dw = new DisclosureWidget("Given FDs", 
				givenFDsWidget);
		add(dw);
		//Minimal Cover
		minimalCoverWidget = new MinimalCoverWidget();
		dw = new DisclosureWidget("Find the minimal cover of FDs", 
				minimalCoverWidget);
		add(dw); 
		decomposition2NF = new DecompositionWidget();
		dw = new DisclosureWidget("Decompose in 2 NF", decomposition2NF); 
		add(dw);
		
		//start with  a loaded assignment
		this.domain = new DomainTable(); //this is necessary 
		givenAttributesWidget.setDomain(domain);
		Assignment a = AssignmentGenerator.generate();
		loadAssignment(a);
	}
	
	public static SolveAssignmentWidget get() {
		if (inst == null) {
			inst = new SolveAssignmentWidget();
		}
		return inst;
	}
	
	public DomainTable getDomainTable() {
		return domain;
	}
	
	public void onClick(Widget sender) {
		if(sender == checkSolution) {
			Window.alert("Not implemented yet");
		} else if (sender == newAssignment) {
			LoadAssignmentDialog.get().load(this);
		} else if (sender == showSolution) {
			Window.alert("Not implemented yet");
		}
	}

	public void loadedIdAndName(String id, String name) {
		AssignmentLoader.loadFromURL(id, this);
	}

	public void onLoadCanceled() {}

	public void onAssignmentLoadError() {}

	public void onAssignmentLoaded(Assignment a) {
		loadAssignment(a);
	}

	private void loadAssignment(Assignment a) {
		clearData();
		this.domain.loadDomainTable(a.getDomain()); //not new so the listeners stay
		this.fds = a.getFDs();
		givenFDsWidget.setFDs(fds);
	}
	
	private void clearData() {
		domain.clearData();
		givenFDsWidget.clearData();
		minimalCoverWidget.getFDHolderPanel().clearData();
		fds.clear();
		decomposition2NF.clearData();
	}
	
	private void checkSolution() {
		//check minimal cover
		List<FD> minCovFDs = minimalCoverWidget.getFDHolderPanel().getFDs();
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
		
		//AssignmentXML axml  = new AssignmentXML();
	}
}
