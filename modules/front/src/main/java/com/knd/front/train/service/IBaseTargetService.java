package com.knd.front.train.service;

import com.knd.common.response.Result;
import com.knd.front.entity.BaseTarget;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
public interface IBaseTargetService extends SuperService<BaseTarget> {
    Result getTargetList();
}
