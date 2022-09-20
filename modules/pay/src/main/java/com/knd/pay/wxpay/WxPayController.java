package com.knd.pay.wxpay;

import com.alibaba.fastjson.JSON;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.RefundModel;
import com.ijpay.wxpay.model.RefundQueryModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.pay.entity.JsApiOrderModel;
import com.knd.pay.entity.Order;
import com.knd.pay.entity.WxPayBean;
import com.knd.pay.utils.StringUtils;
import com.knd.pay.utils.WChartUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>微信支付 Demo</p>
 *
 * @author Javen
 */
@Controller
@CrossOrigin
@RequestMapping("/wxPay")
public class WxPayController extends AbstractWxPayApiController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WxPayBean wxPayBean;

    @Autowired
    com.knd.pay.entity.WxPayV3Bean WxPayV3Bean;

    @Value("${domain}")
    private String domain;

    private String notifyUrl;
    private String refundNotifyUrl;
    private static final String USER_PAYING = "USERPAYING";


    @Override
    public WxPayApiConfig getApiConfig() {
        WxPayApiConfig apiConfig;

        try {
            apiConfig = WxPayApiConfigKit.getApiConfig(wxPayBean.getAppId());
        } catch (Exception e) {
            apiConfig = WxPayApiConfig.builder()
                    .appId(wxPayBean.getAppId())
                    .mchId(wxPayBean.getMchId())
                    .partnerKey(wxPayBean.getPartnerKey())
                    .certPath(wxPayBean.getCertPath())
                    .domain(domain)
                    .build();
        }
        notifyUrl = domain.concat("/wxPay/payNotify");
        refundNotifyUrl = domain.concat("/wxPay/refundNotify");
        return apiConfig;
    }

    @RequestMapping("")
    @ResponseBody
    public String index() {
        log.info("欢迎使用 IJPay 中的微信支付 -By Javen  <br/><br>  交流群：723992875");
        log.info(wxPayBean.toString());
        return ("欢迎使用 IJPay 中的微信支付 -By Javen  <br/><br>  交流群：723992875");
    }

    @GetMapping("/test")
    @ResponseBody
    public WxPayBean test() {
        return wxPayBean;
    }

    @GetMapping("/getKey")
    @ResponseBody
    public String getKey() {
        return WxPayApi.getSignKey(wxPayBean.getMchId(), wxPayBean.getPartnerKey(), SignType.MD5);
    }


