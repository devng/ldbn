package se.umu.cs.ldbn.shared.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class with static methods of different algorithms.
 *
 * For the detailed description of the algorithms see the following book: Alfons
 * Kemper, Andre Eickler: Datenbanksysteme, 6. Auflage. Oldenbourg Verlag ISBN
 * 3-486-5690-9
 */
public final class Algorithms {

	/**
	 * Calculates the attribute closure (Attribute Huelle - German, see p. 172).
	 *
	 * @param a
	 *            Set of attributes.
	 * @param fds
	 *            Set of FDs
	 * @return Set of attributes such that a -> a+
	 */
	public static AttributeSet attributeClosure(AttributeSet a, List<FD> fds) {
		AttributeSet r = a.clone();
		int lastAtts;
		do {
			lastAtts = r.attMask();
			for (FD fd : fds) {
				if (fd.getLHS().isSubSetOf(r)) {
					r.union(fd.getRHS());
				}
			}
		} while (lastAtts != r.attMask());
		return r;
	}

	/**
	 * Find the minimal cover ("kanonishe Ueberdeckung" German, see p. 173) of
	 * an given set of FDs. The algorithm first makes left reduction , then
	 * right reduction , then deleting all FDs such as A -> {}, group all FD
	 * such as A -> B and A -> C to A -> BC
	 *
	 * @param f
	 *            all given FDs of the relation
	 */
	public static void minimalCover(List<FD> f) {
		if(f == null ) {
			throw new IllegalArgumentException("F cannot be null");
		}
		List<FD> oldF;
		int max_iterations = 10;
		int i = 0;
		do {
			oldF = cloneF(f);
			i++;
			//reduction
			leftReduction(f);
			rightReduction(f);

			// clean FD such as A -> {}
			for (Iterator<FD> iter = f.iterator(); iter.hasNext();) {
				FD fd = iter.next();
				if (fd.getRHS().attMask() == 0) {
					iter.remove();
				}
			}

			// group FD in such way that A -> B , A -> C equals to A -> BC
			HashMap<AttributeSet, AttributeSet> table = new HashMap<>();
			for (FD fd : f) {
				if (table.containsKey(fd.getLHS())) {
					AttributeSet rhs = table.get(fd.getLHS());
					rhs.union(fd.getRHS());
				} else {
					table.put(fd.getLHS(), fd.getRHS());
				}
			}
			f.clear();
			Set<AttributeSet> lhsAtts = table.keySet();
			for (AttributeSet lhs : lhsAtts) {
				f.add(new FD(lhs, table.get(lhs)));
			}


		} while (i < max_iterations && isFChanged(f, oldF));

		oldF = null;

	}

	private static List<FD> cloneF(List<FD> f) {
		ArrayList<FD> result = new ArrayList<>(f.size());
		for (FD fd : f) {
			result.add(fd.clone());
		}
		return result;
	}

	private static boolean isFChanged(List<FD> newF, List<FD> oldF) {
		if(newF.size() != oldF.size()) {
			return true;
		}
		boolean result = !newF.containsAll(oldF);
		return result;
	}

	private static void leftReduction(List<FD> fds) {
		HashSet<FD> newFDs = new HashSet<>();
		HashSet<FD> toRemoveFDs = new HashSet<>();

		for (int i = 0; i < fds.size(); i++) {
			FD fd = fds.get(i);

			AttributeSet leftSide = fd.getLHS().clone();
			AttributeSetIterator iter = leftSide.iterator();
			for (; iter.hasNext();) {
				int j = iter.nextAttIndex();
				iter.remove();
				AttributeSet b = attributeClosure(leftSide, fds);
				if (fd.getRHS().isSubSetOf(b)) {
					FD tmp = fd.clone();
					tmp.getLHS().removeAtt(j);
					newFDs.add(tmp);
					toRemoveFDs.add(fd);
				}
				leftSide.addAtt(j);
			}
		}
		for (FD fd : toRemoveFDs) {
			fds.remove(fd);
		}

		for (FD fd : newFDs) {
			if (!fds.contains(fd)) {
				fds.add(fd);
			}
		}
	}

	private static void rightReduction(List<FD> fds) {
		for (int i = 0; i < fds.size(); i++) {
			FD fd = fds.get(i);
			AttributeSet rightSide = fd.getRHS();
			AttributeSetIterator iter = rightSide.iterator();
			for (; iter.hasNext();) {
				int j = iter.nextAttIndex();
				iter.remove();
				AttributeSet b = attributeClosure(fd.getLHS(), fds);
				if (!b.containsAtt(j)) {
					rightSide.addAtt(j);
				}
			}
		}
	}

