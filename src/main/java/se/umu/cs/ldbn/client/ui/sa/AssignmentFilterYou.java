package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentFilterYou implements AssignmentFilter {

	public static final String name = "Assignments Submitted by You";

	public String getName() {
		return name;
	}

	public List<AssignmentDto> apply(List<AssignmentDto> data) {
		if (data == null) {
			return null;
		}
		UserData ud = UserData.get();
		if (!ud.isLoggedIn()) {
			throw new IllegalStateException("You have to login first.");
		}
		final Integer userId = ud.getId();
		List<AssignmentDto> result = new ArrayList<>(data.size());
		for (AssignmentDto a : data) {
			if (a.getAuthor() != null && userId.equals(a.getAuthor().getId())) {
				result.add(a);
			}
		}
		return result;
	}

}
