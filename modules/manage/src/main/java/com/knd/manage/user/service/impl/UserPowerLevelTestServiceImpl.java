package com.knd.manage.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.user.dto.PowerLevelTestListDto;
import com.knd.manage.user.entity.UserPowerLevelTest;
import com.knd.manage.user.mapper.UserPowerLevelTestMapper;
import com.knd.manage.user.service.IUserPowerLevelTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.manage.user.dto.PowerLevelTestDto;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-27
 */
@Service
public class UserPowerLevelTestServiceImpl extends ServiceImpl<UserPowerLevelTestMapper, UserPowerLevelTest> implements IUserPowerLevelTestService {

    @Override
    public UserPowerLevelTest insertReturnEntity(UserPowerLevelTest entity) {
        return null;
    }

    @Override
    public UserPowerLevelTest updateReturnEntity(UserPowerLevelTest entity) {
        return null;
    }

    //查询注册会员力量等级测试列表
    @Override
    public Result queryPowerLevelTestList(String nickName, String mobile, String action, String trainTimeBegin, String trainTimeEnd, String current) throws ParseException {
        IPage<PowerLevelTestDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //分页查询注册会员力量等级测试列表
        page = baseMapper.selectPowerLevelTestPageBySome(page, nickName == null ? "%%" : "%" + nickName + "%", mobile == null ? "%%" : "%" + mobile + "%",
                action == null ? "%%" : "%" + action + "%", trainTimeBegin == null ? null : sdf.parse(trainTimeBegin),
                trainTimeEnd == null ? null : sdf.parse(trainTimeEnd));
        //拼接出参格式
        PowerLevelTestListDto powerLevelTestListDto = new PowerLevelTestListDto();
        powerLevelTestListDto.setTotal((int)page.getTotal());
        powerLevelTestListDto.setPowerLevelTestList(page.getRecords());
        return ResultUtil.success(powerLevelTestListDto);
    }
}
