package se.umu.cs.ldbn.shared.core;

import java.util.List;

public final class Assignment {

	private DomainTable domain;
	private List<FD> fds;
	private Integer id;
	private String name;

	public Assignment(DomainTable domain, List<FD> fds) {
		this.domain = domain;
		this.fds = fds;
	}

	public List<FD> getFDs() {
		return fds;
	}

	public DomainTable getDomain() {
		return domain;
	}

	public void setID(Integer id) {
		this.id = id;
	}

	public Integer getID() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
