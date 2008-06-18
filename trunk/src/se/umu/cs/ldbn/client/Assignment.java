package se.umu.cs.ldbn.client;

import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
import java.util.List;

public final class Assignment {
	private DomainTable domain;
	private List<FD> fds;
	
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
}
