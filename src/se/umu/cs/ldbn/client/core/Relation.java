package se.umu.cs.ldbn.client.core;

import java.util.List;

public final class Relation {
	
	private AttributeSet attrbutes;
	private List<AttributeSet> allKeyCandidates;
	private AttributeSet superKey;
	private List<FD> fds;
	private String name;
	
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
	
	public AttributeSet getSuperKey() {
		return superKey;
	}

	public void setSuperKey(AttributeSet superKey) {
		this.superKey = superKey;
	}

	public List<AttributeSet> getKeyCandidates() {
		return allKeyCandidates;
	}


	public void setKeyCandidates(List<AttributeSet> keys) {
		this.allKeyCandidates = keys;
	}

	public List<FD> getFds() {
		return fds;
	}
	
	public void setFDs(List<FD> fds) {
		this.fds = fds;
	}
	
	public int hashCode() {
		return attrbutes.hashCode();
	}
	
	public boolean equals(Object o) {
		if(o instanceof Relation) {
			Relation that = (Relation) o;
			return that.attrbutes.equals(this.attrbutes);
		}
		return false;
	}
	 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Relation : ");
		sb.append(attrbutes.toString());
		sb.append('\n');
		
		if(allKeyCandidates != null) {
			sb.append("Keys:\n");
			for (AttributeSet key : allKeyCandidates) {
				sb.append(key.toString());
				sb.append('\n');
			}
		} else {
			sb.append("Keys are NULL.\n");
		}
		if(fds != null) {
			for (FD fd : fds) {
				sb.append(fd.toString());
				sb.append('\n');
			}
		} else {
			sb.append("FDs are NULL.\n");
		}
		sb.append("------------------------------");
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
