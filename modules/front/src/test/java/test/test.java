package test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.common.basic.DateUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.FrontApplication;
import com.knd.front.entity.TrainCourseHeadInfo;
import com.knd.front.entity.User;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.user.mapper.SqlMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description 造用户训练 数据
 * @date 2020/7/21
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrontApplication.class)
public class test {

    @Autowired
    private SqlMapper sqlMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 501; i <= 599; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }

    @Test
    public void test2() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 600; i <= 699; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }

    @Test
    public void test3() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 689; i <= 699; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }
    @Test
    public void test13() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 775; i <= 799; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }

    @Test
    public void test4() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 800; i <= 899; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }
    @Test
    public void test14() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 875; i <= 899; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }

    @Test
    public void test5() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 900; i <= 1000; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }

    @Test
    public void test6() throws Exception {
        LocalDateTime beginTime = DateUtils.parseLocalDateTime("2019-03-01 14:26:11");
        LocalDateTime endTime = DateUtils.parseLocalDateTime("2020-07-20 14:26:11");
        List<kk> lk = new ArrayList<>();
        LocalDateTime temp1, temp2;
        int w = 0;
        TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();
        trainCourseHeadInfo.setCourseHeadId("4de9405e9229496491b7ff7d3b6578d1");
        trainCourseHeadInfo.setVedioEndTime("2019-07-06 12:27:50");
        trainCourseHeadInfo.setVedioBeginTime("2019-07-06 11:17:50");
        trainCourseHeadInfo.setTotalDurationSeconds("60");
        trainCourseHeadInfo.setActualTrainSeconds("60");
        trainCourseHeadInfo.setEquipmentNo("60");
        trainCourseHeadInfo.setCreateBy("1");
        trainCourseHeadInfo.setLastModifiedBy("1");
        trainCourseHeadInfo.setDeleted("0");
        trainCourseHeadInfo.setLastModifiedDate(LocalDateTime.now());
        User user = new User();
        user.setMobile("1234567890");
        user.setFrozenFlag("0");
        user.setPassword("12112");
        user.setRegistTime(DateUtils.getCurrentDateTimeStr());
        user.setCreateBy("1");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedBy("1");
        user.setLastModifiedDate(LocalDateTime.now());
        user.setDeleted("0");
        while (true) {
            temp1 = beginTime.plusDays(w * 7);
            w++;
            temp2 = beginTime.plusDays(w * 7 - 1);
            if (temp2.isAfter(endTime)) {
                break;
            }
            lk.add(new kk(temp1, temp2));
        }
        for (int i = 597; i <= 599; i++) {
            String userid = "user" + i;
            user.setId(userid);
            user.setNickName(userid);
            userMapper.insert(user);
            System.out.println("新增用户完成,id为" + userid);
            for (kk k : lk) {
                for (int j = 0; j < 4; j++) {
                    LocalDateTime begin = k.getBegin().plusDays(j);
                    trainCourseHeadInfo.setId(UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setUserId(userid);
                    trainCourseHeadInfo.setCourse("test" + UUIDUtil.getShortUUID());
                    trainCourseHeadInfo.setCreateDate(begin);
                    int insert = sqlMapper.insert(trainCourseHeadInfo);
                    System.out.println(begin);
                    System.out.println("新增用户训练信息中，id为" + trainCourseHeadInfo.getId() + "userid为" + userid);
                }
            }
        }
    }

    @Test
    public void test8(){
        System.out.println((int)(5 * Math.ceil(138/5.0)));
    }

    @AllArgsConstructor
    @Data
    class kk {
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private LocalDateTime begin;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private LocalDateTime end;
    }
}

