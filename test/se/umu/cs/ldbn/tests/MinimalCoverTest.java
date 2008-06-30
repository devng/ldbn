package se.umu.cs.ldbn.tests;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.core.Algorithms;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;
//TODO As junit
public class MinimalCoverTest {
	public static void main(String[] args) {
		
		ArrayList<FD> fds = new ArrayList<FD>();
		String[] a = {"A", "B", "C", "D", "E", "G"};
 		DomainTable dom = new DomainTable(a);
 		/*
 		F = { AB -> C, C -> A, BC -> D, ACD -> B, D -> E, D -> G, BE -> C, 
 			  CG -> B, CG -> D, CE -> A, CE -> G}
 		*/
 		
 		//AB -> C
		FD fd = new FD(dom);
		fd.getLHS().addAtt("A");
		fd.getLHS().addAtt("B");
		fd.getRHS().addAtt("C");
		fds.add(fd);
		
		//C -> A
		fd = new FD(dom);
		fd.getLHS().addAtt("C");
		fd.getRHS().addAtt("A");
		fds.add(fd);
		
		//BC -> D
		fd = new FD(dom);
		fd.getLHS().addAtt("B");
		fd.getLHS().addAtt("C");
		fd.getRHS().addAtt("D");
		fds.add(fd);
		
		//ACD -> B
		fd = new FD(dom);
		fd.getLHS().addAtt("A");
		fd.getLHS().addAtt("C");
		fd.getLHS().addAtt("D");
		fd.getRHS().addAtt("B");
		fds.add(fd);
		
		//D -> E
		fd = new FD(dom);
		fd.getLHS().addAtt("D");
		fd.getRHS().addAtt("E");
		fds.add(fd);
		
		//D -> G
		fd = new FD(dom);
		fd.getLHS().addAtt("D");
		fd.getRHS().addAtt("G");
		fds.add(fd);
		
		//BE -> C
		fd = new FD(dom);
		fd.getLHS().addAtt("B");
		fd.getLHS().addAtt("E");
		fd.getRHS().addAtt("C");
		fds.add(fd);
		
		//CG -> B
		fd = new FD(dom);
		fd.getLHS().addAtt("C");
		fd.getLHS().addAtt("G");
		fd.getRHS().addAtt("B");
		fds.add(fd);
		
		//CG -> D
		fd = new FD(dom);
		fd.getLHS().addAtt("C");
		fd.getLHS().addAtt("G");
		fd.getRHS().addAtt("D");
		fds.add(fd);
		
		//CE -> A
		fd = new FD(dom);
		fd.getLHS().addAtt("C");
		fd.getLHS().addAtt("E");
		fd.getRHS().addAtt("A");
		fds.add(fd);
		
		//CE -> G
		fd = new FD(dom);
		fd.getLHS().addAtt("C");
		fd.getLHS().addAtt("E");
		fd.getRHS().addAtt("G");
		fds.add(fd);
		
		Algorithms.minimalCover(fds);
		/* 
		Answer:
		AB -> C, C -> A, BC -> D, CD -> B, D -> EG, BE -> C,
		CG -> D, CE -> G}
		 */
		for (int i = 0; i < fds.size(); i++) {
			System.out.println(fds.get(i));
		}
		/*
		Yes it works :)
		 */
		AttributeSet allAtts = new AttributeSet(dom);
		for (int i = 0; i < a.length; i++) {
			allAtts.addAtt(a[i]);
		}
		List<AttributeSet> keys = Algorithms.findAllKeyCandidates(fds, allAtts);
		for (AttributeSet k : keys) {
			System.out.println("Key: " + k);
		}
	}
}
