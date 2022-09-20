package com.knd.front.login.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.entity.*;
import com.knd.front.home.mapper.CourseHeadMapper;
import com.knd.front.live.mapper.UserCoachCourseMapper;
import com.knd.front.live.mapper.UserCoachTimeMapper;
import com.knd.front.login.dto.UserOrderDto;
import com.knd.front.login.dto.UserOrderItemDto;
import com.knd.front.login.mapper.TbOrderItemMapper;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.login.mapper.VipMenuMapper;
import com.knd.front.login.request.CreateOrderRequest;
import com.knd.front.login.request.GetOrderInfoRequest;
import com.knd.front.login.service.IVipMenuService;
import com.knd.front.login.service.feignInterface.PayFeignClient;
import com.knd.front.pay.mapper.GoodsMapper;
import com.knd.front.pay.service.IGoodsService;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.user.mapper.UserReceiveAddressMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员套餐表  服务实现类
 * </p>
 *
 * @author will
 * @since 2021-01-13
 */
@Service
@Log4j2
public class VipMenuServiceImpl extends ServiceImpl<VipMenuMapper, VipMenu> implements IVipMenuService {

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Resource
    private PayFeignClient payFeignClient;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private CourseHeadMapper courseHeadMapper;

    @Resource
    private VipMenuMapper vipMenuMapper;

    @Resource
    private AttachMapper attachMapper;

    @Resource
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Resource
    private UserCoachCourseMapper userCoachCourseMapper;

    @Resource
    private UserCoachTimeMapper userCoachTimeMapper;

    @Resource
    private IGoodsService iGoodsService;

    @Override
    public VipMenu insertReturnEntity(VipMenu entity) {
        return null;
    }

    @Override
    public VipMenu updateReturnEntity(VipMenu entity) {
        return null;
    }

    @Value("${pay.order.timeOut}")
    private Integer payTimeOut;

    @Override
    public Result getPayInfo(GetOrderInfoRequest getOrderInfoRequest) {
        log.info("------------------------------getPayInfo 获取支付订单--------------------------");
        log.info("getPayInfo getOrderInfoRequest:{{}}",getOrderInfoRequest);
       /*  QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotEmpty(getOrderInfoRequest.getUserId())){
            queryWrapper.eq("createBy", getOrderInfoRequest.getUserId());
        }else{
            queryWrapper.eq("createBy", UserUtils.getUserId());
        }

        queryWrapper.eq("orderType","1");
        queryWrapper.eq("status","1");
        queryWrapper.gt("createDate", DateUtils.getCurrentLocalDateTime().minusMinutes(payTimeOut));
        queryWrapper.orderByDesc("createDate");
       List<TbOrder> tbOrderList = tbOrderMapper.selectList(queryWrapper);
        log.info("getPayInfo tbOrderList:{{}}",tbOrderList);
        log.info("getPayInfo tbOrderList的长度:{{}}",tbOrderList.size());
        if(tbOrderList.size()>0){
            TbOrder tbOrder = tbOrderList.get(0);
            log.info("getPayInfo tbOrder:{{}}",tbOrder);
            tbOrder.setRemainingTime((ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(),tbOrder.getCreateDate().plusMinutes(payTimeOut)))+"");
            log.info("getPayInfo tbOrder:{{}}",tbOrder);
            return ResultUtil.success(tbOrder);
        }*/
        log.info("------------------------------getPayInfo 获取支付订单往下走--------------------------");
        VipMenu vipMenu = baseMapper.selectById(getOrderInfoRequest.getGoodsRequestList().get(0).getGoodsId());
        log.info("getPayInfo vipMenu:{{}}",vipMenu);
        getOrderInfoRequest.getGoodsRequestList().get(0).setPrice(vipMenu.getPrice());
        if(vipMenu == null ) {
            return ResultUtil.error("U0995","vip套餐不存在");
        }
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderType(getOrderInfoRequest.getOrderType());
        createOrderRequest.setAmount(vipMenu.getPrice());
        createOrderRequest.setDescription(vipMenu.getDescription());
//        createOrderRequest.setGoodsType(getOrderInfoRequest.getGoodsType());
        if(StringUtils.isNotEmpty(getOrderInfoRequest.getUserId())){
            createOrderRequest.setUserId(getOrderInfoRequest.getUserId());
        }else{
            createOrderRequest.setUserId(UserUtils.getUserId());
        }

//        createOrderRequest.setGoodsId(getOrderInfoRequest.getGoodsId());
        createOrderRequest.setGoodsRequestList(getOrderInfoRequest.getGoodsRequestList());
        log.info("getPayInfo createOrderRequest:{{}}",createOrderRequest);
        createOrderRequest.setPlatform(getOrderInfoRequest.getPayPlatform());
        Result result = payFeignClient.tradePreCreatePay(createOrderRequest);
        log.info("getPayInfo result:{{}}",result);
        if(ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
            TbOrder data = JSON.parseObject(JSONObject.toJSONString(result.getData(),true),TbOrder.class);
            data.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
            long remainingTime = ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(), data.getCreateDate().plusMinutes(payTimeOut));
            data.setRemainingTime((remainingTime>=0?remainingTime:-1)+"");
            log.info("getPayInfo data:{{}}",data);
            return ResultUtil.success(data);
        }
        return result;
    }

    @Override
    public Result tradeQuery(String outTradeNo, String tradeNo) {
        log.info("tradeQuery outTradeNo:{{}}",outTradeNo);
        log.info("tradeQuery tradeNo:{{}}",tradeNo);
        QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderNo", tradeNo);
        queryWrapper.eq("deleted", "0");
        TbOrder tbOrder = tbOrderMapper.selectOne(queryWrapper);
        log.info("tradeQuery tbOrder:{{}}",tbOrder);
        if(tbOrder == null) {
            ResultUtil.error(ResultEnum.VALID_ERROR.getCode(),"订单号不存在");
        }
        if(!"1".equals(tbOrder.getStatus())) {
            return ResultUtil.success(tbOrder);
        }
        long remainingTime = ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(), tbOrder.getCreateDate().plusMinutes(payTimeOut));
        log.info("tradeQuery remainingTime:{{}}",remainingTime);
        String remainingTimeStr = (remainingTime>=0?remainingTime:-1)+"";
        log.info("tradeQuery remainingTime:{{}}",remainingTime);
        if(remainingTime<=0&&OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())){
            tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
            tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
            tbOrderMapper.updateById(tbOrder);
            return ResultUtil.success(tbOrder);
        }
        log.info("tradeQuery tbOrder:{{}}",tbOrder);
        tbOrder.setRemainingTime(remainingTimeStr);
        return ResultUtil.success(tbOrder);
