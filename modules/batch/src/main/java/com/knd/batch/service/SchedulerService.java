package com.knd.batch.service;


import com.knd.batch.entity.JobEntry;
import com.knd.batch.entity.JobRun;
import com.knd.batch.entity.JobSearch;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

public interface SchedulerService {
    /**
     * 添加任务
     *
     * @param jobEntry
     */
    void addJob(JobEntry jobEntry) throws IllegalAccessException, InstantiationException, SchedulerException;

    /**
     * 更新任务
     *
     * @param jobEntry
     */
    void updateJob(JobEntry jobEntry) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param jobSearch
     */
    boolean deleteJob(JobSearch jobSearch) throws SchedulerException;

    /**
     * 立即执行一个任务
     *
     * @param jobRun
     */
    void runAJobNow(JobRun jobRun) throws SchedulerException;

    /**
     * 获取所有任务
     *
     * @return
     */
    List<Map<String, Object>> queryAllJob() throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param jobSearch
     */
    void pauseJob(JobSearch jobSearch) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param jobSearch
     */
    void resumeJob(JobSearch jobSearch) throws SchedulerException;


}
