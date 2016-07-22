package se.umu.cs.ldbn.server.dao;

import org.mybatis.guice.transactional.Transactional;
import se.umu.cs.ldbn.server.dao.mapper.CommentMapper;
import se.umu.cs.ldbn.shared.dto.CommentDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Transactional
@Singleton
public class CommentDaoImpl implements CommentDao {

    private final CommentMapper commentMapper;

    @Inject
    CommentDaoImpl(final CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public List<CommentDto> getAssignmentComments(Integer assignmentId) {
        if (assignmentId == null) {
            return Collections.emptyList();
        }
        return commentMapper.selectAssignmentComments(assignmentId);
    }
}
