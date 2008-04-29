package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Class with static methods of different algorithms. 
 * 
 * For the detailed description of the algorithms see the following book:
 * Alfons Kemper, André Eickler: Datenbanksysteme, 6. Auflage. Oldenbourg Verlag
 * ISBN 3-486-5690-9
 * 
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 */
public final class Algorithms {
	
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
				if (fd.getLHS().isSubSetOf(r)) {
					r.union(fd.getRHS());
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
	 * @return the minimal cover as list of FD Objects.
	 */
	public static void minimalCover(List<FD> f) {
		leftReduction(f);
		rightReduction(f);
		
		//clean FD such as A -> {}
		for (Iterator<FD> iter = f.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			if(fd.getRHS().attMask() == 0L) {
				iter.remove();
			}
		}
		
		//group FD in such way that A -> B , A -> C equals to A -> BC
		AttributeSet[][] table = new AttributeSet[f.size()][2];
		int j = 0;
		for (Iterator<FD> iter = f.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			int i = containsEntry(table, fd.getLHS()) ;
			if(i >= 0) {
				table[i][1].union(fd.getRHS());
			} else {
				table[j][0] = fd.getLHS();
				table[j][1] = fd.getRHS();
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

			AttributeSet leftSide =  fd.getLHS().clone();
			AttributeSetIterator iter = leftSide.iterator();
			for (; iter.hasNext();) {
				long j = iter.nextAttIndex();
				iter.remove();
				AttributeSet b = attributeClosure(leftSide, fds);
				if(fd.getRHS().isSubSetOf(b)) {
					FD tmp = fd.clone();
					tmp.getLHS().removeAtt(j);
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
			AttributeSet rightSide = fd.getRHS();
			AttributeSetIterator iter = rightSide.iterator();
			for(; iter.hasNext(); ) {
				long j = iter.nextAttIndex();
				iter.remove();
				AttributeSet b = attributeClosure(fd.getLHS(), fds);
				if(!b.containsAtt(j)) {
					rightSide.addAtt(j);
				}
			}
		}
	}
	
	/**
	 * Convert FDs in such a form, so that all fds in the RHS (right hand sinde)
	 * have only one attribute
	 * 
	 * @param fds list of FDs
	 * @return the canonical form of the list
	 */
	public static List<FD> canonicalForm(List<FD> fds) {
		ArrayList<FD> result = new ArrayList<FD>();
		for (FD fd : fds) {
			for (AttributeSetIterator iter = fd.getRHS().iterator(); iter
			.hasNext();) {
				FD tmp = new FD(fd.getLHS().domain());
				tmp.getLHS().union(fd.getLHS());
				tmp.getRHS().addAtt(iter.nextAttIndex());
				result.add(tmp);
			}
		}
		return result;
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
			if(!member(fd, fdsG)) return false;
		}
		
		for (Iterator<FD> iter = fdsG.iterator(); iter.hasNext();) {
			FD fd = iter.next();
			if(!member(fd, fdsF)) return false;
		}
		return true;
	}
	
	/**
	 * Checks if the memberCandidate FD is in FDS+ (FD closure).
	 * 
	 * @param memberCandidate
	 * @param fds
	 * @return
	 */
	public static boolean member(FD memberCandidate, List<FD> fds) {
		AttributeSet lhsClosure = attributeClosure(memberCandidate.getLHS(), 
				fds);
		if(lhsClosure.containsAttSet(memberCandidate.getRHS())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gives all key candidates of a relationship, thus the keys are minimal.
	 * 
	 * @param fds
	 * @param atts
	 * @return
	 */
	public static List<AttributeSet> findAllKeyCandidates(List<FD> fds, AttributeSet atts) {
		
		long mask = atts.attMask();
		List<AttributeSet> keys = new ArrayList<AttributeSet>();
		for (long l = 0; l <= mask; l++) {
			long keyMask = mask & l;
			if(keyMask != 0L) {
				AttributeSet key = new AttributeSet(atts.domain());
				key.setMask(keyMask);
				
				if(isKey(key, atts, fds)) {
					boolean addKey = true;
					for (Iterator<AttributeSet> i = keys.iterator(); 
						i.hasNext();) {
						AttributeSet k = i.next();
						if(key.containsAttSet(k)) { // E.g. ABC contains AB, thus dont add ABC
							addKey = false;
						} else if (k.containsAttSet(key)) { // E.g. AB from the key list contains the new key A, thus remove AB
							i.remove();
						}
					}
					if(addKey) {
						keys.add(key);
					}
				}
			}
		}
		return keys;
	}
	
	/**
	 * Checks if a attribute set is a key. It does not test if the key is 
	 * minimal. 
	 * @param key 
	 * @param atts
	 * @param fds
	 * @return
	 */
	public static boolean isKey(AttributeSet key, AttributeSet atts, 
			List<FD> fds) {
		AttributeSet b = attributeClosure(key, fds);
		return b.equals(atts);
	}

	
	//TODO better or use find allKeyCandidates
	public static AttributeSet findKey(Relation r) {
		List<FD> fds = r.getFds();
		if(fds == null) {
			throw new IllegalArgumentException("Relation has no assosiate FDs");
		}
		for (FD fd : fds) {
			AttributeSet lhs = fd.getLHS();
			if(isKey(lhs, r.getAttrbutes(), fds)) {
				return lhs.clone();
			}
		}
		return r.getAttrbutes().clone();
	}
	
	/**
	 * Check if a decomposition (a list of relationships) is dependency 
	 * preserving. Dependency preservation ensures that no FDs are lost during
	 * a decomposition. By definition a decomposition is dependency preserving
	 * if the  union of all FDs of all dependencies in the decomposition is 
	 * within the FD closure of the initial FDs. Since computing the FD closure 
	 * is quite an expensive operation, a different approach is taken. Since we
	 * know all the initial FDs, as they are part of the assignment, we can use
	 * a bottom up approach and test if all of the initial FDs are part of the 
	 * relation FDs' closure. Thus we build a collection of all FDs, contained 
	 * in the decomposition, and use the member algorithm to test if the initial
	 * FDs are members of its closure.
	 * 
	 * @param initialFDs the FDs which were originally given in the assignment.
	 * @param decomposition a  List of all relationships in a decomposition. 
	 * @return true if the decomposition is dependency preserving. 
	 */
	public static boolean isDependencyPreserving(List<FD> initialFDs, 
		List<Relation> decomposition) {
		List<FD> decompositionFDs = new ArrayList<FD>();
		for (Relation relation : decomposition) {
			List<FD> rFDs = relation.getFds();
			if(rFDs != null) {
				decompositionFDs.addAll(rFDs);
			}
		}
		
		return equivalence(initialFDs, decompositionFDs);
	}
	
	public static List<FD> reductionByResolution(AttributeSet u, List<FD> f,
			AttributeSet r) {
		List<FD> canonicalForm = canonicalForm(f);
		ArrayList<FD> g = new ArrayList<FD>(canonicalForm); // G = F
		AttributeSet x = u.clone();  // X = U - R
		x.removeAttSet(r);
		
		AttributeSetIterator iter = x.iterator();
		for (; iter.hasNext();) {
			String a = iter.next();
			iter.remove();
			ArrayList<FD> res = new ArrayList<FD>();
			for (FD fd : g) {
				if(fd.getRHS().containsAtt(a)) {
					for (FD fd2 : g) {
						if(fd2.getLHS().containsAtt(a)) {
							FD h = fd2.clone();
							h.getLHS().removeAtt(a);
							h.getLHS().union(fd.getLHS());
							if(!isTrivial(h)) {
								res.add(h);
							}
						}
					}
				}
			}
			for (Iterator<FD> i = g.iterator(); i.hasNext();) {
				FD fd3 = i.next();
				if(fd3.getLHS().containsAtt(a) || fd3.getRHS().containsAtt(a)) {
					i.remove();
				}
			}
			g.addAll(res);
		}
		return g;
	}
	
	
	
	/**
	 * checks if elements in the LHS are present in the RHS
	 * @param f an FD
	 * @return true if an element of the LHS is present in the RHS as well.
	 */
	private static boolean isTrivial(FD f) {
		if((f.getLHS().attMask() & f.getRHS().attMask()) == 0L) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a decomposition has the lossless/nonaditive join property. 
	 * The algorithm is the one proposed in Elmasri's Book. 
	 * 
	 * @param initialFDs
	 * @param atts
	 * @param decomposition
	 * @return
	 */
	public static boolean isLossless(List<FD> initialFDs, AttributeSet atts, 
			List<Relation> decomposition) {
		AttributeSet[] s = new AttributeSet[decomposition.size()];
		long[] sMask = new long[s.length];
		for (int i = 0; i < s.length; i++) {
			s[i] = decomposition.get(i).getAttrbutes().clone();
			sMask[i] = s[i].attMask();
		}
		do {
			for (FD fd : initialFDs) {
				for (int i = 0; i < s.length; i++) {
					if(s[i].containsAttSet(fd.getLHS())) {
						s[i].union(fd.getRHS());
					}
				}
			}
		} while (hasSChanged(sMask, s));
		
		for (int i = 0; i < s.length; i++) {
			if(s[i].containsAttSet(atts)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasSChanged(long[] sMask, AttributeSet[] s) {
		boolean result = false;
		for (int i = 0; i < s.length; i++) {
			if(sMask[i] != s[i].attMask()) {
				result = true;
				sMask[i] = s[i].attMask();
			}
		}
		return result;
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
			AttributeSet att3nf = fd.getLHS().clone();
			att3nf.union(fd.getRHS());
			Relation r3nf = new Relation(
					att3nf);
			result.add(r3nf);
			
		}
		
		//Find FDs for each relation
		for (Iterator<Relation> iter = result.iterator(); iter.hasNext();) {
			Relation r3nf = iter.next();
			for (Iterator<FD> iter2 = fds.iterator(); iter2.hasNext();) {
				FD fd3nf = iter2.next();
				if(r3nf.getAttrbutes().containsAttSet(fd3nf.getLHS()) &&
					r3nf.getAttrbutes().containsAttSet(fd3nf.getRHS())) {
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
				if(isKey(fd3nf.getLHS(), r3nf.getAttrbutes(), fds3nf)) {
					r3nf.setKeys(fd3nf.getLHS());
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
	
	public static boolean isIn2NF(List<Relation> r, List<FD> minimalCoverFDs) {
		for (Iterator<Relation> iter = r.iterator(); iter.hasNext();) {
			Relation r2nf = iter.next();
			Log.info("Checking " + r2nf.toString());
			//if there are not FDs associated with this relationship, take the
			//minimal cover FDs, and find out which ones can be associated 
			//with this relationship
			if(r2nf.getFds() == null) {
				Log.info("No fds set, assosiating new relation fds");
				r2nf.assciateFDs(minimalCoverFDs);
			}
			//if no key is set find one. Note that in the extreme situation 
			//it could be the every element of the relationship
			if(r2nf.getKeys() == null) {
				Log.info("No key set, finding new Key");
				AttributeSet key = findKey(r2nf);
				r2nf.setKeys(key);
			}
			//see if the key is actually a key ( verify user input). 
			if(r2nf.getKeys().size() == 1) {//TODO better
				Log.info("Key size == 1");
				return true;
			} else {
				//to see if the relation ship is in 2 nf we hae to insure that 
				//every element that is not part of the key, aka not a key element,
				//is not depending on only just part of the key, e.g.
				//Relationship ABCD with FDs AB -> CD, B->C 
				//is not in 2NF since C depends also on B, which
				//is part of the key. In order to insure that is not the case ,
				//we iterate over every FD and compare it LHS (left hand side) with
				///the key, if the LHS is part of the key, then the relationship
				//is not in 2 nf
				AttributeSet key = r2nf.getKeys();
				for (FD fd2 : minimalCoverFDs) {
					if(!fd2.getLHS().equals(key) & 
						key.containsAttSet(fd2.getLHS())) {
						return true;
					}
				}
				
				
				//see every element depend only on the key 
				
				AttributeSet nonKeyAtt = r2nf.getAttrbutes().clone();
				nonKeyAtt.removeAttSet(key);
				for (AttributeSetIterator iter2 = nonKeyAtt.iterator(); iter2
						.hasNext();) {
					for (FD fd : r2nf.getFds()) {
						if(fd.getRHS().containsAtt(iter2.nextAttIndex()) &
								!fd.getLHS().equals(key)) {
							return false;
						}
					}
					
				}
			}
		}
		return true;
	}
	
	public static boolean isIn3NF(List<Relation> r, List<FD> minimalCoverFDs) {
		for (Iterator<Relation> iter = r.iterator(); iter.hasNext();) {
			Relation r2nf = iter.next();
			Log.info("Checking " + r2nf.toString());
			//if there are not FDs associated with this relationship, take the
			//minimal cover FDs, and find out which ones can be associated 
			//with this relationship
			if(r2nf.getFds() == null) {
				Log.info("No fds set, assosiating new relation fds");
				r2nf.assciateFDs(minimalCoverFDs);
			}
			//if no key is set find one. Note that in the extreme situation 
			//it could be the every element of the relationship 
			if(r2nf.getKeys() == null) {
				Log.info("No key set, finding new Key");
				AttributeSet key = findKey(r2nf);
				r2nf.setKeys(key);
			}
			System.out.println("Key " + r2nf.getKeys());
			//see if the key is actually a key ( verify user input). 
			if(r2nf.getKeys().size() == 1) {//TODO better
				Log.info("Key size == 1");
				return true;
			} else {
				//see every element depend only on the key, and there are no
				//transitive functional dependacies 
				AttributeSet key = r2nf.getKeys();
				AttributeSet nonKeyAtt = r2nf.getAttrbutes().clone();
				nonKeyAtt.removeAttSet(key);
				for (AttributeSetIterator iter2 = nonKeyAtt.iterator(); iter2
						.hasNext();) {
					for (FD fd : r2nf.getFds()) {
						System.out.println("!"+fd);
						if(fd.getRHS().containsAtt(iter2.nextAttIndex()) &
								!fd.getLHS().equals(key)) {
							return false;
						}
					}
					
				}
			}
		}
		return true;
	}
}
