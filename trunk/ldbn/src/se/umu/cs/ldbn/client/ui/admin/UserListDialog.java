package se.umu.cs.ldbn.client.ui.admin;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.io.UserAdminList;
import se.umu.cs.ldbn.client.io.UserListEntry;
import se.umu.cs.ldbn.client.ui.dialog.OkCancelDialog;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class UserListDialog extends OkCancelDialog implements ClickHandler {
	
	private static UserListDialog inst;
	
	public static UserListDialog get() {
		if (inst == null) {
			inst = new UserListDialog();
		}
		return inst;
	}
	
	private class ColumnHeader extends Composite {
		
		private HorizontalPanel hp;
		private Label name;
		
		public ColumnHeader(int type) {
			super();
			hp = new HorizontalPanel();
			initWidget(hp);
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setSpacing(4);
			switch (type) {
			case 1:
				name = new HTML("<B>Name</B>");
				break;
			case 2:
				name = new HTML("<B>Administrator</B>");
				break;
			case 3:
				name = new HTML("<B><NOBR>Super User</NOBR></B>");
				break;
			default: //should not be used
				name = new HTML("");
				break;
			}
			
			hp.add(name);
			//Common.setCursorPointer(name);
			addStyleName("nad-column-header");
		}
	}
	
	private FlexTable table;
	private boolean toRemove;
	private List<UserListEntry> data;
	private ArrayList<ColumnHeader> colHeaders;
	private int lastSelectedRow;
	
	private UserListDialog() {
		super("Select a User", true);
		lastSelectedRow = 0;
		data = new ArrayList<UserListEntry>();
	}
	
	public void loadUserList(List<UserListEntry> data, boolean toRemove) {
		this.data = data;
		table.clear();
		this.toRemove = toRemove;
		for (int i = 0; i < colHeaders.size(); i++) {
			table.setWidget(0, i, colHeaders.get(i));
		}
		
		int row = 1;
		for (UserListEntry entry : data) {
			table.setWidget(row, 0, Common.createCursorLabel(entry.getName()));
			if (entry.isAdmin()) {
				table.setWidget(row, 1, Common.createCursorHTML("<center><font color=\"green\">YES</font></center>"));
			} else {
				table.setWidget(row, 1, Common.createCursorHTML("<center><font color=\"red\">NO</font></center>"));
			}
			
			if (entry.isSuperUser()) {
				table.setWidget(row, 2, Common.createCursorHTML("<center><font color=\"green\">YES</font></center>"));
			} else {
				table.setWidget(row, 2, Common.createCursorHTML("<center><font color=\"red\">NO</font></center>"));
			}
			
			row++;
		}
		center();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Cell cell = table.getCellForEvent(event);
		if (cell != null) {
			int row = cell.getRowIndex();
			if (row > 0) {
				RowFormatter rf = table.getRowFormatter();
				if(lastSelectedRow > 0) {
					rf.removeStyleName(lastSelectedRow, "nad-selected");
				}
				lastSelectedRow = row;
				rf.addStyleName(lastSelectedRow, "nad-selected");
			}
		}
	
	}
	
	@Override
	public void center() {
		super.center();
		table.getRowFormatter().removeStyleName(lastSelectedRow, "nad-selected");
		lastSelectedRow = 0;
	}
	
	protected Widget getDialogContentWidget() {
		
		HorizontalPanel hp = new HorizontalPanel();
		colHeaders = new ArrayList<ColumnHeader>();
		colHeaders.add(new ColumnHeader(1));
		colHeaders.add(new ColumnHeader(2));
		colHeaders.add(new ColumnHeader(3));
		
		//START INIT TABLE
		ScrollPanel sp = new ScrollPanel();
		sp.setSize("100%", "200px");
		sp.setStyleName("nad-innerPanel");
		DOM.setStyleAttribute(sp.getElement(), "overflowY", "scroll");
		DOM.setStyleAttribute(sp.getElement(), "overflowX", "hidden");
		table = new FlexTable();
		table.setWidth("100%");
		table.addClickHandler(this);
		sp.add(table);
		DOM.setStyleAttribute(table.getElement(), "padding-right", "20px");
		//END INIT TABLE
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(sp);
		vp.add(hp);
		return vp;
	}
	
	protected void onOkClick() {
		if(lastSelectedRow < 1) {
			setErrorMsg("Invalid selection.");
			return;
		}
		setErrorMsg("");
		UserListEntry selected = data.get(lastSelectedRow - 1);
		if (toRemove && Window.confirm(
				"Are you sure you want to remove the user \"" + selected.getName()
				+ "\" from the administrator list?")) {
			hide();
			UserAdminList.get().send(selected.getId(), toRemove);
			return;
		} 
		
		if (!toRemove && Window.confirm(
				"Are you sure you want to add the user \"" + selected.getName()
				+ "\" to the administrator list?")) {
			hide();
			UserAdminList.get().send(selected.getId(), toRemove);
			return;	
		}
	}
}
