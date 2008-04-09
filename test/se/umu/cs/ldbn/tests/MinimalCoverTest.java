package se.umu.cs.ldbn.tests;

import java.util.ArrayList;

import se.umu.cs.ldbn.client.core.*;
//TODO As junit
public class MinimalCoverTest {
	public static void main(String[] args) {
		ArrayList<FD> fds = new ArrayList<FD>();
		String[] a = {"A", "B", "C", "D", "E", "F", "G"};
 		AttributeNameTable dom = new AttributeNameTable(a);
 		/*
 		F = { AB -> C, C -> A, BC -> D, ACD -> B, D -> E, D -> G, BE -> C, 
 			  CG -> B, CG -> D, CE -> A, CE -> G}
 		*/
 		
 		//AB -> C
		FD fd = new FD(dom);
		fd.getLeftSide().addAtt("A");
		fd.getLeftSide().addAtt("B");
		fd.getRightSide().addAtt("C");
		fds.add(fd);
		
		//C -> A
		fd = new FD(dom);
		fd.getLeftSide().addAtt("C");
		fd.getRightSide().addAtt("A");
		fds.add(fd);
		
		//BC -> D
		fd = new FD(dom);
		fd.getLeftSide().addAtt("B");
		fd.getLeftSide().addAtt("C");
		fd.getRightSide().addAtt("D");
		fds.add(fd);
		
		//ACD -> B
		fd = new FD(dom);
		fd.getLeftSide().addAtt("A");
		fd.getLeftSide().addAtt("C");
		fd.getLeftSide().addAtt("D");
		fd.getRightSide().addAtt("B");
		fds.add(fd);
		
		//D -> E
		fd = new FD(dom);
		fd.getLeftSide().addAtt("D");
		fd.getRightSide().addAtt("E");
		fds.add(fd);
		
		//D -> G
		fd = new FD(dom);
		fd.getLeftSide().addAtt("D");
		fd.getRightSide().addAtt("G");
		fds.add(fd);
		
		//BE -> C
		fd = new FD(dom);
		fd.getLeftSide().addAtt("B");
		fd.getLeftSide().addAtt("E");
		fd.getRightSide().addAtt("C");
		fds.add(fd);
		
		//CG -> B
		fd = new FD(dom);
		fd.getLeftSide().addAtt("C");
		fd.getLeftSide().addAtt("G");
		fd.getRightSide().addAtt("B");
		fds.add(fd);
		
		//CG -> D
		fd = new FD(dom);
		fd.getLeftSide().addAtt("C");
		fd.getLeftSide().addAtt("G");
		fd.getRightSide().addAtt("D");
		fds.add(fd);
		
		//CE -> A
		fd = new FD(dom);
		fd.getLeftSide().addAtt("C");
		fd.getLeftSide().addAtt("E");
		fd.getRightSide().addAtt("A");
		fds.add(fd);
		
		//CE -> G
		fd = new FD(dom);
		fd.getLeftSide().addAtt("C");
		fd.getLeftSide().addAtt("E");
		fd.getRightSide().addAtt("G");
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
	}
}
