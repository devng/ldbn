package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Class with static methods of different algorithms. 
 * 
 * For the detailed description of the algorithms see the following book:
 * Alfons Kemper, André Eickler: Datenbanksysteme, 6. Auflage. Oldenbourg Verlag
 * ISBN 3-486-5690-9
 * 
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 */
public class Algorithms {
	
	/**
	 * Calculates the attribute closure (Attribute Huelle - German, see p. 172).
	 *  
	 * @param a Set of attributes.
	 * @param fds Set of FDs
	 * @return Set of attributes such that a -> a+ 
	 * @see p. 172
	 */
	public static AttributeSet attributeClosure(AttributeSet a, List<FD> fds) {
		AttributeSet r = a.clone();
		long lastAtts = 0;
		do {
			lastAtts = r.attMask();
			for (int i = 0; i < fds.size(); i++) {
				FD fd = fds.get(i);
				if (fd.getLeftSide().isSubSetOf(r)) {
					r.union(fd.getRightSide());
				}
			}
		} while (lastAtts != r.attMask());
		return r;
	}
	
	/**
	 * Find the minimal cover ("kanonishe Ueberdeckung" German, see p. 173) of 
	 * an given set of FDs. The algorithm first makes 
	 * 	left reduction , then 
	 * 	right reduction , then 
	 * 	deleting all FDs such as A -> {},
	 * 	group all FD such as A -> B and A -> C to A -> BC
	 * @param f all given FDs of the relation
	 * @return the minimal cover as list of FD Objects. Note JAVA 5 Syntax 
	 * is not yet supported by GWT.
	 */
	public static void minimalCover(List<FD> f) {
		leftReduction(f);
		rightReduction(f);
		
		//clean FD such as A -> {}
		for (Iterator<FD> iter = f.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			if(fd.getRightSide().attMask() == 0L) {
				iter.remove();
			}
		}
		
		//group FD in such way that A -> B , A -> C equals to A -> BC
		AttributeSet[][] table = new AttributeSet[f.size()][2];
		int j = 0;
		for (Iterator<FD> iter = f.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			int i = containsEntry(table, fd.getLeftSide()) ;
			if(i >= 0) {
				table[i][1].union(fd.getRightSide());
			} else {
				table[j][0] = fd.getLeftSide();
				table[j][1] = fd.getRightSide();
				j++;
			}
		}
		f.clear();
		for (int i = 0; i < j; i++) {
			FD fd = new FD(table[i][0], table[i][1]);
			f.add(fd);
		}
	}
	
	private static int containsEntry(AttributeSet[][] t, AttributeSet key) {
		for (int i = 0; i < t.length; i++) {
			if(t[i][0] != null && t[i][0].equals(key)) return i;
		}
		return -1;
		
	}
	
	private static void leftReduction(List<FD> fds) {
		HashSet<FD> newFDs = new HashSet<FD>();
		HashSet<FD> toRemoveFDs = new HashSet<FD>();
		
		for (int i = 0; i < fds.size(); i++) {
			FD fd = ((FD) fds.get(i));

			AttributeSet leftSide =  fd.getLeftSide().clone();
			AttributeSetIterator iter = leftSide.iterator();
			for (; iter.hasNext();) {
				long j = iter.nextAttIndex();
				iter.remove();
				AttributeSet b = attributeClosure(leftSide, fds);
				if(fd.getRightSide().isSubSetOf(b)) {
					FD tmp = fd.clone();
					tmp.getLeftSide().removeAtt(j);
					newFDs.add(tmp);
					toRemoveFDs.add(fd);
				}
				leftSide.addAtt(j);
			}
		}
		for (Iterator<FD> it = toRemoveFDs.iterator(); it.hasNext();) {
			FD fd = it.next();
			fds.remove(fd);
		}
		
		for (Iterator<FD> it = newFDs.iterator(); it.hasNext();) {
			FD fd = it.next();
			if(!fds.contains(fd)) {
				fds.add(fd);
			}
		}
	}
	
	private static void rightReduction(List<FD> fds) {
		for (int i = 0; i < fds.size(); i++) {
			FD fd = ((FD) fds.get(i));
			AttributeSet rightSide = fd.getRightSide();
			AttributeSetIterator iter = rightSide.iterator();
			for(; iter.hasNext(); ) {
				long j = iter.nextAttIndex();
				iter.remove();
				AttributeSet b = attributeClosure(fd.getLeftSide(), fds);
				if(!b.containsAtt(j)) {
					rightSide.addAtt(j);
				}
			}
		}
	}
	
