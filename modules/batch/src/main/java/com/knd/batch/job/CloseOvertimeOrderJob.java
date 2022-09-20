package com.knd.batch.job;

import com.knd.batch.service.feignInterface.PayFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author will
 */
@Slf4j
@Component
public class CloseOvertimeOrderJob implements BaseJob {
    @Resource
    private PayFeignClient payFeignClient;

    /**
     * //job批处理超时订单关闭
     */
    @Override
    public void execute(JobExecutionContext context) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            double begin = System.currentTimeMillis();
            //调用feign更新过期订单状态
            payFeignClient.closeOvertimeOrderBatch();
            double end = System.currentTimeMillis();
            String time2 = "共经历了 " + ((end - begin) / 1000) + "秒";
            //日志打印
            log.info("===============================job批处理超时订单关闭==================================");
            log.info("开始运行时间：" + sdf.format(new Date((long) begin)));
            log.info("执行结束时间：" + sdf.format(new Date((long) end)));
            log.info("共经历： " + time2);
            log.info("===============================job批处理超时订单关闭==================================");
//            return ResultUtil.success("成功， " + time2);
        } catch (Exception e) {
            //日志打印
            log.info("===============================job批处理超时订单关闭==================================\n"
                    + "失败，原因：\n"
                    + e.getMessage()
                    + "\n"
                    + "===============================job批处理超时订单关闭==================================");
            //这里不需要回滚，每个用户的计算都是由单独的线程独立计算的，用户之间的数据互不影响，因此，如果某一用户抛出异常，
            // 只需要在计算该用户的线程回滚即可，其他线程不需要回滚
//            return ResultUtil.error("U0000", "失败,原因：" + e.getMessage());
        }

    }
}
