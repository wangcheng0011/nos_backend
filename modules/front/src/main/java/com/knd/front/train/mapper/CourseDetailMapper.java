package com.knd.front.train.mapper;

import com.knd.front.train.dto.CourseDetailDto;
import com.knd.front.train.dto.CourseNodeDetailDto;
import com.knd.front.train.dto.CourseVideoProgressInfoDto;
import com.knd.front.train.dto.FreeTrainDetailDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liulongxiang
 * @interfaceName
 * @description
 * @date 19:57
 * @Version 1.0
 */
public interface CourseDetailMapper {
    CourseDetailDto getCourseDetail(@Param("courseId") String courseId, @Param("userId") String userId);
    List<CourseNodeDetailDto> getCourseNodeDetail(@Param("courseId") String courseId, @Param("userId") String userId);
    FreeTrainDetailDto getFreeTrainDetail(@Param("actionId") String actionId, @Param("userId") String userId);
    List<CourseNodeDetailDto> getCourseVideoProgressInfo(@Param("courseId") String courseId, @Param("userId") String userId);

}
