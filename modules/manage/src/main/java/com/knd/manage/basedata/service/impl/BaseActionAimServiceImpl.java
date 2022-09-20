package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.AimSettingDto;
import com.knd.manage.basedata.vo.VoAimSetting;
import com.knd.manage.basedata.entity.BaseActionAim;
import com.knd.manage.basedata.mapper.BaseActionAimMapper;
import com.knd.manage.basedata.service.IBaseActionAimService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
@Service
public class BaseActionAimServiceImpl extends ServiceImpl<BaseActionAimMapper, BaseActionAim> implements IBaseActionAimService {

    @Override
    public BaseActionAim insertReturnEntity(BaseActionAim entity) {
        return null;
    }

    @Override
    public BaseActionAim updateReturnEntity(BaseActionAim entity) {
        return null;
    }

    //新增动作目标信息
    @Override
    public void add(String userId, String actionId, List<VoAimSetting> voAimSettingList) {
        BaseActionAim ba = new BaseActionAim();
        ba.setCreateBy(userId);
        ba.setCreateDate(LocalDateTime.now());
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        ba.setDeleted("0");
        ba.setAction(actionId);
        for (VoAimSetting a : voAimSettingList) {
            ba.setId(UUIDUtil.getShortUUID());
            ba.setPowerLevel(a.getPowerLevel());
            ba.setAimDuration(a.getAimDuration());
            ba.setAimTimes(a.getAimTimes());
            ba.setBasePower(a.getBasePower());
            baseMapper.insert(ba);
        }
    }

    @Override
    public void edit(String userId, String actionId, List<VoAimSetting> voAimSettingList) {
        //根据动作id删除所有
        this.delete(userId, actionId);
        //重新插入
        this.add(userId, actionId, voAimSettingList);

    }

    @Override
    public void delete(String userId, String actionId) {
        //根据动作id删除所有
        UpdateWrapper<BaseActionAim> uw = new UpdateWrapper<>();
        uw.eq("action", actionId);
        uw.eq("deleted", "0");
        BaseActionAim ba = new BaseActionAim();
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.update(ba, uw);
    }

    @Override
    public List<AimSettingDto> getList(String actionId) {
        QueryWrapper<BaseActionAim> qw = new QueryWrapper<>();
        qw.eq("action", actionId);
        qw.eq("deleted", "0");
        qw.select("powerLevel", "aimDuration", "aimTimes", "basePower");
        List<BaseActionAim> list = baseMapper.selectList(qw);
        //转换数据格式
        List<AimSettingDto> aimSettingDtos = new ArrayList<>();
        for (BaseActionAim b : list) {
            AimSettingDto a = new AimSettingDto();
            a.setAimDuration(b.getAimDuration());
            a.setAimTimes(b.getAimTimes());
            a.setPowerLevel(b.getPowerLevel());
            a.setBasePower(b.getBasePower());
            aimSettingDtos.add(a);
        }
        return aimSettingDtos;
    }
}
