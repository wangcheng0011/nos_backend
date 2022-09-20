package com.knd.batch.util;

import com.knd.batch.entity.User;
import com.knd.batch.service.LevelInfoThreadService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;


@Data
@Slf4j
public class LevelInfoThread implements Runnable {
    //用户信息
    private User u;
    //昨天日期
    private LocalDate yesterday;
    //
    private LevelInfoThreadService levelInfoThreadService;

    //主执行方法
    @Override
    public void run() {
        //等级批处理具体逻辑
        levelInfoThreadService.doLevelInfoThread(u, yesterday);
    }

}
