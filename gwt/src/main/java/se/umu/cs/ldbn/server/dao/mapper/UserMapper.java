package se.umu.cs.ldbn.server.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import se.umu.cs.ldbn.shared.dto.UserDto;

import java.util.List;

public interface UserMapper {

    @Results(value = {
            @Result(column = "user_id", property = "id"),
            @Result(column = "is_admin", property = "admin"),
            @Result(column = "is_su", property = "su"),
            @Result(column = "is_active", property = "active")
    })
    @Select("SELECT * FROM model WHERE user_id = #{userId}")
    UserDto selectUserById(@Param("userId") int userId);

    @Results(value = {
            @Result(column = "user_id", property = "id"),
            @Result(column = "is_admin", property = "admin"),
            @Result(column = "is_su", property = "su"),
            @Result(column = "is_active", property = "active")
    })
    @Select("SELECT * FROM model")
    List<UserDto> selecAllUsers();

    @Results(value = {
            @Result(column = "user_id", property = "id"),
            @Result(column = "is_admin", property = "admin"),
            @Result(column = "is_su", property = "su"),
            @Result(column = "is_active", property = "active")
    })
    @Select("SELECT * FROM model WHERE is_active = 1")
    List<UserDto> selecAllActiveUsers();
}
