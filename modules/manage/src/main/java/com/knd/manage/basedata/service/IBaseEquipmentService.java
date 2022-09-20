package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseEquipment;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBaseEquipmentService extends SuperService<BaseEquipment> {
    //新增器材
    Result add(String userId, String equipment, String remark);

    //更新器材
    Result edit(String userId, String equipment, String remark, String equipmentId);

    //删除器材
    Result deleteEquipment(String userId, String id);

    //获取器材
    Result getEquipment(String id);

    //获取器材列表
    Result getEquipmentList(String part, String currentPage);

}
