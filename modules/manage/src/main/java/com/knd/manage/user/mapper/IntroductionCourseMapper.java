package com.knd.manage.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.knd.manage.user.entity.IntroductionCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.user.dto.IntroductionCourseDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-08
 */
public interface IntroductionCourseMapper extends BaseMapper<IntroductionCourse> {
    //查询注册会员课程列表
    IPage<IntroductionCourseDto> selectUserCoursePageBySome(IPage<IntroductionCourseDto> page,
                                                            @Param("nickName") String nickName, @Param("mobile") String mobile,
                                                            @Param("equipmentNo") String equipmentNo,
                                                            @Param("trainTimeBegin") Date trainTimeBegin,
                                                            @Param("trainTimeEnd") Date trainTimeEnd);
}
