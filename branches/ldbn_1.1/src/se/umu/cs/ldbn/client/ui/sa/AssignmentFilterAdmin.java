package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;

public final class AssignmentFilterAdmin implements AssignmentFilter {

	public static final String name = "Assignments Submitted by Instructional Users";
	
	public String getName() {
		return name;
	}
	
	public List<AssignmentListEntry> apply(List<AssignmentListEntry> data) {
		ArrayList<AssignmentListEntry> result = 
			new ArrayList<AssignmentListEntry>(data.size());
		for (AssignmentListEntry ale : data) {
			if (ale.isAdmin()) {
				result.add(ale);
			}
		}
		return result;
	}
}
