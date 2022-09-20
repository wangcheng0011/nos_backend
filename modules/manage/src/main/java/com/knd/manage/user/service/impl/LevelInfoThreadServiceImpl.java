//package com.knd.manage.user.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.knd.common.basic.StringUtils;
//import com.knd.common.level.LevelUtil;
//import com.knd.common.response.JobException;
//import com.knd.common.uuid.UUIDUtil;
//import com.knd.manage.course.mapper.TrainCourseHeadInfoMapper;
//import com.knd.manage.user.dto.StarCountAndBufferDayCountDto;
//import com.knd.manage.user.entity.User;
//import com.knd.manage.user.entity.UserLevelInfo;
//import com.knd.manage.user.entity.UserTrainLevel;
//import com.knd.manage.user.mapper.UserLevelInfoMapper;
//import com.knd.manage.user.mapper.UserMapper;
//import com.knd.manage.user.mapper.UserTrainLevelMapper;
//import com.knd.manage.user.service.LevelInfoThreadService;
//import com.knd.manage.user.util.TrainWeek;
//import com.knd.manage.user.util.WeekData;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Service
//@Slf4j
//@Transactional
//public class LevelInfoThreadServiceImpl implements LevelInfoThreadService {
//
//    //昨天日期
//    private LocalDate yesterday;
//
//    //用户训练等级历史记录表接口
//    @Resource
//    private UserTrainLevelMapper userTrainLevelMapper;
//
//    //训练课信息表接口
//    @Resource
//    private TrainCourseHeadInfoMapper trainCourseHeadInfoMapper;
//
//    @Resource
//    private UserMapper userMapper;
//
//    @Resource
//    private UserLevelInfoMapper baseMapper;
//
//
//    @Override
//    public void doLevelInfoThread(User u, LocalDate yesterday) {
//        this.yesterday = yesterday;
//        try {
//            //处理信息
//            //判断“当前训练周期开始日期”是否为空？
//            if (StringUtils.isEmpty(u.getTrainPeriodBeginTime())) {
//                //空，【说明是新用户，未曾完成过课程训练】
//                List<Date> ld;
//                try {
//                    //根据用户id获取完成课程训练的日期列表,升序【排第一的日期则是最早的】
//                    ld = trainCourseHeadInfoMapper.selectTrainDateListById(u.getId());
//                } catch (Exception e) {
//                    ld = new ArrayList<>();
//                }
//                //是否空的，
//                if (!ld.isEmpty()) {
//                    //不空,【说明有完成训练】
//                    this.noTrainBeginInUser(u, ld);
//                }
//                //空的，没有完成过课程训练，结束这一次循环
//            } else {
//                //不空
//                //根据用户id,获取最新的训练周期信息
//                UserTrainLevel t = userTrainLevelMapper.selectNewestByUserid(u.getId());
//                //最新的训练周期的结束日期
//                LocalDate trainPeriodEndTime = LocalDate.parse(t.getTrainPeriodEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                //判断昨天日期是否在该训练周期范围里？
//                if (!yesterday.isAfter(trainPeriodEndTime)) {
//                    //是，昨天日期在有记录的最新范围内
//                    this.haveTrainBeginInUserInPeriod(u, t);
//                } else {
//                    //不是，说明有可能已经宕机很久了，甚至一年以上啦。。。。，也有可能刚好在上周结束训练周期，这一这周进入新的训练周期
//                    //获取训练周期开始和结束日期
//                    List<TrainWeek> trainCycles = this.getTrainCycles(u, trainPeriodEndTime);
//                    if (trainCycles.size() > 1) {
//                        //说明已经宕机一年以上
//                        //todo
//                        //return ResultUtil.success("用户有训练周期开始时间，但是已经宕机一年以上了，目前不做处理");
//                        System.out.println(u.getId() + "用户有训练周期开始时间，但是已经宕机一年以上了，目前不做处理");
//                    } else {
//                        //进入下一训练周期
//                        this.intoNextTrainBegin(u, trainPeriodEndTime, t, trainCycles);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //日志打印
//            log.info("\n===============================job批处理等级信息【多线程并发运行】==================================\n"
//                    + "用户id：" + u.getId() + " 等级数据计算失败，原因：\n"
//                    + e.getMessage()
//                    + "\n"
//                    + "===============================job批处理等级信息【多线程并发运行】==================================");
//            //抛出异常
//            throw new JobException();
//        }
//    }
//
//    /**
//     * 场景一： 用户表 无 “当前训练周期开始日期”,且有课程训练数据
//     */
//    private void noTrainBeginInUser(User u, List<Date> ld) {
//        //不空,【说明有完成训练】
//        //将最早的元素日期转成 LocalDate
//        LocalDate minDate = ld.get(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        //“训练周期结束日期”为 从第一天 开始算 第 52*7  天的那一天
//        LocalDate trainPeriodEndTime = minDate.plusDays(52 * 7 - 1);
//        //判断完成课程训练的最早日期是不是昨天？
//        //Date转换成local后比较日期
//        if (minDate.isEqual(yesterday)) {
//            //是，【说明昨天是用户第一次完成课程训练的日期】
//            this.yesterdayHaveCourseAtFirst(u, minDate, trainPeriodEndTime, ld.size() + "");
//        } else {
//            //不是,【说明恰巧宕机了，这几天都没有处理等级】
//            //判断昨天日期是否在该训练周期范围里？
//            if (!yesterday.isAfter(trainPeriodEndTime)) {
//                //是
//                this.downtimeAtNoTrainBegin(u, minDate, trainPeriodEndTime);
//            } else {
//                //不是，已经宕机一年以上了
//                //不太可能发生，目前不做
//                //todo
//                //return ResultUtil.success("用户没有训练周期开始时间，但是在一年之前就已经有课程训练数据了，也就是说宕机一年以上，目前不做处理");
//                System.out.println(u.getId() + "用户没有训练周期开始时间，但是在一年之前就已经有课程训练数据了，也就是说宕机一年以上，目前不做处理");
//            }
//        }
//    }
//
//    /*
//     * 昨天是用户第一次完成课程训练的日期
//     */
//    //首次完成课程训练日期、训练周期结束日期、当前周完成训练的天数
//    private void yesterdayHaveCourseAtFirst(User u, LocalDate minDate, LocalDate trainPeriodEndTime, String trainDayCount) {
//        //"周结束日期"为 为 从第一天 开始算 第 7  天的那一天
//        LocalDate endDate = minDate.plusDays(7 - 1);
//        //“训练周期开始日期”为 第一天【即昨天】
//        u.setTrainPeriodBeginTime(minDate.toString());
//        //当前等级为新手
//        u.setTrainLevel("新手");
//        u.setLastModifiedDate(LocalDateTime.now());
//        u.setLastModifiedBy("等级更新程序");
//        //更新user表
//        userMapper.updateById(u);
//        //user_train_level表新增
//        //当前训练周期id
//        String userTrainLevelId = UUIDUtil.getShortUUID();
//        this.insertTrainLevel(u, userTrainLevelId, "新手", minDate.toString(), trainPeriodEndTime.toString());
//        //user_level_info表新增
//        this.insertUserLevelInfo(u, "1", minDate.toString(), endDate.toString(), "0", trainDayCount, "0", userTrainLevelId);
//    }
//
//    /*
//     * 恰巧宕机了，这几天都没有处理等级【宕机范围是第一次完成课程训练日期到昨天日期不超过一个训练周期的时间，即一年】
//     */
//    //首次完成课程训练日期、训练周期结束日期
//    private void downtimeAtNoTrainBegin(User u, LocalDate minDate, LocalDate trainPeriodEndTime) {
//        //“训练周期开始日期”为 第一天
//        u.setTrainPeriodBeginTime(minDate.toString());
//        //当前等级为新手
//        u.setTrainLevel("新手");
//        u.setLastModifiedDate(LocalDateTime.now());
//        u.setLastModifiedBy("等级更新程序");
//        //更新user表
//        userMapper.updateById(u);
//        //user_train_level表新增
//        //当前训练周期id
//        String userTrainLevelId = UUIDUtil.getShortUUID();
//        UserTrainLevel t = this.insertTrainLevel(u, userTrainLevelId, "新手", minDate.toString(), trainPeriodEndTime.toString());
//        //获取每一周的开始和结束日期
//        List<TrainWeek> lw = this.getTrainWeek(minDate);
//        //当前星星总数
//        int starCount = 0;
//        int dostarsCount = this.dostars(u, lw, starCount, userTrainLevelId);
//        //计算等级
//        if (starCount != dostarsCount) {
//            //星星数有改变
//            this.doLevel(u, dostarsCount, t, "新手");
//        }
//    }
//
//    /*
//    获取每一周的开始和结束日期
//     */
//    //起始日期
//    private List<TrainWeek> getTrainWeek(LocalDate minDate) {
//        List<TrainWeek> lw = new ArrayList<>();
//        //临时变量
//        LocalDate temp1, temp2;
//        int i = 0;
//        while (true) {
//            //周开始日期
//            temp1 = minDate.plusDays(i * 7);
//            i++;
//            //周结束日期
//            temp2 = minDate.plusDays(i * 7 - 1);
//            lw.add(new TrainWeek(temp1, temp2));
//            //如果昨天日期是否小于或者等于周结束日期
//            if (!yesterday.isAfter(temp2)) {
//                //结束循环
//                break;
//            }
//        }
//        return lw;
//    }
//
//    /*
//    计算星星
//     */
//    private int dostars(User u, List<TrainWeek> lw, int starCount, String userTrainLevelId) {
//        //这里是临时存储的临时变量
//        //当周星星情况
//        int starFlag = 0;
//        //需要存储的消耗的缓冲天数
//        int buffer = 0;
//        //需要存储的课程训练天数
//        int traindaycount;
//        //当前缓冲剩余天数【设为最大值，无消耗】
//        int surplusBuffer = 7 * 7;
//        //周序号
//        int sort = 0;
//        //遍历每一周
//        for (TrainWeek w : lw) {
//            sort++;
//            //根据周日期范围获取完成的的课程训练天数
//            traindaycount = trainCourseHeadInfoMapper.selectCountByDateAndId(u.getId(), w.getBeginDate(), w.getEndDate());
//            //判断昨天日期 是否 不在该周内 或者 刚好是周结束日期
//            if (!yesterday.isBefore(w.getEndDate())) {
//                //是,如果不满勤 会 扣一颗星星
//                if (traindaycount > 3) {
//                    //这一周满勤4天，可以涨星星
//                    starCount++;
//                    starFlag++;
//                } else {
//                    //不满勤4天，
//                    //计算需要消耗的缓冲天数
//                    int useBuffer = 7 - traindaycount;
//                    //计算缓冲剩余天数
//                    int temp = surplusBuffer - useBuffer;
//                    if (temp >= 0) {
//                        //缓冲天速足够抵消，不扣星星
//                        buffer = useBuffer;
//                        surplusBuffer = temp;
//                    } else {
//                        //缓冲天数不足
//                        if (starCount > 0) {
//                            //扣星星
//                            starCount--;
//                            starFlag--;
//                        }
//                    }
//                }
//            } else {
//                //否,如果不满勤 不会 扣一颗星星
//                if (traindaycount > 3) {
//                    //这一周满勤4天，可以涨星星
//                    starCount++;
//                    starFlag++;
//                }
//                //不满勤4天，不扣星星，也不需要消耗缓冲天数
//            }
//            //user_level_info表新增
//            this.insertUserLevelInfo(u, sort + "", w.getBeginDate().toString(), w.getEndDate().toString(), starFlag + "",
//                    traindaycount + "", buffer + "", userTrainLevelId);
//            //初始化变量
//            //当前需要消耗的缓冲天数
//            buffer = 0;
//            //当周星星情况
//            starFlag = 0;
//        }
//        return starCount;
//    }
//
//    /*
//    UserLevelInfo表新增数据
//     */
//    private UserLevelInfo insertUserLevelInfo(User u, String sort, String beginDate, String endDate, String starFlag, String traindaycount,
//                                              String bufferDayCount, String userTrainLevelId) {
//        UserLevelInfo info = new UserLevelInfo();
//        //user_level_info表新增
//        info.setId(UUIDUtil.getShortUUID());
//        info.setUserId(u.getId());
//        //周序号为1
//        info.setSort(sort);
//        //周开始日期
//        info.setBeginDate(beginDate);
//        //周结束日期
//        info.setEndDate(endDate);
//        //当周星星情况
//        info.setStarFlag(starFlag);
//        //本周训练天数
//        info.setTrainDayCount(traindaycount);
//        //缓冲天数
//        info.setBufferDayCount(bufferDayCount);
//        //当前训练周期id
//        info.setUserTrainLevelId(userTrainLevelId);
//        info.setCreateDate(LocalDateTime.now());
//        info.setLastModifiedDate(LocalDateTime.now());
//        info.setDeleted("0");
//        baseMapper.insert(info);
//        return info;
//    }
//
//    /*
//    星星数有改变，计算等级
//     */
//    //当前星星总数、训练等级历史记录对象、原等级名称
//    private void doLevel(User u, int starCount, UserTrainLevel t, String LevelWord) {
//        //计算等级
//        String level = LevelUtil.getLevel(starCount);
//        if (!level.equals(LevelWord)) {
//            u.setTrainLevel(level);
//            u.setLastModifiedDate(LocalDateTime.now());
//            userMapper.updateById(u);
//            t.setTrainLevel(level);
//            t.setLastModifiedDate(LocalDateTime.now());
//            userTrainLevelMapper.updateById(t);
//        }
//
//    }
//
//    /*
//  星星数有改变，计算等级, -----只需要记录到历史等级信息表
//   */
//    //当前星星总数、训练等级历史记录对象、原等级名称
//    private String doLevel2(int starCount, UserTrainLevel t, String LevelWord) {
//        //计算等级
//        String level = LevelUtil.getLevel(starCount);
//        if (!level.equals(LevelWord)) {
//            //只需要记录到历史等级信息表
//            t.setTrainLevel(level);
//            t.setLastModifiedDate(LocalDateTime.now());
//            userTrainLevelMapper.updateById(t);
//        }
//        return level;
//    }
//
//    /**
//     * 场景二： 用户表 有 “当前训练周期开始日期” -- 昨天日期在有记录的最新范围内
//     */
//    private void haveTrainBeginInUserInPeriod(User u, UserTrainLevel t) {
//        //根据用户id获取最新的周日期信息【user_train_level表获取】
//        UserLevelInfo info = this.getUserLevelInfo(u, t);
//        //将周开始和结束日期字符串转成日期格式
//        TrainWeek w = this.getTrainWeek(info.getBeginDate(), info.getEndDate());
//        //根据周日期范围获取完成的的课程训练天数【即获取有记录的最新周里的完成课程训练的天数】
//        int traindaycount = trainCourseHeadInfoMapper.selectCountByDateAndId(u.getId(), w.getBeginDate(), w.getEndDate());
//        //
//        //判断昨天日期是否在该周日期中
//        if (!yesterday.isBefore(w.getBeginDate()) && !yesterday.isAfter(w.getEndDate())) {
//            //在
//            this.yesterdayInWeek(u, w, info, traindaycount, t);
//        } else {
//            //不在
//            //判断该周的最新修改日期是否等于昨天日期？
//            if (info.getLastModifiedDate().toLocalDate().isEqual(yesterday)) {
//                //是，进入下一周啦,【即昨天是新周开始的第一天】
//                this.intoNextWeek(u, info);
//            } else {
//                //宕机,宕机结束日期【即昨天日期】仍在最新训练周期范围内，不进入下一训练周期
//                this.downtimeBeforeTrainEnd(u, info, w, t);
//            }
//        }
//    }
//
//    /*
//     根据用户id获取最新的周日期信息【user_train_level表获取】
//     */
//    private UserLevelInfo getUserLevelInfo(User u, UserTrainLevel t) {
//        UserLevelInfo info = baseMapper.selectNewestById(u.getId());
//        //是否为空
//        if (info == null) {
//            //空的，数据库被改动了,全部被删除了
//            //直接数据初始化即可，给user_level_info表新增一条数据，训练开始周期日期为该周日期，后面代码会认为宕机了，而重新计算该用户数据，还需要把等级数据改成新手
//            //周开始日期
//            LocalDate beginDate = LocalDate.parse(u.getTrainPeriodBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            info = this.insertUserLevelInfo(u, "1", u.getTrainPeriodBeginTime(), beginDate.plusDays(7 - 1).toString(), "0", "0", "0", t.getId());
//            //修改等级信息
//            u.setTrainLevel("新手");
//            u.setLastModifiedDate(LocalDateTime.now());
//            userMapper.updateById(u);
//            t.setTrainLevel("新手");
//            t.setLastModifiedDate(LocalDateTime.now());
//            userTrainLevelMapper.updateById(t);
//        }
//        return info;
//    }
//
//    /*
//     将周开始和结束日期字符串转成日期格式
//     */
//    private TrainWeek getTrainWeek(String beginDate, String endDate) {
//        return new TrainWeek(LocalDate.parse(beginDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//    }
//
//    /*
//    昨天日期在该周日期中
//     */
//    private void yesterdayInWeek(User u, TrainWeek w, UserLevelInfo info, int traindaycount, UserTrainLevel t) {
//        //获取周开始的第4天日期
//        LocalDate no4Day = w.getBeginDate().plusDays(4 - 1);
//        //判断昨天日期是否是该周的前3天任意一天？
//        if (yesterday.isBefore(no4Day)) {
//            //是
//            //【user_level_info表更新】
//            info.setTrainDayCount(traindaycount + "");
//            info.setLastModifiedDate(LocalDateTime.now());
//        } else {
//            //不是
//            //根据用户id和训练周期id获取当前星星总数和已消耗缓冲总天数[包括当前周]
//            StarCountAndBufferDayCountDto sab = baseMapper.selectStarCountBySome(u.getId(), t.getId());
//            //除去当前周的星星总数[即训练开始日期到上一周的星星总数]
//            int starCount = sab.getStarCount() - Integer.parseInt(info.getStarFlag());
//            //除去当前周的剩余缓冲天数[即训练开始日期到上一周的剩余缓冲天数]
//            int surplusBuffer = 7 * 7 - sab.getBufferDayCount() + Integer.parseInt(info.getBufferDayCount());
//            //判断 课程训练天数 是否大于3?
//            if (traindaycount > 3) {
//                //是，涨星星
//                //【user_level_info表更新】
//                info.setStarFlag("1");
//                info.setTrainDayCount(traindaycount + "");
//                info.setLastModifiedDate(LocalDateTime.now());
//                starCount++;
//            } else {
//                //不是，
//                //判断昨天日期是否是周结束日期？
//                if (yesterday.isEqual(w.getEndDate())) {
//                    //是
//                    //判断剩余缓冲时间能否抵消
//                    if (surplusBuffer - (7 - traindaycount) >= 0) {
//                        //能
//                        info.setBufferDayCount((7 - traindaycount) + "");
//                    } else {
//                        //不能
//                        //判断星星总数是否大于0
//                        if (starCount > 0) {
//                            info.setStarFlag("-1");
//                            starCount--;
//                        }
//                    }
//                }
//                //不是，不操作星星
//                info.setTrainDayCount(traindaycount + "");
//                info.setLastModifiedDate(LocalDateTime.now());
//            }
//            if (starCount != sab.getStarCount()) {
//                //星星数有改变
//                this.doLevel(u, starCount, t, u.getTrainLevel());
//            }
//        }
//        //更新
//        baseMapper.updateById(info);
//    }
//
//    /*
//    进入下一周啦,【即昨天是新周开始的第一天】
//     */
//    private void intoNextWeek(User u, UserLevelInfo info) {
//        //根据周日期范围获取完成的的课程训练天数
//        int traindaycount2 = trainCourseHeadInfoMapper.selectCountByDateAndId(u.getId(), yesterday, yesterday.plusDays(7 - 1));
//        //存储新的周信息
//        this.insertUserLevelInfo(u, (Integer.parseInt(info.getSort()) + 1) + "", yesterday.toString(), yesterday.plusDays(7 - 1).toString(),
//                "0", traindaycount2 + "", "0", info.getUserTrainLevelId());
//    }
//
//    /*
//    宕机,宕机结束日期【即昨天日期】仍在最新训练周期范围内，不进入下一训练周期
//     */
//    private void downtimeBeforeTrainEnd(User u, UserLevelInfo info, TrainWeek w, UserTrainLevel t) {
//        //获取需要计算的开始日期、星星、缓冲天数据
//        WeekData weekData = this.getWeekData(info, w);
//        // 获取每一周需要计算的宕机日期
//        List<TrainWeek> lw = this.getTrainWeek(weekData.getDoDate());
//        //根据用户id和训练周期id获取当前星星总数和已消耗缓冲总天数
//        StarCountAndBufferDayCountDto sab = baseMapper.selectStarCountBySome(u.getId(), t.getId());
//        //星星总数【如果是重新计算该最新周，则需要减去该周的星星状态】
//        int starCount = sab.getStarCount() - weekData.getTempStar();
//        //剩余缓冲天数【如果是重新计算该最新周，则需要加上该周的缓冲天数】
//        int surplusBuffer = 7 * 7 - sab.getBufferDayCount() + weekData.getTempBuffer();
//        starCount = this.dostars2(u, lw, starCount, surplusBuffer, info, t);
//        if (starCount != sab.getStarCount()) {
//            //星星数有改变
//            this.doLevel(u, starCount, t, u.getTrainLevel());
//        }
//
//    }
//
//    /*
//    获取需要计算的开始日期、星星、缓冲天数据
//     */
//    private WeekData getWeekData(UserLevelInfo info, TrainWeek w) {
//        WeekData d = new WeekData();
//        //判断有记录的最新周的最新修改日期的前一天是否等于周结束日期？
//        if (info.getLastModifiedDate().toLocalDate().minusDays(1).isEqual(w.getEndDate())) {
//            //是,【这一周刚好在计算结束后才宕机，真巧】
//            d.setDoDate(w.getEndDate().plusDays(1));
//            d.setTempStar(0);
//            d.setTempBuffer(0);
//        } else {
//            //不是,【这一周未计算完就宕机了】，需要重新计算该最新周
//            d.setDoDate(w.getBeginDate());
//            //获取这一周的信息
//            d.setTempStar(Integer.parseInt(info.getStarFlag()));
//            d.setTempBuffer(Integer.parseInt(info.getBufferDayCount()));
//        }
//        return d;
//    }
//
//    /*
//  计算星星
//   */
//    private int dostars2(User u, List<TrainWeek> lw, int starCount, int surplusBuffer, UserLevelInfo info, UserTrainLevel t) {
//        //需要存储的星星状态
//        int starFlag = 0;
//        //需要存储的消耗的缓冲天数
//        int buffer = 0;
//        //需要存储的课程训练天数
//        int mtraindaycount;
//        //剩余缓冲天数【如果是重新计算该最新周，则需要加上该周的缓冲天数】
////        int surplusBuffer = 7 * 7 - sab.getBufferDayCount() + weekData.getTempBuffer();
//        //是否需要检查周数据是否存在
//        boolean isCheckWeek = true;
//        //当前周序号
//        int sort = 0;
//        //是否新增数据
//        boolean isAdd = false;
//        //如果不需要新增，记录该数据id
//        String infoId = "";
//        for (TrainWeek w : lw) {
//            sort++;
//            //根据周日期范围获取完成的的课程训练天数
//            mtraindaycount = trainCourseHeadInfoMapper.selectCountByDateAndId(u.getId(), w.getBeginDate(), w.getEndDate());
//            //判断昨天日期 是否 不在该周内 或者 刚好是周结束日期
//            if (!yesterday.isBefore(w.getEndDate())) {
//                //是,如果不满勤 会 扣一颗星星
//                if (mtraindaycount > 3) {
//                    //这一周满勤4天，可以涨星星
//                    starCount++;
//                    starFlag++;
//                } else {
//                    //不满勤4天，
//                    //计算需要消耗的缓冲天数
//                    int useBuffer = 7 - mtraindaycount;
//                    //计算缓冲剩余天数
//                    int temp = surplusBuffer - useBuffer;
//                    if (temp >= 0) {
//                        //缓冲天数足够抵消，不扣星星
//                        buffer = useBuffer;
//                        surplusBuffer = temp;
//                    } else {
//                        //缓冲天数不足
//                        if (starCount > 0) {
//                            //扣星星
//                            starCount--;
//                            starFlag--;
//                        }
//                    }
//                }
//            } else {
//                //否,如果不满勤 不会 扣一颗星星
//                if (mtraindaycount > 3) {
//                    //这一周满勤4天，可以涨星星
//                    starCount++;
//                    starFlag++;
//                }
//                //不满勤4天，不扣星星，也不需要消耗缓冲天数
//            }
//            //只需要检查一次【首次】
//            if (isCheckWeek) {
//                //判断该周的数据是否存在
//                QueryWrapper<UserLevelInfo> mqw = new QueryWrapper<>();
//                mqw.eq("userId", u.getId());
//                mqw.eq("deleted", "0");
//                mqw.eq("beginDate", w.getBeginDate().toString());
//                mqw.eq("userTrainLevelId", t.getId());
//                UserLevelInfo info1 = baseMapper.selectOne(mqw);
//                if (info1 == null) {
//                    //设为新增
//                    isAdd = true;
//                    //获取已存在的最新周序号 +1
//                    sort = Integer.parseInt(info.getSort()) + 1;
//                } else {
//                    //记录这一条数据id,用于更新
//                    infoId = info1.getId();
//                    sort = Integer.parseInt(info1.getSort());
//                }
//                isCheckWeek = false;
//            }
//
//            UserLevelInfo minfo = new UserLevelInfo();
//            //当周星星情况
//            minfo.setStarFlag(starFlag + "");
//            //本周训练天数
//            minfo.setTrainDayCount(mtraindaycount + "");
//            //缓冲天数
//            minfo.setBufferDayCount(buffer + "");
//            minfo.setLastModifiedDate(LocalDateTime.now());
//            if (isAdd) {
//                //新增
//                minfo.setId(UUIDUtil.getShortUUID());
//                minfo.setUserId(u.getId());
//                //周序号为1
//                minfo.setSort(sort + "");
//                //周开始日期
//                minfo.setBeginDate(w.getBeginDate().toString());
//                //周结束日期
//                minfo.setEndDate(w.getEndDate().toString());
//                minfo.setCreateDate(LocalDateTime.now());
//                //当前训练周期id
//                minfo.setUserTrainLevelId(t.getId());
//                minfo.setDeleted("0");
//                baseMapper.insert(minfo);
//            } else {
//                //更新
//                minfo.setId(infoId);
//                baseMapper.updateById(minfo);
//                //下一周将需要新增
//                //设为新增
//                isAdd = true;
//            }
//            //初始化变量
//            //当前需要消耗的缓冲天数
//            buffer = 0;
//            //当周星星情况
//            starFlag = 0;
//        }
//        return starCount;
//
//    }
//
//    /*
//    //获取训练周期开始和结束日期
//     */
//    private List<TrainWeek> getTrainCycles(User u, LocalDate trainPeriodEndTime) {
//        List<TrainWeek> trainCycles = new ArrayList<>();
//        int i = 0;
//        while (true) {
//            //临时变量
//            LocalDate temp1, temp2;
//            //周开始日期
//            temp1 = trainPeriodEndTime.plusDays(i * 7 * 52 + 1);
//            i++;
//            //周结束日期
//            temp2 = temp1.plusDays(7 * 52 - 1);
//            trainCycles.add(new TrainWeek(temp1, temp2));
//            //如果昨天日期在小于或者等于周结束日期
//            if (!yesterday.isAfter(temp2)) {
//                //结束循环
//                break;
//            }
//        }
//        return trainCycles;
//    }
//
//
//    /**
//     * 场景三，进入下一个训练周期
//     */
//    private void intoNextTrainBegin(User u, LocalDate trainPeriodEndTime, UserTrainLevel t, List<TrainWeek> trainCycles) {
//        //检查上一训练周期是否有宕机
//        //获取有记录的最新周数据【user_train_level表获取】
//        UserLevelInfo info = baseMapper.selectNewestById(u.getId());
//        //判断最新周数据的最后修改日期的前一天是否等于上一训练周期结束日期
//        if (!info.getLastModifiedDate().toLocalDate().minusDays(1).isEqual(trainPeriodEndTime)) {
//            //不是，则说明上一训练周期还没有计算完成就宕机了
//            //获取需要计算的开始日期、星星、缓冲天数据
//            WeekData weekData = this.getWeekData2(info);
//            // 获取每一周需要计算的宕机时间
//            List<TrainWeek> lw = this.getTrainWeek2(weekData.getDoDate(), trainPeriodEndTime);
//            //根据用户id和训练周期id获取当前星星总数和已消耗缓冲总天数
//            StarCountAndBufferDayCountDto sab = baseMapper.selectStarCountBySome(u.getId(), t.getId());
//            //星星总数【如果是重新计算该最新周，则需要减去该周的星星状态】
//            int starCount = sab.getStarCount() - weekData.getTempStar();
//            //剩余缓冲天数【如果是重新计算该最新周，则需要加上该周的缓冲天数】
//            int surplusBuffer = 7 * 7 - sab.getBufferDayCount() + weekData.getTempBuffer();
//            //计算上一训练周期的每一周星星数据
//            this.doLastTrainCycle(u, t, info, lw, sab.getStarCount(), starCount, surplusBuffer);
//        }
//        //不论是不是都需要新增新的训练周期数据，下面的是公共执行的
//        //新的训练周期开始日期
//        LocalDate trainCycleBegin = trainCycles.get(0).getBeginDate();
//        //新的训练周期结束日期
//        LocalDate trainCycleEnd = trainCycles.get(0).getEndDate();
//        //新增新的训练周期
//        //user_train_level表新增
//        //当前新训练周期id
//        String userTrainLevelId = UUIDUtil.getShortUUID();
//        UserTrainLevel t2 = this.insertTrainLevel(u, userTrainLevelId, "新手", trainCycleBegin.toString(), trainCycleEnd.toString());
//        //计算新训练周期到昨天的每周数据【有可能不仅进入了新的训练周期，还宕机了几个星期，因此，这里可以统一计算】
//        // 获取每一周需要计算的宕机时间
//        List<TrainWeek> lw = this.getTrainWeek(trainCycleBegin);
//        //当前星星总数
//        int starCount = 0;
//        int dostarsCount = this.dostars(u, lw, starCount, userTrainLevelId);
//        //初始化用户表数据【即等级和训练周期开始日期】
//        u.setTrainLevel("新手");
//        //计算等级
//        if (starCount != dostarsCount) {
//            //星星数有改变,计算等级
//            String level = this.doLevel2(dostarsCount, t2, "新手");
//            if (!level.equals("新手")) {
//                u.setTrainLevel(level);
//            }
//        }
//        //更新用户等级信息
//        u.setLastModifiedDate(LocalDateTime.now());
//        u.setTrainPeriodBeginTime(trainCycleBegin.toString());
//        userMapper.updateById(u);
//    }
//
//    /*
//   获取需要计算的开始日期、星星、缓冲天数据
//    */
//    private WeekData getWeekData2(UserLevelInfo info) {
//        WeekData d = new WeekData();
//        //判断有记录的最新周是否已经计算完成，即判断该周最新修改日期的前一天是否等于周结束日期
//        if (info.getLastModifiedDate().toLocalDate().minusDays(1).isEqual(LocalDate.parse(info.getBeginDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
//            //是，说明该周计算完了才宕机
//            d.setDoDate(info.getLastModifiedDate().toLocalDate());
//            d.setTempStar(0);
//            d.setTempBuffer(0);
//        } else {
//            //不是，说明该周还没有计算完就已经宕机了
//            d.setDoDate(LocalDate.parse(info.getBeginDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            //获取这一周的信息
//            d.setTempStar(Integer.parseInt(info.getStarFlag()));
//            d.setTempBuffer(Integer.parseInt(info.getBufferDayCount()));
//        }
//        return d;
//    }
//
//    /*
//    获取每一周的开始和结束日期
//     */
//    //起始日期
//    private List<TrainWeek> getTrainWeek2(LocalDate doDate, LocalDate trainPeriodEndTime) {
//        List<TrainWeek> lw = new ArrayList<>();
//        //临时变量
//        LocalDate temp1, temp2;
//        int i2 = 0;
//        while (true) {
//            //周开始日期
//            temp1 = doDate.plusDays(i2 * 7);
//            i2++;
//            //周结束日期
//            temp2 = doDate.plusDays(i2 * 7 - 1);
//            lw.add(new TrainWeek(temp1, temp2));
//            //如果上一训练周期结束日期小于或者等于周结束日期
//            if (!trainPeriodEndTime.isAfter(temp2)) {
//                //结束循环
//                break;
//            }
//        }
//        return lw;
//    }
//
//    /*
//    计算上一训练周期的每一周星星数据
//     */
//    private void doLastTrainCycle(User u, UserTrainLevel t, UserLevelInfo info, List<TrainWeek> lw, int starCountTemp,
//                                  int starCount, int surplusBuffer) {
//        //需要存储的星星状态
//        int starFlag = 0;
//        //需要存储的消耗的缓冲天数
//        int buffer = 0;
//        //需要存储的课程训练天数
//        int mtraindaycount;
//        //是否需要检查周数据是否存在
//        boolean isCheckWeek = true;
//        //当前周序号
//        int sort = 0;
//        //是否新增数据
//        boolean isAdd = false;
//        //如果不需要新增，记录该周数据id
//        String infoId = "";
//        for (TrainWeek mw : lw) {
//            sort++;
//            //根据周日期范围获取完成的的课程训练天数
//            mtraindaycount = trainCourseHeadInfoMapper.selectCountByDateAndId(u.getId(), mw.getBeginDate(), mw.getEndDate());
//            //不满勤 会 扣一颗星星
//            if (mtraindaycount > 3) {
//                //这一周满勤4天，可以涨星星
//                starCount++;
//                starFlag++;
//            } else {
//                //不满勤4天，
//                //计算需要消耗的缓冲天数
//                int useBuffer = 7 - mtraindaycount;
//                if ((surplusBuffer - useBuffer) >= 0) {
//                    //缓冲天数足够抵消，不扣星星
//                    //计算缓冲剩余天数
//                    surplusBuffer -= useBuffer;
//                    buffer = useBuffer;
//                } else {
//                    //缓冲天数不足
//                    if (starCount > 0) {
//                        //扣星星
//                        starCount--;
//                        starFlag--;
//                    }
//                }
//            }
//            //只需要检查一次【首次】
//            if (isCheckWeek) {
//                //判断该周的数据是否存在
//                QueryWrapper<UserLevelInfo> mqw = new QueryWrapper<>();
//                mqw.eq("userId", u.getId());
//                mqw.eq("deleted", "0");
//                mqw.eq("beginDate", mw.getBeginDate().toString());
//                mqw.eq("userTrainLevelId", t.getId());
//                UserLevelInfo info1 = baseMapper.selectOne(mqw);
//                if (info1 == null) {
//                    //设为新增
//                    isAdd = true;
//                    //获取已存在的最新周序号 +1
//                    sort = Integer.parseInt(info.getSort()) + 1;
//                } else {
//                    //记录这一条数据id,用于更新
//                    infoId = info1.getId();
//                    sort = Integer.parseInt(info1.getSort());
//                }
//                isCheckWeek = false;
//            }
//
//            UserLevelInfo minfo = new UserLevelInfo();
//            //当周星星情况
//            minfo.setStarFlag(starFlag + "");
//            //本周训练天数
//            minfo.setTrainDayCount(mtraindaycount + "");
//            //缓冲天数
//            minfo.setBufferDayCount(buffer + "");
//            minfo.setLastModifiedDate(LocalDateTime.now());
//            minfo.setUserId(u.getId());
//            //周序号为1
//            minfo.setSort(sort + "");
//            //周开始日期
//            minfo.setBeginDate(mw.getBeginDate().toString());
//            //周结束日期
//            minfo.setEndDate(mw.getEndDate().toString());
//            //当前训练周期id
//            minfo.setUserTrainLevelId(t.getId());
//            minfo.setDeleted("0");
//            if (isAdd) {
//                //新增
//                minfo.setCreateDate(LocalDateTime.now());
//                minfo.setId(UUIDUtil.getShortUUID());
//                baseMapper.insert(minfo);
//            } else {
//                //更新
//                minfo.setCreateDate(info.getCreateDate());
//                minfo.setId(infoId);
//                baseMapper.updateById(minfo);
//                //下一周将需要新增
//                //设为新增
//                isAdd = true;
//            }
//            //初始化变量
//            //当前需要消耗的缓冲天数
//            buffer = 0;
//            //当周星星情况
//            starFlag = 0;
//        }
//        if (starCount != starCountTemp) {
//            //星星数有改变,计算等级
//            this.doLevel2(starCount, t, u.getTrainLevel());
//        }
//    }
//
//    /*
//    新增训练周期历史记录
//     */
//    private UserTrainLevel insertTrainLevel(User u, String userTrainLevelId, String trainLevel, String trainCycleBegin,
//                                            String trainCycleEnd) {
//        //user_train_level表新增
//        UserTrainLevel t2 = new UserTrainLevel();
//        t2.setId(userTrainLevelId);
//        t2.setUserId(u.getId());
//        t2.setTrainLevel(trainLevel);
//        t2.setTrainPeriodBeginTime(trainCycleBegin);
//        t2.setTrainPeriodEndTime(trainCycleEnd);
//        t2.setCreateDate(LocalDateTime.now());
//        t2.setCreateBy("等级更新程序");
//        t2.setLastModifiedDate(LocalDateTime.now());
//        t2.setLastModifiedBy("等级更新程序");
//        t2.setDeleted("0");
//        userTrainLevelMapper.insert(t2);
//        return t2;
//    }
//
//}
