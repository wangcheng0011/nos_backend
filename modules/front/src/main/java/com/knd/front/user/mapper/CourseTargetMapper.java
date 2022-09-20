package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.CourseTarget;
import com.knd.front.home.dto.BaseTargetDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseTargetMapper extends BaseMapper<CourseTarget> {

    @Select("select b.id,b.target as name from course_target a\n" +
            "join base_target b on a.targetId =b.id and b.deleted=0\n" +
            "where a.courseId = #{courseId} and a.deleted=0")
    List<BaseTargetDto> getCourseTarget(String courseId);
}
