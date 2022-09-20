package com.knd.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.*;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.RefundModel;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.Payer;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.knd.common.basic.DateUtils;
import com.knd.common.constant.Constant;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.em.PaymentTypeEnum;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.pay.dto.TbOrderDto;
import com.knd.pay.entity.*;
import com.knd.pay.mapper.TbOrderItemMapper;
import com.knd.pay.mapper.TbOrderMapper;
import com.knd.pay.mapper.UserMapper;
import com.knd.pay.request.*;
import com.knd.pay.service.IPayService;
import com.knd.pay.service.feignInterface.CoachOrderFeignClient;
import com.knd.pay.service.feignInterface.UserFeignClient;
import com.knd.pay.utils.StringUtils;
import com.knd.pay.utils.WChartUtils;
import com.knd.permission.util.RedisClientTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Slf4j
@Service
public class PayServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements IPayService {
    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private RedisClientTokenUtil redisClient;

    @Resource
    private WxPayBean wxPayBean;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private CoachOrderFeignClient coachOrderFeignClient;

    /**
     * openId过期时间
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 5 * 60;


    @Value("${domain}")
    private String domain;


    private final static String NOTIFY_URL = "/pay/pay/notify_url";

    private final static String WX_NOTIFY_URL = "/pay/pay/wx_notify_url";

    private final static String WX_REFUND_URL = "/pay/pay/wx_refund_url";

    private final static String RETURN_URL = "/pay/pay/return_url";


    @Resource
    private WxPayV3Bean wxPayV3Bean;

    @Value("${pay.order.timeOut}")
    private Integer payTimeOut;
    String serialNo;
    private final static int OK = 200;

    @Override
    public TbOrder insertReturnEntity(TbOrder entity) {
        return null;
    }

    @Override
    public TbOrder updateReturnEntity(TbOrder entity) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result tradePreCreatePay(CreateOrderRequest createOrderRequest) throws AlipayApiException {
        //    try {
        log.info("-----------------------付款开始----------------------------------");
        log.info("tradePreCreatePay createOrderRequest:{{}}", createOrderRequest);
        String notifyUrl = domain + NOTIFY_URL;
        log.info("异步通知地址是异步通知地址是异步通知地址是异步通知地址是::::::::::::::::" + notifyUrl);
        TbOrder tbOrder = new TbOrder();
        List<TbOrderItem> tbOrderItemList = new ArrayList<>();
        BeanUtils.copyProperties(createOrderRequest, tbOrder);
        //设置订单为未付款状态
        tbOrder.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
        tbOrder.setConfirmStatus("0");
        // preOrderDto.setOrderNo(tbOrder.getOrderNo());
        if (StringUtils.isEmpty(createOrderRequest.getOrderNo())) {
            tbOrder.setOrderNo(StringUtils.getOutTradeNo());
        }
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(tbOrder.getDescription());
        model.setTotalAmount(tbOrder.getAmount().toString());
        model.setStoreId("1");
        model.setTimeoutExpress(payTimeOut + "m");
        model.setOutTradeNo(tbOrder.getOrderNo());
        log.info("tradePreCreatePay model", model);
        String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
        tbOrder.setCreateDate(DateUtils.getCurrentLocalDateTime());
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        if (!"Success".equals(jsonObject.getJSONObject("alipay_trade_precreate_response").getString("msg"))) {
            //throw new CustomResultException(jsonObject.getJSONObject("alipay_trade_precreate_response").getString("sub_msg"));
            return ResultUtil.error(ResultEnum.FAIL.getCode(), jsonObject.getJSONObject("alipay_trade_precreate_response").getString("sub_msg"));
        }
        String aliQrCode = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        String wxQrCode = getWxQrCode(createOrderRequest, tbOrder);
        if ("-1".equals(wxQrCode)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "获取微信支付二维码失败");
        }
        tbOrder.setWxQrCode(wxQrCode);
//      preOrderDto.setAliQrCode(aliQrCode);
        tbOrder.setId(UUIDUtil.getShortUUID());
        tbOrder.setAliQrCode(aliQrCode);
        tbOrder.setCreateBy(createOrderRequest.getUserId());
        tbOrder.setDeleted("0");
        tbOrder.setPaymentTime(LocalDateTime.now());
        tbOrder.setPlatform(createOrderRequest.getPlatform());
        log.info("tradePreCreatePay goodsId", createOrderRequest.getGoodsRequestList().get(0).getGoodsId());
        if (StringUtils.isNotBlank(createOrderRequest.getGoodsRequestList().get(0).getGoodsId())) {
            tbOrder.setGoodsId(createOrderRequest.getGoodsRequestList().get(0).getGoodsId());
        }
//            LocalDateTime createDate = tbOrder.getCreateDate();
//            LocalDateTime payEndTime = createDate.plusMinutes(30);
        // preOrderDto.setPayRemainingTime((ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(),payEndTime))+"");
//            tbOrder.setRemainingTime((ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(),payEndTime))+"");
        if (OrderTypeEnum.GOODS.getCode().equals(createOrderRequest.getOrderType())) {
            tbOrder.setUserReceiveAddressId(createOrderRequest.getUserReceiveAddressId());
        }
        log.info("tradePreCreatePay id:{{}}", createOrderRequest.getId());
        if (createOrderRequest.getId() != null) {
            QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
            tbOrderQueryWrapper.eq("id", createOrderRequest.getId());
            tbOrderQueryWrapper.eq("deleted", "0");
            baseMapper.update(tbOrder, tbOrderQueryWrapper);
        } else {
            tbOrder.setId(UUIDUtil.getShortUUID());
            baseMapper.insert(tbOrder);
        }
        for (GoodsRequest goodsRequest : createOrderRequest.getGoodsRequestList()) {
            TbOrderItem tbOrderItem = new TbOrderItem();
            BeanUtils.copyProperties(goodsRequest, tbOrderItem);
            tbOrderItem.setTbOrderId(tbOrder.getId());
            tbOrderItemMapper.insert(tbOrderItem);
            tbOrderItemList.add(tbOrderItem);
        }
        tbOrder.setTbOrderItemList(tbOrderItemList);
        log.info("tradePreCreatePay tbOrder", tbOrder);
        log.info("-----------------------付款结束----------------------------------");
        return ResultUtil.success(tbOrder);
        //  } catch (Exception e) {
        //     e.printStackTrace();
        //     throw new CustomResultException(ResultEnum.UNKNOWN_ERROR);

        //   }
        //return ResultUtil.error(ResultEnum.FAIL.getCode(),"获取支付信息失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result tradeAppPay(CreateOrderRequest createOrderRequest) {
        try {
            log.info("---------------------------------------tradeAppPay开始------------------------------------------------------");
            log.info("tradeAppPay createOrderRequest:{{}}", createOrderRequest);
            String notifyUrl = domain + NOTIFY_URL;
            String wxNotifyUrl = domain + WX_NOTIFY_URL;
            System.out.println("异步通知地址是异步通知地址是异步通知地址是异步通知地址是::::::::::::::::" + notifyUrl);
            log.info("异步通知地址是异步通知地址是异步通知地址是异步通知地址是:notifyUrl=" + notifyUrl);
            TbOrder tbOrder = new TbOrder();
            List<TbOrderItem> tbOrderItemList = new ArrayList<>();
            BeanUtils.copyProperties(createOrderRequest, tbOrder);
            log.info("tradeAppPay tbOrder:{{}}", tbOrder);
            log.info("tradeAppPay OrderNo:{{}}", tbOrder.getOrderNo());
            if (StringUtils.isEmpty(createOrderRequest.getOrderNo())) {
                tbOrder.setOrderNo(StringUtils.getOutTradeNo());
            }
            log.info("tradeAppPay OrderNo:{{}}", tbOrder.getOrderNo());
            //设置订单为发货状态
            tbOrder.setConfirmStatus("0");
            String orderInfo = "";
            log.info("tradeAppPay PaymentType", createOrderRequest.getPaymentType());
            if ("1".equals(createOrderRequest.getPaymentType())) {
                log.info("---------------------------------------------支付宝支付开始-----------------------------------------------------------");
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(tbOrder.getDescription());
                model.setSubject(tbOrder.getDescription());
                model.setOutTradeNo(tbOrder.getOrderNo());
                model.setTimeoutExpress(payTimeOut + "m");
                model.setTotalAmount(tbOrder.getAmount().toString());
                model.setPassbackParams("callback params");
                model.setProductCode(tbOrder.getDescription());
                orderInfo = AliPayApi.appPayToResponse(model, notifyUrl).getBody();
                log.info("tradeAppPay orderInfo:{{}}", orderInfo);
                log.info("---------------------------------------------支付宝支付结束-----------------------------------------------------------");
            } else if ("2".equals(createOrderRequest.getPaymentType())) {
                log.info("-------------------------------------------微信支付开始-----------------------------------------------");
                String ip = "127.0.0.1";
                int amount = createOrderRequest.getAmount()
                        .multiply(new BigDecimal(100)).intValue();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime now = LocalDateTime.now();
                now = now.plusMinutes(payTimeOut);
                String expiresTime = dtf.format(now);
                Map<String, String> params = com.ijpay.wxpay.model.UnifiedOrderModel
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
                        .notify_url(wxNotifyUrl)
                        .trade_type(TradeType.APP.getTradeType())
                        .build()
                        .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);
                log.info("tradeAppPay params:{{}}", params);
                String xmlResult = WxPayApi.pushOrder(false, params);

                log.info("tradeAppPay xmlResult:{{}}", xmlResult);
                Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
                String returnCode = result.get("return_code");
                String returnMsg = result.get("return_msg");
                if (!WxPayKit.codeIsOk(returnCode)) {
                    return ResultUtil.error(ResultEnum.FAIL.getCode(), "获取微信支付信息失败");
                }
                String resultCode = result.get("result_code");
                if (!WxPayKit.codeIsOk(resultCode)) {
                    return ResultUtil.error(ResultEnum.FAIL.getCode(), "获取微信支付信息失败");
                }
                // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
                String prepayId = result.get("prepay_id");
                Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(WxPayApiConfigKit.getWxPayApiConfig().getAppId(), WxPayApiConfigKit.getWxPayApiConfig().getMchId(), prepayId,
                        WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);
                log.info("tradeAppPay packageParams:{{}}", packageParams);
                orderInfo = JSON.toJSONString(packageParams);
                log.info("-------------------------------------------微信支付结束-----------------------------------------------");
            }
            tbOrder.setPaymentType(createOrderRequest.getPaymentType());
            tbOrder.setCreateDate(DateUtils.getCurrentLocalDateTime());
            tbOrder.setCreateBy(createOrderRequest.getUserId());
            tbOrder.setDeleted("0");
            tbOrder.setAppPayInfo(orderInfo);
            tbOrder.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
            if (StringUtils.isNotBlank(createOrderRequest.getGoodsRequestList().get(0).getGoodsId())) {
                tbOrder.setGoodsId(createOrderRequest.getGoodsRequestList().get(0).getGoodsId());
            }
            tbOrder.setOrderCreateTime(LocalDateTime.now());
            tbOrder.setOrderExpireTime(LocalDateTime.now().plusMinutes(30));
            tbOrder.setPaymentTime(LocalDateTime.now());
            log.info("tradeAppPay tbOrder:{{}}", tbOrder);
            log.info("tradeAppPay orderCreateTime:{{}}", tbOrder.getCreateDate());
            log.info("tradeAppPay orderExpireTime:{{}}", tbOrder.getOrderExpireTime());
            if (OrderTypeEnum.GOODS.getCode().equals(createOrderRequest.getOrderType())) {
                tbOrder.setUserReceiveAddressId(createOrderRequest.getUserReceiveAddressId());
            }
            log.info("tradeAppPay id:{{}}", createOrderRequest.getId());
            if (createOrderRequest.getId() != null) {
                QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
                tbOrderQueryWrapper.eq("id", createOrderRequest.getId());
                tbOrderQueryWrapper.eq("deleted", "0");
                baseMapper.update(tbOrder, tbOrderQueryWrapper);
            } else {
                tbOrder.setId(UUIDUtil.getShortUUID());
                baseMapper.insert(tbOrder);
            }
            for (GoodsRequest goodsRequest : createOrderRequest.getGoodsRequestList()) {
                TbOrderItem tbOrderItem = new TbOrderItem();
                BeanUtils.copyProperties(goodsRequest, tbOrderItem);
                tbOrderItem.setTbOrderId(tbOrder.getId());
                tbOrderItemMapper.insert(tbOrderItem);
                tbOrderItemList.add(tbOrderItem);
            }
            //tbOrder.setTbOrderItemList(tbOrderItemList);
            log.info("tradeAppPay tbOrder:{{}}", tbOrder);
            log.info("---------------------------------------tradeAppPay结束------------------------------------------------------");
            return ResultUtil.success(tbOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomResultException(ResultEnum.UNKNOWN_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result jsApiPay(HttpServletResponse response, CreateOrderRequest createOrderRequest) throws Exception {

        //   try {
        log.info("------------------------------jsApiPay开始-----------------------------------------------");
        log.info("jsApiPay HttpServletResponse:{{}}", response);
        log.info("jsApiPay createOrderRequest:{{}}", createOrderRequest);
        TbOrder tbOrder = new TbOrder();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", createOrderRequest.getUserId());
        userQueryWrapper.eq("deleted", 0);
        User user = userMapper.selectOne(userQueryWrapper);
        log.info("jsApiPay user:{{}}", user);
        log.info("jsApiPay createOrderRequest:{{}}", createOrderRequest);
        List<TbOrderItem> tbOrderItemList = new ArrayList<>();
        BeanUtils.copyProperties(createOrderRequest, tbOrder);
        if (StringUtils.isEmpty(createOrderRequest.getOrderNo())) {
            tbOrder.setOrderNo(StringUtils.getOutTradeNo());
        }
        log.info("jsApiPay tbOrder:{{}}", tbOrder);
        //设置订单为发货状态
        tbOrder.setConfirmStatus("0");
        String orderInfo = null;
        String openid = null;
        String aliPayUrl = null;
        TbOrderDto tbOrderDto = new TbOrderDto();
        log.info("jsApiPay PaymentType:{{}}", createOrderRequest.getPaymentType());
        if ("1".equals(createOrderRequest.getPaymentType())) {
            //try {
            log.info("----------------------支付宝支付开始----------------------------");
            AliPayApiConfig aliPayApiConfig = AliPayApiConfigKit.getAliPayApiConfig();
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("app_id", aliPayApiConfig.getAppId());
            log.info("wapNoSdk appid:{{}}", aliPayApiConfig.getAppId());
            paramsMap.put("method", "alipay.trade.wap.pay");
            paramsMap.put("return_url", domain);
            //paramsMap.put("return_url", domain + RETURN_URL);
            log.info("wapNoSdk return_url:{{}}", domain);
            //log.info("wapNoSdk return_url:{{}}", domain + RETURN_URL);
            paramsMap.put("charset", aliPayApiConfig.getCharset());
            log.info("wapNoSdk charset:{{}}", aliPayApiConfig.getCharset());
            paramsMap.put("sign_type", aliPayApiConfig.getSignType());
            log.info("wapNoSdk sign_type:{{}}", aliPayApiConfig.getSignType());
            paramsMap.put("timestamp", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            paramsMap.put("version", "1.0");
            paramsMap.put("notify_url", domain + NOTIFY_URL);
            log.info("wapNoSdk notify_url:{{}}", domain + NOTIFY_URL);
            Map<String, String> bizMap = new HashMap<>();
            bizMap.put("body", "IJPay 聚合支付-H5");
            bizMap.put("subject", tbOrder.getDescription());
            bizMap.put("out_trade_no", tbOrder.getOrderNo());
            bizMap.put("total_amount", tbOrder.getAmount().toString());
            bizMap.put("product_code", "QUICK_WAP_WAY");
            paramsMap.put("biz_content", JSON.toJSONString(bizMap));
            log.info("wapNoSdk paramsMap:{{}}", paramsMap);
            log.info("wapNoSdk bizMap:{{}}", bizMap);
            String content = PayKit.createLinkString(paramsMap);
            log.info("wapNoSdk bizMap:{{}}", content);
            System.out.println(content);
            log.info("wapNoSdk PrivateKey:{{}}", aliPayApiConfig.getPrivateKey());
            String encrypt = RsaKit.encryptByPrivateKey(content, aliPayApiConfig.getPrivateKey());
            // String encrypt = AlipaySignature.rsaSign(content,aliPayApiConfig.getPrivateKey(),aliPayApiConfig.getCharset(),aliPayApiConfig.getSignType());
            log.info("wapNoSdk encrypt:{{}}", encrypt);
            System.out.println(encrypt);
            paramsMap.put("sign", encrypt);
            log.info("wapNoSdk ServiceUrl:{{}}", aliPayApiConfig.getServiceUrl());
            log.info("wapNoSdk createLinkString:{{}}", PayKit.createLinkString(paramsMap, true));
            aliPayUrl = aliPayApiConfig.getServiceUrl() + "?" + PayKit.createLinkString(paramsMap, true);
            log.info("wapNoSdk url:{{}}", aliPayUrl);
            tbOrderDto.setPaymentType(PaymentTypeEnum.ALIPAY.getCode());
            // response.sendRedirect(url);
            log.info("----------------------支付宝支付结束----------------------------");
        } else if ("2".equals(createOrderRequest.getPaymentType())) {
            //调用
            //1.code 前端传过来的
            //2.appid与appsecret 去自己小程序中查询
            log.info("jsApiPay appId:{{}}", wxPayV3Bean.getAppId());
            log.info("jsApiPay appsecret:{{}}", wxPayV3Bean.getAppSecret());
            openid = redisClient.getStr(createOrderRequest.getCode());
            log.info("jsApiPay code:{{}}", createOrderRequest.getCode());
            log.info("jsApiPay redis获取openId:{{}}", openid);
            if(StringUtils.isEmpty(openid)){
                openid = WChartUtils.getOpenid(createOrderRequest.getCode(), wxPayV3Bean.getAppId(), wxPayV3Bean.getAppSecret());
                log.info("jsApiPay 根据code获取openId:{{}}", openid);
                redisClient.set(createOrderRequest.getCode(),openid,EXPIRE_SECOND_WEB);
            }
            log.info("jsApiPay openid:{{}}", openid);
            orderInfo = jsApiPay(tbOrder, createOrderRequest, openid, tbOrder.getOrderNo());
            tbOrderDto.setPaymentType(PaymentTypeEnum.WXPAY.getCode());
        }
        log.info("jsApiPay orderInfo:{{}}", orderInfo);
        tbOrder.setPaymentType(createOrderRequest.getPaymentType());
        tbOrder.setCreateDate(DateUtils.getCurrentLocalDateTime());
        tbOrder.setCreateBy(createOrderRequest.getUserId());
        tbOrder.setDeleted("0");
        tbOrder.setAppPayInfo(orderInfo);
        tbOrder.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
        tbOrder.setPaymentTime(LocalDateTime.now());
        if (StringUtils.isNotBlank(createOrderRequest.getGoodsRequestList().get(0).getGoodsId())) {
            tbOrder.setGoodsId(createOrderRequest.getGoodsRequestList().get(0).getGoodsId());
        }
        tbOrder.setOrderCreateTime(LocalDateTime.now());
        tbOrder.setOrderExpireTime(LocalDateTime.now().plusMinutes(30));
        log.info("jsApiPay tbOrder:{{}}", tbOrder);
        log.info("jsApiPay orderCreateTime:{{}}", tbOrder.getCreateDate());
        log.info("jsApiPay orderExpireTime:{{}}", tbOrder.getOrderExpireTime());
        if (OrderTypeEnum.GOODS.getCode().equals(createOrderRequest.getOrderType())) {
            tbOrder.setUserReceiveAddressId(createOrderRequest.getUserReceiveAddressId());
        }
        if (createOrderRequest.getId() != null) {
            QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
            tbOrderQueryWrapper.eq("id", createOrderRequest.getId());
            tbOrderQueryWrapper.eq("deleted", "0");
            baseMapper.update(tbOrder, tbOrderQueryWrapper);
        } else {
            tbOrder.setId(UUIDUtil.getShortUUID());
            baseMapper.insert(tbOrder);
        }
        for (GoodsRequest goodsRequest : createOrderRequest.getGoodsRequestList()) {
            TbOrderItem tbOrderItem = new TbOrderItem();
            BeanUtils.copyProperties(goodsRequest, tbOrderItem);
            tbOrderItem.setTbOrderId(tbOrder.getId());
            tbOrderItemMapper.insert(tbOrderItem);
            tbOrderItemList.add(tbOrderItem);
        }
        log.info("jsApiPay tbOrderDto:{{}}", tbOrderDto);
        BeanUtils.copyProperties(tbOrder, tbOrderDto);
        tbOrderDto.setOpenId(openid);
        tbOrderDto.setAliPayUrl(aliPayUrl);
        //tbOrder.setTbOrderItemList(tbOrderItemList);
        log.info("jsApiPay tbOrderDto:{{}}", tbOrderDto);
        log.info("------------------------------jsApiPay结束-----------------------------------------------");
        return ResultUtil.success(tbOrderDto);
        //   } catch (Exception e) {
        ///      e.printStackTrace();
        //      throw new CustomResultException(ResultEnum.UNKNOWN_ERROR);
        //  }
    }

    public String jsApiPay(TbOrder tbOrder, CreateOrderRequest createOrderRequest, String openId, String orderNo) throws Exception {
        log.info("------------------------------jsApiPay微信支付开始----------------------------------------------");
        String ip = "127.0.0.1";
        log.info("jsApiPay createOrderRequest:{{}}", createOrderRequest);
        log.info("jsApiPay openId:{{}}", openId);
        log.info("jsApiPay orderNo:{{}}", orderNo);
        int total = (createOrderRequest.getAmount().multiply(new BigDecimal("100"))).intValue();
        log.info("jsApiPay total:{{}}", total);
        Amount amount = new Amount();
        amount.setTotal(total);
        //amount.setCurrency("CNY");
        log.info("jsApiPay amount:{{}}", amount);
        // try {
        String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
        log.info("jsApiPay timeExpire:{{}}", timeExpire);
        log.info("jsApiPay orderNo:{{}}", tbOrder.getOrderNo());
        UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                .setAppid(wxPayV3Bean.getAppId())
                .setMchid(wxPayV3Bean.getMchId())
                .setDescription("JSAPI支付")
                .setOut_trade_no(tbOrder.getOrderNo())
                .setTime_expire(timeExpire)
                .setAttach("微信系开发脚手架 https://gitee.com/javen205/TNWX")
                .setNotify_url(domain.concat(WX_NOTIFY_URL))
                .setAmount(amount)
                .setPayer(new Payer().setOpenid(openId));

        log.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
        IJPayHttpResponse response = WxPayApi.v3(
                RequestMethod.POST,
                WxDomain.CHINA.toString(),
                WxApiType.JS_API_PAY.toString(),
                wxPayV3Bean.getMchId(),
                getSerialNumber(),
                null,
                wxPayV3Bean.getKeyPath(),
                JSONUtil.toJsonStr(unifiedOrderModel)
        );

        log.info("jsApiPay 统一下单响应 {}", response);
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
        log.info("jsApiPay verifySignature: {}", verifySignature);

        if (verifySignature) {
            String body = response.getBody();
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
            String prepayId = jsonObject.getStr("prepay_id");
            Map<String, String> map = WxPayKit.jsApiCreateSign(wxPayV3Bean.getAppId(), prepayId, wxPayV3Bean.getKeyPath());
            log.info("唤起支付参数:{}", map);
            return JSONUtil.toJsonStr(map);
        }
        log.info("jsApiPay Body:{{}}", response.getBody());
        log.info("------------------------------jsApiPay微信支付结束----------------------------------------------");
        return response.getBody();

    }


    @Override
    public Result createOfficialAccountUnifiedOrder(String openid, String orderNo, BigDecimal amount) throws Exception {
        log.info("createOfficialAccountUnifiedOrder openid:{{}}", openid);
        log.info("createOfficialAccountUnifiedOrder orderNo:{{}}", orderNo);
        log.info("createOfficialAccountUnifiedOrder amount:{{}}", amount);
        log.info("createOfficialAccountUnifiedOrder appid:{{}}", wxPayV3Bean.getAppId());
        log.info("createOfficialAccountUnifiedOrder mch_id:{{}}", wxPayV3Bean.getMchId());
        String money = String.valueOf((amount.multiply(new BigDecimal("100"))).intValue());
        log.info("createOfficialAccountUnifiedOrder money:{{}}", money);
        String ip = "127.0.0.1";
        log.info("createOfficialAccountUnifiedOrder spbill_create_ip:{{}}", ip);
        log.info("createOfficialAccountUnifiedOrder notify_url:{{}}", domain + Constant.WX_NOTIFY_URL);
        log.info("createOfficialAccountUnifiedOrder openid:{{}}", openid);
        log.info("createOfficialAccountUnifiedOrder PartnerKey():{{}}", wxPayV3Bean.getPartnerKey());
        Map<String, String> params = JsApiOrderModel
                .builder()
                .appid(wxPayV3Bean.getAppId())
                .mch_id(wxPayV3Bean.getMchId())
                // 订单描述
                .nonce_str("JSAPI支付")
                .body("JSAPI支付")
                // 订单号
                .out_trade_no(orderNo)
                // 支付金额注意是以分为单位的
                .total_fee(money)
                //发起订单者ip
                .spbill_create_ip(ip)
                .notify_url(domain + Constant.WX_NOTIFY_URL)
                .trade_type(TradeType.JSAPI.getTradeType())
                .attach("微信系开发脚手架 https://gitee.com/javen205/TNWX")
                .openid(openid)
                .build()
                .createSign(wxPayV3Bean.getPartnerKey(), SignType.MD5);
        // 发送请求
        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("createOfficialAccountUnifiedOrder xmlResult:{{}}", xmlResult);
        // 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        log.info("createOfficialAccountUnifiedOrder resultMap:{{}}", resultMap);
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
        log.info("createOfficialAccountUnifiedOrder prepayId:{{}}", prepayId);
        // 二次签名，构建公众号唤起支付的参数,这里的签名方式要与上面统一下单请求签名方式保持一致
        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId,
                wxPayV3Bean.getAppId(), wxPayV3Bean.getPartnerKey(), SignType.MD5);
        log.info("createOfficialAccountUnifiedOrder packageParams:{{}}", packageParams);
        // 将二次签名构建的数据返回给前端并唤起公众号支付
        String jsonStr = JSON.toJSONString(packageParams);
        return ResultUtil.success(jsonStr);
    }


/*
    String schema = "WECHATPAY2-SHA256-RSA2048";
    HttpUrl httpurl = HttpUrl.parse(url);

    String getToken(String method, HttpUrl url, String body) throws UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException {
        String nonceStr = "your nonce string";
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes("utf-8"));

        return "mchid=\"" + yourMerchantId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + yourCertificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }

    String sign(byte[] message) throws NoSuchAlgorithmException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(yourPrivateKey);
        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }

        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }*/


