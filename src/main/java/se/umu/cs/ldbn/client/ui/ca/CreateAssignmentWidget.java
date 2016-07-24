package se.umu.cs.ldbn.client.ui.ca;

import java.util.Collection;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.io.*;
import se.umu.cs.ldbn.client.rest.AssignmentsRestClient;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.DomainTableEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogCallback;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogFilter;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialog;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialogCallback;
import se.umu.cs.ldbn.client.ui.dialog.UploadDialog;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.ui.visualization.VisualizationWindow;
import se.umu.cs.ldbn.client.utils.Common;
import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.FD;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

public final class CreateAssignmentWidget extends Composite 
	implements ClickHandler, RenameDialogCallback, LoadAssignmentDialogCallback,
	LoadAssignmentDialogFilter, AssignmentLoaderCallback, LoginListener, MethodCallback<AssignmentDto> {
	
	//assignment variables  
	private Assignment currentAssignment;
	private String loadedName;
	private Integer loadedId;
	
	private AbsolutePanel mainPanel;
	private EditableGivenAttributesWidget egas;
	private Button addAtts;
	
	private static CreateAssignmentWidget inst;
	private FDHolderPanel givenFDs;
	private Button addFDs;
	private Button newButton;
	private Button saveButton;
	private Button editButton;
	private Button exportButton;
	private Button importButton;
	private Button loadInSATabButton;
	private Label editMode;
	private Label loginFirst;
	private DisclosureWidget dwGivenAttributes;
	private DisclosureWidget dwGivenFDs;
	private FormPanel downloadForm;
	private Hidden hidenXML;
	private Hidden hidenXMLFilename;
	
	private CreateAssignmentWidget() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		
		HeaderWidget hw = new HeaderWidget();
		loadInSATabButton = new Button(I18N.constants().loadInSABut());
		loadInSATabButton.addClickHandler(this);
		newButton = new Button(I18N.constants().newBut());
		newButton.addClickHandler(this);
		saveButton = new Button(I18N.constants().saveBut());
		saveButton.addClickHandler(this);
		editButton = new Button(I18N.constants().editBut());
		editButton.addClickHandler(this);
		exportButton = new Button(I18N.constants().exportBut());
		exportButton.addClickHandler(this);
		importButton = new Button(I18N.constants().importBut());
		importButton.addClickHandler(this);
		
		newButton.setStyleName("att-but");
		Common.setCursorPointer(newButton);
		saveButton.setStyleName("att-but");
		Common.setCursorPointer(saveButton);
		editButton.setStyleName("att-but");
		Common.setCursorPointer(editButton);
		exportButton.setStyleName("att-but");
		Common.setCursorPointer(importButton);
		importButton.setStyleName("att-but");
		Common.setCursorPointer(exportButton);
		loadInSATabButton.setStyleName("att-but");
		Common.setCursorPointer(loadInSATabButton);
		
		InfoButton info = new InfoButton("ca-tab");
		info.setStyleName("att-img");
		
		hw.add(loadInSATabButton);
		Image trenner = new Image(Common.getResourceUrl("img/trenner.jpg"));
		trenner.setStyleName("att-but");
		hw.add(trenner);
		hw.add(newButton);
		hw.add(saveButton);
		hw.add(editButton);
		hw.add(exportButton);
		hw.add(importButton);
		loginFirst = new Label(I18N.constants().loginFirst());
		loginFirst.setStyleName("att-but");
		hw.add(loginFirst);
		trenner = new Image(Common.getResourceUrl("img/trenner.jpg"));
		trenner.setStyleName("att-but");
		editMode = new Label(I18N.constants().editMode());
		editMode.setVisible(false);
		editMode.setStyleName("att-but");
		hw.add(editMode);
		hw.add(trenner);
		hw.add(info);
		mainPanel.add(hw);
		//given attributes
		VerticalPanel vp = new VerticalPanel();
		addAtts = new Button (I18N.constants().addRemoveAtt());
		addAtts.setStyleName("min-cov-but");
		addAtts.addClickHandler(this);
		Common.setCursorPointer(addAtts);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.add(addAtts);
		hp.add(new InfoButton("givenatt-ca"));
		vp.add(hp);
		egas = new EditableGivenAttributesWidget();
		vp.add(egas);
		
		dwGivenAttributes = new DisclosureWidget(I18N.constants().givenAtt(), vp);
		mainPanel.add(dwGivenAttributes);
		//Given FDs
		givenFDs = new FDHolderPanel();
		egas.getDomain().addListener(givenFDs);
		hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		addFDs = new Button(I18N.constants().addFDs());
		addFDs.setStyleName("min-cov-but");
		addFDs.addClickHandler(this);
		Common.setCursorPointer(addFDs);
		hp.add(addFDs);
		hp.add(new InfoButton("givenfds-ca"));
		givenFDs.add(hp);
		
		/* additional controls */
		Image visual = new Image(Common.getResourceUrl("img/eye.png"));
		visual.setTitle("FD Visualization");
		Common.setCursorPointer(visual);
		visual.addClickHandler(event -> {
			VisualizationWindow vw = VisualizationWindow.get();
			vw.setData(egas.getDomain().createAttributeSet(),
					givenFDs.getFDs());
			vw.center();
		});
		
		Widget[] superAdditionalControl = givenFDs.getAdditionalControlls();
		Widget[] additionalContorll = 
			new Widget[superAdditionalControl.length+1];
		additionalContorll[0] = visual;
		for (int i = 0; i < superAdditionalControl.length; i++) {
			Widget widget = superAdditionalControl[i];
			additionalContorll[i+1] = widget;
		}
		/* END additional controls */
		dwGivenFDs = new DisclosureWidget(I18N.constants().givenFDs(), givenFDs, additionalContorll);
		mainPanel.add(dwGivenFDs);
		
		initWidget(mainPanel);
		Login.get().addListener(this);
		if(UserData.get().isLoggedIn()) {
			onLoginSuccess();
		} else {
			onSessionKilled();
		}
	}
	
	private void setDownloadForm() {
		//upload
		downloadForm = new FormPanel();
		hidenXML = new Hidden();
		hidenXML.setName("xml");
		hidenXML.setValue("");
		hidenXMLFilename = new Hidden();
		hidenXMLFilename.setName("filename");
		hidenXMLFilename.setValue("");
		downloadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		downloadForm.setMethod(FormPanel.METHOD_POST);
		downloadForm.setAction(Config.get().getDownloadScriptURL());
	    // Create a panel to hold all of the form widgets.
		downloadForm.add(hidenXML);
		downloadForm.add(hidenXMLFilename);
		mainPanel.add(downloadForm);
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
	
	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if(sender == addAtts) {
			DomainTableEditorDialog atd = DomainTableEditorDialog.get();
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
			Assignment tmp = getAssignemntFromUserInput();
			if (tmp == null) return;
			currentAssignment = tmp;
			RenameDialog.get().rename(this);
		} else if (sender == editButton) {
			LoadAssignmentDialog.get().load(this);
		} else if (sender == exportButton) {
			Assignment tmp = getAssignemntFromUserInput();
			if (tmp == null) return;
			if (downloadForm == null) setDownloadForm();
			currentAssignment = tmp;
			String data = AssignmentSaver.buildXML(currentAssignment);
			hidenXML.setValue(data);
			hidenXMLFilename.setValue("");
			if (currentAssignment.getName() != null && !currentAssignment.getName().trim().isEmpty()) {
				hidenXMLFilename.setValue(currentAssignment.getName().trim() + ".xml");
			}
			downloadForm.submit();
		} else if (sender == importButton) {
			UploadDialog.get().center();
		} else if (sender == loadInSATabButton) {
			Assignment tmp = getAssignemntFromUserInput();
			if (tmp == null) return;
			currentAssignment = tmp;
			Main.get().loadSATab();
			SolveAssignmentWidget.get().loadAssignment(tmp);
		}
		
	}
	
	private Assignment getAssignemntFromUserInput() {
		DomainTable domain = egas.getDomain();
		if(domain.size() < 1) {
			Window.alert(I18N.constants().noAttWarn());
			return null;
		}
		List<FD> fds = givenFDs.getFDs();
		if(fds.size() < 1) {
			Window.alert(I18N.constants().noFDsWarn());
			return null;
			
		}
		return new Assignment(domain, fds);
	}

	public String getOldName() {
		if(loadedName != null) {
			return loadedName;
		}
		return "";
	}
	
	
	@SuppressWarnings("rawtypes")
	public Collection getTakenNames() {
		//TODO
		return null;
	}
	
	public void setNewName(String s) {
		saveAssignemnt(s);
		
	}

	public void onRenameCanceled() {
		currentAssignment = null;
	}
	
	public void onLoaded(AssignmentDto entry) {
		loadedId = entry.getId();
		loadedName = entry.getName();
		AssignmentsRestClient.INSTANCE.getAssignment(entry.getId(), this);
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
			AssignmentSaver.get().sendToSaveScript(xml, name, 
					loadedId == null ? 0 : loadedId);
			currentAssignment  = null;
			editMode.setVisible(false);
			loadedId = null;
			loadedName = null;
			//TODO Use callbacks and on success set values to null 
		} else {
			Log.warn("CurrentAsigment = null. Could not save assignment.");
		}
	}
	
	private void restoreDefaultSize() {
		dwGivenAttributes.resetHeightToDefault();
		dwGivenFDs.resetHeightToDefault();
	}

	public void onSessionKilled() {
		loginFirst.setVisible(true);
		newButton.setEnabled(false);
		saveButton.setEnabled(false);
		editButton.setEnabled(false);
		exportButton.setEnabled(false);
		importButton.setEnabled(false);
		editMode.setVisible(false);
		
		loadedId = null;
		loadedName = null;
	}

	public void onLoginSuccess() {
		loginFirst.setVisible(false);
		newButton.setEnabled(true);
		saveButton.setEnabled(true);
		editButton.setEnabled(true);
		importButton.setEnabled(true);
		exportButton.setEnabled(true);
	}

	public boolean filter(AssignmentDto dto) {
		//Administrators can edit all assignments
		boolean isAdmin = UserData.get().isAdmin();
		if (isAdmin) {
			return true;
		}

		Integer userId = UserData.get().getId();
		if (userId == null) {
			return false;
		}

		if (dto.getAuthor() == null) {
			return false;
		}

		return userId.equals(dto.getAuthor().getId());
	}

	public boolean checkUserRights() {
		return true;
	}

	@Override
	public void onFailure(Method method, Throwable throwable) {
		// TODO
	}

	@Override
	public void onSuccess(Method method, AssignmentDto assignmentDto) {
		AssignmentXmlParser p = AssignmentXmlParser.get();
		Assignment a = p.parse(assignmentDto);
		// TODO if a == null, do what?
		onAssignmentLoaded(a);
	}
}
