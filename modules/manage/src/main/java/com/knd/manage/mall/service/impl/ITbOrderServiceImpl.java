package com.knd.manage.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.RoleEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.admin.entity.Admin;
import com.knd.manage.admin.mapper.AdminMapper;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.entity.CourseHead;
import com.knd.manage.course.mapper.CourseHeadMapper;
import com.knd.manage.homePage.entity.AmapAdCodeEntity;
import com.knd.manage.homePage.mapper.AmapAdCodeMapper;
import com.knd.manage.mall.dto.CodeDto;
import com.knd.manage.mall.dto.OrderConsultingDTO;
import com.knd.manage.mall.dto.OrderDto;
import com.knd.manage.mall.dto.OrderItemDto;
import com.knd.manage.mall.entity.*;
import com.knd.manage.mall.mapper.*;
import com.knd.manage.mall.request.*;
import com.knd.manage.mall.service.ITbOrderService;
import com.knd.manage.mall.service.feignInterface.CoachFeignClient;
import com.knd.manage.mall.service.feignInterface.FrontInfoFeignClient;
import com.knd.manage.mall.service.feignInterface.PayFeignClient;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.service.IUserReceiveAddressService;
import com.knd.manage.user.service.IUserService;
import com.knd.permission.bean.Token;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@Log4j2
public class ITbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements ITbOrderService {

     @Resource
    private GoodsAttrValueMapper goodsAttrValueMapper;

     @Resource
    private UserReceiveAddressMapper userReceiveAddressMapper;

     @Resource
    private GoodsMapper goodsMapper;

     @Resource
    private TbOrderItemMapper tbOrderItemMapper;

     @Resource
    private OrderIcMapper orderIcMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private VipMenuMapper vipMenuMapper;

    @Resource
    private CourseHeadMapper courseHeadMapper;

    @Resource
    private AmapAdCodeMapper amapAdCodeMapper;

    @Resource
    private IUserService iUserService;

    @Resource
    private IUserReceiveAddressService iUserReceiveAddressService;

    @Resource
    private FrontInfoFeignClient frontInfoFeignClient;

    @Resource
    private PayFeignClient payFeignClient;

    @Resource
    private UserCoachTimeMapper userCoachTimeMapper;

    @Resource
    private CoachFeignClient coachFeignClient;

    @Resource
    private IAttachService attachService;

    @Resource
    private OrderConsultingMapper orderConsultingMapper;

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;


