package com.knd.batch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.batch.entity.UserCoachTimeEntity;
import com.knd.batch.mapper.UserCoachTimeMapper;
import com.knd.batch.service.ILiveCourseService;
import com.knd.common.qiniu.LiveManager;
import com.knd.common.qiniu.RtcRoomManager;
import com.knd.common.response.CustomResultException;
import com.knd.common.userutil.UserUtils;
import com.qiniu.pili.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Slf4j
public class ILiveCourseServiceImpl implements ILiveCourseService {
    @Autowired
    private UserCoachTimeMapper userCoachTimeMapper;


    @Value("${qiniu.appId}")
    private String appId;


    @Override
    public void replayBatch() {
        String yestodayStr = LocalDate.now().minusDays(1).toString();
        QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
        userCoachTimeEntityQueryWrapper.eq("replayUrl","");
        userCoachTimeEntityQueryWrapper.and(e->{
            e.and(f->{f.isNull("actualEndTime")
                    .eq("date_format(endTime ,'%Y-%m-%d')", yestodayStr);})
                .or(g->{
                       g.eq("date_format(actualEndTime ,'%Y-%m-%d')", yestodayStr);
                    });
        });

        List<UserCoachTimeEntity> userCoachTimeEntities = userCoachTimeMapper.selectList(userCoachTimeEntityQueryWrapper);

        for(UserCoachTimeEntity userCoachTimeEntity : userCoachTimeEntities) {
            //判断如果是没有实际结束时间的，那么直播预计预计结束时间-实际直播开始时间必须大于1分钟，否则不予录制
            if(userCoachTimeEntity.getActualEndTime()== null) {
                if(userCoachTimeEntity.getActualBeginTime().plusSeconds(1)
                        .isAfter(userCoachTimeEntity.getEndTime())) {
                    continue;
                }
            }
            try {
                //进行录制
                Stream stream = LiveManager.getStream(userCoachTimeEntity.getCoachUserId());
                if(stream != null) {
                    long startTime = userCoachTimeEntity.getBeginTime().toEpochSecond(ZoneOffset.of("+8"));
                    long endTime = userCoachTimeEntity.getEndTime().toEpochSecond(ZoneOffset.of("+8"));
                    String replayURL = LiveManager.saveReplay(stream, startTime, endTime);
                    userCoachTimeEntity.setReplayUrl(replayURL);
                    userCoachTimeMapper.updateById(userCoachTimeEntity);
                }
            }catch (Exception e){
                log.info("id为{}的直播课程录制回放失败{}",
                        userCoachTimeEntity.getId(),e.getMessage());
            }


        }
    }

}
