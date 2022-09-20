package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.UserTrainLevel;

import com.knd.front.user.mapper.UserTrainLevelMapper;
import com.knd.front.user.service.IUserTrainLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
@Service
public class UserTrainLevelServiceImpl extends ServiceImpl<UserTrainLevelMapper, UserTrainLevel> implements IUserTrainLevelService {

    @Autowired
    private UserTrainLevelMapper userTrainLevelMapper;
    @Override
    public UserTrainLevel insertReturnEntity(UserTrainLevel entity) {
        return null;
    }

    @Override
    public UserTrainLevel updateReturnEntity(UserTrainLevel entity) {
        return null;
    }

    @Override
    public Result getUserTrainLevels(String userId, String currentPage) {
        IPage<UserTrainLevel> iPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        QueryWrapper<UserTrainLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("deleted",0);
        queryWrapper.select("trainLevel","trainPeriodBeginTime","trainPeriodEndTime");
        return ResultUtil.success(userTrainLevelMapper.selectPage(iPage,queryWrapper));
    }
}
