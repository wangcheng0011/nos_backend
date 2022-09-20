//package com.knd.manage.user.util;
//
//import com.knd.manage.basedata.service.IBaseActionEquipmentService;
//import com.knd.manage.user.service.IUserLevelInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//
//@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
//public class MyScheduled {
//     @Resource
//    private IUserLevelInfoService iUserLevelInfoService;
//
//    //启动定时任务,每天凌晨一点执行一次
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void sc() throws InterruptedException {
//        iUserLevelInfoService.doLevel2(LocalDate.now().minusDays(1));
//    }
//}
//
//
