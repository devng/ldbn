package se.umu.cs.ldbn.client.ui.comment;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import se.umu.cs.ldbn.client.io.CommentEdit;
import se.umu.cs.ldbn.client.io.CommentEditCallback;
import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.LoginListener;
import se.umu.cs.ldbn.client.model.UserModel;
import se.umu.cs.ldbn.client.utils.Common;
import se.umu.cs.ldbn.shared.dto.CommentDto;

final class CommentEntry extends Composite implements MouseOutHandler, MouseOverHandler,
		ClickHandler, LoginListener, CommentEditCallback {

	private VerticalPanel mainPanel;
	private Grid header;
	private HTML content;
	private HTML userName;
	private Label lastModified;
	private Image editButton;
	private Image deleteButton;
	private CommentDto dto;
	private boolean isToDelete;
	private String possibleText;

	public CommentEntry(CommentDto dto) {
		this.dto = dto;
		isToDelete = false;
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("relW");
		mainPanel.setWidth("100%");

		header = new Grid(1, 5);
		HTMLTable.CellFormatter cf = header.getCellFormatter();
		header.setStyleName("relW-header");


		userName = new HTML("<B>"+dto.getAuthor().getName()+"</B>");
		header.setWidget(0, 0, userName);
		cf.setWidth(0, 0, "20");
		cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		editButton = new Image(Common.getResourceUrl("img/edit-name.png"), 0, 0, 15, 15);
		editButton.addClickHandler(this);
		editButton.addMouseOutHandler(this);
		editButton.addMouseOverHandler(this);
		editButton.setTitle("Edit the comment.");
		Common.setCursorPointer(editButton);
		header.setWidget(0, 1, editButton);
		cf.setWidth(0, 1, "20px");
		cf.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		deleteButton = new Image(Common.getResourceUrl("img/bin.png"), 0, 0, 15, 15);
		deleteButton.addClickHandler(this);
		deleteButton.addMouseOutHandler(this);
		deleteButton.setTitle("Delete the comment.");
		Common.setCursorPointer(deleteButton);
		header.setWidget(0, 2, deleteButton);
		cf.setWidth(0, 2, "20px");
		cf.setAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		lastModified = new Label(dto.getModifiedOn().toString());
		header.setWidget(0, 4, lastModified);
		cf.setAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		mainPanel.add(header);

		content = new HTML(dto.getCommentVal());
		mainPanel.add(content);

		this.initWidget(mainPanel);
		setWidth("100%");
		Login.get().addListener(this);

		editButton.setVisible(false);
		deleteButton.setVisible(false);

		//display edit and delete buttons only if the model is logged in
		if (UserModel.get().isLoggedIn()) {
			Integer id = UserModel.get().getId();
			boolean isAdmin = UserModel.get().isAdmin();
			if (isAdmin || id.equals(dto.getAuthor().getId())) {
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
				ce.send(this, dto.getId(), "", true);
			}

		} else if (sender == editButton) {
			CommentEditDialog ced = CommentEditDialog.get(cle, this);
			ced.center();
		}
	}

	public void onLoginSuccess() {
		Integer id = UserModel.get().getId();
		boolean isAdmin = UserModel.get().isAdmin();
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