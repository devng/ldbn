package se.umu.cs.ldbn.shared.core;

import java.util.ArrayList;
import java.util.List;

public final class Relation {

	private AttributeSet attributes;
	private List<AttributeSet> allKeyCandidates;
	private AttributeSet primaryKey;
	private List<FD> fds;
	private String name;

	public Relation() {
	}

	public Relation(AttributeSet attributes) {
		this.attributes = attributes;
	}

	public void setAttributes(AttributeSet attributes) {
		this.attributes = attributes;
	}

	public AttributeSet getAttributes() {
		return attributes;
	}

	public AttributeSet getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(AttributeSet primaryKey) {
		this.primaryKey = primaryKey;
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
		return attributes.hashCode();
	}

	public boolean equals(Object o) {
		if(o instanceof Relation) {
			Relation that = (Relation) o;
			return that.attributes.equals(this.attributes);
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Relation : ");
		sb.append(attributes.toString());
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


	public Relation clone() {
		Relation r = new Relation();
		if(this.attributes != null) {
			r.attributes = this.attributes.clone();
		}

		if(this.allKeyCandidates != null) {
			ArrayList<AttributeSet> tmp = new ArrayList<>();
			for (AttributeSet key : allKeyCandidates) {
				tmp.add(key.clone());
			}
			r.allKeyCandidates = tmp;
		}

		if(primaryKey != null) {
			r.primaryKey = primaryKey.clone();
		}

		if(name != null) {
			r.name = new String(name);
		}

		if(fds != null) {
			ArrayList<FD> tmp = new ArrayList<>(fds.size());
			for (FD fd : fds) {
				if(fd != null) {
					tmp.add(fd.clone());
				}
			}
			r.fds = tmp;
		}


		return r;
	}
}
