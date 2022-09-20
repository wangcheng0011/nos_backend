package com.knd.front.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knd.common.response.Result;
import com.knd.front.entity.EquipmentReportInfo;
import com.knd.front.train.request.EquipmentReportInfoRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-08
 */
public interface IEquipmentReportInfoService extends IService<EquipmentReportInfo> {
   Result commitEquipmentReportInfo(EquipmentReportInfoRequest equipmentReportInfoRequest);
}
