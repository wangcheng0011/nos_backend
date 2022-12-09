package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.CourseHead;
import com.knd.front.home.mapper.CourseHeadMapper;
import com.knd.front.home.request.UserQueryCourseRequest;
import com.knd.front.home.service.ICourseHeadService;
import com.knd.front.user.entity.UserCourseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseHeadServiceImpl extends ServiceImpl<CourseHeadMapper, CourseHead> implements ICourseHeadService {

    private final CourseHeadMapper courseHeadMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result getCourse(UserQueryCourseRequest request) {
        log.info("getCourse request:{{}}",request);
        Page<UserCourseEntity> page = new Page<>(Long.parseLong(request.getCurrentPage()), PageInfo.pageSize);
        QueryWrapper<UserCourseEntity> qw = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(request.getSortContent()) && StringUtils.isNotEmpty(request.getSortOrder())){
            if("desc".equals(request.getSortOrder().toLowerCase())){
                qw.orderByDesc("length(a."+request.getSortContent()+")","a."+request.getSortContent());
            }
            if("asc".equals(request.getSortOrder().toLowerCase())){
                qw.orderByAsc("length(a."+request.getSortContent()+")","a."+request.getSortContent());
            }
        }
        List<UserCourseEntity> list ;
//        if(!StringUtils.isEmpty(request.getHobyId())) {
//           list = courseHeadMapper.getListByBobby(page,request.getHobyId(),request.getCourseType().ordinal());
//        }else{
//        }
        qw.eq("a.deleted","0");
        //过滤掉体验课程 1是体验课程  0是非体验课
        qw.eq("a.appHomeFlag","0");
        //已发布的信息
        qw.eq("a.releaseFlag","1");

        if (StringUtils.isNotEmpty(request.getCourseType())){
            qw.like("a.courseType",request.getCourseType());
        }
        log.info("getCourse PartIdList:{{}}",request.getPartIdList());
        if(StringUtils.isNotEmpty(request.getPartIdList())){
            qw.in("b.partId",request.getPartIdList());
        }
        if(StringUtils.isNotEmpty(request.getTargetIdList())){
            qw.in("c.targetId",request.getTargetIdList());
        }
        if (StringUtils.isNotEmpty(request.getHobbyIdList())){
            qw.in("e.id",request.getHobbyIdList());
        }
        if(StringUtils.isNotEmpty(request.getDifficultyId())){
            qw.eq("a.difficultyId",request.getDifficultyId());
        }
        qw.groupBy("a.id");
        list = courseHeadMapper.getList(page,qw);
        log.info("getCourse list:{{}}",list);

        for(UserCourseEntity entity : list){
            entity.setPicAttachUrl(StringUtils.isNotEmpty(entity.getPicAttachUrl()) ? fileImagesPath+entity.getPicAttachUrl() : "");
        }

        page.setRecords(list);
        return ResultUtil.success(page);
    }

    @Override
    public Result getCoursePage(UserQueryCourseRequest request) {
        Page<UserCourseEntity> page = new Page<>(Long.parseLong(request.getCurrentPage()), PageInfo.pageSize);
        QueryWrapper<UserCourseEntity> qw = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(request.getSortContent()) && StringUtils.isNotEmpty(request.getSortOrder())){
            if("desc".equals(request.getSortOrder().toLowerCase())){
                qw.orderByDesc("length(a."+request.getSortContent()+")","a."+request.getSortContent());
            }
            if("asc".equals(request.getSortOrder().toLowerCase())){
                qw.orderByAsc("length(a."+request.getSortContent()+")","a."+request.getSortContent());
            }
        }
        List<UserCourseEntity> list ;
//        if(!StringUtils.isEmpty(request.getHobyId())) {
//           list = courseHeadMapper.getListByBobby(page,request.getHobyId(),request.getCourseType().ordinal());
//        }else{
//        }
        qw.eq("a.deleted","0");
        //过滤掉非体验课程 1是体验课程  0是非体验课
        qw.eq("a.appHomeFlag","1");
        //已发布的信息
        qw.eq("a.releaseFlag","1");

        if (StringUtils.isNotEmpty(request.getCourseType())){
            qw.like("a.courseType",request.getCourseType());
        }
        log.info("getCourse PartIdList:{{}}",request.getPartIdList());
        if(StringUtils.isNotEmpty(request.getPartIdList())){
            qw.in("b.partId",request.getPartIdList());
        }
        if(StringUtils.isNotEmpty(request.getTargetIdList())){
            qw.in("c.targetId",request.getTargetIdList());
        }
        if (StringUtils.isNotEmpty(request.getHobbyIdList())){
            qw.in("e.id",request.getHobbyIdList());
        }
        if(StringUtils.isNotEmpty(request.getDifficultyId())){
            qw.eq("a.difficultyId",request.getDifficultyId());
        }

        qw.groupBy("a.id");

        list = courseHeadMapper.getList(page,qw);

        for(UserCourseEntity entity : list){
            entity.setPicAttachUrl(StringUtils.isNotEmpty(entity.getPicAttachUrl()) ? fileImagesPath+entity.getPicAttachUrl() : "");
        }

        page.setRecords(list);
        return ResultUtil.success(page);
    }

    @Override
    public CourseHead insertReturnEntity(CourseHead entity) {
        return null;
    }

    @Override
    public CourseHead updateReturnEntity(CourseHead entity) {
        return null;
    }


}
