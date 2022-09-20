package com.knd.front.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knd.common.em.VipEnum;
import com.knd.front.common.util.JPushUtil;
import com.knd.front.entity.ProgramEntity;
import com.knd.front.entity.ProgramPlanGenerationEntity;
import com.knd.front.entity.User;
import com.knd.front.live.entity.RoomEntity;
import com.knd.front.live.mapper.RoomMapper;
import com.knd.front.live.service.impl.RoomServiceImpl;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.train.mapper.ProgramPlanGenerationDao;
import com.knd.front.train.mapper.TrainProgramMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Log4j2
public class SaticScheduleTask {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomServiceImpl roomServiceImpl;
    @Autowired
    private TrainProgramMapper trainProgramMapper;
    @Autowired
    private ProgramPlanGenerationDao programPlanGenerationDao;
    @Autowired
    private JPushUtil jPushUtil;
    //1.添加定时任务 每天更新会员状态
     @Scheduled(cron = "0 01 0 ? * *")
    //或直接指定时间间隔，例如：5秒
     // @Scheduled(fixedRate=5000)
    private void updateUserVipStatusTasks() {
        System.err.println("每天更新会员状态 执行静态定时任务时间: " + LocalDateTime.now());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("deleted",0);
        List<User> users = userMapper.selectList(userQueryWrapper);
        System.out.println("user"+users);
        users.stream().forEach(i->{
            LocalDate vipEndDate = i.getVipEndDate();
            if(null!=vipEndDate&&vipEndDate.isBefore(LocalDate.now())){
                userMapper.update(
                        null,
                        Wrappers.<User>lambdaUpdate()
                                .set(User::getMasterId, "")
                                .set(User::getVipStatus, VipEnum.ORDINARY_VIP.getCode())
                                .eq(User::getId, i.getId())
                );
            }
        });
    }

    //1.添加定时任务 关闭房间
   // @Scheduled(cron = "0 01 0 ? * *")
    @Scheduled(cron = "0 */1 * * * ?")
    //或直接指定时间间隔，例如：5秒
    // @Scheduled(fixedRate=5000)
    private void closeRoom() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
      //  log.info("定时任务 closeRoom--------------------执行静态定时任务--------------------------------");
        QueryWrapper<RoomEntity> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("deleted",0);
        List<RoomEntity> rooms = roomMapper.selectList(roomQueryWrapper);
        System.out.println("定时任务 closeRoom rooms"+rooms);
        rooms.stream().forEach(i->{
          //  log.info("定时任务 closeRoom room:{{}}",i);
            LocalDateTime vipEndDate = i.getEndDate();
            if(null!=vipEndDate&&vipEndDate.isBefore(LocalDateTime.now())&&"0".equals(i.getRoomStatus())){
                    roomServiceImpl.closeRoom(i.getId());
                    roomServiceImpl.scheduledCloseRoom(i.getId());

            }
        });
    }

    //1.添加定时任务 每天早上8点和晚上8点触发
     @Scheduled(cron = ("0 0 8,20 * * ?"))
     //@Scheduled(cron = ("0 0 16 * * ?"))
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(cron = ("*/5 * * * * ?"))
    //每小时执行一次
    //@Scheduled(cron = ("0 0 */1 * * ?"))
   // @PostConstruct
    private void trainProgramPush() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        log.info("定时任务 trainProgramPush--------------------执行静态定时任务--------------------------------");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String formatDate = dtf.format(now);
        System.out.println("trainProgramPush:"+formatDate);
        QueryWrapper<ProgramPlanGenerationEntity> programPlanGenerationEntityQueryWrapper = new QueryWrapper<>();
        programPlanGenerationEntityQueryWrapper.eq("date_format(trainDate ,'%Y-%m-%d')",formatDate);
        programPlanGenerationEntityQueryWrapper.eq("trainFinishFlag","0");
         programPlanGenerationEntityQueryWrapper.eq("deleted","0");
        List<ProgramPlanGenerationEntity> programPlanGenerationEntities = programPlanGenerationDao.selectList(programPlanGenerationEntityQueryWrapper);
        log.info("trainProgramPush programPlanGenerationEntities:{{}}",programPlanGenerationEntities);
        if(programPlanGenerationEntities.size()>0){
            ProgramPlanGenerationEntity programPlanGenerationEntity = programPlanGenerationEntities.get(0);
            log.info("定时任务 trainProgramPush programPlanGenerationEntity:{{}}",programPlanGenerationEntity);
            QueryWrapper<ProgramEntity> programEntityQueryWrapper = new QueryWrapper<>();
            programEntityQueryWrapper.eq("id",programPlanGenerationEntity.getTrainProgramId());
            programEntityQueryWrapper.eq("deleted",0);
            ProgramEntity programEntity = trainProgramMapper.selectOne(programEntityQueryWrapper);
            log.info("定时任务 trainProgramPush programEntity:{{}}",programEntity);
            // 设置推送参数
            // 这里可以自定义推送参数了
            Map<String, String> parm = new HashMap<>();
            // 设置提示信息,内容是文章标题
            parm.put("title","待接受任务");
            parm.put("alias",programEntity.getUserId());
            parm.put("msg",programEntity.getProgramName());
            parm.put("trainProgramId",programEntity.getId());
            log.info("定时任务 trainProgramPush parm:{{}}",parm);
            jPushUtil.jpushAll(parm);
            System.out.println("定时任务 trainProgramPush parm"+parm);
        }
    }


}