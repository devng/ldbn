package se.umu.cs.ldbn.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.umu.cs.ldbn.server.dao.AssignmentDao;
import se.umu.cs.ldbn.server.dao.UserDao;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Singleton
class AssignmentsRestServiceImpl implements AssignmentsRestService {

    private static final Logger log = LoggerFactory.getLogger(AssignmentsRestServiceImpl.class);

    private final AssignmentDao assignmentDao;

    private final UserDao userDao;

    @Inject
    AssignmentsRestServiceImpl(final AssignmentDao assignmentDao, final UserDao userDao) {
        this.assignmentDao = assignmentDao;
        this.userDao = userDao;
    }

    @Override
    public AssignmentDto getAssignment(final Integer id) {
        log.debug("Get assignment by id.");
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Illegal assignment id.");
        }
        AssignmentDto dto = assignmentDao.getAssignment(id);
        if (dto == null) {
            throw new NotFoundException();
        }
        return dto;
    }

    @Override
    public List<AssignmentDto> getAllAssignments(final boolean includeXml) {
        log.debug("Get all assignments.");
        List<AssignmentDto> dtos = assignmentDao.getAllAssignment(includeXml);
        if (dtos == null) {
            throw new NotFoundException();
        }
        return dtos;
    }

    @Nonnull
    @Override
    public Response createAssignment(AssignmentDto dto, final UriInfo uriInfo) {
        log.debug("Create a new assignment.");
        if (dto == null || dto.getName() == null || dto.getXml() == null || dto.getAuthor() == null || dto.getAuthor().getId() == null) {
            throw new IllegalArgumentException("Missing assignment attributes.");
        }

        // TODO get the userId from the login session

        dto = assignmentDao.saveAssignment(dto);

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.getId())).build();
        return Response.created(uri).build();
    }

    @Nonnull
    @Override
    public void updateAssignment(final AssignmentDto dto, final Integer id) {
        // TODO
    }
}
