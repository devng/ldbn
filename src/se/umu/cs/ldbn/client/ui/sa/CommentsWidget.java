package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.io.CommentListEntry;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.CommentDialog;
import se.umu.cs.ldbn.client.ui.user.UserData;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public final class CommentsWidget extends Composite implements ClickListener {
	
	private static CommentsWidget inst;
	
	public static CommentsWidget get() {
		if (inst == null) {
			inst = new CommentsWidget();
		}
		return inst;
	}

	private final class CommentEntry extends Composite {
		
		private VerticalPanel mainPanel;
		private Grid header;
		private HTML content;
		private HTML userName;
		private Label lastModified;
		
		public CommentEntry(CommentListEntry cle) {
			mainPanel = new VerticalPanel();
			mainPanel.setStyleName("relW");
			mainPanel.setWidth("100%");

			
			userName = new HTML("<B>"+cle.getAuthor()+"</B>");
			lastModified = new Label(cle.getLastModified());
			content = new HTML(cle.getCommentString());
			
			header = new Grid(1, 3);
			header.setStyleName("relW-header");
			header.setWidget(0, 0, userName);
			header.setWidget(0, 2, lastModified);
			CellFormatter cf = header.getCellFormatter();
			cf.setWidth(0, 0, "20");
			cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			
			cf.setAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			
			mainPanel.add(header);
			mainPanel.add(content);
			
			this.initWidget(mainPanel);
			setWidth("100%");
		}
	}
	
	private VerticalPanel mainPanel;
	private List<CommentEntry> entries;
	private Button addCommentBut;
	
	private CommentsWidget() {
		mainPanel = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		addCommentBut = new Button("Add new Comment");
		addCommentBut.setStyleName("min-cov-but");
		addCommentBut.addClickListener(this);
		hp.add(addCommentBut);
		hp.add(new InfoButton("comment"));
		mainPanel.add(hp);
		entries = new ArrayList<CommentEntry>();
		initWidget(mainPanel);
		mainPanel.setSpacing(6);
		setWidth("100%");
	}
	
	public void clearData() {
		for (CommentEntry cm : entries) {
			cm.removeFromParent();
		}
		entries.clear();
	}
	
	public void addComments(List<CommentListEntry> comments) {
		for (CommentListEntry cle : comments) {
			CommentEntry c =  new CommentEntry(cle);
			mainPanel.add(c);
			entries.add(c);
		}
	}
	
	public void onClick(Widget sender) {
		if(sender == addCommentBut) {
			if(!UserData.get().isLoggedIn()) {
				Window.alert("You have to login first.");
				return;
			}
//			if(!SolveAssignmentWidget.get().isAssignmentLoadedFromDB()) {
//				Window.alert("This is a static assignment,\n" +
//						"thus it is not loaded from the database\n" +
//						"and comments cannot be submited.");
//				return;
//			}
			CommentDialog.get().center();
		}
	}
}
