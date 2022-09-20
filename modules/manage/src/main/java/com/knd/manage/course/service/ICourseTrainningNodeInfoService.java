package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.entity.CourseTrainningNodeInfo;
import com.knd.manage.course.dto.NodeDto;
import com.knd.manage.course.vo.VoSaveCourseNodeInfo;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-06
 */
public interface ICourseTrainningNodeInfoService extends SuperService<CourseTrainningNodeInfo> {
    //维护训练课小节信息
    Result saveCourseNodeInfo(VoSaveCourseNodeInfo vo);

    //查询训练课小节信息
    Result getCourseNodeInfo(String id);

    //删除训练课小节信息
    Result deleteCourseNodeInfo(String userId, String id);



}
