package com.knd.manage.equip.service;

import com.knd.common.response.Result;
import com.knd.manage.equip.entity.CourseEquipment;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.mybatis.SuperService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangcheng
 * @since 2020-07-02
 */
public interface ICourseEquipmentService extends SuperService<CourseEquipment> {

    //新增
    Result add(String userId, String equipmentNo, String remark,String courseHeadId);

    //删除设备
    Result deleteEquipment(String userId, String id);

    //更新
    Result edit(String userId, String equipmentNo, String remark, String id,String courseHeadId);

    //根据课程Id获取列表
    List<CourseEquipment> getEquipmentListByCourseId(String courseId);

}
