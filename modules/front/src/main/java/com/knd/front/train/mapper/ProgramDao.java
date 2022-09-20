package com.knd.front.train.mapper;

import com.knd.front.entity.ProgramEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.train.dto.TrainProgramPlanDto;
import com.knd.front.train.request.GetTrainProgramRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 训练计划 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Mapper
public interface ProgramDao extends BaseMapper<ProgramEntity> {

    TrainProgramPlanDto getTrainProgramPlan(GetTrainProgramRequest getTrainProgramRequest);
	
}
