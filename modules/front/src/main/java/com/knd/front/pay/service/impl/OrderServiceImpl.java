package com.knd.front.pay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.utils.IosVerifyUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.dto.CodeDto;
import com.knd.front.common.dto.ResponseDto;
import com.knd.front.common.service.AttachService;
import com.knd.front.common.service.IVerifyCodeService;
import com.knd.front.diagnosis.mapper.AdminMapper;
import com.knd.front.dto.OrderIcDTO;
import com.knd.front.entity.*;
import com.knd.front.live.entity.UserCoachTimeEntity;
import com.knd.front.live.mapper.UserCoachTimeMapper;
import com.knd.front.live.request.CancelOrderCoachRequest;
import com.knd.front.live.service.CoachOrderService;
import com.knd.front.login.mapper.TbOrderItemMapper;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.login.request.ChangeVipTypeRequest;
import com.knd.front.login.service.IUserService;
import com.knd.front.login.service.feignInterface.PayFeignClient;
import com.knd.front.pay.dto.ImgDto;
import com.knd.front.pay.dto.OrderConsultingDTO;
import com.knd.front.pay.dto.OrderDto;
import com.knd.front.pay.dto.OrderItemDto;
import com.knd.front.pay.mapper.GoodsAttrValueMapper;
import com.knd.front.pay.mapper.GoodsMapper;
import com.knd.front.pay.mapper.OrderConsultingMapper;
import com.knd.front.pay.mapper.OrderIcMapper;
import com.knd.front.pay.request.*;
import com.knd.front.pay.service.IOrderService;
import com.knd.front.train.mapper.AmapAdcodeMapper;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.user.mapper.UserReceiveAddressMapper;
import com.knd.front.user.request.ObligationRequest;
import com.knd.front.user.service.IUserReceiveAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
@Service
@Transactional
@Slf4j
public class OrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements IOrderService {
    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private OrderIcMapper orderIcMapper;

    @Resource
    private OrderConsultingMapper orderConsultingMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private IUserReceiveAddressService iUserReceiveAddressService;

    @Resource
    private IUserService iUserService;

    @Resource
    private AmapAdcodeMapper amapAdcodeMapper;

    @Resource
    private GoodsAttrValueMapper goodsAttrValueMapper;

    @Resource
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private AttachMapper attachMapper;

    @Resource
    private UserCoachTimeMapper userCoachTimeMapper;

    @Resource
    private AttachService attachService;

    @Resource
    private IVerifyCodeService verifyCodeService;

    @Resource
    private CoachOrderService coachOrderService;

    @Resource
    private PayFeignClient payFeignClient;

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Value("${pay.order.timeOut}")
    private Integer payTimeOut;



