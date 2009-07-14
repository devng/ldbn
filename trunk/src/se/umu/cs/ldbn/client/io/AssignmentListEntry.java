package se.umu.cs.ldbn.client.io;

public class AssignmentListEntry 
	implements Comparable<AssignmentListEntry> {
	
	public enum compareAttribute {id, name, author, isAdmin, modified}
	
	private static compareAttribute defaultComparationAtt = compareAttribute.id;
	private static boolean isDec = false;
	
	public static void setCompareAttribute (compareAttribute att) {
		defaultComparationAtt = att;
	}
	
	public static void setDecreasing(boolean isDecreasing) {
		isDec = isDecreasing;
		
	}
	
	private String id;
	private String name;
	private String author;
	private String authorID;
	private String modifiedOn;
	private boolean isAdmin;
	
	public AssignmentListEntry(String id, String name, String authorID, 
			String author, boolean isAdmin, String modifiedOn) {
		if(id == null || name == null || author == null || authorID == null || 
				modifiedOn == null) {
			throw new IllegalArgumentException("Some arguments are null.");
		}
		this.id = id;
		this.name = name;
		this.author = author;
		this.authorID = authorID;
		this.modifiedOn = modifiedOn;
		this.isAdmin = isAdmin;
	}
	
	public int compareTo(AssignmentListEntry o) {
		int result;
		switch (defaultComparationAtt) {
		case name:
			result = this.name.compareToIgnoreCase(o.name);
			break;
		case author:
			result = this.authorID.compareToIgnoreCase(o.authorID);
			break;
		case modified:
			result = this.modifiedOn.compareToIgnoreCase(o.modifiedOn);
			break;
		case id:
		default:
			result = this.id.compareToIgnoreCase(o.id);
			break;
		}
		if(isDec) {
			result = -result;
		}
		return result;
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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	} 

}
