package com.knd.front.login.service;


import com.knd.common.response.Result;
import com.knd.front.entity.VipMenu;
import com.knd.front.login.request.GetOrderInfoRequest;
import com.knd.mybatis.SuperService;

import java.math.BigDecimal;

/**
 * <p>
 * 会员套餐表  服务类
 * </p>
 *
 * @author will
 * @since 2021-01-13
 */
public interface IVipMenuService extends SuperService<VipMenu> {

    Result getPayInfo(GetOrderInfoRequest getOrderInfoRequest);

    Result tradeQuery(String outTradeNo, String tradeNo);

    Result getVipMenu(String userId);

    Result getOrderList(String userId,String status,String current,String queryParam,String platform);

    Result createOfficialAccountUnifiedOrder(String openid, String orderNo, BigDecimal amount);
}
