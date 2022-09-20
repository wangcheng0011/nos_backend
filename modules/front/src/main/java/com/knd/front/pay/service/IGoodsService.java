package com.knd.front.pay.service;

import com.knd.common.response.Result;
import com.knd.front.entity.GoodsEntity;
import com.knd.front.login.request.GetOrderInfoRequest;
import com.knd.front.pay.request.CreateGoodsRequest;
import com.knd.front.pay.request.GoodsListRequest;
import com.knd.front.pay.request.ParseOrderNotifyRequest;
import com.knd.mybatis.SuperService;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IGoodsService extends SuperService<GoodsEntity> {


    Result getGoodsList(GoodsListRequest request);

    Result getGoods(String goodsId);


    Result getPayInfo(HttpServletResponse response, GetOrderInfoRequest getOrderInfoRequest);

    Result cancelOrder(String tradeNo);

    void add(CreateGoodsRequest createGoodsRequest);

    Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest);

    Result alipayCallback(String outBizNo, String orderId);
}
