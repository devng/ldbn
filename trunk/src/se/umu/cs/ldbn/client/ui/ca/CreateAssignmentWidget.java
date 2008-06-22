package se.umu.cs.ldbn.client.ui.ca;

import java.util.Collection;
import java.util.List;

import se.umu.cs.ldbn.client.Assignment;
import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.io.AssignmentLoaderCallback;
import se.umu.cs.ldbn.client.io.AssignmentSaver;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.AttributeEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogCallback;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialog;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialogCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class CreateAssignmentWidget extends Composite 
	implements ClickListener, RenameDialogCallback, LoadAssignmentDialogCallback,
	AssignmentLoaderCallback {
	
	//assignment variables  
	private Assignment currentAssignment;
	private String loadedName;
	private String loadedId;
	
	private AbsolutePanel mainPanel;
	private EditableGivenAttributesWidget egas;
	private Button addAtts;
	
	private static CreateAssignmentWidget inst;
	private FDHolderPanel givenFDs;
	private Button addFDs;
	private Button newButton;
	private Button saveButton;
	private Button editButton;
	private Label editMode;
	private DisclosureWidget dwGivenAttributes;
	private DisclosureWidget dwGivenFDs;
	
	private CreateAssignmentWidget() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		
		HeaderWidget hw = new HeaderWidget();
		newButton = new Button("New");
		newButton.addClickListener(this);
		saveButton = new Button("Save");
		saveButton.addClickListener(this);
		editButton = new Button("Edit");
		editButton.addClickListener(this);
		newButton.setStyleName("att-but");
		CommonFunctions.setCursorPointer(newButton);
		saveButton.setStyleName("att-but");
		CommonFunctions.setCursorPointer(saveButton);
		editButton.setStyleName("att-but");
		CommonFunctions.setCursorPointer(editButton);
		InfoButton info = new InfoButton("example");
		info.setStyleName("att-img");
		
		hw.add(newButton);
		hw.add(saveButton);
		hw.add(editButton);
		hw.add(info);
		editMode = new Label("Edit mode - Assigment will be updated in the DB");
		editMode.setVisible(false);
		hw.add(editMode);
		mainPanel.add(hw);
		
		VerticalPanel vp = new VerticalPanel();
		addAtts = new Button ("Add/Remove Attributes");
		addAtts.setStyleName("min-cov-but");
		addAtts.addClickListener(this);
		CommonFunctions.setCursorPointer(addAtts);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.add(addAtts);
		hp.add(new InfoButton("example"));
		vp.add(hp);
		egas = new EditableGivenAttributesWidget();
		vp.add(egas);
		
		dwGivenAttributes = new DisclosureWidget("Given attributes", vp);
		mainPanel.add(dwGivenAttributes);
		
		givenFDs = new FDHolderPanel();
		hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		addFDs = new Button("Add FDs");
		addFDs.setStyleName("min-cov-but");
		addFDs.addClickListener(this);
		CommonFunctions.setCursorPointer(addFDs);
		hp.add(addFDs);
		hp.add(new InfoButton("example"));
		givenFDs.add(hp);
		dwGivenFDs = new DisclosureWidget("Given FDs", givenFDs);
		mainPanel.add(dwGivenFDs);
		
		initWidget(mainPanel);
	}
	
	public static CreateAssignmentWidget get() {
		if (inst == null) {
			inst = new CreateAssignmentWidget();
		}
		return inst;
	}
	
	public DomainTable getDomain() {
		return egas.getDomain();
	}

	public void clearData() {
		egas.getDomain().clearData();
		givenFDs.clearData();
		currentAssignment = null;
		editMode.setVisible(false);
	}
	
	public void onClick(Widget sender) {
		if(sender == addAtts) {
			AttributeEditorDialog atd = AttributeEditorDialog.get();
			atd.center();
		} else if (sender == addFDs) {
			FDEditorDialog fded = FDEditorDialog.get();
			fded.center(); //always center first
			fded.setCurrentFDHolderPanel(givenFDs);
			fded.setCurrentDomain(egas.getDomain());
			
		}  else if (sender == newButton) {
			clearData();
			restoreDefaultSize();
			loadedId = null;
			loadedName = null;
		} else if (sender == saveButton) {
			DomainTable domain = egas.getDomain();
			if(domain.size() < 1) {
				Window.alert("You did not enter any attributes.");
				return;
			}
			List<FD> fds = givenFDs.getFDs();
			if(fds.size() < 1) {
				Window.alert("You did not enter any FDs.");
				return;
				
			}
			currentAssignment = new Assignment(domain, fds);
			RenameDialog.get().rename(this);
		} else if (sender == editButton) {
			LoadAssignmentDialog.get().load(this);
		}
		
	}

	public String getOldName() {
		if(loadedName != null) {
			return loadedName;
		}
		return "";
	}
	
	public Collection getTakenNames() {
		return null;
	}
	
	public void setNewName(String s) {
		saveAssignemnt(s);
		
	}

	public void onRenameCanceled() {
		currentAssignment = null;
	}
	
	public void onLoaded(String id, String name) {
		loadedId = id;
		loadedName = name;
		AssignmentLoader.loadFromURL(id, this);
	}
	
	public void onLoadCanceled() {}
	
	public void onAssignmentLoadError() {
		editMode.setVisible(false);
		loadedId = null;
		loadedName = null;
	}

	public void onAssignmentLoaded(Assignment a) {
		clearData();
		restoreDefaultSize();
		egas.getDomain().loadDomainTable(a.getDomain());
		for (FD fd : a.getFDs()) {
			givenFDs.addFDWidget(new FDWidget(true, fd));
		}
		editMode.setVisible(true);
	}
	
	private void saveAssignemnt(String name) {
		if(currentAssignment != null) {
			String xml = AssignmentSaver.buildXML(currentAssignment);
			AssignmentSaver.sendToSaveScript(xml, name, loadedId == null ? "" : loadedId);
			currentAssignment  = null;
			editMode.setVisible(false);
			loadedId = null;
			loadedName = null;
			//TODO Use callbacks and on success set values to null 
		} else {
			Log.warn("CurrentAsigment = null. Could not save assigment.");
		}
	}
	
	private void restoreDefaultSize() {
		dwGivenAttributes.resetHeightToDefault();
		dwGivenFDs.resetHeightToDefault();
	}
}
