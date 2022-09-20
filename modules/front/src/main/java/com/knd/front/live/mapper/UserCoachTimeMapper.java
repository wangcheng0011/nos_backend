package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.live.dto.LiveListDto;
import com.knd.front.live.entity.UserCoachTimeEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface UserCoachTimeMapper extends BaseMapper<UserCoachTimeEntity> {

    @Select("select a.id as timeId,a.date,a.beginTime,a.liveStatus,b.courseName,b.courseTime," +
            "b.consume,a.replayUrl,b.picAttachId as picAttachUrl,b.difficultyId as difficulty, " +
            "a.coachUserId,d.nickName as coachName,b.courseType  " +
            "from lb_user_coach_time a " +
            "JOIN lb_user_coach_course b on a.coachCourseId = b.id and b.deleted=0 " +
            "join lb_user_coach c on a.coachUserId = c.userId and c.deleted=0 " +
            "left join user d on c.userId = d.id and d.deleted=0 " +
            " ${ew.customSqlSegment} ")
    List<LiveListDto> getLiveList(Page<LiveListDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