    private String getWxQrCode(CreateOrderRequest createOrderRequest, TbOrder tbOrder) {

        String ip = "127.0.0.1";

        String wxNotifyUrl = domain + WX_NOTIFY_URL;
        int amount = createOrderRequest.getAmount()
                .multiply(new BigDecimal(100)).intValue();


        LocalDateTime now = LocalDateTime.now();
        now = now.plusMinutes(payTimeOut);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String expiresTime = dtf.format(now);
        Map<String, String> params = com.ijpay.wxpay.model.UnifiedOrderModel
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
                .notify_url(wxNotifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.debug("统一下单:" + xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        System.out.println(returnMsg);
        if (!WxPayKit.codeIsOk(returnCode)) {
            return "-1";
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return "-1";
        }
        //生成预付订单success

        return result.get("code_url");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result tradeQuery(String outTradeNo, String tradeNo) {
        try {
            log.info("tradeQuery outTradeNo:{{]}", outTradeNo);
            log.info("tradeQuery tradeNo:{{]}", tradeNo);
            QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("orderNo", tradeNo);
            TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
            if (tbOrder != null) {
                if (OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())) {

                    Map<String, String> params = OrderQueryModel.builder()
                            .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                            .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                            .out_trade_no(tradeNo)
                            .nonce_str(WxPayKit.generateStr())
                            .build()
                            .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.MD5);
//                        log.debug("请求参数："+ WxPayKit.toXml(params));
                    String query = WxPayApi.orderQuery(params);
                    log.debug("查询结果: " + query);
                    Map<String, String> paramsMap = WxPayKit.xmlToMap(query);
                    String returnCode = paramsMap.get("return_code");
                    if (!WxPayKit.codeIsOk(returnCode)) {
//                            return ResultUtil.success(tbOrder);
                        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
                        if (StringUtils.isNotEmpty(tradeNo)) {
                            model.setOutTradeNo(tradeNo);
                        }
                        if (StringUtils.isNotEmpty(outTradeNo)) {
                            model.setTradeNo(outTradeNo);
                        }
                        String resultStr = AliPayApi.tradeQueryToResponse(model).getBody();
                        JSONObject jsonObject = JSONObject.parseObject(resultStr);
                        if (!"Success".equals(jsonObject.getJSONObject("alipay_trade_query_response").getString("msg"))) {
                            //return ResultUtil.error(ResultEnum.FAIL.getCode(), jsonObject.getJSONObject("alipay_trade_query_response").getString("sub_msg"));
                            return ResultUtil.success(tbOrder);
                        }
                        String tradeStatus = jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_status");
                        tbOrder.setPaymentType("1");
                        log.info("tradeQuery tradeStatus:{{]}", tradeStatus);
                        switch (tradeStatus) {
                            case "TRADE_SUCCESS":
                                BigDecimal totalAmount = new BigDecimal(
                                        jsonObject.getJSONObject("alipay_trade_query_response").getString("total_amount"));
                                if (totalAmount.equals(tbOrder.getAmount())) {
                                    if ("1".equals(tbOrder.getOrderType()) || "3".equals(tbOrder.getOrderType())) {
                                        //会员、课程购买直接完成
                                        tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
                                    } else {
                                        tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                                    }

                                    tbOrder.setPaymentTime(DateUtils.getCurrentLocalDateTime());
                                    tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
                                    tbOrder.setLastModifiedDate(tbOrder.getPaymentTime());
                                    List<TbOrderItem> orderItems = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                                            .eq("tbOrderId", tbOrder.getId()));
                                    tbOrder.setTbOrderItemList(orderItems);
                                    log.info("tradeQuery tbOrder:{{]}", tbOrder);
                                    if (OrderTypeEnum.VIP.getCode().equals(tbOrder.getOrderType())) {
                                        this.vipHandler(tbOrder);
                                    } else if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())) {
                                        this.lbHandler(tbOrder);
                                    } else {
                                        tbOrderMapper.updateById(tbOrder);
                                    }
                                }
                                break;
                            case "TRADE_CLOSED":
                                tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
                                log.info("tradeQuery tbOrder:{{}}", tbOrder);
                                tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
                                tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
                                log.info("tradeQuery tbOrder:{{]}", tbOrder);
                                tbOrderMapper.updateById(tbOrder);
                                break;
//                        case "TRADE_FINISHED":
//                            tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
//                            tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
//                            tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                            tbOrderMapper.updateById(tbOrder);
//                            break;
                            default:
                                break;
                        }
                    }
                    String tradeState = paramsMap.get("trade_state");
                    tbOrder.setPaymentType("2");
                    log.info("tradeQuery tradeState:{{]}", tradeState);
                    switch (tradeState) {
                        case "SUCCESS":
                            BigDecimal totalAmount = new BigDecimal(
                                    paramsMap.get("total_fee"));
                            if (totalAmount.intValue() == tbOrder.getAmount().multiply(new BigDecimal(100)).intValue()) {
                                if (OrderTypeEnum.VIP.getCode().equals(tbOrder.getOrderType())
                                        || OrderTypeEnum.COURSE.getCode().equals(tbOrder.getOrderType())) {
                                    //会员、课程购买直接完成
                                    tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
                                } else {
                                    tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                                }
                                //tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                                tbOrder.setPaymentTime(DateUtils.getCurrentLocalDateTime());
                                tbOrder.setOutOrderNo(paramsMap.get("transaction_id"));
                                tbOrder.setLastModifiedDate(tbOrder.getPaymentTime());
                                List<TbOrderItem> orderItems = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                                        .eq("tbOrderId", tbOrder.getId()));
                                tbOrder.setTbOrderItemList(orderItems);
                                log.info("tradeQuery tbOrder:{{]}", tbOrder);
                                if (OrderTypeEnum.VIP.getCode().equals(tbOrder.getOrderType())) {
                                    this.vipHandler(tbOrder);
                                } else if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())) {
                                    this.lbHandler(tbOrder);
                                } else {
                                    tbOrderMapper.updateById(tbOrder);
                                }
                            }
                            break;
                        case "CLOSED":
                            tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
                            log.info("tradeQuery tbOrder:{{}}", tbOrder);
                            //tbOrder.setOutOrderNo(paramsMap.get("transaction_id"));
                            tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
                            log.info("tradeQuery tbOrder:{{]}", tbOrder);
                            tbOrderMapper.updateById(tbOrder);
                            break;
//                        case "TRADE_FINISHED":
//                            tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
//                            tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
//                            tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                            tbOrderMapper.updateById(tbOrder);
//                            break;
                        default:
                            break;
                    }

