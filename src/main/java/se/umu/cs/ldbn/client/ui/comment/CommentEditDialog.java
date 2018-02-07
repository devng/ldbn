package se.umu.cs.ldbn.client.ui.comment;

import se.umu.cs.ldbn.client.io.CommentEdit;
import se.umu.cs.ldbn.client.io.CommentEditCallback;
import se.umu.cs.ldbn.client.io.CommentListEntry;
import se.umu.cs.ldbn.client.ui.dialog.OkCancelDialog;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class CommentEditDialog extends OkCancelDialog {

    private static CommentEditDialog inst;

	public static CommentEditDialog get(CommentListEntry cle, CommentEditCallback callback) {
		if(inst == null) {
			inst =  new CommentEditDialog();
		}
		inst.currentListEntry = cle;
		inst.currentCallback = callback;
		return inst;
	}

	private CommentListEntry currentListEntry;
	private CommentEditCallback currentCallback;

	private TextArea textBox;


	private CommentEditDialog() {
		super("Edit the comment below", true);
	}

	protected void onOkClick() {
		String s = textBox.getText();
		if(s.length()<1) {
			setErrorMsg("Comment is empty.");
			return;
		} else if (s.equals(currentListEntry.getCommentString())) {
			//do nothing
		} else {
			CommentEdit ce = CommentEdit.get();
			currentCallback.setPossibleCommentText(s);
			ce.send(currentCallback, currentListEntry.getId(), s, false);
		}
		//Comment.get().send(this);
		hide();

	}

	@Override
	public void center() {
		super.center();
		String noHTML = Common.reconvertHTMLToChar(currentListEntry.getCommentString());
		textBox.setText(noHTML);
	}

	protected Widget getDialogContentWidget() {
		textBox = new TextArea();
		textBox.setSize("260", "120");
		return textBox;
	}

}
