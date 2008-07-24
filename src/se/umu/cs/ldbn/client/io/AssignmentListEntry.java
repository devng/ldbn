package se.umu.cs.ldbn.client.io;

public class AssignmentListEntry 
	implements Comparable<AssignmentListEntry> {
	
	public enum compareAttribute {id, name, author, modified}
	
	private static compareAttribute defaultComparationAtt = compareAttribute.id;
	
	public static void setCompareAttribute (compareAttribute att) {
		defaultComparationAtt = att;
	}
	
	private String id;
	private String name;
	private String author;
	private String authorID;
	private String modifiedOn;
	
	public AssignmentListEntry(String id, String name, String author, 
			String authorID, String modifiedOn) {
		if(id == null || name == null || author == null || authorID == null || 
				modifiedOn == null) {
			throw new IllegalArgumentException("Some arguments are null.");
		}
		this.id = id;
		this.name = name;
		this.author = author;
		this.authorID = authorID;
		this.modifiedOn = modifiedOn;
	}
	
	public int compareTo(AssignmentListEntry o) {
		switch (defaultComparationAtt) {
		case name:
			return this.name.compareToIgnoreCase(o.name);
		case author:
			return this.authorID.compareToIgnoreCase(o.authorID);
		case modified:
			return this.modifiedOn.compareToIgnoreCase(o.modifiedOn);
		case id:
		default:
			return this.id.compareToIgnoreCase(o.id);
		}
	}

	public static compareAttribute getDefaultComparationAtt() {
		return defaultComparationAtt;
	}

	public static void setDefaultComparationAtt(
			compareAttribute defaultComparationAtt) {
		AssignmentListEntry.defaultComparationAtt = defaultComparationAtt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	} 

}