//    /**
//     * 扫码模式一
//     */
//    @RequestMapping(value = "/scanCode1", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public AjaxResult scanCode1(HttpServletRequest request, HttpServletResponse response,
//                                @RequestParam("productId") String productId) {
//        try {
//            if (StrKit.isBlank(productId)) {
//                return new AjaxResult().addError("productId is null");
//            }
//            WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
//            //获取扫码支付（模式一）url
//            String qrCodeUrl = WxPayKit.bizPayUrl(config.getPartnerKey(), config.getAppId(), config.getMchId(), productId);
//            log.info(qrCodeUrl);
//            //生成二维码保存的路径
//            String name = "payQRCode1.png";
//            log.info(ResourceUtils.getURL("classpath:").getPath());
//            boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H,
//                    "png", 200, 200,
//                    ResourceUtils.getURL("classpath:").getPath().concat("static").concat(File.separator).concat(name));
//            if (encode) {
//                //在页面上显示
//                return new AjaxResult().success(name);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new AjaxResult().addError("系统异常：" + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 扫码支付模式一回调
//     */
//    @RequestMapping(value = "/scanCodeNotify", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public String scanCodeNotify(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String result = HttpKit.readData(request);
//            log.info("scanCodeNotify>>>" + result);
//            /**
//             * 获取返回的信息内容中各个参数的值
//             */
//            Map<String, String> map = WxPayKit.xmlToMap(result);
//            for (String key : map.keySet()) {
//                log.info("key= " + key + " and value= " + map.get(key));
//            }
//
//            String appId = map.get("appid");
//            String openId = map.get("openid");
//            String mchId = map.get("mch_id");
//            String isSubscribe = map.get("is_subscribe");
//            String nonceStr = map.get("nonce_str");
//            String productId = map.get("product_id");
//            String sign = map.get("sign");
//
//            Map<String, String> packageParams = new HashMap<String, String>(6);
//            packageParams.put("appid", appId);
//            packageParams.put("openid", openId);
//            packageParams.put("mch_id", mchId);
//            packageParams.put("is_subscribe", isSubscribe);
//            packageParams.put("nonce_str", nonceStr);
//            packageParams.put("product_id", productId);
//
//            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
//
//            String packageSign = WxPayKit.createSign(packageParams, wxPayApiConfig.getPartnerKey(), SignType.MD5);
//
//            String ip = IpKit.getRealIp(request);
//            if (StrKit.isBlank(ip)) {
//                ip = "127.0.0.1";
//            }
//            Map<String, String> params = UnifiedOrderModel
//                    .builder()
//                    .appid(wxPayApiConfig.getAppId())
//                    .mch_id(wxPayApiConfig.getMchId())
//                    .nonce_str(WxPayKit.generateStr())
//                    .body("IJPay 让支付触手可及-扫码支付模式一")
//                    .attach("Node.js 版:https://gitee.com/javen205/TNWX")
//                    .out_trade_no(WxPayKit.generateStr())
//                    .total_fee("1")
//                    .spbill_create_ip(ip)
//                    .notify_url(notifyUrl)
//                    .trade_type(TradeType.NATIVE.getTradeType())
//                    .openid(openId)
//                    .build()
//                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
//            String xmlResult = WxPayApi.pushOrder(false, params);
//            log.info("统一下单:" + xmlResult);
//            /**
//             * 发送信息给微信服务器
//             */
//            Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);
//            String returnCode = payResult.get("return_code");
//            String resultCode = payResult.get("result_code");
//            if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
//                // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
//                String prepayId = payResult.get("prepay_id");
//
//                Map<String, String> prepayParams = new HashMap<String, String>(10);
//                prepayParams.put("return_code", "SUCCESS");
//                prepayParams.put("appid", appId);
//                prepayParams.put("mch_id", mchId);
//                prepayParams.put("nonce_str", System.currentTimeMillis() + "");
//                prepayParams.put("prepay_id", prepayId);
//                String prepaySign;
//                if (sign.equals(packageSign)) {
//                    prepayParams.put("result_code", "SUCCESS");
//                } else {
//                    prepayParams.put("result_code", "FAIL");
//                    //result_code为FAIL时，添加该键值对，value值是微信告诉客户的信息
//                    prepayParams.put("err_code_des", "订单失效");
//                }
//                prepaySign = WxPayKit.createSign(prepayParams, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
//                prepayParams.put("sign", prepaySign);
//                String xml = WxPayKit.toXml(prepayParams);
//                log.error(xml);
//                return xml;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 扫码支付模式二
     */
    @RequestMapping(value = "/scanCode2", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Result scanCode2(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("total_fee") String totalFee) {

        if (StringUtils.isBlank(totalFee)) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "支付金额不能为空");
        }

        String ip = IpKit.getRealIp(request);
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        //WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        WxPayApiConfig wxPayApiConfig = getApiConfig();
        WxPayApiConfigKit.putApiConfig(wxPayApiConfig);
        LocalDateTime now = LocalDateTime.now();
        now = now.plusMinutes(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String strDate = dtf.format(now);
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-扫码支付模式二")
                .attach("Node.js 版:https://gitee.com/javen205/TNWXX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1")
                .time_expire(strDate)
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("统一下单:" + xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        System.out.println(returnMsg);
        if (!WxPayKit.codeIsOk(returnCode)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), returnMsg);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), returnMsg);
        }
        //生成预付订单success

        String qrCodeUrl = result.get("code_url");
        //String name = "payQRCode2.png";

