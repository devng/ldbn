package se.umu.cs.ldbn.server.dao;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

@Transactional
class AssignmentDaoImpl implements AssignmentDao {
	
	private final AssignmentMapper assignmentMapper;
	
	@Inject
	AssignmentDaoImpl(AssignmentMapper assignmentMapper) {
		super();
		this.assignmentMapper = assignmentMapper;
	}

	@Override
	public AssignmentDto getAssignmen(Integer id) {
		return assignmentMapper.selectAssignmentById(id);
	}
	
	@Override
	public List<AssignmentDto> getAllAssignmen() {
		return assignmentMapper.selecAllAssignments();
	}

}
