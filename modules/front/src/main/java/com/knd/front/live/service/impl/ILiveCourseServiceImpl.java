package com.knd.front.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
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
    public Result getRoomToken(String timeId,String type) {
        log.info("----------我要开始获取直播课程roomToken啦------------------");
        log.info("getRoomToken roomId:{{}}",timeId);
        log.info("getRoomToken type:{{}}",type);
        try {
            Map<String,String> map = new HashMap<>();
            //判断身份
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(timeId);
            //timeId查不到用courseid查
            if(StringUtils.isEmpty(userCoachTimeEntity)){
                QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
                userCoachTimeEntityQueryWrapper.eq("coachCourseId",timeId);
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
                //当前身份是所属直播课程教练

                //获取对应预约课程的roomToken
                //RoomEntity roomEntity1 = roomMapper.selectById(roomId);
                //观看的时候创建新的用户id
                userId = "1".equals(type) ? userId + UUIDUtil.getShortUUID() : userId;
                log.info("getRoomToken newCoachUserId:{{}}",userId);
                String roomToken = rtcRoomManager.getRoomToken(appId,timeId,userId,DateUtils.addDate(new Date(), 30).getTime(),"admin");
                log.info("教练获取签发roomToken："+roomToken);
                map.put("roomToken",roomToken);
                String rtmpPublishURL = LiveManager.getRTMPPublishURL(coachUserId);
                map.put("rtmpPublishURL",rtmpPublishURL);
                userCoachTimeEntity.setReplayUrl(rtmpPublishURL);
                //标记直播课程实际开始直播时间
                if(StringUtils.isEmpty(userCoachTimeEntity.getActualBeginTime())) {
                    userCoachTimeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_ING.getCode());
                    userCoachTimeEntity.setActualBeginTime(LocalDateTime.now());
                }
                userCoachTimeMapper.updateById(userCoachTimeEntity);
            }else{
                //当前身份是预约用户
                //判断用户是否已预约该直播课程
                UserCoachCourseOrderEntity userCoachCourseOrderEntity = userCoachCourseOrderMapper.selectOne(new QueryWrapper<UserCoachCourseOrderEntity>()
                        .eq("coachTimeId", userCoachTimeEntity.getId())
                        .eq("orderUserId", UserUtils.getUserId())
                        .eq("isOrder", LiveOrderStatusEnum.LIVE_FINISHED.getCode()).eq("deleted", "0"));
                log.info("getRoomToken userCoachCourseOrderEntity:{{}}",userCoachCourseOrderEntity);
                if(userCoachCourseOrderEntity == null) {
                    return ResultUtil.error(ResultEnum.FAIL.getCode(),"未预约该课程");
                }
                String roomToken = null;
                //RoomEntity roomEntity1 = roomMapper.selectById(roomId);
                //观看的时候创建新的用户id
                userId = "1".equals(type) ? userId + UUIDUtil.getShortUUID() : userId;
                log.info("getRoomToken coachUserId:{{}}",coachUserId);
                roomToken = rtcRoomManager.getRoomToken(appId,timeId,userId,DateUtils.addDate(new Date(), 30).getTime(),"user");

                log.info("预约用户获取签发roomToken："+roomToken);
                map.put("roomToken",roomToken);
            }
            return ResultUtil.success(map);
        } catch (Exception e) {
                e.printStackTrace();
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"服务器异常,获取roomToken失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result endCourse(String id) {
        //结束课程更新结束课程时间
        UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectById(id);
        userCoachTimeEntity.setLiveStatus(LiveCourseStatusEnum.LIVE_FINISHED.getCode());
        userCoachTimeEntity.setActualEndTime(LocalDateTime.now());
       // userCoachTimeMapper.updateById(userCoachTimeEntity);
        //进行录制
        Stream stream = LiveManager.getStream(UserUtils.getUserId());
        if(stream == null) {
            throw new CustomResultException("录播失败");
        }
        long startTime = userCoachTimeEntity.getActualBeginTime().toEpochSecond(ZoneOffset.of("+8"));
        long endTime = userCoachTimeEntity.getActualEndTime().toEpochSecond(ZoneOffset.of("+8"));
        String replayURL = LiveManager.saveReplay(stream, startTime, endTime);
        log.info("replayURL:"+replayURL+"-----startTime"+startTime+"-----endTime"+endTime);
        userCoachTimeEntity.setReplayUrl(replayURL);
        userCoachTimeMapper.updateById(userCoachTimeEntity);
        return ResultUtil.success();
    }

    @Override
    public Result closeUserCoachTime(String id) {
        UserCoachTimeEntity userCoachTimeEntity = new UserCoachTimeEntity();
        try {
            log.info("closeUserCoachCourseOrder id:{{}}",id);
            userCoachTimeEntity = userCoachTimeMapper.selectById(id);
            if(userCoachTimeEntity== null || "2".equals(userCoachTimeEntity.getLiveStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"直播间不存在或已关闭");
            }
            userCoachTimeEntity.setLiveStatus("2");
            userCoachTimeMapper.updateById(userCoachTimeEntity);

            log.info("closeUserCoachCourseOrder roomEntity:{{}}",userCoachTimeEntity);
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
}
