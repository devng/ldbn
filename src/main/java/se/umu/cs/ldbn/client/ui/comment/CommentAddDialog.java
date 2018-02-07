package se.umu.cs.ldbn.client.ui.comment;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import se.umu.cs.ldbn.client.io.Comment;
import se.umu.cs.ldbn.client.rest.CommentsRestClient;
import se.umu.cs.ldbn.client.ui.dialog.OkCancelDialog;

import javax.inject.Inject;

public class CommentAddDialog extends OkCancelDialog {

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

		// TODO send a comment to the backend via REST

		hide();
	}

	public String getComment() {

		return textBox.getText();
	}
}
