package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.io.CommentEdit;
import se.umu.cs.ldbn.client.io.CommentEditCallback;
import se.umu.cs.ldbn.client.io.CommentListEntry;
import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.LoginListener;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.CommentAddDialog;
import se.umu.cs.ldbn.client.ui.dialog.CommentEditDialog;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class CommentsWidget extends Composite implements ClickHandler {

	private static CommentsWidget inst;

	public static CommentsWidget get() {
		if (inst == null) {
			inst = new CommentsWidget();
		}
		return inst;
	}

	private final class CommentEntry extends Composite implements MouseOutHandler, MouseOverHandler,
	    ClickHandler, LoginListener, CommentEditCallback {

		private VerticalPanel mainPanel;
		private Grid header;
		private HTML content;
		private HTML userName;
		private Label lastModified;
		private Image editButton;
		private Image deleteButton;
		private CommentListEntry cle;
		private boolean isToDelete;
		private String possibleText;

		public CommentEntry(CommentListEntry cle) {
			this.cle = cle;
			isToDelete = false;
			mainPanel = new VerticalPanel();
			mainPanel.setStyleName("relW");
			mainPanel.setWidth("100%");

			header = new Grid(1, 5);
			CellFormatter cf = header.getCellFormatter();
			header.setStyleName("relW-header");


			userName = new HTML("<B>"+cle.getAuthor()+"</B>");
			header.setWidget(0, 0, userName);
			cf.setWidth(0, 0, "20");
			cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);

			editButton = new Image("img/edit-name.png", 0, 0, 15, 15);
			editButton.addClickHandler(this);
			editButton.addMouseOutHandler(this);
			editButton.addMouseOverHandler(this);
			editButton.setTitle("Edit the comment.");
			Common.setCursorPointer(editButton);
			header.setWidget(0, 1, editButton);
			cf.setWidth(0, 1, "20px");
			cf.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);

			deleteButton = new Image("img/bin.png", 0, 0, 15, 15);
			deleteButton.addClickHandler(this);
			deleteButton.addMouseOutHandler(this);
			deleteButton.setTitle("Delete the comment.");
			Common.setCursorPointer(deleteButton);
			header.setWidget(0, 2, deleteButton);
			cf.setWidth(0, 2, "20px");
			cf.setAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);

			lastModified = new Label(cle.getLastModified());
			header.setWidget(0, 4, lastModified);
			cf.setAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_MIDDLE);

			mainPanel.add(header);

			content = new HTML(cle.getCommentString());
			mainPanel.add(content);

			this.initWidget(mainPanel);
			setWidth("100%");
			Login.get().addListener(this);

			editButton.setVisible(false);
			deleteButton.setVisible(false);

			//display edit and delete buttons only if the user is logged in
			if (UserData.get().isLoggedIn()) {
				String id = UserData.get().getId();
				boolean isAdmin = UserData.get().isAdmin();
				if (isAdmin || id.equals(cle.getAuthorID())) {
					editButton.setVisible(true);
					deleteButton.setVisible(true);
				}
			}

		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			Object sender = event.getSource();
			if (sender == deleteButton) {
				deleteButton.setVisibleRect(0, 0, 15, 15);
			} else if (sender == editButton) {
				editButton.setVisibleRect(0, 0, 15, 15);
			}

		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			Object sender = event.getSource();
			if (sender == deleteButton) {
				deleteButton.setVisibleRect(15, 0, 15, 15);
			} else if (sender == editButton) {
				editButton.setVisibleRect(15, 0, 15, 15);
			}

		}

		@Override
		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
			if (sender == deleteButton) {
				if(Window.confirm("Are you sure you wan to delete the comment?")) {
					CommentEdit ce = CommentEdit.get();
					isToDelete = true;
					ce.send(this, cle.getId(), "", true);
				}

			} else if (sender == editButton) {
				CommentEditDialog ced = CommentEditDialog.get(cle, this);
				ced.center();
			}
		}

		public void onLoginSuccess() {
			String id = UserData.get().getId();
			boolean isAdmin = UserData.get().isAdmin();
			if (isAdmin || id.equals(cle.getAuthorID())) {
				editButton.setVisible(true);
				deleteButton.setVisible(true);
			}

		}

		public void onSessionKilled() {
			editButton.setVisible(false);
			deleteButton.setVisible(false);
		}


		public void onReseive(boolean isOK) {
			if(isOK) {
				if (isToDelete) {
					removeFromParent();
					//Login.get().removeListener(this);
				} else if (possibleText != null) {
					content.setHTML(Common.escapeHTMLCharacters(possibleText));
				}
			} else {
				if (isToDelete) {
					Window.alert("The comment could not be deleted.");
				} else {
					Window.alert("The comment could not be edited.");
				}
			}
			possibleText = null;
			isToDelete = false;
		}

		public void setPossibleCommentText(String str) {
			this.possibleText = str;
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
		addCommentBut.addClickHandler(this);
		hp.add(addCommentBut);
		hp.add(new InfoButton("comment"));
		mainPanel.add(hp);
		entries = new ArrayList<>();
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

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender == addCommentBut) {
			if (!UserData.get().isLoggedIn()) {
				Window.alert("You have to login first.");
				return;
			}
//			if(!SolveAssignmentWidget.get().isAssignmentLoadedFromDB()) {
//				Window.alert("This is a static assignment,\n" +
//						"thus it is not loaded from the database\n" +
//						"and comments cannot be submited.");
//				return;
//			}
			CommentAddDialog.get().center();
		}
	}
}
