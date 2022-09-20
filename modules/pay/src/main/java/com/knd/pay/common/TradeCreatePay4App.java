package com.knd.pay.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.knd.common.constant.Constant;
import com.knd.common.em.PaymentTypeEnum;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.ResultEnum;
import com.knd.pay.entity.TbOrder;
import com.knd.pay.request.CreateOrderRequest;
import com.knd.pay.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 *
 * @author will
 * @date 2021/8/5 7:07
 */
@Component
@Slf4j
public class TradeCreatePay4App extends TradeCreatePayTemplate {


    @Override
    protected void tradeCreatePayCore(TbOrder tbOrder, CreateOrderRequest createOrderRequest) {
        try {
            log.info("tradeCreatePayCore tbOrder:{{}}",tbOrder);
            log.info("tradeCreatePayCore createOrderRequest:{{}}",createOrderRequest);
            String orderInfo;
            if(StringUtils.isEmpty(createOrderRequest.getPaymentType())) {
                throw new CustomResultException(ResultEnum.PARAM_ERROR,"支付方式paymentType不能为空");
            }
            if(PaymentTypeEnum.ALIPAY.getCode().equals(createOrderRequest.getPaymentType())) {
                orderInfo = getAliPayInfo(tbOrder, createOrderRequest);
            }else{
                orderInfo = getWxPayInfo(tbOrder, createOrderRequest);
            }
            //设置app预支付信息
            tbOrder.setAppPayInfo(orderInfo);
        } catch (CustomResultException e) {
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw new CustomResultException(ResultEnum.FAIL,"获取 微信/支付宝 支付链接失败");

        }
    }

    private String getWxPayInfo(TbOrder tbOrder, CreateOrderRequest createOrderRequest) {
        log.info("getWxPayInfo tbOrder:{{}}",tbOrder);
        log.info("getWxPayInfo createOrderRequest:{{}}",createOrderRequest);
        String orderInfo;
        String  ip = "127.0.0.1";
        int amount = createOrderRequest.getAmount()
                .multiply(new BigDecimal(100)).intValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        now = now.plusMinutes(Constant.PAY_TIME_OUT);
        String expiresTime = dtf.format(now);
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(createOrderRequest.getDescription())
                .attach("")
                .out_trade_no(tbOrder.getOrderNo())
                .total_fee(amount+"")
                .time_expire(expiresTime)
                .spbill_create_ip(ip)
                .notify_url(createOrderRequest.getWxPayNotifyUrl())
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info("微信支付预订单创建返回结果"+xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        if (!WxPayKit.codeIsOk(returnCode)) {
            throw new RuntimeException("微信app支付预订单创建失败，returnCode为"+returnCode);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            throw new RuntimeException("微信app支付预订单创建失败，resultCode"+resultCode);
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(WxPayApiConfigKit.getWxPayApiConfig().getAppId(), WxPayApiConfigKit.getWxPayApiConfig().getMchId(), prepayId,
                WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);
        orderInfo = JSON.toJSONString(packageParams);
        return orderInfo;
    }

    private String getAliPayInfo(TbOrder tbOrder, CreateOrderRequest createOrderRequest) throws AlipayApiException {
        log.info("getAliPayInfo tbOrder:{{}}",tbOrder);
        log.info("getAliPayInfo createOrderRequest:{{}}",createOrderRequest);
        String orderInfo;
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(tbOrder.getDescription());
        model.setSubject(tbOrder.getDescription());
        model.setOutTradeNo(tbOrder.getOrderNo());
        model.setTimeoutExpress(Constant.PAY_TIME_OUT+"m");
        model.setTotalAmount(tbOrder.getAmount().toString());
        model.setPassbackParams("callback params");
        model.setProductCode(tbOrder.getDescription());
        AlipayTradeAppPayResponse alipayTradeAppPayResponse = AliPayApi.appPayToResponse(model, createOrderRequest.getAliPayNotifyUrl());
        log.info("支付宝支付预订单创建返回结果："+ JSONObject.toJSONString(alipayTradeAppPayResponse));
        orderInfo = alipayTradeAppPayResponse.getBody();
        return orderInfo;
    }



}
