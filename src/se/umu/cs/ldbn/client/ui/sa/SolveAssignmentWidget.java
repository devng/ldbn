package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.AssignmentGenerator;
import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.core.Relation;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.io.AssignmentLoaderCallback;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.CheckSolutionDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public final class SolveAssignmentWidget extends AbsolutePanel 
	implements ClickListener, LoadAssignmentDialogCallback, AssignmentLoaderCallback {

	private static SolveAssignmentWidget inst;
	public static SolveAssignmentWidget get() {
		if (inst == null) {
			inst = new SolveAssignmentWidget();
		}
		return inst;
	}
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
	//3nf decomposition
	private DecompositionWidget decomposition3NF;
	//BCNF decomposition
	private DecompositionWidget decompositionBCNF;
	//disclosure widgets
	private DisclosureWidget dwGivenAttributes;
	private DisclosureWidget dwMinimalCover;
	private DisclosureWidget dwDecomposition2NF;
	private DisclosureWidget dwDecomposition3NF;
	private DisclosureWidget dwDecompositionBCNF;
	DisclosureWidget dwFDs;
	
	private SolveAssignmentWidget() {
		super();
		setWidth("100%");
		//this are not used, before an assignment has been loaded,  but
		//if they are not set a NullPointer or IllegalArgument exception
		//can be thrown
		domain = new DomainTable();
		fds = new ArrayList<FD>();
		
		//Controls
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
		InfoButton info = new InfoButton("sa-tab");
		info.setStyleName("att-img");
		HeaderWidget hw = new HeaderWidget();
		hw.add(newAssignment);
		hw.add(checkSolution);
		hw.add(showSolution);
		hw.add(info);
		add(hw);
		dwGivenAttributes = new DisclosureWidget("Given attributes", 
				givenAttributesWidget);
		add(dwGivenAttributes);
		//Given FDs
		givenFDsWidget = new GivenFDsWidget();
		dwFDs = new DisclosureWidget("Given FDs", 
				givenFDsWidget);
		add(dwFDs);
		//Minimal Cover
		minimalCoverWidget = new MinimalCoverWidget();
		dwMinimalCover = new DisclosureWidget("Find minimal cover of FDs", 
				minimalCoverWidget);
		add(dwMinimalCover); 
		//2NF
		decomposition2NF = new DecompositionWidget();
		dwDecomposition2NF = new DisclosureWidget("Decompose in 2 NF", decomposition2NF);
		add(dwDecomposition2NF);
		//3NF
		decomposition3NF = new DecompositionWidget();
		dwDecomposition3NF = new DisclosureWidget("Decompose in 3 NF", decomposition3NF);
		add(dwDecomposition3NF);
		//BCNF
		decompositionBCNF = new DecompositionWidget();
		dwDecompositionBCNF = new DisclosureWidget("Decompose in BCNF", decompositionBCNF);
		add(dwDecompositionBCNF);
		
		//start with  a loaded assignment
		this.domain = new DomainTable(); //this is necessary 
		givenAttributesWidget.setDomain(domain);
		Assignment a = AssignmentGenerator.generate();
		loadAssignment(a);
	}
	
	public DomainTable getDomainTable() {
		return domain;
	}
	
	public void onAssignmentLoaded(Assignment a) {
		loadAssignment(a);
	}

	public void onAssignmentLoadError() {}

	public void onClick(Widget sender) {
		if(sender == checkSolution) {
			checkSolution();
		} else if (sender == newAssignment) {
			LoadAssignmentDialog.get().load(this);
		} else if (sender == showSolution) {
			showSolution();
		}
	}

	public void onLoadCanceled() {}

	public void onLoaded(String id, String name) {
		AssignmentLoader.loadFromURL(id, this);
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
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		dialog.clearMsgs();
		dialog.msgTitle("Minimal Cover Check:");
		if (!Algorithms.equivalence(fds, minCovFDs)) {
			dialog.msgErr("wrong - fds are not equivalent to the given fds");
		} else if(deepCopy.size() != minCovFDs.size()) {
			dialog.msgErr("wrong - too much fds");
		} else if (!deepCopy.containsAll(minCovFDs)) { 
			// compute the minimal cover over the user input and compare it with
			// the user actual input, if it does not contain all FDs, then a 
			// FD was not minimal and it was computed and added to the list. 
			dialog.msgErr("wrong - some fds are not redused");
		} else {
			dialog.msgOK("right");
			isMinCoverRight = true;
		}
		//check 2nf
		dialog.msgTitle("2NF Decomposition Check:");
		if(!isMinCoverRight) { //TODO is this right????
//			dialog.msgWarn("Minimal cover was wrong, program will compute " +
//				"it, in order to check the decomposition solutions, but it " +
//				"wont show the solution,  Note that different minimal covers " +
//				"may give different solutions.");
			deepCopy = new ArrayList<FD>();
			for (FD fd2 : fds) {
				deepCopy.add(fd2);
			}
			Algorithms.minimalCover(deepCopy);
		}
		boolean isDependencyPreserving = false;
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, decomposition2NF.getRelations());
		if(isDependencyPreserving) {
			dialog.msgOK("Decomposition is dependency preserving");
		} else {
			dialog.msgErr("Decomposition is NOT dependency preserving");
		}
		//check 3nf
		dialog.msgTitle("3NF Decomposition Check:");
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, decomposition3NF.getRelations());
		if(isDependencyPreserving) {
			dialog.msgOK("Decomposition is dependency preserving");
		} else {
			dialog.msgErr("Decomposition is NOT dependency preserving");
		}
		//check bcnf
		dialog.msgTitle("BCNF Decomposition Check:");
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, decompositionBCNF.getRelations());
		if(isDependencyPreserving) {
			dialog.msgOK("Decomposition is dependency preserving");
		} else {
			dialog.msgWarn("Decomposition is NOT dependency preserving");
		}		
		
		
		
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
		dialog.center();
	}
	
	private void showSolution() {
		clearUserInput();
		//minimal cover
		List<FD> deepCopy = new ArrayList<FD>(fds.size());
		for (FD fd : fds) {
			deepCopy.add(fd.clone());
		}
		Algorithms.minimalCover(deepCopy); // TODO THIS IS A BUG
		Algorithms.minimalCover(deepCopy);
		for (FD fd : deepCopy) {
			FDWidget fdw = new FDWidget(true, fd);
			minimalCoverWidget.getFDHolderPanel().addFDWidget(fdw);
		}
		//2nf 3nf
		Relation r = new Relation();
		r.setAttributes(domain.createAttributeSet());
		r.setFDs(deepCopy);
		Collection<Relation> synthese = Algorithms.synthese(r, true);
		decomposition2NF.addRelationList(synthese);
		decomposition3NF.addRelationList(synthese);
		Collection<Relation> bcnf = Algorithms.findBCNF(synthese);
		decompositionBCNF.addRelationList(bcnf);
		
	}
	
	private void clearUserInput() {
		minimalCoverWidget.getFDHolderPanel().clearData();
		decomposition2NF.clearData();
		decomposition3NF.clearData();
		decompositionBCNF.clearData();
	}
	
	private void clearData() {
		clearUserInput();
		domain.clearData();
		givenFDsWidget.clearData();
		fds.clear();
	}
	
	private void loadAssignment(Assignment a) {
		clearData();
		restoreDefaultSize();
		this.domain = a.getDomain();
		givenAttributesWidget.setDomain(domain);
		this.fds = a.getFDs();
		
		givenFDsWidget.setFDs(fds);
	}
	
	private void restoreDefaultSize() {
		dwGivenAttributes.resetHeightToDefault();
		dwFDs.resetHeightToDefault();
		dwMinimalCover.resetHeightToDefault();
		dwDecomposition2NF.resetHeightToDefault();
		dwDecomposition3NF.resetHeightToDefault();
		dwDecompositionBCNF.resetHeightToDefault();
	}
}
