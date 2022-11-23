package com.knd.manage.basedata.service.impl;

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
import com.knd.manage.basedata.dto.*;
import com.knd.manage.basedata.entity.BaseAction;
import com.knd.manage.basedata.mapper.BaseActionMapper;
import com.knd.manage.basedata.service.IBaseActionAimService;
import com.knd.manage.basedata.service.IBaseActionEquipmentService;
import com.knd.manage.basedata.service.IBaseActionService;
import com.knd.manage.basedata.vo.VoSaveAction;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.mapper.CourseTrainningNodeInfoMapper;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
@Service
@Transactional
public class BaseActionServiceImpl extends ServiceImpl<BaseActionMapper, BaseAction> implements IBaseActionService {

    @Resource
    private IAttachService iAttachService;

     @Resource
    private IBaseActionAimService iBaseActionAimService;
     @Resource
    private IBaseActionEquipmentService iBaseActionEquipmentService;

    @Resource
    private BaseActionMapper baseActionMapper;

    @Resource
    private CourseTrainningNodeInfoMapper courseTrainningNodeInfoMapper;

    //视频路径
    @Value("${upload.FileVideoPath}")
    private String FileVideoPath;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String FileImagesPath;

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


    @Override
    public BaseAction insertReturnEntity(BaseAction entity) {
        return null;
    }

    @Override
    public BaseAction updateReturnEntity(BaseAction entity) {
        return null;
    }

    //新增
    @Override
    public Result add(VoSaveAction vo) {
        //查重
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("action", vo.getAction());
        qw.eq("deleted", "0");
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
        Attach aVi = null;
        if (StringUtils.isNotEmpty(vo.getVideoAttachNewName())) {
            //视频不空
            //获取视频访问的路径
            String videoUrl = ObsObjectUtil.getUrl(obsClient, vo.getVideoAttachNewName(), bucketname, expireSeconds);
            String[] strs1 = videoUrl.split("\\/");
            videoUrl = strs1[strs1.length - 1];
            //存储视频信息
            aVi = new Attach();
            aVi.setId(UUIDUtil.getShortUUID());
            aVi.setFileName(vo.getVideoAttachName());
            aVi.setFilePath(videoUrl);
            aVi.setFileSize(vo.getVideoAttachSize());
            aVi.setFileType(vo.getVideoAttachName().substring(vo.getVideoAttachName().lastIndexOf(".") + 1));
            aVi.setCreateDate(LocalDateTime.now());
            aVi.setCreateBy(vo.getUserId());
            aVi.setLastModifiedDate(LocalDateTime.now());
            aVi.setLastModifiedBy(vo.getUserId());
            aVi.setDeleted("0");
            attachMapper.insert(aVi);
        }
        Attach aPi = null;
        if (StringUtils.isNotEmpty(vo.getPicAttachNewName())) {
            //获取图片访问的路径
            String picUrl = ObsObjectUtil.getUrl(obsClient, vo.getPicAttachNewName(), bucketname, expireSeconds);
            String[] strs2 = picUrl.split("\\/");
            picUrl = strs2[strs2.length - 1];
            //存储图片信息
            aPi = new Attach();
            aPi.setId(UUIDUtil.getShortUUID());
            aPi.setFileName(vo.getPicAttachName());
            aPi.setFilePath(picUrl);
            aPi.setFileSize(vo.getPicAttachSize());
            aPi.setFileType(vo.getPicAttachName().substring(vo.getPicAttachName().lastIndexOf(".") + 1));
            aPi.setCreateDate(LocalDateTime.now());
            aPi.setCreateBy(vo.getUserId());
            aPi.setLastModifiedDate(LocalDateTime.now());
            aPi.setLastModifiedBy(vo.getUserId());
            aPi.setDeleted("0");
            attachMapper.insert(aPi);
        }


        //
        //存储动作
        BaseAction b = new BaseAction();
        b.setId(UUIDUtil.getShortUUID());
        BeanUtils.copyProperties(vo,b);

        if (aVi != null) {
            b.setVideoAttachId(aVi.getId());
        }
        if (aPi != null) {
            b.setPicAttachId(aPi.getId());
        }
        //如果是默认动作类型则重置测试标识为非测试
        if("1".equals(vo.getStrengthTestFlag()) && "0".equals(vo.getActionType())){
            b.setStrengthTestFlag("0");
        }else{
            b.setStrengthTestFlag(vo.getStrengthTestFlag());
        }
        b.setIsTwoArms(vo.getIsTwoArms());
        b.setCreateBy(vo.getUserId());
        b.setCreateDate(LocalDateTime.now());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        b.setDeleted("0");
        baseMapper.insert(b);
        //遍历存储 到 动作目标信息基础表
        iBaseActionAimService.add(vo.getUserId(), b.getId(), vo.getAimSettingList());
        //遍历存储 到 动作适宜器材关系表
        iBaseActionEquipmentService.add(vo.getUserId(), b.getId(), vo.getEquipmentList());


        //如果测试标识为1 去除原此动作类型测试动作测试标识
        if("1".equals(b.getStrengthTestFlag())) {
            //TODO 动作查询
            //查重
            QueryWrapper<BaseAction> qw1 = new QueryWrapper<>();
            qw1.eq("strengthTestFlag", "1");
            qw1.eq("actionType", b.getActionType());
            qw1.eq("deleted", "0");
            qw1.ne("id", b.getId());
            qw1.select("id","strengthTestFlag");
            BaseAction baseAction = baseMapper.selectOne(qw1);
            if(baseAction != null && !StringUtils.isEmpty(baseAction.getId())) {
                baseAction.setStrengthTestFlag("0");
                baseAction.setLastModifiedBy(vo.getUserId());
                baseAction.setLastModifiedDate(LocalDateTime.now());
                baseMapper.updateById(baseAction);
            }
        }
        //成功
        return ResultUtil.success();
    }

