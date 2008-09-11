package se.umu.cs.ldbn.client.ui.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.utils.Common;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public final class LoadAssignmentDialog extends OkCancelDialog implements 
	TableListener {

	private class MyLabel extends Label {
		private AssignmentListEntry entry;
		public MyLabel(AssignmentListEntry entry) {
			super(entry.getName());
			this.entry = entry;
			Common.setCursorPointer(this);
			addStyleName("nad-myLabel");
		}
		
		public AssignmentListEntry getEntry() {
			return entry;
		}
	}
	
	private class ColumnHeader extends Composite implements ClickListener {
		
		private HorizontalPanel hp;
		private AssignmentListEntry.compareAttribute cAtt;
		private Label name;
		private boolean isSorting;
		private boolean isDec;
		private Image sortingImg;
		
		//type == 1 - assignment name
		//type == 2 - author name
		//type == 3 - date
		public ColumnHeader(int type) {
			super();
			isSorting = false;
			isDec = false;
			sortingImg = new Image("img/column-sorting.png", 0, 0, 9, 7);
			switch (type) {
			case 1:
				name = new HTML("<B>Name</B>");
				cAtt = AssignmentListEntry.compareAttribute.name;
				break;
			case 2:
				name = new HTML("<B>Author</B>");
				cAtt = AssignmentListEntry.compareAttribute.author;
				break;
			case 3:
				name = new HTML("<B>Last modified</B>");
				cAtt = AssignmentListEntry.compareAttribute.modified;
				break;
			default: //should not be used
				name = new HTML("");
				cAtt = AssignmentListEntry.compareAttribute.id;
				break;
			}
			hp = new HorizontalPanel();
			initWidget(hp);
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setSpacing(4);
			hp.add(name);
			hp.add(sortingImg);
			Common.setCursorPointer(name);
			name.addClickListener(this);
			addStyleName("nad-column-header");
		}

		public void onClick(Widget sender) {
			if(colHeaders != null) {
				for (ColumnHeader ch : colHeaders) {
					if(ch != this) {
						ch.setDefaults();
					}
				}
			}
			if(isSorting) {
				isDec = !isDec;
			}
			if(!isSorting) {
				isSorting = true;
			}
			sortingImg.setVisibleRect(isDec ? 9 : 18 , 0, 9, 7);
			sort(cAtt, isDec);
		}
		
		private void setDefaults() {
			isSorting = false;
			isDec = false;
			sortingImg.setVisibleRect(0, 0, 9, 7);
		}
		
	}
	
	private static LoadAssignmentDialog inst;
	public static LoadAssignmentDialog get() {
		if (inst == null) {
			inst = new LoadAssignmentDialog();
		}
		return inst;
	}
	
	private LoadAssignmentDialogCallback caller;
	private FlexTable table;
	private ArrayList<ColumnHeader> colHeaders;
	private List<AssignmentListEntry> data;
	private int lastSelectedRow;
	
	private LoadAssignmentDialog() {
		super("Load Assignment", "Choose an assignment from the list bellow", true);
		lastSelectedRow = 0;
		colHeaders = new ArrayList<ColumnHeader>();
		colHeaders.add(new ColumnHeader(1));
		colHeaders.add(new ColumnHeader(2));
		colHeaders.add(new ColumnHeader(3));
		data = new ArrayList<AssignmentListEntry>(); //
		table.addTableListener(this);
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
	

	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
		if(row > 0) {
			RowFormatter rf = table.getRowFormatter();
			if(lastSelectedRow > 0) {
				rf.removeStyleName(lastSelectedRow, "nad-selected");
			}
			lastSelectedRow = row;
			rf.addStyleName(lastSelectedRow, "nad-selected");
		}
		
	}
	
	/**
	 * Used by the AssignmentLoader.
	 * @param list
	 */
	public void loadAssigmentList(List<AssignmentListEntry> list) {
		data = list;
		loadAssinmentListWithoutReCenter();
		center();
	}
	
	public void center() {
		super.center();
		table.getRowFormatter().removeStyleName(lastSelectedRow, "nad-selected");
		if(colHeaders != null) {
			for (ColumnHeader ch : colHeaders) {
				ch.setDefaults();
			}
		}
		lastSelectedRow = 0;
	}
	
	protected Widget getContentWidget() {
		ScrollPanel sp = new ScrollPanel();
		sp.setSize("100%", "160");
		sp.setStyleName("nad-innerPanel");
		table = new FlexTable();
		table.setWidth("100%");
		sp.add(table);
		return sp;
	}
	
	protected void onOkClick() {
		if(lastSelectedRow < 1) {
			setErrorMsg("Invalid selection.");
			return;
		}
		setErrorMsg("");
		if(caller == null) {
			Log.warn("LoadAssignmentDialog.caller == null. Cannot load assignment");
			return;
		}
		caller.onLoaded(((MyLabel) (table.getWidget(lastSelectedRow, 0))).getEntry());
		hide();
	}

	
	private void sort(AssignmentListEntry.compareAttribute cAtt, boolean isDec) {
		AssignmentListEntry.setCompareAttribute(cAtt);
		AssignmentListEntry.setDecreasing(isDec);
		table.getRowFormatter().removeStyleName(lastSelectedRow, "nad-selected");
		lastSelectedRow = 0;
		
		Collections.sort(data);
		loadAssinmentListWithoutReCenter();
	}
	
	private void loadAssinmentListWithoutReCenter() {
		table.clear();
		
		
		for (int i = 0; i < colHeaders.size(); i++) {
			table.setWidget(0, i, colHeaders.get(i));
		}
		LoadAssignmentDialogFilter filter = null;
		if(caller instanceof LoadAssignmentDialogFilter) {
			filter = (LoadAssignmentDialogFilter) caller;
		}
		int row = 1;
		for (AssignmentListEntry entry : data) {
			if(filter != null && !filter.filter(entry)) {
				continue;
			}
			table.setWidget(row, 0, new MyLabel(entry));
			table.setWidget(row, 1, Common.createCursorLabel(entry.getAuthor()));
			table.setWidget(row, 2, Common.createCursorLabel(entry.getModifiedOn()));
			row++;
		}
	}
} 
