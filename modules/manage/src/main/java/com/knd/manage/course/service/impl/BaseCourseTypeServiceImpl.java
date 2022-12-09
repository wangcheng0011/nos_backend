package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.course.dto.CourseTypeListDto;
import com.knd.manage.course.entity.BaseCourseType;
import com.knd.manage.course.mapper.BaseCourseTypeMapper;
import com.knd.manage.course.mapper.CourseTypeMapper;
import com.knd.manage.course.service.IBaseCourseTypeService;
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
 * @since 2020-07-01
 */
@Service
@Transactional
public class BaseCourseTypeServiceImpl extends ServiceImpl<BaseCourseTypeMapper, BaseCourseType> implements IBaseCourseTypeService {

    @Resource
    private CourseTypeMapper courseTypeMapper;

    @Override
    public BaseCourseType insertReturnEntity(BaseCourseType entity) {
        return null;
    }

    @Override
    public BaseCourseType updateReturnEntity(BaseCourseType entity) {
        return null;
    }

    //新增
    @Override
    public Result add(String userId, String type, String remark, String appHomeFlag, String sort) {
        //查重
        QueryWrapper<BaseCourseType> qw = new QueryWrapper<>();
        qw.eq("type", type);
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        //新增
        BaseCourseType b = new BaseCourseType();
        b.setId(UUIDUtil.getShortUUID());
        b.setType(type);
        b.setRemark(remark);
        b.setAppHomeFlag(appHomeFlag);
        b.setSort(sort);
        b.setCreateBy(userId);
        b.setCreateDate(LocalDateTime.now());
        b.setDeleted("0");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.insert(b);
        //成功
        return ResultUtil.success();
    }

    //更新
    @Override
    public Result edit(String userId, String type, String remark, String appHomeFlag, String sort, String typeId) {
        //根据id获取名称
        QueryWrapper<BaseCourseType> qw = new QueryWrapper<>();
        qw.eq("id", typeId);
        qw.eq("deleted", "0");
        qw.select("type");
        BaseCourseType ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getType().equals(type)) {
            //查重
            qw.clear();
            qw.eq("type", type);
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        BaseCourseType b = new BaseCourseType();
        b.setId(typeId);
        b.setType(type);
        b.setRemark(remark);
        b.setAppHomeFlag(appHomeFlag);
        b.setSort(sort);
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //删除
    @Override
    public Result deleteCourseType(String userId, String id) {
        //检查是否有未删除的课程使用该类型
        int aCount = courseTypeMapper.selectCourseCountByTypeId(id);
        System.out.println("是什么");
        System.out.println(aCount);
        if (aCount != 0) {
            return ResultUtil.error(ResultEnum.Type_ERROR);
        }
        BaseCourseType b = new BaseCourseType();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //获取课程分类
    @Override
    public Result getCourseType(String id) {
        QueryWrapper<BaseCourseType> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        qw.select("id", "type", "remark", "appHomeFlag", "sort");
        BaseCourseType b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    //获取课程分类列表
    @Override
    public Result getCourseTypeList(String type, String currentPage) {
        QueryWrapper<BaseCourseType> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(type)) {
            qw.like("type", type);
        }
        qw.select("id", "type", "remark", "appHomeFlag", "sort", "createDate", "lastModifiedDate");
        qw.eq("deleted", "0");
        qw.orderByAsc("length(sort+0)","sort+0","lastModifiedDate");

        List<BaseCourseType> list;
//        Map<String, Object> map = new HashMap<>();
        CourseTypeListDto courseTypeListDto = new CourseTypeListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            courseTypeListDto.setTotal(list.size());
        } else {
            //分页
            Page<BaseCourseType> page = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            page = baseMapper.selectPage(page, qw);
            list = page.getRecords();
            courseTypeListDto.setTotal((int) page.getTotal());
        }
        courseTypeListDto.setCourseTypeList(list);
        //成功
        return ResultUtil.success(courseTypeListDto);
    }


}
