package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.EquipmentDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.dto.*;
import com.knd.manage.course.entity.*;
import com.knd.manage.course.mapper.*;
import com.knd.manage.course.service.*;
import com.knd.manage.course.vo.*;
import com.knd.manage.equip.entity.CourseEquipment;
import com.knd.manage.equip.service.ICourseEquipmentService;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
@Service
//所有方法开启事务
@Transactional
public class CourseHeadServiceImpl extends ServiceImpl<CourseHeadMapper, CourseHead> implements ICourseHeadService {


     @Resource
    private ICourseBodyPartService iCourseBodyPartService;

     @Resource
    private ICourseTargetService iCourseTargetService;
     @Resource
    private ICourseTypeService iCourseTypeService;
     @Resource
    private ICourseEquipmentService iCourseEquipmentService;
     @Resource
    private ICourseTrainningBlockService iCourseTrainningBlockService;


     @Resource
    private IAttachService iAttachService;

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String FileImagesPath;
    //视频路径
    @Value("${upload.FileVideoPath}")
    private String FileVideoPath;


    //终端
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
    private CourseTrainningNodeInfoMapper courseTrainningNodeInfoMapper;

    @Resource
    private CourseTypeMapper courseTypeMapper;

    @Resource
    private CourseTargetMapper courseTargetMapper;

    @Resource
    private CourseBodyPartMapper courseBodyPartMapper;

    @Resource
    private AttachMapper attachMapper;

    @Resource
    private CourseTrainningBlockMapper courseTrainningBlockMapper;

    @Override
    public CourseHead insertReturnEntity(CourseHead entity) {
        return null;
    }

    @Override
    public CourseHead updateReturnEntity(CourseHead entity) {
        return null;
    }

    //新增
    @Override
    public Result add(VoSaveCourseHead vo) throws RuntimeException{

            //查重
            QueryWrapper<CourseHead> qw = new QueryWrapper<>();
            qw.eq("course", vo.getCourse());
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
            //获取视频访问的路径
            String videoUrl = ObsObjectUtil.getUrl(obsClient, vo.getVideoAttachNewName(), bucketname, expireSeconds);
            String[] strs1 = videoUrl.split("\\/");
            videoUrl = strs1[strs1.length - 1];
            //获取图片访问的路径
            String picUrl = ObsObjectUtil.getUrl(obsClient, vo.getPicAttachNewName(), bucketname, expireSeconds);
            String[] strs2 = picUrl.split("\\/");
            picUrl = strs2[strs2.length - 1];
            //存储视频信息
            Attach aVi = new Attach();
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
             //存储图片信息
            Attach aPi = new Attach();
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
            //存储课程信息
            CourseHead c = new CourseHead();
            String courseId = UUIDUtil.getShortUUID();
            c.setId(courseId);
            c.setCourse(vo.getCourse());
            c.setTrainingFlag(vo.getTrainingFlag());
            c.setRemark(vo.getRemark());
            c.setFitCrowdRemark(vo.getFitCrowdRemark());
            c.setUnFitCrowdRemark(vo.getUnFitCrowdRemark());
            c.setSort(vo.getSort());
            c.setUserScope(vo.getUserScope());
            c.setAppHomeFlag(vo.getAppHomeFlag());
            //
            c.setVideoAttachId(aVi.getId());
            c.setVideoAttachName(vo.getVideoAttachName());
            //
            c.setPicAttachId(aPi.getId());
            c.setPicAttachUrl(picUrl);
            //
            c.setCreateDate(LocalDateTime.now());
            c.setCreateBy(vo.getUserId());
            c.setLastModifiedDate(LocalDateTime.now());
            c.setLastModifiedBy(vo.getUserId());
            c.setReleaseFlag("0");
            c.setDeleted("0");
            c.setAmount(new BigDecimal(vo.getAmount()));
            c.setCourseType(vo.getCourseType());
            c.setDifficultyId(vo.getDifficultyId());
            c.setFrequency(vo.getFrequency());
            baseMapper.insert(c);
            //存储课程与分类关联的信息
            CourseType courseType = new CourseType();
            courseType.setCourseId(courseId);
            courseType.setCreateDate(LocalDateTime.now());
            courseType.setCreateBy(vo.getUserId());
            courseType.setLastModifiedDate(LocalDateTime.now());
            courseType.setLastModifiedBy(vo.getUserId());
            courseType.setDeleted("0");
            for (VoType j : vo.getTypeList()) {
                courseType.setId(UUIDUtil.getShortUUID());
                courseType.setCourseTypeId(j.getTypeid());
                iCourseTypeService.add(courseType);
            }
            for(CourseEquipment e:vo.getEquipmentList()){
                String equipmentNo = e.getEquipmentNo();
                iCourseEquipmentService.add(vo.getUserId(),equipmentNo,null,courseId);
            }
            //如果是训练课则需要添加
            if (vo.getTrainingFlag().equals("1")) {
                //
                //存储课程与目标关联的信息
                CourseTarget courseTarget = new CourseTarget();
                courseTarget.setCourseId(courseId);
                courseTarget.setCreateDate(LocalDateTime.now());
                courseTarget.setCreateBy(vo.getUserId());
                courseTarget.setLastModifiedDate(LocalDateTime.now());
                courseTarget.setLastModifiedBy(vo.getUserId());
                courseTarget.setDeleted("0");
                for (VoTarget j : vo.getTargetList()) {
                    courseTarget.setId(UUIDUtil.getShortUUID());
                    courseTarget.setTargetId(j.getTargetid());
                    iCourseTargetService.add(courseTarget);
                }

                //存储课程与部位关联的信息
                CourseBodyPart courseBodyPart = new CourseBodyPart();
                courseBodyPart.setCourseId(courseId);
                courseBodyPart.setCreateDate(LocalDateTime.now());
                courseBodyPart.setCreateBy(vo.getUserId());
                courseBodyPart.setLastModifiedDate(LocalDateTime.now());
                courseBodyPart.setLastModifiedBy(vo.getUserId());
                courseBodyPart.setDeleted("0");
                for (VoPart j : vo.getPartList()) {
                    courseBodyPart.setId(UUIDUtil.getShortUUID());
                    courseBodyPart.setPartId(j.getPartid());
                    iCourseBodyPartService.add(courseBodyPart);
                }
            }
            Map<String, Object> m = new HashMap<>();
            m.put("id", courseId);
            //成功
            return ResultUtil.success(m);
    }

