package com.knd.manage.course.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.course.dto.TrainWeekDetailDto;
import com.knd.manage.course.entity.TrainProgramDetailEntity;
import com.knd.manage.course.entity.TrainProgramEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author zm
 */
public interface TrainProgramMapper extends BaseMapper<TrainProgramEntity> {

    @Select(" select id,programName,userId,beginTime,trainWeekNum,type  " +
            " from train_program " +
            " ${ew.customSqlSegment} ")
    List<TrainProgramEntity> getList(Page<TrainProgramEntity> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select  (case b.weekdayName  " +
            "      when 1 then '周一' " +
            "      when 2 then '周二' " +
            "      when 3 then '周三' " +
            "      when 4 then '周四' " +
            "      when 5 then '周五' " +
            "      when 6 then '周六' " +
            "      when 7 then '周日'  " +
            "     end) as weekDayName, " +
            "  a.itemType,a.itemId, " +
            " IF(a.itemType='0','课程','动作序列') as typeName, " +
            " IFNULL(d.course,e.actionArrayName) as itemName, " +
            " c.picAttachId as picAttach " +
            "from train_program_day_item a  " +
            "join train_program_week_detail b on a.trainProgramWeekDetailId = b.id " +
            "join train_program c on b.programId = c.id " +
            "left join course_head d on d.id = a.itemId and a.itemType='0' " +
            "left join action_array e on e.id = a.itemId and a.itemType='1' " +
            "where c.id=#{id} " +
            "order by b.weekdayName")
    List<TrainWeekDetailDto> getDetail(@Param("id")String id);

    //根据日期分组每天得计划数量
    @Select("SELECT day_list.day as date,IFNULL(data.allNum, 0) as count from  " +
            " (SELECT " +
            "   a.time time, " +
            "   a.allNum allNum " +
            " FROM " +
            "   ( " +
            "   SELECT " +
            "     a.time, " +
            "     SUM( b.count ) AS allNum  " +
            "   FROM " +
            "     (SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM train_program where deleted=0 GROUP BY  time ) a " +
            "     JOIN (  SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM train_program where deleted=0 GROUP BY  time) b ON a.time >= b.time  " +
            "   GROUP BY " +
            "     time  " +
            "   ORDER BY " +
            "   time ASC  " +
            "   ) a) data " +
            " right join  " +
            " (SELECT @date := DATE_ADD(@date, interval 1 day) day from  " +
            " (SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from train_program) " +
            "  days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryPlanTotal(Integer days, String beginDate, String endDate);
}
