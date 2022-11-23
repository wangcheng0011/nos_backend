package com.knd.front.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knd.common.em.VipEnum;
import com.knd.front.common.util.JPushUtil;
import com.knd.front.entity.User;
import com.knd.front.live.entity.RoomEntity;
import com.knd.front.live.mapper.RoomMapper;
import com.knd.front.live.service.UserOrderRecordService;
import com.knd.front.live.service.impl.RoomServiceImpl;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.train.mapper.ProgramPlanGenerationDao;
import com.knd.front.train.mapper.TrainProgramMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Log4j2
@Component
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
    /*    @Autowired
    private UserOrderRecordService userOrderRecordService;*/
    //注入方式不用@Autowired，改成下面的代码注入
    private final UserOrderRecordService userOrderRecordService= ApplicationContextUtil.getBean(UserOrderRecordService.class);




    @Autowired
    private JPushUtil jPushUtil;
    //1.添加定时任务 每天更新会员状态
     @Scheduled(cron = "0 01 0 ? * *")
    //或直接指定时间间隔，例如：5秒
     // @Scheduled(fixedRate=5000)
     public void updateUserVipStatusTasks() {
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
    public void closeRoom() {
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
    //  @Scheduled(cron = ("0 0 17 * * ?"))
   /*@Scheduled(cron = ("0 0 8,20 * * ?"))*/
     //@Scheduled(cron = ("0 0 16 * * ?"))
    //或直接指定时间间隔，例如：60秒
   //@Scheduled(cron = ("*/60 * * * * ?"))
    //每小时执行一次
   // @Scheduled(cron = ("0 0 */1 * * ?"))
  /* @PostConstruct
   public void trainProgramPush() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        log.info("定时任务 trainProgramPush--------------------执行静态定时任务--------------------------------");
        log.info("--------------------训练计划推送-----------------------");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String formatDate = dtf.format(now);
        System.out.println("trainProgramPush:"+formatDate);
        QueryWrapper<ProgramPlanGenerationEntity> programPlanGenerationEntityQueryWrapper = new QueryWrapper<>();
        programPlanGenerationEntityQueryWrapper.eq("date_format(trainDate ,'%Y-%m-%d')",formatDate);
        programPlanGenerationEntityQueryWrapper.eq("trainFinishFlag","0");
        programPlanGenerationEntityQueryWrapper.eq("deleted","0");
        programPlanGenerationEntityQueryWrapper.groupBy("trainDate","userId");
        List<ProgramPlanGenerationEntity> programPlanGenerationEntities = programPlanGenerationDao.selectList(programPlanGenerationEntityQueryWrapper);
        log.info("trainProgramPush programPlanGenerationEntities:{{}}",programPlanGenerationEntities);
        programPlanGenerationEntities.stream().forEach(programPlanGenerationEntity->{
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
            parm.put("alias", programEntity.getUserId());
            parm.put("msg",programEntity.getProgramName());
            parm.put("trainProgramId",programEntity.getId());
            jPushUtil.jpushAll(parm);
            log.info("定时任务 trainProgramPush parm:{{}}",parm);
            log.info("定时任务 trainProgramPush userId:{{}}",programEntity.getUserId());
            log.info("定时任务 trainProgramPush orderType:{{}}","5");
            log.info("定时任务 trainProgramPush content:{{}}",programEntity.getProgramName()+"开始时间为：" + DateUtils.formatLocalDateTime(programPlanGenerationEntity.getTrainDate(),"yyyy年MM月dd日 HH时"));
            log.info("定时任务 trainProgramPush trainDate:{{}}",programPlanGenerationEntity.getTrainDate());
            log.info("定时任务 trainProgramPush programId:{{}}",programEntity.getId());
            userOrderRecordService.save(programEntity.getUserId(), "5",
                    "计划提醒",
                    programEntity.getProgramName()+"时间为：" + DateUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy年MM月dd日HH时"),
                    programPlanGenerationEntity.getTrainDate(),
                    programEntity.getId());
            log.info("定时任务 trainProgramPush userId:{{}}",programPlanGenerationEntity.getUserId());
        });

    }*/
}