package se.umu.cs.ldbn.client.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Relation {
	
	private AttributeSet attrbutes;
	private AttributeSet keys;
	private List<FD> fds;
	
	public Relation(AttributeSet attributes) {
		this.attrbutes = attributes;
	}
	//TODO This is wrong
	public void assciateFDs(List<FD> minmalCoverFds) {
		fds = new ArrayList<FD>();
		//First chop the RHS in to bits, and create new FDs with only one el.
		//in the RHS
		List<FD> minimalCoverFDsnotRightReduced = new ArrayList<FD>(); 
		for (FD fd2 : minmalCoverFds) {
			for (AttributeSetIterator iter = fd2.getRHS().iterator(); iter
					.hasNext();) {
				FD tmp = new FD(fd2.getLHS().domain());
				tmp.getLHS().union(fd2.getLHS());
				tmp.getRHS().addAtt(iter.nextAttIndex());
				minimalCoverFDsnotRightReduced.add(tmp);
			}
		}
		//see which fds can be associated with this relationship attributes
		for (FD fd : minimalCoverFDsnotRightReduced) {
			if(attrbutes.containsAttSet(fd.getLHS()) &&
				attrbutes.containsAttSet(fd.getRHS())) {
				fds.add(fd.clone()); //TODO is cloning neccesary 
			}
		}
		//use minimal cover algorithm on the associated fds
		Algorithms.minimalCover(fds);
	}


	public AttributeSet getAttrbutes() {
		return attrbutes;
	}


	public AttributeSet getKeys() {
		return keys;
	}


	public void setKeys(AttributeSet keys) {
		this.keys = keys;
	}


	public List<FD> getFds() {
		return fds;
	}
	
	public String toString() {
		return "Relation : " + attrbutes.toString();
	}
}
