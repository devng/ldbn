package se.umu.cs.ldbn.server.dao;

import org.mybatis.guice.transactional.Transactional;
import se.umu.cs.ldbn.server.dao.mapper.AssignmentMapper;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Transactional
@Singleton
class AssignmentDaoImpl implements AssignmentDao {

    private final AssignmentMapper assignmentMapper;

    @Inject
    AssignmentDaoImpl(AssignmentMapper assignmentMapper) {
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    public AssignmentDto getAssignment(final Integer id) {
        if (id == null) {
            return null;
        }
        return assignmentMapper.selectAssignmentDtoById(id);
    }

    @Override
    public List<AssignmentDto> getAllAssignment(final boolean includeXml) {
        List<AssignmentDto> dbAssignments = assignmentMapper.selectAllAssignments();
        if (!includeXml) {
            dbAssignments.forEach(e -> e.setXml(null));
        }
        return dbAssignments;
    }

    @Override
    public AssignmentDto saveAssignment(AssignmentDto dto) {
        if (dto == null || dto.getName() == null || dto.getXml() == null || dto.getAuthor() == null || dto.getAuthor().getId() == null) {
            return null;
        }
        assignmentMapper.insertAssignment(dto);
        return dto; // The id field should be updated by MyBatis
    }

    @Override
    public AssignmentDto updateAssignment(AssignmentDto dto) {
        return null;
    }
}
