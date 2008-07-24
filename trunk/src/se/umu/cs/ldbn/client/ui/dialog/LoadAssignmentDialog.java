package se.umu.cs.ldbn.client.ui.dialog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.io.AssignmentListEntry;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class LoadAssignmentDialog extends OkCancelDialog {

	private class MyLabel extends Label implements ClickListener {
		private AssignmentListEntry entry;
		public MyLabel(AssignmentListEntry entry) {
			super(entry.getName());
			if(entry == null) {
				throw new IllegalArgumentException("Some arguments are null");
			}
			this.entry = entry;
			addClickListener(this);
			Common.setCursorPointer(this);
			addStyleName("nad-myLabel");
		}
		
		public void onClick(Widget sender) {
			if (lastSelected != null) {
				lastSelected.removeStyleName("nad-myLabel-selected");
			}
			lastSelected = (MyLabel) sender;
			addStyleName("nad-myLabel-selected");
		}
	}
	private static LoadAssignmentDialog inst;
	public static LoadAssignmentDialog get() {
		if (inst == null) {
			inst = new LoadAssignmentDialog();
		}
		return inst;
	}
	
	private Panel mainPanel;
	
	private MyLabel lastSelected;
	
	private LoadAssignmentDialogCallback caller;
	
	private LoadAssignmentDialog() {
		super("Load assigment", "Choose an assigment from the list bellow", true);
		//setText("New assigment."); //What is this???
	}
	
	/**
	 * Use this method to load assignments.
	 * 
	 * @param caller
	 */
	public void load(LoadAssignmentDialogCallback caller) {
		this.caller = caller;
		AssignmentLoader.get().loadAssignmentList();
	}
	
	/**
	 * Used by the AssignmentLoader.
	 * @param list
	 */
	public void loadAssigmentList(List<AssignmentListEntry> list) {
		mainPanel.clear();
		for (AssignmentListEntry entry : list) {
			MyLabel l = new MyLabel(entry);
			mainPanel.add(l);
		}
		center();
	}
	
	protected Widget getContentWidget() {
		ScrollPanel sp = new ScrollPanel();
		sp.setSize("100%", "120");
		sp.setStyleName("nad-innerPanel");
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		sp.add(mainPanel);
		return sp;
	}
	
	protected void onCancelClick()  {
		super.onCancelClick();
		lastSelected = null;
	}
	
	protected void onOkClick() {
		if(lastSelected == null) {
			setErrorMsg("Invalid selection.");
			return;
		}
		setErrorMsg("");
		if(caller == null) {
			Log.warn("LoadAssignmentDialog.caller == null. Cannot load assignment");
			return;
		}
		caller.onLoaded(lastSelected.entry);
		hide();
	}
} 
