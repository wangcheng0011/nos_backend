package com.knd.pay.common;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.RefundQueryModel;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.pay.entity.AliPayBean;
import com.knd.pay.entity.TbOrder;
import com.knd.pay.entity.TbOrderItem;
import com.knd.pay.entity.WxPayBean;
import com.knd.pay.mapper.TbOrderItemMapper;
import com.knd.pay.mapper.TbOrderMapper;
import com.knd.pay.request.CancelOrderCoachRequest;
import com.knd.pay.request.CreateOrderRequest;
import com.knd.pay.request.ParseOrderNotifyRequest;
import com.knd.pay.service.IPayService;
import com.knd.pay.service.feignInterface.CoachOrderFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author will
 */
@Controller
@RequestMapping("/pay")
@Api(tags = "pay")
@Slf4j
public class PayController {

    String serialNo;

    String platSerialNo;

    @Value("${domain}")
    private String domain;


    @Resource
    private AliPayBean aliPayBean;

    @Resource
    private WxPayBean wxPayBean;

    @Autowired
    private IPayService iPayService;

    @Autowired
    private TradeCreatePay4Equipment tradeCreatePay4Equipment;

    @Autowired
    private TradeCreatePay4App tradeCreatePay4App;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private CoachOrderFeignClient coachOrderFeignClient;

