package se.umu.cs.ldbn.client.ui.dialog;

import java.util.List;

import se.umu.cs.ldbn.client.io.Comment;
import se.umu.cs.ldbn.client.io.CommentCallback;
import se.umu.cs.ldbn.client.io.CommentListEntry;
import se.umu.cs.ldbn.client.ui.sa.CommentsWidget;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class CommentAddDialog extends OkCancelDialog implements CommentCallback {

	private static CommentAddDialog inst;

	public static CommentAddDialog get() {
		if(inst == null) {
			inst =  new CommentAddDialog();
		}
		return inst;
	}

	private TextArea textBox;


	private CommentAddDialog() {
		super("Leave a comment", true);
	}

	protected Widget getDialogContentWidget() {
		textBox = new TextArea();
		textBox.setSize("260", "120");
		return textBox;
	}

	@Override
	public void center() {
		super.center();
		textBox.setText("");
	}

	protected void onOkClick() {
		String s = textBox.getText();
		if(s.length()<1) {
			setErrorMsg("Comment is empty.");
			return;
		}
		Comment.get().send(this);
		hide();
	}

	public String getAssignmentID() {
		return SolveAssignmentWidget.get().getCurrentAssignmentId();
	}

	public String getComment() {

		return textBox.getText();
	}

	public void onCommentsReceived(List<CommentListEntry> comments,
			String assignmentID) {
		CommentsWidget.get().addComments(comments);
	}
}
