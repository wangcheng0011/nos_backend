package com.knd.batch.mapper;

import com.knd.batch.entity.TrainCourseHeadInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-08-13
 */
public interface TrainCourseHeadInfoMapper extends BaseMapper<TrainCourseHeadInfo> {

    //根据用户id获取完成课程训练的日期列表,升序
    List<Date> selectTrainDateListById(String id);

    //根据周日期范围获取完成的的课程训练天数
    int selectCountByDateAndId(@Param("userId") String userId, @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);


}