    @Override
    public Result getOrderById(String orderId) {
        OrderDto orderDto = new OrderDto();
        TbOrder order = baseMapper.selectById(orderId);
        if (StringUtils.isEmpty(order)) {
            return ResultUtil.error("U0999", "订单无数据");
        }
        copyProperties(order, orderDto);
        List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                .eq("tbOrderId", order.getId()));
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for (TbOrderItem tbOrderItem : tbOrderItemList) {
            OrderItemDto orderItemDto = new OrderItemDto();
            List<GoodsAttrValueEntity> goodsAttrValueEntities = goodsAttrValueMapper.selectList(
                    new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", tbOrderItem.getGoodsId())
                            .orderByAsc("sort"));
            GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItem.getGoodsId());
            copyProperties(goodsEntity, orderItemDto);
            orderItemDto.setGoodsAttrValueEntityList(goodsAttrValueEntities);
            orderItemDtoList.add(orderItemDto);


        }
        orderDto.setOrderItemDtoList(orderItemDtoList);
        OrderIcEntity orderIcEntity = orderIcMapper.selectOne(new QueryWrapper<OrderIcEntity>()
                .eq("tbOrderId", order.getId()).eq("deleted", "0"));
        if (!StringUtils.isEmpty(orderIcEntity.getPersonId())) {
            Admin admin = adminMapper.selectById(orderIcEntity.getPersonId());
            orderIcEntity.setPersonName(admin.getNickName());
            orderIcEntity.setPersonMobile(admin.getMobile());
        }

        orderDto.setOrderIcEntity(orderIcEntity);
        UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(order.getUserReceiveAddressId());
        orderDto.setUserReceiveAddressEntity(userReceiveAddressEntity);
        return ResultUtil.success(orderDto);
    }

    @Override
    public Result getOrderList(OrderListQueryParam orderListQueryParam) {
        log.info("--------------------------获取后管订单列表---------------------------");
        log.info("getOrderList status:{{}}",orderListQueryParam.getStatus());
        log.info("getOrderList orderCreateStartTime:{{}}",orderListQueryParam.getOrderCreateStartTime());
        log.info("getOrderList orderCreateEndTime:{{}}",orderListQueryParam.getOrderCreateEndTime());
        log.info("getOrderList orderNo:{{}}",orderListQueryParam.getOrderNo());
        log.info("getOrderList current:{{}}",orderListQueryParam.getCurrent());
        Page<TbOrder> page = new Page<>(Integer.parseInt(orderListQueryParam.getCurrent()), PageInfo.pageSize);
        QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(orderListQueryParam.getOrderNo())) {
            tbOrderQueryWrapper.like("orderNo", orderListQueryParam.getOrderNo());
        }
        if (!StringUtils.isEmpty(orderListQueryParam.getStatus())) {
            tbOrderQueryWrapper.eq("status", orderListQueryParam.getStatus());
        }
        if (!StringUtils.isEmpty(orderListQueryParam.getLogisticsCompanies())) {
            tbOrderQueryWrapper.eq("logisticsCompanies", orderListQueryParam.getLogisticsCompanies());
        }
        if (!StringUtils.isEmpty(orderListQueryParam.getTrackingNumber())) {
            tbOrderQueryWrapper.like("trackingNumber", orderListQueryParam.getTrackingNumber());
        }
        if (!StringUtils.isEmpty(orderListQueryParam.getOrderCreateStartTime())) {
            tbOrderQueryWrapper.ge("orderCreateTime",orderListQueryParam.getOrderCreateStartTime());
        }
        if (!StringUtils.isEmpty(orderListQueryParam.getOrderCreateEndTime())) {
            tbOrderQueryWrapper.le("orderCreateTime",orderListQueryParam.getOrderCreateEndTime());
        }
        //待付款过滤付款超过半小时的订单
        tbOrderQueryWrapper.notInSql("id","select b.id from tb_order b where b.orderType=2 and ((b.status = 1 and TIMESTAMPDIFF(MINUTE,b.createDate,NOW()) > 30) or b.status = 5) ");
        //tbOrderQueryWrapper.eq("orderType","2");
        tbOrderQueryWrapper.orderByDesc("createDate");
        Page<TbOrder> p = this.baseMapper.selectPage(page, tbOrderQueryWrapper);
        log.info("getOrderList TbOrder:{{}}",p);
        List<TbOrder> records = p.getRecords();
        List<OrderDto> newRecords = new ArrayList<>();
        for (TbOrder order : records) {
            OrderDto orderDto = new OrderDto();
            copyProperties(order, orderDto);
            if (!StringUtils.isEmpty(order.getUserId())) {
                User user = iUserService.getById(order.getUserId());

                orderDto.setCreateBy(user != null ? user.getNickName() : "");
            }
            List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                    .eq("tbOrderId", order.getId()));
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (TbOrderItem tbOrderItem : tbOrderItemList) {
                OrderItemDto orderItemDto = new OrderItemDto();
                if ("2".equals(order.getOrderType()) || "4".equals(order.getOrderType())) {
                    List<GoodsAttrValueEntity> goodsAttrValueEntities = goodsAttrValueMapper.selectList(
                            new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", tbOrderItem.getGoodsId())
                                    .orderByAsc("sort"));
                    UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(order.getUserReceiveAddressId());
                    orderDto.setUserReceiveAddressEntity(userReceiveAddressEntity);
                    GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItem.getGoodsId());
                    if(null!=goodsEntity){
                        copyProperties(goodsEntity, orderItemDto);
                    }
                    orderItemDto.setGoodsAttrValueEntityList(goodsAttrValueEntities);

                } else if ("3".equals(order.getOrderType())) {
                    //特色课程
                    CourseHead courseHead = courseHeadMapper.selectById(tbOrderItem.getGoodsId());
                    orderItemDto.setGoodsName(courseHead.getCourse());
                    orderItemDto.setGoodsDesc(courseHead.getRemark());
                    orderItemDto.setQuantity("1");
                    orderItemDto.setPrice(courseHead.getAmount());
//                    //根据id获取图片信息
//                    Attach aPi = attachMapper.selectById(courseHead.getPicAttachId());
//                    if (aPi != null) {
//                        userOrderItemDto.setCoverUrl(fileImagesPath + aPi.getFilePath());
//                    }
                } else if ("1".equals(order.getOrderType())) {
                    VipMenu vipMenu = vipMenuMapper.selectById(tbOrderItem.getGoodsId());
                    orderItemDto.setGoodsName(vipMenu.getVipName());
                    orderItemDto.setGoodsDesc(vipMenu.getDescription());
                    orderItemDto.setPrice(vipMenu.getPrice());
                    orderItemDto.setQuantity("1");
                }
                orderItemDtoList.add(orderItemDto);
            }
            orderDto.setOrderItemDtoList(orderItemDtoList);
            newRecords.add(orderDto);
        }
        Page<OrderDto> objectPage = new Page<>();
        objectPage.setCurrent(page.getCurrent());
        objectPage.setRecords(newRecords);
        objectPage.setSize(PageInfo.pageSize);
        objectPage.setTotal(p.getTotal());
        objectPage.setPages(p.getPages());
        return ResultUtil.success(objectPage);
    }

    @Override
    public Result getOrderInfo(String goodsId) {
        return null;
    }

    @Override
    public Result updateOrderStatus(String userId, String status, String id, String trackingNumber,String logisticsCompanies,String serialNo) {
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
        LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
        if(StringUtils.isNotEmpty(trackingNumber)){
            eq.setTrackingNumber(trackingNumber);
        }
        if(StringUtils.isNotEmpty(logisticsCompanies)){
            eq.setLogisticsCompanies(logisticsCompanies);
        }
        if(StringUtils.isNotEmpty(serialNo)){
            eq.setSerialNo(serialNo);
        }
        if (OrderStatusEnum.DELIVER_FINISHED.getCode().equals(status)) {
            //已发货，待确认收货
            eq.setStatus(status);
            //发送收货确认码短信至客户手机号，将来提供给派送员以便确认收货
            UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(eq.getUserReceiveAddressId());
            CodeDto codeDto = new CodeDto();
            codeDto.setCodeType("30");//收货确认码
            codeDto.setMobile(userReceiveAddressEntity.getPhone());
            //验证码生成
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            codeDto.setCode(verifyCode);
           // Result result = frontInfoFeignClient.sendReceivingGoodsConfirmSms(codeDto);
         //   if (!ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
           //     return result;
          //  }
            eq.setReceivingCode(verifyCode);
            eq.setDeliveryTime(currentLocalDateTime);
            // eq.setStatus(status);
            eq.setLastModifiedBy(userId);
            eq.setLastModifiedDate(currentLocalDateTime);
            baseMapper.updateById(eq);
        } else if (OrderStatusEnum.ORDER_FINISHED.getCode().equals(status)) {
         /*   if (!eq.getReceivingCode().equals(recevingCode)) {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "收货确认码不正确");
            }*/
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
                    coachFeignClient.cancelOrderSuccess(request);
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

    @Override
    public Result createOrderFromOffline(CreateOfflineOrderRequest createOfflineOrderRequest) {
        String receiveAddressId = iUserReceiveAddressService.add(createOfflineOrderRequest.getUserReceiveAddressRequest());
        TbOrder tbOrder = new TbOrder();
        copyProperties(createOfflineOrderRequest, tbOrder);
        tbOrder.setUserReceiveAddressId(receiveAddressId);
        tbOrder.setMobile(createOfflineOrderRequest.getUserReceiveAddressRequest().getPhone());
        tbOrder.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        tbOrder.setCreateDate(DateUtils.getCurrentLocalDateTime());
        tbOrder.setPaymentTime(tbOrder.getCreateDate());
        if(StringUtils.isEmpty(createOfflineOrderRequest.getUserId())){
            createOfflineOrderRequest.setUserId(UserUtils.getUserId());
        }
        tbOrder.setCreateBy(createOfflineOrderRequest.getUserId());
        tbOrder.setDeleted("0");
        tbOrder.setOrderNo(StringUtils.getOutTradeNo());
        baseMapper.insert(tbOrder);
        //创建订单子项
        for (TbOrderItemRequest tbOrderItemRequest : createOfflineOrderRequest.getTbOrderItemRequestList()) {
            TbOrderItem tbOrderItem = new TbOrderItem();
            copyProperties(tbOrderItemRequest, tbOrderItem);
            tbOrderItem.setTbOrderId(tbOrder.getId());
            tbOrderItem.setId(null);
            GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItemRequest.getGoodsId());
            tbOrderItem.setPrice(goodsEntity.getPrice());
            tbOrderItemMapper.insert(tbOrderItem);

        }
        //插入安装信息
        if ("1".equals(createOfflineOrderRequest.getInstallFlag())) {
            OrderIcEntity orderIcEntity = new OrderIcEntity();
            copyProperties(createOfflineOrderRequest.getOrderIcRequest(), orderIcEntity);
            orderIcEntity.setId(null);
            orderIcEntity.setTbOrderId(tbOrder.getId());
            orderIcMapper.insert(orderIcEntity);
        }
        return ResultUtil.success();
    }

    @Override
    public Result getOrderList4App(OrderQueryParam orderQueryParam, Token token) {
        Page<TbOrder> page = new Page<>(Integer.parseInt(orderQueryParam.getCurrent()), Integer.parseInt(orderQueryParam.getPageSize()));
        QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(orderQueryParam.getKeyWord())) {
            tbOrderQueryWrapper.and(e -> e.like("tr.orderNo", orderQueryParam.getKeyWord())
                    .or(q -> q.like("tr.mobile", orderQueryParam.getKeyWord())));
        }

        tbOrderQueryWrapper.and(e -> e.eq("tr.installFlag", "1").in("tr.orderType", "2", "4"));
//        tbOrderQueryWrapper.in("tr.orderType","2","4");
        if (!StringUtils.isEmpty(orderQueryParam.getInstallStatus())) {
            tbOrderQueryWrapper.eq("tr.installStatus", orderQueryParam.getInstallStatus());
        }
        if (RoleEnum.SALE.getCode().equals(token.getRoleFlag())) {
            tbOrderQueryWrapper.eq("tr.createBy", token.getUserId());
        }
        if (RoleEnum.ERECTOR.getCode().equals(token.getRoleFlag())) {
            tbOrderQueryWrapper.eq("tc.personId", token.getUserId());
        }
        if (RoleEnum.CUSTOMER_SERVICE.getCode().equals(token.getRoleFlag())) {
            tbOrderQueryWrapper.in("tr.status",
                    OrderStatusEnum.PAY_SUCCESS.getCode(), OrderStatusEnum.ORDER_FINISHED.getCode());
        }
        //tbOrderQueryWrapper.eq("orderType","2");
        tbOrderQueryWrapper.orderByDesc("tr.createDate");
        Page<TbOrder> p = this.baseMapper.selectOrderPage4App(page, tbOrderQueryWrapper);
        List<TbOrder> records = p.getRecords();
        List<OrderDto> newRecords = new ArrayList<>();
        for (TbOrder order : records) {
            OrderDto orderDto = new OrderDto();
            copyProperties(order, orderDto);
            List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                    .eq("tbOrderId", order.getId()));
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (TbOrderItem tbOrderItem : tbOrderItemList) {
                OrderItemDto orderItemDto = new OrderItemDto();
                List<GoodsAttrValueEntity> goodsAttrValueEntities = goodsAttrValueMapper.selectList(
                        new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", tbOrderItem.getGoodsId())
                                .orderByAsc("sort"));
                GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItem.getGoodsId());
                copyProperties(goodsEntity, orderItemDto);
                orderItemDto.setGoodsAttrValueEntityList(goodsAttrValueEntities);
                orderItemDtoList.add(orderItemDto);


            }
            orderDto.setOrderItemDtoList(orderItemDtoList);
            OrderIcEntity orderIcEntity = orderIcMapper.selectOne(new QueryWrapper<OrderIcEntity>()
                    .eq("tbOrderId", order.getId()).eq("deleted", "0"));
            if (!StringUtils.isEmpty(orderIcEntity.getPersonId())) {
                Admin admin = adminMapper.selectById(orderIcEntity.getPersonId());
                orderIcEntity.setPersonName(admin.getNickName());
                orderIcEntity.setPersonMobile(admin.getMobile());
            }

            orderDto.setOrderIcEntity(orderIcEntity);

            UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(order.getUserReceiveAddressId());
            orderDto.setUserReceiveAddressEntity(userReceiveAddressEntity);
            newRecords.add(orderDto);
        }

        Page<OrderDto> objectPage = new Page<>();
        objectPage.setCurrent(page.getCurrent());
        objectPage.setRecords(newRecords);
        objectPage.setSize(PageInfo.pageSize);
        objectPage.setTotal(p.getTotal());
        objectPage.setPages(p.getPages());
        return ResultUtil.success(objectPage);
    }

    private Result getCustomerServiceOrderList(
            OrderQueryParam orderQueryParam, Token token) {
        Page<TbOrder> page = new Page<>(Integer.parseInt(orderQueryParam.getCurrent()), Integer.parseInt(orderQueryParam.getPageSize()));
        QueryWrapper<TbOrder> tbOrderQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(orderQueryParam.getKeyWord())) {
            tbOrderQueryWrapper.like("tr.orderNo", orderQueryParam.getKeyWord())
                    .or(q -> q.like("tr.mobile", orderQueryParam.getKeyWord()));
        }
        if (RoleEnum.SALE.getCode().equals(token.getRoleFlag())) {
            tbOrderQueryWrapper.eq("tr.createBy", token.getUserId());
        }
        if (RoleEnum.ERECTOR.getCode().equals(token.getRoleFlag())) {
            tbOrderQueryWrapper.eq("tc.personId", token.getUserId());

        }
        //tbOrderQueryWrapper.eq("orderType","2");
        tbOrderQueryWrapper.orderByDesc("tr.createDate");
        Page<TbOrder> p = this.baseMapper.selectOrderPage4App(page, tbOrderQueryWrapper);
        List<TbOrder> records = p.getRecords();
        List<OrderDto> newRecords = new ArrayList<>();
        for (TbOrder order : records) {
            OrderDto orderDto = new OrderDto();
            copyProperties(order, orderDto);
            List<TbOrderItem> tbOrderItemList = tbOrderItemMapper.selectList(new QueryWrapper<TbOrderItem>()
                    .eq("tbOrderId", order.getId()));
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (TbOrderItem tbOrderItem : tbOrderItemList) {
                OrderItemDto orderItemDto = new OrderItemDto();
                List<GoodsAttrValueEntity> goodsAttrValueEntities = goodsAttrValueMapper.selectList(
                        new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", tbOrderItem.getGoodsId())
                                .orderByAsc("sort"));
                GoodsEntity goodsEntity = goodsMapper.selectById(tbOrderItem.getGoodsId());
                copyProperties(goodsEntity, orderItemDto);
                orderItemDto.setGoodsAttrValueEntityList(goodsAttrValueEntities);
                orderItemDtoList.add(orderItemDto);


            }
            orderDto.setOrderItemDtoList(orderItemDtoList);
            OrderIcEntity orderIcEntity = orderIcMapper.selectOne(new QueryWrapper<OrderIcEntity>()
                    .eq("tbOrderId", order.getId()).eq("deleted", "0"));
            orderDto.setOrderIcEntity(orderIcEntity);
            UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(order.getUserReceiveAddressId());
            orderDto.setUserReceiveAddressEntity(userReceiveAddressEntity);
            newRecords.add(orderDto);
        }

        Page<OrderDto> objectPage = new Page<>();
        objectPage.setCurrent(page.getCurrent());
        objectPage.setRecords(newRecords);
        objectPage.setSize(PageInfo.pageSize);
        objectPage.setTotal(p.getTotal());
        objectPage.setPages(p.getPages());
        return ResultUtil.success(objectPage);
    }

    @Override
    public Result addOrUpdateOrderConsulting(OrderConsultingRequest orderConsultingRequest) {
        OrderConsultingEntity orderConsultingEntity = new OrderConsultingEntity();
        copyProperties(orderConsultingRequest, orderConsultingEntity);
        orderConsultingEntity.setDeleted("0");
        orderConsultingEntity.setPicAttachId(orderConsultingRequest.getHeadPicUrlId());
        log.info("addOrUpdateOrderConsulting PictureUrl", orderConsultingRequest.getPictureUrl());
        log.info("addOrUpdateOrderConsulting PicAttachName", orderConsultingRequest.getPictureUrl().getPicAttachName());
        log.info("addOrUpdateOrderConsulting PicAttachNewName", orderConsultingRequest.getPictureUrl().getPicAttachNewName());
        log.info("addOrUpdateOrderConsulting PicAttachSize", orderConsultingRequest.getPictureUrl().getPicAttachSize());
        if (StringUtils.isEmpty(orderConsultingRequest.getHeadPicUrlId())) {
            //保存选中图片
            Attach attach = attachService.saveAttach(orderConsultingRequest.getUserId(), orderConsultingRequest.getPictureUrl().getPicAttachName()
                    , orderConsultingRequest.getPictureUrl().getPicAttachNewName(), orderConsultingRequest.getPictureUrl().getPicAttachSize());
            String attachId = attach.getId();
            log.info("addOrUpdateOrderConsulting attachId", attachId);
            orderConsultingEntity.setPicAttachId(attachId);
        }
        if (null == orderConsultingEntity.getId()) {
            orderConsultingEntity.setId(UUIDUtil.getShortUUID());
            orderConsultingEntity.setCreateBy(orderConsultingRequest.getUserId());
            orderConsultingEntity.setCreateDate(LocalDateTime.now());
            orderConsultingMapper.insert(orderConsultingEntity);
        } else {
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
        if (StringUtils.isNotEmpty(queryOrderConsultingRequest.getKeyWord())) {
            orderConsultingEntityQueryWrapper.and(e -> e.like("userName", queryOrderConsultingRequest.getKeyWord())
                    .or(q -> q.like("phone", queryOrderConsultingRequest.getKeyWord()))
                    .or(q -> q.like("installAddress", queryOrderConsultingRequest.getKeyWord())))
                    .or(q -> q.like("detailAddress", queryOrderConsultingRequest.getKeyWord()))
                    .or(q -> q.like("installFloor", queryOrderConsultingRequest.getKeyWord()));

        }
        if (StringUtils.isNotEmpty(queryOrderConsultingRequest.getIsElevator())) {
            orderConsultingEntityQueryWrapper.eq("isElevator", queryOrderConsultingRequest.getIsElevator());
        }
        if (StringUtils.isNotEmpty(queryOrderConsultingRequest.getBuyPlatform())) {
            orderConsultingEntityQueryWrapper.eq("buyPlatform", queryOrderConsultingRequest.getBuyPlatform());
        }
        if (StringUtils.isNotEmpty(queryOrderConsultingRequest.getInstallStartTime())) {
            orderConsultingEntityQueryWrapper.ge("installStartTime", queryOrderConsultingRequest.getInstallStartTime());
        }
        if (StringUtils.isNotEmpty(queryOrderConsultingRequest.getInstallEndTime())) {
            orderConsultingEntityQueryWrapper.le("installEndTime", queryOrderConsultingRequest.getInstallEndTime());
        }


        orderConsultingEntityQueryWrapper.eq("deleted", "0");
        orderConsultingEntityQueryWrapper.orderByDesc("createDate");
        Page<OrderConsultingEntity> orderConsultingEntityPage = orderConsultingMapper.selectPage(page, orderConsultingEntityQueryWrapper);
        List<OrderConsultingEntity> records = orderConsultingEntityPage.getRecords();
        Page<OrderConsultingDTO> orderConsultingDTOPage = new Page<>();
        ArrayList<OrderConsultingDTO> orderConsultingDTOS = new ArrayList<>();
        records.stream().forEach(r -> {
                    OrderConsultingDTO orderConsultingDTO = new OrderConsultingDTO();
                    BeanUtils.copyProperties(r, orderConsultingDTO);
                    orderConsultingDTO.setPicAttachUrl(getImgDto(r.getPicAttachId()));
                    orderConsultingDTOS.add(orderConsultingDTO);
                }
        );
        orderConsultingDTOPage.setRecords(orderConsultingDTOS);
        ResponseDto dto = ResponseDto.<OrderConsultingDTO>builder().total((int) orderConsultingDTOPage.getTotal()).resList(orderConsultingDTOS).build();
        return ResultUtil.success(dto);
    }

    public ImgDto getImgDto(String urlId) {
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

    @Transactional
    @Override
    public Result saveOrderCousultingByBatch(InputStream inputStream, String originalFilename) {
        try {
            log.info("------------我要开始批量导入订单咨询表啦-------------------------");
            List<OrderConsultingEntity> list = getListByExcel(inputStream, originalFilename);
            log.info("saveOrderCousultingByBatch list:{{}}", list);
            for (OrderConsultingEntity OrderConsultingEntity : list) {
                QueryWrapper<OrderConsultingEntity> objectQueryWrapper = new QueryWrapper<>();
                objectQueryWrapper.eq("phone", OrderConsultingEntity.getPhone());
                //objectQueryWrapper.eq("userName", OrderConsultingEntity.getUserName());
                objectQueryWrapper.eq("deleted", "0");
                List<OrderConsultingEntity> OrderConsultingEntity1 = orderConsultingMapper.selectList(objectQueryWrapper);
                log.info("saveOrderCousultingByBatch OrderConsultingEntity1:{{}}", OrderConsultingEntity1);
                if (OrderConsultingEntity1.size() > 0) {
                    OrderConsultingEntity.setLastModifiedBy(UserUtils.getUserId());
                    OrderConsultingEntity.setLastModifiedDate(LocalDateTime.now());
                    orderConsultingMapper.update(OrderConsultingEntity, objectQueryWrapper);
                    continue;
                }
                OrderConsultingEntity.setId(UUIDUtil.getShortUUID());
                OrderConsultingEntity.setCreateBy(UserUtils.getUserId());
                OrderConsultingEntity.setCreateDate(LocalDateTime.now());
                OrderConsultingEntity.setLastModifiedBy(UserUtils.getUserId());
                OrderConsultingEntity.setLastModifiedDate(LocalDateTime.now());
                OrderConsultingEntity.setDeleted("0");
                log.info("saveOrderCousultingByBatch OrderConsultingEntity:{{}}", OrderConsultingEntity);
                int insert = orderConsultingMapper.insert(OrderConsultingEntity);
                log.info("saveOrderCousultingByBatch insert:{{}}", insert);
                log.info("------------导入单个订单咨询表-------------------------");

            }
            log.info("------------批量导入订单咨询表结束-------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FILE_NONE_ERROR);
        }
        return ResultUtil.success();
    }

    @Override
    public Result getUserReceiveAddressById(String id) {
        UserReceiveAddressEntity userReceiveAddressEntity = iUserReceiveAddressService.getById(id);
        //省拼接
        QueryWrapper<AmapAdCodeEntity> provinceQueryWrapper = new QueryWrapper<>();
        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
        provinceQueryWrapper.eq("deleted", "0");
        provinceQueryWrapper.last("limit 1");
        AmapAdCodeEntity provinceAmapAdCodeEntity = amapAdCodeMapper.selectOne(provinceQueryWrapper);
        if(StringUtils.isNotEmpty(provinceAmapAdCodeEntity)){
            String provinceAdCode =provinceAmapAdCodeEntity.getAdcode();
            userReceiveAddressEntity.setProvince(userReceiveAddressEntity.getProvince()+","+provinceAdCode);
        }

        //市拼接
        QueryWrapper<AmapAdCodeEntity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
        cityQueryWrapper.eq("deleted", "0");
        cityQueryWrapper.last("limit 1");
        AmapAdCodeEntity cityAmapAdCodeEntity = amapAdCodeMapper.selectOne(cityQueryWrapper);
        if(StringUtils.isNotEmpty(cityAmapAdCodeEntity)) {
            String cityAdCode = cityAmapAdCodeEntity.getAdcode();
            userReceiveAddressEntity.setCity(userReceiveAddressEntity.getCity() + "," + cityAdCode);
        }
        //区拼接
        QueryWrapper<AmapAdCodeEntity> regionWrapper = new QueryWrapper<>();
        regionWrapper.eq("name",userReceiveAddressEntity.getRegion());
        regionWrapper.eq("deleted", "0");
        regionWrapper.last("limit 1");
        AmapAdCodeEntity regionAmapAdCodeEntity = amapAdCodeMapper.selectOne(regionWrapper);
        if(StringUtils.isNotEmpty(regionAmapAdCodeEntity)) {
            String regionAdCode = regionAmapAdCodeEntity.getAdcode();
            userReceiveAddressEntity.setRegion(userReceiveAddressEntity.getRegion() + "," + regionAdCode);
        }
        return ResultUtil.success(userReceiveAddressEntity);
    }

    @Override
    public Result saveUserReceiveAddressEntity(UserReceiveAddressRequest userReceiveAddressRequest) {
        log.info("saveUserReceiveAddressEntity userReceiveAddressRequest:{{}}",userReceiveAddressRequest);
        UserReceiveAddressEntity userReceiveAddressEntity = new UserReceiveAddressEntity();
        BeanUtils.copyProperties(userReceiveAddressRequest,userReceiveAddressEntity);
        userReceiveAddressEntity.setDeleted("0");
        if("1".equals(userReceiveAddressEntity.getDefaultStatus())) {
            cleanOldDefaultAddress(userReceiveAddressEntity.getUserId());
        }
        if(!"1".equals(userReceiveAddressEntity.getDefaultStatus())&&iUserReceiveAddressService.count(new QueryWrapper<UserReceiveAddressEntity>()
                .eq("userId", userReceiveAddressEntity.getUserId())
                .eq("deleted", "0"))<=0) {
            userReceiveAddressEntity.setDefaultStatus("1");
        }
        if(StringUtils.isNotEmpty(userReceiveAddressEntity.getId())){
            UserReceiveAddressEntity old = iUserReceiveAddressService.getById(userReceiveAddressEntity.getId());
            log.info("saveUserReceiveAddressEntity old:{{}}",old);
            if("0".equals(userReceiveAddressEntity.getDefaultStatus())
                    &&"1".equals(old.getDefaultStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"该地址是默认地址，请先更换默认收货地址");
            }
            userReceiveAddressEntity.setLastModifiedBy(userReceiveAddressRequest.getUserId());
            userReceiveAddressEntity.setLastModifiedDate(LocalDateTime.now());
            iUserReceiveAddressService.updateById(userReceiveAddressEntity);
        }else {
            userReceiveAddressEntity.setId(UUIDUtil.getShortUUID());
            userReceiveAddressEntity.setCreateBy(userReceiveAddressRequest.getUserId());
            userReceiveAddressEntity.setCreateDate(LocalDateTime.now());
            iUserReceiveAddressService.save(userReceiveAddressEntity);
        }
        return ResultUtil.success(userReceiveAddressEntity);
    }



    /**
     * 处理上传的文件
     *
     * @param in
     * @param fileName
     * @return
     * @throws Exception
     */
    public List getListByExcel(InputStream in, String fileName) throws Exception {
        log.info("----------------getListByExcel开始----------------------");
        log.info("getListByExcel InputStream:{{}}", in);
        log.info("getListByExcel fileName:{{}}", fileName);

        //创建Excel工作薄
        Workbook work = this.getWorkbook(in, fileName);
        log.info("getListByExcel work:{{}}", work);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = work.getSheetAt(0);
        List<OrderConsultingEntity> list = new ArrayList<>();
        Row row = null;
        log.info("getListByExcel numberOfSheets:{{}}", work.getNumberOfSheets());
     /*   for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }*/

        for (int j = sheet.getFirstRowNum() + 1; j <= sheet.getLastRowNum(); j++) {
            log.info("getListByExcel j:{{}}", j);
            row = sheet.getRow(j);
            if (row == null || StringUtils.isEmpty(row.getCell(0).getStringCellValue())) {
                continue;
            }
            log.info("getListByExcel FirstRowNum:{{}}", sheet.getFirstRowNum());
            log.info("getListByExcel LastRowNum:{{}}", sheet.getLastRowNum());

            OrderConsultingEntity orderConsultingEntity = new OrderConsultingEntity();
            log.info("getListByExcel FirstCellNum:{{}}", row.getFirstCellNum());
            log.info("getListByExcel LastCellNum:{{}}", row.getLastCellNum());
            log.info("getListByExcel UserName:{{}}", row.getCell(0));
            log.info("getListByExcel UserName:{{}}", row.getCell(0).getStringCellValue());
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                if (null != row.getCell(i)) {
                    row.getCell(i).setCellType(CellType.STRING);
                }
            }
            log.info("getListByExcel UserName:{{}}", row.getCell(0));
            log.info("getListByExcel UserName:{{}}", row.getCell(0).getStringCellValue());
            orderConsultingEntity.setUserName(row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue());//用户姓名
            log.info("getListByExcel UserName:{{}}", row.getCell(0));
            orderConsultingEntity.setPhone(row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue());//电话
            log.info("getListByExcel Phone:{{}}", row.getCell(1));
            orderConsultingEntity.setInstallAddress(row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue());//安装地址
            log.info("getListByExcel InstallAddress:{{}}", row.getCell(2));
            orderConsultingEntity.setDetailAddress(row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue());//详细地址
            log.info("getListByExcel DetailAddress:{{}}", row.getCell(3));
            orderConsultingEntity.setBuyPlatform(row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue());//购买平台
            log.info("getListByExcel DetailAddress:{{}}", row.getCell(4));
            orderConsultingEntity.setProductModel(row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue());//产品型号
            log.info("getListByExcel ProductModel:{{}}", row.getCell(5));
            orderConsultingEntity.setIsElevator(row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue());//是否有电梯 0-没有 1-有
            log.info("getListByExcel IsElevator:{{}}", row.getCell(6));
            orderConsultingEntity.setInstallFloor(row.getCell(7) == null ? "" : row.getCell(7).getStringCellValue());//安装楼层
            log.info("getListByExcel InstallFloor:{{}}", row.getCell(7));
            log.info("getListByExcel InstallStartTime:{{}}", row.getCell(8));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //安装开始时间
            if (row.getCell(8) == null) {
                orderConsultingEntity.setInstallStartTime(null);
            } else {
                orderConsultingEntity.setInstallStartTime(LocalDateTime.parse(row.getCell(8).getStringCellValue(),df));
               // orderConsultingEntity.setInstallStartTime(DateUtils.parseLocalDateTime(row.getCell(8).getStringCellValue()));
                log.info("getListByExcel InstallStartTime:{{}}", row.getCell(8));
            }
            log.info("getListByExcel InstallEndTime:{{}}", row.getCell(9));
            //安装结束时间
            if (row.getCell(9) == null) {
                orderConsultingEntity.setInstallEndTime(null);
            } else {
                orderConsultingEntity.setInstallStartTime(LocalDateTime.parse(row.getCell(9).getStringCellValue(),df));
                //orderConsultingEntity.setInstallEndTime(DateUtils.parseLocalDateTime(row.getCell(9).getStringCellValue()));
                log.info("getListByExcel InstallEndTime:{{}}", row.getCell(9));
            }
            log.info("getListByExcel InstallPosition:{{}}", row.getCell(10));
            orderConsultingEntity.setInstallPosition(row.getCell(10) == null ? "" : row.getCell(10).getStringCellValue());//安装位置
            log.info("getListByExcel InstallStyle:{{}}", row.getCell(11));
            orderConsultingEntity.setInstallStyle(row.getCell(11) == null ? "" : row.getCell(11).getStringCellValue());//安装方式
            log.info("getListByExcel WallType:{{}}", row.getCell(12));
            orderConsultingEntity.setWallType(row.getCell(12) == null ? "" : row.getCell(12).getStringCellValue());//墙面类型
            log.info("getListByExcel WallStructure:{{}}", row.getCell(13));
            orderConsultingEntity.setWallStructure(row.getCell(13) == null ? "" : row.getCell(13).getStringCellValue());//墙体结构
            log.info("getListByExcel WiringRequire:{{}}", row.getCell(14));
            orderConsultingEntity.setWiringRequire(row.getCell(14) == null ? "" : row.getCell(14).getStringCellValue());//布线要求
            log.info("getListByExcel Distance:{{}}", row.getCell(15));
            orderConsultingEntity.setDistance(row.getCell(15) == null ? "" : row.getCell(15).getStringCellValue());//安装区域距离插座（米）
            log.info("getListByExcel Remark:{{}}", row.getCell(16));
            orderConsultingEntity.setRemark(row.getCell(16) == null ? "" : row.getCell(16).getStringCellValue());//备注
            log.info("getListByExcel OrderNo:{{}}", row.getCell(17));
            orderConsultingEntity.setOrderNo(row.getCell(17) == null ? "" : row.getCell(17).getStringCellValue());//订单编号
            log.info("getListByExcel ProductSerialNumber:{{}}", row.getCell(18));
            orderConsultingEntity.setProductSerialNumber(row.getCell(18) == null ? "" : row.getCell(18).getStringCellValue());//产品序列号
            log.info("getListByExcel LogisticsSingleNumber:{{}}", row.getCell(19));
            orderConsultingEntity.setLogisticsSingleNumber(row.getCell(19) == null ? "" : row.getCell(19).getStringCellValue());//物流单号
            log.info("getListByExcel LogisticsCompany:{{}}", row.getCell(20));
            orderConsultingEntity.setLogisticsCompany(row.getCell(20) == null ? "" : row.getCell(20).getStringCellValue());//物流公司

            //送货时间
            if (row.getCell(21) == null) {
                orderConsultingEntity.setDeliveryTime(null);
            } else {
                orderConsultingEntity.setInstallStartTime(LocalDateTime.parse(row.getCell(21).getStringCellValue(),df));
                log.info("getListByExcel DeliveryTime:{{}}", row.getCell(21));
                //orderConsultingEntity.setDeliveryTime(DateUtils.parseLocalDateTime(row.getCell(21).getStringCellValue()));
            }
            log.info("getListByExcel InstallPlatform:{{}}", row.getCell(22));
            orderConsultingEntity.setInstallPlatform(row.getCell(22) == null ? "" : row.getCell(22).getStringCellValue());//安装平台
            log.info("getListByExcel InstallPerson:{{}}", row.getCell(23));
            orderConsultingEntity.setInstallPerson(row.getCell(23) == null ? "" : row.getCell(23).getStringCellValue());//安装人员
            //安装时间
            if (row.getCell(24) == null) {
                orderConsultingEntity.setInstallTime(null);
            } else {
                orderConsultingEntity.setInstallStartTime(LocalDateTime.parse(row.getCell(24).getStringCellValue(),df));
                log.info("getListByExcel InstallTime:{{}}", row.getCell(24));
                //orderConsultingEntity.setInstallTime(DateUtils.parseLocalDateTime(row.getCell(24).getStringCellValue()));

            }
            log.info("getListByExcel InstallNo:{{}}", row.getCell(25));
            orderConsultingEntity.setInstallNo(row.getCell(25) == null ? "" : row.getCell(25).getStringCellValue());//安装单号
            log.info("getListByExcel GoodsArrival:{{}}", row.getCell(26));
            orderConsultingEntity.setGoodsArrival(row.getCell(26) == null ? "" : row.getCell(26).getStringCellValue());//到货情况
            log.info("getListByExcel orderConsultingEntity:{{}}", orderConsultingEntity);
            //      }
            log.info("getListByExcel orderConsultingEntity:{{}}", orderConsultingEntity);
            list.add(orderConsultingEntity);
            log.info("getListByExcel list:{{}}", list);
        }

        work.close();
        log.info("getListByExcel list:{{}}", list);
        log.info("----------------getListByExcel结束----------------------");
        return list;
    }

    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        log.info("getWorkbook fileType:{{}}", fileType);
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }

    private void cleanOldDefaultAddress(String userId) {
        UserReceiveAddressEntity one = iUserReceiveAddressService
                .getOne(new QueryWrapper<UserReceiveAddressEntity>()
                        .eq("userId", userId)
                        .eq("defaultStatus", "1")
                        .eq("deleted", "0")
                );
        log.info("cleanOldDefaultAddress one:{{}}",one);
        log.info("cleanOldDefaultAddress userId:{{}}",userId);
        if(one != null ){
            one.setDefaultStatus("0");
            iUserReceiveAddressService.updateById(one);
        }


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