    //更新
    @Override
    public Result edit(VoSaveCourseHead vo) {
        //根据id获取名称
        QueryWrapper<CourseHead> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        qw.select("course", "videoAttachId", "picAttachId");
        CourseHead co = baseMapper.selectOne(qw);
        if (co == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error("U0995", "课程id不存在");
        }
        if (!co.getCourse().equals(vo.getCourse())) {
            //查重
            qw.clear();
            qw.eq("course", vo.getCourse());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        //更新课程信息表
        CourseHead c = new CourseHead();
        c.setId(vo.getId());
        c.setCourse(vo.getCourse());
        c.setTrainingFlag(vo.getTrainingFlag());
        c.setRemark(vo.getRemark());
        c.setFitCrowdRemark(vo.getFitCrowdRemark());
        c.setUnFitCrowdRemark(vo.getUnFitCrowdRemark());
        c.setSort(vo.getSort());
        c.setUserScope(vo.getUserScope());
        c.setAppHomeFlag(vo.getAppHomeFlag());
        c.setFrequency(vo.getFrequency());
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        //检查视频文件是否有更新
        Attach aVi = iAttachService.getInfoById(co.getVideoAttachId());
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
            iAttachService.deleteFile(co.getVideoAttachId(),vo.getUserId());
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
            c.setVideoAttachId(aVi2.getId());
            c.setVideoAttachName(vo.getVideoAttachName());
        }
        //检查图片文件是否有更新
       Attach aPi = iAttachService.getInfoById(co.getPicAttachId());
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
            iAttachService.deleteFile(co.getPicAttachId(),vo.getUserId());
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
            c.setPicAttachId(aPi2.getId());
            c.setPicAttachUrl(picUrl);
        }
        c.setLastModifiedDate(LocalDateTime.now());
        c.setLastModifiedBy(vo.getUserId());
        c.setDeleted("0");
        c.setAmount(new BigDecimal(vo.getAmount()));
        c.setCourseType(vo.getCourseType());
        c.setDifficultyId(vo.getDifficultyId());
        baseMapper.updateById(c);
        /*
        更新课程与类型关联的信息,
            多出来的则新增，少的则删除
         */
        //获取入参 类型id 列表
        List<String> rt = new ArrayList<>();
        for (VoType j : vo.getTypeList()) {
            rt.add(j.getTypeid());
        }