    //更新
    @Override
    public Result edit(VoSaveAction vo) {
        //根据id获取动作信息
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("id", vo.getActionId());
        qw.eq("deleted", "0");
        qw.select("action", "videoAttachId", "picAttachId");
        BaseAction ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error("U0995", "动作id不存在");
        }
        if (!ta.getAction().equals(vo.getAction())) {
            //查重
            qw.clear();
            qw.eq("action", vo.getAction());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }

        BaseAction b = new BaseAction();
        b.setId(vo.getActionId());
        BeanUtils.copyProperties(vo,b);
        //
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;

        if (StringUtils.isNotEmpty(vo.getVideoAttachNewName())) {
            //检查视频文件是否有更新
            Attach aVi = iAttachService.getInfoById(ta.getVideoAttachId());
            //视频是否需要更新
            boolean vFlag = false;
            if (aVi != null) {
                String[] strs1 = aVi.getFilePath().split("\\?");
                if (!strs1[0].equals(vo.getVideoAttachNewName())) {
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
                iAttachService.deleteFile(ta.getVideoAttachId(),vo.getUserId());

                //存储文件信息
                ObsConfiguration config = new ObsConfiguration();
                config.setSocketTimeout(30000);
                config.setConnectionTimeout(10000);
                config.setEndPoint(endPoint);
                // 创建ObsClient实例
                ObsClient obsClient = new ObsClient(ak, sk, config);
                //获取视频访问的路径
                String videoUrl = ObsObjectUtil.getUrl(obsClient, vo.getVideoAttachNewName(), bucketname, expireSeconds);
                String[] strs11 = videoUrl.split("\\/");
                videoUrl = strs11[strs11.length - 1];
                //存储到数据库
                Attach aVi2 = new Attach();
                aVi2.setId(UUIDUtil.getShortUUID());
                aVi2.setFileName(vo.getVideoAttachName());
                aVi2.setFilePath(videoUrl);
                aVi2.setFileSize(vo.getVideoAttachSize());
                aVi2.setFileType(vo.getVideoAttachName().substring(vo.getVideoAttachName().lastIndexOf(".") + 1));
                aVi2.setCreateDate(LocalDateTime.now());
                aVi2.setCreateBy(vo.getUserId());
                aVi2.setLastModifiedDate(LocalDateTime.now());
                aVi2.setLastModifiedBy(vo.getUserId());
                aVi2.setDeleted("0");
                attachMapper.insert(aVi2);
                //
                b.setVideoAttachId(aVi2.getId());
            }
        }else {
            //视频已经被清除
            //清除数据库信息
            b.setVideoAttachId("");
        }

        if (StringUtils.isNotEmpty(vo.getPicAttachNewName())) {
            //检查图片文件是否有更新
            Attach aPi = iAttachService.getInfoById(ta.getPicAttachId());
            //图片是否需要更新
            boolean pFlag = false;
            if (aPi != null) {
                String[] strs2 = aPi.getFilePath().split("\\?");
                if (!strs2[0].equals(vo.getPicAttachNewName())) {
                    //更新
                    pFlag = true;
                }
                //不需要更新
            } else {
                //更新
                pFlag = true;
            }
            if (pFlag) {
                //更新图片
                //将原文件标识设为删除
                iAttachService.deleteFile(ta.getPicAttachId(),vo.getUserId());
                //存储文件信息
                ObsConfiguration config = new ObsConfiguration();
                config.setSocketTimeout(30000);
                config.setConnectionTimeout(10000);
                config.setEndPoint(endPoint);
                // 创建ObsClient实例
                ObsClient obsClient = new ObsClient(ak, sk, config);
                //获取图片访问的路径
                String picUrl = ObsObjectUtil.getUrl(obsClient, vo.getPicAttachNewName(), bucketname, expireSeconds);
                String[] strs22 = picUrl.split("\\/");
                picUrl = strs22[strs22.length - 1];
                //存储图片信息
                Attach aPi2 = new Attach();
                aPi2.setId(UUIDUtil.getShortUUID());
                aPi2.setFileName(vo.getPicAttachName());
                aPi2.setFilePath(picUrl);
                aPi2.setFileSize(vo.getPicAttachSize());
                aPi2.setFileType(vo.getPicAttachName().substring(vo.getPicAttachName().lastIndexOf(".") + 1));
                aPi2.setCreateDate(LocalDateTime.now());
                aPi2.setCreateBy(vo.getUserId());
                aPi2.setLastModifiedDate(LocalDateTime.now());
                aPi2.setLastModifiedBy(vo.getUserId());
                aPi2.setDeleted("0");
                attachMapper.insert(aPi2);
                //
                b.setPicAttachId(aPi2.getId());
            }
        }else {
            b.setPicAttachId("");
        }
        b.setIsTwoArms(vo.getIsTwoArms());
        b.setStrengthTestFlag(vo.getStrengthTestFlag());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());

