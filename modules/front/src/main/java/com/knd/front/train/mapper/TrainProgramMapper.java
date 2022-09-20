package com.knd.front.train.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.ProgramEntity;
import com.knd.front.entity.ProgramWeekDetailEntity;
import com.knd.front.user.dto.TrainWeekDetailDto;
import com.knd.front.user.entity.UserCourseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zm
 */
public interface TrainProgramMapper extends BaseMapper<ProgramEntity> {

    @Select("select id,weekdayName from train_program_week_detail where programId = #{id}")
    List<ProgramWeekDetailEntity> getWeekList(@Param("id") String id);

    @Select("select a.id,a.course,a.remark,c.filePath as picAttachUrl,a.videoDurationMinutes, " +
            " a.amount,a.courseType,e.difficulty,a.trainingFlag  " +
            " from course_head a " +
            " LEFT JOIN attach c ON a.picAttachId = c.id and c.deleted=0 " +
            " LEFT JOIN train_program_day_item b on a.id = b.itemId and b.deleted = 0 " +
            " JOIN base_difficulty e on e.id = a.difficultyId and e.deleted=0 " +
            " where b.trainProgramWeekDetailId = #{id} and a.releaseFlag = 1")
    List<UserCourseEntity> getCourseList(@Param("id") String id);

    @Select("select  (case b.weekdayName  " +
            "      when 1 then '周一' " +
            "      when 2 then '周二' " +
            "      when 3 then '周三' " +
            "      when 4 then '周四' " +
            "      when 5 then '周五' " +
            "      when 6 then '周六' " +
            "      when 7 then '周日'  " +
            "     end) as weekDayName, b.weekdayName as weekdaynum, " +
            "  a.itemType,a.itemId," +
            " IF(a.itemType='0','课程','动作序列') as typeName, " +
            " IFNULL(d.course,e.actionArrayName) as itemName, " +
            " c.picAttachId as picAttach " +
            "from train_program_day_item a  " +
            "join train_program_week_detail b on a.trainProgramWeekDetailId = b.id " +
            "join train_program c on b.programId = c.id " +
            "left join course_head d on d.id = a.itemId and a.itemType='0' " +
            "left join action_array e on e.id = a.itemId and a.itemType='1' " +
            "where c.id=#{id} " +
            "order by b.weekdayName ")
    List<TrainWeekDetailDto> getDetail(@Param("id")String id);
}
