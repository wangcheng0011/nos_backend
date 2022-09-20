package com.knd.manage.equip.service;

import com.knd.common.response.Result;
import com.knd.manage.equip.entity.EquipmentReportInfo;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface IEquipmentReportInfoService extends SuperService<EquipmentReportInfo> {
    //新增设备上传信息
    Result saveReportInfo(String userId, String equipmentNo, String turnOnTime, String hardVersion,
                          String mainboardVersion, String positionInfo, String appVersion);

    //获取设备上传信息列表
    Result getReportInfoList(String equipmentNo, String current);
}
