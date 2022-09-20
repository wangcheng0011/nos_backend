package com.knd.pay.common;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.knd.common.basic.DateUtils;
import com.knd.common.constant.Constant;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.pay.entity.TbOrder;
import com.knd.pay.entity.TbOrderItem;
import com.knd.pay.mapper.TbOrderItemMapper;
import com.knd.pay.mapper.TbOrderMapper;
import com.knd.pay.request.CreateOrderRequest;
import com.knd.pay.request.GoodsRequest;
import com.knd.pay.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author will
 * @date 2021/8/5 7:07
 */
@Component
@Slf4j
public class TradeCreatePay4Equipment extends TradeCreatePayTemplate {


    @Override
    protected void tradeCreatePayCore(TbOrder tbOrder, CreateOrderRequest createOrderRequest) {
        String aliQrCode;
        String wxQrCode;
        try {
            aliQrCode = getAliQrCode(createOrderRequest, tbOrder);
            wxQrCode = getWxQrCode(createOrderRequest, tbOrder);
            tbOrder.setWxQrCode(wxQrCode);
            tbOrder.setAliQrCode(aliQrCode);
        } catch (CustomResultException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomResultException(ResultEnum.FAIL,"获取 微信/支付宝 支付二维码失败");
        }
    }

    private String getAliQrCode(CreateOrderRequest createOrderRequest, TbOrder tbOrder) throws AlipayApiException {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(tbOrder.getDescription());
        model.setTotalAmount(tbOrder.getAmount().toString());
        model.setStoreId("1");
        model.setTimeoutExpress(Constant.PAY_TIME_OUT + "m");
        model.setOutTradeNo(tbOrder.getOrderNo());
        String resultStr = AliPayApi.tradePrecreatePayToResponse(model, createOrderRequest.getAliPayNotifyUrl()).getBody();
        tbOrder.setCreateDate(DateUtils.getCurrentLocalDateTime());
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        log.info("支付宝支付二维码返回结果："+jsonObject.getJSONObject("alipay_trade_precreate_response").toJSONString());
        if (!"Success".equals(jsonObject.getJSONObject("alipay_trade_precreate_response").getString("msg"))) {
            throw new CustomResultException(ResultEnum.FAIL,jsonObject.getJSONObject("alipay_trade_precreate_response").getString("sub_msg"));
        }
        return jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
    }

    private String getWxQrCode(CreateOrderRequest createOrderRequest, TbOrder tbOrder) {

        String ip = "127.0.0.1";
        int amount = createOrderRequest.getAmount()
                .multiply(new BigDecimal(100)).intValue();
        LocalDateTime now = LocalDateTime.now();
        now = now.plusMinutes(Constant.PAY_TIME_OUT);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String expiresTime = dtf.format(now);
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(createOrderRequest.getDescription())
                .attach("")
                .out_trade_no(tbOrder.getOrderNo())
                .total_fee(amount + "")
                .time_expire(expiresTime)
                .spbill_create_ip(ip)
                .notify_url(createOrderRequest.getWxPayNotifyUrl())
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("wx统一下单支付二维码返回结果:" + xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            throw new CustomResultException(ResultEnum.FAIL,"微信支付二维码创建失败，returnCode为"+returnCode);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            throw new CustomResultException(ResultEnum.FAIL,"微信支付二维码创建失败，resultCode"+resultCode);
        }
        return result.get("code_url");
    }

}
