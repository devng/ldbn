package se.umu.cs.ldbn.shared.core;

import java.util.ArrayList;
import java.util.List;

public final class AttributeSet {

	private DomainTable domain;
	private int attMask;

	//cache
	private boolean hasChanged;
	private List<String> attNames;

	private class Iterator implements AttributeSetIterator {

		private int index;
		private int next;
		private boolean hasNext;

		public Iterator() {
			index = 0;
			next = 0;
			hasNext = false;
		}

		public int nextAttIndex() {
			if(hasNext) {
				hasNext = false;
				index++;
				return next;
			}
			return 0;
		}

		public String nextAttName() {
			if(hasNext) {
				hasNext = false;
				index++;
				return domain.getAttName(next);
			}
			return null;
		}

		public boolean hasNext() {
			for (int i = index; i < 32; i++) {
				int mask = 1 << i;
				if((attMask & mask) != 0) {
					next = mask;
					index = i;
					hasNext = true;
					return true;
				}
			}
			hasNext = false;
			return false;
		}

		public String next() {
			return nextAttName();
		}

		public void remove() {
			removeAtt(next);
		}
	}

	public AttributeSet (DomainTable domain) {
		if(domain == null) {
			throw new IllegalArgumentException("DomainTable cannot be null");
		}
		this.domain = domain;
		attNames = new ArrayList<>();
		hasChanged = true;
	}

	public AttributeSet (DomainTable domain, String[] att) {
		if(domain == null) {
			throw new IllegalArgumentException("DomainTable cannot be null");
		}
		this.domain = domain;
		for (String str : att) {
			addAtt(str);
		}
		attNames = new ArrayList<>();
		hasChanged = true;
	}



	public boolean containsAtt(String attName) {
		int attIndex = domain.getAttIndex(attName);
		if (attIndex != 0) {
			int r = attMask & attIndex;
			return r != 0;
		}
		return false;
	}

	public boolean containsAtt(int attIndex) {
		if (domain.containsAttIndex(attIndex)) {
			int r = attMask & attIndex;
			return r != 0;
		}
		return false;
	}

	public void recalculateMask() {
		hasChanged = true;
		int i = domain.getAttIndicesAsInteger();
		attMask &= i;
	}

	public boolean addAtt(String attName) {
		int i = domain.getAttIndex(attName);
		if  (i != 0) {
			attMask = attMask | i;
			hasChanged = true;
			return true;
		}
		return false;
	}

	public boolean addAtt(int attIndex) {
		if  (domain.containsAttIndex(attIndex)) {
			attMask = attMask | attIndex;
			hasChanged = true;
			return true;
		}
		return false;
	}

	public boolean removeAtt(String attName) {
		int attIndex = domain.getAttIndex(attName);
		if (attIndex != 0) {
			attMask = attMask & (~attIndex);
			hasChanged = true;
			return true;
		}
		return false;
	}

	public void clearAllAttributes() {
		attMask = 0;
		hasChanged = true;
	}

	public boolean removeAtt(int attIndex) {
		if  (domain.containsAttIndex(attIndex)) {
			attMask = attMask & (~attIndex);
			hasChanged = true;
			return true;
		}
		return false;
	}

	public void union (AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask | a.attMask;
			hasChanged = true;
		}
	}

	public void removeAttSet(AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask & (~a.attMask);
			hasChanged = true;
		}
	}

	public void andOperator(AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask & a.attMask;
			hasChanged = true;
		}
	}

	public boolean isSubSetOf(AttributeSet a) {
		if (a.domain == this.domain) {
			int r = a.attMask & this.attMask;
			return r == this.attMask;
		}
		return false;
	}

	public boolean isEmpty() {
		return attMask == 0;
	}

	public boolean containsAttSet(AttributeSet a) {
		if (a.domain == this.domain) {
			int r = a.attMask & this.attMask;
			return r == a.attMask;
		}
		return false;
	}

	public boolean equals(Object o) {
		if(o instanceof AttributeSet) {
			AttributeSet that = (AttributeSet) o;
			return this.attMask == that.attMask;
		}
		return false;
	}

	public int hashCode() {
		return attMask;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		int one = 1;
		for (int j = 0; j < 32; j++) {
			int attIndex = attMask & (one << j);
			if(attIndex != 0) {
				String name = domain.getAttName(attIndex);
				if(name != null) {
					sb.append(name);
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}

	public AttributeSet clone() {
		AttributeSet result = new AttributeSet(domain);
		result.attMask = attMask;
		return result;
	}


	public AttributeSetIterator iterator() {
		return new Iterator();
	}

	public List<String> getAttributeNames() {
		if(hasChanged) {
			attNames.clear();
			for (AttributeSetIterator iter = iterator(); iter.hasNext();) {
				String str = iter.next();
				attNames.add(str);
			}
			hasChanged = false;
		}
		return attNames;
	}

	int attMask() {
		return attMask;
	}

	void setMask(int mask) {
		this.attMask = mask;
	}

	public DomainTable domain() {
		return domain;
	}

	int size() {
		int size = 0;
		int one = 1;
		for (int j = 0; j < 32; j++) {
			int attIndex = attMask & (one << j);
			if(attIndex != 0) {
				size++;
			}
		}
		return size;
	}
}


