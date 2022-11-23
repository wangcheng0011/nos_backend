package com.knd.front.train.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.TrainCourseHeadInfo;
import com.knd.front.user.dto.TrainCourseHeadInfoDataDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
public interface TrainCourseHeadInfoMapper extends BaseMapper<TrainCourseHeadInfo> {
    String getCourseHeadTotalTrainKg(@Param("userId") String userId);
    String getFreeHeadTotalTrainKg(@Param("userId") String userId);
    String getLatestTrainLevel(@Param("userId") String userId);
    TrainCourseHeadInfoDataDto getUserCourseHeadInfo(@Param("userId") String userId);
}