	/**
	 * Equivalence between sets of Functional Dependencies:
	 * 
Consider two sets of FDs, F and G,
F = {A -> B, B -> C, AC -> D} and
G = {A -> B, B -> C, A -> D}
Are F and G equivalent?
To determine their equivalence, we need to prove that F+ = G+. However, since computing F+ or
G+ is computationally expensive we take a short cut. We can conclude that F and G are
equivalent, if we can prove that all FDs in F can be inferred from the set of FDs in G and vice
versa.

For our example above, let us see if we can infer all FDs in F using G; we will use attribute
closure to determine that.
Take the attributes from the LHS of FDs in F and compute attribute closure for each using FDs in
G:
A+ using G = ABCD; A -> A; A -> B; A -> C; A -> D;
B+ using G = BC; B -> B; B -> C;
AC+ using G = ABCD; AC -> A; AC -> B; AC -> C; AC -> D;
Notice that all FDs in F can be inferred using FDs in G.
To see if all FDs in G are inferred by F, compute attribute closure for attributes on the LHS of
FDs in G using FDs in F:
A+ using F = ABCD; A -> A; A -> B; A -> C; A-> D;
B+ using F = BC; B -> B; B -> C;
Since all FDs in F can be obtained from G and vice versa, we conclude that F and G are
equivalent

	 * @param fdsF List of FDs F
	 * @param fdsG List of FDs G
	 * @return true if F and G are equivalent
	 */
	public static boolean equivalence(List<FD> fdsF, List<FD> fdsG) {
		for (Iterator<FD> iter = fdsF.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			AttributeSet a = attributeClosure(fd.getLeftSide(), fdsG);
			if(!a.containsAttSet(fd.getRightSide())) return false;
		}
		
		for (Iterator<FD> iter = fdsG.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			AttributeSet a = attributeClosure(fd.getLeftSide(), fdsF);
			if(!a.containsAttSet(fd.getRightSide())) return false;
		}
		return true;
	}
	
	public static boolean isKey(AttributeSet key, AttributeSet atts, 
			List<FD> fds) {
		AttributeSet b = attributeClosure(key, fds);
		return b.equals(atts);
	}
	
	public static List<Relation> synthese(Relation r) {
		List<Relation> result = new ArrayList<Relation>();
		List<FD> fds = r.getFds();
		minimalCover(fds);
		
		//create new relations
		for (Iterator<FD> iter = fds.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			List<FD> fds3nf = new ArrayList<FD>();
			fds3nf.add(fd);
			AttributeSet att3nf = fd.getLeftSide().clone();
			att3nf.union(fd.getRightSide());
			Relation r3nf = new Relation(
					att3nf,
					r.getAttributeNameTable(),
					null,
					fds3nf,
					"");
			result.add(r3nf);
			
		}
		
		//Find FDs for each relation
		for (Iterator<Relation> iter = result.iterator(); iter.hasNext();) {
			Relation r3nf = iter.next();
			for (Iterator<FD> iter2 = fds.iterator(); iter2.hasNext();) {
				FD fd3nf = iter2.next();
				if(r3nf.getAttrbutes().containsAttSet(fd3nf.getLeftSide()) &&
					r3nf.getAttrbutes().containsAttSet(fd3nf.getRightSide())) {
					if (!r3nf.getFds().contains(fd3nf)) {
						r3nf.getFds().add(fd3nf);
					}
				}
			}
		}
		
		//find a key for each relation
		for (Iterator<Relation> iter = result.iterator(); iter.hasNext();) {
			Relation r3nf = iter.next();
			List<FD> fds3nf = r3nf.getFds();
			for (Iterator<FD> iter2 = fds3nf.iterator(); iter2.hasNext();) {
				FD fd3nf = iter2.next();
				if(isKey(fd3nf.getLeftSide(), r3nf.getAttrbutes(), fds3nf)) {
					r3nf.setKeys(fd3nf.getLeftSide());
				}
			}
			if(r3nf.getKeys() == null) {
				r3nf.setKeys(r3nf.getAttrbutes());
			}
		}
		
		//eliminate relationships such as Ra is subset of Rb
		for (Iterator<Relation> iter = result.iterator(); iter.hasNext();) {
			Relation r1 = iter.next();
			for (Iterator<Relation> iter2 = result.iterator(); iter2.hasNext();) {
				Relation r2 = iter2.next();
				if(r2.getAttrbutes().isSubSetOf(r1.getAttrbutes())) {
					iter2.remove();
				}
			}
		}
		return result;
	}
	
	public static boolean isIn2NF(List<Relation> r) {
		for (Iterator<Relation> iter = r.iterator(); iter.hasNext();) {
			Relation r2nf = iter.next();
			if(r2nf.getKeys() != null) {
				if(r2nf.getKeys().size() == 1) {//TODO better
					return true;
				} else {
					AttributeSet key = r2nf.getKeys();
					AttributeSetIterator iter2 = key.iterator();
					for(; iter2.hasNext();) {
						AttributeSet tmp = new AttributeSet(r2nf.getKeys().domain());
						long tmpIndex = iter2.nextAttIndex();
						tmp.addAtt(tmpIndex);
						AttributeSet b = attributeClosure(tmp, r2nf.getFds());
						if(b.attMask() != 0) {
							return false;
						}
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isIn3NF(List<Relation> r) {
		for (Iterator<Relation> iter = r.iterator(); iter.hasNext();) {
			//Relation r3nf = (Relation) iter.next();
		}
		return false;
	}
}
