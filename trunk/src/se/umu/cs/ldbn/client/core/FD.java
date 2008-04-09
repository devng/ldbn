package se.umu.cs.ldbn.client.core;

public class FD {
	private AttributeSet left_side;
	private AttributeSet right_side;
	
	public FD(AttributeNameTable domain) {
		left_side = new AttributeSet(domain);
		right_side = new AttributeSet(domain);
	}
	
	public FD(AttributeSet l, AttributeSet r) {
		left_side = l;
		right_side = r;
	}
	
	public AttributeSet getLeftSide() {
		return left_side;
	}
	
	public AttributeSet getRightSide() {
		return right_side;
	}
	
	public boolean equals(Object o) {
		if (o instanceof FD) {
			FD that = (FD) o;
			return that.left_side.equals(this.left_side) &&
				that.right_side.equals(this.right_side);
		}
 		return false;
	}
	
	public int hashCode() { //TODO doit better
		return this.left_side.hashCode() * 31 * this.right_side.hashCode();
	}
	
	public String toString() {
		return left_side.toString() + "-> " + right_side.toString();
	}
	
	public FD clone() {
		FD r = new FD(left_side.clone(), right_side.clone());
		return r;
	}
}
