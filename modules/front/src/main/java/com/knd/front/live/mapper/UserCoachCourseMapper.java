package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.live.dto.CoachCourseByUserListDto;
import com.knd.front.live.dto.CoachCourseOrderListDto;
import com.knd.front.live.dto.CoachCourseTimeDto;
import com.knd.front.live.entity.UserCoachCourseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserCoachCourseMapper extends BaseMapper<UserCoachCourseEntity> {

    @Select("select a.id as courseId,b.id as timeId,b.beginTime,b.endTime,a.courseType,a.courseName," +
            "b.liveStatus,b.replayUrl,a.picAttachId as picAttachUrl,a.difficultyId as difficulty," +
            "coach.userId as coachUserId,d.nickName as coachName,c.createDate as orderTime " +
            "from lb_user_coach coach " +
            "join lb_user_coach_course a on coach.id=a.coachId and a.deleted=0 " +
            "join lb_user_coach_time b on a.id = b.coachCourseId and b.deleted=0 " +
            "join lb_user_coach_course_order c on b.id = c.coachTimeId and c.deleted=0 " +
            "left join user d on coach.userId = d.id and d.deleted=0 " +
            "${ew.customSqlSegment}")
    List<CoachCourseByUserListDto> getCoachCourseByUserList(Page<CoachCourseByUserListDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select b.id as timeId,b.beginTime,b.endTime,a.courseType,a.courseName,b.price as coursePrice,b.liveStatus,ifNull(c.isOrder,'') as isOrder," +
            "a.courseTime,a.consume,coach.userId as coachUserId,a.picAttachId as picAttachUrl,a.difficultyId as difficulty " +
            "from lb_user_coach coach " +
            "left join lb_user_coach_course a on coach.id=a.coachId and a.deleted=0 " +
            "left join lb_user_coach_time b on a.id = b.coachCourseId and b.deleted=0 " +
            "left join lb_user_coach_course_order c on b.id = c.coachTimeId and c.deleted=0 and c.orderUserId=#{userId} " +
            "${ew.customSqlSegment}")
    List<CoachCourseOrderListDto> getCoachCourseOrderList(@Param(Constants.WRAPPER) Wrapper wrapper,String userId);



    //根据状态分页查询教练课程
    Page<CoachCourseTimeDto> selectCoachCoursePageByStatus(@Param("page") Page<CoachCourseTimeDto> page,@Param("userId")String userId,@Param("queryType")String queryType);
}
