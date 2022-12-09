package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.EquipmentListDto;
import com.knd.manage.basedata.entity.BaseEquipment;
import com.knd.manage.basedata.mapper.BaseActionEquipmentMapper;
import com.knd.manage.basedata.mapper.BaseEquipmentMapper;
import com.knd.manage.basedata.service.IBaseEquipmentService;
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
public class BaseEquipmentServiceImpl extends ServiceImpl<BaseEquipmentMapper, BaseEquipment> implements IBaseEquipmentService {



    @Resource
    private BaseActionEquipmentMapper baseActionEquipmentMapper;

//    @Resource
//    private GoodEquipmentMapper goodEquipmentMapper;

    @Override
    public BaseEquipment insertReturnEntity(BaseEquipment entity) {
        return null;
    }

    @Override
    public BaseEquipment updateReturnEntity(BaseEquipment entity) {
        return null;
    }

    @Override
    public Result add(String userId, String equipment, String remark) {
        //查重
        QueryWrapper<BaseEquipment> qw = new QueryWrapper<>();
        qw.eq("equipment", equipment);
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseEquipment b = new BaseEquipment();
        b.setId(UUIDUtil.getShortUUID());
        b.setEquipment(equipment);
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

    @Override
    public Result edit(String userId, String equipment, String remark, String equipmentId) {
        //根据id获取名称
        QueryWrapper<BaseEquipment> qw = new QueryWrapper<>();
        qw.eq("id", equipmentId);
        qw.eq("deleted", "0");
        qw.select("equipment");
        BaseEquipment ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getEquipment().equals(equipment)) {
            //查重
            qw.clear();
            qw.eq("equipment", equipment);
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        BaseEquipment b = new BaseEquipment();
        b.setId(equipmentId);
        b.setEquipment(equipment);
        b.setRemark(remark);
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result deleteEquipment(String userId, String id) {
        //检查是否有未删除的动作使用该器材
        int aCount = baseActionEquipmentMapper.selectActionCountByEquipId(id);
        if (aCount != 0) {
            return ResultUtil.error(ResultEnum.Equip_ERROR);
        }

        BaseEquipment b = new BaseEquipment();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getEquipment(String id) {
        QueryWrapper<BaseEquipment> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        qw.select("id", "equipment", "remark");
        BaseEquipment b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    @Override
    public Result getEquipmentList(String equipment, String currentPage) {
        QueryWrapper<BaseEquipment> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(equipment)) {
            qw.like("equipment", equipment);
        }
        qw.select("id", "equipment", "remark");
        qw.eq("deleted", "0");
        qw.orderByAsc("length(equipment)","equipment");
        List<BaseEquipment> list;
        EquipmentListDto equipmentListDto = new EquipmentListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            equipmentListDto.setTotal(list.size());
        } else {
            //分页
            Page<BaseEquipment> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            list = partPage.getRecords();
            equipmentListDto.setTotal((int) partPage.getTotal());
        }
        equipmentListDto.setEquipmentList(list);

        /*EquipmentListDto equipmentListDto = new EquipmentListDto();
        List<GoodEquipment> list = goodEquipmentMapper.getList();
        equipmentListDto.setEquipmentList(list);*/
        //成功
        return ResultUtil.success(equipmentListDto);
    }
}
