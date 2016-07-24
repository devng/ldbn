package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.List;

public final class AssignmentFilterAll implements AssignmentFilter {

	public static final String NAME = "All Assignments";
	
	public String getName() {
		return NAME;
	}
	
	public List<AssignmentDto> apply(List<AssignmentDto> data) {
		return data;
	}
}