        List<CourseEquipment> equipmentList = iCourseEquipmentService.getEquipmentListByCourseId(vo.getId());
        for(CourseEquipment e:equipmentList){
            iCourseEquipmentService.deleteEquipment(vo.getUserId(),e.getId());
        }
        for (CourseEquipment e : vo.getEquipmentList()) {
            iCourseEquipmentService.add(vo.getUserId(),e.getEquipmentNo(),null,vo.getId());
        }


        //根据课程id查询与分类关联的数据
        List<CourseType> list = iCourseTypeService.getIDListByCourseid(vo.getId());
        //获取原有关联的类型id列表
        List<String> yt = new ArrayList<>();
        for (CourseType ct : list) {
            yt.add(ct.getCourseTypeId());
        }

        //新增的类型数据
        List<String> newType = new ArrayList<>(rt);
        newType.removeAll(yt);
        //删除的类型数据
        yt.removeAll(rt);
        if (!newType.isEmpty()) {
            //新增类型数据
            //存储课程与分类关联的信息
            CourseType k = new CourseType();
            k.setCourseId(vo.getId());
            k.setCreateDate(LocalDateTime.now());
            k.setCreateBy(vo.getUserId());
            k.setLastModifiedDate(LocalDateTime.now());
            k.setLastModifiedBy(vo.getUserId());
            k.setDeleted("0");
            for (String j : newType) {
                k.setId(UUIDUtil.getShortUUID());
                k.setCourseTypeId(j);
                iCourseTypeService.add(k);
            }
        }
        if (!yt.isEmpty()) {
            CourseType courseType = new CourseType();
            courseType.setDeleted("1");
            courseType.setLastModifiedDate(LocalDateTime.now());
            //删除数据
            for (CourseType c2 : list) {
                for (String str : yt) {
                    //获取id号
                    if (str.equals(c2.getCourseTypeId())) {
                        //删除
                        courseType.setId(c2.getId());
                        iCourseTypeService.delete(courseType);
                        //停止这一层循环
                        break;
                    }
                }
            }
        }