    @Override
    public Result getOrderById(String orderId, String orderNo, String userId) {
        OrderDto orderDto = new OrderDto();
        QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(orderId)) {
            tbOrderQueryWrapper.eq("id", orderId);
        } else {
            tbOrderQueryWrapper.eq("orderNo", orderNo);
        }
        tbOrderQueryWrapper.eq("userId", userId);
        tbOrderQueryWrapper.eq("deleted", 0);
        TbOrder order = baseMapper.selectOne(tbOrderQueryWrapper);
        log.info("getOrderById order:{{}}", order);
        //  TbOrder order = baseMapper.selectById(orderId);
        if (StringUtils.isEmpty(order)) {
            return ResultUtil.error("U0999", "订单无数据");
        }
        if(OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(order.getStatus())
                &&order.getCreateDate()
                .isAfter(DateUtils.getCurrentLocalDateTime().minusMinutes(payTimeOut))) {
            order.setRemainingTime((ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(),order.getCreateDate().plusMinutes(payTimeOut)))+"");
        }
        BeanUtils.copyProperties(order, orderDto);
        List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                .eq("tbOrderId", order.getId()));
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for (TbOrderItem tbOrderItem : tbOrderItemList) {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(tbOrderItem.getGoodsId());
            orderItemDto.setPrice(tbOrderItem.getPrice());
            orderItemDto.setQuantity(tbOrderItem.getQuantity());
            List<GoodsAttrValueEntity> goodsAttrValueEntities = goodsAttrValueMapper.selectList(
                    new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", tbOrderItem.getGoodsId())
                            .orderByAsc("sort"));
            GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItem.getGoodsId());
            BeanUtils.copyProperties(goodsEntity, orderItemDto);
            if (StringUtils.isNotEmpty(goodsEntity.getCoverAttachId())){
                Attach attach = attachMapper.selectById(goodsEntity.getCoverAttachId());
                orderItemDto.setCoverUrl(attach!=null ? fileImagesPath+attach.getFilePath() : "");
            }
            orderItemDto.setGoodsAttrValueEntityList(goodsAttrValueEntities);
            orderItemDtoList.add(orderItemDto);


        }
        orderDto.setUserOrderItemDtoList(orderItemDtoList);
        QueryWrapper<OrderIcEntity> orderIcEntityQueryWrapper = new QueryWrapper<>();
        orderIcEntityQueryWrapper.eq("tbOrderId", order.getId());
        orderIcEntityQueryWrapper.eq("deleted", "0");
        OrderIcEntity orderIcEntity = orderIcMapper.selectOne(orderIcEntityQueryWrapper);
        if (null != orderIcEntity && !StringUtils.isEmpty(orderIcEntity.getPersonId())) {
            OrderIcDTO orderIcDTO = new OrderIcDTO();
            BeanUtils.copyProperties(orderIcEntity, orderIcDTO);
            Admin admin = adminMapper.selectById(orderIcEntity.getPersonId());
            orderIcDTO.setPersonName(admin.getNickName());
            orderIcDTO.setPersonMobile(admin.getMobile());
        }

        orderDto.setOrderIcEntity(orderIcEntity);
        UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(order.getUserReceiveAddressId());
        //省截取
        QueryWrapper<AmapAdcodeEntity> provinceQueryWrapper = new QueryWrapper<>();
        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
        provinceQueryWrapper.eq("deleted", "0");
        provinceQueryWrapper.last("limit 1");
        AmapAdcodeEntity provinceAmapAdCodeEntity = amapAdcodeMapper.selectOne(provinceQueryWrapper);
        if(StringUtils.isEmpty(provinceAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getProvince().split(",");
            userReceiveAddressEntity.setProvince(split[0]);
        }
        //市截取
        QueryWrapper<AmapAdcodeEntity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
        cityQueryWrapper.eq("deleted", "0");
        cityQueryWrapper.last("limit 1");
        AmapAdcodeEntity cityAmapAdCodeEntity = amapAdcodeMapper.selectOne(cityQueryWrapper);
        if(StringUtils.isEmpty(cityAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getCity().split(",");
            userReceiveAddressEntity.setCity(split[0]);
        }
        //区截取
        QueryWrapper<AmapAdcodeEntity> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("name",userReceiveAddressEntity.getRegion());
        regionQueryWrapper.eq("deleted", "0");
        regionQueryWrapper.last("limit 1");
        AmapAdcodeEntity regionAmapAdCodeEntity = amapAdcodeMapper.selectOne(regionQueryWrapper);
        if(StringUtils.isEmpty(regionAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getRegion().split(",");
            userReceiveAddressEntity.setRegion(split[0]);
        }
        orderDto.setUserReceiveAddress(userReceiveAddressEntity);
        return ResultUtil.success(orderDto);
    }


    @Override
    public Result createOrderFromWebsite(CreateWebsiteOrderRequest createWebsiteOrderRequest) {
        String receiveAddressId = iUserReceiveAddressService.add(createWebsiteOrderRequest.getUserReceiveAddressRequest());
        if ("-1".equals(receiveAddressId)) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "无法解析地址经纬度");
        }
        TbOrder tbOrder = new TbOrder();
        BeanUtils.copyProperties(createWebsiteOrderRequest, tbOrder);
        tbOrder.setUserReceiveAddressId(receiveAddressId);
        tbOrder.setUserId(UserUtils.getUserId());
        tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        tbOrder.setCreateDate(createWebsiteOrderRequest.getCreateDate());
        baseMapper.insert(tbOrder);

        //创建订单子项
        for (TbOrderItemRequest tbOrderItemRequest : createWebsiteOrderRequest.getTbOrderItemRequestList()) {
            TbOrderItem tbOrderItem = new TbOrderItem();
            tbOrderItem.setTbOrderId(tbOrder.getId());
//            if(!StringUtils.isEmpty(tbOrderItemRequest.getGoodsId())){
//                BeanUtils.copyProperties(tbOrderItemRequest,tbOrderItem);
//                GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItemRequest.getGoodsId());
//                tbOrderItem.setPrice(goodsEntity.getPrice());
//            }else{
//                //先同步网站商品部分部分信息
//                iGoodsService.add(tbOrderItemRequest.getCreateGoodsRequest());
//                tbOrderItem.setPrice(tbOrderItemRequest.getCreateGoodsRequest().getPrice());
//                tbOrderItem.setGoodsId(tbOrderItemRequest.getCreateGoodsRequest().getId());
//                tbOrderItem.setQuantity(tbOrderItemRequest.getQuantity());
//            }
            BeanUtils.copyProperties(tbOrderItemRequest, tbOrderItem);
            GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItemRequest.getGoodsId());
            tbOrderItem.setPrice(goodsEntity.getPrice());

            tbOrderItemMapper.insert(tbOrderItem);

        }

        //插入安装信息
        if ("1".equals(createWebsiteOrderRequest.getInstallFlag())) {
            OrderIcEntity orderIcEntity = new OrderIcEntity();
            BeanUtils.copyProperties(createWebsiteOrderRequest.getOrderIcRequest(), orderIcEntity);
            orderIcEntity.setId(null);
            orderIcEntity.setTbOrderId(tbOrder.getId());
            orderIcMapper.insert(orderIcEntity);
        }
        return ResultUtil.success();
    }

    @Override
    public Result iosInAppPurchase(VipMenu vipMenu, IosInAppPurchaseRequest iosInAppPurchaseRequest) {
        // 0沙盒 非0即线上
        String verifyResult = IosVerifyUtil.buyAppVerify(iosInAppPurchaseRequest.getReceipt(), Integer.parseInt(iosInAppPurchaseRequest.getVerifyType()));
        if (StringUtils.isEmpty(verifyResult)) {// 苹果服务器没有返回验证结果
            log.info("IOS内购(充值)=>苹果服务器没有返回验证结果");
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR.getCode(), "苹果服务器没有返回验证结果");

        } else {// 苹果验证有返回结果
            JSONObject job = JSONObject.parseObject(verifyResult);
            log.info("IOS内购(充值)=>苹果服务器返回验证结果:{}", job);
            String states = job.getString("status");
            //是沙盒环境，应沙盒测试，否则执行下面
            if ("21007".equals(states)) {
                //2.在沙盒测试  发送平台验证
                verifyResult = IosVerifyUtil.buyAppVerify(iosInAppPurchaseRequest.getReceipt(), 0);
                job = JSONObject.parseObject(verifyResult);
                states = job.getString("status");
            }
            if (states.equals("0")) { // 前端所提供的收据是有效的    验证成功
                String r_receipt = job.getString("receipt");
                JSONObject returnJson = JSONObject.parseObject(r_receipt);
                String in_app = returnJson.getString("in_app");
                JSONArray jsonArray = JSONArray.parseArray(in_app);
                log.info("IOS内购(充值)=>苹果服务器返回验证结果订单数量:{}", jsonArray.size());

                LocalDateTime now = LocalDateTime.now();
                //配置本地vip商品id对应的IOS平台的商品id，增加‘UB_’前缀
                String iosProductId = "UB_" + vipMenu.getId();
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (iosProductId.equals(jsonArray.getJSONObject(i).get("product_id") == null ? null : jsonArray.getJSONObject(i).get("product_id").toString())) {
                        // 订单号
                        String transactionId = jsonArray.getJSONObject(i).get("transaction_id") == null ? null : jsonArray.getJSONObject(i).get("transaction_id").toString();
                        /* 自己的业务处理 */
                        // 1.创建订单信息
                        TbOrder tbOrder = new TbOrder();
                        tbOrder.setOrderType("1");
                        tbOrder.setPaymentType("5");
                        tbOrder.setOrderSource("1");
                        tbOrder.setOrderNo(StringUtils.getOutTradeNo());
                        tbOrder.setOutOrderNo(transactionId);
                        tbOrder.setAmount(vipMenu.getPrice());
                        tbOrder.setUserId(UserUtils.getUserId());
                        tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
                        tbOrder.setCreateDate(now);
                        tbOrder.setPaymentTime(now);
                        tbOrder.setDescription(vipMenu.getVipName() + "-ios内购");
                        baseMapper.insert(tbOrder);
                        TbOrderItem orderItem = new TbOrderItem();
                        orderItem.setQuantity("1");
                        orderItem.setGoodsId(vipMenu.getId());
                        orderItem.setPrice(vipMenu.getPrice());
                        orderItem.setTbOrderId(tbOrder.getId());
                        tbOrderItemMapper.insert(orderItem);

                        // 2.更新用户会员
                        ChangeVipTypeRequest changeVipTypeRequest = new ChangeVipTypeRequest();
                        changeVipTypeRequest.setUserId(UserUtils.getUserId());
                        changeVipTypeRequest.setVipMenuId(vipMenu.getId());
                        iUserService.changeVipType(changeVipTypeRequest);
                    }
                }
            } else {
                return ResultUtil.error(ResultEnum.UNKNOWN_ERROR.getCode(), "验证失败，请重新尝试购买");
            }
        }
        return ResultUtil.success();
    }

