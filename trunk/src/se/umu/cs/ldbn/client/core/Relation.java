package se.umu.cs.ldbn.client.core;

import java.util.List;

public class Relation {
	private AttributeSet attrbutes;
	private AttributeNameTable domain;
	private AttributeSet keys;
	private List<FD> fds;
	private String name; //optional
	
	
	public Relation(AttributeSet attributes, AttributeNameTable domain, AttributeSet keys,
			List<FD> fds, String name) {
		this.attrbutes = attributes;
		this.domain = domain;
		this.keys = keys;
		this.fds = fds;
		this.name = name;
	}


	public AttributeSet getAttrbutes() {
		return attrbutes;
	}


	public void setAttrbutes(AttributeSet attrbutes) {
		this.attrbutes = attrbutes;
	}


	public AttributeNameTable getAttributeNameTable() {
		return domain;
	}


	public void setAttributeNameTable(AttributeNameTable domain) {
		this.domain = domain;
	}


	public AttributeSet getKeys() {
		return keys;
	}


	public void setKeys(AttributeSet keys) {
		this.keys = keys;
	}


	public List<FD> getFds() {
		return fds;
	}


	public void setFds(List<FD> fds) {
		this.fds = fds;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
}
