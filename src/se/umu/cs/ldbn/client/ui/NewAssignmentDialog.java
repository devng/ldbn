package se.umu.cs.ldbn.client.ui;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import se.umu.cs.ldbn.client.CommonFunctions;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewAssignmentDialog extends DialogBox implements ClickListener {

	private Panel mainPanel;
	private Button okButton;
	private Button cancelButton;
	private MyLabel lastSelected = null;
	
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
		super(false, true);
		setText("New assigment.");
		mainPanel = new VerticalPanel();
		
		mainPanel.setStyleName("nad-innerPanel");
		okButton = new Button("OK", this);
		cancelButton = new Button("Cancel", this);
		okButton.addStyleName("nad-okBut");
		CommonFunctions.setCursorPointer(okButton);
		CommonFunctions.setCursorPointer(cancelButton);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		hp.setWidth("100%");
		HorizontalPanel hp2 = new HorizontalPanel();
		
		hp2.add(okButton);
		hp2.add(cancelButton);
		hp.add(hp2);
		HTML msg = new HTML(
				"<center>Choose an assigment from the list bellow.</center>", true);
		
		DockPanel dock = new DockPanel();
		dock.setSpacing(4);
		
		dock.add(hp, DockPanel.SOUTH);
		dock.add(msg, DockPanel.NORTH);
		dock.add(mainPanel, DockPanel.CENTER);
		setWidget(dock);
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
	
	
	public void onClick(Widget sender) {
		if (sender == cancelButton) {
			hide();
		} else if (sender == okButton ) {
			
		}
		
	}

}
