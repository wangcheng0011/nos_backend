package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.live.dto.CoachListEntityDto;
import com.knd.front.live.entity.UserCoachEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface UserCoachMapper extends BaseMapper<UserCoachEntity> {

    @Select("select distinct a.coachId " +
            " from lb_user_coach_course a " +
            " join lb_user_coach_time b on a.id = b.coachCourseId and b.deleted=0 " +
            " join lb_user_coach_course_order c on b.id = c.coachTimeId and c.deleted=0 and c.isOrder in (0,1) " +
            " join lb_user_coach d on b.coachUserId = d.userId and d.deleted=0 " +
            " where c.orderUserId = #{userId} and a.deleted=0 and a.courseType=1 ")
    List<String> getCoachIdByUserId(@Param("userId") String userId);


    @Select("select MIN(a.price) " +
            "from lb_user_coach_time a  " +
            "join lb_user_coach_course b on a.coachCourseId = b.id and b.deleted=0 " +
            "join lb_user_coach c on a.coachUserId = c.userId and c.deleted=0 " +
            "where a.beginTime>=#{time} and b.courseType=#{courseType} and a.coachUserId=#{coachUserId}")
    BigDecimal getMinPrice(@Param("time") LocalDateTime time, @Param("courseType") String courseType, @Param("coachUserId") String coachUserId);

    @Select("select a.userId as coachUserId,a.traineeNum,a.id as coachId,u.nickName as coachName,a.content,a.depict," +
            "MIN(d.price) as coursePrice,count(1) as num  " +
            "from lb_user_coach a " +
            "JOIN `user` u on a.userId = u.id and u.deleted=0 " +
            "LEFT JOIN user_detail b on a.userId = b.userId and b.deleted=0 " +
            "JOIN lb_user_coach_course c on a.id = c.coachId and c.deleted=0 and c.courseType='1' " +
            "JOIN lb_user_coach_time d on c.id = d.coachCourseId and d.deleted=0 " +
            " ${ew.customSqlSegment} ")
    List<CoachListEntityDto> getCoachListEntityDto(Page<CoachListEntityDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