    @PostConstruct
    public AliPayApiConfig getAliApiConfig() throws AlipayApiException {
        AliPayApiConfig aliPayApiConfig;
        try {
            aliPayApiConfig = AliPayApiConfigKit.getApiConfig(aliPayBean.getAppId());
        } catch (Exception e) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(aliPayBean.getAppId())
                    .setAliPayPublicKey(aliPayBean.getPublicKey())
                    .setCharset("UTF-8")
                    .setPrivateKey(aliPayBean.getPrivateKey())
                    .setServiceUrl(aliPayBean.getServerUrl())
                    .setSignType("RSA2")
                    // 普通公钥方式
                    .build();
                    // 证书模式
                    //.buildByCert();

        }
        AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
        return aliPayApiConfig;
    }

    @PostConstruct
    public WxPayApiConfig getWxPayApiConfig() {
        WxPayApiConfig wxPayApiConfig;
        try {
            wxPayApiConfig = WxPayApiConfigKit.getApiConfig(wxPayBean.getAppId());
        } catch (Exception e) {
            wxPayApiConfig = WxPayApiConfig.builder()
                    .appId(wxPayBean.getAppId())
                    .mchId(wxPayBean.getMchId())
                    .partnerKey(wxPayBean.getPartnerKey())
                    .certPath(wxPayBean.getCertPath())
                    .domain(domain)
                    .build();
        }
        WxPayApiConfigKit.putApiConfig(wxPayApiConfig);
        return wxPayApiConfig;
    }


    /**
     * 扫码支付
     */
    @PostMapping(value = "/tradePreCreatePay")
    @ResponseBody
    @Log("I11X-获取大屏支付二维码")
    @ApiOperation(value = "I11X-获取大屏支付二维码", notes = "I11X-获取大屏支付二维码")
    public Result tradePreCreatePay(@Valid @RequestBody CreateOrderRequest createOrderRequest, BindingResult bindingResult) throws AlipayApiException {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        //  return tradeCreatePay4Equipment.tradeCreatePay(createOrderRequest);
      return iPayService.tradePreCreatePay(createOrderRequest);
    }


    @PostMapping(value = "/v3Get")
    @ResponseBody
    @Log("I11X-v3Get")
    @ApiOperation(value = "I11X-v3Get", notes = "I11X-v3Get")
    public Result v3Get() {
        return iPayService.v3Get();
    }



    /**
     * app支付
     */
    @PostMapping(value = "/tradeAppPay")
    @ResponseBody
    @Log("I11X-获取app预支付信息")
    @ApiOperation(value = "I11X-获取app预支付信息", notes = "I11X-获取app预支付信息")
    public Result tradeAppPay(@Valid @RequestBody CreateOrderRequest createOrderRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        //   return tradeCreatePay4App.tradeCreatePay(createOrderRequest);
        return iPayService.tradeAppPay(createOrderRequest);
    }

    /**
     * jsapi支付
     */
    @PostMapping(value = "/jsApiPay")
    @ResponseBody
    @Log("I11X-官网jsApiPay支付")
    @ApiOperation(value = "I11X-官网jspApi支付", notes = "I11X-官网jspApi支付")
    public Result jsApiPay(HttpServletResponse response,@Valid @RequestBody CreateOrderRequest createOrderRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iPayService.jsApiPay(response,createOrderRequest);

    }

    /**
     * 微信H5支付
     */
    @PostMapping(value = "/wxH5Pay")
    @ResponseBody
    @Log("I11X-微信H5支付")
    @ApiOperation(value = "I11X-微信H5支付", notes = "I11X-微信H5支付")
    public Result wxH5Pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return iPayService.wapPay(request, response) ;
    }


    /**
     * 小程序支付
     */
    @PostMapping(value = "/smallRoutinePay")
    @ResponseBody
    @Log("I11X-小程序支付")
    @ApiOperation(value = "I11X-小程序支付", notes = "I11X-小程序支付")
    public Result smallRoutinePay(@Valid @RequestBody CreateOrderRequest createOrderRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iPayService.smallRoutinePay(createOrderRequest);

    }


    /**
     * 支付宝回调接口
     */
    @PostMapping(value = "/alipayCallback")
    @Log("I11X-支付宝回调接口")
    @ApiOperation(value = "I11X-支付宝回调接口", notes = "I11X-支付宝回调接口")
    public Result alipayCallback(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                @RequestParam(required = false, name = "orderId") String orderId)  {
        return  iPayService.alipayCallback(outBizNo,orderId);
    }


    /**
     * 微信回调接口
     */
    @PostMapping(value = "/parseOrderNotifyResult")
    @Log("I11X-微信回调接口")
    @ApiOperation(value = "I11X-微信回调接口", notes = "I11X-微信回调接口")
    public Result parseOrderNotifyResult(@RequestBody ParseOrderNotifyRequest parseOrderNotifyRequest) throws ParseException {
        return  iPayService.parseOrderNotifyResult(parseOrderNotifyRequest);
    }







    /**
     * 公众号微信支付的第一步,统一下单
     *
     * @return
     * @author 王诚
     * @time 2019年4月23日17:36:35
     */
    @GetMapping(value = "/createOfficialAccountUnifiedOrder")
    @ResponseBody
    @Log("I11X-JS 公众号微信支付的第一步,统一下单")
    @ApiOperation(value = "I11X-JS 公众号微信支付的第一步,统一下单", notes = "I11X-JS 公众号微信支付的第一步,统一下单")
    public Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                                    @RequestParam(required = false, name = "orderNo") String orderNo,
                                                    @RequestParam(required = false, name = "amount") BigDecimal amount) throws Exception {
        return iPayService.createOfficialAccountUnifiedOrder(openid,orderNo,amount);
    }


    /**
     * 退款
     */
    @RequestMapping(value = "/tradeRefund")
    @ResponseBody
    public Result tradeRefund(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {

//        try {
//            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
//            if (StringUtils.isNotEmpty(outTradeNo)) {
//                model.setOutTradeNo(outTradeNo);
//            }
//            if (StringUtils.isNotEmpty(tradeNo)) {
//                model.setTradeNo(tradeNo);
//            }
//            model.setRefundAmount("0.01");
//            model.setRefundReason("正常退款");
//            return AliPayApi.tradeRefundToResponse(model).getBody();
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
        return iPayService.refund(outTradeNo,tradeNo);
    }

    /**
     * 交易查询
     */
    @GetMapping(value = "/tradeQuery")
    @ResponseBody
    public Result tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
        return iPayService.tradeQuery(outTradeNo,tradeNo);
    }

    /**
     * 撤销订单
     */
    @RequestMapping(value = "/tradeCancel")
    @ResponseBody
    public String tradeCancel(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
        try {
            AlipayTradeCancelModel model = new AlipayTradeCancelModel();
            if (StringUtils.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (StringUtils.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }

            return AliPayApi.tradeCancelToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/tradeClose")
    @ResponseBody
    public Result tradeClose(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("tradeNo") String tradeNo) {
//        try {
//            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
//            if (StringUtils.isNotEmpty(outTradeNo)) {
//                model.setOutTradeNo(outTradeNo);
//            }
//            if (StringUtils.isNotEmpty(tradeNo)) {
//                model.setTradeNo(tradeNo);
//            }
//
//            return AliPayApi.tradeCloseToResponse(model).getBody();
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
        return iPayService.tradeClose(null,outTradeNo,tradeNo);

    }

    /**
     * 批处理关闭超时订单
     */
    @GetMapping(value = "/closeOvertimeOrderBatch")
    @ResponseBody
    public void closeOvertimeOrderBatch() {
        iPayService.closeOvertimeOrderBatch();
    }




    @RequestMapping(value = "/wx_notify_url",method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String wxNotifyUrl(HttpServletRequest request) {
        try {
            log.info("wx_notify_url request:{{}}",request);
            String xmlMsg = HttpKit.readData(request);
            log.trace("支付通知=" + xmlMsg);
            Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
            log.info("wx_notify_url params:{{}}",params);
            QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("orderNo", params.get("out_trade_no"));
            TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
            log.info("wx_notify_url tbOrder:{{}}",tbOrder);
            if (tbOrder != null && (OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())||
                    OrderStatusEnum.ORDER_CLOSED.getCode().equals(tbOrder.getStatus()))) {
                String returnCode = params.get("return_code");
                // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
                // 注意此处签名方式需与统一下单的签名类型一致
                if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
                    if (WxPayKit.codeIsOk(returnCode)&&"SUCCESS".equals(params.get("result_code"))) {
                        // 更新订单信息
                        if((tbOrder.getAmount().multiply(new BigDecimal(100)).intValue())==new BigDecimal(params.get("total_fee")).intValue()){
                            tbOrder.setPaymentType("2");
                            tbOrder.setOutOrderNo(params.get("transaction_id"));
                            log.info("wx_notify_url OrderType:{{}}",tbOrder.getOrderType());
                            if("1".equals(tbOrder.getOrderType())||"3".equals(tbOrder.getOrderType())){
                                //会员、课程购买直接完成
                                tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
                                log.info("wx_notify_url Status:{{}}",tbOrder.getStatus());
                            }else{
                                tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                                log.info("wx_notify_url Status:{{}}",tbOrder.getStatus());
                            }
                            //tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                            tbOrder.setPaymentTime(DateUtils.getCurrentLocalDateTime());
                            tbOrder.setLastModifiedDate(tbOrder.getPaymentTime());
                            List<TbOrderItem> orderItems = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                                    .eq("tbOrderId", tbOrder.getId()));
                            tbOrder.setTbOrderItemList(orderItems);
                            log.info("wx_notify_url tbOrder:{{}}",tbOrder);
                            if(OrderTypeEnum.VIP.getCode().equals(tbOrder.getOrderType())) {
                                iPayService.vipHandler(tbOrder);
                            }else if(OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())) {
                                iPayService.lbHandler(tbOrder);
                            }else{
                                tbOrderMapper.updateById(tbOrder);
                            }
                        }
                        // 发送通知等
                        log.info("wx_notify_url tbOrder:{{}}",tbOrder);
                        Map<String, String> xml = new HashMap<String, String>(2);
                        xml.put("return_code", "SUCCESS");
                        xml.put("return_msg", "OK");
                        return WxPayKit.toXml(xml);
                    }
                }
            }else{
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信退款查询
     */
    @RequestMapping(value = "/wxRefundQuery", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Result wxRefundQuery(@RequestParam("transactionId") String transactionId,
                              @RequestParam("out_trade_no") String outTradeNo,
                              @RequestParam("out_refund_no") String outRefundNo,
                              @RequestParam("refund_id") String refundId) {
        QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderNo", outTradeNo);
        TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
//        if(tbOrder!=null && OrderStatusEnum.REFUNDING.getCode().equals(tbOrder.getStatus())) {
        if(tbOrder!=null && StringUtils.isNotEmpty(tbOrder.getRefundNo())) {
            Map<String, String> params = RefundQueryModel.builder()
                    .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                    .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .transaction_id(transactionId)
                    .out_trade_no(outTradeNo)
                    .out_refund_no(outRefundNo)
                    .refund_id(refundId)
                    .build()
                    .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey()
                            , SignType.MD5);

            String orderRefundQueryStr = WxPayApi.orderRefundQuery(false, params);
            Map<String, String> orderRefundQueryStrParamsMap = WxPayKit.xmlToMap(orderRefundQueryStr);
            String refundQueryReturnCode = orderRefundQueryStrParamsMap.get("return_code");
            if(!WxPayKit.codeIsOk(refundQueryReturnCode)
                    ||!"SUCCESS".equals(orderRefundQueryStrParamsMap.get("result_code"))
            ||!"SUCCESS".equals(orderRefundQueryStrParamsMap.get("refund_status_0"))) {
               // tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
            }else{
                //预约类型，执行回调
                if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())){
                    CancelOrderCoachRequest coachRequest = new CancelOrderCoachRequest();
                    coachRequest.setUserId(tbOrder.getUserId());
                    List<TbOrderItem> itemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", tbOrder.getId()));
                    if (StringUtils.isNotEmpty(itemList)){
                        //此处默认一个订单只有一个商品
                        coachRequest.setCoachTimeId(itemList.get(0).getGoodsId());
                    }
                    coachOrderFeignClient.cancelOrderSuccess(coachRequest);
                }
                tbOrder.setStatus(OrderStatusEnum.REFUNDED.getCode());
                tbOrderMapper.updateById(tbOrder);
            }
            tbOrderMapper.updateById(tbOrder);

        }

       return ResultUtil.success(tbOrder);
    }

    /**
     * 微信退款通知
     */
    @RequestMapping(value = "/wx_refund_url", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String wxRefundNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("退款通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        if (WxPayKit.codeIsOk(returnCode)) {
            String reqInfo = params.get("req_info");
            String decryptData = WxPayKit.decryptData(reqInfo, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey());
            log.info("退款通知解密后的数据=" + decryptData);
            Map<String, String> info = WxPayKit.xmlToMap(decryptData);
            // 更新订单信息
            QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("orderNo", info.get("out_trade_no"));
            TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
//            if(tbOrder != null&&
//                    !OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())
//                    &&
//                    !OrderStatusEnum.REFUNDED.getCode().equals(tbOrder.getStatus()))
            if(tbOrder != null&& OrderStatusEnum.REFUNDING.getCode().equals(tbOrder.getStatus()))
            {
               if("SUCCESS".equals(info.get("refund_status"))) {
                   tbOrder.setStatus(OrderStatusEnum.REFUNDED.getCode());
                   // TODO: 直播预约类型执行回调
                   //预约类型，执行回调
                   if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())){
                       CancelOrderCoachRequest coachRequest = new CancelOrderCoachRequest();
                       coachRequest.setUserId(tbOrder.getUserId());
                       List<TbOrderItem> itemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", tbOrder.getId()));
                       if (StringUtils.isNotEmpty(itemList)){
                           //此处默认一个订单只有一个商品
                           coachRequest.setCoachTimeId(itemList.get(0).getGoodsId());
                       }
                       coachOrderFeignClient.cancelOrderSuccess(coachRequest);
                   }
               }
//               else{
//                   tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
//               }
                tbOrder.setLastModifiedDate(tbOrder.getPaymentTime());
                tbOrderMapper.updateById(tbOrder);
            }

            // 发送通知等
            Map<String, String> xml = new HashMap<String, String>(2);
            xml.put("return_code", "SUCCESS");
            xml.put("return_msg", "OK");
            return WxPayKit.toXml(xml);
        }
        return null;
    }

    @RequestMapping(value = "/notify_url", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String notifyUrl(HttpServletRequest request) {
        try {
            log.info("----------------------------------回调notify_url开始--------------------------------------------");
            log.info("notify_url request:{{}}",request);
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);
            log.info("notify_url params:{{}}",params);
            QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("orderNo", params.get("out_trade_no"));
            TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
            log.info("notify_url tbOrder:{{}}",tbOrder);
            if (tbOrder != null && (OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())||
                    OrderStatusEnum.ORDER_CLOSED.getCode().equals(tbOrder.getStatus()))) {
//                for (Map.Entry<String, String> entry : params.entrySet()) {
//                    System.out.println(entry.getKey() + " = " + entry.getValue());
//                }
                boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8", "RSA2");
                log.info("notify_url verifyResult:{{}}",verifyResult);
                if (verifyResult) {
                    // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                    String trade_status = params.get("trade_status");
                    log.info("notify_url trade_status :{{}}",trade_status);
                    if("TRADE_SUCCESS".equals(trade_status)||"TRADE_FINISHED".equals(trade_status)){
                        if(tbOrder.getAmount().equals(new BigDecimal(params.get("total_amount")))){
                            tbOrder.setPaymentType("1");
                            tbOrder.setOutOrderNo(params.get("trade_no"));
                            log.info("notify_url tbOrder.getOrderType :{{}}",tbOrder.getOrderType());
                            if(OrderTypeEnum.VIP.getCode().equals(tbOrder.getOrderType())
                                    ||OrderTypeEnum.COURSE.getCode().equals(tbOrder.getOrderType())){
                                //会员、课程购买直接完成
                                tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
                            }else{
                                tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                            }
                            //tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                            tbOrder.setPaymentTime(DateUtils.getCurrentLocalDateTime());
                            tbOrder.setLastModifiedDate(tbOrder.getPaymentTime());
                            List<TbOrderItem> orderItems = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                                    .eq("tbOrderId", tbOrder.getId()));
                            tbOrder.setTbOrderItemList(orderItems);
                            log.info("notify_url tbOrder:{{}}",tbOrder);
                            if(OrderTypeEnum.VIP.getCode().equals(tbOrder.getOrderType())) {
                                iPayService.vipHandler(tbOrder);
                            }else if(OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())) {
                                iPayService.lbHandler(tbOrder);
                            }else{
                                tbOrderMapper.updateById(tbOrder);
                            }
                        }
                    }
                    // iPayService.updateOrder();
                    log.info("---------------------------------notify_url 验证成功succcess------------------------------------------");
                    return "success";
                } else {
                    log.info("---------------------------------notify_url 验证失败failure-----------------------------------------------------");
                    // TODO
                    return "failure";
                }
            }
            return "success";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    /**
     * 支付宝退款通知
     */
    @RequestMapping(value = "/return_url", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String returnUrl(HttpServletRequest request) {
        try {
            log.info("----------------------------------回调return_url开始--------------------------------------------");
            log.info("returnUrl HttpServletRequest:{{}}",request);
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            log.info("returnUrl map:{{}}",map);
            boolean verifyResult = AlipaySignature.rsaCheckV1(map, aliPayBean.getPublicKey(), "UTF-8",
                    "RSA2");
            log.info("returnUrl verifyResult:{{}}",verifyResult);
            if (verifyResult) {
                // 更新订单信息
                QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
                queryWrapper.eq("orderNo", map.get("out_trade_no"));
                TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
//            if(tbOrder != null&&
//                    !OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())
//                    &&
//                    !OrderStatusEnum.REFUNDED.getCode().equals(tbOrder.getStatus()))
                if(tbOrder != null&& OrderStatusEnum.REFUNDING.getCode().equals(tbOrder.getStatus()))
                {
                    if("SUCCESS".equals(map.get("refund_status"))) {
                        tbOrder.setStatus(OrderStatusEnum.REFUNDED.getCode());
                        // TODO: 直播预约类型执行回调
                        //预约类型，执行回调
                        if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())){
                            CancelOrderCoachRequest coachRequest = new CancelOrderCoachRequest();
                            coachRequest.setUserId(tbOrder.getUserId());
                            List<TbOrderItem> itemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", tbOrder.getId()));
                            if (StringUtils.isNotEmpty(itemList)){
                                //此处默认一个订单只有一个商品
                                coachRequest.setCoachTimeId(itemList.get(0).getGoodsId());
                            }
                            coachOrderFeignClient.cancelOrderSuccess(coachRequest);
                        }
                    }
//               else{
//                   tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
//               }
                    tbOrder.setLastModifiedDate(tbOrder.getPaymentTime());
                    tbOrderMapper.updateById(tbOrder);
                }
                log.info("-------------------------------------notify_url 验证成功succcess--------------------------------------");
            } else {
                log.info("-------------------------------------notify_url 验证失败failure--------------------------------------");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }

        return null;
    }












}