package se.umu.cs.ldbn.client.ui.dialog;

import java.util.Map;
import java.util.Set;

import se.umu.cs.ldbn.client.CommonFunctions;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class NewAssignmentDialog extends OkCancelDialog {

	private Panel mainPanel;
	private MyLabel lastSelected;
	
	private class MyLabel extends Label implements ClickListener {
		private String id;
		public MyLabel(String name, String id) {
			super(name);
			this.id = id;
			addClickListener(this);
			CommonFunctions.setCursorPointer(this);
			addStyleName("nad-myLabel");
		}
		
		public String getId() {
			return id;
		}
		
		public void onClick(Widget sender) {
			if (lastSelected != null) {
				lastSelected.removeStyleName("nad-myLabel-selected");
			}
			lastSelected = (MyLabel) sender;
			addStyleName("nad-myLabel-selected");
		}
	}
	
	private static NewAssignmentDialog inst;
	
	public static NewAssignmentDialog get() {
		if (inst == null) {
			inst = new NewAssignmentDialog();
		}
		return inst;
	}
	
	private NewAssignmentDialog() {
		super("New assigment", "Choose an assigment from the list bellow", true);
		setText("New assigment.");
	}
	
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
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("nad-innerPanel");
		return mainPanel;
	}
	
	protected void onOkClick() {
		// TODO Auto-generated method stub
		
	}
}
