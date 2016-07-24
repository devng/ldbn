package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AssignmentFilterAdmin implements AssignmentFilter {

	public static final String name = "Assignments Submitted by Instructional Users";

	public String getName() {
		return name;
	}

	public List<AssignmentDto> apply(List<AssignmentDto> data) {
		if (data == null) {
			return null;
		}
		List<AssignmentDto> result = new ArrayList<>(data.size());
		for (AssignmentDto a : data) {
			if (a.getAuthor() != null && a.getAuthor().isAdmin()) {
				result.add(a);
			}
		}
		return result;
	}
}
