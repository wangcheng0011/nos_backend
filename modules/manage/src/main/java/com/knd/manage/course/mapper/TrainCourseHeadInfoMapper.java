package com.knd.manage.course.mapper;

import com.knd.manage.course.entity.TrainCourseHeadInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
public interface TrainCourseHeadInfoMapper extends BaseMapper<TrainCourseHeadInfo> {

    //根据用户id获取完成课程训练的日期列表,升序
    List<Date> selectTrainDateListById(String id);

    //根据周日期范围获取完成的的课程训练天数
    int selectCountByDateAndId(@Param("userId") String userId,@Param("beginDate") LocalDate beginDate,@Param("endDate") LocalDate endDate);

    @Select("select c.difficulty,count(1) as num from course_head b " +
            "            LEFT JOIN base_difficulty c on b.difficultyId = c.id  " +
            "            where b.deleted=0 and c.deleted=0 GROUP BY b.difficultyId")
    List<Map<String,Object>> getTrainDifficultyNumList();

}
