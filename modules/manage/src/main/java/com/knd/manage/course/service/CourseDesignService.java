package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.entity.TrainProgramEntity;
import com.knd.manage.course.request.AddTrainRequest;
import com.knd.mybatis.SuperService;

/**
 * @author zm
 */
public interface CourseDesignService extends SuperService<TrainProgramEntity> {

    /**
     * 获取训练计划列表
     * @param programName
     * @param userId
     * @return
     */
    Result getTrainList(String programName,String type,String userId,String current);

    /**
     * 删除训练计划
     * @param id
     * @return
     */
    Result deleteTrain(String id);

    /**
     * 新增训练计划
     * @param request
     * @return
     */
    Result addTrain(AddTrainRequest request);

    /**
     * 更新训练计划
     * @param request
     * @return
     */
    Result editTrain(AddTrainRequest request);

    /**
     * 获取动作序列列表
     * @param actionArrayName
     * @param current
     * @return
     */
    Result getActionList(String actionArrayName,String current);

    /**
     * 获取训练计划详情
     * @param id
     * @return
     */
    Result getDetail(String id);

}
