package com.knd.batch.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.knd.batch.entity.TrainCourseHeadInfo;
import com.knd.batch.entity.User;
import com.knd.batch.mapper.TrainCourseHeadInfoMapper;
import com.knd.batch.mapper.UserMapper;
import com.knd.batch.service.LevelInfoThreadService;
import com.knd.batch.util.LevelInfoThread;
import com.knd.common.basic.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DoLevelJob implements BaseJob {
    //             com.knd.batch.job.DoLevelJob
    @Resource
    private UserMapper userMapper;

    @Autowired
    private LevelInfoThreadService levelInfoThreadService;

    @Resource
    private TrainCourseHeadInfoMapper trainCourseHeadInfoMapper;

    /**
     * //job批处理等级信息【多线程并发运行】
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
//            //获取昨天日期[测试]
//            QueryWrapper<TrainCourseHeadInfo> qw2 = new QueryWrapper<>();
//            qw2.eq("id","666");
//            qw2.select("lastModifiedDate");
//            TrainCourseHeadInfo t = trainCourseHeadInfoMapper.selectOne(qw2);
//            LocalDate ye = t.getLastModifiedDate().toLocalDate().minusDays(1);
            //
            LocalDate ye = LocalDate.now().minusDays(1);
            log.info("昨天日期是"+ye);
            //
            //
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            double begin = new Date().getTime();
            //昨天日期
            //获取所有未删除的用户信息
            QueryWrapper<User> qw = new QueryWrapper<>();
            //分别是用户id、训练等级、训练周期开始日期
            qw.select("id", "trainLevel", "trainPeriodBeginTime");
            qw.eq("deleted", "0");
            List<User> lu = userMapper.selectList(qw);
            if (lu.isEmpty()) {
                //无用户信息
                log.info("结束，无用户信息");
//                return ResultUtil.success("结束，无用户信息");
            }
            log.info("开启线程池");
            //创建线程池，线程数为10，等待池大小为用户的数量，线程空闲200毫秒后则会被回收
            ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 15, 200, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<Runnable>(lu.size()));
            //遍历每一个用户，让后放入线程池中
            for (User u : lu) {
                //new一个线程对象，将对象放入线程池中，成为每一个需要做的任务
                LevelInfoThread lit = new LevelInfoThread();
                lit.setU(u);
                //昨天日期
                lit.setYesterday(ye);
                lit.setLevelInfoThreadService(levelInfoThreadService);
                executor.execute(lit);
            }
            //延迟关闭线程池，需要所有线程都完成任务并空闲200毫秒后被回收才关闭线程池
            executor.shutdown();
            //不结束主线程，等待所有线程都结束后才结束，每秒执行一次
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            }
            log.info("线程池已经关闭");
            double end = new Date().getTime();
            String time2 = "共经历了 " + ((end - begin) / 1000) + "秒";
            //日志打印
            log.info("===============================job批处理等级信息【多线程并发运行】==================================");
            log.info("开始运行时间：" + sdf.format(new Date((long) begin)));
            log.info("执行结束时间：" + sdf.format(new Date((long) end)));
            log.info("共经历： " + time2);
            log.info("===============================job批处理等级信息【多线程并发运行】==================================");
//            return ResultUtil.success("成功， " + time2);
        } catch (InterruptedException e) {
            //日志打印
            log.info("===============================job批处理等级信息【多线程并发运行】==================================\n"
                    + "失败，原因：\n"
                    + e.getMessage()
                    + "\n"
                    + "===============================job批处理等级信息【多线程并发运行】==================================");
            //这里不需要回滚，每个用户的计算都是由单独的线程独立计算的，用户之间的数据互不影响，因此，如果某一用户抛出异常，
            // 只需要在计算该用户的线程回滚即可，其他线程不需要回滚
//            return ResultUtil.error("U0000", "失败,原因：" + e.getMessage());
        }

    }
}
