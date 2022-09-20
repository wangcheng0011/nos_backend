package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.basedata.dto.PlatformInfoDto;
import com.knd.manage.basedata.service.IPlatformInfoService;
import com.knd.manage.course.entity.CourseHead;
import com.knd.manage.course.mapper.CourseHeadMapper;
import com.knd.manage.equip.entity.AppReleaseVersion;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.manage.equip.entity.EquipmentReportInfo;
import com.knd.manage.equip.mapper.AppReleaseVersionMapper;
import com.knd.manage.equip.mapper.EquipmentInfoMapper;
import com.knd.manage.equip.mapper.EquipmentReportInfoMapper;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlatformInfoServiceImpl implements IPlatformInfoService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private CourseHeadMapper courseHeadMapper;
    @Resource
    private EquipmentReportInfoMapper equipmentReportInfoMapper;
    @Resource
    private AppReleaseVersionMapper appReleaseVersionMapper;


    //获取后管首页统计信息
    @Override
    public Result getPlatformInfo() {
        PlatformInfoDto p = new PlatformInfoDto();
        //获取用户总数
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        int userCount = userMapper.selectCount(qw);
        //课程视频数量
        QueryWrapper<CourseHead> qw2 = new QueryWrapper<>();
        qw2.eq("deleted", "0");
        qw2.eq("releaseFlag","1");
        qw2.eq("trainingFlag", "1");
        int courseCount = courseHeadMapper.selectCount(qw2);
        //上报设备数量
        QueryWrapper<EquipmentReportInfo> qw3 = new QueryWrapper<>();
        qw3.eq("deleted", "0");
        //去重
        qw3.select("DISTINCT equipmentNo");
        int equipmentReportCount = equipmentReportInfoMapper.selectCount(qw3);
        //设备最新版本
        QueryWrapper<AppReleaseVersion> qw4 = new QueryWrapper<>();
        qw4.eq("deleted", "0");
        qw4.eq("releaseStatus", "1");
        qw4.eq("appType", "0");
        qw4.select("appVersion");
        qw4.orderByDesc("STR_TO_DATE(releaseTime,'%Y-%m-%d %H:%i:%s')");
        qw4.last("limit 0,1");
        AppReleaseVersion a = appReleaseVersionMapper.selectOne(qw4);
        //
        //拼接出参格式
        p.setRegisterUserCount(userCount + "");
        p.setCourseCount(courseCount + "");
        p.setReportDeviceCount(equipmentReportCount + "");
        if (a != null) {
            p.setAppLatestVersion(a.getAppVersion());
        } else {
            p.setAppLatestVersion("");
        }
        return ResultUtil.success(p);
    }
}