	/**
	 * Convert FDs in such a form, so that all fds in the RHS (right hand sinde)
	 * have only one attribute
	 *
	 * @param fds
	 *            list of FDs
	 * @return the canonical form of the list
	 */
	public static List<FD> canonicalForm(List<FD> fds) {
		ArrayList<FD> result = new ArrayList<>();
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
	 * Consider two sets of FDs, F and G, F = {A -> B, B -> C, AC -> D} and G =
	 * {A -> B, B -> C, A -> D} Are F and G equivalent? To determine their
	 * equivalence, we need to prove that F+ = G+. However, since computing F+
	 * or G+ is computationally expensive we take a short cut. We can conclude
	 * that F and G are equivalent, if we can prove that all FDs in F can be
	 * inferred from the set of FDs in G and vice versa.
	 *
	 * For our example above, let us see if we can infer all FDs in F using G;
	 * we will use attribute closure to determine that. Take the attributes from
	 * the LHS of FDs in F and compute attribute closure for each using FDs in
	 * G: A+ using G = ABCD; A -> A; A -> B; A -> C; A -> D; B+ using G = BC; B ->
	 * B; B -> C; AC+ using G = ABCD; AC -> A; AC -> B; AC -> C; AC -> D; Notice
	 * that all FDs in F can be inferred using FDs in G. To see if all FDs in G
	 * are inferred by F, compute attribute closure for attributes on the LHS of
	 * FDs in G using FDs in F: A+ using F = ABCD; A -> A; A -> B; A -> C; A->
	 * D; B+ using F = BC; B -> B; B -> C; Since all FDs in F can be obtained
	 * from G and vice versa, we conclude that F and G are equivalent
	 *
	 * @param fdsF
	 *            List of FDs F
	 * @param fdsG
	 *            List of FDs G
	 * @return true if F and G are equivalent
	 */
	public static boolean equivalence(List<FD> fdsF, List<FD> fdsG) {
		for (FD fd : fdsF) {
			if (!fdTest(fd, fdsG))
				return false;
		}

		for (FD fd : fdsG) {
			if (!fdTest(fd, fdsF))
				return false;
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
	public static boolean fdTest(FD memberCandidate, List<FD> fds) {
		AttributeSet lhsClosure = attributeClosure(memberCandidate.getLHS(),
				fds);
		return lhsClosure.containsAttSet(memberCandidate.getRHS());
	}

	/**
	 * Gives all key candidates of a relationship, thus the keys are minimal, if
	 * the FDs List is null or empty, a list with only one element is returned
	 * containing <code>att</code>.
	 *
	 * @param fds
	 * @param atts
	 * @return
	 */
	public static List<AttributeSet> findAllKeyCandidates(List<FD> fds,
			AttributeSet atts) {
		List<AttributeSet> keys = new ArrayList<>();
		if (fds == null || fds.size() == 0) {
			keys.add(atts);
			return keys;
		}
		int mask = atts.attMask();

		for (int l = 0; l <= mask; l++) {
			int keyMask = mask & l;
			if (keyMask != 0) {
				AttributeSet key = new AttributeSet(atts.domain());
				key.setMask(keyMask);

				if (isSuperKey(key, atts, fds)) {
					boolean addKey = true;
					for (Iterator<AttributeSet> i = keys.iterator(); i
							.hasNext();) {
						AttributeSet k = i.next();
						if (key.containsAttSet(k)) { // E.g. ABC contains AB, thus don't add ABC
							addKey = false;
						} else if (k.containsAttSet(key)) { // E.g. AB from the
															// key list contains
															// the new key A,
															// thus remove AB
							i.remove();
						}
					}
					if (addKey) {
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
	 *
	 * @param key
	 * @param atts
	 * @param fds
	 * @return
	 */
	public static boolean isSuperKey(AttributeSet key, AttributeSet atts,
			List<FD> fds) {
		AttributeSet b = attributeClosure(key, fds);
		return b.equals(atts);
	}

	/**
	 * Check if a decomposition (a list of relationships) is dependency
	 * preserving. Dependency preservation ensures that no FDs are lost during a
	 * decomposition. By definition a decomposition is dependency preserving if
	 * the union of all FDs of all dependencies in the decomposition is within
	 * the FD closure of the initial FDs. Since computing the FD closure is
	 * quite an expensive operation, a different approach is taken. Since we
	 * know all the initial FDs, as they are part of the assignment, we can use
	 * a bottom up approach and test if all of the initial FDs are part of the
	 * relation FDs' closure. Thus we build a collection of all FDs, contained
	 * in the decomposition, and use the member algorithm to test if the initial
	 * FDs are members of its closure.
	 *
	 * @param initialFDs
	 *            the FDs which were originally given in the assignment.
	 * @param decomposition
	 *            a List of all relationships in a decomposition.
	 * @return true if the decomposition is dependency preserving.
	 */
	public static boolean isDependencyPreserving(List<FD> initialFDs,
			List<Relation> decomposition) {
		List<FD> decompositionFDs = new ArrayList<>();
		for (Relation relation : decomposition) {
			List<FD> rFDs = relation.getFds();
			if (rFDs != null) {
				decompositionFDs.addAll(rFDs);
			}
		}

		return equivalence(initialFDs, decompositionFDs);
	}

	/**
	 * See the paper of Georg Gottlob "Computing covers for embedded functional
	 * dependencies"
	 *
	 * @param u
	 *            Original scheme U
	 * @param f
	 *            set of FDs on U
	 * @param r
	 *            subschema R of U
	 * @return a cover for F+[R]
	 */
	public static List<FD> reductionByResolution(AttributeSet u, List<FD> f,
			AttributeSet r) {
		List<FD> canonicalForm = canonicalForm(f);
		ArrayList<FD> g = new ArrayList<>(canonicalForm); // G = F
		AttributeSet x = u.clone(); // X = U - R
		x.removeAttSet(r);

		AttributeSetIterator iter = x.iterator();
		for (; iter.hasNext();) {
			String a = iter.next();
			iter.remove();
			ArrayList<FD> res = new ArrayList<>();
			for (FD fd : g) {
				if (fd.getRHS().containsAtt(a)) {
					for (FD fd2 : g) {
						if (fd2.getLHS().containsAtt(a)) {
							FD h = fd2.clone();
							h.getLHS().removeAtt(a);
							h.getLHS().union(fd.getLHS());
							if (!isTrivial(h)) {
								res.add(h);
							}
						}
					}
				}
			}
			for (Iterator<FD> i = g.iterator(); i.hasNext();) {
				FD fd3 = i.next();
				if (fd3.getLHS().containsAtt(a) || fd3.getRHS().containsAtt(a)) {
					i.remove();
				}
			}
			g.addAll(res);
		}
		return g;
	}

	/**
	 * checks if elements in the LHS are present in the RHS
	 *
	 * @param f
	 *            an FD
	 * @return true if an element of the LHS is present in the RHS as well.
	 */
	private static boolean isTrivial(FD f) {
		return (f.getLHS().attMask() & f.getRHS().attMask()) != 0;
	}

	/**
	 * Checks if a decomposition has the lossless/nonaditive join property. The
	 * algorithm is the one proposed in Elmasri's Book.
	 *
	 * @param initialFDs
	 * @param atts
	 * @param decomposition
	 * @return
	 */
	public static boolean isLossless(List<FD> initialFDs, AttributeSet atts, List<Relation> decomposition) {
		AttributeSet[] s = new AttributeSet[decomposition.size()];
		int[] sMask = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			s[i] = decomposition.get(i).getAttributes().clone();
			sMask[i] = decomposition.get(i).getAttributes().attMask();
		}
		List<FD> fdCan = canonicalForm(initialFDs);
		do {
			for (FD fd : fdCan) {
				boolean containsLHSRHS =  false;
				for (AttributeSet value : s) {
					if (value.containsAttSet(fd.getLHS()) && value.containsAttSet(fd.getRHS())) {
						containsLHSRHS = true;
						break;
					}
				}
				setSymbol(s, fd.getLHS(), fd.getRHS(), containsLHSRHS);
			}
		} while (hasSChanged(sMask, s));

		for (AttributeSet value : s) {
			if (value.containsAttSet(atts)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasSChanged(int[] sMask, AttributeSet[] s) {
		boolean result = false;
		for (int i = 0; i < s.length; i++) {
			if (sMask[i] != s[i].attMask()) {
				result = true;
				sMask[i] = s[i].attMask();
			}
		}
		return result;
	}

	private static void setSymbol(AttributeSet[] s, AttributeSet x, AttributeSet y, boolean isASymbol) {
		for (AttributeSet sA : s) {
			if(sA.containsAttSet(x)) {
				if(isASymbol) {
					sA.union(y);
				} else {
					sA.removeAttSet(y);
				}
			}
		}
	}

	public static Collection<Relation> synthese(Relation r, boolean areFDMinimalCoder) {
		HashSet<Relation> result = new HashSet<>();
		List<FD> fds = r.getFds();
		if (!areFDMinimalCoder) {
			minimalCover(fds);
		}
		List<AttributeSet> allKeys = findAllKeyCandidates(fds, r.getAttributes());

		//2. create new relations
		for (FD fd : fds) {
			List<FD> fds3nf = new ArrayList<>();
			fds3nf.add(fd);
			AttributeSet att3nf = fd.getLHS().clone();
			att3nf.union(fd.getRHS());
			Relation r3nf = new Relation(att3nf);
			r3nf.setFDs(fds3nf);
			result.add(r3nf);
		}

		// Associate FDs for each relation , see Kemper's book p. 185
		for (Relation r1 : result) {
			List<FD> r1_fds = reductionByResolution(r.getAttributes(), r.getFds(), r1.getAttributes());
			minimalCover(r1_fds);
			r1.setFDs(r1_fds);
		}


		if (allKeys.size() > 0) {
			boolean containsKey = false;
			for (Relation rr : result) {
				for (AttributeSet k : allKeys) {
					System.out.println(k);
					if (rr.getAttributes().containsAttSet(k)) {
						containsKey = true;
						break;
					}
				}
			}

			if (!containsKey) {
				Relation keyRelation = new Relation(allKeys.get(0));
				result.add(keyRelation);
				keyRelation.setFDs(new ArrayList<>()); // should not be null;
				ArrayList<AttributeSet> keys = new ArrayList<>();
				keys.add(allKeys.get(0));
				keyRelation.setKeyCandidates(keys);
				keyRelation.setPrimaryKey(allKeys.get(0));

			}
		} else {
			System.out.println("Could not find key for the relation");
		}

		// find a key for each relation
		// TODO is this right, because it finds a key and not a key candidate??
		for (Relation r3nf : result) {
			List<FD> fds3nf = r3nf.getFds();
			for (FD fd3nf : fds3nf) {
				if (isSuperKey(fd3nf.getLHS(), r3nf.getAttributes(), fds3nf)) {
					r3nf.setPrimaryKey(fd3nf.getLHS());
				}
			}
			if (r3nf.getPrimaryKey() == null) {
				r3nf.setPrimaryKey(r3nf.getAttributes());
			}
		}

		// eliminate relationships such as Ra is subset of Rb
		List<Relation> toRemove = new ArrayList<>();
		for (Relation r1 : result) {
			for (Relation r2 : result) {
				if (r1 == r2)
					continue;
				if (r1.getAttributes().containsAttSet(r2.getAttributes())) {
					toRemove.add(r2);
				}
			}
		}
		result.removeAll(toRemove);
		return result;
	}

	/**
	 * Finds a BCNF decomposition if there is one. The input is the output of the
	 * syntese algorithm.
	 *
	 * Algorithm is described in the book [1]. page 187
	 */
	public static Collection<Relation> findBCNF(Collection<Relation> synthese) {
		List<FD> nonBCNF = null;
		Relation curRel = null;
		for (Relation r : synthese) {
			List<FD> tmp = isBCNF(r);
			if (tmp != null) {
				nonBCNF = tmp;
				curRel = r;
				break;
			}
		}

		if (nonBCNF != null && curRel != null && nonBCNF.size() != 0) {
			FD fd = nonBCNF.get(0);
			AttributeSet lhs = fd.getLHS();
			AttributeSet rhs = fd.getRHS();
			AttributeSet attU = lhs.clone();
			attU.union(rhs);
			AttributeSet attN = curRel.getAttributes().clone();
			attN.removeAttSet(rhs);
			Relation r1 = new Relation(attU);
			Relation r2 = new Relation(attN);
			// associate FDs with r1 and r2;
			List<FD> fds = reductionByResolution(curRel.getAttributes(), curRel
					.getFds(), r1.getAttributes());
			r1.setFDs(fds);
			fds = reductionByResolution(curRel.getAttributes(), curRel.getFds(),
					r2.getAttributes());
			r2.setFDs(fds);
			// find keys for r1 and r2
			List<AttributeSet> keys = findAllKeyCandidates(r1.getFds(), r1
					.getAttributes());
			r1.setKeyCandidates(keys);
			r1.setPrimaryKey(keys.get(0));
			keys = findAllKeyCandidates(r2.getFds(), r2.getAttributes());
			r2.setKeyCandidates(keys);
			r2.setPrimaryKey(keys.get(0));

			synthese.remove(curRel);
			synthese.add(r1);
			synthese.add(r2);
			findBCNF(synthese);
		}

		return synthese;
	}

	/**
	 * Check if a Relation r is in BCNF, and returns a list of FDs, which
	 * violate the BCNF rule, or null if the relation is in deed in BCNF
	 *
	 * @param r
	 *            a Relation, returned from the syntheses algorithm.
	 * @return null if the Relation r is in BCNF or a list of FD, which violate
	 *         the BCNF rule.
	 */
	private static List<FD> isBCNF(Relation r) {
		if (r.getFds() == null || r.getFds().size() <= 1) {
			return null;
		}
		if (r.getKeyCandidates() == null) {
			List<AttributeSet> keys = findAllKeyCandidates(r.getFds(), r
					.getAttributes());
			r.setKeyCandidates(keys);
		}
		List<FD> fds = r.getFds();
		List<AttributeSet> keys = r.getKeyCandidates();
		List<FD> nonBCNF = new ArrayList<>();
		for (FD fd : fds) {
			AttributeSet lhs = fd.getLHS();
			AttributeSet rhs = fd.getRHS();
			if (lhs.containsAttSet(rhs))
				continue; // FD trivial
			boolean isKey = false; // if LHS of FD is a super key
			for (AttributeSet key : keys) {
				if (lhs.containsAttSet(key)) {
					isKey = true;
					break;
				}
			}
			if (isKey) {
				continue;
			}
			nonBCNF.add(fd);
		}
		if(nonBCNF.isEmpty()) return null;
		return nonBCNF;
	}


	/**
	 * Checks if a decomposition is in 2nd normal form (2NF).
	 * @param rel list of relations with computed key candidates and FDs
	 * computed with reduce by resolution algorithm.
	 * @param domain the current domain table, usually taken from the SolveAssignmentWidget#getDomainTable
	 * @return true if every relation is in 2NF.
	 */
	public static boolean isIn2NF(List<Relation> rel, DomainTable domain) {
        //to see if the relation ship is in 2 nf we hae to insure that
        //every element that is not part of the key, aka not a key element,
        //is not depending on only just part of the key, e.g.
        //Relationship ABCD with FDs AB -> CD, B->C
        //is not in 2NF since C depends also on B, which
        //is part of the key. In order to insure that is not the case ,
        //we iterate over every FD and compare it LHS (left hand side) with
        //the key, if the LHS is part of the key, then the relationship
        //is not in 2 nf

		for (Relation r : rel) {
			List<AttributeSet> keys = r.getKeyCandidates();
			AttributeSet keyAttributes = new AttributeSet(domain);
			for (AttributeSet k : keys) {
				keyAttributes.union(k);
			}
			if(keyAttributes.equals(r.getAttributes())) {
				return true; // all attributes are key attributes
			}
			for (AttributeSet k : keys) {
				for (int i = 0; i <= k.attMask(); i++) {
					int tmpAtt = i & k.attMask();
					if((tmpAtt != k.attMask()) && (tmpAtt != 0)) {
						AttributeSet tmp = new AttributeSet(domain);
						tmp.setMask(tmpAtt);
						if(keys.contains(tmp)) continue;

						AttributeSet tmp2 = attributeClosure(tmp, r.getFds());
						if(tmp2.attMask() != 0 && tmp.attMask() != tmp2.attMask() && !keyAttributes.containsAttSet(tmp2)) {
							return false;
						}
					}
				}
			}

		}

		return true;
	}


	/**
	 * Checks if a decomposition is in 3nd normal form (3NF).
	 * @param rel list of relations with computed key candidates and FDs
	 * computed with reduce by resolution algorithm.
	 * @param domain the current domain table, usually taken from the SolveAssignmentWidget#getDomainTable
	 * @return true if every relation is in 3NF.
	 */
	public static boolean isIn3NF(List<Relation> rel, DomainTable domain) {
		for (Relation r : rel) {
			List<AttributeSet> keys = r.getKeyCandidates();
			AttributeSet keyAttributes = new AttributeSet(domain);
			for (AttributeSet k : keys) {
				keyAttributes.union(k);
			}
			List<FD> fds = r.getFds();
			for (FD fd : fds) {
				if (fd.getRHS().isSubSetOf(fd.getLHS())) {
					continue;
				} else if (fd.getRHS().isSubSetOf(keyAttributes)) {
					continue;
				} else {
					boolean isKey = false;
					for (AttributeSet k : keys) {
						if(fd.getLHS().equals(k)) {
							isKey = true;
							break;
						}
					}
					if(isKey) {
						continue;
					}
				}
				return false;
			}
		}
		return true;
	}

	public static boolean isInBCNF(List<Relation> rel) {
		for (Relation r : rel) {
			List<FD> tmp = isBCNF(r);
			if(tmp == null || tmp.size() == 0) {
				continue;
			} else {
				System.out.println(r.getName());
				for (FD fd : tmp) {
					System.out.println(fd.toString());
				}
				System.out.println("--------------------------");
			}
			return false;
		}
		return true;
	}
}
