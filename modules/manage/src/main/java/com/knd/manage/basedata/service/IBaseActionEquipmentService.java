package com.knd.manage.basedata.service;

import com.knd.manage.basedata.dto.EquipmentDto;
import com.knd.manage.basedata.entity.BaseActionEquipment;
import com.knd.manage.basedata.vo.VoEquipment;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBaseActionEquipmentService extends SuperService<BaseActionEquipment> {
    //新增动作适宜器材关系
    void add(String userId, String actionId, List<VoEquipment> voEquipmentList);
    //遍历更新 到 动作适宜器材关系表
    void edit(String userId,String actionId,List<VoEquipment> voEquipmentList);
    //删除
    void delete(String userId, String actionId);
    //根据动作id获取列表
    List<EquipmentDto> getList(String actionId);
}
