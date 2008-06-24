package se.umu.cs.ldbn.client.core;

import java.util.List;

public final class Relation {
	
	private AttributeSet attrbutes;
	private AttributeSet keys;
	private List<FD> fds;
	
	public Relation() {
	}
	
	public Relation(AttributeSet attributes) {
		this.attrbutes = attributes;
	}
	
	public void setAttributes(AttributeSet attributes) {
		this.attrbutes = attributes;
	}

	public AttributeSet getAttrbutes() {
		return attrbutes;
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
	
	public void setFDs(List<FD> fds) {
		this.fds = fds;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Relation : ");
		sb.append(attrbutes.toString());
		sb.append('\n');
		if(fds != null) {
			for (FD fd : fds) {
				sb.append(fd.toString());
				sb.append('\n');
			}
		} else {
			sb.append("FDs are NULL!!!");
		}
		sb.append("------------------------------");
		return sb.toString();
	}
}
