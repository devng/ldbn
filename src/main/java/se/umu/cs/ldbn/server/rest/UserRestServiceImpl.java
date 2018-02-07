package se.umu.cs.ldbn.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.umu.cs.ldbn.server.dao.UserDao;
import se.umu.cs.ldbn.shared.dto.UserDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Singleton
public class UserRestServiceImpl implements UserRestService {

    private static final Logger log = LoggerFactory.getLogger(UserRestServiceImpl.class);

    private final UserDao userDao;

    @Inject
    UserRestServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Nonnull
    @Override
    public List<UserDto> indexUsers(final boolean activeOnly) {
        log.debug("Index users.");
        if (activeOnly) {
            return userDao.getActiveUsers();
        } else {
            return userDao.getUsers();
        }
    }

    @Nonnull
    @Override
    public UserDto getUser(final Integer id) {
        log.debug("Get model by id.");
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Illegal model id.");
        }
        UserDto dto = userDao.getUser(id);
        if (dto == null) {
            throw new NotFoundException();
        }
        return dto;
    }
}
