package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.basedata.entity.ActionType;
import com.knd.manage.basedata.mapper.ActionTypeMapper;
import com.knd.manage.basedata.service.IBaseActionTypeService;
import org.springframework.stereotype.Service;

/**
 * @author will
 */
@Service
public class BaseActionTypeServiceImpl extends ServiceImpl<ActionTypeMapper, ActionType> implements IBaseActionTypeService {
    @Override
    public Result getActionTypeList() {
        QueryWrapper<ActionType> actionTypeQueryWrapper = new QueryWrapper<>();
        actionTypeQueryWrapper.eq("deleted","0");
        return ResultUtil.success(baseMapper.selectList(actionTypeQueryWrapper));
    }

    @Override
    public ActionType insertReturnEntity(ActionType entity) {
        return null;
    }

    @Override
    public ActionType updateReturnEntity(ActionType entity) {
        return null;
    }
}
