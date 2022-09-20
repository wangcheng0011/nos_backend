package com.knd.front.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.knd.front.entity.Attach;
import com.knd.front.entity.TbOrder;
import com.knd.front.entity.User;
import com.knd.front.entity.UserDetail;
import com.knd.front.live.dto.CoachOrderListDto;
import com.knd.front.live.entity.*;
import com.knd.front.live.mapper.*;
import com.knd.front.live.request.OrderCoachRequest;
import com.knd.front.live.service.CoachOrderService;
import com.knd.front.live.service.UserOrderRecordService;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.request.GetOrderInfoRequest;
import com.knd.front.login.request.GoodsRequest;
import com.knd.front.login.service.IVipMenuService;
import com.knd.front.login.service.feignInterface.PayFeignClient;
import com.knd.front.pay.dto.OrderDto;
import com.knd.front.pay.service.IGoodsService;
import com.knd.front.train.mapper.AttachMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
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
    private final UserDetailMapper userDetailMapper;
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
                dto.setCoachHeadUrl(getHeadPicUrl(timeEntity.getCoachUserId()));
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
        log.info("----------------------------------------预约教练课程 开始-------------------------------------------");
        log.info("orderTime request:{{}}",request);
        log.info("orderTime platform:{{}}",platform);
        // TODO: 2021/7/29 订单改造需要支持多个
        String coachTimeId = request.getCoachTimeId();
        String userId = request.getUserId();
        UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(coachTimeId);
        if (StringUtils.isEmpty(timeEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该时间段课程不存在");
        }
        /*if (timeEntity.getBeginTime().isAfter(LocalDateTime.now())){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"课程已开始,预约失败");
        }*/
        if(timeEntity.getBeginTime().isAfter(LocalDateTime.now())){
            timeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_NOT_START.getCode());
        }
        /*if(timeEntity.getBeginTime().isBefore(LocalDateTime.now())||timeEntity.getEndTime().isAfter(LocalDateTime.now())){
            timeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_ING.getCode());
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"课程正在直播中，预约失败");
        }
        if(timeEntity.getEndTime().isBefore(LocalDateTime.now())){
            timeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_FINISHED.getCode());
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该课程已经直播结束，预约失败");
        }*/

        List<String> isOrderList = new ArrayList<>();
        isOrderList.add("0");
        isOrderList.add("1");
        //判断该课程是直播课还是私教课
        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
        log.info("orderTime userCoachCourseEntity:{{}}",userCoachCourseEntity);
        List<String> courseTypeList = new ArrayList<>();
        courseTypeList.add(CoachCourseTypeEnum.COURSE_CONSULT.ordinal()+"");
        courseTypeList.add(CoachCourseTypeEnum.COURSE.ordinal()+"");
        //判断私教课或者课前咨询只能预约一次
        if (courseTypeList.contains(userCoachCourseEntity.getCourseType())){
            UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("orderId",request.getOrderId()).eq("coachTimeId", coachTimeId).in("isOrder", isOrderList));
            if (StringUtils.isNotEmpty(orderEntity) && !userId.equals(orderEntity.getOrderUserId())){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"该课程已被预约");
            }
        }
        //判断该用户是否已预约该课程
        int orderNumByUser = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted", "0").eq("coachTimeId", coachTimeId).eq("orderUserId", userId).eq("isOrder","1"));
        if (orderNumByUser>0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"您已预约该课程");
        }
        //预约中状态的需要订单信息直接返回
        UserCoachCourseOrderEntity userCoachCourseOrderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted", "0").eq("orderId",request.getOrderId()).eq("coachTimeId", coachTimeId).eq("orderUserId", userId).eq("isOrder", "0"));
        log.info("orderTime userCoachCourseOrderEntity:{{}}",userCoachCourseOrderEntity);
        if (StringUtils.isNotEmpty(userCoachCourseOrderEntity)){
            String orderId = userCoachCourseOrderEntity.getOrderId();
            log.info("orderTime orderId:{{}}",orderId);
            TbOrder tbOrder = tbOrderMapper.selectById(orderId);
            log.info("orderTime tbOrder:{{}}",tbOrder);
            //ios,安卓渠道,返回是个String
            if(PlatformEnum.IOS_PLATFORM.getName().equals(platform) || PlatformEnum.ANDROID_PLATFORM.getName().equals(platform)){
                //如果是原有支付方式则直接返回原订单预支付链接
                if(request.getPaymentType().equals(tbOrder.getPaymentType())) {
                    return ResultUtil.success(tbOrder.getAppPayInfo());
                }
                //更换了支付方式 则更新已有预约中订单取消，全新下单
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
            //价格为0.直接预约成功
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
            //价格不为0，调用支付接口
            GoodsRequest goodsRequest = new GoodsRequest();
            goodsRequest.setGoodsId(coachTimeId);
            goodsRequest.setPrice(timeEntity.getPrice());
            goodsRequest.setQuantity("1");
            goodsRequestList.add(goodsRequest);

            GetOrderInfoRequest orderInfoRequest = new GetOrderInfoRequest();
            orderInfoRequest.setUserId(userId);
            orderInfoRequest.setOrderType(OrderTypeEnum.LIVE.getCode());
            orderInfoRequest.setGoodsRequestList(goodsRequestList);
            orderInfoRequest.setRemarks("购买教练课程");
            orderInfoRequest.setPaymentType(request.getPaymentType());
            orderInfoRequest.setPayPlatform(platform);
            log.info("orderTime orderInfoRequest:{{}}",orderInfoRequest);
            log.info("--------------------------------------payPlatform:{{}}-----------------------------------------",orderInfoRequest.getPaymentType());
            Result payInfo = iGoodsService.getPayInfo(response, orderInfoRequest);
            OrderDto orderDto = (OrderDto)payInfo.getData();

            //预约表插入数据,为预约中状态
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
            log.info("------------------预约教练课程 结束------------------");
            //ios,安卓渠道,返回是个String
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
            //查询该笔预约订单:状态为0
            UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted","0").eq("orderId",orderId).eq("isOrder", "0").eq("coachTimeId",coachTimeId).eq("orderUserId",userId));
            if (StringUtils.isEmpty(orderEntity)){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"数据异常");
            }
            //教练用户id
            String coachUserId = orderEntity.getCoachUserId();
            //查询该用户在此之前已预约该教练课程的次数
            int num = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("isOrder","1").eq("coachUserId", coachUserId).eq("orderUserId",userId));
            if (num==0){
                UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", coachUserId));
                if (StringUtils.isNotEmpty(userCoachEntity)) {
                    //教练当前学员人数
                    long traineeNum = userCoachEntity.getTraineeNum();
                    //学员人数新增
                    userCoachEntity.setTraineeNum(traineeNum + 1);
                    userCoachMapper.updateById(userCoachEntity);
                }
            }
            //课程预约状态修改
            orderEntity.setIsOrder("1");
            orderEntity.setOrderId(orderId);
            orderEntity.setLastModifiedBy(userId);
            orderEntity.setLastModifiedDate(LocalDateTime.now());
            userCoachCourseOrderMapper.updateById(orderEntity);
        }else {
            //无订单id，代表是价格为0得预约
            UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(coachTimeId);
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
            //查询该笔预约订单:状态为1
            int num = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("orderId",orderId).eq("isOrder","1").eq("coachUserId", timeEntity.getCoachUserId()).eq("orderUserId",userId));
            if (num==0){
                UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", timeEntity.getCoachUserId()));
                if (StringUtils.isNotEmpty(userCoachEntity)) {
                    //教练当前学员人数
                    long traineeNum = userCoachEntity.getTraineeNum();
                    //学员人数新增
                    userCoachEntity.setTraineeNum(traineeNum + 1);
                    userCoachMapper.updateById(userCoachEntity);
                }
            }
            //预约表插入数据,为预约成功
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
        //预约记录表插入数据
        UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(coachTimeId);
        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
        User user = userMapper.selectById(timeEntity.getCoachUserId());

        String courseType = userCoachCourseEntity.getCourseType();
        userOrderRecordService.save(userId,courseType,
                CoachCourseTypeEnum.values()[Integer.valueOf(courseType)].getDisplay()+"预约通知",
                "您已预约"+user.getNickName()+"教练的"+CoachCourseTypeEnum.values()[Integer.valueOf(courseType)].getDisplay()+
                        "课程时间为："+ DateUtils.formatLocalDateTime(timeEntity.getBeginTime(),"yyyy-MM-dd HH:mm:ss")
                        /*+ "-"+DateUtils.formatLocalDateTime(timeEntity.getEndTime(),"yyyy-MM-dd HH:mm:ss")*/,
                timeEntity.getBeginTime(),
                coachTimeId);
        return ResultUtil.success();
    }

    @Override
    public Result cancelOrderTime(String coachTimeId, String userId) {
        UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(coachTimeId);
        if (StringUtils.isEmpty(userCoachTimeEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该时间段不存在");
        }
        UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted","0").ne("isOrder","2").eq("coachTimeId",coachTimeId).eq("orderUserId",userId));
        if (StringUtils.isEmpty(orderEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"预约订单不存在");
        }
        //判断时间是否在课程开始前24小时
        LocalDateTime nowTime = LocalDateTime.now();
        Duration duration = Duration.between(userCoachTimeEntity.getBeginTime(), nowTime);
        if (duration.toMillis()<(24*60*60*1000)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该课程距离开课已不足24小时,不能取消预约");
        }

        //当前课程的价格
        BigDecimal amount = userCoachTimeEntity.getPrice();
        if (amount.compareTo(new BigDecimal(0)) == 0){
            //价格为0.直接取消预约成功
            this.cancelOrderSuccess(coachTimeId,userId);
        }else{
            //需要进入退款流程
            //当前是一个预约单取消，整个订单中所有的预约单都会取消
            TbOrder tbOrder = orderMapper.selectById(orderEntity.getOrderId());
            payFeignClient.tradeRefund(tbOrder.getOutOrderNo(), tbOrder.getOrderNo());
        }
        return ResultUtil.success();
    }

    @Override
    public Result cancelOrderSuccess(String coachTimeId, String userId) {
        if (StringUtils.isEmpty(userId)){
            //userId从token获取
            userId = UserUtils.getUserId();
        }
        //查询该笔预约订单
        UserCoachCourseOrderEntity orderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("deleted","0").ne("isOrder","2").eq("coachTimeId",coachTimeId).eq("orderUserId",userId));
        if (StringUtils.isNotEmpty(orderEntity)){
            //教练id
            String coachUserId = orderEntity.getCoachUserId();
            //查询该用户已预约该教练课程的次数
            int num = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("deleted", "0").eq("isOrder","1").eq("coachUserId", coachUserId).eq("orderUserId",userId));
            if (num==1){
                //仅此一次，所以需要修改该教练的学员人数
                //修改该教练的学员人数
                UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", coachUserId));
                //教练当前学员人数
                long traineeNum = userCoachEntity.getTraineeNum();
                //学员人数新增
                userCoachEntity.setTraineeNum(traineeNum-1);
                userCoachMapper.updateById(userCoachEntity);
            }
            if ("1".equals(orderEntity.getIsOrder())){
                //删除预约直播,私教记录
                Result removeResult = userOrderRecordService.remove(userId, coachTimeId);
                if (!ResultEnum.SUCCESS.getCode().equals(removeResult.getCode())){
                    return removeResult;
                }
            }
            //订单表数据更新状态,释放该课程
           // orderEntity.setIsOrder("2");
            userCoachCourseOrderMapper.deleteById(orderEntity.getId());

        }
        return ResultUtil.success();
    }

    @Override
    public Result closeUserCoachCourseOrder(String id) {

        try {
            QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
            userCoachTimeEntityQueryWrapper.eq("coachCourseId",id);
            userCoachTimeEntityQueryWrapper.eq("deleted",0);
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
            if(userCoachTimeEntity== null || "2".equals(userCoachTimeEntity.getLiveStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"私教不存在或已关闭");
            }
            userCoachTimeEntity.setLiveStatus("2");
            userCoachTimeMapper.updateById(userCoachTimeEntity);
            log.info("closeUserCoachCourseOrder userCoachTimeEntity:{{}}",userCoachTimeEntity);
            //踢出该房间所有用户
            Response response = rtcRoomManager.listUser(appId,id);
//            JSONObject jsonObject = JSON.parseObject(response.getInfo());
//            JSONArray users = jsonObject.getJSONArray("users");
            log.info("closeUserCoachCourseOrder response:{{}}",response);
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            log.info("closeUserCoachCourseOrder users:{{}}",users);
            users.stream().forEach( e-> {
                JSONObject jo = (JSONObject)e;
                String userId = jo.getString("userId");
                try {
                    rtcRoomManager.kickUser(appId,id,userId);
                } catch (QiniuException qiniuException) {
                    qiniuException.printStackTrace();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"系统异常");
        }
        return ResultUtil.success();
    }

    private String getHeadPicUrl(String userId){
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if(userDetail!=null){
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            return attach!=null ? fileImagesPath+attach.getFilePath() : "";
        }else{
            return "";
        }
    }
}
