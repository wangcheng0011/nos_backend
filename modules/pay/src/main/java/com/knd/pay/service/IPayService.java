package com.knd.pay.service;


import com.alipay.api.AlipayApiException;
import com.knd.common.response.Result;
import com.knd.mybatis.SuperService;
import com.knd.pay.entity.TbOrder;
import com.knd.pay.request.CreateOrderRequest;
import com.knd.pay.request.ParseOrderNotifyRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
public interface IPayService extends SuperService<TbOrder> {

    Result tradePreCreatePay(CreateOrderRequest createOrderRequest) throws AlipayApiException;

    Result tradeQuery(String outTradeNo, String tradeNo);

    void vipHandler(TbOrder tbOrder);

    void closeOvertimeOrderBatch();

    Result refund(String outTradeNo, String tradeNo);

    Result tradeClose(TbOrder tbOrder, String outTradeNo, String tradeNo);

    void lbHandler(TbOrder tbOrder);

    Result tradeAppPay(CreateOrderRequest createOrderRequest);

    Result jsApiPay(HttpServletResponse response,CreateOrderRequest createOrderRequest) throws Exception;

    Result createOfficialAccountUnifiedOrder(String openid, String orderNo, BigDecimal amount) throws Exception;

    Result  v3Get();

    Result smallRoutinePay(CreateOrderRequest createOrderRequest);

    Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest) throws ParseException;

    Result wapPay(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Result alipayCallback(String outBizNo, String orderId);

    //   Result createOfficialAccountUnifiedOrder(String code);
}