//                        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
//                        if (StringUtils.isNotEmpty(tradeNo)) {
//                            model.setOutTradeNo(tradeNo);
//                        }
//                        if (StringUtils.isNotEmpty(outTradeNo)) {
//                            model.setTradeNo(outTradeNo);
//                        }
//                        String resultStr = AliPayApi.tradeQueryToResponse(model).getBody();
//                        JSONObject jsonObject = JSONObject.parseObject(resultStr);
//                        if(!"Success".equals(jsonObject.getJSONObject("alipay_trade_query_response").getString("msg"))) {
//                            return ResultUtil.success(tbOrder);
//                        }
//                        String tradeStatus = jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_status");
//                        switch (tradeStatus) {
//                            case "TRADE_SUCCESS":
//                                BigDecimal totalAmount = new BigDecimal(
//                                        jsonObject.getJSONObject("alipay_trade_query_response").getString("total_amount"));
//                                if(totalAmount.equals(tbOrder.getAmount())){
//                                    tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
//                                    tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
//                                    tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                                    if("1".equals(tbOrder.getGoodsType())) {
//                                        this.vipHandler(tbOrder);
//                                    }else{
//                                        tbOrderMapper.updateById(tbOrder);
//                                    }
//                                }
//                                break;
//                            case "TRADE_CLOSED":
//                                tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
//                                tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
//                                tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                                tbOrderMapper.updateById(tbOrder);
//                                break;
//                            default:
//                                break;
//                        }


                    //交易超时关闭
