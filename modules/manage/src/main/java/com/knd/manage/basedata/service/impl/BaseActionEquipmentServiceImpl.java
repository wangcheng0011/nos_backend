package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.EquipmentDto;
import com.knd.manage.basedata.entity.BaseActionEquipment;
import com.knd.manage.basedata.vo.VoEquipment;
import com.knd.manage.basedata.mapper.BaseActionEquipmentMapper;
import com.knd.manage.basedata.service.IBaseActionEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class BaseActionEquipmentServiceImpl extends ServiceImpl<BaseActionEquipmentMapper, BaseActionEquipment> implements IBaseActionEquipmentService {

    @Override
    public BaseActionEquipment insertReturnEntity(BaseActionEquipment entity) {
        return null;
    }

    @Override
    public BaseActionEquipment updateReturnEntity(BaseActionEquipment entity) {
        return null;
    }
    //新增动作适宜器材关系
    @Override
    public void add(String userId, String actionId, List<VoEquipment> voEquipmentList) {
        if (StringUtils.isNotEmpty(voEquipmentList)){
            BaseActionEquipment bae = new BaseActionEquipment();
            bae.setActionId(actionId);
            bae.setCreateBy(userId);
            bae.setCreateDate(LocalDateTime.now());
            bae.setLastModifiedBy(userId);
            bae.setLastModifiedDate(LocalDateTime.now());
            bae.setDeleted("0");
            for (VoEquipment e : voEquipmentList) {
                bae.setId(UUIDUtil.getShortUUID());
                bae.setEquipmentId(e.getEquipmentId());
                baseMapper.insert(bae);
            }
        }
    }
    //遍历更新 到 动作适宜器材关系表
    @Override
    public void edit(String userId, String actionId, List<VoEquipment> voEquipmentList) {
        //根据动作id删除所有
        this.delete(userId, actionId);
        //重新插入
        this.add(userId, actionId, voEquipmentList);
    }

    @Override
    public void delete(String userId, String actionId) {
        //根据动作id删除所有
        UpdateWrapper<BaseActionEquipment> uw = new UpdateWrapper<>();
        uw.eq("actionId", actionId);
        uw.eq("deleted", "0");
        BaseActionEquipment bae = new BaseActionEquipment();
        bae.setDeleted("1");
        bae.setLastModifiedBy(userId);
        bae.setLastModifiedDate(LocalDateTime.now());
        baseMapper.update(bae, uw);
    }

    //根据动作id获取器材id和器材名称
    @Override
    public List<EquipmentDto> getList(String actionId) {
        return  baseMapper.selectIDAndNameByActionId(actionId);
    }
}
