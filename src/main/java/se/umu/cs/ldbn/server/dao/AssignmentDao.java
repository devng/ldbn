package se.umu.cs.ldbn.server.dao;

import java.util.List;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

public interface AssignmentDao {
	
	AssignmentDto getAssignmen(Integer id);

	List<AssignmentDto> getAllAssignmen();

}
