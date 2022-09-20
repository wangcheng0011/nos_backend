package com.knd.front.train.mapper;

import com.knd.front.entity.ProgramPlanGenerationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 训练计划生成表 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Mapper
public interface ProgramPlanGenerationDao extends BaseMapper<ProgramPlanGenerationEntity> {
	String getEndTrainDate(@Param("userId") String userId,@Param("trainProgramId") String trainProgramId);
}
