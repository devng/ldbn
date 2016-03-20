package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.io.AssignmentListEntry;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.io.AssignmentLoaderCallback;
import se.umu.cs.ldbn.client.io.Comment;
import se.umu.cs.ldbn.client.io.CommentCallback;
import se.umu.cs.ldbn.client.io.CommentListEntry;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.CheckSolutionDialog;
import se.umu.cs.ldbn.client.ui.dialog.CheckSolutionDialog.MSG_TYPE;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogCallback;
import se.umu.cs.ldbn.client.utils.AssignmentGenerator;
import se.umu.cs.ldbn.client.utils.Common;
import se.umu.cs.ldbn.shared.core.Algorithms;
import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.core.AttributeSet;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.FD;
import se.umu.cs.ldbn.shared.core.Relation;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public final class SolveAssignmentWidget extends AbsolutePanel
	implements ClickHandler, LoadAssignmentDialogCallback,
	AssignmentLoaderCallback, CommentCallback {

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
	public AttributeSet domainAsAttSet;
	public List<FD> fds;
	private List<AttributeSet> originalKeys;
	//given attributes
	private Button checkSolution;
	private Button loadAssignment;
	private Button showSolution;
	private GivenAttributesWidget givenAttributesWidget;
	//given fds
	private GivenFDsWidget givenFDsWidget;
	//find key
	private FindKeyWidget findKeyWidget;
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
	private DisclosureWidget dwFindKey;
	private DisclosureWidget dwMinimalCover;
	private DisclosureWidget dwDecomposition2NF;
	private DisclosureWidget dwDecomposition3NF;
	private DisclosureWidget dwDecompositionBCNF;
	DisclosureWidget dwFDs;
	private DisclosureWidget dwComments;
	//imports
	private Image import2NF;
	private Image import3NF;
	//current assignment
	private String curAssinmentId;
	private List<FD> minCoverF;
	//Label assignment name
	private HTML assignmentName;

	private SolveAssignmentWidget() {
		super();
		curAssinmentId = "1";
	}

	private void init() {
		setWidth("100%");
		//this are not used, before an assignment has been loaded,  but
		//if they are not set a NullPointer or IllegalArgument exception
		//can be thrown
		domain = new DomainTable();
		fds = new ArrayList<>();

		//Controls
		givenAttributesWidget = new GivenAttributesWidget();
		loadAssignment = new Button(I18N.constants().sawLoadAssignmentBut());
		loadAssignment.setStyleName("att-but");
		Common.setCursorPointer(loadAssignment);
		loadAssignment.addClickHandler(this);
		checkSolution = new Button(I18N.constants().sawCheckSolutionBut());
		checkSolution.setStyleName("att-but");
		Common.setCursorPointer(checkSolution);
		checkSolution.addClickHandler(this);
		showSolution = new Button(I18N.constants().sawShowSolutionBut());
		showSolution.setStyleName("att-but");
		Common.setCursorPointer(showSolution);
		showSolution.addClickHandler(this);
		InfoButton info = new InfoButton("sa-tab");
		info.setStyleName("att-img");
		assignmentName = new HTML();
		assignmentName.setStyleName("att-but");

		HeaderWidget hw = new HeaderWidget();
		hw.add(loadAssignment);
		hw.add(checkSolution);
		hw.add(showSolution);
		hw.add(info);
		Image trenner = new Image(Common.getResourceUrl("img/trenner.jpg"));
		trenner.setStyleName("att-but");
		hw.add(trenner);
		hw.add(assignmentName);
		add(hw);

		dwGivenAttributes = new DisclosureWidget(I18N.constants().givenAtt(),
				givenAttributesWidget);
		add(dwGivenAttributes);
		//Given FDs
		givenFDsWidget = new GivenFDsWidget();
		dwFDs = new DisclosureWidget(I18N.constants().givenFDs(), givenFDsWidget);
		add(dwFDs);
		//find key
		findKeyWidget = new FindKeyWidget();
		dwFindKey = new DisclosureWidget("Find a Key", findKeyWidget);
		add(dwFindKey);
		//Minimal Cover
		minimalCoverWidget = new MinimalCoverWidget();
		dwMinimalCover = new DisclosureWidget(I18N.constants().sawMinCoverTitle(),
				minimalCoverWidget);
		add(dwMinimalCover);
		//2NF
		decomposition2NF = new DecompositionWidget();
		dwDecomposition2NF = new DisclosureWidget(I18N.messages().sawDecomposeInto("2NF"), decomposition2NF);
		add(dwDecomposition2NF);
		//3NF
		import2NF = new Image(Common.getResourceUrl("img/import.png"), 0, 0, 15, 15);
		import2NF.addClickHandler(this);
		import2NF.setTitle(I18N.constants().sawImportRelations());
		Common.setCursorPointer(import2NF);
		import2NF.addMouseOverHandler(event -> import2NF.setVisibleRect(15, 0, 15, 15));
		import2NF.addMouseOutHandler(event -> import2NF.setVisibleRect(0, 0, 15, 15));
		decomposition3NF = new DecompositionWidget();
		Widget[] tmp = new Widget[decomposition3NF.getAdditionalControlls().length+1];
		for (int i = 1; i < tmp.length; i++) {
			tmp[i] = decomposition3NF.getAdditionalControlls()[i-1];
		}
		tmp[0] = import2NF;
		dwDecomposition3NF = new DisclosureWidget(I18N.messages().sawDecomposeInto("3NF"), decomposition3NF, tmp);
		add(dwDecomposition3NF);
		//BCNF
		import3NF = new Image(Common.getResourceUrl("img/import.png"), 0, 0, 15, 15);
		import3NF.addClickHandler(this);
		import3NF.setTitle(I18N.constants().sawImportRelations());
		Common.setCursorPointer(import3NF);
		import3NF.addMouseOverHandler(event -> import3NF.setVisibleRect(15, 0, 15, 15));
		import3NF.addMouseOutHandler(event -> import3NF.setVisibleRect(0, 0, 15, 15));
		decompositionBCNF = new DecompositionWidget();
		tmp = new Widget[decompositionBCNF.getAdditionalControlls().length+1];
		for (int i = 1; i < tmp.length; i++) {
			tmp[i] = decompositionBCNF.getAdditionalControlls()[i-1];
		}
		tmp[0] = import3NF;
		dwDecompositionBCNF = new DisclosureWidget(I18N.messages().sawDecomposeInto("BCNF"), decompositionBCNF, tmp);
		add(dwDecompositionBCNF);
		//comments
		dwComments = new DisclosureWidget(I18N.constants().sawUserCommentsTitle(), CommentsWidget.get());
		add(dwComments);
		//cache
		cacheFD = new HashMap<>();
		cacheKeys = new HashMap<>();

		//start with  a loaded assignment
		this.domain = new DomainTable(); //this is necessary
		givenAttributesWidget.setDomain(domain);
		Assignment a = AssignmentGenerator.generate();
		loadAssignment(a);
	}

	public DomainTable getDomainTable() {
		return domain;
	}

	public String getCurrentAssignmentId() {
		return curAssinmentId;
	}

	public void onAssignmentLoaded(Assignment a) {
		loadAssignment(a);
	}

	public void onAssignmentLoadError() {}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if(sender == checkSolution) {
			Main.get().showGlassPanelLoading();
			Scheduler.get().scheduleDeferred(() -> checkSolution());
		} else if (sender == loadAssignment) {
			LoadAssignmentDialog.get().load(this);
		} else if (sender == showSolution) {
			if (Window.confirm("Are you sure you want to see the solution?\n" +
					"This will erase all of your input!")) {
				Main.get().showGlassPanelLoading();
				Scheduler.get().scheduleDeferred(() -> showSolution());
			}
		} else if (sender == import2NF) {
			boolean b = Window.confirm(I18N.messages().sawImportRelationsConfirm("2NF"));
			if(b) {
				decomposition3NF.addRelationList(Common.
						deepCopyDecomposition(decomposition2NF.getRelations()));
			}
		} else if (sender == import3NF) {
			boolean b = Window.confirm(I18N.messages().sawImportRelationsConfirm("3NF"));
			if(b) {
				decompositionBCNF.addRelationList(Common.
						deepCopyDecomposition(decomposition3NF.getRelations()));
			}
		}
	}

	public void onLoadCanceled() {}

	public void onLoaded(AssignmentListEntry entry) {
		curAssinmentId = entry.getId();
		AssignmentLoader.get().loadFromURL(entry.getId(), this);
	}

	public String getAssignmentID() {
		return curAssinmentId;
	}

	public String getComment() {
		return null;
	}

	public void onCommentsReceived(List<CommentListEntry> comments,
			String assignentID) {
		CommentsWidget.get().addComments(comments);
	}

	protected void onAttach() {
		super.onAttach();
		init();
	}

	private void checkSolution() {
		//minimal cover check
		List<FD> minCovFDs = minimalCoverWidget.getFDHolderPanel().getFDs();
		minCoverF = new ArrayList<>(minCovFDs.size());
		for (FD fd : minCovFDs) {
			minCoverF.add(fd.clone());
		}
		Algorithms.minimalCover(minCoverF);
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		dialog.clearMsgs();

		AttributeSet key = findKeyWidget.getKey();

		//check key
		dialog.msgTitle("Key Check:");
		if(key.isEmpty()) {
			dialog.msgWarn("You have not specified a key for the initial relation.");
		} else {
			//TODO Do it faster
//			Relation tmp = new Relation();
//			tmp.setAttributes(domainAsAttSet);
//			tmp.setFDs(fds);
//			tmp.setPrimaryKey(key);
//			ArrayList<Relation> tmpList = new ArrayList<Relation>(1);
//			tmpList.add(tmp);
//			checkKeyInput(tmpList);
			if(originalKeys == null) {
				originalKeys = Algorithms.findAllKeyCandidates(fds, domainAsAttSet);
			}
			boolean keyFound = false;
			boolean isSuperKey = false;
			for (AttributeSet oKey : originalKeys) {
				if(key.isSubSetOf(oKey)) {
					isSuperKey = true;
				}
				if(oKey.equals(key)) {
					keyFound = true;
					break;
				}
			}
			if(keyFound) {
				dialog.msgOK("The provided key is correct.");
			} else {
				if(isSuperKey) {
					dialog.msgWarn("The provided key is not minimal.");
				} else {
					dialog.msgErr("The provided key is incorrect.");
				}
			}
		}

		dialog.msgTitle(I18N.constants().sawMinCoverCheck());
		if(minCovFDs.size() == 0) {
			dialog.msgWarn(I18N.constants().sawNoFDsWarn());
		} else if (!Algorithms.equivalence(fds, minCovFDs)) {
			dialog.msgErr(I18N.constants().sawFDsNotEquivalent());
		} else if(minCoverF.size() != minCovFDs.size()) {
			dialog.msgErr(I18N.constants().sawFDsTooMany());
		} else if (!minCoverF.containsAll(minCovFDs)) {
			// compute the minimal cover over the user input and compare it with
			// the user actual input, if it does not contain all FDs, then a
			// FD was not minimal and it was computed and added to the list.
			dialog.msgErr(I18N.constants().sawFDsNotReduced());
		} else {
			dialog.msgOK(I18N.constants().sawRight());
		}
		//2nf check
		dialog.msgTitle(I18N.messages().sawNFDecompositionCheck("2NF"));
		List<Relation> relations = decomposition2NF.getRelations();
		updateCache(relations);
//		if (checkFDInput(relations)) {
//			if(checkForLossless(relations)) {
//				if (checkDependencyPreservation(relations, false)) {
//					if (checkKeyInput(relations)) {
//						updateRelations(relations);
//						boolean isIn2NF = Algorithms.isIn2NF(relations);
//						if(isIn2NF) {
//							dialog.msgOK(I18N.messages().sawDecompositionIsInNF("2NF"));
//						} else {
//							dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("2NF"));
//						}
//					}
//				}
//			}
//		}
		if (!isRelationsListEmpry(relations)) {
			boolean checkFDs = checkFDInput(relations);
			boolean checkLossless = checkForLossless(relations);
			boolean checkDP = checkDependencyPreservation(relations, false);
			boolean checkKey = checkKeyInput(relations);
			if (checkKey) {
				updateRelations(relations);
			}
			//BCNF must not be dependency preserving
			boolean errorFree = checkFDs && checkLossless && checkDP && checkKey;
			if (!errorFree) {
				dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("2NF") + " See previous error messages.");
			} else {
				boolean isIn2NF = Algorithms.isIn2NF(relations, getDomainTable());
				if(isIn2NF) {
					dialog.msgOK(I18N.messages().sawDecompositionIsInNF("2NF"));
				} else {
					dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("2NF"));
				}
			}
		}

		//3nf check
		dialog.msgTitle(I18N.messages().sawNFDecompositionCheck("3NF"));
		relations = decomposition3NF.getRelations();
		updateCache(relations);
