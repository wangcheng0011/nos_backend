package com.knd.manage.logistics.service;

import com.knd.common.response.Result;
import com.knd.manage.logistics.vo.VoAneLogistics;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface IAneLogisticsService {

    Result trajectoryQuery(VoAneLogistics vo);


}
