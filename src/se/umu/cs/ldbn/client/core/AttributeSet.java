package se.umu.cs.ldbn.client.core;

public class AttributeSet {
	
	private AttributeNameTable domain;
	private long attMask;
	
	
	private class Iterator implements AttributeSetIterator {

		private long index = 0;
		private long next = 0;
		private boolean hasNext = false;
		
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
		this.domain = domain;
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
	
	public boolean addAtt(String attName) {
		long i = domain.getAttIndex(attName);
		if  (i != 0) {
			attMask = attMask | i;
			return true;
		}
		return false;
	}
	
	public boolean addAtt(long attIndex) {
		if  (domain.containsAttIndex(attIndex)) {
			attMask = attMask | attIndex;
			return true;
		}
		return false;
	}
	
	public boolean removeAtt(String attName) {
		long attIndex = domain.getAttIndex(attName);
		if (attIndex != 0) {
			attMask = attMask & (~attIndex);
			return true;
		}
		return false;
	}
	
	public boolean removeAtt(long attIndex) {
		if  (domain.containsAttIndex(attIndex)) {
			attMask = attMask & (~attIndex);
			return true;
		}
		return false;
	}
	
	public void union (AttributeSet a) {
		if (a.domain == this.domain) {
			this.attMask = this.attMask | a.attMask;
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
	
	protected long attMask() {
		return attMask;
	}
	
	protected AttributeNameTable domain() {
		return domain;
	}
	
	protected int size() {
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


