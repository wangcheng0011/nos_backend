package com.knd.front.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.LiveCourseStatusEnum;
import com.knd.common.em.LiveOrderStatusEnum;
import com.knd.common.qiniu.LiveManager;
import com.knd.common.qiniu.RtcRoomManager;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.live.entity.UserCoachCourseOrderEntity;
import com.knd.front.live.entity.UserCoachTimeEntity;
import com.knd.front.live.mapper.RoomMapper;
import com.knd.front.live.mapper.UserCoachCourseMapper;
import com.knd.front.live.mapper.UserCoachCourseOrderMapper;
import com.knd.front.live.mapper.UserCoachTimeMapper;
import com.knd.front.live.service.ILiveCourseService;
import com.qiniu.pili.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ILiveCourseServiceImpl implements ILiveCourseService {

    @Autowired
    private UserCoachCourseMapper userCoachCourseMapper;
    @Autowired
    private UserCoachCourseOrderMapper userCoachCourseOrderMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserCoachTimeMapper userCoachTimeMapper;

    @Autowired
    private RtcRoomManager rtcRoomManager;

    @Value("${qiniu.appId}")
    private String appId;

    @Override
    public Result getRoomToken(String id,String type) {
        log.info("----------??????????????????????????????roomToken???------------------");
        log.info("getRoomToken roomId:{{}}",id);
        log.info("getRoomToken type:{{}}",type);
        try {
            Map<String,String> map = new HashMap<>();
            //????????????
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(id);
            //timeId????????????courseid???
            if(StringUtils.isEmpty(userCoachTimeEntity)){
                QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
                userCoachTimeEntityQueryWrapper.eq("coachCourseId",id);
                userCoachTimeEntityQueryWrapper.eq("deleted",0);
                userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
            }
            log.info("getRoomToken userCoachTimeEntity:{{}}",userCoachTimeEntity);
     //       String coachCourseId = userCoachTimeEntity.getCoachCourseId();
//            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(coachCourseId);
            String coachUserId = userCoachTimeEntity.getCoachUserId();
            log.info("getRoomToken coachUserId:{{}}",coachUserId);
            String userId = UserUtils.getUserId();
            log.info("getRoomToken userId:{{}}",userId);
            if(UserUtils.getUserId().equals(coachUserId)) {
                //???????????????????????????????????????

                //???????????????????????????roomToken
                //RoomEntity roomEntity1 = roomMapper.selectById(roomId);
                //?????????????????????????????????id
                userId = "1".equals(type) ? userId + UUIDUtil.getShortUUID() : userId;
                log.info("getRoomToken newCoachUserId:{{}}",userId);
                String roomToken = rtcRoomManager.getRoomToken(appId,id,userId,DateUtils.addDate(new Date(), 30).getTime(),"admin");
                log.info("??????????????????roomToken???"+roomToken);
                map.put("roomToken",roomToken);
                String rtmpPublishURL = LiveManager.getRTMPPublishURL(coachUserId);
                map.put("rtmpPublishURL",rtmpPublishURL);
                userCoachTimeEntity.setReplayUrl(rtmpPublishURL);
                //??????????????????????????????????????????
                if(StringUtils.isEmpty(userCoachTimeEntity.getActualBeginTime())) {
                    userCoachTimeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_ING.getCode());
                    userCoachTimeEntity.setActualBeginTime(LocalDateTime.now());
                }
                userCoachTimeMapper.updateById(userCoachTimeEntity);
            }else{
                //???????????????????????????
                //??????????????????????????????????????????
                UserCoachCourseOrderEntity userCoachCourseOrderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                        .eq("coachTimeId", userCoachTimeEntity.getId())
                        .eq("orderUserId", UserUtils.getUserId())
                        .eq("isOrder", LiveOrderStatusEnum.LIVE_FINISHED.getCode()).eq("deleted", "0"));
                log.info("getRoomToken userCoachCourseOrderEntity:{{}}",userCoachCourseOrderEntity);
                if(userCoachCourseOrderEntity == null) {
                    return ResultUtil.error(ResultEnum.FAIL.getCode(),"??????????????????");
                }
                String roomToken = null;
                //RoomEntity roomEntity1 = roomMapper.selectById(roomId);
                //?????????????????????????????????id
                userId = "1".equals(type) ? userId + UUIDUtil.getShortUUID() : userId;
                log.info("getRoomToken coachUserId:{{}}",coachUserId);
                roomToken = rtcRoomManager.getRoomToken(appId,id,userId,DateUtils.addDate(new Date(), 30).getTime(),"user");

                log.info("????????????????????????roomToken???"+roomToken);
                map.put("roomToken",roomToken);
            }
            return ResultUtil.success(map);
        } catch (Exception e) {
                e.printStackTrace();
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"???????????????,??????roomToken??????");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result endCourse(String id) {
        //????????????????????????????????????
        UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(id);
        userCoachTimeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_FINISHED.getCode());
        userCoachTimeEntity.setActualEndTime(LocalDateTime.now());
       // userCoachTimeMapper.updateById(userCoachTimeEntity);
        //????????????
        Stream stream = LiveManager.getStream(UserUtils.getUserId());
        if(stream == null) {
            throw new CustomResultException("????????????");
        }
        long startTime = userCoachTimeEntity.getActualBeginTime().toEpochSecond(ZoneOffset.of("+8"));
        long endTime = userCoachTimeEntity.getActualEndTime().toEpochSecond(ZoneOffset.of("+8"));
        String replayURL = LiveManager.saveReplay(stream, startTime, endTime);
        log.info("replayURL:"+replayURL+"-----startTime"+startTime+"-----endTime"+endTime);
        userCoachTimeEntity.setReplayUrl(replayURL);
        userCoachTimeMapper.updateById(userCoachTimeEntity);
        return ResultUtil.success();
    }

  /*  @Override
    public Result closeUserCoachTime(String id) {
        UserCoachTimeEntity userCoachTimeEntity = new UserCoachTimeEntity();
        try {
            log.info("closeUserCoachCourseOrder id:{{}}",id);
            userCoachTimeEntity = userCoachTimeMapper.selectById(id);
            if(userCoachTimeEntity== null || "2".equals(userCoachTimeEntity.getLiveStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"??????????????????????????????");
            }
            userCoachTimeEntity.setLiveStatus("2");
            userCoachTimeMapper.updateById(userCoachTimeEntity);

            log.info("closeUserCoachCourseOrder roomEntity:{{}}",userCoachTimeEntity);
            //???????????????????????????
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
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"????????????");
        }
        return ResultUtil.success();
    }*/
}
