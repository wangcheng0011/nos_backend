package com.knd.front.train.service;

import com.knd.common.response.Result;
import com.knd.front.train.dto.CourseDetailDto;

/**
 * @author liulongxiang
 * @interfaceName
 * @description
 * @date 17:13
 * @Version 1.0
 */
public interface ICourseDetailService {
   Result getCourseDetail(String courseId, String userId);
   Result getCourseNodeDetail(String courseId,String userId);
   Result getFreeTrainDetail(String actionId,String userId);
   Result getCourseVideoProgressInfo(String courseId,String userId);
}
