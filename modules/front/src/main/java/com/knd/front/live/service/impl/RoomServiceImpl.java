package com.knd.front.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.qiniu.RtcRoomManager;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.dto.VoUrl;
import com.knd.front.entity.Attach;
import com.knd.front.live.dto.RoomListDto;
import com.knd.front.live.entity.*;
import com.knd.front.live.mapper.*;
import com.knd.front.live.request.CreateOrUpdateRoomRequest;
import com.knd.front.live.request.QueryLiveRoomRequest;
import com.knd.front.live.request.ReportRoomRequest;
import com.knd.front.live.service.IRoomService;
import com.knd.front.live.service.ITrainGroupService;
import com.knd.front.live.service.UserOrderRecordService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Service
@Slf4j
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomEntity> implements IRoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RtcRoomManager rtcRoomManager;
    @Autowired
    private TrainGroupUserMapper trainGroupUserMapper;
    @Autowired
    private RoomReserveMapper roomReserveMapper;
    @Autowired
    private ITrainGroupService iTrainGroupService;
    @Autowired
    private RoomReportMapper roomReportMapper;
    @Autowired
    private RoomReportAttachMapper roomReportAttachMapper;
    @Autowired
    private UserCoachTimeMapper userCoachTimeMapper;
    @Autowired
    private UserCoachCourseMapper userCoachCourseMapper;
    @Autowired
    private UserCoachCourseOrderMapper userCoachCourseOrderMapper;

    @Value("${qiniu.appId}")
    private String appId;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Autowired
    private UserOrderRecordService userOrderRecordService;
    @Autowired
    private AttachService attachService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createRoom(CreateOrUpdateRoomRequest createOrUpdateRoomRequest) {
        QueryWrapper<RoomEntity> roomEntityQueryWrapper = new QueryWrapper<>();
        roomEntityQueryWrapper.eq("roomName",createOrUpdateRoomRequest.getRoomName());
        roomEntityQueryWrapper.eq("trainGroupId",createOrUpdateRoomRequest.getTrainGroupId());
        roomEntityQueryWrapper.eq("roomStatus","0");
        roomEntityQueryWrapper.eq("deleted","0");
        Integer integer = baseMapper.selectCount(roomEntityQueryWrapper);
        if(integer>0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "活动名称已存在");
        }
        if (checkGroupIsExist(createOrUpdateRoomRequest.getTrainGroupId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
        }
        TrainGroupUserEntity groupUser = getGroupUser(createOrUpdateRoomRequest.getTrainGroupId());
        if(groupUser == null){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"请先加入小组并成为管理员");
        }
        if("1".equals(createOrUpdateRoomRequest.getPublicFlag()) && StringUtils.isEmpty(createOrUpdateRoomRequest.getInvitationCode())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"不公开房间必须设置验证码");
        }

        RoomEntity roomEntity = new RoomEntity();
        BeanUtils.copyProperties(createOrUpdateRoomRequest,roomEntity);
        roomEntity.setUserId(UserUtils.getUserId());
        baseMapper.insert(roomEntity);
        return ResultUtil.success();
    }


    @Override
    public Result<Page<RoomListDto>> roomList(QueryLiveRoomRequest queryLiveRoomRequest) {
        Page<RoomListDto> page = new Page<>(Long.parseLong(queryLiveRoomRequest.getCurrentPage()), PageInfo.pageSize);
        QueryWrapper<RoomEntity> qw = new QueryWrapper<>();
        qw.eq("ltg.deleted","0");

        if(queryLiveRoomRequest.getTrainGroupId()!=null){
            qw.eq("ltg.trainGroupId",queryLiveRoomRequest.getTrainGroupId());
        }
        if(queryLiveRoomRequest.getRoomStatus()!=null){
            qw.eq("ltg.roomStatus",queryLiveRoomRequest.getRoomStatus());
        }
        qw.orderByDesc("ltg.createDate");
        page = roomMapper.roomList(page,qw);
        LocalDateTime now = LocalDateTime.now();
        List<RoomListDto> collect = page.getRecords().stream().map(e -> {
            //Attach attach = attachMapper.selectById(e.);
            e.setHeadPicUrl(fileImagesPath +e.getHeadPicUrl());
            if("0".equals(e.getRoomStatus())&&e.getBeginDate().isAfter(now)) {
                Integer count = roomReserveMapper.selectCount(new QueryWrapper<RoomReserveEntity>()
                        .eq("userId", UserUtils.getUserId())
                        .eq("roomId", e.getId()));
                if(count>0) {
                    e.setRoomStatus("3");
                }else{
                    e.setRoomStatus("2");
                }

            }

            return e;
        }).collect(Collectors.toList());

        page.setRecords(collect);

        return ResultUtil.success(page);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result editRoom(CreateOrUpdateRoomRequest createOrUpdateRoomRequest) {

        if (checkGroupIsExist(createOrUpdateRoomRequest.getTrainGroupId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
        }
        TrainGroupUserEntity groupUser = getGroupUser(createOrUpdateRoomRequest.getTrainGroupId());
        if(groupUser == null){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"请先加入小组并成为管理员");
        }
        RoomEntity roomEntity = baseMapper.selectById(createOrUpdateRoomRequest.getId());
        if(roomEntity== null
                || "1".equals(roomEntity.getDeleted())
                ||"1".equals(roomEntity.getRoomStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
        }

        if("1".equals(createOrUpdateRoomRequest.getPublicFlag()) && StringUtils.isEmpty(createOrUpdateRoomRequest.getInvitationCode())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"不公开房间必须设置验证码");
        }
        RoomEntity roomEntity1 = new RoomEntity();
        BeanUtils.copyProperties(createOrUpdateRoomRequest,roomEntity1);
        baseMapper.deleteById(roomEntity1.getId());
        QueryWrapper<RoomEntity> roomEntityQueryWrapper = new QueryWrapper<>();
        roomEntityQueryWrapper.eq("roomName",createOrUpdateRoomRequest.getRoomName());
        roomEntityQueryWrapper.eq("trainGroupId",createOrUpdateRoomRequest.getTrainGroupId());
        roomEntityQueryWrapper.eq("roomStatus","0");
        roomEntityQueryWrapper.eq("deleted","0");
        Integer integer = baseMapper.selectCount(roomEntityQueryWrapper);
        if(integer>1){
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "活动名称已存在");
        }
        baseMapper.insert(roomEntity1);
        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changeRoomStatus(String id, String status) {
        try {

            RoomEntity roomEntity = baseMapper.selectById(id);
            if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
            }
            TrainGroupUserEntity groupUser = getGroupUser(roomEntity.getTrainGroupId());
            if(groupUser == null){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"请先加入小组并成为管理员");
            }
            if(!roomEntity.getUserId().equals(UserUtils.getUserId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"只有房主才能修改房间状态");
            }

            roomEntity.setRoomStatus(status);
            baseMapper.updateById(roomEntity);
            if("0".equals(status)) {

                Response response = rtcRoomManager.listUser(appId, roomEntity.getRoomName());
                JSONObject jsonObject = JSON.parseObject(response.getInfo());
                JSONArray users = jsonObject.getJSONArray("users");
                users.stream().forEach( e-> {
                    JSONObject jo = (JSONObject)e;
                    String userId = jo.getString("userId");
                    try {
                        rtcRoomManager.kickUser(appId,roomEntity.getRoomName(),userId);
                    } catch (QiniuException qiniuException) {
                        qiniuException.printStackTrace();
                    }
                });

            }else{
                Integer count = baseMapper.selectCount(new QueryWrapper<RoomEntity>()
                        .eq("userId", UserUtils.getUserId())
                        .eq("roomStatus", "1"));
                if(count>0) {
                    return ResultUtil.error(ResultEnum.FAIL.getCode(),"存在正在直播的房间，请先关闭");
                }
                roomEntity.setRoomStatus(status);
                baseMapper.updateById(roomEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"系统异常");
        }
        return ResultUtil.success();
    }

    private boolean checkGroupIsExist(String trainGroupId) {
        TrainGroupEntity group = iTrainGroupService.getGroup(trainGroupId);
        if (group == null || "1".equals(group.getDeleted())) {
            return true;
        }
        return false;
    }

    @Override
    public Result getRoomToken(String roomId,String invitationCode) throws Exception {
        log.info("------------------------------getRoomToken开始--------------------------------");
        log.info("getRoomToken roomId:{{}}",roomId);
        log.info("getRoomToken invitationCode:{{}}",invitationCode);
      //  try {
            boolean owner = false;
            RoomEntity roomEntity = baseMapper.selectById(roomId);
            log.info("getRoomToken roomEntity:{{}}",roomEntity);
            if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
            }
            TrainGroupUserEntity groupUser = getGroupUser(roomEntity.getTrainGroupId());
            if(groupUser == null){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"请先加入小组");
            }

            if(roomEntity== null
                    || "1".equals(roomEntity.getDeleted())
                    ||"1".equals(roomEntity.getRoomStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
            }
            //TODO
            if(roomEntity.getUserId().equals(UserUtils.getUserId())){
                owner = true;
            }

            Response response = rtcRoomManager.listUser(appId, roomId);
            log.info("getRoomToken response:{{}}",response);
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            if(users.size()>roomEntity.getMemberSize()) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间人数已满");
            }


            if(LocalDateTime.now().isBefore(roomEntity.getBeginDate())) {
                //未开始
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"直播未开始");
            }

            if(!owner&&"1".equals(roomEntity.getPublicFlag())
                    &&!roomEntity.getInvitationCode().equals(invitationCode)){
                //不公开
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间密码不正确");
            }

            String role = null;
            log.info("getRoomToken roomEntity.getUserId:{{}}",roomEntity.getUserId());
            log.info("getRoomToken UserId:{{}}",UserUtils.getUserId());
            if(roomEntity.getUserId().equals(UserUtils.getUserId())) {
                role = "admin";
            }else{
                role = "user";
            }
            log.info("getRoomToken role:{{}}",role);
            RoomEntity roomEntity1 = roomMapper.selectById(roomId);
            log.info("getRoomToken UserId:{{}}",UserUtils.getUserId());
          //  String roomToken = rtcRoomManager.getRoomToken(appId, roomEntity1.getTrainGroupId(), UserUtils.getUserId(), DateUtils.addDate(new Date(), 30).getTime(), role);
            String roomToken = rtcRoomManager.getRoomToken(appId, roomId, UserUtils.getUserId(), DateUtils.addDate(new Date(), 30).getTime(), role);
            log.info("getRoomToken roomToken:{{}}",roomToken);
            log.info("获取签发roomToken："+roomToken);
            Map<String,String> map = new HashMap<>();
            map.put("roomToken",roomToken);
            if(owner &&"1".equals(roomEntity.getPublicFlag())){
                map.put("invitationCode",roomEntity.getInvitationCode());
            }
            log.info("------------------------------getRoomToken结束--------------------------------");
            return ResultUtil.success(map);
       // } catch (Exception e) {
      //      e.printStackTrace();
      //      return ResultUtil.error(ResultEnum.FAIL.getCode(),"服务器异常,获取roomToken失败");
      //  }
    }

    @Override
    public Result deleteRoom(String id) {
        RoomEntity roomEntity = baseMapper.selectById(id);
        if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
        }
        if(roomEntity== null || "1".equals(roomEntity.getDeleted())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在");
        }
        if(!roomEntity.getUserId().equals(UserUtils.getUserId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"只有房主才能修改房间状态");
        }
        if(!roomEntity.getRoomStatus().equals("1")) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该房间正在直播中，请先关闭");
        }
        roomEntity.setDeleted("1");
        baseMapper.updateById(roomEntity);
        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result closeRoom(String id)  {
        try {
            log.info("-------------------------------关闭房间 开始-----------------------------------");
            log.info("closeRoom id:{{}}",id);
            RoomEntity roomEntity = baseMapper.selectById(id);
            if (!roomEntity.getUserId().equals(UserUtils.getUserId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "只有房主才能修改房间状态");
            }

            if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
            }
            if(roomEntity== null || "1".equals(roomEntity.getRoomStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
            }
         /*   if(!roomEntity.getUserId().equals(UserUtils.getUserId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"只有房主才能修改房间状态");
            }*/

           // roomEntity.setRoomStatus("1");
             baseMapper.update(
                null,
                Wrappers.<RoomEntity>lambdaUpdate()
                        .set(RoomEntity::getRoomStatus, "1")
                        .eq(RoomEntity::getId, roomEntity.getId())
            );

            //踢出该房间所有用户
            Response response = rtcRoomManager.listUser(appId, roomEntity.getId());
//            JSONObject jsonObject = JSON.parseObject(response.getInfo());
//            JSONArray users = jsonObject.getJSONArray("users");
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            users.stream().forEach( e-> {
                JSONObject jo = (JSONObject)e;
                String userId = jo.getString("userId");
                log.info("closeRoom userId:{{}}",userId);
                try {
                    rtcRoomManager.kickUser(appId,roomEntity.getId(),userId);
                } catch (QiniuException qiniuException) {
                    qiniuException.printStackTrace();
                }
            });
            log.info("-------------------------------关闭房间 结束-----------------------------------");
       } catch (Exception e) {
         e.printStackTrace();
          return ResultUtil.error(ResultEnum.FAIL.getCode(),"系统异常");
       }
        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result scheduledCloseRoom(String id)  {
        try {
            log.info("-------------------------------关闭房间 开始-----------------------------------");
            log.info("closeRoom id:{{}}",id);
            RoomEntity roomEntity = baseMapper.selectById(id);
            if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
            }
            if(roomEntity== null || "1".equals(roomEntity.getRoomStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
            }
         /*   if(!roomEntity.getUserId().equals(UserUtils.getUserId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"只有房主才能修改房间状态");
            }*/

            // roomEntity.setRoomStatus("1");
            baseMapper.update(
                    null,
                    Wrappers.<RoomEntity>lambdaUpdate()
                            .set(RoomEntity::getRoomStatus, "1")
                            .eq(RoomEntity::getId, roomEntity.getId())
            );

            //踢出该房间所有用户
            Response response = rtcRoomManager.listUser(appId, roomEntity.getId());
//            JSONObject jsonObject = JSON.parseObject(response.getInfo());
//            JSONArray users = jsonObject.getJSONArray("users");
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            users.stream().forEach( e-> {
                JSONObject jo = (JSONObject)e;
                String userId = jo.getString("userId");
                log.info("closeRoom userId:{{}}",userId);
                try {
                    rtcRoomManager.kickUser(appId,roomEntity.getId(),userId);
                } catch (QiniuException qiniuException) {
                    qiniuException.printStackTrace();
                }
            });
            log.info("-------------------------------关闭房间 结束-----------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"系统异常");
        }
        return ResultUtil.success();
    }



    @Override
    public Result closeRoomForManage(String id) {
        try {
            log.info("closeRoomForManage id:{{}}",id);
            RoomEntity roomEntity = baseMapper.selectById(id);
            log.info("closeRoomForManage roomEntity:{{}}",roomEntity);
            if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
            }
            if(roomEntity== null || "1".equals(roomEntity.getRoomStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
            }
            roomEntity.setRoomStatus("1");
            baseMapper.updateById(roomEntity);
            log.info("closeRoomForManage roomEntity:{{}}",roomEntity);

            //踢出该房间所有用户
            Response response = rtcRoomManager.listUser(appId, roomEntity.getId());
//            JSONObject jsonObject = JSON.parseObject(response.getInfo());
//            JSONArray users = jsonObject.getJSONArray("users");
            log.info("closeRoomForManage response:{{}}",response);
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            log.info("closeRoomForManage users:{{}}",users);
            users.stream().forEach( e-> {
                JSONObject jo = (JSONObject)e;
                String userId = jo.getString("userId");
                try {
                    rtcRoomManager.kickUser(appId,roomEntity.getId(),userId);
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

    @Override
    public Result closeCourse(String id) {
        try {
            log.info("closeCourse id:{{}}",id);
            //踢出该房间所有用户
            Response response = rtcRoomManager.listUser(appId, id);
//          JSONObject jsonObject = JSON.parseObject(response.getInfo());
//          JSONArray users = jsonObject.getJSONArray("users");
            log.info("closeCourse response:{{}}",response);
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            log.info("closeCourse users:{{}}",users);
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

    @Override
    public RoomEntity insertReturnEntity(RoomEntity entity) {
        return null;
    }

    @Override
    public RoomEntity updateReturnEntity(RoomEntity entity) {
        return null;
    }
    private TrainGroupUserEntity getGroupUser(String groupId) {
        return trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                .eq("userId", UserUtils.getUserId())
                .eq("trainGroupId", groupId)
                .eq("deleted", "0"));
    }

    @Override
    public void kickUser(List<RoomEntity> rooms,String userId) {
        rooms.forEach(e-> {
            Response response = null;
            try {
                response = rtcRoomManager.listUser(appId, e.getId());
                JSONObject jsonObject = JSON.parseObject(response.bodyString());
                JSONArray users = jsonObject.getJSONArray("users");
                users.forEach(i->{
                    String id = (String)i;
                    if(id.equals(userId)) {
                        try {
                            rtcRoomManager.kickUser(appId,e.getId(),userId);
                        } catch (QiniuException qiniuException) {
                            qiniuException.printStackTrace();
                        }
                    }
                });
            } catch (QiniuException qiniuException) {
                qiniuException.printStackTrace();
            }

        });
    }

    @Override
    public Result reserve(String id) {
        RoomEntity roomEntity = baseMapper.selectById(id);
        if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
        }
        TrainGroupUserEntity groupUser = getGroupUser(roomEntity.getTrainGroupId());
        if(groupUser == null){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"请先加入小组");
        }
        if(roomEntity== null
                || "1".equals(roomEntity.getDeleted())
                ||"1".equals(roomEntity.getRoomStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
        }

        Integer count = roomReserveMapper.selectCount(new QueryWrapper<RoomReserveEntity>()
                .eq("userId", UserUtils.getUserId())
                .eq("roomId", id));
        if(count>0) {
            //未开始
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"已预约");
        }

//        if(LocalDateTime.now().isAfter(roomEntity.getBeginDate().minusDays(1))) {
//            //未开始
//            return ResultUtil.error(ResultEnum.FAIL.getCode(),"直播即将开始，无需预约");
//        }
        RoomReserveEntity roomReserveEntity = new RoomReserveEntity();
        roomReserveEntity.setRoomId(id);
        roomReserveEntity.setUserId(UserUtils.getUserId());
        roomReserveMapper.insert(roomReserveEntity);
        userOrderRecordService.save(UserUtils.getUserId(),"3",
                "小组预约通知",
                "您已预约小组,时间为："+ DateUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss"),
                LocalDateTime.now(),
                "");

        return ResultUtil.success();
    }

    @Override
    public Result<Page<RoomListDto>> roomReserveList(String currentPage) {
        List<RoomListDto> roomListDtos = roomReserveMapper.roomList(UserUtils.getUserId());
        List<RoomListDto> collect = roomListDtos.stream().map(e -> {
            e.setHeadPicUrl(fileImagesPath +e.getHeadPicUrl());
            return e;
        }).collect(Collectors.toList());

        return ResultUtil.success(collect);
    }

    @Override
    @Transactional
    public Result reportRoom(ReportRoomRequest request) {
        log.info("-----------------举报房间开始啦------------------------");
        log.info("reportRoom request:{{}}",request);
        RoomReportEntity entity = new RoomReportEntity();
        BeanUtils.copyProperties(request,entity);
      if("0".equals(request.getType())) {
            RoomEntity roomEntity = roomMapper.selectById(request.getRoomId());
            if(roomEntity == null){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在");
            }
          entity.setBeginTime(roomEntity.getBeginDate());
        }else if("1".equals(request.getType())){
            QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
            userCoachTimeEntityQueryWrapper.eq("id",request.getRoomId());
            userCoachTimeEntityQueryWrapper.eq("deleted","0");
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
            log.info("reportRoom userCoachTimeEntity:{{}}",userCoachTimeEntity);
            if(userCoachTimeEntity == null){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"直播不存在");
            }
          entity.setBeginTime(userCoachTimeEntity.getBeginTime());
        }else if("2".equals(request.getType())){
          QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
          userCoachTimeEntityQueryWrapper.eq("coachCourseId",request.getRoomId());
          userCoachTimeEntityQueryWrapper.eq("deleted","0");
          UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
          log.info("reportRoom userCoachTimeEntity:{{}}",userCoachTimeEntity);
          if(userCoachTimeEntity == null){
              return ResultUtil.error(ResultEnum.FAIL.getCode(),"私教课程不存在");
          }
          entity.setBeginTime(userCoachTimeEntity.getBeginTime());
        }
        entity.setId(UUIDUtil.getShortUUID());
        entity.setType(request.getType());
        entity.setReportTime(LocalDateTime.now());
        entity.setCreateBy(request.getReportUserId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setLastModifiedBy(request.getReportUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        entity.setDeleted("0");
        log.info("reportRoom entity:{{}}",entity);
        roomReportMapper.insert(entity);
        if (StringUtils.isNotEmpty(request.getAttachUrlList())){
            for (VoUrl url : request.getAttachUrlList()){
                //保存选中图片
                Attach attach = attachService.saveAttach(request.getReportUserId(), url.getPicAttachName(), url.getPicAttachNewName(), url.getPicAttachSize());
                RoomReportAttachEntity attachEntity = new RoomReportAttachEntity();
                attachEntity.setId(UUIDUtil.getShortUUID());
                attachEntity.setReportId(entity.getId());
                attachEntity.setAttachUrlId(attach.getId());
                attachEntity.setCreateBy(request.getReportUserId());
                attachEntity.setCreateDate(LocalDateTime.now());
                attachEntity.setDeleted("0");
                attachEntity.setLastModifiedBy(request.getReportUserId());
                attachEntity.setLastModifiedDate(LocalDateTime.now());
                roomReportAttachMapper.insert(attachEntity);
            }
        }
        return ResultUtil.success();
    }

    @Override
    public Result coachcloseRoom(String id) {
        UserCoachTimeEntity userCoachTimeEntity = new UserCoachTimeEntity();
        try {
            log.info("coachcloseRoom id:{{}}",id);
           /* RoomEntity roomEntity = baseMapper.selectById(id);
            log.info("closeRoomForManage roomEntity:{{}}",roomEntity);
            if (checkGroupIsExist(roomEntity.getTrainGroupId())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组不存在");
            }
            if(roomEntity== null || "1".equals(roomEntity.getRoomStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"房间不存在或已关闭");
            }
            roomEntity.setRoomStatus("1");
            baseMapper.updateById(roomEntity);*/

            userCoachTimeEntity = userCoachTimeMapper.selectById(id);
            if(userCoachTimeEntity== null || "2".equals(userCoachTimeEntity.getLiveStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"直播间不存在或已关闭");
            }
            userCoachTimeEntity.setLiveStatus("2");
            userCoachTimeMapper.updateById(userCoachTimeEntity);

            log.info("coachcloseRoom roomEntity:{{}}",userCoachTimeEntity);
            //踢出该房间所有用户
            Response response = rtcRoomManager.listUser(appId,id);
//            JSONObject jsonObject = JSON.parseObject(response.getInfo());
//            JSONArray users = jsonObject.getJSONArray("users");
            log.info("coachcloseRoom response:{{}}",response);
            JSONObject jsonObject = JSON.parseObject(response.bodyString());
            JSONArray users = jsonObject.getJSONArray("users");
            log.info("coachcloseRoom users:{{}}",users);
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

}
