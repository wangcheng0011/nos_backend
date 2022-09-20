package com.knd.front.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.em.PlatformEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.entity.*;
import com.knd.front.home.service.ICourseHeadService;
import com.knd.front.live.entity.UserCoachCourseEntity;
import com.knd.front.live.entity.UserCoachTimeEntity;
import com.knd.front.live.mapper.UserCoachCourseMapper;
import com.knd.front.live.mapper.UserCoachTimeMapper;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.request.CreateOrderRequest;
import com.knd.front.login.request.GetOrderInfoRequest;
import com.knd.front.login.request.GoodsRequest;
import com.knd.front.login.service.feignInterface.PayFeignClient;
import com.knd.front.pay.dto.*;
import com.knd.front.pay.mapper.GoodsAttrValueMapper;
import com.knd.front.pay.mapper.GoodsImgMapper;
import com.knd.front.pay.mapper.GoodsMapper;
import com.knd.front.pay.request.CreateGoodsRequest;
import com.knd.front.pay.request.GoodsListRequest;
import com.knd.front.pay.request.ParseOrderNotifyRequest;
import com.knd.front.pay.service.IGoodsService;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.user.request.UserPayAddRequest;
import com.knd.front.user.request.UserPayDetailRequest;
import com.knd.front.user.service.IUserPayService;
import com.knd.front.user.service.IUserReceiveAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsEntity> implements IGoodsService {



    //图片路径
    @Value("${upload.FileImagesPath}")
    private String FileImagesPath;


    @Resource
    private AttachMapper attachMapper;

    @Resource
    private GoodsImgMapper goodsImgMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private GoodsAttrValueMapper goodsAttrValueMapper;

    @Resource
    private PayFeignClient payFeignClient;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Value("${pay.order.timeOut}")
    private Integer payTimeOut;

    @Resource
    private IUserReceiveAddressService iUserReceiveAddressService;

    @Resource
    private ICourseHeadService iCourseHeadService;

    @Resource
    private IUserPayService iUserPayService;

    @Resource
    private UserCoachCourseMapper userCoachCourseMapper;

    @Resource
    private UserCoachTimeMapper userCoachTimeMapper;

    @Resource
    private IGoodsService iGoodsService;







    @Override
    public Result getGoodsList(GoodsListRequest request) {
        //分页
        Page<GoodsDto> partPage = new Page<>(Integer.parseInt(request.getCurrent()), PageInfo.pageSize);
        Page<GoodsDto> p = this.baseMapper.selectPageByLike(partPage, request.getTypeList(), request.getGoodName());
        List<GoodsDto> records = p.getRecords();
        for (GoodsDto goodsDto : records) {
            //根据id获取图片信息
            Attach aPi = attachMapper.selectById(goodsDto.getCoverUrl());
            if (aPi != null) {
                goodsDto.setCoverUrl(FileImagesPath + aPi.getFilePath());
            }
        }

        return ResultUtil.success(p);

    }

    @Override
    public Result getGoods(String goodsId) {
        QueryWrapper<GoodsEntity> qw = new QueryWrapper<>();
        qw.eq("id", goodsId);
        qw.eq("deleted", "0");
        GoodsEntity goodsEntity = baseMapper.selectOne(qw);

        GoodsInfoDto goodsInfoDto = new GoodsInfoDto();
        BeanUtils.copyProperties(goodsEntity, goodsInfoDto);
        if (goodsEntity != null) {
            List<GoodsAttrValueEntity> goodsAttrValueEntitys =
                    goodsAttrValueMapper.selectList(
                            new QueryWrapper<GoodsAttrValueEntity>()
                                    .select("attrName", "attrValue", "sort")
                                    .eq("goodsId", goodsId).orderByAsc("sort"));
            List<GoodsAttrValueDto> goodsAttrValueDtoList = new ArrayList<>();
            for (GoodsAttrValueEntity g : goodsAttrValueEntitys) {
                GoodsAttrValueDto goodsAttrValueDto = new GoodsAttrValueDto();
                BeanUtils.copyProperties(goodsAttrValueEntitys, goodsAttrValueDto);
                goodsAttrValueDtoList.add(goodsAttrValueDto);
            }

            goodsInfoDto.setAttrList(goodsAttrValueDtoList);
            List<GoodsImgEntity> goodsHeadImgEntities = goodsImgMapper.selectList(new QueryWrapper<GoodsImgEntity>()
                    .eq("goodsId", goodsId).eq("imgType", "0"));
            List<ImgDto> headImgList = new ArrayList<>();
            for (GoodsImgEntity goodsHeadImgEntity : goodsHeadImgEntities) {
                headImgList.add(getAttachById(goodsHeadImgEntity.getAttachId()));
            }
            goodsInfoDto.setHeadImgList(headImgList);

            List<GoodsImgEntity> goodsInfoImgEntities = goodsImgMapper.selectList(new QueryWrapper<GoodsImgEntity>()
                    .eq("goodsId", goodsId).eq("imgType", "1"));

            List<ImgDto> infoImgList = new ArrayList<>();
            for (GoodsImgEntity goodsInfoImgEntity : goodsInfoImgEntities) {
                //根据id获取图片信息
                infoImgList.add(getAttachById(goodsInfoImgEntity.getAttachId()));
            }
            goodsInfoDto.setInfoImgList(infoImgList);

            //根据id获取图片信息
            goodsInfoDto.setCoverImg(getAttachById(goodsEntity.getCoverAttachId()));
        }
        return ResultUtil.success(goodsInfoDto);
    }

    private ImgDto getAttachById(String id) {
        //根据id获取图片信息
        Attach aPi = attachMapper.selectById(id);
        if (aPi != null) {
            ImgDto imgDto = new ImgDto();
            imgDto.setPicAttachUrl(FileImagesPath + aPi.getFilePath());
            imgDto.setPicAttachSize(aPi.getFileSize());
            return imgDto;
        }
        return null;
    }

    @Override
    public Result getPayInfo(HttpServletResponse response,GetOrderInfoRequest getOrderInfoRequest) {
        log.info("--------------------我要开始支付啦------------------------------");
        log.info("getPayInfo getOrderInfoRequest:{{}}",getOrderInfoRequest);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        UserReceiveAddressEntity userReceiveAddress = null;
        BigDecimal totalAmount = new BigDecimal(0);
        StringBuilder orderDesc = new StringBuilder("");
        List<UserPayDetailRequest> userPayDetailRequestList = new ArrayList<>();
        if(OrderTypeEnum.COURSE.getCode().equals(getOrderInfoRequest.getOrderType())) {
            for(GoodsRequest goodsRequest : getOrderInfoRequest.getGoodsRequestList()) {
                CourseHead courseHead = iCourseHeadService.getById(goodsRequest.getGoodsId());
                if(courseHead == null ) {
                    return ResultUtil.error("U0995","课程不存在");
                }
                Result check = iUserPayService.check(getOrderInfoRequest.getUserId(), 0, goodsRequest.getGoodsId());
                if((Boolean) check.getData()) {
                    return ResultUtil.error("U0995","课程已购买");
                }
                UserPayDetailRequest userPayDetailRequest = new UserPayDetailRequest();
                userPayDetailRequest.setType(0);
                userPayDetailRequest.setPayId(goodsRequest.getGoodsId());
                userPayDetailRequestList.add(userPayDetailRequest);
                totalAmount =totalAmount.add(courseHead.getAmount().setScale(2));
                orderDesc.append(courseHead.getCourse()+"|");
                goodsRequest.setPrice(courseHead.getAmount().setScale(2));
            }
        }else if(OrderTypeEnum.EQUIPMENT.getCode().equals(getOrderInfoRequest.getOrderType())) {

        }else if(OrderTypeEnum.LIVE.getCode().equals(getOrderInfoRequest.getOrderType())) {
            for(GoodsRequest goodsRequest : getOrderInfoRequest.getGoodsRequestList()) {
                UserCoachTimeEntity coachCourseTimeEntity = userCoachTimeMapper
                        .selectById(goodsRequest.getGoodsId());
                UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper
                        .selectById(coachCourseTimeEntity.getCoachCourseId());
                totalAmount =totalAmount.add(goodsRequest.getPrice().setScale(2));
                orderDesc.append("直播"+userCoachCourseEntity.getCourseName()+"预约|");
                goodsRequest.setPrice(goodsRequest.getPrice().setScale(2));
            }
        }else{
            userReceiveAddress = iUserReceiveAddressService
                    .getById(getOrderInfoRequest.getUserReceiveAddressId());
            log.info("getPayInfo UserReceiveAddressId:{{}}",getOrderInfoRequest.getUserReceiveAddressId());
            log.info("getPayInfo userReceiveAddress:{{}}",userReceiveAddress);
            if(userReceiveAddress == null ) {
                return ResultUtil.error("U0995","收货人地址信息不存在");
            }
            for(GoodsRequest goodsRequest : getOrderInfoRequest.getGoodsRequestList()) {
                GoodsEntity goodsEntity = baseMapper.selectById(goodsRequest.getGoodsId());
                if(goodsEntity == null ) {
                    return ResultUtil.error("U0995","商品不存在");
                }
                log.info("getPayInfo quantity:{{}}",goodsRequest.getQuantity());
                BigDecimal b = goodsEntity.getPrice().multiply(new BigDecimal(goodsRequest.getQuantity().trim())).setScale(2);
                totalAmount = totalAmount.add(b);
                orderDesc.append(goodsEntity.getGoodsName()+"|");
                goodsRequest.setPrice(goodsEntity.getPrice().setScale(2));
            }
        }
        createOrderRequest.setAmount(totalAmount);
        createOrderRequest.setOrderType(getOrderInfoRequest.getOrderType());
        createOrderRequest.setUserReceiveAddressId(getOrderInfoRequest.getUserReceiveAddressId());
        createOrderRequest.setDescription(orderDesc.toString().substring(0,orderDesc.length()-1));
        createOrderRequest.setUserId(getOrderInfoRequest.getUserId());
        createOrderRequest.setGoodsRequestList(getOrderInfoRequest.getGoodsRequestList());
        createOrderRequest.setRemarks(getOrderInfoRequest.getRemarks());
        createOrderRequest.setId(getOrderInfoRequest.getId());
        createOrderRequest.setOrderNo(getOrderInfoRequest.getOrderNo());
        log.info("getPayInfo createOrderRequest:{{}}",createOrderRequest);
        Result result = new Result();
       // log.info("UserUtils.getPlatform():{{}}",UserUtils.getPlatform());

     if(PlatformEnum.ANDROID_PLATFORM.getName().equals(getOrderInfoRequest.getPayPlatform()) ||PlatformEnum.IOS_PLATFORM.getName().equals(getOrderInfoRequest.getPayPlatform())) {
            createOrderRequest.setPlatform(getOrderInfoRequest.getPayPlatform());
            createOrderRequest.setPaymentType(getOrderInfoRequest.getPaymentType());
            log.info("payFeignClient.tradeAppPay(createOrderRequest):{{}}"+createOrderRequest);
            result = payFeignClient.tradeAppPay(createOrderRequest);
            log.info("payFeignClient.tradeAppPay(createOrderRequest) result:{{}}",result);
            if(ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
                TbOrder data = JSON.parseObject(JSONObject.toJSONString(result.getData(),true),TbOrder.class);
             //   if(OrderTypeEnum.LIVE.getCode().equals(getOrderInfoRequest.getOrderType())) {
                    OrderDto orderDto = new OrderDto();
                    BeanUtils.copyProperties(data,orderDto);
                    result.setData(orderDto);
                    return result;
                //}else{
                 //   result.setData(data.getAppPayInfo());
                   // return result;
               // }
            }
            return result;
        }else if(PlatformEnum.JSAPI_PLATFORM.getName().equals(getOrderInfoRequest.getPayPlatform())){
            createOrderRequest.setPlatform(getOrderInfoRequest.getPayPlatform());
            createOrderRequest.setPaymentType(getOrderInfoRequest.getPaymentType());
            createOrderRequest.setCode(getOrderInfoRequest.getCode());
            log.info("payFeignClient.jsApiPay(createOrderRequest):{{}}"+createOrderRequest);
            result = payFeignClient.jsApiPay(response,createOrderRequest);
            log.info("payFeignClient.jsApiPay(createOrderRequest) result:{{}}",result);
            if(ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
                OrderDto data = JSON.parseObject(JSONObject.toJSONString(result.getData(),true),OrderDto.class);
                //   if(OrderTypeEnum.LIVE.getCode().equals(getOrderInfoRequest.getOrderType())) {
                log.info("jsApiPay data:{{}}",data);
                result.setData(data);
                return result;
                //}else{
                //   result.setData(data.getAppPayInfo());
                // return result;
                // }
            }
            return result;

        }else if(PlatformEnum.SMALL_ROUTINE.getName().equals(getOrderInfoRequest.getPayPlatform())){
         createOrderRequest.setPlatform(getOrderInfoRequest.getPayPlatform());
         createOrderRequest.setPaymentType(getOrderInfoRequest.getPaymentType());
         User user = userMapper.selectById(getOrderInfoRequest.getUserId());
         if(StringUtils.isNotEmpty(user)){
             createOrderRequest.setOpenId(user.getSmallRoutineOpenId());
         }
         log.info("payFeignClient.smallRoutinePay:{{}}"+createOrderRequest);
         result = payFeignClient.smallRoutinePay(createOrderRequest);
         log.info("payFeignClient.smallRoutinePay result:{{}}",result);
         if(ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
             OrderDto data = JSON.parseObject(JSONObject.toJSONString(result.getData(),true),OrderDto.class);
             //   if(OrderTypeEnum.LIVE.getCode().equals(getOrderInfoRequest.getOrderType())) {
             return ResultUtil.success(data);
             //}else{
             //   result.setData(data.getAppPayInfo());
             // return result;
             // }
         }
         return result;

       } else if(PlatformEnum.QUINNOID.getName().equals(getOrderInfoRequest.getPayPlatform())){
            createOrderRequest.setPlatform(getOrderInfoRequest.getPayPlatform());
            createOrderRequest.setId(getOrderInfoRequest.getId());
            result = payFeignClient.tradePreCreatePay(createOrderRequest);
            System.out.println("payFeignClient.tradePreCreatePay(createOrderRequest):{{}}"+createOrderRequest);
            log.info("payFeignClient.tradePreCreatePay(createOrderRequest):{{}}"+createOrderRequest);
            if(ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
                TbOrder data = JSON.parseObject(JSONObject.toJSONString(result.getData(),true),TbOrder.class);
                data.setStatus("1");
                long remainingTime = ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(), data.getCreateDate().plusMinutes(payTimeOut));
                data.setRemainingTime((remainingTime>=0?remainingTime:-1)+"");
                OrderDto orderDto = new OrderDto();
                BeanUtils.copyProperties(data,orderDto);
                orderDto.setOrderCreateTime(LocalDateTime.now());
                orderDto.setOrderExpireTime(LocalDateTime.now().plusMinutes(payTimeOut));
                if(OrderTypeEnum.GOODS.getCode().equals(getOrderInfoRequest.getOrderType())
                ||OrderTypeEnum.EQUIPMENT.getCode().equals(getOrderInfoRequest.getOrderType())) {
                    UserReceiveAddressDto userReceiveAddressDto = new UserReceiveAddressDto();
                    BeanUtils.copyProperties(userReceiveAddress,userReceiveAddressDto);
                    orderDto.setUserReceiveAddressDto(userReceiveAddressDto);
                }else if(OrderTypeEnum.COURSE.getCode().equals(getOrderInfoRequest.getOrderType())) {
                    UserPayAddRequest userPayAddRequest = new UserPayAddRequest();
                    userPayAddRequest.setUserId(data.getUserId());
                    userPayAddRequest.setDetailRequests(userPayDetailRequestList);
                    userPayAddRequest.setOrderId(data.getId());
                    iUserPayService.add(userPayAddRequest);
                }
                return ResultUtil.success(orderDto);
            }
            return result;
        }
        return result;
    }

    @Override
    public Result cancelOrder(String orderNo) {
        QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        log.info("cancelOrder orderNo:{{}}", orderNo);
        queryWrapper.eq("orderNo", orderNo);
        queryWrapper.eq("deleted", "0");
        TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
        log.info("cancelOrder tbOrder:{{}}", tbOrder);
        if (tbOrder != null && !OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())) {
            return ResultUtil.success(tbOrder);
        }
        LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
        tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
        log.info("cancelOrder tbOrder:{{}}", tbOrder);
        tbOrder.setLastModifiedBy(UserUtils.getUserId());
        tbOrder.setLastModifiedDate(currentLocalDateTime);
        tbOrder.setCancelTime(currentLocalDateTime);
        tbOrderMapper.updateById(tbOrder);
        return ResultUtil.success(tbOrder);
    }

    @Override
    public void add(CreateGoodsRequest createGoodsRequest) {
        GoodsEntity goodsEntity = baseMapper.selectById(createGoodsRequest.getId());
        if (goodsEntity == null) {
            //存储商品
            GoodsEntity g = new GoodsEntity();
            g.setId(createGoodsRequest.getId());
            g.setCategoryId(createGoodsRequest.getCategoryId());
            g.setGoodsName(createGoodsRequest.getGoodsName());
            g.setGoodsDesc(createGoodsRequest.getGoodsDesc());
            g.setGoodsType(createGoodsRequest.getGoodsType());
            g.setPublishStatus("0");
            g.setWebsiteFlag("1");
            g.setPrice(createGoodsRequest.getPrice());
            g.setCreateDate(LocalDateTime.now());
            g.setLastModifiedDate(LocalDateTime.now());
            g.setDeleted("0");
            baseMapper.insert(g);
        }


    }


    @Override
    public Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest) {
        return payFeignClient.parseOrderNotifyResult(parseOrderNotifyRequest);
    }

    @Override
    public Result alipayCallback(String outBizNo, String orderId) {
        return payFeignClient.alipayCallback(outBizNo,orderId);
    }






/*
   @Override
    public Result cancelPayByExpiredTime(GetOrderInfoRequest getOrderInfoRequest) {
        LocalDateTime orderCreateTime = getOrderInfoRequest.getOrderCreateTime();
        LocalDateTime orderExpireTime = getOrderInfoRequest.getOrderExpireTime();
        TbOrder tbOrder = new TbOrder();
        if(LocalDateTime.now().isAfter(orderExpireTime)){
        }
    }
*/


    @Override
    public GoodsEntity insertReturnEntity(GoodsEntity entity) {
        return null;
    }

    @Override
    public GoodsEntity updateReturnEntity(GoodsEntity entity) {
        return null;
    }
}
