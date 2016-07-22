package se.umu.cs.ldbn.server.dao;

import org.mybatis.guice.transactional.Transactional;
import se.umu.cs.ldbn.server.dao.mapper.UserMapper;
import se.umu.cs.ldbn.shared.dto.UserDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Transactional
@Singleton
public class UserDaoImpl implements UserDao {

    private final UserMapper userMapper;

    @Inject
    UserDaoImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDto getUser(Integer id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectUserById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return userMapper.selecAllUsers();
    }

    @Override
    public List<UserDto> getActiveUsers() {
        return userMapper.selecAllActiveUsers();
    }
}
