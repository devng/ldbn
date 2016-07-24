package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.List;

public interface AssignmentFilter {
		
	String getName();
	
	List<AssignmentDto> apply(List<AssignmentDto> data);
	
}
