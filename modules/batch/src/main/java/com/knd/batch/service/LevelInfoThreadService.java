package com.knd.batch.service;



import com.knd.batch.entity.User;

import java.time.LocalDate;

public interface LevelInfoThreadService {

    //等级批处理具体逻辑
    void doLevelInfoThread(User u, LocalDate yesterday);

}
