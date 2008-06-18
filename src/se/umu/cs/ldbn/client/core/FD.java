package se.umu.cs.ldbn.client.core;


public final class FD {
	private AttributeSet leftHandSide;
	private AttributeSet rightHandSide;
	
	public FD(AttributeNameTable domain) {
		leftHandSide = new AttributeSet(domain);
		rightHandSide = new AttributeSet(domain);
	}
	
	public FD(AttributeNameTable domain, String[] lhs, String[] rhs) {
		leftHandSide = new AttributeSet(domain);
		rightHandSide = new AttributeSet(domain);
		for (int i = 0; i < lhs.length; i++) {
			leftHandSide.addAtt(lhs[i]);
		}
		for (int i = 0; i < rhs.length; i++) {
			rightHandSide.addAtt(rhs[i]);
		}
	}
	
	public void recalculateMask() {
		leftHandSide.recalculateMask();
		rightHandSide.recalculateMask();
	}
	
	public FD(AttributeSet l, AttributeSet r) {
		leftHandSide = l;
		rightHandSide = r;
	}
	
	public AttributeSet getLHS() {
		return leftHandSide;
	}
	
	public AttributeSet getRHS() {
		return rightHandSide;
	}
	
	public boolean equals(Object o) {
		if (o instanceof FD) {
			FD that = (FD) o;
			return that.leftHandSide.equals(this.leftHandSide) &&
				that.rightHandSide.equals(this.rightHandSide);
		}
 		return false;
	}
	
	public int hashCode() { //TODO doit better
		return this.leftHandSide.hashCode() * 31 * this.rightHandSide.hashCode();
	}
	
	public String toString() {
		return leftHandSide.toString() + "-> " + rightHandSide.toString();
	}
	
	public FD clone() {
		FD r = new FD(leftHandSide.clone(), rightHandSide.clone());
		return r;
	}
}
