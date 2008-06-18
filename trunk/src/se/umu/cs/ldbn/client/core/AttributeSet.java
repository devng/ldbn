package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.List;

public final class AttributeSet {
	
	private AttributeNameTable domain;
	private long attMask;
	
	//cache
	private boolean hasCahnge;
	private List<String> attNames;
	
	private class Iterator implements AttributeSetIterator {

		private long index;
		private long next;
		private boolean hasNext;
		
		public Iterator() {
			index = 0;
			next = 0;
			hasNext = false;
		}
		
		public long nextAttIndex() {
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
			for (long i = index; i < 64; i++) {
				long mask = 1l << i;
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
	
	public AttributeSet (AttributeNameTable domain) {
		if(domain == null) {
			throw new IllegalArgumentException("AttributeNameTable cannot be null");
		}
		this.domain = domain;
		attNames = new ArrayList<String>();
		hasCahnge = true;
	}
	
	public AttributeSet (AttributeNameTable domain, String[] att) {
		if(domain == null) {
			throw new IllegalArgumentException("AttributeNameTable cannot be null");
		}
		
		if(domain == null) {
			throw new IllegalArgumentException("String[] att - cannot be null");
		}
		this.domain = domain;
		for (String str : att) {
			addAtt(str);
		}
		attNames = new ArrayList<String>();
		hasCahnge = true;
	}
	
	
	
	public boolean containsAtt(String attName) {
		long attIndex = domain.getAttIndex(attName);
		if (attIndex != 0) {
			long r = attMask & attIndex;
			return r != 0;
		} 
		return false;
	}
	
	public boolean containsAtt(long attIndex) {
		if (domain.containsAttIndex(attIndex)) {
			long r = attMask & attIndex;
			return r != 0;
		}
		return false;
	}
	
	public void recalculateMask() {
		hasCahnge = true;
		long l = domain.getAttIndicesAsLong();
		attMask &= l; 
	}
	
	public boolean addAtt(String attName) {
		long i = domain.getAttIndex(attName);
		if  (i != 0) {
			attMask = attMask | i;
			hasCahnge = true;
			return true;
		}
		return false;
	}
	
	public boolean addAtt(long attIndex) {
		if  (domain.containsAttIndex(attIndex)) {
			attMask = attMask | attIndex;
			hasCahnge = true;
			return true;
		}
		return false;
	}
	
	public boolean removeAtt(String attName) {
		long attIndex = domain.getAttIndex(attName);
		if (attIndex != 0) {
			attMask = attMask & (~attIndex);
			hasCahnge = true;
			return true;
		}
		return false;
	}
	
	public void clearAllAttributes() {
		attMask = 0L;
		hasCahnge = true;
	}
	
	public boolean removeAtt(long attIndex) {
		if  (domain.containsAttIndex(attIndex)) {
			attMask = attMask & (~attIndex);
			hasCahnge = true;
			return true;
		}
		return false;
	}
	
	public void union (AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask | a.attMask;
			hasCahnge = true;
		}
	}
	
	public void removeAttSet(AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask & (~a.attMask);
			hasCahnge = true;
		}	
	}
	
	public void andOperator(AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask & a.attMask;
			hasCahnge = true;
		}
	}
	
	public boolean isSubSetOf(AttributeSet a) {
		if (a.domain == this.domain) {
			long r = a.attMask & this.attMask;
			return r == this.attMask;
		}
		return false;
	}
	
	public boolean containsAttSet(AttributeSet a) {
		if (a.domain == this.domain) {
			long r = a.attMask & this.attMask;
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
		return (int)(attMask ^ (attMask >>> 32));
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		long one = 1;
		for (int j = 0; j < 64; j++) {
			long attIndex = attMask & (one << j);
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
		if(hasCahnge) {
			attNames.clear();
			for (AttributeSetIterator iter = iterator(); iter.hasNext();) {
				String str = iter.next();
				attNames.add(str);
			}
			hasCahnge = false;
		}
		return attNames;
	}
	
	long attMask() {
		return attMask;
	}
	
	void setMask(long mask) {
		this.attMask = mask;
	}
	
	AttributeNameTable domain() {
		return domain;
	}
	
	int size() {
		int size = 0;
		long one = 1;
		for (int j = 0; j < 64; j++) {
			long attIndex = attMask & (one << j);
			if(attIndex != 0) {
				size++;
			}
		}
		return size;
	}
}


