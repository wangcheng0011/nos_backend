package com.knd.manage.admin.service;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.admin.entity.Power;
import com.knd.mybatis.SuperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-17
 */
public interface IPowerService extends SuperService<Power> {
    //查询权限信息
    Result getPowerList(String roleId);
}
