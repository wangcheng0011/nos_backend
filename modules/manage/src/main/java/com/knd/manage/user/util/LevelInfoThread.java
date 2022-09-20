//package com.knd.manage.user.util;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.knd.common.basic.StringUtils;
//import com.knd.common.level.LevelUtil;
//import com.knd.common.response.CustomResultException;
//import com.knd.common.response.JobException;
//import com.knd.common.response.ResultEnum;
//import com.knd.common.uuid.UUIDUtil;
//import com.knd.manage.course.mapper.TrainCourseHeadInfoMapper;
//import com.knd.manage.user.entity.User;
//import com.knd.manage.user.entity.UserLevelInfo;
//import com.knd.manage.user.entity.UserTrainLevel;
//import com.knd.manage.user.mapper.UserLevelInfoMapper;
//import com.knd.manage.user.mapper.UserMapper;
//import com.knd.manage.user.mapper.UserTrainLevelMapper;
//import com.knd.manage.user.dto.StarCountAndBufferDayCountDto;
//import com.knd.manage.user.service.LevelInfoThreadService;
//import com.knd.manage.user.service.impl.LevelInfoThreadServiceImpl;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
//@Data
//@Slf4j
//public class LevelInfoThread implements Runnable {
//    //用户信息
//    private User u;
//    //昨天日期
//    private LocalDate yesterday;
//    //
//    private LevelInfoThreadService levelInfoThreadService;
//
//    //主执行方法
//    @Override
//    public void run() {
//        //等级批处理具体逻辑
//        levelInfoThreadService.doLevelInfoThread(u, yesterday);
//    }
//
//}
