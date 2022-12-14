package com.knd.front.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.*;
import com.knd.common.page.PageInfo;
import com.knd.common.qiniu.RtcRoomManager;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.impl.AttachServiceImpl;
import com.knd.front.entity.TbOrder;
import com.knd.front.entity.User;
import com.knd.front.live.dto.CoachOrderListDto;
import com.knd.front.live.entity.*;
import com.knd.front.live.mapper.*;
import com.knd.front.live.request.OrderCoachRequest;
import com.knd.front.live.service.CoachOrderService;
import com.knd.front.live.service.UserOrderRecordService;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.request.GetOrderInfoRequest;
import com.knd.front.login.request.GoodsRequest;
import com.knd.front.login.service.IVipMenuService;
import com.knd.front.login.service.feignInterface.PayFeignClient;
import com.knd.front.pay.dto.OrderDto;
import com.knd.front.pay.service.IGoodsService;
import com.knd.front.train.mapper.AttachMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class CoachOrderServiceImpl implements CoachOrderService {

    private final UserCoachMapper userCoachMapper;
    private final UserCoachCourseMapper userCoachCourseMapper;
    private final UserCoachTimeMapper userCoachTimeMapper;
    private final UserCoachCourseOrderMapper userCoachCourseOrderMapper;
    private final IGoodsService iGoodsService;
    private final PayFeignClient payFeignClient;
    private final TbOrderMapper orderMapper;
    private final UserOrderRecordMapper userOrderRecordMapper;
    private final UserMapper userMapper;
    private final AttachServiceImpl attachServiceImpl;
    private final AttachMapper attachMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    private final TbOrderMapper tbOrderMapper;
    private final IVipMenuService iVipMenuService;
    private final UserOrderRecordService userOrderRecordService;
    @Value("${qiniu.appId}")
    private String appId;
    @Autowired
    private RtcRoomManager rtcRoomManager;

    @Override
    public Result getCoachOrderList(String userId, String current) {
        List<CoachOrderListDto> dtoList = new ArrayList<>();
        QueryWrapper<UserOrderRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        wrapper.orderByDesc("orderTime");
        Page<UserOrderRecordEntity> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<UserOrderRecordEntity> userOrderRecordEntityPage = userOrderRecordMapper.selectPage(page, wrapper);
        List<UserOrderRecordEntity> records = userOrderRecordEntityPage.getRecords();
        for (UserOrderRecordEntity entity : records){
            CoachOrderListDto dto = new CoachOrderListDto();
            BeanUtils.copyProperties(entity,dto);
            dto.setTimeId(entity.getRelevancyId());
            UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(entity.getRelevancyId());
            if (StringUtils.isNotEmpty(timeEntity)){
                User user = userMapper.selectById(timeEntity.getCoachUserId());
                dto.setCoachName(user!=null ? user.getNickName() : "");
                dto.setCoachHeadUrl(attachServiceImpl.getHeadPicUrl(timeEntity.getCoachUserId()));
                dto.setBeginTime(timeEntity.getBeginTime());
                dto.setEndTime(timeEntity.getEndTime());
            }
            dtoList.add(dto);
        }

        Page<CoachOrderListDto> dto = new Page<>();
        dto.setTotal(page.getTotal());
        dto.setCurrent(page.getCurrent());
        dto.setSize(page.getSize());
        dto.setRecords(dtoList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result orderTime(HttpServletResponse response, OrderCoachRequest request,String platform) {
        log.info("----------------------------------------?????????????????? ??????-------------------------------------------");
        log.info("orderTime request:{{}}",request);
        log.info("orderTime platform:{{}}",platform);
        // TODO: 2021/7/29 ??????????????????????????????
        String coachTimeId = request.getCoachTimeId();
        String userId = request.getUserId();
        UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(coachTimeId);
        if (StringUtils.isEmpty(timeEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"???????????????????????????");
        }
        /*if (timeEntity.getBeginTime().isAfter(LocalDateTime.now())){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"???????????????,????????????");
        }*/
        if(timeEntity.getBeginTime().isAfter(LocalDateTime.now())){
            timeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_NOT_START.getCode());
        }
        /*if(timeEntity.getBeginTime().isBefore(LocalDateTime.now())||timeEntity.getEndTime().isAfter(LocalDateTime.now())){
            timeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_ING.getCode());
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"????????????????????????????????????");
        }
        if(timeEntity.getEndTime().isBefore(LocalDateTime.now())){
            timeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_FINISHED.getCode());
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"??????????????????????????????????????????");
        }*/

        List<String> isOrderList = new ArrayList<>();
        isOrderList.add("0");
        isOrderList.add("1");
        //??????????????????????????????????????????
        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
        log.info("orderTime userCoachCourseEntity:{{}}",userCoachCourseEntity);
        List<String> courseTypeList = new ArrayList<>();
        courseTypeList.add(CoachCourseTypeEnum.COURSE_CONSULT.ordinal()+"");
        courseTypeList.add(CoachCourseTypeEnum.COURSE.ordinal()+"");
        //???????????????????????????????????????????????????
        if (courseTypeList.contains(userCoachCourseEntity.getCourseType())){
            UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("orderId",request.getOrderId()).eq("coachTimeId", coachTimeId).in("isOrder", isOrderList));
            if (StringUtils.isNotEmpty(orderEntity) && !userId.equals(orderEntity.getOrderUserId())){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"?????????????????????");
            }
        }
        //???????????????????????????????????????
        int orderNumByUser = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted", "0").eq("coachTimeId", coachTimeId).eq("orderUserId", userId).eq("isOrder","1"));
        if (orderNumByUser>0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"?????????????????????");
        }
        //????????????????????????????????????????????????
        UserCoachCourseOrderEntity userCoachCourseOrderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted", "0").eq("orderId",request.getOrderId()).eq("coachTimeId", coachTimeId).eq("orderUserId", userId).eq("isOrder", "0"));
        log.info("orderTime userCoachCourseOrderEntity:{{}}",userCoachCourseOrderEntity);
        if (StringUtils.isNotEmpty(userCoachCourseOrderEntity)){
            String orderId = userCoachCourseOrderEntity.getOrderId();
            log.info("orderTime orderId:{{}}",orderId);
            TbOrder tbOrder = tbOrderMapper.selectById(orderId);
            log.info("orderTime tbOrder:{{}}",tbOrder);
            //ios,????????????,????????????String
            if(PlatformEnum.IOS_PLATFORM.getName().equals(platform) || PlatformEnum.ANDROID_PLATFORM.getName().equals(platform)){
                //??????????????????????????????????????????????????????????????????
                if(request.getPaymentType().equals(tbOrder.getPaymentType())) {
                    return ResultUtil.success(tbOrder.getAppPayInfo());
                }
                //????????????????????? ???????????????????????????????????????????????????
                tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
                log.info("orderTime tbOrder:{{}}",tbOrder);
                tbOrderMapper.deleteById(tbOrder);
               // tbOrderMapper.updateById(tbOrder);
                cancelOrderSuccess(userCoachCourseOrderEntity.getCoachTimeId(),userId);
            }else {
                tbOrderMapper.updateById(tbOrder);
                return iVipMenuService.tradeQuery(null, tbOrder.getOrderNo());
            }

        }

        if (timeEntity.getPrice().compareTo(new BigDecimal(0)) == 0){
            //?????????0.??????????????????
            OrderCoachRequest orderCoachRequest = new OrderCoachRequest();
            orderCoachRequest.setUserId(userId);
            orderCoachRequest.setCoachTimeId(coachTimeId);
            Result result = this.orderSuccess(orderCoachRequest);
            if (result!=null && ResultEnum.SUCCESS.getCode().equals(result.getCode())){
                return ResultUtil.success();
            }else{
                return result;
            }
        }else{
            List<GoodsRequest> goodsRequestList = new ArrayList<>();
            //????????????0?????????????????????
            GoodsRequest goodsRequest = new GoodsRequest();
            goodsRequest.setGoodsId(coachTimeId);
            goodsRequest.setPrice(timeEntity.getPrice());
            goodsRequest.setQuantity("1");
            goodsRequestList.add(goodsRequest);

            GetOrderInfoRequest orderInfoRequest = new GetOrderInfoRequest();
            orderInfoRequest.setUserId(userId);
            orderInfoRequest.setOrderType(OrderTypeEnum.LIVE.getCode());
            orderInfoRequest.setGoodsRequestList(goodsRequestList);
            orderInfoRequest.setRemarks("??????????????????");
            orderInfoRequest.setPaymentType(request.getPaymentType());
            orderInfoRequest.setPayPlatform(platform);
            log.info("orderTime orderInfoRequest:{{}}",orderInfoRequest);
            log.info("--------------------------------------payPlatform:{{}}-----------------------------------------",orderInfoRequest.getPaymentType());
            Result payInfo = iGoodsService.getPayInfo(response, orderInfoRequest);
            OrderDto orderDto = (OrderDto)payInfo.getData();

            //?????????????????????,??????????????????
            UserCoachCourseOrderEntity entity = new UserCoachCourseOrderEntity();
            entity.setId(UUIDUtil.getShortUUID());
            entity.setCoachUserId(timeEntity.getCoachUserId());
            entity.setCoachTimeId(coachTimeId);
            entity.setOrderUserId(userId);
            entity.setOrderId(orderDto.getId());
            entity.setIsOrder("0");
            entity.setCreateBy(userId);
            entity.setCreateDate(LocalDateTime.now());
            entity.setDeleted("0");
            entity.setLastModifiedBy(userId);
            entity.setLastModifiedDate(LocalDateTime.now());
            log.info("orderTime entity:{{}}",entity);
            userCoachCourseOrderMapper.insert(entity);
            UserCoachTimeEntity userCoachTimeEntity = new UserCoachTimeEntity();
            userCoachTimeEntity.setId(coachTimeId);
            userCoachTimeEntity.setLiveStatus("0");
            userCoachTimeEntity.setCoachUserId(userId);
            userCoachTimeEntity.setCoachCourseId(entity.getId());
            userCoachTimeEntity.setActualBeginTime(timeEntity.getActualBeginTime());
            userCoachTimeEntity.setActualEndTime(timeEntity.getActualEndTime());
            userCoachTimeEntity.setDate(timeEntity.getDate());
            userCoachTimeEntity.setBeginTime(timeEntity.getBeginTime());
            userCoachTimeEntity.setEndTime(timeEntity.getEndTime());
            userCoachTimeEntity.setPrice(timeEntity.getPrice());
            userCoachTimeEntity.setCreateBy(userId);
            userCoachTimeEntity.setCreateDate(LocalDateTime.now());
            userCoachTimeMapper.insert(userCoachTimeEntity);
            log.info("------------------?????????????????? ??????------------------");
            //ios,????????????,????????????String
            if(PlatformEnum.IOS_PLATFORM.getName().equals(platform) || PlatformEnum.ANDROID_PLATFORM.getName().equals(platform)){
                return ResultUtil.success(orderDto.getAppPayInfo());
            }else {
                return payInfo;
            }
        }
    }

    @Override
    public Result orderSuccess(OrderCoachRequest request) {
        String coachTimeId = request.getCoachTimeId();
        String userId = request.getUserId();
        String orderId = request.getOrderId();
        if (StringUtils.isNotEmpty(orderId)){
            //????????????????????????:?????????0
            UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted","0").eq("orderId",orderId).eq("isOrder", "0").eq("coachTimeId",coachTimeId).eq("orderUserId",userId));
            if (StringUtils.isEmpty(orderEntity)){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"????????????");
            }
            //????????????id
            String coachUserId = orderEntity.getCoachUserId();
            //????????????????????????????????????????????????????????????
            int num = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("isOrder","1").eq("coachUserId", coachUserId).eq("orderUserId",userId));
            if (num==0){
                UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", coachUserId));
                if (StringUtils.isNotEmpty(userCoachEntity)) {
                    //????????????????????????
                    long traineeNum = userCoachEntity.getTraineeNum();
                    //??????????????????
                    userCoachEntity.setTraineeNum(traineeNum + 1);
                    userCoachMapper.updateById(userCoachEntity);
                }
            }
            //????????????????????????
            orderEntity.setIsOrder("1");
            orderEntity.setOrderId(orderId);
            orderEntity.setLastModifiedBy(userId);
            orderEntity.setLastModifiedDate(LocalDateTime.now());
            userCoachCourseOrderMapper.updateById(orderEntity);
        }else {
            //?????????id?????????????????????0?????????
            UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(coachTimeId);
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
            //????????????????????????:?????????1
            int num = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("orderId",orderId).eq("isOrder","1").eq("coachUserId", timeEntity.getCoachUserId()).eq("orderUserId",userId));
            if (num==0){
                UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", timeEntity.getCoachUserId()));
                if (StringUtils.isNotEmpty(userCoachEntity)) {
                    //????????????????????????
                    long traineeNum = userCoachEntity.getTraineeNum();
                    //??????????????????
                    userCoachEntity.setTraineeNum(traineeNum + 1);
                    userCoachMapper.updateById(userCoachEntity);
                }
            }
            //?????????????????????,???????????????
            UserCoachCourseOrderEntity entity = new UserCoachCourseOrderEntity();
            entity.setId(UUIDUtil.getShortUUID());
            entity.setCoachUserId(timeEntity.getCoachUserId());
            entity.setCoachTimeId(coachTimeId);
            entity.setOrderUserId(userId);
            entity.setOrderId(orderId);
            entity.setIsOrder("1");
            entity.setCreateBy(userId);
            entity.setCreateDate(LocalDateTime.now());
            entity.setDeleted("0");
            entity.setLastModifiedBy(userId);
            entity.setLastModifiedDate(LocalDateTime.now());
            userCoachCourseOrderMapper.insert(entity);
        }
        //???????????????????????????
        UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(coachTimeId);
        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
        User user = userMapper.selectById(timeEntity.getCoachUserId());

        String courseType = userCoachCourseEntity.getCourseType();
        userOrderRecordService.save(userId,courseType,
                CoachCourseTypeEnum.values()[Integer.valueOf(courseType)].getDisplay()+"????????????",
                "????????????"+user.getNickName()+"?????????"+CoachCourseTypeEnum.values()[Integer.valueOf(courseType)].getDisplay()+
                        "??????????????????"+ DateUtils.formatLocalDateTime(timeEntity.getBeginTime(),"yyyy-MM-dd HH:mm:ss")
                        /*+ "-"+DateUtils.formatLocalDateTime(timeEntity.getEndTime(),"yyyy-MM-dd HH:mm:ss")*/,
                timeEntity.getBeginTime(),
                coachTimeId);
        return ResultUtil.success();
    }

    @Override
    public Result cancelOrderTime(String coachTimeId, String userId) {
        UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(coachTimeId);
        if (StringUtils.isEmpty(userCoachTimeEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"?????????????????????");
        }
        UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted","0").ne("isOrder","2").eq("coachTimeId",coachTimeId).eq("orderUserId",userId));
        if (StringUtils.isEmpty(orderEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"?????????????????????");
        }
        //????????????????????????????????????24??????
        LocalDateTime nowTime = LocalDateTime.now();
        Duration duration = Duration.between(userCoachTimeEntity.getBeginTime(), nowTime);
        if (duration.toMillis()<(24*60*60*1000)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"??????????????????????????????24??????,??????????????????");
        }

        //?????????????????????
        BigDecimal amount = userCoachTimeEntity.getPrice();
        if (amount.compareTo(new BigDecimal(0)) == 0){
            //?????????0.????????????????????????
            this.cancelOrderSuccess(coachTimeId,userId);
        }else{
            //????????????????????????
            //??????????????????????????????????????????????????????????????????????????????
            TbOrder tbOrder = orderMapper.selectById(orderEntity.getOrderId());
            payFeignClient.tradeRefund(tbOrder.getOutOrderNo(), tbOrder.getOrderNo());
        }
        return ResultUtil.success();
    }

    @Override
    public Result cancelOrderSuccess(String coachTimeId, String userId) {
        if (StringUtils.isEmpty(userId)){
            //userId???token??????
            userId = UserUtils.getUserId();
        }
        //????????????????????????
        UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted","0").ne("isOrder","2").eq("coachTimeId",coachTimeId).eq("orderUserId",userId));
        if (StringUtils.isNotEmpty(orderEntity)){
            //??????id
            String coachUserId = orderEntity.getCoachUserId();
            //????????????????????????????????????????????????
            int num = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("isOrder","1").eq("coachUserId", coachUserId).eq("orderUserId",userId));
            if (num==1){
                //?????????????????????????????????????????????????????????
                //??????????????????????????????
                UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", coachUserId));
                //????????????????????????
                long traineeNum = userCoachEntity.getTraineeNum();
                //??????????????????
                userCoachEntity.setTraineeNum(traineeNum-1);
                userCoachMapper.updateById(userCoachEntity);
            }
            if ("1".equals(orderEntity.getIsOrder())){
                //??????????????????,????????????
                Result removeResult = userOrderRecordService.remove(userId, coachTimeId);
                if (!ResultEnum.SUCCESS.getCode().equals(removeResult.getCode())){
                    return removeResult;
                }
            }
            //???????????????????????????,???????????????
           // orderEntity.setIsOrder("2");
            userCoachCourseOrderMapper.deleteById(orderEntity.getId());

        }
        return ResultUtil.success();
    }




}
