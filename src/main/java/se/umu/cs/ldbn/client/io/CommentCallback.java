package se.umu.cs.ldbn.client.io;

import java.util.List;

public interface CommentCallback {
	void onCommentsReceived(List<CommentListEntry> comments, Integer assignentID);
	Integer getAssignmentID();
	//can be null if no comment
	String getComment();
}
