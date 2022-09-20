package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.basedata.entity.BaseArea;
import com.knd.manage.basedata.mapper.BaseAreaMapper;
import com.knd.manage.basedata.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final BaseAreaMapper baseAreaMapper;

    @Override
    public Result getAreaList() {
        QueryWrapper<BaseArea> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        List<BaseArea> baseAreaList = baseAreaMapper.selectList(wrapper);
        return ResultUtil.success(baseAreaList);
    }
}
