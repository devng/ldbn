package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.AssignmentGenerator;
import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.AttributeSet;
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
	//cache
	/**
	 * Holds the partial FD cover for every user created relation.
	 * The key is the hash code  of the relation attribute set
	 */
	private HashMap<Integer, List<FD>> cacheFD;
	private HashMap<Integer, List<AttributeSet>> cacheKeys;
	//assignment variables  
	private DomainTable domain;
	private AttributeSet domainAsAttSet;
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
		//cache
		cacheFD = new HashMap<Integer, List<FD>>();
		cacheKeys = new HashMap<Integer, List<AttributeSet>>();
		
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
		//minimal cover check
		List<FD> minCovFDs = minimalCoverWidget.getFDHolderPanel().getFDs();
		List<FD> deepCopy = new ArrayList<FD>(minCovFDs.size());
		for (FD fd : minCovFDs) {
			deepCopy.add(fd.clone());
		}
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
		}
		//2nf check
		AttributeSet initAtts = domain.createAttributeSet();
		boolean isDependencyPreserving = false;
		dialog.msgTitle("2NF Decomposition Check:");
		List<Relation> relations = decomposition2NF.getRelations();
		checkForLossless(fds, initAtts, relations);
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, decomposition2NF.getRelations());
		if(isDependencyPreserving) {
			dialog.msgOK("Decomposition is dependency preserving");
		} else {
			dialog.msgErr("Decomposition is NOT dependency preserving");
		}
		updateCache(relations);
		checkFDInput(relations);
		checkKeyInput(relations);
		updateRelations(relations);
		boolean isIn2NF = Algorithms.isIn2NF(relations);
		System.out.println(isIn2NF);
		//3nf check
		dialog.msgTitle("3NF Decomposition Check:");
		relations = decomposition3NF.getRelations();
		checkForLossless(fds, initAtts, relations);
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, decomposition3NF.getRelations());
		if(isDependencyPreserving) {
			dialog.msgOK("Decomposition is dependency preserving");
		} else {
			dialog.msgErr("Decomposition is NOT dependency preserving");
		}
		updateCache(relations);
		checkFDInput(relations);
		checkKeyInput(relations);
		//bcnf check
		dialog.msgTitle("BCNF Decomposition Check:");
		relations = decompositionBCNF.getRelations();
		checkForLossless(fds, initAtts, relations);
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, decompositionBCNF.getRelations());
		if(isDependencyPreserving) {
			dialog.msgOK("Decomposition is dependency preserving");
		} else {
			dialog.msgWarn("Decomposition is NOT dependency preserving");
		}		
		updateCache(relations);
		checkFDInput(relations);
		checkKeyInput(relations);
		dialog.center();
	}
	
	
	private boolean checkFDInput(List<Relation> rel) {
		boolean resultFD = true;
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		for (Relation r : rel) {
			List<FD> fdsRBR = cacheFD.get(r.getAttrbutes().hashCode());
			for (FD fd : r.getFds()) {
				if (!Algorithms.member(fd, fdsRBR)) {
					resultFD = false;
					dialog.msgErr("The FD "+fd.toString()+" for \""+r.getName()+"\" are incorrect");
					break;
				}
					
			}
		}
		return resultFD;
	}
	
	private boolean checkKeyInput(List<Relation> rel) {
		boolean result = false;
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		for (Relation r : rel) {
			List<AttributeSet> keys = cacheKeys.get(r.getAttrbutes().hashCode());
			boolean isKeyFound = false;
			for (AttributeSet k : keys) {
				if(k.equals(r.getSuperKey())) {
					isKeyFound = true;
					break;
				}
			}
			if(!isKeyFound) {
				dialog.msgErr("The Key for \""+r.getName()+"\" is incorrect");
				result = false;
			}
			
		}
		return result;
	}
	
	private void checkForLossless(List<FD> fds, AttributeSet initAtts, List<Relation> relations) {
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		boolean isLossless = Algorithms.isLossless(fds, initAtts, relations);
		if(isLossless) {
			dialog.msgOK("Decomposition is lossless");
		} else {
			dialog.msgErr("Decomposition is NOT lossless");
		}
	}
	
	private void updateCache(Collection<Relation> rel) {
		for (Relation r : rel) {
			int i = r.getAttrbutes().hashCode();
			if(!cacheFD.containsKey(i)) {
				List<FD> fdsRBR = Algorithms.reductionByResolution(domainAsAttSet, fds, r.getAttrbutes());
				Algorithms.minimalCover(fdsRBR);
				cacheFD.put(i, fdsRBR);
			}
			if(!cacheKeys.containsKey(i)) {
				List<FD> fdsRBR = cacheFD.get(i);
				List<AttributeSet> keys = Algorithms.findAllKeyCandidates(fdsRBR, r.getAttrbutes());
				cacheKeys.put(i, keys);
			}
		}
	}
	
	
	private void updateRelations(List<Relation> rel) {
		for (Relation r : rel) {
			r.setFDs(cacheFD.get(r.getAttrbutes().hashCode()));
			r.setKeyCandidates(cacheKeys.get(r.getAttrbutes().hashCode()));
		}
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
		cacheFD.clear();
		cacheKeys.clear();
		domain.clearData();
		givenFDsWidget.clearData();
		fds.clear();
	}
	
	private void loadAssignment(Assignment a) {
		clearData();
		restoreDefaultSize();
		this.domain = a.getDomain();
		this.domainAsAttSet = domain.createAttributeSet();
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
