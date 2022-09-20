package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.live.entity.UserCoachAttachEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserCoachAttachMapper extends BaseMapper<UserCoachAttachEntity> {
    @Select("select a.* from lb_user_coach_attach a left join lb_user_coach_course_order o on a.coachUserId = o.orderUserId where o.coachUserId = #{coachUserId} and a.deleted = '0';")
    List<UserCoachAttachEntity> selectCoachAttachList(String coachUserId);
}
