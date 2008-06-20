package se.umu.cs.ldbn.client.ui.dialog;

import java.util.Map;
import java.util.Set;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.io.AssignmentLoader;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class LoadAssignmentDialog extends OkCancelDialog {

	private Panel mainPanel;
	private MyLabel lastSelected;
	private LoadAssignmentDialogCallback caller;
	
	private class MyLabel extends Label implements ClickListener {
		String id;
		String name;
		public MyLabel(String name, String id) {
			super(name);
			if(name == null || id == null) {
				throw new IllegalArgumentException("Some arguments are null");
			}
			this.id = id;
			this.name = name;
			addClickListener(this);
			CommonFunctions.setCursorPointer(this);
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
		AssignmentLoader.loadAssignmentList();
	}
	
	/**
	 * Used by the AssignmentLoader.
	 * @param list
	 */
	public void loadAssigmentList(Map<String, String> list) {
		mainPanel.clear();
		Set<String> keys = list.keySet();
		for (String k : keys) {
			MyLabel l = new MyLabel(list.get(k), k);
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
		caller.loadedIdAndName(lastSelected.id, lastSelected.name);
		hide();
	}
	
	protected void onCancelClick()  {
		super.onCancelClick();
		lastSelected = null;
	}
} 
