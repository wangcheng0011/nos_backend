package com.knd.manage.equip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.equip.dto.ApkInfoDto;
import com.knd.manage.equip.dto.ApkInfoListDto;
import com.knd.manage.equip.entity.AppReleaseVersion;
import com.knd.manage.equip.mapper.AppReleaseVersionMapper;
import com.knd.manage.equip.service.IAppReleaseVersionService;
import com.knd.manage.equip.vo.VoGetApkInfoList;
import com.knd.manage.equip.vo.VoSaveApkInfo;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
@Service
@Transactional
public class AppReleaseVersionServiceImpl extends ServiceImpl<AppReleaseVersionMapper, AppReleaseVersion> implements IAppReleaseVersionService {

     @Resource
    private IAttachService iAttachService;
    //apk路径
    @Value("${upload.FileAppPath}")
    private String FileAppPath;


    @Override
    public AppReleaseVersion insertReturnEntity(AppReleaseVersion entity) {
        return null;
    }

    @Override
    public AppReleaseVersion updateReturnEntity(AppReleaseVersion entity) {
        return null;
    }

    //终端，可以不带协议、也可以带协议
    @Value("${OBS.endPoint}")
    private String endPoint;
    //公钥
    @Value("${OBS.ak}")
    private String ak;
    //私钥
    @Value("${OBS.sk}")
    private String sk;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;
    //视频文件夹路径
    @Value("${OBS.videoFoldername}")
    private String videoFoldername;
    //apk文件夹路径
    @Value("${OBS.appFoldername}")
    private String appFoldername;
    //桶名
    @Value("${OBS.bucketname}")
    private String bucketname;

    @Resource
    private AttachMapper attachMapper;