//		if (checkFDInput(relations)) {
//			if(checkForLossless(relations)) {
//				if (checkDependencyPreservation(relations, false)) {
//					if (checkKeyInput(relations)) {
//						updateRelations(relations);
//						boolean isIn3NF = Algorithms.isIn3NF(relations);
//						if(isIn3NF) {
//							dialog.msgOK(I18N.messages().sawDecompositionIsInNF("3NF"));
//						} else {
//							dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("3NF"));
//						}
//					}
//				}
//			}
//		}
		if (!isRelationsListEmpry(relations)) {
			boolean checkFDs = checkFDInput(relations);
			boolean checkLossless = checkForLossless(relations);
			boolean checkDP = checkDependencyPreservation(relations, false);
			boolean checkKey = checkKeyInput(relations);
			if (checkKey) {
				updateRelations(relations);
			}
			//BCNF must not be dependency preserving
			boolean errorFree = checkFDs && checkLossless && checkDP && checkKey;
			if (!errorFree) {
				dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("3NF") + " See previous error messages.");
			} else {
				boolean isIn3NF = Algorithms.isIn3NF(relations, getDomainTable());
				if(isIn3NF) {
					dialog.msgOK(I18N.messages().sawDecompositionIsInNF("3NF"));
				} else {
					dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("3NF"));
				}
			}

		}


		//bcnf check
		dialog.msgTitle(I18N.messages().sawNFDecompositionCheck("BCNF"));
		relations = decompositionBCNF.getRelations();
		updateCache(relations);
