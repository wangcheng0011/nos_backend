package com.knd.batch.service.impl;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.knd.batch.entity.JobEntry;
import com.knd.batch.entity.JobRun;
import com.knd.batch.entity.JobSearch;
import com.knd.batch.job.BaseJob;
import com.knd.batch.service.SchedulerService;
import com.knd.common.response.CustomResultException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Resource
    private Scheduler scheduler;


    @Override
    public void addJob(JobEntry jobEntry) throws IllegalAccessException, InstantiationException, SchedulerException {
        if (StringUtils.isBlank(jobEntry.getCronExpression())) {
            throw new NullPointerException();
        }
        if (!isValidExpression(jobEntry.getCronExpression())) {
            throw new CustomResultException("cron表达式错误");
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(jobEntry.getJobClassName());
        } catch (ClassNotFoundException e) {
            throw new CustomResultException("类未找到");
        }
        if (BaseJob.class.isAssignableFrom(clazz)) {
            BaseJob bj = (BaseJob) clazz.newInstance();
            JobDetail jobDetail = JobBuilder.newJob(bj.getClass()).withIdentity(jobEntry.getJobClassName(), jobEntry.getJobGroupName()).build();// 任务名称和组构成任务key
            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobEntry.getJobClassName(), jobEntry.getJobGroupName())// 触发器key
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobEntry.getCronExpression())).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    @Override
    public void updateJob(JobEntry jobEntry) throws SchedulerException {
        if (StringUtils.isBlank(jobEntry.getCronExpression())) {
            throw new NullPointerException();
        }
        if (!isValidExpression(jobEntry.getCronExpression())) {
            throw new CustomResultException("cron表达式错误");
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobEntry.getJobClassName(), jobEntry.getJobGroupName());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobEntry.getCronExpression())).build();
        // 重启触发器
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    @Override
    public boolean deleteJob(JobSearch jobSearch) throws SchedulerException {
        return scheduler.deleteJob(new JobKey(jobSearch.getJobClassName(), jobSearch.getJobGroupName()));
    }

    @Override
    public void runAJobNow(JobRun jobRun) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobRun.getJobClassName(), jobRun.getJobGroupName());
        JobDataMap map = new JobDataMap();
        if (jobRun.getId() != null) {
            map.put("id", jobRun.getId());
        }
        scheduler.triggerJob(jobKey, map);
    }

    @Override
    public List<Map<String, Object>> queryAllJob() throws SchedulerException {
        List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Map<String, Object> map = new HashMap<>();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("description", "触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime", cronExpression);
                }
                jobList.add(map);
            }
        }
        return jobList;
    }

    @Override
    public void pauseJob(JobSearch jobSearch) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobSearch.getJobClassName(), jobSearch.getJobGroupName());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(JobSearch jobSearch) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobSearch.getJobClassName(), jobSearch.getJobGroupName());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 判断是否是cron表达式
     *
     * @param cronExpression
     * @return
     */
    private static boolean isValidExpression(final String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date
     */
    private LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        return instant.atZone(zoneId).toLocalDateTime();
    }

}
