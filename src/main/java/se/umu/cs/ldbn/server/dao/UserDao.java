package se.umu.cs.ldbn.server.dao;

import se.umu.cs.ldbn.shared.dto.UserDto;

import java.util.List;

public interface UserDao {

    UserDto getUser(Integer id);

    List<UserDto> getUsers();

    List<UserDto> getActiveUsers();
}