//		if (checkFDInput(relations)) {
//			if(checkForLossless(relations)) {
//				checkDependencyPreservation(relations, true);
//				if (checkKeyInput(relations)) {
//					updateRelations(relations);
//					boolean isInBCNF = Algorithms.isInBCNF(relations);
//					if(isInBCNF) {
//						dialog.msgOK(I18N.messages().sawDecompositionIsInNF("BCNF"));
//					} else {
//						dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("BCNF"));
//					}
//				}
//			}
//		}

		if (!isRelationsListEmpry(relations)) {
			boolean checkFDs = checkFDInput(relations);
			boolean checkLossless = checkForLossless(relations);
			checkDependencyPreservation(relations, true);
			boolean checkKey = checkKeyInput(relations);
			if (checkKey) {
				updateRelations(relations);
			}
			//BCNF must not be dependency preserving
			boolean errorFree = checkFDs && checkLossless && checkKey;
			if (!errorFree) {
				dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("BCNF") + " See previous error messages.");
			} else {
				boolean isInBCNF = Algorithms.isInBCNF(relations);
				if(isInBCNF) {
					dialog.msgOK(I18N.messages().sawDecompositionIsInNF("BCNF"));
				} else {
					dialog.msgErr(I18N.messages().sawDecompositionIsNotInNF("BCNF"));
				}
			}
		}

		Main.get().hideGlassPanel();
		dialog.center();
	}

	private boolean isRelationsListEmpry(List<Relation> rel) {
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		if(rel == null || rel.size() == 0){
			dialog.msgWarn(I18N.constants().sawNoRelationWarn());
			return true;
		}
		return false;
	}

	//this is unnecessary it is done by checking dependency preservation
	private boolean checkFDInput(List<Relation> rel) {
		CheckSolutionDialog dialog = CheckSolutionDialog.get();

		boolean resultFD = true;

		for (Relation r : rel) {
			List<FD> fdsRBR = cacheFD.get(r.getAttributes().hashCode());
			if(!Algorithms.equivalence(fdsRBR, r.getFds())) {
				resultFD = false;
				dialog.msgErr(I18N.messages().sawFDsNotClosure(r.getName()));
				break;
			}
		}
		if(resultFD) {
			dialog.msgOK(I18N.constants().sawFDsCorrect());
		}
		return resultFD;
	}

	private boolean checkDependencyPreservation(List<Relation> rel, boolean displayOnlyWarning) {
		boolean isDependencyPreserving = false;
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		isDependencyPreserving = Algorithms.isDependencyPreserving(fds, rel);
		if(isDependencyPreserving) {
			dialog.msgOK(I18N.constants().sawDecompositionDP());
		} else {

			dialog.msg(I18N.constants().sawDecompositionNDP(), displayOnlyWarning ? MSG_TYPE.warn : MSG_TYPE.error);
		}


		return isDependencyPreserving;
	}

	private boolean checkKeyInput(List<Relation> rel) {
		boolean result = true;
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		for (Relation r : rel) {
			List<AttributeSet> keys = cacheKeys.get(r.getAttributes().hashCode());
			AttributeSet pk = r.getPrimaryKey();
			pk.andOperator(r.getAttributes());
			boolean isKeyFound = false;
			for (AttributeSet k : keys) {
				if(!k.isEmpty() && k.equals(r.getPrimaryKey())) {
					isKeyFound = true;
					break;
				}
			}
			if(!isKeyFound) {
				dialog.msgErr(I18N.messages().sawKeyWrong(r.getName()));
				result = false;
			}

		}
		if(result) {
			dialog.msgOK(I18N.constants().sawKeysCorrect());
		}
		return result;
	}

	private boolean checkForLossless(List<Relation> relations) {
		CheckSolutionDialog dialog = CheckSolutionDialog.get();
		boolean isLossless = Algorithms.isLossless(fds, domainAsAttSet, relations);
		if(isLossless) {
			dialog.msgOK(I18N.constants().sawDecompositionLossless());
		} else {
			dialog.msgErr(I18N.constants().sawDecompositionNotLossless());
		}
		return isLossless;
	}

	private void updateCache(Collection<Relation> rel) {
		for (Relation r : rel) {
			int i = r.getAttributes().hashCode();
			if(!cacheFD.containsKey(i)) {
				List<FD> fdsRBR = Algorithms.reductionByResolution(domainAsAttSet, fds, r.getAttributes());
				Algorithms.minimalCover(fdsRBR);
				cacheFD.put(i, fdsRBR);
			}
			if(!cacheKeys.containsKey(i)) {
				List<FD> fdsRBR = cacheFD.get(i);
				List<AttributeSet> keys = Algorithms.findAllKeyCandidates(fdsRBR, r.getAttributes());
				cacheKeys.put(i, keys);
			}
		}
	}


	private void updateRelations(Collection<Relation> rel) {
		for (Relation r : rel) {
			r.setFDs(cacheFD.get(r.getAttributes().hashCode()));
			r.setKeyCandidates(cacheKeys.get(r.getAttributes().hashCode()));
		}
	}

	private void showSolution() {
		clearUserInput();
		//initial relation key
		if(originalKeys == null) {
			originalKeys = Algorithms.findAllKeyCandidates(fds, domainAsAttSet);
		}
		if(!originalKeys.isEmpty()) {
			findKeyWidget.setKey(originalKeys.get(0));
		}

		//minimal cover
		List<FD> deepCopy = new ArrayList<>(fds.size());
		for (FD fd : fds) {
			deepCopy.add(fd.clone());
		}
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

		decomposition2NF.addRelationList(Common.deepCopyDecomposition(synthese));
		decomposition3NF.addRelationList(Common.deepCopyDecomposition(synthese));
		//bcnf
		updateCache(synthese);
		updateRelations(synthese);
		Collection<Relation> bcnf = Algorithms.findBCNF(synthese);
		for (Relation rel : bcnf) {
			List<FD> bcnfFDs = rel.getFds();
			Algorithms.minimalCover(bcnfFDs);
		}
		decompositionBCNF.addRelationList(bcnf);
		Main.get().hideGlassPanel();
	}

	private void clearUserInput() {
		minimalCoverWidget.getFDHolderPanel().clearData();
		decomposition2NF.clearData();
		decomposition3NF.clearData();
		decompositionBCNF.clearData();
		findKeyWidget.clearData();
	}

	private void clearData() {
		clearUserInput();
		cacheFD.clear();
		cacheKeys.clear();
		//TODO domain.clearData() causes a bug, if we use the same
		//domain table in the create assignment tab -> we have to
		//implement clone for this class in the future.
		//domain.clearData();
		givenFDsWidget.clearData();
		fds.clear();
		originalKeys = null;
		CommentsWidget.get().clearData();
	}

	public void loadAssignment(Assignment a) {
		clearData();
		restoreDefaultSize();
		this.domain = a.getDomain();
		this.domainAsAttSet = domain.createAttributeSet();
		givenAttributesWidget.setDomain(domain);
		this.fds = a.getFDs();
		givenFDsWidget.setFDs(fds);
		String name = a.getName();
		if (name != null) {
			this.assignmentName.setHTML("Assignment: <i>"+a.getName()+"</i>");
		} else {
			this.assignmentName.setText("");
		}

		Scheduler.get().scheduleDeferred(() -> Comment.get().send(SolveAssignmentWidget.this));
	}

	private void restoreDefaultSize() {
		dwGivenAttributes.resetHeightToDefault();
		dwFDs.resetHeightToDefault();
		dwMinimalCover.resetHeightToDefault();
		dwDecomposition2NF.resetHeightToDefault();
		dwDecomposition3NF.resetHeightToDefault();
		dwDecompositionBCNF.resetHeightToDefault();
	}

	public boolean checkUserRights() {
		return false;
	}
}