        //如果是默认动作类型则重置测试标识为非测试
        if("1".equals(vo.getStrengthTestFlag()) && "0".equals(vo.getActionType())){
            b.setStrengthTestFlag("0");
        }else{
            b.setStrengthTestFlag(vo.getStrengthTestFlag());
        }
        baseMapper.updateById(b);
        //遍历更新 到 动作目标信息基础表
        iBaseActionAimService.edit(vo.getUserId(), vo.getActionId(), vo.getAimSettingList());
        //遍历更新 到 动作适宜器材关系表
        iBaseActionEquipmentService.edit(vo.getUserId(), vo.getActionId(), vo.getEquipmentList());

        //如果测试标识为1 去除原此动作类型测试动作测试标识
        if("1".equals(b.getStrengthTestFlag())) {
            //查重
            QueryWrapper<BaseAction> qw1 = new QueryWrapper<>();
            qw1.eq("strengthTestFlag", "1");
            qw1.eq("actionType", b.getActionType());
            qw1.eq("deleted", "0");
            qw1.ne("id", b.getId());
            qw1.select("id","strengthTestFlag");
            BaseAction baseAction = baseMapper.selectOne(qw1);
            if(baseAction != null && !StringUtils.isEmpty(baseAction.getId())) {
                baseAction.setStrengthTestFlag("0");
                baseAction.setLastModifiedBy(vo.getUserId());
                baseAction.setLastModifiedDate(LocalDateTime.now());
                baseMapper.updateById(baseAction);
            }
        }