        if (vo.getTrainingFlag().equals("1")) {
            //是训练课
                /*
        更新课程与目标关联的信息,
        多出来的则新增，少的则删除
         */
            //获取入参 目标id 列表
            List<String> ra = new ArrayList<>();
            for (VoTarget j : vo.getTargetList()) {
                ra.add(j.getTargetid());
            }
            //根据课程id查询与分类关联的数据
            List<CourseTarget> list2 = iCourseTargetService.getIDListByCourseid(vo.getId());
            //获取原有关联的类型id列表
            List<String> ya = new ArrayList<>();
            for (CourseTarget ct : list2) {
                ya.add(ct.getTargetId());
            }
            //新增的类型数据
            List<String> newTarget = new ArrayList<>(ra);
            newTarget.removeAll(ya);
            //删除的类型数据
            ya.removeAll(ra);
            if (!newTarget.isEmpty()) {
                //新增类型数据
                //存储课程与分类关联的信息
                CourseTarget k = new CourseTarget();
                k.setCourseId(vo.getId());
                k.setCreateDate(LocalDateTime.now());
                k.setCreateBy(vo.getUserId());
                k.setLastModifiedDate(LocalDateTime.now());
                k.setLastModifiedBy(vo.getUserId());
                k.setDeleted("0");
                for (String j : newTarget) {
                    k.setId(UUIDUtil.getShortUUID());
                    k.setTargetId(j);
                    iCourseTargetService.add(k);
                }
            }
            if (!ya.isEmpty()) {
                CourseTarget k = new CourseTarget();
                k.setDeleted("1");
                k.setLastModifiedDate(LocalDateTime.now());
                //删除数据
                for (CourseTarget c2 : list2) {
                    for (String str : ya) {
                        //获取id号
                        if (str.equals(c2.getTargetId())) {
                            //删除
                            k.setId(c2.getId());
                            iCourseTargetService.delete(k);
                            //停止这一层循环
                            break;
                        }
                    }
                }
            }
            /*
             更新课程与部位关联的信息,
            多出来的则新增，少的则删除
            */
            //获取入参 目标id 列表
            List<String> rp = new ArrayList<>();
            for (VoPart j : vo.getPartList()) {
                rp.add(j.getPartid());
            }
            //根据课程id查询与分类关联的数据
            List<CourseBodyPart> list3 = iCourseBodyPartService.getIDListByCourseid(vo.getId());
            //获取原有关联的类型id列表
            List<String> yp = new ArrayList<>();
            for (CourseBodyPart ct : list3) {
                yp.add(ct.getPartId());
            }
            //新增的类型数据
            List<String> newPart = new ArrayList<>(rp);
            newPart.removeAll(yp);
            //删除的类型数据
            yp.removeAll(rp);
            if (!newPart.isEmpty()) {
                //新增类型数据
                //存储课程与分类关联的信息
                CourseBodyPart k = new CourseBodyPart();
                k.setCourseId(vo.getId());
                k.setCreateDate(LocalDateTime.now());
                k.setCreateBy(vo.getUserId());
                k.setLastModifiedDate(LocalDateTime.now());
                k.setLastModifiedBy(vo.getUserId());
                k.setDeleted("0");
                for (String j : newPart) {
                    k.setId(UUIDUtil.getShortUUID());
                    k.setPartId(j);
                    iCourseBodyPartService.add(k);
                }
            }
            if (!yp.isEmpty()) {
                CourseBodyPart k = new CourseBodyPart();
                k.setDeleted("1");
                k.setLastModifiedDate(LocalDateTime.now());
                //删除数据
                for (CourseBodyPart c2 : list3) {
                    for (String str : yp) {
                        //获取id号
                        if (str.equals(c2.getPartId())) {
                            //删除
                            k.setId(c2.getId());
                            iCourseBodyPartService.delete(k);
                            //停止这一层循环
                            break;
                        }
                    }
                }
            }
        } else {
            //是视频介绍
            //改成视频介绍了，因此需要全部删除
            //删除目标
            QueryWrapper<CourseTarget> qw2 = new QueryWrapper<>();
            qw2.eq("courseId", vo.getId());
            qw2.eq("deleted", "0");
            CourseTarget ta = new CourseTarget();
            ta.setDeleted("1");
            ta.setLastModifiedDate(LocalDateTime.now());
            ta.setLastModifiedBy(vo.getUserId());
            courseTargetMapper.update(ta, qw2);
            //删除部位
            QueryWrapper<CourseBodyPart> qw3 = new QueryWrapper<>();
            qw3.eq("courseId", vo.getId());
            qw3.eq("deleted", "0");
            CourseBodyPart b = new CourseBodyPart();
            b.setDeleted("1");
            b.setLastModifiedDate(LocalDateTime.now());
            b.setLastModifiedBy(vo.getUserId());
            courseBodyPartMapper.update(b, qw3);
        }
        //成功
        return ResultUtil.success();
    }

    //设置课程发布状态
    @Override
    public Result saveCourseReleaseFlag(VoSaveCourseReleaseFlag vo) {
        //检查视频是否是训练视频
        QueryWrapper<CourseHead> qw = new QueryWrapper<>();
        qw.select("trainingFlag");
        qw.eq("deleted", "0");
        qw.eq("id", vo.getId());
        CourseHead c = baseMapper.selectOne(qw);
        if (c == null) {
            return ResultUtil.error("U995", "课程id不存在");
        }
        //检查是否是训练课程
        if (vo.getReleaseFlag().equals("1") && c.getTrainingFlag().equals("1")) {
            //想要发布且是训练课程
            QueryWrapper<CourseTrainningBlock> qc = new QueryWrapper<>();
            qc.eq("deleted", "0");
            qc.eq("courseId", vo.getId());
            //检查是否有block
            int blCount = courseTrainningBlockMapper.selectCount(qc);
            if (blCount < 1) {
                return ResultUtil.error("U995", "该课程未设置block，不可发布");
            }
            //小节绑定block的数量检查
            int bnCount = courseTrainningNodeInfoMapper.selectBNCountByCourseId(vo.getId());
            if (bnCount != 0) {
                return ResultUtil.error("U995", "该课程存在无视频小节的block信息，不可发布");
            }
        }

        CourseHead c2 = new CourseHead();
        c2.setId(vo.getId());
        c2.setReleaseFlag(vo.getReleaseFlag());
        if (vo.getReleaseFlag().equals("0")) {
            c2.setReleaseTime("");
        } else {
            c2.setReleaseTime(DateUtils.getCurrentDateTimeStr());
        }
        c2.setLastModifiedBy(vo.getUserId());
        c2.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(c2);
        //成功
        return ResultUtil.success();
    }

    //删除课程
    @Override
    public Result deleteCourse(String userId, String id) {
        //根据id获取数据
        QueryWrapper<CourseHead> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        qw.select("videoAttachId", "picAttachId");
        CourseHead co = baseMapper.selectOne(qw);
        if (co == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error("U0995", "课程id不存在");
        }
        //将原文件标识设为删除
        iAttachService.deleteFile(co.getVideoAttachId(),userId);
        iAttachService.deleteFile(co.getPicAttachId(),userId);

        CourseHead c = new CourseHead();
        c.setId(id);
        c.setDeleted("1");
        c.setLastModifiedBy(userId);
        c.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(c);
        return ResultUtil.success();
    }

    //获取课程维护信息
    @Override
    public Result getCourseHead(String id) {
        QueryWrapper<CourseHead> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.select("id", "course", "trainingFlag", "remark", "fitCrowdRemark", "unFitCrowdRemark",
                "appHomeFlag", "sort", "userScope", "videoAttachId", "videoAttachName",
                "videoDurationMinutes", "videoDurationSeconds", "frequency","picAttachId", "picAttachUrl",
                "courseType","amount","difficultyId");
        qw.eq("deleted", "0");
        //获取课程信息
        CourseHead courseHead = baseMapper.selectOne(qw);
        //获取关联的分类信息
        List<CourseType> courseTypes = iCourseTypeService.getIDListByCourseid(id);
        List<TypeDto> typeList = new ArrayList<>();
        for (CourseType c : courseTypes) {
            TypeDto t = new TypeDto();
            t.setTypeid(c.getCourseTypeId());
            typeList.add(t);
        }

        //获取关联的目标信息
        List<CourseTarget> courseTargets = iCourseTargetService.getIDListByCourseid(id);
        List<TargetDto> targetList = new ArrayList<>();
        for (CourseTarget c : courseTargets) {
            TargetDto t = new TargetDto();
            t.setTargetid(c.getTargetId());
            targetList.add(t);
        }
        //获取关联的部位信息
        List<CourseBodyPart> courseBodyParts = iCourseBodyPartService.getIDListByCourseid(id);
        //
        List<PartDto> partList = new ArrayList<>();
        for (CourseBodyPart c : courseBodyParts) {
            PartDto t = new PartDto();
            t.setPartid(c.getPartId());
            partList.add(t);
        }
        //拼接出参格式
        CourseHeadDto courseHeadDto = new CourseHeadDto();
        courseHeadDto.setTypeList(typeList);
        courseHeadDto.setTargetList(targetList);
        courseHeadDto.setPartList(partList);
        courseHeadDto.setId(courseHead.getId());
        courseHeadDto.setFrequency(courseHead.getFrequency());
        courseHeadDto.setCourse(courseHead.getCourse());
        courseHeadDto.setTrainingFlag(courseHead.getTrainingFlag());
        courseHeadDto.setRemark(courseHead.getRemark());
        courseHeadDto.setFitCrowdRemark(courseHead.getFitCrowdRemark());
        courseHeadDto.setUnFitCrowdRemark(courseHead.getUnFitCrowdRemark());
        courseHeadDto.setAppHomeFlag(courseHead.getAppHomeFlag());
        courseHeadDto.setSort(courseHead.getSort());
        courseHeadDto.setUserScope(courseHead.getUserScope());
        //查询设备信息
        if(null!=courseHead.getId()){
            List<CourseEquipment> equipmentList = iCourseEquipmentService.getEquipmentListByCourseId(courseHead.getId());
            ArrayList<EquipmentDto> equipmentDtos = new ArrayList<>();
            for (CourseEquipment e:equipmentList) {
                EquipmentDto equipmentDto = new EquipmentDto();
                equipmentDto.setEquipment(e.getEquipmentNo());
                equipmentDtos.add(equipmentDto);
            }
            courseHeadDto.setEquipmentList(equipmentDtos);
        }
        //根据id获取视频信息
        Attach aVi = iAttachService.getInfoById(courseHead.getVideoAttachId());
        if (aVi != null) {
            courseHeadDto.setVideoAttachUrl(FileVideoPath + aVi.getFilePath());
            courseHeadDto.setVideoAttachSize(aVi.getFileSize());
            courseHeadDto.setVideoAttachName(aVi.getFileName());
            String[] strs = (aVi.getFilePath()).split("\\?");
            courseHeadDto.setVideoAttachNewName(videoFoldername + strs[0]);
        }
        //根据id获取图片信息
        Attach aPi = iAttachService.getInfoById(courseHead.getPicAttachId());
        if (aPi != null) {
            courseHeadDto.setPicAttachUrl(FileImagesPath + aPi.getFilePath());
            courseHeadDto.setPicAttachSize(aPi.getFileSize());
            courseHeadDto.setPicAttachName(aPi.getFileName());
            String[] strs = (aPi.getFilePath()).split("\\?");
            courseHeadDto.setPicAttachNewName(imageFoldername + strs[0]);
        }
        courseHeadDto.setVideoDurationMinutes(courseHead.getVideoDurationMinutes());
        courseHeadDto.setVideoDurationSeconds(courseHead.getVideoDurationSeconds());
        courseHeadDto.setAmount(String.valueOf(courseHead.getAmount()));
        courseHeadDto.setCourseType(courseHead.getCourseType());
        courseHeadDto.setDifficultyId(courseHead.getDifficultyId());
        //成功
        return ResultUtil.success(courseHeadDto);
    }

    //获取课程列表
    @Override
    public Result getCourseHeadList(String course, String typeid, String targetid, String partid, String releaseFlag,String courseType, String current) {
        //出参格式list
        List<CourseHeadInfoDto> courseHeadInfoDtos = new ArrayList<>();
        long total;
        List<String> ls;
//        Map<String, Object> m = new HashMap<>();
        CourseHeadListDto courseHeadListDto = new CourseHeadListDto();

            if (StringUtils.isEmpty(current)) {
                //获取全部
                ls = baseMapper.selectCourseIdListBySome2(course == null ? "%%" : "%" + course + "%", typeid, targetid, partid, releaseFlag,courseType);
                total = ls.size();
            } else {
                //思路：取出所有符合条件的课程id,然后计算出该页需要的课程id,然后for循环获取课程的所有信息后拼装数据
                //分页
                IPage<String> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
                page = baseMapper.selectCourseIdListBySome(page, course == null ? "%%" : "%" + course + "%", typeid, targetid, partid, releaseFlag,courseType);
                total = page.getTotal();
                ls = page.getRecords();
            }
        QueryWrapper<CourseHead> qw = new QueryWrapper<>();
        if (!ls.isEmpty()) {
            for (String id : ls) {
                qw.clear();
                qw.select("course", "sort", "releaseFlag", "appHomeFlag", "userScope", "releaseTime", "trainingFlag","amount","courseType");
                qw.eq("deleted", "0");
                qw.eq("id", id);
                CourseHead c = baseMapper.selectOne(qw);
                //获取课程信息
                CourseHeadInfoDto courseHeadInfoDto = new CourseHeadInfoDto();
                courseHeadInfoDto.setId(id);
                courseHeadInfoDto.setCourse(c.getCourse());
                courseHeadInfoDto.setSort(c.getSort());
                courseHeadInfoDto.setReleaseFlag(c.getReleaseFlag());
                courseHeadInfoDto.setAppHomeFlag(c.getAppHomeFlag());
                courseHeadInfoDto.setUserScope(c.getUserScope());
                courseHeadInfoDto.setReleaseTime(c.getReleaseTime());
                courseHeadInfoDto.setTrainingFlag(c.getTrainingFlag());
                courseHeadInfoDto.setAmount(String.valueOf(c.getAmount()));
              //  courseHeadInfoDto.setCourseTypeName(CourseTypeEnum.values()[Integer.valueOf(c.getCourseType())].getDisplay());
                courseHeadInfoDto.setCourseType(c.getCourseType());
                StringBuilder s = new StringBuilder();
                //获取类型
                //根据课程id获取分类名称
                List<String> lct = courseTypeMapper.selectNameListByCourseid(id);
                if (!lct.isEmpty()) {
                    for (String t : lct) {
                        s.append(t).append(",");
                    }
                    courseHeadInfoDto.setTypes(s.substring(0, s.length() - 1));
                }
                //清除内容
                s.delete(0, s.length());
                //获取目标
                //根据课程id获取目标名称
                List<String> lta = courseTargetMapper.selectNameListByCourseid(id);
                if (!lta.isEmpty()) {
                    for (String t : lta) {
                        s.append(t).append(",");
                    }
                    courseHeadInfoDto.setTargets(s.substring(0, s.length() - 1));
                }
                //清除内容
                s.delete(0, s.length());
                //获取部位
                List<String> lb = courseBodyPartMapper.selectNameListByCourseid(id);
                if (!lb.isEmpty()) {
                    for (String t : lb) {
                        s.append(t).append(",");
                    }
                    courseHeadInfoDto.setParts(s.substring(0, s.length() - 1));
                }
                courseHeadInfoDtos.add(courseHeadInfoDto);
            }
        }
        courseHeadListDto.setTotal((int) total);
        courseHeadListDto.setCourseHeadList(courseHeadInfoDtos);
        return ResultUtil.success(courseHeadListDto);
    }

    //批量更新排序号
    @Override
    public Result updateCoursesSort(String userId, List<CourseSort> courseSortList) {
        CourseHead c = new CourseHead();
        c.setLastModifiedDate(LocalDateTime.now());
        c.setLastModifiedBy(userId);
        for (CourseSort s : courseSortList) {
            c.setId(s.getId());
            c.setSort(s.getSort());
            baseMapper.updateById(c);
        }
        //成功
        return ResultUtil.success();
    }

    //获取课程视频信息
    @Override
    public Result getCourseVedioInfo(String id) {
        //根据课程id获取课程信息主表的信息
        CourseHead c = baseMapper.selectById(id);
        //拼接出参格式
        CourseVedioInfoDto courseVedioInfoDto = new CourseVedioInfoDto();
        courseVedioInfoDto.setId(c.getId());
        courseVedioInfoDto.setCourse(c.getCourse());
        courseVedioInfoDto.setTrainingFlag(c.getTrainingFlag());
        courseVedioInfoDto.setVideoDurationMinutes(c.getVideoDurationMinutes());
        courseVedioInfoDto.setVideoDurationSeconds(c.getVideoDurationSeconds());
        if (c.getTrainingFlag().equals("1")) {
            //是训练课
            //根据课程id获取所有block信息
            List<CourseTrainningBlock> lt = iCourseTrainningBlockService.getCourseBlockInfoToList(id);
            //根据课程 id 获取小节信息
            List<NodeDto> nodeDtos = courseTrainningNodeInfoMapper.selectSomeByCourseId(id);
            courseVedioInfoDto.setBlocks(lt);
            courseVedioInfoDto.setNodes(nodeDtos);
        } else {
            //不是训练课程
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return ResultUtil.success(courseVedioInfoDto);
    }

    //维护视频时长
    @Override
    public Result saveCourseVedioDuration(VoSaveCourseVedioDuration vo) {
        //获取最大的视频小节结束时间
        QueryWrapper<CourseTrainningNodeInfo> qw = new QueryWrapper<>();
        qw.eq("deleted", 0);
        qw.eq("courseId", vo.getId());
        qw.select("nodeEndMinutes", "nodeEndSeconds");
        qw.orderByDesc("nodeSort + 0");
        qw.last("limit 0,1");
        CourseTrainningNodeInfo ct = courseTrainningNodeInfoMapper.selectOne(qw);
        if (ct != null) {
            //最大小节时间
            int da = Integer.parseInt(ct.getNodeEndMinutes()) * 60 + Integer.parseInt(ct.getNodeEndSeconds());
            //当前时间
            int n = Integer.parseInt(vo.getVideoDurationMinutes()) * 60 + Integer.parseInt(vo.getVideoDurationSeconds());
            if (n < da) {
                return ResultUtil.error("U995", "视频总时长不可小于最后一个小节的结束时间");
            }
        }
        CourseHead c = new CourseHead();
        c.setLastModifiedDate(LocalDateTime.now());
        c.setLastModifiedBy(vo.getUserId());
        c.setId(vo.getId());
        c.setVideoDurationMinutes(vo.getVideoDurationMinutes());
        c.setVideoDurationSeconds(vo.getVideoDurationSeconds());
        baseMapper.updateById(c);
        //成功
        return ResultUtil.success();
    }


}

