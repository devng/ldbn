package se.umu.cs.ldbn.client.ui.sa;

import java.util.List;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;

public interface AssignmentFilter {
		
	String getName();
	
	List<AssignmentListEntry> apply(List<AssignmentListEntry> data);
	
}
