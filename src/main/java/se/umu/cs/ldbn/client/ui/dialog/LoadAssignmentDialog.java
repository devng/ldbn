package se.umu.cs.ldbn.client.ui.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;
import se.umu.cs.ldbn.client.io.AssignmentLoader;
import se.umu.cs.ldbn.client.ui.sa.AssignmentFilter;
import se.umu.cs.ldbn.client.ui.sa.AssignmentFilterAdmin;
import se.umu.cs.ldbn.client.ui.sa.AssignmentFilterAll;
import se.umu.cs.ldbn.client.ui.sa.AssignmentFilterYou;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class LoadAssignmentDialog extends OkCancelDialog implements 
	ClickHandler, ChangeHandler {

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
	
	private class ColumnHeader extends Composite implements ClickHandler {
		
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
			hp = new HorizontalPanel();
			initWidget(hp);
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setSpacing(4);
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
			
			hp.add(name);
			hp.add(sortingImg);
			Common.setCursorPointer(name);
			name.addClickHandler(this);
			addStyleName("nad-column-header");
		}

		@Override
		public void onClick(ClickEvent event) {
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
	
	private ListBox filterBox;
	private AssignmentFilter[] filters;
	private int currentFilterIndex;
	
	private LoadAssignmentDialog() {
		super("Select an Assignment", true);
		lastSelectedRow = 0;
		colHeaders = new ArrayList<ColumnHeader>();
		colHeaders.add(new ColumnHeader(1));
		colHeaders.add(new ColumnHeader(2));
		colHeaders.add(new ColumnHeader(3));
		data = new ArrayList<AssignmentListEntry>(); //
		table.addClickHandler(this);
	}
	
	/**
	 * Use this method to load assignments.
	 * 
	 * @param caller
	 */
	public void load(LoadAssignmentDialogCallback caller) {
		this.caller = caller;
		filterBox.clear();
		filterBox.addItem(filters[0].getName());
		filterBox.addItem(filters[1].getName());
		filterBox.addItem(filters[2].getName());
		currentFilterIndex = 2;
		filterBox.setSelectedIndex(2);
		if (caller.checkUserRights()) {
			if(!UserData.get().isAdmin()) {
				filterBox.clear();
				filterBox.addItem(filters[0].getName());
			} 
			currentFilterIndex = 0;
			filterBox.setSelectedIndex(0);
		}
		AssignmentLoader.get().loadAssignmentList();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		super.onClick(event);
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
	
	/**
	 * Used by the AssignmentLoader.
	 * @param list
	 */
	public void loadAssigmentList(List<AssignmentListEntry> list) {
		data = list;
		loadAssinmentListWithoutReCenter(data);
		center();
	}
	
	@Override
	public void center() {
		super.center();
		try {
			AssignmentFilter filter = filters[currentFilterIndex];
			List<AssignmentListEntry> filteredData = filter.apply(data);
			loadAssinmentListWithoutReCenter(filteredData);
		} catch (IllegalStateException e) {
			this.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
		table.getRowFormatter().removeStyleName(lastSelectedRow, "nad-selected");
		if(colHeaders != null) {
			for (ColumnHeader ch : colHeaders) {
				ch.setDefaults();
			}
		}
		lastSelectedRow = 0;
	}
	
	@Override
	public void onChange(ChangeEvent event) {
		Object sender = event.getSource();
		this.setErrorMsg("");
		try {
			if (sender == filterBox) {
				currentFilterIndex = filterBox.getSelectedIndex();
				AssignmentFilter filer = filters[currentFilterIndex];
				List<AssignmentListEntry> filteredData = filer.apply(data);
				loadAssinmentListWithoutReCenter(filteredData);
			}
		} catch (IllegalStateException e) {
			this.setErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected Widget getDialogContentWidget() {
		//START INIT FILTERS
		filterBox = new ListBox(false);
		filters = new AssignmentFilter[3];
		filters[0] = new AssignmentFilterYou();
		filters[1] = new AssignmentFilterAll();
		filters[2] = new AssignmentFilterAdmin();
		
		filterBox.addItem(filters[0].getName());
		filterBox.addItem(filters[1].getName());
		filterBox.addItem(filters[2].getName());
		filterBox.addChangeHandler(this);
		currentFilterIndex = 2;
		filterBox.setSelectedIndex(currentFilterIndex);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(10);
		hp.add(new Label ("Show : "));
		hp.add(filterBox);
		//END INIT FILTERS
		
		//START INIT TABLE
		ScrollPanel sp = new ScrollPanel();
		sp.setSize("100%", "200px");
		sp.setStyleName("nad-innerPanel");
		DOM.setStyleAttribute(sp.getElement(), "overflowY", "scroll");
		DOM.setStyleAttribute(sp.getElement(), "overflowX", "hidden");
		table = new FlexTable();
		table.setWidth("100%");
		sp.add(table);
		DOM.setStyleAttribute(table.getElement(), "PaddingRight", "20px");
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
		if(caller == null) {
			Log.warn("LoadAssignmentDialog.caller == null. Cannot load assignment");
			return;
		}
		caller.onLoaded(((MyLabel) (table.getWidget(lastSelectedRow, 0))).getEntry());
		hide();
	}
	
	@Override
	protected String getHelpMessage() {
		return "Choose an assignment from the list bellow";
	}

	
	private void sort(AssignmentListEntry.compareAttribute cAtt, boolean isDec) {
		AssignmentListEntry.setCompareAttribute(cAtt);
		AssignmentListEntry.setDecreasing(isDec);
		table.getRowFormatter().removeStyleName(lastSelectedRow, "nad-selected");
		lastSelectedRow = 0;
		Collections.sort(data);
		
		currentFilterIndex = filterBox.getSelectedIndex();
		AssignmentFilter filer = filters[currentFilterIndex];
		List<AssignmentListEntry> filteredData = filer.apply(data);
		loadAssinmentListWithoutReCenter(filteredData);
	}
	
	private void loadAssinmentListWithoutReCenter(List<AssignmentListEntry> data) {
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
			table.setWidget(row, 2, Common.createCursorHTML("<nobr>" + 
					entry.getModifiedOn() + "</nobr>"));
			row++;
		}
	}
} 
