package se.umu.cs.ldbn.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

public interface AssignmentMapper {
	
	@Select("SELECT * FROM assignment WHERE id = #{assignmentId}")
    AssignmentDto selectAssignmentById(@Param("assignmentId") Integer assignmentId);
	
	@Select("SELECT * FROM assignment ORDER BY assignment.id DESC")
    List<AssignmentDto> selecAllAssignments();

}
