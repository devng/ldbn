package se.umu.cs.ldbn.server.dao;

import se.umu.cs.ldbn.shared.dto.CommentDto;

import javax.annotation.Nonnull;
import java.util.List;

public interface CommentDao {

    @Nonnull
    List<CommentDto> getAssignmentComments(Integer assignmentId);
}
