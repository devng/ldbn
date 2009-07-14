package se.umu.cs.ldbn.client.io;

public final class UserListEntry {
	
	private String id;
	private String name;
	private boolean isAdmin;
	private boolean isSuperUser;
	
	
	public UserListEntry(String id, String name, boolean isAdmin, 
			boolean isSuperUser) {
		this.id = id;
		this.isAdmin = isAdmin;
		this.name = name;
		this.isSuperUser = isSuperUser;
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
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isSuperUser() {
		return isSuperUser;
	}
	public void setSuperUser(boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

}
