package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.home.dto.GetAppReleaseVersionDto;
import com.knd.front.entity.AppReleaseVersion;
import com.knd.front.home.mapper.AppReleaseVersionMapper;
import com.knd.front.home.service.IAppReleaseVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Service
@Slf4j
public class AppReleaseVersionServiceImpl extends ServiceImpl<AppReleaseVersionMapper, AppReleaseVersion> implements IAppReleaseVersionService {
    private AppReleaseVersionMapper appReleaseVersionMapper;

    @Autowired
    public AppReleaseVersionServiceImpl(AppReleaseVersionMapper appReleaseVersionMapper) {
        this.appReleaseVersionMapper = appReleaseVersionMapper;
    }

    @Value("${upload.FileAppPath}")
    private String fileAppPath;


    @Override
    public AppReleaseVersion insertReturnEntity(AppReleaseVersion entity) {
        return null;
    }

    @Override
    public AppReleaseVersion updateReturnEntity(AppReleaseVersion entity) {
        return null;
    }

    @Override
    public Result getAppReleaseVersion(String currentAppVersion,String currentFirmwareVersion,String currentSystemVersion) {
//        log.info("service 传进来的是什么："+currentAppVersion);
        //获取最新的apk文件信息
        //版本号是KS-YS-1.0.0截取版本号第二个‘-’之前的平台号
        String versionPrefix;
        if("".equals(currentSystemVersion)){
            versionPrefix = "KS-TD";
        }else if("1.0".equals(currentSystemVersion)){
            versionPrefix ="KS-YS";
        }else{
            try {
                int endIndex = currentSystemVersion.lastIndexOf("-");
                versionPrefix = currentSystemVersion.substring(0, endIndex);
            }catch (Exception e){
                log.info("无法获取系统版本的平台代号，使用默认空版本");
                versionPrefix = "";
            }

        }

//        String[] temp = currentSystemVersion.split("-");
//        String versionPrefix = temp[0]+"-"+temp[1];

        List<GetAppReleaseVersionDto> appReleaseVersionList = appReleaseVersionMapper.getAppReleaseVersion();
        String finalVersionPrefix = versionPrefix;
        List<GetAppReleaseVersionDto> appReleaseVersions = appReleaseVersionList.stream().filter(e -> {
            if (!"2".equals(e.getAppType())) {
                return true;
            } else {
                if(StringUtils.isEmpty(finalVersionPrefix)) {
                    return false;
                }else if(e.getVersionPrefix().equals(finalVersionPrefix)) {
                    return true;
                } else {
                    return false;
                }
            }
        }).collect(Collectors.toList());
//        appReleaseVersions = appReleaseVersions.stream().filter(e ->{
//            if()
//            return false;
//        }).collect(Collectors.toList());

        for(GetAppReleaseVersionDto appReleaseVersion: appReleaseVersions) {
            //拼接路径
            appReleaseVersion.setApkUrl(fileAppPath + appReleaseVersion.getApkUrl());
            if(appReleaseVersion.getAppType().equals("0")) {
                if (StringUtils.isEmpty(currentAppVersion) ) {
//            log.info("新设备，强制更新，setForceFlag =1");
                    appReleaseVersion.setForceFlag("1");
                    continue;
                    // return ResultUtil.success(appReleaseVersion);
                }
                if (currentAppVersion.equals(appReleaseVersion.getAppVersion())) {
                    //版本号一样，不更新
//            log.info("版本号一样，不更新，setForceFlag =0");
                    appReleaseVersion.setForceFlag("0");
                    continue;
                }
            }else if(appReleaseVersion.getAppType().equals("1")){
                if (StringUtils.isEmpty(currentFirmwareVersion) ) {
//            log.info("新设备，强制更新，setForceFlag =1");
                    appReleaseVersion.setForceFlag("1");
                    // return ResultUtil.success(appReleaseVersion);
                    continue;
                }
                if (currentFirmwareVersion.equals(appReleaseVersion.getAppVersion())) {
                    //版本号一样，不更新
//            log.info("版本号一样，不更新，setForceFlag =0");
                    appReleaseVersion.setForceFlag("0");
                    continue;
                }
            }else if(appReleaseVersion.getAppType().equals("2")){
                if (StringUtils.isEmpty(currentSystemVersion) ) {
//            log.info("新设备，强制更新，setForceFlag =1");
                    appReleaseVersion.setForceFlag("1");
                    // return ResultUtil.success(appReleaseVersion);
                    continue;
                }
                if (currentSystemVersion.equals(versionPrefix+"-"+appReleaseVersion.getAppVersion())) {
                    //版本号一样，不更新
//            log.info("版本号一样，不更新，setForceFlag =0");
                    appReleaseVersion.setForceFlag("0");
                    continue;
                }
            }else{
                appReleaseVersion.setForceFlag("2");
                continue;
            }

            if ("0".equals(appReleaseVersion.getForceFlag())){
                //不是强制更新
//            log.info("版本号不一样，不强制更新，setForceFlag =2");
                appReleaseVersion.setForceFlag("2");
            }
        }


        return ResultUtil.success(appReleaseVersions);
    }
//    public
}
