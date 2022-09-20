package com.knd.manage.logistics.service;

import com.knd.common.response.Result;
import com.knd.manage.logistics.entity.LogisticsEntity;
import com.knd.manage.logistics.vo.VoLogistics;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface ILogisticsService extends SuperService<LogisticsEntity> {

    Result trajectoryQuery(VoLogistics vo);

    Result queryOrder(String logisticID,String logisticCompanyID);
}
