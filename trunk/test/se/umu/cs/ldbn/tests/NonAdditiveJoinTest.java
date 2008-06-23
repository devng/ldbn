package se.umu.cs.ldbn.tests;

import java.util.ArrayList;

import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.core.Relation;

public class NonAdditiveJoinTest {
	public static void main(String[] args) {
		String[] att = {"Ssn", "Ename", "Pnr", "Pname", "Ploc", "Hours"};
		DomainTable dom = new DomainTable(att);
		AttributeSet atts = new AttributeSet(dom, att);
		ArrayList<FD> fds = new ArrayList<FD>();
		
		String[] lhs1 = {"Ssn"};
		String[] rhs1 = {"Ename"};
 		FD fd = new FD(dom, lhs1, rhs1);
 		fds.add(fd);
 		
 		String[] lhs2 = {"Pnr"};
		String[] rhs2 = {"Pname", "Ploc"};
 		fd = new FD(dom, lhs2, rhs2);
 		fds.add(fd);
 		
 		String[] lhs3 = {"Ssn", "Pnr"};
		String[] rhs3 = {"Hours"};
 		fd = new FD(dom, lhs3, rhs3);
 		fds.add(fd);
 		
 		ArrayList<Relation> r = new ArrayList<Relation>();
 		String[] att1 = {"Ssn", "Ename"};
 		AttributeSet attSet1 = new AttributeSet(dom, att1);
 		Relation r1 = new Relation(attSet1);
 		r.add(r1);
 		
 		String[] att2 = {"Pnr", "Pname", "Ploc"};
 		AttributeSet attSet2 = new AttributeSet(dom, att2);
 		Relation r2 = new Relation(attSet2);
 		r.add(r2);
 		
 		String[] att3 = {"Ssn", "Pnr", "Hours"};
 		AttributeSet attSet3 = new AttributeSet(dom, att3);
 		Relation r3 = new Relation(attSet3);
 		r.add(r3);
 		
 		boolean b = Algorithms.isLossless(fds, atts, r);
 		System.out.println(b);
 		
	}
}
