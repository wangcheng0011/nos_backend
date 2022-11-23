package com.knd.front.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knd.common.response.Result;
import com.knd.front.entity.ProgramEntity;
import com.knd.front.train.request.GetTrainProgramRequest;
import com.knd.front.train.request.SaveTrainProgramRequest;

import java.text.ParseException;

/**
 * 训练计划 
 *
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
public interface ProgramService extends IService<ProgramEntity> {
    /**
     * 保存训练计划
     * @param saveTrainProgramRequest
     * @return
     */
    Result saveTrainProgram(SaveTrainProgramRequest saveTrainProgramRequest);

    /**
     * 获取训练计划
     * @param getTrainProgramRequest
     * @return
     */
    Result getTrainProgram(GetTrainProgramRequest getTrainProgramRequest);

    /**
     * 请假
     * @param userId
     * @param restDate
     * @return
     */
    Result takeRestTrainProgram(String trainProgramId,String userId, String restDate) throws ParseException;

    //删除训练计划
    Result deleteTrainProgram(String userId, String id);

    //查询历史训练计划
    Result queryHistoryTrainProgram(String userId,String current);

    Result trainProgramPush();
}

