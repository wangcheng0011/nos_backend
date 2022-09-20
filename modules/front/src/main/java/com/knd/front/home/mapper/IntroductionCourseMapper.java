package com.knd.front.home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.IntroductionCourse;
import com.knd.front.home.request.FinishWatchCourseVideoRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
public interface IntroductionCourseMapper extends BaseMapper<IntroductionCourse> {
    int updateByUserIdAndCourseId(@Param("finishWatchCourseVideoRequest") FinishWatchCourseVideoRequest finishWatchCourseVideoRequest,
                                  @Param("nowStr") String nowStr);
}
