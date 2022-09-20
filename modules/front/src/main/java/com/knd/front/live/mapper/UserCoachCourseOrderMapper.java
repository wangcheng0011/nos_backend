package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.live.entity.UserCoachCourseOrderEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserCoachCourseOrderMapper extends BaseMapper<UserCoachCourseOrderEntity> {
    @Select("select coachTimeId from lb_user_coach_course_order where isOrder = '1' and deleted = 0 and orderUserId=#{userId}")
    List<String> selectIdList(String userId);
}
