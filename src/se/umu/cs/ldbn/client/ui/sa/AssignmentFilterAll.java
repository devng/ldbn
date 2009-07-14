package se.umu.cs.ldbn.client.ui.sa;

import java.util.List;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;

public final class AssignmentFilterAll implements AssignmentFilter {

	public static final String NAME = "All Assignments";
	
	public String getName() {
		return NAME;
	}
	
	public List<AssignmentListEntry> apply(List<AssignmentListEntry> data) {
		return data;
	}
}
