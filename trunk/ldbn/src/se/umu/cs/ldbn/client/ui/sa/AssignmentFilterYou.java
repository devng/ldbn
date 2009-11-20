package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;
import se.umu.cs.ldbn.client.ui.user.UserData;

public class AssignmentFilterYou implements AssignmentFilter {

	public static final String name = "Assignments Submitted by You";
	
	public String getName() {
		return name;
	}
	
	public List<AssignmentListEntry> apply(List<AssignmentListEntry> data) {
		UserData ud = UserData.get();
		if(!ud.isLoggedIn()) {
			throw new IllegalStateException("You have to login first.");
		}
		String userId = ud.getId().trim();
		ArrayList<AssignmentListEntry> result = 
			new ArrayList<AssignmentListEntry>(data.size());
		for (AssignmentListEntry ale : data) {
			String authorId = ale.getAuthorID();
			if (authorId != null && userId.equals(authorId.trim())) {
				result.add(ale);
			}
		}
		return result;
	}

}
