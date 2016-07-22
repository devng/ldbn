package se.umu.cs.ldbn.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.umu.cs.ldbn.server.dao.CommentDao;
import se.umu.cs.ldbn.shared.dto.CommentDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CommentsRestServiceImpl implements CommentsRestService {

    private static final Logger log = LoggerFactory.getLogger(CommentsRestServiceImpl.class);

    private final CommentDao commentDao;

    @Inject
    CommentsRestServiceImpl(final CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Nonnull
    @Override
    public List<CommentDto> getAssignmentComments(final Integer assignmentId) {
        log.debug("Get comments for assignment.");
        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException("Invalid assignment id.");
        }

        return commentDao.getAssignmentComments(assignmentId);
    }
}