//        boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
//                request.getSession().getServletContext().getRealPath("/") + File.separator + name);
//        if (encode) {
//            //在页面上显示
//            return ResultUtil.success(qrCodeUrl);
//        }
        return ResultUtil.success(qrCodeUrl);
    }


    @RequestMapping(value = "/queryOrder", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String queryOrder(@RequestParam(value = "transactionId", required = false) String transactionId, @RequestParam(value = "outTradeNo", required = false) String outTradeNo) {
        try {
//            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
            WxPayApiConfig wxPayApiConfig = getApiConfig();
            WxPayApiConfigKit.putApiConfig(wxPayApiConfig);
            Map<String, String> params = OrderQueryModel.builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .transaction_id(transactionId)
                    .out_trade_no(outTradeNo)
                    .nonce_str(WxPayKit.generateStr())
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);
            log.info("请求参数：{}", WxPayKit.toXml(params));
            String query = WxPayApi.orderQuery(params);
            Map<String, String> paramsMap = WxPayKit.xmlToMap(query);
            String returnCode = paramsMap.get("return_code");
            if (WxPayKit.codeIsOk(returnCode)) {
                String tradeState = paramsMap.get("trade_state");
                if ("SUCCESS".equals(tradeState)) {
                    return "tradeState";
                }
            }
            log.info("查询结果: {}", query);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return "系统错误";
        }
    }

    @RequestMapping(value = "/closeOrder", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String closeOrder(@RequestParam(value = "outTradeNo", required = false) String outTradeNo) {
        try {
//            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
            WxPayApiConfig wxPayApiConfig = getApiConfig();
            WxPayApiConfigKit.putApiConfig(wxPayApiConfig);
            Map<String, String> params = OrderQueryModel.builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .out_trade_no(outTradeNo)
                    .nonce_str(WxPayKit.generateStr())
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);
            log.info("请求参数：{}", WxPayKit.toXml(params));
            String query = WxPayApi.orderQuery(params);
            Map<String, String> paramsMap = WxPayKit.xmlToMap(query);
            String returnCode = paramsMap.get("return_code");
            if (WxPayKit.codeIsOk(returnCode)) {
                String result_code = paramsMap.get("result_code");
                if ("SUCCESS".equals(result_code)) {
                    return result_code;
                }
            }
            log.info("查询结果: {}", query);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return "系统错误";
        }
    }


    /**
     * 获取RSA加密公钥
     */
    @RequestMapping(value = "/getPublicKey", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String getPublicKey() {
        try {
            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

            Map<String, String> params = new HashMap<String, String>(4);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
            params.put("sign_type", "MD5");
            String createSign = WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.MD5);
            params.put("sign", createSign);
            return WxPayApi.getPublicKey(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 微信退款
     */
    @RequestMapping(value = "/refund", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refund(@RequestParam(value = "transactionId", required = false) String transactionId,
                         @RequestParam(value = "outTradeNo", required = false) String outTradeNo) {
        try {
            log.info("transactionId: {} outTradeNo:{}", transactionId, outTradeNo);

            if (StringUtils.isBlank(outTradeNo) && StringUtils.isBlank(transactionId)) {
                return "transactionId、out_trade_no二选一";
            }
//            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
            WxPayApiConfig wxPayApiConfig = getApiConfig();
            WxPayApiConfigKit.putApiConfig(wxPayApiConfig);

            Map<String, String> params = RefundModel.builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .transaction_id(transactionId)
                    .out_trade_no(outTradeNo)
                    .out_refund_no(WxPayKit.generateStr())
                    .total_fee("1")
                    .refund_fee("1")
                    .notify_url(refundNotifyUrl)
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);
            String refundStr = WxPayApi.orderRefund(false, params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
            log.info("refundStr: {}", refundStr);
            return refundStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信退款查询
     */
    @RequestMapping(value = "/refundQuery", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refundQuery(@RequestParam("transactionId") String transactionId,
                              @RequestParam("out_trade_no") String outTradeNo,
                              @RequestParam("out_refund_no") String outRefundNo,
                              @RequestParam("refund_id") String refundId) {

//        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        WxPayApiConfig wxPayApiConfig = getApiConfig();
        WxPayApiConfigKit.putApiConfig(wxPayApiConfig);

        Map<String, String> params = RefundQueryModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(transactionId)
                .out_trade_no(outTradeNo)
                .out_refund_no(outRefundNo)
                .refund_id(refundId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);

        return WxPayApi.orderRefundQuery(false, params);
    }

    /**
     * 退款通知
     */
    @RequestMapping(value = "/refundNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refundNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("退款通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        if (WxPayKit.codeIsOk(returnCode)) {
            String reqInfo = params.get("req_info");
            String decryptData = WxPayKit.decryptData(reqInfo, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey());
            log.info("退款通知解密后的数据=" + decryptData);
            // 更新订单信息
            // 发送通知等
            Map<String, String> xml = new HashMap<String, String>(2);
            xml.put("return_code", "SUCCESS");
            xml.put("return_msg", "OK");
            return WxPayKit.toXml(xml);
        }
        return null;
    }


    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode) && "SUCCESS".equals(params.get("trade_state"))) {
                // 更新订单信息

                // 发送通知等

                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }

    /**
     * 小程序微信支付的第一步,统一下单
     *
     * @return
     * @author lantian
     * @time 2019年4月23日17:36:35
     */
    @GetMapping("/createUnifiedOrder")
    public Result createUnifiedOrder(String orderNum, HttpServletRequest request) {

        String ip = "127.0.0.1";
        //订单假数据,正式使用中可以通过订单号查询出订单，这里做测试使用
        Order order = new Order();
        order.setMoney(20);
        order.setOrderNum("12313145251");
        order.setOpenid("oV-3C4iM7Wo7B9IrRfCDT8rpsY0I");
        //价格
        BigDecimal money = new BigDecimal(order.getMoney());
        Map<String, String> params = JsApiOrderModel
                .builder()
                .appid(wxPayBean.getAppId())
                .mch_id(wxPayBean.getMchId())
                // 订单描述
                .nonce_str(order.getOrderNum())
                .body("微信支付")
                // 订单号
                .out_trade_no(order.getOrderNum())
                // 支付金额注意是以分为单位的
                .total_fee(String.valueOf((money.multiply(new BigDecimal("100"))).intValue()))
                //发起订单者ip
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(order.getOpenid())
                .build()
                .createSign(wxPayBean.getAppSecret(), SignType.MD5);
        //进行数据加密封装
        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        if (StringUtils.equals(resultMap.get("return_code"), "SUCCESS")) {
            String prepayId = resultMap.get("prepay_id");
            Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayBean.getAppId(),
                    wxPayBean.getAppSecret(), SignType.MD5);
            // 微信浏览器中拿此参数调起支付
            return ResultUtil.success(packageParams);

        }
        return null;
    }


    /**
     * 公众号微信支付的第一步,统一下单
     *
     * @return
     * @author 王诚
     * @time 2019年4月23日17:36:35
     */
    @GetMapping("/createOfficialAccountUnifiedOrder")
    public Result createOfficialAccountUnifiedOrder(String code,String orderNum, HttpServletRequest request) {
        //订单假数据,正式使用中可以通过订单号查询出订单，这里做测试使用
        Order order = new Order();
        order.setMoney(20);
        order.setOrderNum("12313145251");
        order.setOpenid("oV-3C4iM7Wo7B9IrRfCDT8rpsY0I");
        String openid = WChartUtils.getOpenid(code, WxPayV3Bean.getAppId(), WxPayV3Bean.getMchId());
        String ip = "127.0.0.1";
        Map<String, String> params = JsApiOrderModel
                .builder()
                .appid(WxPayV3Bean.getAppId())
                .mch_id(WxPayV3Bean.getMchId())
                // 订单描述
                .nonce_str(order.getOrderNum())
                .body("微信支付")
                // 订单号
                .out_trade_no(order.getOrderNum())
                // 支付金额注意是以分为单位的
                .total_fee("1")
                //发起订单者ip
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(order.getOpenid())
                .build()
                .createSign(wxPayBean.getAppSecret(), SignType.MD5);
        // 发送请求
        String xmlResult = WxPayApi.pushOrder(false, params);
        // 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        // 判断返回的结果
        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return ResultUtil.success(returnMsg);
        }
        String resultCode = resultMap.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return ResultUtil.success(returnMsg);
        }

        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
        String prepayId = resultMap.get("prepay_id");
        // 二次签名，构建公众号唤起支付的参数,这里的签名方式要与上面统一下单请求签名方式保持一致
        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId,
                WxPayV3Bean.getAppId(), WxPayV3Bean.getPartnerKey(), SignType.HMACSHA256);
        // 将二次签名构建的数据返回给前端并唤起公众号支付
        String jsonStr = JSON.toJSONString(packageParams);
        return ResultUtil.success(jsonStr);
    }





    /**
     * 微信回调接口
     */
    @PostMapping("/wxPayNotice")
    public Map<String, String> parseOrderNotifyResult(@RequestBody String xmlData) throws ParseException {
        Map<String, String> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("parseOrderNotifyResult xmlData:{{}}",xmlData);
        System.out.println("微信回调开始了");
        //这里我只进行了部分回调信息的保存，更多参数请查看微信支付官方api:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8
        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlData);
        log.info("parseOrderNotifyResult resultMap:{{}}",resultMap);
        /*----------------------分割线，下面的是业务代码，根据自己业务进行修改--------------------------*/
        //订单成功业务处理
        if ("SUCCESS".equals(resultMap.get("result_code"))) {
            System.out.println(resultMap);
            //订单号
            String nonce_str = resultMap.get("nonce_str");
            System.out.println("执行具体业务");
        }
        /*----------------------分割线，业务结束--------------------------*/

        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        log.info("parseOrderNotifyResult map:{{}}",map);
        return map;
    }
}
