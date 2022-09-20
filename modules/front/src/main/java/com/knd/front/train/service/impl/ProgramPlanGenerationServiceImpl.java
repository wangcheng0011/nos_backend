package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.knd.front.entity.ProgramPlanGenerationEntity;

import com.knd.front.train.mapper.ProgramPlanGenerationDao;
import com.knd.front.train.service.ProgramPlanGenerationService;
import org.springframework.stereotype.Service;


/**
 * @author will
 */
@Service("programPlanGenerationService")
public class ProgramPlanGenerationServiceImpl extends ServiceImpl<ProgramPlanGenerationDao, ProgramPlanGenerationEntity> implements ProgramPlanGenerationService {


}