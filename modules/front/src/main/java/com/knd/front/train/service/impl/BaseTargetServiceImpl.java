package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.BaseTarget;
import com.knd.front.home.dto.BaseTargetDto;
import com.knd.front.train.mapper.BaseTargetMapper;
import com.knd.front.train.service.IBaseTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
@Service
public class BaseTargetServiceImpl extends ServiceImpl<BaseTargetMapper, BaseTarget> implements IBaseTargetService {
    @Autowired
    private BaseTargetMapper baseTargetMapper;
    @Override
    public BaseTarget insertReturnEntity(BaseTarget entity) {
        return null;
    }

    @Override
    public BaseTarget updateReturnEntity(BaseTarget entity) {
        return null;
    }

    @Override
    public Result getTargetList() {
        QueryWrapper<BaseTarget> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.select("id", "target");
        List<BaseTarget> baseTargetList = baseTargetMapper.selectList(queryWrapper);
        List<BaseTargetDto> baseTargetDtoList = new ArrayList<>();
        for (BaseTarget bt:baseTargetList){
            BaseTargetDto baseTargetDto = new BaseTargetDto();
            baseTargetDto.setId(bt.getId());
            baseTargetDto.setName(bt.getTarget());
            baseTargetDtoList.add(baseTargetDto);
        }
        return ResultUtil.success(baseTargetDtoList);
    }
}