//    @Override
//    public Result createOrderFromOffline(CreateWebsiteOrderRequest createWebsiteOrderRequest) {
//        createWebsiteOrderRequest.getUserReceiveAddressEntity().setDefaultStatus("0");
//        String receiveAddressId = iUserReceiveAddressService.add(createWebsiteOrderRequest.getUserReceiveAddressEntity());
//        TbOrder tbOrder = new TbOrder();
//        BeanUtils.copyProperties(createWebsiteOrderRequest,tbOrder);
//        tbOrder.setUserReceiveAddressId(receiveAddressId);
//        tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
//        tbOrder.setCreateDate(createWebsiteOrderRequest.getCreateDate());
//        tbOrder.setMobile(createWebsiteOrderRequest.getUserReceiveAddressEntity().getPhone());
//        tbOrder.setOrderNo(StringUtils.getOutTradeNo());
//        if(createWebsiteOrderRequest.getOrderIcEntity()!= null){
//            tbOrder.setInstallFlag("1");
//        }
//        baseMapper.insert(tbOrder);
//        //创建订单子项
//        for(TbOrderItem tbOrderItem: createWebsiteOrderRequest.getTbOrderItemList()) {
//            tbOrderItem.setTbOrderId(tbOrder.getId());
//            tbOrderItem.setId(null);
//            tbOrderItemMapper.insert(tbOrderItem);
//
//        }
//        //插入安装信息
//        createWebsiteOrderRequest.getOrderIcEntity().setId(null);
//        createWebsiteOrderRequest.getOrderIcEntity().setTbOrderId(tbOrder.getId());
//        orderIcMapper.insert(createWebsiteOrderRequest.getOrderIcEntity());
//        return ResultUtil.success();
//    }

    @Override
    public Result obligation(ObligationRequest obligationRequest) {
        log.info("obligation obligationRequest:{{}}",obligationRequest);
        TbOrder tbOrder = new TbOrder();
        tbOrder.setUserId(obligationRequest.getUserId());
        tbOrder.setOrderType(obligationRequest.getOrderType());
        tbOrder.setDeleted("0");
        tbOrder.setPlatform(UserUtils.getPlatform());
        tbOrder.setInstallFlag(obligationRequest.getInstallFlag());
        tbOrder.setInstallStatus(obligationRequest.getInstallStatus());
        tbOrder.setConfirmStatus("0");
        tbOrder.setOrderCreateTime(LocalDateTime.now());
        tbOrder.setOrderExpireTime(LocalDateTime.now().plusMinutes(30));
        if (OrderTypeEnum.GOODS.getCode().equals(obligationRequest.getOrderType())) {
            tbOrder.setUserReceiveAddressId(obligationRequest.getUserReceiveAddressId());
        }
        if(StringUtils.isEmpty(obligationRequest.getOrderId())){
            tbOrder.setId(UUIDUtil.getShortUUID());
            tbOrder.setOrderNo(StringUtils.getOutTradeNo());
            tbOrder.setAmount(new BigDecimal(0));
            tbOrder.setCreateDate(LocalDateTime.now());
            tbOrder.setCreateBy(obligationRequest.getUserId());
            //设置订单为未付款状态
            tbOrder.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
            log.info("obligation 新建订单tbOrder:{{}}",tbOrder);
            baseMapper.insert(tbOrder);
        }else {
            tbOrder.setId(obligationRequest.getOrderId());
            tbOrder.setOrderNo(obligationRequest.getOrderNo());
            tbOrder.setLastModifiedDate(LocalDateTime.now());
            tbOrder.setLastModifiedBy(obligationRequest.getUserId());
            log.info("obligation 更新订单tbOrder:{{}}",tbOrder);
            baseMapper.updateById(tbOrder);
        }

        return ResultUtil.success(tbOrder);
    }

    @Override
    public Result addOrUpdateOrderConsulting(OrderConsultingRequest orderConsultingRequest) {
        OrderConsultingEntity orderConsultingEntity = new OrderConsultingEntity();
        copyProperties(orderConsultingRequest,orderConsultingEntity);
        orderConsultingEntity.setDeleted("0");
        orderConsultingEntity.setPicAttachId(orderConsultingRequest.getPictureUrlId());
        log.info("addOrUpdateOrderConsulting PictureUrl",orderConsultingRequest.getPictureUrl());
        log.info("addOrUpdateOrderConsulting PicAttachName",orderConsultingRequest.getPictureUrl().getPicAttachName());
        log.info("addOrUpdateOrderConsulting PicAttachNewName",orderConsultingRequest.getPictureUrl().getPicAttachNewName());
        log.info("addOrUpdateOrderConsulting PicAttachSize",orderConsultingRequest.getPictureUrl().getPicAttachSize());
        if(StringUtils.isEmpty(orderConsultingRequest.getPictureUrlId())){
            //保存选中图片
            Attach attach = attachService.saveAttach(orderConsultingRequest.getUserId(), orderConsultingRequest.getPictureUrl().getPicAttachName()
                    , orderConsultingRequest.getPictureUrl().getPicAttachNewName(), orderConsultingRequest.getPictureUrl().getPicAttachSize());
            String attachId = attach.getId();
            log.info("addOrUpdateOrderConsulting attachId",attachId);
            orderConsultingEntity.setPicAttachId(attachId);
        }
        if(null==orderConsultingEntity.getId()){
            orderConsultingEntity.setId(UUIDUtil.getShortUUID());
            orderConsultingEntity.setCreateBy(orderConsultingRequest.getUserId());
            orderConsultingEntity.setCreateDate(LocalDateTime.now());
            orderConsultingMapper.insert(orderConsultingEntity);
        }else {
            orderConsultingEntity.setLastModifiedBy(orderConsultingRequest.getUserId());
            orderConsultingEntity.setLastModifiedDate(LocalDateTime.now());
            orderConsultingMapper.updateById(orderConsultingEntity);
        }

        return ResultUtil.success(orderConsultingEntity);
    }

    @Override
    public Result queryOrderConsulting(QueryOrderConsultingRequest queryOrderConsultingRequest) {
        Page<OrderConsultingEntity> page = new Page<>(Integer.parseInt(queryOrderConsultingRequest.getCurrent()), Integer.parseInt(queryOrderConsultingRequest.getPageSize()));
        QueryWrapper<OrderConsultingEntity> orderConsultingEntityQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(queryOrderConsultingRequest.getKeyWord())) {
            orderConsultingEntityQueryWrapper.and(e -> e.like("userName",queryOrderConsultingRequest.getKeyWord())
                    .or(q ->q.like("phone",queryOrderConsultingRequest.getKeyWord()))
                    .or(q ->q.like("installAddress",queryOrderConsultingRequest.getKeyWord())))
                    .or(q ->q.like("detailAddress",queryOrderConsultingRequest.getKeyWord()))
                    .or(q ->q.like("installFloor",queryOrderConsultingRequest.getKeyWord()));

        }
        if(StringUtils.isNotEmpty(queryOrderConsultingRequest.getIsElevator())){
            orderConsultingEntityQueryWrapper.eq("isElevator",queryOrderConsultingRequest.getIsElevator());
        }
        if(StringUtils.isNotEmpty(queryOrderConsultingRequest.getBuyPlatform())){
            orderConsultingEntityQueryWrapper.eq("buyPlatform",queryOrderConsultingRequest.getBuyPlatform());
        }

        if(StringUtils.isNotEmpty(queryOrderConsultingRequest.getInstallStartTime())){
            orderConsultingEntityQueryWrapper.ge("installStartTime",queryOrderConsultingRequest.getInstallStartTime());
        }
        if(StringUtils.isNotEmpty(queryOrderConsultingRequest.getInstallEndTime())){
            orderConsultingEntityQueryWrapper.le("installEndTime",queryOrderConsultingRequest.getInstallEndTime());
        }


        orderConsultingEntityQueryWrapper.eq("deleted","0");
        orderConsultingEntityQueryWrapper.orderByDesc("createDate");
        Page<OrderConsultingEntity> orderConsultingEntityPage = orderConsultingMapper.selectPage(page, orderConsultingEntityQueryWrapper);
        List<OrderConsultingEntity> records = orderConsultingEntityPage.getRecords();
        Page<OrderConsultingDTO> orderConsultingDTOPage = new Page<>();
        ArrayList<OrderConsultingDTO> orderConsultingDTOS = new ArrayList<>();
        records.stream().forEach(r->{
            OrderConsultingDTO orderConsultingDTO = new OrderConsultingDTO();
            BeanUtils.copyProperties(r,orderConsultingDTO);
            orderConsultingDTO.setPicAttachUrl(getImgDto(r.getPicAttachId()));
            orderConsultingDTOS.add(orderConsultingDTO);}
        );
        orderConsultingDTOPage.setRecords(orderConsultingDTOS);
        ResponseDto dto = ResponseDto.<OrderConsultingDTO>builder().total((int) orderConsultingDTOPage.getTotal()).resList(orderConsultingDTOS).build();
        return ResultUtil.success(dto);
    }


    @Override
    public Result updateOrderStatus(String userId, String status,String id,String trackingNumber,String logisticsCompanies, String deliveryDate) {
        //根据id获取名称
        QueryWrapper<TbOrder> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        //qw.select("status");
        TbOrder eq = baseMapper.selectOne(qw);
        if (eq == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        eq.setTrackingNumber(trackingNumber);
        eq.setLogisticsCompanies(logisticsCompanies);
        LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
        if (OrderStatusEnum.DELIVER_FINISHED.getCode().equals(status)) {
            //已发货，待确认收货
            eq.setStatus(status);
            eq.setDeliveryDate(LocalDate.parse(deliveryDate));
            //发送收货确认码短信至客户手机号，将来提供给派送员以便确认收货
            UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(eq.getUserReceiveAddressId());
            CodeDto codeDto = new CodeDto();
            codeDto.setCodeType("30");//收货确认码
            codeDto.setMobile(userReceiveAddressEntity.getPhone());
            //验证码生成
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            codeDto.setCode(verifyCode);
          //  Result result = verifyCodeService.getVerificationCode(codeDto);
          //  if (!ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
          //      return result;
           // }
            eq.setReceivingCode(verifyCode);
            eq.setDeliveryTime(currentLocalDateTime);
            // eq.setStatus(status);
            eq.setLastModifiedBy(userId);
            eq.setLastModifiedDate(currentLocalDateTime);
            baseMapper.updateById(eq);
        } else if (OrderStatusEnum.ORDER_FINISHED.getCode().equals(status)) {

            //已确认收货，订单已完成
            eq.setStatus(status);
            eq.setConfirmStatus("1");
            eq.setReceiveTime(currentLocalDateTime);
            // eq.setStatus(status);
            eq.setLastModifiedBy(userId);
            eq.setLastModifiedDate(currentLocalDateTime);
            baseMapper.updateById(eq);
        } else if (OrderStatusEnum.REFUNDING.getCode().equals(status)) {
            if (OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(eq.getStatus())
                    || OrderStatusEnum.REFUNDING.getCode().equals(eq.getStatus())
                    || OrderStatusEnum.REFUNDED.getCode().equals(eq.getStatus())
                    || OrderStatusEnum.ORDER_CLOSED.getCode().equals(eq.getStatus())) {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "该订单状态不支持退款操作");
            }
            if ("1".equals(eq.getOrderType()) || "3".equals(eq.getOrderType())) {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "虚拟订单不支持退款操作");
            }
            if ("5".equals(eq.getOrderType())) {
                List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", eq.getId()));
                for (TbOrderItem tbOrderItem : tbOrderItemList) {
                    //获取预约课程的信息
                    UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(tbOrderItem.getGoodsId());
                    //检查开播时间是否在24小时以上
                    if (userCoachTimeEntity.getBeginTime().isBefore(LocalDateTime.now().plusHours(24))) {
                        return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "预约课程暂不支持开播前24小时内退款");
                    }
                }

            }
            //申请退款
            eq.setStatus(OrderStatusEnum.REFUNDING.getCode());
            eq.setLastModifiedBy(userId);
            eq.setLastModifiedDate(currentLocalDateTime);
            baseMapper.updateById(eq);
        } else if (OrderStatusEnum.REFUNDED.getCode().equals(status)) {
            //如果是预约直播课程，取消预约
//            if("5".equals(eq.getOrderType())) {
//                List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", eq.getId()));
//                for(TbOrderItem tbOrderItem:tbOrderItemList) {
//                    //TODO 调用取消预约接口
//                    CancelOrderCoachRequest request = new CancelOrderCoachRequest();
//                    request.setUserId(userId);
//                    request.setCoachTimeId(tbOrderItem.getGoodsId());
//                    coachFeignClient.cancelOrderSuccess(request);
//                }
//            }
            //微信支付宝线上退款
            if (OrderStatusEnum.REFUNDING.getCode().equals(eq.getStatus())) {
                if ("1".equals(eq.getPaymentType()) && StringUtils.isNotEmpty(eq.getRefundNo())) {
                    //查询并刷新退款结果
                    return payFeignClient.wxRefundQuery(eq.getOutOrderNo(), eq.getOrderNo()
                            , eq.getRefundNo(), eq.getOutRefundNo());
                }

                if ("1".equals(eq.getPaymentType()) || "2".equals(eq.getPaymentType())) {
                    return payFeignClient.tradeRefund(eq.getOutOrderNo(), eq.getOrderNo());
                } else {
                    //非支付宝/微信方式线下完成退款直接跟新订单状态为已退款
                    eq.setStatus(OrderStatusEnum.REFUNDED.getCode());
                    eq.setLastModifiedBy(userId);
                    eq.setLastModifiedDate(currentLocalDateTime);
                    baseMapper.updateById(eq);
                }

            } else {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "退款状态不正确");
            }

        } else if ("1000".equals(status)) {
            //如果是预约直播课程，取消预约
            if ("5".equals(eq.getOrderType())) {
                List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>().eq("tbOrderId", eq.getId()));
                for (TbOrderItem tbOrderItem : tbOrderItemList) {
                    //TODO 调用取消预约接口
                    CancelOrderCoachRequest request = new CancelOrderCoachRequest();
                    request.setUserId(userId);
                    request.setCoachTimeId(tbOrderItem.getGoodsId());
                    coachOrderService.cancelOrderSuccess(request.getCoachTimeId(),request.getUserId());
                }
            }
            //手动退款
            if (OrderStatusEnum.REFUNDING.getCode().equals(eq.getStatus()) && StringUtils.isEmpty(eq.getRefundNo())) {
                eq.setStatus(OrderStatusEnum.REFUNDED.getCode());
                eq.setLastModifiedBy(userId);
                eq.setLastModifiedDate(currentLocalDateTime);
                baseMapper.updateById(eq);
            } else {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "退款状态不正确");
            }

        }else if(OrderStatusEnum.ORDER_CLOSED.getCode().equals(status)){
            //已取消
            eq.setStatus(status);
            eq.setLastModifiedBy(userId);
            eq.setLastModifiedDate(currentLocalDateTime);
            baseMapper.updateById(eq);

        }


        //成功
        return ResultUtil.success(eq);
    }

    public ImgDto getImgDto(String urlId){
        //根据id获取图片信息
        Attach aPi = attachService.getInfoById(urlId);
        ImgDto imgDto = new ImgDto();
        if (aPi != null) {
            imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
            imgDto.setPicAttachSize(aPi.getFileSize());
            String[] strs = (aPi.getFilePath()).split("\\?");
            imgDto.setPicAttachNewName(imageFoldername + strs[0]);
            imgDto.setPicAttachName(aPi.getFileName());
        }
        return imgDto;
    }



    @Override
    public TbOrder insertReturnEntity(TbOrder entity) {
        return null;
    }

    @Override
    public TbOrder updateReturnEntity(TbOrder entity) {
        return null;
    }
}






