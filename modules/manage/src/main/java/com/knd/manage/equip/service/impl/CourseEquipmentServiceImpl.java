package com.knd.manage.equip.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.equip.dto.EquipmentListDto;
import com.knd.manage.equip.entity.CourseEquipment;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.manage.equip.mapper.CourseEquipmentMapper;
import com.knd.manage.equip.mapper.EquipmentInfoMapper;
import com.knd.manage.equip.service.ICourseEquipmentService;
import com.knd.manage.equip.service.IEquipmentInfoService;
import com.knd.redis.jedis.RedisClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangcheng
 * @since 2020-07-02
 */
@Service
@Transactional
public class CourseEquipmentServiceImpl extends ServiceImpl<CourseEquipmentMapper, CourseEquipment> implements ICourseEquipmentService {

    @Override
    public Result add(String userId, String equipmentNo, String remark, String courseHeadId) {
        CourseEquipment e = new CourseEquipment();
        e.setId(UUIDUtil.getShortUUID());
        e.setEquipmentNo(equipmentNo);
        e.setCourseHeadId(courseHeadId);
        e.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
        e.setRemark(remark);
        e.setCreateBy(userId);
        e.setCreateDate(LocalDateTime.now());
        e.setLastModifiedBy(userId);
        e.setLastModifiedDate(LocalDateTime.now());
        e.setDeleted("0");
        baseMapper.insert(e);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result edit(String userId, String equipmentNo, String remark, String id, String courseHeadId) {
        //根据id获取名称
        QueryWrapper<CourseEquipment> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        qw.select("equipmentNo");
        CourseEquipment eq = baseMapper.selectOne(qw);
        if (eq ==null){
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //
        CourseEquipment e = new CourseEquipment();
        e.setId(id);
        e.setEquipmentNo(equipmentNo);
        e.setCourseHeadId(courseHeadId);
        e.setRemark(remark);
        e.setLastModifiedBy(userId);
        e.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(e);
        //成功
        return ResultUtil.success();
    }

    @Override
    public List<CourseEquipment> getEquipmentListByCourseId(String courseId) {
        QueryWrapper<CourseEquipment> ce = new QueryWrapper<>();
        ce.eq("deleted","0");
        ce.eq("courseHeadId",courseId);
        List<CourseEquipment> courseEquipments = baseMapper.selectList(ce);
        return courseEquipments;
    }

    //删除
    @Override
    public Result deleteEquipment(String userId, String id) {
        CourseEquipment b = baseMapper.selectById(id);
        if(b != null) {
            b.setId(id);
            b.setDeleted("1");
            b.setLastModifiedBy(userId);
            b.setLastModifiedDate(LocalDateTime.now());
            baseMapper.deleteById(id);
            //成功
            return ResultUtil.success();
        }else{
            //成功
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该资源不存在，无法删除！");
        }

    }


    @Override
    public CourseEquipment insertReturnEntity(CourseEquipment entity) {
        return null;
    }

    @Override
    public CourseEquipment updateReturnEntity(CourseEquipment entity) {
        return null;
    }
}
