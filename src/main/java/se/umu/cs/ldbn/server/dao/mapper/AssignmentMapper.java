package se.umu.cs.ldbn.server.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import se.umu.cs.ldbn.shared.dto.AssignmentDto;
import se.umu.cs.ldbn.shared.dto.UserDto;

import java.util.List;

public interface AssignmentMapper {

    @Results(value = {
            @Result(column = "modified_on", property = "modifiedOn"),
            @Result(id = true, column = "user_id", property = "author", javaType = UserDto.class, one = @One(select = "se.umu.cs.ldbn.server.dao.mapper.UserMapper.selectUserById", fetchType = FetchType.EAGER)),
    })
    @Select("SELECT * FROM assignment")
    List<AssignmentDto> selectAllAssignments();

    @Results(value = {
            @Result(column = "modified_on", property = "modifiedOn"),
            @Result(id = true, column = "user_id", property = "author", javaType = UserDto.class, one = @One(select = "se.umu.cs.ldbn.server.dao.mapper.UserMapper.selectUserById", fetchType = FetchType.EAGER)),
    })
    @Select("SELECT * FROM assignment WHERE id = #{assignmentId}")
    AssignmentDto selectAssignmentDtoById(@Param("assignmentId") int assignmentId);

    @Insert("INSERT INTO assignment(user_id, name, xml) VALUES (#{author.id}, #{name}, #{xml})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insertAssignment(AssignmentDto dto);

}

