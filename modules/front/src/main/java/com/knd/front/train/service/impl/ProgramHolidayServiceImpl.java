package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.knd.front.entity.ProgramHolidayEntity;
import com.knd.front.train.mapper.ProgramHolidayDao;
import com.knd.front.train.service.ProgramHolidayService;
import org.springframework.stereotype.Service;


/**
 * @author will
 */
@Service("programHolidayService")
public class ProgramHolidayServiceImpl extends ServiceImpl<ProgramHolidayDao, ProgramHolidayEntity> implements ProgramHolidayService {

}