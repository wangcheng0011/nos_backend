package com.knd.manage.equip.service;

import com.knd.common.response.Result;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.mybatis.SuperService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface IEquipmentInfoService extends SuperService<EquipmentInfo> {

    //新增
    Result add(String userId, String equipmentNo, String remark,String courseHeadId);


    //更新
    Result edit(String userId, String equipmentNo, String remark, String id,String courseHeadId);

    //更新状态
    Result changeStatus(String userId,String status,String id,String equipmentNo);

    //删除设备
    Result deleteEquipment(String userId, String id);

    //获取
    Result getEquipment(String id);

    //获取列表
    Result getEquipmentList(String equipmentNo,String status, String currentPage);

    //根据课程Id获取列表
    List<EquipmentInfo> getEquipmentListByCourseId(String courseId);

    Result saveEquipmentByBatch(InputStream inputStream, String originalFilename);
}