//        Result result = payFeignClient.tradeQuery(outTradeNo, tradeNo);
//        if(ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
//            TbOrder data = JSON.parseObject(JSONObject.toJSONString(result.getData(),true),TbOrder.class);
//            if( "1".equals(tbOrder.getStatus())) {
//                long remainingTime = ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(), data.getCreateDate().plusMinutes(payTimeOut));
//                data.setRemainingTime((remainingTime>=0?remainingTime:-1)+"");
//            }
//            return ResultUtil.success(data);
//        }
//        return result;
    }

    @Override
    public Result getVipMenu(String userId) {
        return ResultUtil.success(baseMapper.selectList(null));
    }

    @Override
    public Result getOrderList(String userId,String status,String current,String queryParam,String platform) {
        log.info("getOrderList userId:{{}}",userId);
        log.info("getOrderList status:{{}}",status);
        log.info("getOrderList current:{{}}",current);
        log.info("getOrderList queryParam:{{}}",queryParam);
        log.info("getOrderList platform:{{}}",platform);
        QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("a.createBy", userId);
        //queryWrapper.ne("a.orderType", "1");
        queryWrapper.eq("a.orderType", "2");
        if(!StringUtils.isEmpty(status)) {
            queryWrapper.eq("a.status", status);
           /* //待付款过滤付款超过半小时的订单
            if(OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(status)){
                queryWrapper.le("TIMESTAMPDIFF(MINUTE,a.createDate,NOW())",30);
            }*/
        }
        queryWrapper.ne("a.status", OrderStatusEnum.ORDER_CLOSED.getCode());

        if (StringUtils.isNotEmpty(queryParam)){
            queryWrapper.and(wrapper ->
                    wrapper.like("a.orderNo",queryParam).or()
                            .like("c.goodsName",queryParam).or()
                            .like("d.course",queryParam).or()
                            .like("e1.courseName",queryParam).or()
                            .like("f.vipName",queryParam));
        }
        if(StringUtils.isNotEmpty(platform)){
            queryWrapper.notInSql("a.id","select b.id from tb_order b where b.userId ='"+userId+"'and b.platform <>'"+platform+"' and b.platform <>'' and b.status = '1'");
        }

        queryWrapper.notInSql("a.id","select b.id from tb_order b where b.createBy='"+userId+"' and b.orderType=2 and b.status = 1 and TIMESTAMPDIFF(MINUTE,b.createDate,NOW()) > 30");
        queryWrapper.groupBy("a.id");
        queryWrapper.orderByDesc("a.createDate");
        //分页
        Page<TbOrder> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
//        Page<TbOrder> tbOrderPage = tbOrderMapper.selectPage(partPage,queryWrapper);
//        List<TbOrder> tbOrderList = tbOrderPage.getRecords();
        List<TbOrder> tbOrderList = tbOrderMapper.getOrderList(partPage, queryWrapper);
        log.info("getOrderList tbOrderList:{{}}",tbOrderList);
        List<UserOrderDto> userOrderDtoList = new ArrayList<>();
        for(TbOrder tbOrder:tbOrderList) {
            if(OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus()) &&tbOrder.getCreateDate().isAfter(DateUtils.getCurrentLocalDateTime().minusMinutes(payTimeOut))) {
                tbOrder.setRemainingTime((ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(),tbOrder.getCreateDate().plusMinutes(payTimeOut)))+"");
            }
            List<TbOrderItem> orderItems = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                    .eq("tbOrderId", tbOrder.getId()));
            UserOrderDto userOrderDto = new UserOrderDto();
            BeanUtils.copyProperties(tbOrder,userOrderDto);
            log.info("getOrderList tbOrder:{{}}",tbOrder);
            log.info("getOrderList userOrderDto:{{}}",userOrderDto);
            List<UserOrderItemDto> userOrderItemDtoList =new ArrayList<>();
            for(TbOrderItem tbOrderItem : orderItems) {
                UserOrderItemDto userOrderItemDto = new UserOrderItemDto();
                userOrderItemDto.setId(tbOrderItem.getGoodsId());
                userOrderItemDto.setPrice(tbOrderItem.getPrice());
                userOrderItemDto.setQuantity(tbOrderItem.getQuantity());
                if(OrderTypeEnum.GOODS.getCode().equals(tbOrder.getOrderType())) {
                    //配件商品
                    GoodsEntity goodsEntity = goodsMapper.selectById(orderItems.get(0).getGoodsId());
                    userOrderItemDto.setGoodsName(goodsEntity.getGoodsName());
                    userOrderItemDto.setGoodsDesc(goodsEntity.getGoodsDesc());
                    //根据id获取图片信息
                    Attach aPi = attachMapper.selectById(goodsEntity.getCoverAttachId());
                    if (aPi != null) {
                        userOrderItemDto.setCoverUrl(fileImagesPath + aPi.getFilePath());
                    }
                    //获取收货人详情
                    UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(tbOrder.getUserReceiveAddressId());
                    userOrderDto.setUserReceiveAddress(userReceiveAddressEntity);

                }/*else if(OrderTypeEnum.COURSE.getCode().equals(tbOrder.getOrderType())){
                    //特色课程
                    CourseHead courseHead = courseHeadMapper.selectById(tbOrderItem.getGoodsId());
                    userOrderItemDto.setGoodsName(courseHead.getCourse());
                    userOrderItemDto.setGoodsDesc(courseHead.getRemark());
                    //根据id获取图片信息
                    Attach aPi = attachMapper.selectById(courseHead.getPicAttachId());
                    if (aPi != null) {
                        userOrderItemDto.setCoverUrl(fileImagesPath + aPi.getFilePath());
                    }
                }else if(OrderTypeEnum.LIVE.getCode().equals(tbOrder.getOrderType())){
                    //直播预约
                    UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(tbOrderItem.getGoodsId());
                    log.trace("UserCoachTime  的IFD是...................."+tbOrderItem.getGoodsId() );
                    UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(userCoachTimeEntity.getCoachCourseId());
                    userOrderItemDto.setGoodsName(userCoachCourseEntity.getCourseName());
                    userOrderItemDto.setGoodsDesc(tbOrder.getDescription());
                    userOrderItemDto.setCoverUrl("");
                }else{
                    //VIP会员
                    VipMenu vipMenu = vipMenuMapper.selectById(tbOrderItem.getGoodsId());
                    userOrderItemDto.setGoodsName(vipMenu.getVipName());
                    userOrderItemDto.setGoodsDesc(vipMenu.getDescription());
//                    //根据id获取图片信息
//                    Attach aPi = attachMapper.selectById(vipMenu.get);
//                    if (aPi != null) {
//                        userOrderItemDto.setCoverUrl(fileImagesPath + aPi.getFilePath());
//                    }
                }*/
                userOrderItemDtoList.add(userOrderItemDto);
            }
            userOrderDto.setUserOrderItemDtoList(userOrderItemDtoList);
            userOrderDtoList.add(userOrderDto);
        }
        Page<UserOrderDto> orderDtoPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        orderDtoPage.setTotal(tbOrderList.size());
        orderDtoPage.setPages(partPage.getPages());
        orderDtoPage.setRecords(userOrderDtoList);
        log.info("getOrderList userOrderDtoList:{{}}",userOrderDtoList);
        return ResultUtil.success(orderDtoPage);
    }

    @Override
    public Result createOfficialAccountUnifiedOrder(String openid, String orderNo, BigDecimal amount) {
        try {
            log.info("createOfficialAccountUnifiedOrder openid:{{}}",openid);
            log.info("createOfficialAccountUnifiedOrder orderNo:{{}}",orderNo);
            log.info("createOfficialAccountUnifiedOrder amount:{{}}",amount);
            return payFeignClient.createOfficialAccountUnifiedOrder(openid,orderNo,amount);
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