    //新增apk版本信息
    @Override
    public Result add(VoSaveApkInfo vo) {
        //查重
        QueryWrapper<AppReleaseVersion> qw = new QueryWrapper<>();
        qw.eq("versionPrefix",vo.getVersionPrefix());
        qw.eq("appVersion", vo.getAppVersion());
        qw.eq("deleted", "0");
        qw.eq("appType",vo.getAppType());
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        //存储文件信息
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, config);
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        //获取视频访问的路径
        String apkUrl = ObsObjectUtil.getUrl(obsClient, vo.getAttachNewName(), bucketname, expireSeconds);
        String[] strs1 = apkUrl.split("\\/");
        apkUrl = strs1[strs1.length - 1];
        //存储apk信息
        Attach aVi = new Attach();
        aVi.setId(UUIDUtil.getShortUUID());
        aVi.setFileName(vo.getAttachName());
        aVi.setFilePath(apkUrl);
        aVi.setFileSize(vo.getAttachSize());
        aVi.setFileType(vo.getAttachName().substring(vo.getAttachName().lastIndexOf(".") + 1));
        aVi.setCreateDate(LocalDateTime.now());
        aVi.setCreateBy(vo.getUserId());
        aVi.setLastModifiedDate(LocalDateTime.now());
        aVi.setLastModifiedBy(vo.getUserId());
        aVi.setDeleted("0");
        attachMapper.insert(aVi);
        //存储版本信息
        AppReleaseVersion a = new AppReleaseVersion();
        a.setId(UUIDUtil.getShortUUID());
        a.setVersionPrefix(vo.getVersionPrefix());
        a.setAppVersion(vo.getAppVersion());
        a.setAttachId(aVi.getId());
        a.setReleaseStatus("0");
        //0：不更新，1强制更新
        a.setForceFlag(vo.getForceFlag());
        a.setAppType(vo.getAppType());
        a.setContent(vo.getContent());
        a.setCreateBy(vo.getUserId());
        a.setCreateDate(LocalDateTime.now());
        a.setLastModifiedBy(vo.getUserId());
        a.setLastModifiedDate(LocalDateTime.now());
        a.setDeleted("0");
        baseMapper.insert(a);
        //成功
        return ResultUtil.success();
    }

    //更新apk版本信息
    @Override
    public Result edit(VoSaveApkInfo vo) {
        //根据id获取名称
        QueryWrapper<AppReleaseVersion> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        qw.select("appVersion", "attachId");
        AppReleaseVersion ar = baseMapper.selectOne(qw);
        if (ar == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ar.getAppVersion().equals(vo.getAppVersion())) {
            //查重
            qw.clear();
            qw.eq("appVersion", vo.getAppVersion());
            qw.eq("deleted", "0");
            qw.eq("appType",vo.getAppType());
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        AppReleaseVersion a = new AppReleaseVersion();
        a.setId(vo.getId());
        a.setAppVersion(vo.getAppVersion());
        a.setVersionPrefix(vo.getVersionPrefix());
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        //检查视频文件是否有更新
        Attach aVi = iAttachService.getInfoById(ar.getAttachId());
        //视频是否需要更新
        boolean vFlag = false;
        if (aVi != null) {
            String[] strs1 = aVi.getFilePath().split("\\?");
            if (!strs1[0].equals(vo.getAttachNewName())) {
                //更新
                vFlag = true;
            }
            //不需要更新
        } else {
            //更新
            vFlag = true;
        }
        if (vFlag) {
            //更新视频
            //将原文件标识设为删除
            iAttachService.deleteFile(ar.getAttachId(),vo.getUserId());

            //存储文件信息
            ObsConfiguration config = new ObsConfiguration();
            config.setSocketTimeout(30000);
            config.setConnectionTimeout(10000);
            config.setEndPoint(endPoint);
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(ak, sk, config);
            //获取视频访问的路径
            String videoUrl = ObsObjectUtil.getUrl(obsClient, vo.getAttachNewName(), bucketname, expireSeconds);
            String[] strs11 = videoUrl.split("\\/");
            videoUrl = strs11[strs11.length - 1];
            //存储到数据库
            Attach aVi2 = new Attach();
            aVi2.setId(UUIDUtil.getShortUUID());
            aVi2.setFileName(vo.getAttachName());
            aVi2.setFilePath(videoUrl);
            aVi2.setFileSize(vo.getAttachSize());
            aVi2.setFileType(vo.getAttachName().substring(vo.getAttachName().lastIndexOf(".") + 1));
            aVi2.setCreateDate(LocalDateTime.now());
            aVi2.setCreateBy(vo.getUserId());
            aVi2.setLastModifiedDate(LocalDateTime.now());
            aVi2.setLastModifiedBy(vo.getUserId());
            aVi2.setDeleted("0");
            attachMapper.insert(aVi2);
            //
            a.setAttachId(aVi2.getId());
        }
        a.setForceFlag(vo.getForceFlag());
        a.setAppType(vo.getAppType());
        a.setContent(vo.getContent());
        a.setLastModifiedBy(vo.getUserId());
        a.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(a);
        //成功
        return ResultUtil.success();
    }

    //获取apk版本信息
    @Override
    public Result getApkInfo(String id) {
        QueryWrapper<AppReleaseVersion> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.select("id", "versionPrefix","appVersion", "attachId", "releaseStatus", "forceFlag", "content", "releaseTime");
        qw.eq("deleted", "0");
        AppReleaseVersion b = baseMapper.selectOne(qw);
        //拼接出参格式
        ApkInfoDto apkInfoDto = new ApkInfoDto();
        //根据id获取视频信息
        Attach aVi = iAttachService.getInfoById(b.getAttachId());
        if (aVi != null) {
            apkInfoDto.setAttachUrl(FileAppPath + aVi.getFilePath());
            apkInfoDto.setAttachSize(aVi.getFileSize());
            apkInfoDto.setAttachName(aVi.getFileName());
            String[] strs = (aVi.getFilePath()).split("\\?");
            apkInfoDto.setAttachNewName(appFoldername + strs[0]);
        }
        apkInfoDto.setId(b.getId());
        apkInfoDto.setAppVersion(b.getAppVersion());
        apkInfoDto.setVersionPrefix(b.getVersionPrefix());
        apkInfoDto.setAttachId(b.getAttachId());
        apkInfoDto.setReleaseStatus(b.getReleaseStatus());
        apkInfoDto.setForceFlag(b.getForceFlag());
        apkInfoDto.setAppType(b.getAppType());
        apkInfoDto.setContent(b.getContent());
        apkInfoDto.setReleaseTime(b.getReleaseTime());
        //成功
        return ResultUtil.success(apkInfoDto);
    }

    //删除apk版本信息
    @Override
    public Result deleteApkInfo(VoId vo) {
        //根据id获取名称
        QueryWrapper<AppReleaseVersion> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        qw.select("attachId");
        AppReleaseVersion ar = baseMapper.selectOne(qw);
        if (ar == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error("U0995", "课程id不存在");
        }
        //将原文件标识设为删除
        iAttachService.deleteFile(ar.getAttachId(),vo.getUserId());
        //
        AppReleaseVersion b = new AppReleaseVersion();
        b.setId(vo.getId());
        b.setDeleted("1");
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //获取apk版本信息列表
    @Override
    public Result getApkInfoList(VoGetApkInfoList vo) {
        QueryWrapper<AppReleaseVersion> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(vo.getAppVersion())) {
            qw.like("appVersion", vo.getAppVersion());
        }
        if (StringUtils.isNotEmpty(vo.getAppType())) {
            //不空
            if (!vo.getAppType().equals("0") && !vo.getAppType().equals("1")&& !vo.getAppType().equals("2")) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            qw.eq("appType", vo.getAppType());
        }
        if (StringUtils.isNotEmpty(vo.getReleaseStatus())) {
            //不空
            if (!vo.getReleaseStatus().equals("0") && !vo.getReleaseStatus().equals("1")) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            qw.eq("releaseStatus", vo.getReleaseStatus());
        }
        //根据版本号 xx.xx.xx 的格式来排序
//        qw.select("id", "appVersion", "attachId", "releaseStatus", "forceFlag", "content", "releaseTime",
//                "SUBSTRING_INDEX(appVersion,'.',1) AS first_version",
//                "SUBSTRING_INDEX(SUBSTRING_INDEX(appVersion,'.',-2),'.',1) AS second_version",
//                "SUBSTRING_INDEX(appVersion,'.',-1) AS third_version");
//        qw.eq("deleted", "0");
//        qw.orderByDesc("first_version+0", "second_version+0", "third_version+0","createDate");
        //
        qw.select("id", "versionPrefix", "appVersion", "attachId", "releaseStatus", "forceFlag","appType", "content", "releaseTime","createDate");
        qw.eq("deleted", "0");
        qw.orderByDesc("createDate");
        List<AppReleaseVersion> list;
        ApkInfoListDto apkInfoListDto = new ApkInfoListDto();
        //分页
        Page<AppReleaseVersion> partPage = new Page<>(Integer.parseInt(vo.getCurrent()), PageInfo.pageSize);
        partPage = baseMapper.selectPage(partPage, qw);
        list = partPage.getRecords();
        List<ApkInfoDto> apkInfoDtos = new ArrayList<>();
        if (!list.isEmpty()) {
            for (AppReleaseVersion b : list) {
                //根据附件id获取附件路径
                Attach a = iAttachService.getInfoById(b.getAttachId());
                //拼接出参格式
                ApkInfoDto apkInfoDto = new ApkInfoDto();
                if (a != null) {
                    apkInfoDto.setAttachUrl(FileAppPath + a.getFilePath());
                }
                apkInfoDto.setId(b.getId());
                apkInfoDto.setVersionPrefix(b.getVersionPrefix());
                apkInfoDto.setAppVersion(b.getAppVersion());
                apkInfoDto.setAttachId(b.getAttachId());
                apkInfoDto.setReleaseStatus(b.getReleaseStatus());
                apkInfoDto.setForceFlag(b.getForceFlag());
                apkInfoDto.setAppType(b.getAppType());
                apkInfoDto.setContent(b.getContent());
                apkInfoDto.setReleaseTime(b.getReleaseTime());
                //获取文件信息
                //根据id获取视频信息
                Attach aVi = iAttachService.getInfoById(b.getAttachId());
                if (aVi != null) {
                    apkInfoDto.setAttachUrl(FileAppPath + aVi.getFilePath());
                    apkInfoDto.setAttachSize(aVi.getFileSize());
                    apkInfoDto.setAttachName(aVi.getFileName());
                    String[] strs = (aVi.getFilePath()).split("\\?");
                    apkInfoDto.setAttachNewName(appFoldername + strs[0]);
                }
                apkInfoDtos.add(apkInfoDto);
            }
        }
        apkInfoListDto.setTotal((int) partPage.getTotal());
        apkInfoListDto.setIntroductionCourseList(apkInfoDtos);
        //成功
        return ResultUtil.success(apkInfoListDto);
    }

    //修改apk版本发布状态
    @Override
    public Result saveReleaseStatus(String userId, String id, String releaseStatus) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AppReleaseVersion b = new AppReleaseVersion();
        b.setId(id);
        b.setReleaseStatus(releaseStatus);
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        if (releaseStatus.equals("1")) {
            b.setReleaseTime(sdf.format(new Date()));
        } else {
            b.setReleaseTime("");
        }
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }


}
