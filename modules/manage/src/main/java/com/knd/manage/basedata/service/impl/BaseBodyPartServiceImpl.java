package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.PartListDto;
import com.knd.manage.basedata.entity.BaseBodyPart;
import com.knd.manage.basedata.mapper.BaseActionMapper;
import com.knd.manage.basedata.mapper.BaseBodyPartMapper;
import com.knd.manage.basedata.service.IBaseBodyPartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.manage.course.mapper.CourseBodyPartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

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
public class BaseBodyPartServiceImpl extends ServiceImpl<BaseBodyPartMapper, BaseBodyPart> implements IBaseBodyPartService {

    @Resource
    private CourseBodyPartMapper courseBodyPartMapper;

    @Resource
    private BaseActionMapper baseActionMapper;

    @Override
    public BaseBodyPart insertReturnEntity(BaseBodyPart entity) {
        return null;
    }

    @Override
    public BaseBodyPart updateReturnEntity(BaseBodyPart entity) {
        return null;
    }

    //新增部位
    @Override
    public Result add(String userId, String part, String remark) {
        //查重
        QueryWrapper<BaseBodyPart> qw = new QueryWrapper<>();
        qw.eq("part", part);
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseBodyPart b = new BaseBodyPart();
        b.setId(UUIDUtil.getShortUUID());
        b.setPart(part);
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

    //更新部位
    @Override
    public Result edit(String userId, String part, String remark, String partId) {
        //根据id获取名称
        QueryWrapper<BaseBodyPart> qw = new QueryWrapper<>();
        qw.eq("id", partId);
        qw.eq("deleted", "0");
        qw.select("part");
        BaseBodyPart ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getPart().equals(part)) {
            //查重
            qw.clear();
            qw.eq("part", part);
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        //更新
        BaseBodyPart b = new BaseBodyPart();
        b.setId(partId);
        b.setPart(part);
        b.setRemark(remark);
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //删除部位
    @Override
    public Result deletePart(String userId, String id) {
        //检查是否有未删除的课程使用该部位
        int cCount = courseBodyPartMapper.selectCourseCountById(id);
        if (cCount != 0) {
            return ResultUtil.error(ResultEnum.Part_ERROR);
        }
        //检查是否有未删除的动作使用该部位
        int aCount = baseActionMapper.selectActionCountByPartId(id);
        if (aCount != 0) {
            return ResultUtil.error(ResultEnum.Part_ERROR_2);
        }

        BaseBodyPart b = new BaseBodyPart();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //获取部位
    @Override
    public Result GetPart(String id) {
        QueryWrapper<BaseBodyPart> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.select("id", "part", "remark");
        qw.eq("deleted", "0");
        BaseBodyPart b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    //获取部位列表
    @Override
    public Result getPartList(String part, String currentPage) {
        QueryWrapper<BaseBodyPart> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(part)) {
            //不为空时
            qw.like("part", part);
        }
        qw.select("id", "part", "remark");
        qw.eq("deleted", "0");
        qw.orderByAsc("part");
        List<BaseBodyPart> list;
        PartListDto partListDto = new PartListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            //总数据条数
            partListDto.setTotal(list.size());
        } else {
            //分页
            Page<BaseBodyPart> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            //获取全部
            list = partPage.getRecords();
            //总数据条数
            partListDto.setTotal((int) partPage.getTotal());
        }
        partListDto.setPartList(list);
        //成功
        return ResultUtil.success(partListDto);
    }


}
