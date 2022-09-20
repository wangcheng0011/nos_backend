package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.user.dto.CourseTypeNumDto;
import com.knd.front.user.dto.TrainProgramDto;
import com.knd.front.user.dto.UserDetailDto;
import com.knd.front.user.entity.PowerTestEntity;
import com.knd.front.user.entity.SeriesCourseEntity;
import com.knd.front.user.entity.UserCourseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 *
 * @author will
 * @date 2021/8/10 21:40
 */
public interface PowerTestMapper extends BaseMapper<PowerTestEntity> {

    @Select("select targetId,shapeId,hobbyId from user_detail where userId = #{userId}")
    UserDetailDto getUserDetail(@Param("userId") String userId);
}
