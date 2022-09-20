package com.knd.manage.homePage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.basedata.entity.BaseDifficulty;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.mapper.BaseActionMapper;
import com.knd.manage.basedata.mapper.BaseDifficultyMapper;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.course.entity.*;
import com.knd.manage.course.mapper.*;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.manage.equip.mapper.EquipmentInfoMapper;
import com.knd.manage.equip.mapper.EquipmentReportInfoMapper;
import com.knd.manage.homePage.dto.*;
import com.knd.manage.homePage.entity.ProgramHeadEntity;
import com.knd.manage.homePage.mapper.AmapDataMapper;
import com.knd.manage.homePage.mapper.ProgramHeadMapper;
import com.knd.manage.homePage.service.BuyAnalysisService;
import com.knd.manage.mall.mapper.TbOrderMapper;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class BuyAnalysisServiceImpl implements BuyAnalysisService {
    private final AmapDataMapper amapDataMapper;
    private final BaseDifficultyMapper baseDifficultyMapper;
    private final ProgramHeadMapper programHeadMapper;
    private final UserOrderRecordMapper userOrderRecordMapper;
    private final TrainCourseHeadInfoMapper trainCourseHeadInfoMapper;
    private final TrainFreeHeadMapper trainFreeHeadMapper;
    private final CourseHeadMapper courseHeadMapper;
    private final UserMapper userMapper;
    private final UserCoachMapper userCoachMapper;
    private final EquipmentInfoMapper equipmentInfoMapper;
    private final TrainProgramMapper trainProgramMapper;
    private final SeriesCourseHeadMapper seriesCourseHeadMapper;
    private final BaseActionMapper baseActionMapper;
    private final EquipmentReportInfoMapper equipmentReportInfoMapper;
    private final TbOrderMapper tbOrderMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result queryDiffCourseNum() {
        List<DiffCourseNumDto> courseNum = courseHeadMapper.getDiffCourseNum();
        return ResultUtil.success(courseNum);
    }

    @Override
    public Result queryTypeCourseNum() {
        List<DiffCourseNumDto> courseNum = courseHeadMapper.getTypeCourseNum();
        return ResultUtil.success(courseNum);
    }

    @Override
    public Result queryCourseByTypeNum(String beginDate,String endDate) {
        //数据格式化
        DecimalFormat df = new DecimalFormat("0.00");
        //时间格式化
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(beginDate, fmt);
        LocalDate date2 = LocalDate.parse(endDate, fmt);
        long days = date2.toEpochDay()-date1.toEpochDay()+1;
        //自由训练数量
        List<Map<String, Object>> freeTrainingTotal = baseActionMapper.queryFreeTrainingTotal((int)days,beginDate,endDate);
        for(int i=0;i<freeTrainingTotal.size();i++) {
            int count1 = ((BigDecimal)freeTrainingTotal.get(i).get("count")).intValue();
            if(count1== 0 && i>0){
                freeTrainingTotal.get(i).put("count",freeTrainingTotal.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)freeTrainingTotal.get(i-1).get("count")).intValue()
                        :freeTrainingTotal.get(i-1).get("count"));
            }
        }
        //补全数据
        if (freeTrainingTotal.size()<days){
            for (int i = freeTrainingTotal.size(); i < days; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",date1.plusDays(i).format(fmt));
                map.put("count",freeTrainingTotal.get(freeTrainingTotal.size()-1).get("count"));
                freeTrainingTotal.add(map);
            }
        }

        int freeTrainingTotal0 = freeTrainingTotal.get(0).get("count") instanceof  BigDecimal ? ((BigDecimal)freeTrainingTotal.get(0).get("count")).intValue() : (int)freeTrainingTotal.get(0).get("count");
        int freeTrainingTotal1 = freeTrainingTotal.get(freeTrainingTotal.size()-1).get("count") instanceof  BigDecimal ? (int)freeTrainingTotal.get(freeTrainingTotal.size()-1).get("count") : (int)freeTrainingTotal.get(freeTrainingTotal.size()-1).get("count");

        //系列课程数量
        List<Map<String, Object>> seriesCourseTotal = seriesCourseHeadMapper.querySeriesCourseTotal((int)days,beginDate,endDate);
        for(int i=0;i<seriesCourseTotal.size();i++) {
            int count1 = ((BigDecimal)seriesCourseTotal.get(i).get("count")).intValue();
            if(count1== 0 && i>0){
                seriesCourseTotal.get(i).put("count",seriesCourseTotal.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)seriesCourseTotal.get(i-1).get("count")).intValue()
                        :seriesCourseTotal.get(i-1).get("count"));
            }
        }
        //补全数据
        if (seriesCourseTotal.size()<days){
            for (int i = seriesCourseTotal.size(); i < days; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",date1.plusDays(i).format(fmt));
                map.put("count",seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count"));
                seriesCourseTotal.add(map);
            }
        }

        int seriesCourseTotal0 = seriesCourseTotal.get(0).get("count") instanceof BigDecimal ? ((BigDecimal)seriesCourseTotal.get(0).get("count")).intValue() : (int) seriesCourseTotal.get(0).get("count");
        int seriesCourseTotal1 = seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count") instanceof BigDecimal ? ((BigDecimal)seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count")).intValue() : (int)seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count");


        //课程数量
        List<Map<String, Object>> courseTotal = courseHeadMapper.queryCourseTotal((int)days,beginDate,endDate);
        for(int i=0;i<courseTotal.size();i++) {
            int count1 = ((BigDecimal)courseTotal.get(i).get("count")).intValue();
            if(count1== 0 && i>0){
                courseTotal.get(i).put("count",courseTotal.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)courseTotal.get(i-1).get("count")).intValue()
                        :courseTotal.get(i-1).get("count"));
            }
        }
        //补全数据
        if (courseTotal.size()<days){
            for (int i = courseTotal.size(); i < days; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",date1.plusDays(i).format(fmt));
                map.put("count",courseTotal.get(courseTotal.size()-1).get("count"));
                courseTotal.add(map);
            }
        }
        int courseTotal0 = courseTotal.get(0).get("count") instanceof BigDecimal ? ((BigDecimal)courseTotal.get(0).get("count")).intValue() : (int)courseTotal.get(0).get("count");
        int courseTotal1 = courseTotal.get(courseTotal.size()-1).get("count") instanceof BigDecimal ? ((BigDecimal)courseTotal.get(courseTotal.size()-1).get("count")).intValue() : (int)courseTotal.get(courseTotal.size()-1).get("count");

        //计划数量
        List<Map<String, Object>> planTotal = trainProgramMapper.queryPlanTotal((int)days,beginDate,endDate);
        for(int i=0;i<planTotal.size();i++) {
            int count1 = ((BigDecimal)planTotal.get(i).get("count")).intValue();
            if(count1== 0 && i>0){
                planTotal.get(i).put("count",planTotal.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)planTotal.get(i-1).get("count")).intValue()
                        :planTotal.get(i-1).get("count"));
            }
        }
        //补全数据
        if (planTotal.size()<days){
            for (int i = planTotal.size(); i < days; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",date1.plusDays(i).format(fmt));
                map.put("count",planTotal.get(planTotal.size()-1).get("count"));
                planTotal.add(map);
            }
        }
        int planTotal0 = planTotal.get(0).get("count") instanceof BigDecimal ? ((BigDecimal)planTotal.get(0).get("count")).intValue() : (int) planTotal.get(0).get("count");
        int planTotal1 = planTotal.get(planTotal.size()-1).get("count") instanceof BigDecimal ? ((BigDecimal)planTotal.get(planTotal.size()-1).get("count")).intValue() : (int) planTotal.get(planTotal.size()-1).get("count");

        CourseByTypeNumDto dto = new CourseByTypeNumDto();
        dto.setFreeTrainingTotal(freeTrainingTotal);
        dto.setFreeTrainingTotalGrowth(freeTrainingTotal0 == 0 ? "0" : df.format((float)(freeTrainingTotal1-freeTrainingTotal0)/freeTrainingTotal0));
        dto.setSeriesCourseTotal(seriesCourseTotal);
        dto.setSeriesCourseTotalGrowth(seriesCourseTotal0 == 0 ? "0" : df.format((float)(seriesCourseTotal1-seriesCourseTotal0)/seriesCourseTotal0));
        dto.setCourseTotal(courseTotal);
        dto.setCourseTotalGrowth(courseTotal0 == 0 ? "0" : df.format((float)(courseTotal1-courseTotal0)/courseTotal0));
        dto.setPlanTotal(planTotal);
        dto.setPlanTotalGrowth(planTotal0 == 0 ? "0" : df.format((float)(planTotal1-planTotal0)/planTotal0));
        return ResultUtil.success(dto);
    }

    @Override
    public Result queryCourseByTargetNum(String beginDate, String endDate) {
        //数据格式化
        DecimalFormat df = new DecimalFormat("0");
        //时间格式化
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(beginDate, fmt);
        LocalDate date2 = LocalDate.parse(endDate, fmt);
        long days = date2.toEpochDay()-date1.toEpochDay()+1;

        //减脂课程每日数量
        List<Map<String, Object>> fatReductionTotal = courseHeadMapper.queryCourseByTarget((int)days,beginDate,endDate,"减脂");

        //塑形课程数量
        List<Map<String, Object>> shapingTotal = courseHeadMapper.queryCourseByTarget((int)days,beginDate,endDate,"塑形");

        //增肌课程数量
        List<Map<String, Object>> buildMusclesTotal = courseHeadMapper.queryCourseByTarget((int)days,beginDate,endDate,"增肌");

        //系列课程数量
        List<Map<String, Object>> seriesCourseTotal = seriesCourseHeadMapper.querySeriesCourseTotal((int)days,beginDate,endDate);
        for(int i=0;i<seriesCourseTotal.size();i++) {
            int count1 = ((BigDecimal)seriesCourseTotal.get(i).get("count")).intValue();
            if(count1== 0 && i>0){
                seriesCourseTotal.get(i).put("count",seriesCourseTotal.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)seriesCourseTotal.get(i-1).get("count")).intValue()
                        :seriesCourseTotal.get(i-1).get("count"));
            }
        }
        //补全数据
        if (seriesCourseTotal.size()<days){
            for (int i = seriesCourseTotal.size(); i < days; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",date1.plusDays(i).format(fmt));
                map.put("count",seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count"));
                seriesCourseTotal.add(map);
            }
        }

        int seriesCourseTotal0 = seriesCourseTotal.get(0).get("count") instanceof BigDecimal ? ((BigDecimal)seriesCourseTotal.get(0).get("count")).intValue() : (int) seriesCourseTotal.get(0).get("count");
        int seriesCourseTotal1 = seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count") instanceof BigDecimal ? ((BigDecimal)seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count")).intValue() : (int)seriesCourseTotal.get(seriesCourseTotal.size()-1).get("count");


        CourseByTargetNumDto dto = new CourseByTargetNumDto();
        dto.setFatReductionTotal(fatReductionTotal);
        dto.setFatReductionTotalGrowth(getTargetCourseGrowth(fatReductionTotal,days,date1,fmt,df));
        dto.setShapingTotal(shapingTotal);
        dto.setShapingTotalGrowth(getTargetCourseGrowth(shapingTotal,days,date1,fmt,df));
        dto.setBuildMusclesTotal(buildMusclesTotal);
        dto.setBuildMusclesGrowth(getTargetCourseGrowth(buildMusclesTotal,days,date1,fmt,df));
        dto.setSeriesCourseTotal(seriesCourseTotal);
        dto.setSeriesCourseTotalGrowth(seriesCourseTotal0 == 0 ? "0" : df.format((float)(seriesCourseTotal1-seriesCourseTotal0)/seriesCourseTotal0));
        return ResultUtil.success(dto);
    }

    public String getTargetCourseGrowth(List<Map<String, Object>> total,long days,LocalDate date1,DateTimeFormatter fmt,DecimalFormat df){
        for(int i=0;i<total.size();i++) {
            int count1 = ((BigDecimal)total.get(i).get("count")).intValue();
            if(count1== 0 && i>0){
                total.get(i).put("count",total.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)total.get(i-1).get("count")).intValue()
                        :total.get(i-1).get("count"));
            }
        }
        //补全数据
        if (total.size()<days){
            for (int i = total.size(); i < days; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",date1.plusDays(i).format(fmt));
                map.put("count",total.get(total.size()-1).get("count"));
                total.add(map);
            }
        }
        int total0 = total.get(0).get("count") instanceof BigDecimal ? ((BigDecimal)total.get(0).get("count")).intValue() : (int) total.get(0).get("count");
        int total1 = total.get(total.size()-1).get("count") instanceof BigDecimal ? ((BigDecimal)total.get(total.size()-1).get("count")).intValue() : (int)total.get(total.size()-1).get("count");
        return total0 == 0 ? "0" : df.format((float)(total1-total0)/total0);
    }


    @Override
    public Result queryDiffUserNum() {
        DecimalFormat df = new DecimalFormat("0.00");
        LocalDate today = LocalDate.now();
        //获取上上个月得起始日期
        LocalDate lastMonthFirst = today.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
        //获取上上个月得结束日期
        LocalDate lastMonthLast = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        //获取上个月得起始日期
        LocalDate monthFirst = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        //获取上个月得结束日期
        LocalDate monthLast = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        //付费用户总数
        String payingUser = userMapper.getPayingUser();
        //上上个月付费用户总数
        int payingUserLastMonth = userMapper.getPayingUserGrowth(lastMonthFirst,lastMonthLast);
        //上个月付费用户总数
        int payingUserMonth = userMapper.getPayingUserGrowth(monthFirst,monthLast);
        //续费用户总数
        String renewalUser = userMapper.getRenewalUser();
        //上上个月续费用户数量
        int renewalUserLastMonth = userMapper.getRenewalUserByMonth(lastMonthFirst,lastMonthLast);
        //上个月续费用户数量
        int renewalUserMonth = userMapper.getRenewalUserByMonth(monthFirst,monthLast);
        //流失用户总数
        int lostUsers = userMapper.selectCount(new QueryWrapper<User>().lt("vipEndDate",today).eq("deleted","0"));
        //上上个月流失用户总数
        int lostUsersLastMonth = userMapper.selectCount(new QueryWrapper<User>().between("vipEndDate",lastMonthFirst,lastMonthLast).eq("deleted","0"));
        //上个月流失用户总数
        int lostUsersMonth = userMapper.selectCount(new QueryWrapper<User>().between("vipEndDate",monthFirst,monthLast).eq("deleted","0"));
        //注册教练总数
        int registeredCoach = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>().eq("deleted","0"));
        //上上个月注册教练总数
        int registeredCoachLastMonth = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>().eq("deleted","0").between("createDate",lastMonthFirst,lastMonthLast));
        //上个月注册教练总数
        int registeredCoachMonth = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>().eq("deleted","0").between("createDate",monthFirst,monthLast));

        DiffUserNumDto dto = new DiffUserNumDto();
        dto.setPayingUser(payingUser);
        dto.setPayingUserGrowth(payingUserLastMonth == 0 ? "0" : df.format((float)(payingUserMonth-payingUserLastMonth)/payingUserLastMonth));
        dto.setRenewalUser(renewalUser);
        dto.setRenewalUserGrowth(renewalUserLastMonth == 0 ? "0" : df.format((float)(renewalUserMonth-renewalUserLastMonth)/renewalUserLastMonth));
        dto.setLostUsers(lostUsers+"");
        dto.setLostUsersGrowth(lostUsersLastMonth == 0 ? "0" : df.format((float)(lostUsersMonth-lostUsersLastMonth)/lostUsersLastMonth));
        dto.setRegisteredCoach(registeredCoach+"");
        dto.setRegisteredCoachGrowth(registeredCoachLastMonth == 0 ? "0" : df.format((float)(registeredCoachMonth-registeredCoachLastMonth)/registeredCoachLastMonth));
        return ResultUtil.success(dto);
    }

    @Override
    public Result queryDiffUserDeviceCoursePlanNum() {
        DecimalFormat df = new DecimalFormat("0.00");
        LocalDate today = LocalDate.now();
        //获取上上个月得起始日期
        LocalDate lastMonthFirst = today.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
        //获取上上个月得结束日期
        LocalDate lastMonthLast = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        //获取上个月得起始日期
        LocalDate monthFirst = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        //获取上个月得结束日期
        LocalDate monthLast = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        //用户数量
        int userTotal = userMapper.selectCount(new QueryWrapper<User>().eq("deleted", "0"));
        log.info("queryDiffUserDeviceCoursePlanNum userTotal:{{}}",userTotal);
        //上上个月用户数量
        int userNumLastMonth = userMapper.selectCount(new QueryWrapper<User>().eq("deleted", "0").between("registTime",lastMonthFirst,lastMonthLast));
        log.info("queryDiffUserDeviceCoursePlanNum userNumLastMonth:{{}}",userNumLastMonth);
        //上个月用户数量
        int userNumMonth = userMapper.selectCount(new QueryWrapper<User>().eq("deleted", "0").between("registTime",monthFirst,monthLast));
        log.info("queryDiffUserDeviceCoursePlanNum userNumMonth:{{}}",userNumMonth);
        //设备数量
        int deviceTotal = equipmentInfoMapper.selectCount(new QueryWrapper<EquipmentInfo>().eq("deleted","0"));
        log.info("queryDiffUserDeviceCoursePlanNum deviceTotal:{{}}",deviceTotal);
        //上上个月设备数量
        int deviceNumLastMonth = equipmentInfoMapper.selectCount(new QueryWrapper<EquipmentInfo>().eq("deleted", "0").between("createDate",lastMonthFirst,lastMonthLast));
        log.info("queryDiffUserDeviceCoursePlanNum deviceNumLastMonth:{{}}",deviceNumLastMonth);
        //上个月设备数量
        int deviceNumMonth = equipmentInfoMapper.selectCount(new QueryWrapper<EquipmentInfo>().eq("deleted", "0").between("createDate",monthFirst,monthLast));
        log.info("queryDiffUserDeviceCoursePlanNum deviceNumMonth:{{}}",deviceNumMonth);
        //课程数量
        int courseTotal = courseHeadMapper.selectCount(new QueryWrapper<CourseHead>().eq("deleted","0"));
        log.info("queryDiffUserDeviceCoursePlanNum courseTotal:{{}}",courseTotal);
        //上上个月课程数量
        int courseNumLastMonth = courseHeadMapper.selectCount(new QueryWrapper<CourseHead>().eq("deleted", "0").between("createDate",lastMonthFirst,lastMonthLast));
        log.info("queryDiffUserDeviceCoursePlanNum courseNumLastMonth:{{}}",courseNumLastMonth);
        //上个月课程数量
        int courseNumMonth = courseHeadMapper.selectCount(new QueryWrapper<CourseHead>().eq("deleted", "0").between("createDate",monthFirst,monthLast));
        log.info("queryDiffUserDeviceCoursePlanNum courseNumMonth:{{}}",courseNumMonth);
        //计划数量
        int planTotal = trainProgramMapper.selectCount(new QueryWrapper<TrainProgramEntity>().eq("deleted","0"));
        log.info("queryDiffUserDeviceCoursePlanNum planTotal:{{}}",planTotal);
        //上上个月计划数量
        int planNumLastMonth = trainProgramMapper.selectCount(new QueryWrapper<TrainProgramEntity>().eq("deleted", "0").between("createDate",lastMonthFirst,lastMonthLast));
        log.info("queryDiffUserDeviceCoursePlanNum planNumLastMonth:{{}}",planNumLastMonth);
        //上个月计划数量
        int planNumMonth = trainProgramMapper.selectCount(new QueryWrapper<TrainProgramEntity>().eq("deleted", "0").between("createDate",monthFirst,monthLast));
        log.info("queryDiffUserDeviceCoursePlanNum planNumMonth:{{}}",planNumMonth);
        DiffUserDeviceCoursePlanNumDto dto = new DiffUserDeviceCoursePlanNumDto();
        dto.setUserTotal(userTotal+"");
        dto.setUserTotalGrowth(userNumLastMonth == 0 ? "0" : df.format((float)(userNumMonth-userNumLastMonth)/userNumLastMonth));
        dto.setDeviceTotal(deviceTotal+"");
        dto.setDeviceTotalGrowth(deviceNumLastMonth == 0 ? "0" : df.format((float)(deviceNumMonth-deviceNumLastMonth)/deviceNumLastMonth));
        dto.setCourseTotal(courseTotal+"");
        dto.setCourseTotalGrowth(courseNumLastMonth == 0 ? "0" : df.format((float)(courseNumMonth-courseNumLastMonth)/courseNumLastMonth));
        dto.setPlanTotal(planTotal+"");
        dto.setPlanTotalGrowth(planNumLastMonth == 0 ? "0" : df.format((float)(planNumMonth-planNumLastMonth)/planNumLastMonth));
        return ResultUtil.success(dto);
    }

    @Override
    public Map<String, Double> getPercentOfVip() {
        Map<String, Double> stringIntegerMap = userMapper.queryPercentOfVip();
        return stringIntegerMap;
    }

    @Override
    public Map<String, Double> getGenderPercent() {
        Map<String, Double> stringIntegerMap = userMapper.queryGenderPercent();
        return stringIntegerMap;
    }

    @Override
    public List<Map<String, Double>> queryAgePercent() {
        return userMapper.queryAgePercent();
    }

    @Override
    public Map<String,Object> queryUserGrowthOfWeeks(String beginDate, String endDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(beginDate, fmt);
        LocalDate date2 = LocalDate.parse(endDate, fmt);
        long days = date2.toEpochDay()-date1.toEpochDay()+1;
        Map<String,Object> resultSet = new HashMap<>();
        List<Map<String,Float>> growthRates = new ArrayList<>();
        resultSet.put("growthRates",growthRates);
        calculateAllUserRateOfWeek(resultSet,growthRates,beginDate, endDate, (int) days);
        calculateVipUserRateOfWeek(resultSet,growthRates,beginDate, endDate, (int) days);
        calculateFamilyVipUserRateOfWeek(resultSet,growthRates,beginDate, endDate, (int) days);
        return resultSet;

    }

    private Map<String,Object> calculateAllUserRateOfWeek(Map<String,Object> resultSet,
                                                           List<Map<String,Float>> growthRates,String beginDate, String endDate, int days) {
        List<Map<String, Object>> maps = userMapper.queryRegistUserGrowthOfweeks(days, beginDate, endDate);
        for(int i=0;i<maps.size();i++) {
            int count1 = ((BigDecimal)maps.get(i).get("count")).intValue();
            if(i==0 && count1== 0 ) {
                Integer count = userMapper.selectCount(
                        new QueryWrapper<User>()
                                .last(" where str_to_date(registTime,'%Y-%m-%d') <= str_to_date('"+maps.get(i).get("date")+"','%Y-%m-%d')"));
                maps.get(i).put("count",count);
            }else if(count1== 0 ){
                maps.get(i).put("count",maps.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)maps.get(i-1).get("count")).intValue()
                        :maps.get(i-1).get("count"));
            }

        }
        Integer registUserEnd= maps.get(maps.size() - 1).get("count") instanceof BigDecimal
                ?((BigDecimal)maps.get(maps.size()-1).get("count")).intValue()
                :(Integer)maps.get(maps.size()-1).get("count");
        Integer registUserStart = maps.get(0).get("count") instanceof BigDecimal
                ?((BigDecimal)maps.get(0).get("count")).intValue():(Integer) maps.get(0).get("count");

        float registUserGrowthRate=registUserEnd;
        if(registUserStart != 0) {
            registUserGrowthRate= (float)(registUserEnd-registUserStart)/registUserStart;
        }
        Map<String,Float> registUserGrowthRateMap = new HashMap<>();
        registUserGrowthRateMap.put("userRate",registUserGrowthRate);
        growthRates.add(registUserGrowthRateMap);
        resultSet.put("registUserGrowth",maps);
        return resultSet;
    }

    private Map<String,Object> calculateVipUserRateOfWeek(Map<String,Object> resultSet,
                                            List<Map<String,Float>> growthRates,String beginDate, String endDate, int days) {
        List<Map<String, Object>> maps = userMapper.queryVipUserGrowthOfweeks(days, beginDate, endDate);
        for(int i=0;i<maps.size();i++) {
            int count1 = ((BigDecimal)maps.get(i).get("count")).intValue();
            if(i==0 && count1== 0 ) {
                Integer count = userMapper.selectCount(
                        new QueryWrapper<User>()
                                .last(" where vipStatus = '1' and vipBeginDate <= str_to_date('"+maps.get(i).get("date")+"','%Y-%m-%d')"));
                maps.get(i).put("count",count);
            }else if(count1== 0 ){
                maps.get(i).put("count",maps.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)maps.get(i-1).get("count")).intValue()
                        :maps.get(i-1).get("count"));
            }

        }
        Integer registUserEnd= maps.get(maps.size() - 1).get("count") instanceof BigDecimal
                ?((BigDecimal)maps.get(maps.size()-1).get("count")).intValue()
                :(Integer)maps.get(maps.size()-1).get("count");
        Integer registUserStart = maps.get(0).get("count") instanceof BigDecimal
                ?((BigDecimal)maps.get(0).get("count")).intValue():(Integer) maps.get(0).get("count");

        float registUserGrowthRate=registUserEnd;
        if(registUserStart != 0) {
            registUserGrowthRate= (float)(registUserEnd-registUserStart)/registUserStart;
        }
        Map<String,Float> registUserGrowthRateMap = new HashMap<>();
        registUserGrowthRateMap.put("vipUserRate",registUserGrowthRate);
        growthRates.add(registUserGrowthRateMap);
        resultSet.put("growthRates",growthRates);
        resultSet.put("vipUserGrowth",maps);
        return resultSet;
    }
    private Map<String,Object> calculateFamilyVipUserRateOfWeek(Map<String,Object> resultSet,
                                                          List<Map<String,Float>> growthRates,String beginDate, String endDate, int days) {
        List<Map<String, Object>> maps = userMapper.queryFamilyVipUserGrowthOfweeks(days, beginDate, endDate);
        for(int i=0;i<maps.size();i++) {
            int count1 = ((BigDecimal)maps.get(i).get("count")).intValue();
            if(i==0 && count1== 0 ) {
                Integer count = userMapper.selectCount(
                        new QueryWrapper<User>()
                                .last(" where vipStatus = '2' and vipBeginDate <= str_to_date('"+maps.get(i).get("date")+"','%Y-%m-%d')"));
                maps.get(i).put("count",count);
            }else if(count1== 0 ){
                maps.get(i).put("count",maps.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)maps.get(i-1).get("count")).intValue()
                        :maps.get(i-1).get("count"));
            }

        }
        Integer registUserEnd= maps.get(maps.size() - 1).get("count") instanceof BigDecimal
                ?((BigDecimal)maps.get(maps.size()-1).get("count")).intValue()
                :(Integer)maps.get(maps.size()-1).get("count");
        Integer registUserStart = maps.get(0).get("count") instanceof BigDecimal
                ?((BigDecimal)maps.get(0).get("count")).intValue():(Integer) maps.get(0).get("count");
        float registUserGrowthRate=registUserEnd;
        if(registUserStart != 0) {
            registUserGrowthRate= (float)(registUserEnd-registUserStart)/registUserStart;
        }

        Map<String,Float> registUserGrowthRateMap = new HashMap<>();
        registUserGrowthRateMap.put("familyVipUserRate",registUserGrowthRate);
        growthRates.add(registUserGrowthRateMap);
        resultSet.put("growthRates",growthRates);
        resultSet.put("familyVipUserGrowth",maps);
        return resultSet;
    }

    @Override
    public List<Map<String, Object>> queryUserGrowthOfDay(String beginDate, String endDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(beginDate, fmt);
        LocalDate date2 = LocalDate.parse(endDate, fmt);
        long days = date2.toEpochDay()-date1.toEpochDay()+1;
        List<Map<String, Object>> maps = userMapper.queryRegistUserGrowthOfweeks((int)days, beginDate, endDate);
        for(int i=0;i<maps.size();i++) {
            int count1 = ((BigDecimal)maps.get(i).get("count")).intValue();
            if(i==0 && count1== 0 ) {
                Integer count = userMapper.selectCount(
                        new QueryWrapper<User>()
                                .last(" where str_to_date(registTime,'%Y-%m-%d') <= str_to_date('"+maps.get(i).get("date")+"','%Y-%m-%d')"));
                maps.get(i).put("count",count);
            }else if(count1== 0 ){
                maps.get(i).put("count",maps.get(i-1).get("count")
                        instanceof BigDecimal?((BigDecimal)maps.get(i-1).get("count")).intValue()
                        :maps.get(i-1).get("count"));
            }

        }
        return maps;
    }

    @Override
    public List<Map<String, Integer>> getPartsPurchase() {
        return userMapper.getPartsPurchase();
    }

    @Override
    public Result queryEquipmentAddress(String type) {
        if ("0".equals(type)){
            //省
            return ResultUtil.success(amapDataMapper.getCountByColumn("provinceAdcode"));
        }else if ("1".equals(type)){
            //市
            return ResultUtil.success(amapDataMapper.getCountByColumn("cityAdcode"));
        }else if ("2".equals(type)){
            //区
            return ResultUtil.success(amapDataMapper.getCountByColumn("districtAdcode"));
        }else {
            return ResultUtil.error(ResultEnum.FAIL);
        }
    }

    @Override
    public List<Map<String, Object>> queryUserPayRanking() {
        List<Map<String, Object>> userPayRankingList = tbOrderMapper.getUserPayRankingList();
        for (Map<String, Object> map : userPayRankingList) {
            if (StringUtils.isNotEmpty(map.get("headPicUrl"))) {
                map.put("headPicUrl", fileImagesPath + map.get("headPicUrl"));
            }
        }
        return userPayRankingList;
    }

    @Override
    public List<Map<String, Object>> getFaultAnalysis() {
        return userMapper.getFaultAnalysis();
    }

    @Override
    public Result getDeviceAnalysis() {
        //自由训练数量
        Integer freeTrainingNum = trainFreeHeadMapper.selectCount(new QueryWrapper<TrainFreeHead>().eq("deleted", 0));
        //课程数量
        Integer courseNum = trainCourseHeadInfoMapper.selectCount(new QueryWrapper<TrainCourseHeadInfo>().eq("deleted", 0));
        //训练计划数量
        Integer planNum = programHeadMapper.selectCount(new QueryWrapper<ProgramHeadEntity>().eq("deleted", "0"));
        //系列课程数量
        Integer seriesCourseNum = seriesCourseHeadMapper.selectCount(new QueryWrapper<SeriesCourseHead>().eq("deleted", 0));
        //直播数量
        List<String> orderTypeList = new ArrayList<>();
        orderTypeList.add("0");
        orderTypeList.add("1");
        orderTypeList.add("2");
        Integer liveNum = userOrderRecordMapper.selectCount(new QueryWrapper<UserOrderRecordEntity>().eq("deleted", 0).in("orderType", orderTypeList));
        DeviceAnalysisDto dto = new DeviceAnalysisDto();
        dto.setFreeTrainingNum(freeTrainingNum+"");
        dto.setCourseNum(courseNum+"");
        dto.setPlanNum(planNum+"");
        dto.setSeriesCourseNum(seriesCourseNum+"");
        dto.setLiveNum(liveNum+"");
        return ResultUtil.success(dto);
    }

    @Override
    public List<Map<String, Object>> getViewRecords(String beginDate, String endDate) {
        return userMapper.getViewRecords(beginDate,endDate);
    }

    @Override
    public List<Map<String, Object>> getTrainDifficultNum() {
        List<Map<String, Object>> trainDifficultyNumList = trainCourseHeadInfoMapper.getTrainDifficultyNumList();
        List<BaseDifficulty> difficultyList = baseDifficultyMapper.selectList(new QueryWrapper<BaseDifficulty>().eq("deleted", 0));

        List<String> diffList = new ArrayList<>();
        for (Map<String, Object> map : trainDifficultyNumList){
            diffList.add((String) map.get("difficulty"));
        }

        for (BaseDifficulty d : difficultyList){
            if (!diffList.contains(d.getDifficulty())){
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("difficulty",d.getDifficulty());
                newMap.put("num",0);
                trainDifficultyNumList.add(newMap);
            }
        }
        return trainDifficultyNumList;
    }

    @Override
    public Result queryEquipmentUseFrequency(String beginDate, String endDate) {
        //时间格式化
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(beginDate, fmt);
        LocalDate date2 = LocalDate.parse(endDate, fmt);
        long days = date2.toEpochDay()-date1.toEpochDay()+1;

        List<String> dateList = new ArrayList<>();
        List<Map<String, Object>> equipmentAddressList = equipmentReportInfoMapper.getEquipmentUseFrequencyList(beginDate,endDate);
        for (Map m : equipmentAddressList){
            dateList.add(m.get("time")+"");
        }
        //补全数据
        for (int i = 0; i < days; i++){
            String targetDate = date1.plusDays(i).format(fmt);
            if (!dateList.contains(targetDate)){
                Map<String, Object> map = new HashMap<>();
                map.put("time",targetDate);
                map.put("count",0);
                equipmentAddressList.add(map);
            }
        }
        return ResultUtil.success(equipmentAddressList);
    }

    @Override
    public List<Map<String, Object>> getPartsUsageAnalysis() {
        return userMapper.getPartsUsageAnalysis();
    }

    @Override
    public Map<String, Object> getEquipmentStatusNum() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yestDay = now.plusDays(-1);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("a.turnOnTime",yestDay);
        String equipmentOffNum = equipmentReportInfoMapper.getEquipmentStatusNum(wrapper);
        wrapper.clear();
        wrapper.gt("a.turnOnTime",yestDay);
        String equipmentOnNum = equipmentReportInfoMapper.getEquipmentStatusNum(wrapper);
        Map<String, Object> dtoMapper = new HashMap<>();
        dtoMapper.put("on",equipmentOnNum);
        dtoMapper.put("off",equipmentOffNum);
        return dtoMapper;
    }
}
