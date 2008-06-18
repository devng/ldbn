package se.umu.cs.ldbn.client;

import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.core.FD;
import java.util.List;

public final class Assignment {
	private AttributeNameTable domain;
	private List<FD> fds;
	
	public Assignment(AttributeNameTable domain, List<FD> fds) {
		this.domain = domain;
		this.fds = fds;
	}
	
	public List<FD> getFDs() {
		return fds;
	}
	
	public AttributeNameTable getDomain() {
		return domain;
	}
}
