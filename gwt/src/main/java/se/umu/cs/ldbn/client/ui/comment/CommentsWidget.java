package se.umu.cs.ldbn.client.ui.comment;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.events.CommentsReceivedEventHandler;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.model.UserModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import se.umu.cs.ldbn.shared.dto.CommentDto;

public final class CommentsWidget extends Composite implements ClickHandler, CommentsReceivedEventHandler {

	private static CommentsWidget inst;

	public static CommentsWidget get() {
		if (inst == null) {
			inst = new CommentsWidget();
		}
		return inst;
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

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender == addCommentBut) {
			if (!UserModel.get().isLoggedIn()) {
				Window.alert("You have to login first.");
				return;
			}
			CommentAddDialog.get().center();
		}
	}

	@Override
	public void onCommentsReceived(List<CommentDto> comments) {
		for (CommentDto dto : comments) {
			CommentEntry c =  new CommentEntry(dto);
			mainPanel.add(c);
			entries.add(c);
		}
	}
}
