package se.umu.cs.ldbn.client.core;

import java.util.List;

public final class Assignment {
	private DomainTable domain;
	private List<FD> fds;
	private String id;
//	private boolean isLoadedFromDB;
	
	public Assignment(DomainTable domain, List<FD> fds) {
		this.domain = domain;
		this.fds = fds;
//		isLoadedFromDB = true;
	}
	
	public List<FD> getFDs() {
		return fds;
	}
	
	public DomainTable getDomain() {
		return domain;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
//	
//	public boolean isLoadedFromDB() {
//		return isLoadedFromDB;
//	}
//	
//	public void setLoadedFromDB(boolean isLoadedFromDB) {
//		this.isLoadedFromDB = isLoadedFromDB;
//	}
}
