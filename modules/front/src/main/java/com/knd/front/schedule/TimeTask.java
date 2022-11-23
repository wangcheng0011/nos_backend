/*
package com.knd.front.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.front.common.util.JPushUtil;
import com.knd.front.entity.ProgramEntity;
import com.knd.front.entity.ProgramPlanGenerationEntity;
import com.knd.front.live.service.UserOrderRecordService;
import com.knd.front.train.mapper.ProgramPlanGenerationDao;
import com.knd.front.train.mapper.TrainProgramMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Lazy(value = false)
public class TimeTask implements SchedulingConfigurer {
    //private final UserOrderRecordService userOrderRecordService= (UserOrderRecordService) ApplicationContextUtil.getBean("userOrderRecordService");
    @Autowired
    private UserOrderRecordService userOrderRecordService;
    @Autowired
    private TrainProgramMapper trainProgramMapper;
    @Autowired
    private ProgramPlanGenerationDao programPlanGenerationDao;
    @Autowired
    private JPushUtil jPushUtil;
    private static Logger log = LoggerFactory.getLogger(TimeTask.class);
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("定时器逻辑放这里");
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
        */
/*    UserOrderRecordEntity recordEntity = new UserOrderRecordEntity();
            recordEntity.setId(UUIDUtil.getShortUUID());
            recordEntity.setUserId(programEntity.getUserId());
            recordEntity.setIsRead("0");
            recordEntity.setOrderType("5");
            recordEntity.setOrderName("计划提醒");
            recordEntity.setContent(programEntity.getProgramName()+"开始时间为：" + DateUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy年MM月dd日HH时"));
            recordEntity.setOrderTime(programPlanGenerationEntity.getTrainDate());
            recordEntity.setRelevancyId(programEntity.getId());
            recordEntity.setCreateBy(programEntity.getUserId());
            recordEntity.setCreateDate(LocalDateTime.now());
            recordEntity.setDeleted("0");
            recordEntity.setLastModifiedBy(programEntity.getUserId());
            recordEntity.setLastModifiedDate(LocalDateTime.now());
            userOrderRecordMapper.insertUserOrderRecord(recordEntity);*//*

                    */
/* UserOrderRecordService userOrderRecordService = (UserOrderRecordService)ApplicationContextUtil.getBean("userOrderRecordService");*//*

                    userOrderRecordService.save(programEntity.getUserId(), "5",
                            "计划提醒",
                            programEntity.getProgramName()+"开始时间为：" + DateUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy年MM月dd日HH时"),
                            programPlanGenerationEntity.getTrainDate(),
                            programEntity.getId());
                    log.info("定时任务 trainProgramPush userId:{{}}",programPlanGenerationEntity.getUserId());
                });
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 每五秒执行一次
               // String cron = "0/5 * * * * ?";
               String cron = "0 0 17,20 * * ?";
                CronTrigger cronTrigger = new CronTrigger(cron);
                Date nextExec = cronTrigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        });
    }
}

*/
