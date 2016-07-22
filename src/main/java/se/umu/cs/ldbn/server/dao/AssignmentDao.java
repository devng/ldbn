package se.umu.cs.ldbn.server.dao;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.List;

public interface AssignmentDao {

    AssignmentDto getAssignment(Integer id);

    List<AssignmentDto> getAllAssignment(boolean includeXml);

    AssignmentDto saveAssignment(AssignmentDto dto);

    AssignmentDto updateAssignment(AssignmentDto dto);

}
