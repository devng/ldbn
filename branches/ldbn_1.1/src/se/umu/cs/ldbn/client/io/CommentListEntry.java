package se.umu.cs.ldbn.client.io;

public class CommentListEntry {
	private String id;
	private String author;
	private String authorID;
	private String lastModified;
	private String commentString;
	
	public CommentListEntry(String id, String authorID, String author, 
			String lastModified, String commentString) {
		this.id = id;
		this.authorID = authorID;
		this.author = author;
		this.lastModified = lastModified;
		this.commentString = commentString;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorID() {
		return authorID;
	}
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public String getCommentString() {
		return commentString;
	}
	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}
}
