package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.AssignmentGenerator;
import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class SolveAssignmentWidget extends AbsolutePanel 
	implements ClickListener {

	private static SolveAssignmentWidget inst;
	//assignment variables  
	private AttributeNameTable domain;
	private List<FD> fds;
	//given attributes
	private Button checkSolution;
	private Button newAssignment;
	private GivenAttributesWidget givenAttributesWidget;
	//given fds
	private GivenFDsWidget givenFDsWidget;
	//minimal cover
	private FDHolderPanel minimalCoverFDs;
	private Button minCovAddFD;
	//2nf decomposition
	private List<RelationWidget> NF2Relations;
	private HorizontalPanel relationsPanel;
	private Button addRelation2NF;
	
	private SolveAssignmentWidget() {
		super();
		setWidth("100%");
		//this are not used, before an assigment has been loaded,  but
		//if they are not set a NullPointer or IllegalArgument exception
		//can be thrown
		domain = new AttributeNameTable();
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
		Button[] attUpBut = {newAssignment, checkSolution};
		DisclosureWidget dw = new DisclosureWidget("Given attributes", 
				givenAttributesWidget, attUpBut);
		add(dw);
		//Given FDs
		givenFDsWidget = new GivenFDsWidget();
		dw = new DisclosureWidget("Given FDs", 
				givenFDsWidget);
		add(dw);
		//Minimal Cover
		minimalCoverFDs = new FDHolderPanel();
		HorizontalPanel minCoverBut = new HorizontalPanel();
		minCovAddFD = new Button("Add new FD");
		minCovAddFD.addStyleName("min-cov-but");
		minCovAddFD.addClickListener(this);
		minCoverBut.add(minCovAddFD);
		minimalCoverFDs.add(minCovAddFD);
		dw = new DisclosureWidget("Find the minimal cover of FDs", 
				minimalCoverFDs);
		add(dw); 
		//Decomposition 2NF
		DecompositionWidget decW2NF = new DecompositionWidget();
		NF2Relations = decW2NF.getRelations();
		dw = new DisclosureWidget("Decompose in 2 NF", decW2NF); 
		add(dw);
		
		newAssignment();
	}
	
	private void newAssignment() {
		minimalCoverFDs.clearAll();
		//NF2DecompositionEditorWidget.clearAll();
		//NF3DecompositionEditorWidget.clearAll();
		Assignment a = AssignmentGenerator.generate();
		this.domain = a.getDomain();
		this.fds = a.getFDs();
		givenAttributesWidget.setDomain(domain);
		givenFDsWidget.setFDs(fds);
	}
	
	public static SolveAssignmentWidget get() {
		if (inst == null) {
			inst = new SolveAssignmentWidget();
		}
		return inst;
	}
	
	public void onClick(Widget sender) {
		if(sender == checkSolution) {
			//checkSolution();
			//AssignmentLoader.get().loadFromURL("");
//			Assignment a = RandomAssignmentGenerator.generate();
//			String xml = AssignmentSaver.buildXML(a);
//			AssignmentSaver.sendToSaveScript(xml, "test2");
		} else if (sender == newAssignment) {
			//newAssignment();
			//Assignment a = RandomAssignmentGenerator.generate();
			//String xml = AssignmentSaver.get().buildXML(a);
			//Log.debug(xml);
			//AssignmentSaver.get().sendToSaveScript(xml, "indiana jounes:;#?");
			//AssignmentLoader.get().loadFromURL("1");
			AssignmentLoader.loadAssignmentList();
		} else  if (sender == minCovAddFD) {
			FDEditorDialog.get().center();
			FDEditorDialog.get().setCurrentFDHolderPanel(minimalCoverFDs);
		} else if (sender == addRelation2NF) {
			RelationWidget r = new RelationWidget();
			NF2Relations.add(r);
			relationsPanel.add(r);
			FDEditorDialog.get().center();
			FDEditorDialog.get().setCurrentFDHolderPanel(r.fetFDHolderPanel());
		}
	}
	
	
	public List<FD> getFds() {
		return fds;
	}
	
	public void clearData() {
		minimalCoverFDs.clearAll();
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
		
		//AssignmentXML axml  = new AssignmentXML();
	}
	
	

}
