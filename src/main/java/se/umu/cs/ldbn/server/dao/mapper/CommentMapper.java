package se.umu.cs.ldbn.server.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import se.umu.cs.ldbn.shared.dto.CommentDto;
import se.umu.cs.ldbn.shared.dto.UserDto;

import java.util.List;

public interface CommentMapper {

    @Results(value = {
            @Result(column = "modified_on", property = "modifiedOn"),
            @Result(id = true, column = "user_id", property = "author", javaType = UserDto.class, one = @One(select = "se.umu.cs.ldbn.server.dao.mapper.UserMapper.selectUserById", fetchType = FetchType.EAGER)),
    })
    @Select("SELECT * FROM comment WHERE assignment_id = #{assignmentId}")
    List<CommentDto> selectAssignmentComments(@Param("assignmentId") int assignmentId);
}
