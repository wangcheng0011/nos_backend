package com.knd.manage.mall.service;

import com.knd.common.response.Result;
import com.knd.manage.mall.request.EditInstallRequest;
import com.knd.manage.mall.request.OrderInstallRequest;

public interface IOrderInstallService {

    /**
     * 新增或者更新安装信息
     * @param request
     * @return
     */
    Result addOrUpdate(OrderInstallRequest request);

    /**
     * 根据订单编号获取安装信息
     * @param orderId
     * @return
     */
    Result getInstall(String orderId);

    /**
     * 根据区域id获取安装人员信息
     * @param areaId
     * @return
     */
    Result getInstallPersonList(String areaId);

    /**
     * 维护安装流程
     * @param request
     * @return
     */
    Result editInstall(EditInstallRequest request);

}
