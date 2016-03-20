package se.umu.cs.ldbn.shared.core;


public final class FD {
	private AttributeSet leftHandSide;
	private AttributeSet rightHandSide;

	public FD(DomainTable domain) {
		leftHandSide = new AttributeSet(domain);
		rightHandSide = new AttributeSet(domain);
	}

	public FD(DomainTable domain, String[] lhs, String[] rhs) {
		leftHandSide = new AttributeSet(domain);
		rightHandSide = new AttributeSet(domain);
		for (String lh : lhs) {
			leftHandSide.addAtt(lh);
		}
		for (String rh : rhs) {
			rightHandSide.addAtt(rh);
		}
	}

	public FD(AttributeSet l, AttributeSet r) {
		leftHandSide = l;
		rightHandSide = r;
	}

	public void recalculateMask() {
		leftHandSide.recalculateMask();
		rightHandSide.recalculateMask();
	}

	public AttributeSet getLHS() {
		return leftHandSide;
	}

	public AttributeSet getRHS() {
		return rightHandSide;
	}

	public boolean containsAtt(int attIndex) {
		return leftHandSide.containsAtt(attIndex)
				&& rightHandSide.containsAtt(attIndex);
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