//                    if("TRADE_CLOSED".equals(tradeStatus)) {
//                        tbOrder.setStatus("3");
//                        tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                        tbOrderMapper.updateById(tbOrder);
//                    }
//                    //交易完成
//                    if("TRADE_FINISHED".equals(tradeStatus)) {
//                        tbOrder.setStatus("4");
//                        tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                        tbOrderMapper.updateById(tbOrder);
//                    }
//                    BigDecimal totalAmount = new BigDecimal(jsonObject.getJSONObject("alipay_trade_query_response").getString("total_amount"));
//
//                    if("TRADE_SUCCESS".equals(tradeStatus)&&totalAmount.equals(tbOrder.getAmount())) {
//                        tbOrder.setStatus("2");
//                        tbOrder.setOutOrderNo(jsonObject.getJSONObject("alipay_trade_query_response").getString("trade_no"));
//                        tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
//                        tbOrderMapper.updateById(tbOrder);
//                    }

                    return ResultUtil.success(tbOrder);
                } else {
                    return ResultUtil.success(tbOrder);
                }
            } else {
                return ResultUtil.error("U0995", "订单号不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomResultException(ResultEnum.UNKNOWN_ERROR);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void vipHandler(TbOrder tbOrder) {
        tbOrderMapper.updateById(tbOrder);
        ChangeVipTypeRequest changeVipTypeRequest = new ChangeVipTypeRequest();
        changeVipTypeRequest.setUserId(tbOrder.getUserId());
        changeVipTypeRequest.setVipMenuId(tbOrder.getTbOrderItemList().get(0).getGoodsId());
        userFeignClient.changeVipType(changeVipTypeRequest);
    }

    @Override
    public void closeOvertimeOrderBatch() {
        QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", OrderStatusEnum.WAIT_FOR_PAY.getCode());
        queryWrapper.lt("createDate", DateUtils.getCurrentLocalDateTime().minusMinutes(payTimeOut));
        queryWrapper.orderByDesc("createDate");
        List<TbOrder> tbOrderList = tbOrderMapper.selectList(queryWrapper);
        log.info("当前检测到" + tbOrderList.size() + "条超时订单");
        tbOrderList.stream().forEach(e -> {
            //this.tradeClose(e,null,null);
            e.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
            log.info("closeOvertimeOrderBatch tbOrder:{{}}", e);
            LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
            e.setLastModifiedDate(currentLocalDateTime);
            e.setCancelTime(currentLocalDateTime);
            tbOrderMapper.updateById(e);
            //如果是直播类型，更新直播预约记录状态是取消 todo
            //预约类型，执行回调
            if (OrderTypeEnum.LIVE.getCode().equals(e.getOrderType())) {
                CancelOrderCoachRequest coachRequest = new CancelOrderCoachRequest();
                coachRequest.setUserId(e.getUserId());
                List<TbOrderItem> itemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", e.getId()));
                if (com.knd.common.basic.StringUtils.isNotEmpty(itemList)) {
                    //此处默认一个订单只有一个商品
                    coachRequest.setCoachTimeId(itemList.get(0).getGoodsId());
                }
                coachOrderFeignClient.cancelOrderSuccess(coachRequest);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result refund(String outTradeNo, String tradeNo) {
        log.info("refund outTradeNo:{{}}", outTradeNo);
        log.info("refund tradeNo:{{}}", tradeNo);
        try {
            QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("orderNo", tradeNo);
            TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
            log.info("refund tbOrder:{{}}", tbOrder);
            if (tbOrder != null) {
//                if(OrderStatusEnum.PAY_SUCCESS.getCode().equals(tbOrder.getStatus())
//                ||OrderStatusEnum.DELIVER_FINISHED.getCode().equals(tbOrder.getStatus())
//                ||OrderStatusEnum.ORDER_FINISHED.getCode().equals(tbOrder.getStatus()))
                if (OrderStatusEnum.REFUNDING.getCode().equals(tbOrder.getStatus())) {
                    LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
                    if ("2".equals(tbOrder.getPaymentType())) {
                        //微信
                        //商户内部退款单号
                        String outRefundNo = WxPayKit.generateStr();
                        tbOrder.setRefundNo(outRefundNo);
                        Map<String, String> params = RefundModel.builder()
                                .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                                .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                                .nonce_str(WxPayKit.generateStr())
                                .transaction_id(outTradeNo)
                                .out_trade_no(tradeNo)
                                .out_refund_no(outRefundNo)
                                .total_fee(tbOrder.getAmount().multiply(new BigDecimal(100)).intValue() + "")
                                .refund_fee(tbOrder.getAmount().multiply(new BigDecimal(100)).intValue() + "")
                                .notify_url(domain + WX_REFUND_URL)
                                .build()
                                .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.MD5);
                        // String refundStr = WxPayApi.orderRefundByProtocol(false, params, WxPayApiConfigKit.getWxPayApiConfig().getCertPath(), WxPayApiConfigKit.getWxPayApiConfig().getMchId(),"");
                        //微信退款协议升级使用新的指定接口
                        //证书路径调整为绝对路径
                        String refundStr = WxPayApi.orderRefundByProtocol(false, params, FileUtil.getInputStream(WxPayApiConfigKit.getWxPayApiConfig().getCertPath()), WxPayApiConfigKit.getWxPayApiConfig().getMchId(), "");
                        Map<String, String> paramsMap = WxPayKit.xmlToMap(refundStr);
                        String returnCode = paramsMap.get("return_code");
                        if (WxPayKit.codeIsOk(returnCode)) {
                            String result_code = paramsMap.get("result_code");
                            if ("SUCCESS".equals(result_code)) {
                                tbOrder.setOutRefundNo(paramsMap.get("refund_id"));
                                //tbOrder.setStatus(OrderStatusEnum.REFUNDING.getCode());
//                                Map<String, String> refundQueryParams = RefundQueryModel.builder()
//                                        .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
//                                        .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
//                                        .nonce_str(WxPayKit.generateStr())
//                                        .transaction_id(outTradeNo)
//                                        .out_trade_no(tradeNo)
//                                        .out_refund_no(outRefundNo)
//                                        .refund_id(paramsMap.get("refund_id"))
//                                        .build()
//                                        .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.MD5);
//
//                                String orderRefundQueryStr = WxPayApi.orderRefundQuery(false, refundQueryParams);
//                                Map<String, String> orderRefundQueryStrParamsMap = WxPayKit.xmlToMap(orderRefundQueryStr);
//                                String refundQueryReturnCode = orderRefundQueryStrParamsMap.get("return_code");
//                                if (WxPayKit.codeIsOk(refundQueryReturnCode)) {
//                                    String refundQueryResultCode = orderRefundQueryStrParamsMap.get("result_code");
//                                    if("SUCCESS".equals(refundQueryResultCode)) {
//                                    }else{
//
//                                    }
//                                }else{
//
//                                }
                            } else {
                                return ResultUtil.error(ResultEnum.FAIL.getCode(), "微信退款申请失败");
                            }
                        } else {
                            return ResultUtil.error(ResultEnum.FAIL.getCode(), "微信退款申请失败");
                        }


                    } else {
                        //支付宝
                        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                        if (StringUtils.isNotEmpty(tradeNo)) {
                            model.setOutTradeNo(tradeNo);
                        }
                        if (StringUtils.isNotEmpty(outTradeNo)) {
                            model.setTradeNo(outTradeNo);
                        }
                        model.setRefundAmount(tbOrder.getAmount().toString());
                        model.setRefundReason("正常退款");
                        String resultStr = AliPayApi.tradeRefundToResponse(model).getBody();
                        log.trace("订单" + tradeNo + "发起退款=》" + resultStr);
                        JSONObject jsonObject = JSONObject.parseObject(resultStr);

                        if (!"Success".equals(jsonObject.getJSONObject("alipay_trade_refund_response").getString("msg"))) {
                            return ResultUtil.error(ResultEnum.FAIL.getCode(), "支付宝退款失败");
                        }
                        tbOrder.setStatus(OrderStatusEnum.REFUNDED.getCode());
                        // TODO: 支付宝得直播预约退款回调
                        //预约类型，执行回调
                        if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())) {
                            CancelOrderCoachRequest coachRequest = new CancelOrderCoachRequest();
                            coachRequest.setUserId(tbOrder.getUserId());
                            List<TbOrderItem> itemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", tbOrder.getId()));
                            if (com.knd.common.basic.StringUtils.isNotEmpty(itemList)) {
                                //此处默认一个订单只有一个商品
                                coachRequest.setCoachTimeId(itemList.get(0).getGoodsId());
                            }
                            coachOrderFeignClient.cancelOrderSuccess(coachRequest);
                        }

                    }
                    tbOrder.setRefundTime(currentLocalDateTime);
                    tbOrder.setLastModifiedDate(currentLocalDateTime);
                    tbOrderMapper.updateById(tbOrder);
                } else {
                    return ResultUtil.error("U0995", "该订单状态无法申请退款");
                }
            } else {
                return ResultUtil.error("U0995", "无法查询到订单信息");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    @Override
    public Result tradeClose(TbOrder tbOrder, String outTradeNo, String tradeNo) {
        try {
            if (tbOrder == null) {
                QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
                queryWrapper.eq("orderNo", tradeNo);
                tbOrder = tbOrderMapper.selectOne(queryWrapper);
            }

            //必须是在未支付情况下才可取消关闭订单
            if (tbOrder != null && OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())) {
                LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
                if ("2".equals(tbOrder.getPaymentType())) {
                    //微信订单
                    Map<String, String> params = OrderQueryModel.builder()
                            .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                            .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                            .out_trade_no(tbOrder.getOrderNo())
                            .nonce_str(WxPayKit.generateStr())
                            .build()
                            .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.MD5);
                    log.trace("微信关单请求参数:" + WxPayKit.toXml(params));
                    String query = WxPayApi.closeOrder(params);
                    log.trace("微信订单" + tbOrder.getOrderNo() + "发起关闭=》" + query);
                    Map<String, String> paramsMap = WxPayKit.xmlToMap(query);
                    if (!WxPayKit.codeIsOk(paramsMap.get("return_code"))
                            || !"SUCCESS".equals(paramsMap.get("result_code"))) {
                        return ResultUtil.error(ResultEnum.FAIL.getCode(), "微信关闭订单失败");
                    }
                    tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
                    log.info("tradeClose tbOrder:{{}}", tbOrder);
                    tbOrder.setCancelTime(currentLocalDateTime);

                } else {
                    //支付宝订单
                    AlipayTradeCloseModel model = new AlipayTradeCloseModel();
                    model.setOutTradeNo(tbOrder.getOrderNo());

                    String resultStr = AliPayApi.tradeCloseToResponse(model).getBody();
                    log.trace("支付宝订单" + tbOrder.getOrderNo() + "发起关闭=》" + resultStr);
                    JSONObject jsonObject = JSONObject.parseObject(resultStr);
                    if (!"Success".equals(jsonObject.getJSONObject("alipay_trade_close_response").getString("msg"))) {
                        return ResultUtil.error(ResultEnum.FAIL.getCode(), "支付宝关闭订单失败");
                    }
                    tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
                    log.info("tradeClose tbOrder:{{}}", tbOrder);
                    tbOrder.setCancelTime(currentLocalDateTime);
                }

                tbOrderMapper.updateById(tbOrder);
                //预约类型，执行回调
                if (OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())) {
                    CancelOrderCoachRequest coachRequest = new CancelOrderCoachRequest();
                    coachRequest.setUserId(tbOrder.getUserId());
                    List<TbOrderItem> itemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", tbOrder.getId()));
                    if (com.knd.common.basic.StringUtils.isNotEmpty(itemList)) {
                        //此处默认一个订单只有一个商品
                        coachRequest.setCoachTimeId(itemList.get(0).getGoodsId());
                    }
                    coachOrderFeignClient.cancelOrderSuccess(coachRequest);
                }
            }
            return ResultUtil.success(tbOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success(tbOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lbHandler(TbOrder tbOrder) {
        //1、变更订单状态
        tbOrderMapper.updateById(tbOrder);

        //2、创建预约记录
        OrderCoachRequest request = new OrderCoachRequest();

        List<TbOrderItem> TbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", tbOrder.getId()));

        request.setUserId(tbOrder.getUserId());
        request.setOrderId(tbOrder.getId());
        request.setCoachTimeId(TbOrderItemList.get(0).getGoodsId());
        coachOrderFeignClient.orderSuccess(request);
    }


    public Result wxH5Pay(HttpServletRequest request, HttpServletResponse response, CreateOrderRequest createOrderRequest, TbOrder tbOrder) throws IOException {
        log.info("wxH5Pay ");
        String ip = "127.0.0.1";
        String wxNotifyUrl = domain + WX_NOTIFY_URL;
        int amount = createOrderRequest.getAmount()
                .multiply(new BigDecimal(100)).intValue();
        H5SceneInfo sceneInfo = new H5SceneInfo();
        H5SceneInfo.H5 h5_info = new H5SceneInfo.H5();
        h5_info.setType("Wap");
        //此域名必须在商户平台--"产品中心"--"开发配置"中添加
        // h5_info.setWap_url("https://gitee.com/javen205/IJPay");
        h5_info.setWap_url("http://www.quinnoid.com");
        h5_info.setWap_name("IJPay VIP 充值");
        sceneInfo.setH5Info(h5_info);

        Map<String, String> params = com.ijpay.wxpay.model.UnifiedOrderModel
                .builder()
                .appid(WxPayApiConfigKit.getWxPayApiConfig().getAppId())
                .mch_id(WxPayApiConfigKit.getWxPayApiConfig().getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(createOrderRequest.getDescription())
                .attach("123")
                .out_trade_no(tbOrder.getOrderNo())
                .total_fee(amount + "")
                .spbill_create_ip(ip)
                .notify_url(wxNotifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .scene_info(JSON.toJSONString(sceneInfo))
                .build()
                .createSign(WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(return_code)) {
            throw new RuntimeException(return_msg);
        }
        String result_code = result.get("result_code");
        if (!WxPayKit.codeIsOk(result_code)) {
            throw new RuntimeException(return_msg);
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepayId = result.get("prepay_id");
        String webUrl = result.get("mweb_url");

        log.info("prepay_id:" + prepayId + " mweb_url:" + webUrl);
        response.sendRedirect(webUrl);
        return ResultUtil.success(response);
    }




    /**
     * 微信H5 支付
     * 注意：必须再web页面中发起支付且域名已添加到开发配置中
     */
    @Override
    public Result wapPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = IpKit.getRealIp(request);
        if (StrUtil.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        String wxNotifyUrl = domain + WX_NOTIFY_URL;
        H5SceneInfo sceneInfo = new H5SceneInfo();

        H5SceneInfo.H5 h5_info = new H5SceneInfo.H5();
        h5_info.setType("Wap");
        //此域名必须在商户平台--"产品中心"--"开发配置"中添加
        h5_info.setWap_url("http://www.quinnoid.com");
        h5_info.setWap_name("IJPay VIP 充值");
        sceneInfo.setH5Info(h5_info);

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = com.ijpay.wxpay.model.UnifiedOrderModel
                .builder()
                .appid(wxPayV3Bean.getAppId())
                .mch_id(wxPayV3Bean.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-H5支付")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(StringUtils.getOutTradeNo())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(wxNotifyUrl)
                .trade_type(TradeType.MWEB.getTradeType())
                .scene_info(JSON.toJSONString(sceneInfo))
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(return_code)) {
            throw new RuntimeException(return_msg);
        }
        String result_code = result.get("result_code");
        if (!WxPayKit.codeIsOk(result_code)) {
            throw new RuntimeException(return_msg);
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepayId = result.get("prepay_id");
        String webUrl = result.get("mweb_url");

        log.info("prepay_id:" + prepayId + " mweb_url:" + webUrl);
        //  response.sendRedirect(webUrl);
        return ResultUtil.success(webUrl);
    }

    /**
     * 小程序支付
     * 注意：必须再web页面中发起支付且域名已添加到开发配置中
     */

    @Override
    public Result smallRoutinePay(CreateOrderRequest createOrderRequest) {
        log.info("------------------------------smallRoutinePay开始-----------------------------------------------");
        TbOrder tbOrder = new TbOrder();
        BeanUtils.copyProperties(createOrderRequest, tbOrder);
        if (StringUtils.isEmpty(createOrderRequest.getOrderNo())) {
            createOrderRequest.setOrderNo(StringUtils.getOutTradeNo());
        }
        log.info("smallRoutinePay tbOrder:{{}}", tbOrder);
        log.info("smallRoutinePay createOrderRequest:{{}}", createOrderRequest);
        String ip = "127.0.0.1";
        String money = String.valueOf((createOrderRequest.getAmount().multiply(new BigDecimal("100"))).intValue());
        log.info("smallRoutinePay money:{{}}", money);
        TbOrderDto tbOrderDto = new TbOrderDto();
        String orderInfo = null;
        if ("2".equals(createOrderRequest.getPaymentType())) {
            Map<String, String> params = JsApiOrderModel
                    .builder()
                    .appid(wxPayBean.getSmallRoutineappId())
                    .mch_id(wxPayBean.getMchId())
                    // 订单描述
                    .nonce_str(WxPayKit.generateStr())
                    .body("IJPay 让支付触手可及-小程序支付")
                    // 订单号
                    .out_trade_no(createOrderRequest.getOrderNo())
                    // 支付金额注意是以分为单位的
                    .total_fee(money)
                    //发起订单者ip
                    .spbill_create_ip(ip)
                    .notify_url(WX_NOTIFY_URL)
                    .trade_type(TradeType.JSAPI.getTradeType())
                    .openid(createOrderRequest.getOpenId())
                    .build()
                    .createSign(wxPayBean.getPartnerKey(), SignType.MD5);
            //进行数据加密封装
            String xmlResult = WxPayApi.pushOrder(false, params);
            Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
            log.info("smallRoutinePay resultMap:{{}}", resultMap);
            Map<String, String> packageParams = null;

            if (StringUtils.equals(resultMap.get("return_code"), "SUCCESS")) {
                if (StringUtils.isNotEmpty(resultMap.get("err_code_des"))) {
                    throw new CustomResultException(ResultEnum.FAIL, resultMap.get("err_code_des"));
                }
                String prepayId = resultMap.get("prepay_id");
                log.info("smallRoutinePay prepayId:{{}}", prepayId);
                packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayBean.getSmallRoutineappId(),
                        wxPayBean.getPartnerKey(), SignType.MD5);
                orderInfo = JSON.toJSONString(packageParams);
                // 微信浏览器中拿此参数调起支付
                //    return ResultUtil.success(packageParams);

            }
        }
        log.info("smallRoutinePay orderInfo:{{}}", orderInfo);
        List<TbOrderItem> tbOrderItemList = new ArrayList<>();
        BeanUtils.copyProperties(createOrderRequest, tbOrder);
        log.info("smallRoutinePay tbOrder:{{}}", tbOrder);
        tbOrder.setAppPayInfo(orderInfo);
        //设置订单为发货状态
        tbOrder.setConfirmStatus("0");
        tbOrder.setPaymentType(createOrderRequest.getPaymentType());
        tbOrder.setPaymentTime(LocalDateTime.now());
        tbOrder.setDeleted("0");
        tbOrder.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
        tbOrder.setPlatform(createOrderRequest.getPlatform());
        if (StringUtils.isNotBlank(createOrderRequest.getGoodsRequestList().get(0).getGoodsId())) {
            tbOrder.setGoodsId(createOrderRequest.getGoodsRequestList().get(0).getGoodsId());
        }
        tbOrder.setOrderExpireTime(LocalDateTime.now().plusMinutes(30));
        tbOrder.setOrderCreateTime(LocalDateTime.now());
        log.info("smallRoutinePay tbOrder:{{}}", tbOrder);
        log.info("smallRoutinePay orderCreateTime:{{}}", tbOrder.getOrderCreateTime());
        log.info("smallRoutinePay orderExpireTime:{{}}", tbOrder.getOrderExpireTime());
        if (OrderTypeEnum.GOODS.getCode().equals(createOrderRequest.getOrderType())) {
            tbOrder.setUserReceiveAddressId(createOrderRequest.getUserReceiveAddressId());
        }
        if (createOrderRequest.getId() != null) {
            QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
            tbOrderQueryWrapper.eq("id", createOrderRequest.getId());
            tbOrderQueryWrapper.eq("deleted", "0");
            tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
            tbOrder.setLastModifiedBy(createOrderRequest.getUserId());
            baseMapper.update(tbOrder, tbOrderQueryWrapper);
        } else {
            tbOrder.setId(UUIDUtil.getShortUUID());
            tbOrder.setCreateDate(DateUtils.getCurrentLocalDateTime());
            tbOrder.setCreateBy(createOrderRequest.getUserId());
            tbOrder.setOrderCreateTime(LocalDateTime.now());
            log.info("smallRoutinePay tbOrder:{{}}", tbOrder);
            baseMapper.insert(tbOrder);
        }
        for (GoodsRequest goodsRequest : createOrderRequest.getGoodsRequestList()) {
            TbOrderItem tbOrderItem = new TbOrderItem();
            BeanUtils.copyProperties(goodsRequest, tbOrderItem);
            tbOrderItem.setTbOrderId(tbOrder.getId());
            tbOrderItemMapper.insert(tbOrderItem);
            tbOrderItemList.add(tbOrderItem);
        }
        log.info("smallRoutinePay tbOrderDto:{{}}", tbOrderDto);
        BeanUtils.copyProperties(tbOrder, tbOrderDto);
        //tbOrder.setTbOrderItemList(tbOrderItemList);
        log.info("smallRoutinePay tbOrderDto:{{}}", tbOrderDto);
        log.info("------------------------------smallRoutinePay结束-----------------------------------------------");
        return ResultUtil.success(tbOrderDto);
    }

    /**
     * 微信回调接口
     */
    @Override
    public Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest) throws ParseException {
        log.info("parseOrderNotifyResult parseOrderNotifyRequest:{{}}", parseOrderNotifyRequest);
        Map<String, String> map = new HashMap<>();
        System.out.println("微信回调开始了");
        log.info("--------------------------------微信回调开始了---------------------------");
        //这里我只进行了部分回调信息的保存，更多参数请查看微信支付官方api:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8

        /*----------------------分割线，下面的是业务代码，根据自己业务进行修改--------------------------*/
        //订单成功业务处理
        if ("ok".equals(parseOrderNotifyRequest.getResultCode())) {
            //订单号
            String orderNo = parseOrderNotifyRequest.getOrderNo();
            QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
            tbOrderQueryWrapper.eq("orderNo", orderNo);
            tbOrderQueryWrapper.eq("deleted", 0);
            TbOrder order = tbOrderMapper.selectOne(tbOrderQueryWrapper);
            log.info("parseOrderNotifyResult order:{{}}", order);
            if (null != order) {
                order.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                order.setLastModifiedDate(LocalDateTime.now());
                tbOrderMapper.updateById(order);
            }
        }
        /*----------------------分割线，业务结束--------------------------*/

        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        log.info("parseOrderNotifyResult map:{{}}", map);
        return ResultUtil.success(map);
    }

    /**
     * 支付宝回调接口
     */
    @Override
    public Result alipayCallback(String outBizNo, String orderId) {
        log.info("transferQuery outBizNo:{{}}", outBizNo);
        log.info("transferQuery orderId:{{}}", orderId);
        AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
        if (StringUtils.isNotEmpty(outBizNo)) {
            model.setOutBizNo(outBizNo);
        }
        if (StringUtils.isNotEmpty(orderId)) {
            model.setOrderId(orderId);
        }
        String body = null;
        try {
            body = AliPayApi.transferQueryToResponse(model).getBody();
            log.info("transferQuery body:{{}}", body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        System.out.println("微信回调开始了");
        log.info("--------------------------------微信回调开始了---------------------------");
        //这里我只进行了部分回调信息的保存，更多参数请查看微信支付官方api:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8

        /*----------------------分割线，下面的是业务代码，根据自己业务进行修改--------------------------*/
        //订单成功业务处理
        if ("ok".equals(body)) {
            //订单号
            QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
            tbOrderQueryWrapper.eq("orderNo", orderId);
            tbOrderQueryWrapper.eq("deleted", 0);
            TbOrder order = tbOrderMapper.selectOne(tbOrderQueryWrapper);
            if (null != order) {
                order.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                order.setLastModifiedDate(LocalDateTime.now());
                tbOrderMapper.updateById(order);
            }
        }
        /*----------------------分割线，业务结束--------------------------*/

        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        return ResultUtil.success(map);
    }


    @Override
    public Result v3Get() {
        // 获取平台证书列表
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomain.CHINA.toString(),
                    WxApiType.GET_CERTIFICATES.toString(),
                    wxPayV3Bean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Bean.getKeyPath(),
                    ""
            );


            String timestamp = response.getHeader("Wechatpay-Timestamp");
            String nonceStr = response.getHeader("Wechatpay-Nonce");
            String serialNumber = response.getHeader("Wechatpay-Serial");
            String signature = response.getHeader("Wechatpay-Signature");

            String body = response.getBody();
            int status = response.getStatus();

            log.info("serialNumber: {}", serialNumber);
            log.info("status: {}", status);
            log.info("body: {}", body);
            int isOk = 200;
            if (status == isOk) {
                cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                // 默认认为只有一个平台证书
                cn.hutool.json.JSONObject encryptObject = dataArray.getJSONObject(0);
                cn.hutool.json.JSONObject encryptCertificate = encryptObject.getJSONObject("encrypt_certificate");
                String associatedData = encryptCertificate.getStr("associated_data");
                String cipherText = encryptCertificate.getStr("ciphertext");
                String nonce = encryptCertificate.getStr("nonce");
                String serialNo = encryptObject.getStr("serial_no");
                /*            final String platSerialNo = savePlatformCert(associatedData, nonce, cipherText, wxPayV3Bean.getPlatformCertPath());*/
                /*        log.info("平台证书序列号: {} serialNo: {}", platSerialNo, serialNo);*/
            }
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Bean.getPlatformCertPath());
            System.out.println("verifySignature:" + verifySignature);
            return ResultUtil.success(body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String savePlatformCert(String associatedData, String nonce, String cipherText, String certPath) {
        try {
            AesUtil aesUtil = new AesUtil(wxPayV3Bean.getApiKey3().getBytes(StandardCharsets.UTF_8));
            // 平台证书密文解密
            // encrypt_certificate 中的  associated_data nonce  ciphertext
            String publicKey = aesUtil.decryptToString(
                    associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8),
                    cipherText
            );
            // 保存证书
            FileWriter writer = new FileWriter(certPath);
            writer.write(publicKey);
            // 获取平台证书序列号
            X509Certificate certificate = PayKit.getCertificate(new ByteArrayInputStream(publicKey.getBytes()));
            return certificate.getSerialNumber().toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    private String getSerialNumber() {
        if (StrUtil.isEmpty(serialNo)) {
            // 获取证书序列号
            X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(wxPayV3Bean.getCertPath()));
            serialNo = certificate.getSerialNumber().toString(16).toUpperCase();

//            System.out.println("输出证书信息:\n" + certificate.toString());
//            // 输出关键信息，截取部分并进行标记
//            System.out.println("证书序列号:" + certificate.getSerialNumber().toString(16));
//            System.out.println("版本号:" + certificate.getVersion());
//            System.out.println("签发者：" + certificate.getIssuerDN());
//            System.out.println("有效起始日期：" + certificate.getNotBefore());
//            System.out.println("有效终止日期：" + certificate.getNotAfter());
//            System.out.println("主体名：" + certificate.getSubjectDN());
//            System.out.println("签名算法：" + certificate.getSigAlgName());
//            System.out.println("签名：" + certificate.getSignature().toString());
        }
        System.out.println("serialNo:" + serialNo);
        return serialNo;
    }

    /**
     * 微信H5 支付
     * 注意：必须再web页面中发起支付且域名已添加到开发配置中
     *//*
    public void wapPay(){

           String ip = "127.0.0.1";

        H5ScencInfo sceneInfo = new H5ScencInfo();

        H5ScencInfo.H5 h5_info = new H5();
        h5_info.setType("Wap");
        //此域名必须在商户平台--"产品中心"--"开发配置"中添加
        h5_info.setWap_url("https://pay.qq.com");
        h5_info.setWap_name("腾讯充值");
        sceneInfo.setH5_info(h5_info);

        Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
                .setAttach("IJPay H5支付测试  -By Javen")
                .setBody("IJPay H5支付测试  -By Javen")
                .setSpbillCreateIp(ip)
                .setTotalFee("520")
                .setTradeType(TradeType.MWEB)
                .setNotifyUrl(notify_url)
                .setOutTradeNo(String.valueOf(System.currentTimeMillis()))
                .setSceneInfo(h5_info.toString())
                .build();

        String xmlResult = WxPayApi.pushOrder(false,params);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(return_code)) {
            ajax.addError(return_msg);
            renderJson(ajax);
            return;
        }
        String result_code = result.get("result_code");
        if (!PaymentKit.codeIsOK(result_code)) {
            ajax.addError(return_msg);
            renderJson(ajax);
            return;
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepay_id = result.get("prepay_id");
        String mweb_url = result.get("mweb_url");

        System.out.println("prepay_id:"+prepay_id+" mweb_url:"+mweb_url);
        redirect(mweb_url);
    }

    *//**
     * 公众号支付
     * @TODO 待测试 o5NJx1dVRilQI6uUVSaBDuLnM3iM
     *//*
    public void webPay() {

        // openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
        String openId = (String) getSession().getAttribute("openId");

        String total_fee=getPara("total_fee");

        if (StrKit.isBlank(openId)) {
            ajax.addError("openId is null");
            renderJson(ajax);
            return;
        }
        if (StrKit.isBlank(total_fee)) {
            ajax.addError("请输入数字金额");
            renderJson(ajax);
            return;
        }

        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
                .setAttach("IJPay 公众号支付测试  -By Javen")
                .setBody("IJPay 公众号支付测试  -By Javen")
                .setOpenId(openId)
                .setSpbillCreateIp(ip)
                .setTotalFee(total_fee)
                .setTradeType(TradeType.JSAPI)
                .setNotifyUrl(notify_url)
                .setOutTradeNo(String.valueOf(System.currentTimeMillis()))
                .build();

        String xmlResult = WxPayApi.pushOrder(false,params);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!PaymentKit.codeIsOK(return_code)) {
            ajax.addError(return_msg);
            renderJson(ajax);
            return;
        }
        String result_code = result.get("result_code");
        if (!PaymentKit.codeIsOK(result_code)) {
            ajax.addError(return_msg);
            renderJson(ajax);
            return;
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
        String prepay_id = result.get("prepay_id");

        Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepay_id);

        String jsonStr = JsonKit.toJson(packageParams);
        ajax.success(jsonStr);
        renderJson(ajax);
    }*/


}
