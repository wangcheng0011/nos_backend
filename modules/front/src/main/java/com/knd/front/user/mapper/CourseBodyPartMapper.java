package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.CourseBodyPart;
import com.knd.front.home.dto.BaseBodyPartDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseBodyPartMapper extends BaseMapper<CourseBodyPart> {

    @Select("select b.id,b.part as name from course_body_part a\n" +
            "join base_body_part b on a.partId =b.id and b.deleted=0\n" +
            "where a.courseId = #{courseId} and a.deleted=0")
    List<BaseBodyPartDto> getCoursePart(String courseId);
}
