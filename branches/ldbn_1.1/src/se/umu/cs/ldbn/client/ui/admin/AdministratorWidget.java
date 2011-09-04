package se.umu.cs.ldbn.client.ui.admin;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;
import se.umu.cs.ldbn.client.io.DeleteAssignment;
import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.LoginListener;
import se.umu.cs.ldbn.client.io.UserList;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialogCallback;
import se.umu.cs.ldbn.client.ui.user.UserData;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public final class AdministratorWidget extends AbsolutePanel 
	implements ClickHandler, LoginListener, LoadAssignmentDialogCallback {

	private final static int VIEWER_PX_HEIGHT = 750;
	
	private static AdministratorWidget inst;
	
	public static AdministratorWidget get() {
		if (inst == null) {
			inst = new AdministratorWidget();
		}
		return inst;
	}
	
	private Button addToAdmin;
	private Button removeFromAdmin;
	private Button deleteAssignment;
	private Label admin;
	private Label su;
	
	private AdministratorWidget() {
		HeaderWidget hw = new HeaderWidget();
		addToAdmin = new Button("Add Instructional Users");
		addToAdmin.setStyleName("att-but");
		addToAdmin.addClickHandler(this);
		hw.add(addToAdmin);
		
		removeFromAdmin = new Button("Remove Instructional Users");
		removeFromAdmin.addClickHandler(this);
		removeFromAdmin.setStyleName("att-but");
		removeFromAdmin.setEnabled(false);
		Login.get().addListener(this);
		hw.add(removeFromAdmin);
		
		deleteAssignment = new Button("Delete Assignments");
		deleteAssignment.setStyleName("att-but");
		deleteAssignment.addClickHandler(this);
		hw.add(deleteAssignment);
		
		InfoButton info = new InfoButton("admin-tab");
		info.setStyleName("att-img");
		hw.add(info);
		
		Image trenner = new Image("img/trenner.jpg");
		trenner.setStyleName("att-but");
		hw.add(trenner);
			
		admin = new Label("You have administrator rights");
		admin.setVisible(false);
		admin.setStyleName("att-but");
		su    = new Label("You have super user rights");
		su.setVisible(false);
		su.setStyleName("att-but");
		hw.add(admin);
		hw.add(su);
		
		this.add(hw);
		
		Frame viewer = new Frame("info/user_rights.html"); 
		viewer.setWidth("100%");
		viewer.setHeight(VIEWER_PX_HEIGHT+"px");
		//DOM.setStyleAttribute(viewer.getElement(), "overflow", "hidden");
		DisclosureWidget dw = new DisclosureWidget("User Rights", viewer);
		this.add(dw);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender == addToAdmin) {
			UserList.get().send(false);
		} else if (sender == removeFromAdmin) {
			UserList.get().send(true);
		} else if (sender == deleteAssignment) {
			LoadAssignmentDialog.get().load(this);
		}
	}
	
	public void onLoginSuccess() {
		if (UserData.get().isSuperUser()) {
			removeFromAdmin.setEnabled(true);
			admin.setVisible(false);
			su.setVisible(true);
		} else {
			admin.setVisible(true);
			su.setVisible(false);
		}
	}
	
	public void onSessionKilled() {
		removeFromAdmin.setEnabled(false);
	}
	
	public boolean checkUserRights() {
		return true;
	}

	public void onLoadCanceled() {
		//DO Nothing
	}

	
	public void onLoaded(AssignmentListEntry entry) {
		String name = entry.getName();
		String autor = entry.getAuthor();
		if (Window.confirm("Are you sure you want to delete the assignment \"" + 
				name + "\" by \"" + autor + "\"")) {
			DeleteAssignment.get().send(entry.getId());
		}
		
	}
}
