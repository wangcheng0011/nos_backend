package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.TargetListDto;
import com.knd.manage.basedata.entity.BaseTarget;
import com.knd.manage.basedata.mapper.BaseActionMapper;
import com.knd.manage.basedata.mapper.BaseTargetMapper;
import com.knd.manage.basedata.service.IBaseTargetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.manage.course.mapper.CourseTargetMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class BaseTargetServiceImpl extends ServiceImpl<BaseTargetMapper, BaseTarget> implements IBaseTargetService {

    @Override
    public BaseTarget insertReturnEntity(BaseTarget entity) {
        return null;
    }

    @Override
    public BaseTarget updateReturnEntity(BaseTarget entity) {
        return null;
    }

    @Resource
    private CourseTargetMapper courseTargetMapper;

    @Resource
    private BaseActionMapper baseActionMapper;

    //新增目标
    @Override
    public Result add(String userId, String target, String remark) {
        //查重
        QueryWrapper<BaseTarget> qw = new QueryWrapper<>();
        qw.eq("target", target);
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseTarget b = new BaseTarget();
        b.setId(UUIDUtil.getShortUUID());
        b.setTarget(target);
        b.setRemark(remark);
        b.setCreateBy(userId);
        b.setCreateDate(LocalDateTime.now());
        b.setDeleted("0");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.insert(b);
        //成功
        return ResultUtil.success();
    }

    //更新目标
    @Override
    public Result edit(String userId, String target, String remark, String targetId) {
        //根据id获取名称
        QueryWrapper<BaseTarget> qw = new QueryWrapper<>();
        qw.eq("id", targetId);
        qw.eq("deleted", "0");
        qw.select("target");
        BaseTarget ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getTarget().equals(target)) {
            //需要修改目标名字
            //查重
            qw.clear();
            qw.eq("target", target);
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }

        BaseTarget b = new BaseTarget();
        b.setId(targetId);
        b.setTarget(target);
        b.setRemark(remark);
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //删除目标
    @Override
    public Result deleteTarget(String userId, String id) {
        //检查是否有未删除的课程使用该目标
        int cCount = courseTargetMapper.selectCourseCountById(id);
        if (cCount != 0) {
            return ResultUtil.error(ResultEnum.Target_ERROR);
        }
        //检查是否有未删除的动作使用该目标
        int aCount = baseActionMapper.selectActionCountByTargetId(id);
        if (aCount != 0) {
            return ResultUtil.error(ResultEnum.Target_ERROR_2);
        }
        BaseTarget b = new BaseTarget();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //获取目标
    @Override
    public Result getTarget(String id) {
        QueryWrapper<BaseTarget> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.select("id", "target", "remark");
        BaseTarget b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    //获取目标列表
    @Override
    public Result getTargetList(String target, String currentPage) {
        QueryWrapper<BaseTarget> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(target)) {
            qw.like("target", target);
        }
        qw.select("id", "target", "remark");
        qw.eq("deleted", "0");
        qw.orderByAsc("target");
        List<BaseTarget> list;
        TargetListDto targetListDto = new TargetListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            targetListDto.setTotal(list.size());
        } else {
            //分页
            Page<BaseTarget> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            list = partPage.getRecords();
            targetListDto.setTotal((int) partPage.getTotal());
        }
        targetListDto.setTargetList(list);
        //成功
        return ResultUtil.success(targetListDto);
    }


}
