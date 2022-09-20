package com.knd.front.home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.CourseType;
import com.knd.front.home.dto.BaseCourseTypeDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
public interface CourseTypeMapper extends BaseMapper<CourseType> {

    @Select("select b.id,b.type as name from course_type a\n" +
            "join base_course_type b on a.courseTypeId =b.id and b.deleted=0\n" +
            "where a.courseId = #{courseId} and a.deleted=0")
    List<BaseCourseTypeDto> getCourseType(String courseId);

}
