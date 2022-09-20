package com.knd.batch.job;

import com.knd.batch.service.IAttachService;
import com.knd.batch.service.ILiveCourseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class LiveReplayJob implements BaseJob {

    @Autowired
    private ILiveCourseService iLiveCourseService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("===============================job批处理直播回放录制开始==================================");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double begin = System.currentTimeMillis();
        iLiveCourseService.replayBatch();
        double end = System.currentTimeMillis();
        String time2 = "共经历了 " + ((end - begin) / 1000) + "秒";
        //日志打印
        log.info("开始运行时间：" + sdf.format(new Date((long) begin)));
        log.info("执行结束时间：" + sdf.format(new Date((long) end)));
        log.info("共经历： " + time2);
        log.info("===============================job批处理直播回放录制结束==================================");
    }
}
