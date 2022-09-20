package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.User;
import com.knd.front.user.dto.*;
import com.knd.front.user.entity.UserTrainCourseEntity;
import com.knd.front.user.request.UserTrainDataQueryRequest;
import com.knd.front.user.request.UserTrainInfoQueryRequest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liulongxiang
 * @interfaceName
 * @description
 * @date 10:24
 * @Version 1.0
 */
public interface UserInfoMapper extends BaseMapper<User> {
    TrainCourseHeadInfoDto getUserCourseHeadInfo(@Param("userId") String userId, @Param("trainReportId") String trainReportId );
    List<UserCourseInfoDto> getUserCourseInfo(@Param("page") Page<UserCourseInfoDto> page, @Param("userId") String userId);
    List<TrainCourseBlockInfoDto> getUserCourseBlockInfoList(@Param("trainCourseHeadInfoId") String trainCourseHeadInfoId);
    List<TrainCourseActInfoDto> getUserCourseActInfoList(@Param("trainCourseBlockInfoId") String trainCourseBlockInfoId);
    TrainFreeHeadDto getUserTrainFreeDetail(@Param("userId") String userId, @Param("trainReportId") String trainReportId);
    List<TrainListDto> getUserTrainInfo(@Param("page") Page<TrainListDto> page, @Param("userId") String userId);
    UserTrainDataDto getUserTrainData(UserTrainDataQueryRequest userTrainDataQueryRequest);

    List<UserConsecutiveTrainDayDto> getUserTrainConsecutiveDays(UserTrainDataQueryRequest userTrainDataQueryRequest);

    List<TrainListDto> getUserTrainInfoNew(@Param("page") Page<TrainListDto> tPage, @Param("userTrainInfoQueryRequest")UserTrainInfoQueryRequest userTrainInfoQueryRequest);

    @Select("select a.calorie,b.id,b.course,b.remark,c.filePath as picAttachUrl,b.videoDurationMinutes,b.amount,b.courseType,d.difficulty,  " +
            " max(a.lastModifiedDate) as time  " +
            " from train_course_head_info a   " +
            " LEFT JOIN course_head b on a.courseHeadId = b.id and b.releaseFlag =1 and b.deleted = 0 " +
            " LEFT JOIN attach c on b.picAttachId = c.id and c.deleted = 0 " +
            " LEFT JOIN base_difficulty d on b.difficultyId = d.id and d.deleted=0  " +
            " where a.userId = #{userId} and a.deleted=0   " +
            " GROUP BY a.courseHeadId  " +
            " ORDER BY time desc")
    List<UserTrainCourseEntity> getUserTrainCourseList(@Param("page") Page<UserTrainCourseDto> tPage, @Param("userId") String userId);

}
