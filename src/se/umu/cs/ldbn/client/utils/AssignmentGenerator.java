package se.umu.cs.ldbn.client.utils;

import java.util.ArrayList;

import se.umu.cs.ldbn.client.core.Assignment;
import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.FD;

public final class AssignmentGenerator {
	
	public static Assignment generateMinCoverTest() {
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
		Assignment result = new Assignment(dom, fds);
//		result.setLoadedFromDB(false);
		return result;
	}
	
	
	public static Assignment generate() {
		return generateMinCoverTest();
		/*
		String[] atts = {"A", "B", "C", "D", "E", "F", "G"};
		DomainTable dom = new DomainTable(atts);
		ArrayList<FD> fds = new ArrayList<FD>(5);
		{
			String[] lhs = {"A", "B"}; 
			String[] rhs = {"C", "D"};
			FD  fd = new FD(dom, lhs, rhs);
			fds.add(fd);
		}
		{
			String[] lhs = {"C", "D"}; 
			String[] rhs = {"E", "F"};
			FD  fd = new FD(dom, lhs, rhs);
			fds.add(fd);
		}
		{
			String[] lhs = {"E", "F"}; 
			String[] rhs = {"G"};
			FD  fd = new FD(dom, lhs, rhs);
			fds.add(fd);
		}
		
		return new Assignment(dom, fds);
		*/
	}
}
