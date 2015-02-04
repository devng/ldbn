package se.umu.cs.ldbn.client.io;

public interface CommentEditCallback {
	
	void onReseive(boolean isOK);
	
	void setPossibleCommentText(String str);

}
