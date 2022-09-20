package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.entity.Agreement;
import com.knd.front.entity.ViewRecordEntity;
import com.knd.front.home.mapper.AgreementMapper;
import com.knd.front.home.mapper.ViewRecordMapper;
import com.knd.front.home.service.IAgreementService;
import com.knd.front.home.service.IViewRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 浏览类型
 * @author will
 */
@Service
public class ViewRecordImpl extends ServiceImpl<ViewRecordMapper, ViewRecordEntity> implements IViewRecordService {

    @Override
    public void addViewRecord(Integer type, String itemId) {
        String userId = UserUtils.getUserId();
        ViewRecordEntity viewRecordEntity = new ViewRecordEntity();
        viewRecordEntity.setUserId(userId);
        viewRecordEntity.setType(type);
        viewRecordEntity.setCreateDate(DateUtils.getCurrentLocalDateTime());
        viewRecordEntity.setItemId(itemId);
        baseMapper.insert(viewRecordEntity);
    }

    @Override
    public ViewRecordEntity insertReturnEntity(ViewRecordEntity entity) {
        return null;
    }

    @Override
    public ViewRecordEntity updateReturnEntity(ViewRecordEntity entity) {
        return null;
    }
}