        //成功
        return ResultUtil.success();
    }

    //删除
    @Override
    public Result deleteAction(VoId vo) {
        //检查是否有未删除的课程使用该动作
        int aCount = courseTrainningNodeInfoMapper.selectCourseCountByActionId(vo.getId());
        if (aCount != 0) {
            return ResultUtil.error(ResultEnum.Action_ERROR);
        }
        //根据id获取数据
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        qw.select("videoAttachId", "picAttachId");
        BaseAction ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error("U0995", "动作id不存在");
        }
        //将原文件标识设为删除
        iAttachService.deleteFile(ta.getVideoAttachId(),vo.getUserId());
        iAttachService.deleteFile(ta.getPicAttachId(),vo.getUserId());
        BaseAction b = new BaseAction();
        b.setDeleted("1");
        b.setId(vo.getId());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //删除 动作目标信息基础表信息
        iBaseActionAimService.delete(vo.getUserId(), vo.getId());
        //删除 动作适宜器材关系表信息
        iBaseActionEquipmentService.delete(vo.getUserId(), vo.getId());
        //成功
        return ResultUtil.success();
    }

    //获取动作
    @Override
    public Result getAction(String actionId) {
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("id", actionId);
        qw.eq("deleted", "0");
        BaseAction b = baseMapper.selectOne(qw);
        ActionDto actionDto = new ActionDto();
        if (b != null) {
            BeanUtils.copyProperties(b,actionDto);
            //根据动作id获取 动作目标信息列表
            List<AimSettingDto> voAimSettingList = iBaseActionAimService.getList(actionId);
            //根据动作id获取 动作适宜器材关系信息
            List<EquipmentDto> voEquipmentList = iBaseActionEquipmentService.getList(actionId);
            //拼接出参格式
            actionDto.setId(actionId);
            actionDto.setAimSettingList(voAimSettingList);
            actionDto.setEquipmentList(voEquipmentList);

            //根据id获取视频信息
            Attach aVi = iAttachService.getInfoById(b.getVideoAttachId());
            if (aVi != null) {
                actionDto.setVideoAttachUrl(FileVideoPath + aVi.getFilePath());
                actionDto.setVideoAttachSize(aVi.getFileSize());
                actionDto.setVideoAttachName(aVi.getFileName());
                String[] strs = (aVi.getFilePath()).split("\\?");
                actionDto.setVideoAttachNewName(videoFoldername + strs[0]);

            }
            //根据id获取图片信息
            Attach aPi = iAttachService.getInfoById(b.getPicAttachId());
            if (aPi != null) {
                actionDto.setPicAttachUrl(FileImagesPath + aPi.getFilePath());
                actionDto.setPicAttachSize(aPi.getFileSize());
                String[] strs = (aPi.getFilePath()).split("\\?");
                actionDto.setPicAttachNewName(imageFoldername + strs[0]);
                actionDto.setPicAttachName(aPi.getFileName());
            }

        }
        return ResultUtil.success(actionDto);
    }

    //获取动作列表
    @Override
    public Result getActionList(String actionType,String target, String part, String action, String currentPage) {
        ActionListDto actionListDto = new ActionListDto();
        List<ActionInfoDto> actionInfoDtos;
        //全部模糊查询
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            actionInfoDtos = baseActionMapper.selectAllByLike("%" + (actionType == null ? "" : actionType) + "%","%" + (target == null ? "" : target) + "%",
                    "%" + (part == null ? "" : part) + "%", "%" + (action == null ? "" : action) + "%");
            actionListDto.setTotal(actionInfoDtos.size());
        } else {
            //分页
            Page<ActionInfoDto> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            Page<ActionInfoDto> p = this.baseMapper.selectPageByLike(partPage,"%" + (actionType == null ? "" : actionType) + "%", "%" + (target == null ? "" : target) + "%",
                    "%" + (part == null ? "" : part) + "%", "%" + (action == null ? "" : action) + "%");
            actionInfoDtos = p.getRecords();
            actionListDto.setTotal((int) p.getTotal());
        }
        //拼接出参格式
        for (ActionInfoDto a : actionInfoDtos) {
            //根据动作id获取 动作适宜器材关系信息
            List<EquipmentDto> voEquipmentList = iBaseActionEquipmentService.getList(a.getId());
            StringBuilder s = new StringBuilder();
            if (!voEquipmentList.isEmpty()) {
                for (EquipmentDto e : voEquipmentList) {
                    if (e != null) {
                        s.append(e.getEquipment()).append(",");
                    }
                }
            }
            //去掉最后的那个逗号
            if (s.length() > 0) {
                String str = s.substring(0, s.length() - 1);
                a.setEquipments(str);
            }
        }
        actionListDto.setActionList(actionInfoDtos);
        return ResultUtil.success(actionListDto);
    }


}
