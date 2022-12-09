package com.knd.manage.equip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.equip.dto.ReportInfoListDto;
import com.knd.manage.equip.entity.EquipmentReportInfo;
import com.knd.manage.equip.mapper.EquipmentReportInfoMapper;
import com.knd.manage.equip.service.IEquipmentReportInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
@Service
@Transactional
public class EquipmentReportInfoServiceImpl extends ServiceImpl<EquipmentReportInfoMapper, EquipmentReportInfo> implements IEquipmentReportInfoService {

    @Override
    public EquipmentReportInfo insertReturnEntity(EquipmentReportInfo entity) {
        return null;
    }

    @Override
    public EquipmentReportInfo updateReturnEntity(EquipmentReportInfo entity) {
        return null;
    }

    @Override
    public Result saveReportInfo(String userId, String equipmentNo, String turnOnTime, String hardVersion,
                                 String mainboardVersion, String positionInfo, String appVersion) {
        EquipmentReportInfo e = new EquipmentReportInfo();
        e.setId(UUIDUtil.getShortUUID());
        e.setEquipmentNo(equipmentNo);
        e.setTurnOnTime(turnOnTime);
        e.setHardVersion(hardVersion);
        e.setMainboardVersion(mainboardVersion);
        e.setPositionInfo(positionInfo);
        e.setAppVersion(appVersion);
        e.setCreateUser(userId);
        e.setCreateTime(LocalDateTime.now());
        e.setDeleted("0");
        baseMapper.insert(e);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getReportInfoList(String equipmentNo, String current) {
        QueryWrapper<EquipmentReportInfo> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(equipmentNo)) {
            qw.like("equipmentNo", equipmentNo);
        }
        qw.select("id", "equipmentNo", "turnOnTime", "hardVersion", "mainboardVersion", "appVersion", "positionInfo");
        qw.eq("deleted", "0");
        qw.orderByDesc("str_to_date(turnOnTime,'%Y-%m-%d %H:%i:%s')", "equipmentNo");
        List<EquipmentReportInfo> list;
        ReportInfoListDto reportInfoListDto = new ReportInfoListDto();
        if (StringUtils.isEmpty(current)) {
            //获取全部
            list = baseMapper.selectList(qw);
            reportInfoListDto.setTotal(list.size());
        } else {
            //分页
            Page<EquipmentReportInfo> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            list = partPage.getRecords();
            reportInfoListDto.setTotal((int) partPage.getTotal());
        }
        reportInfoListDto.setReportInfoList(list);
        //成功
        return ResultUtil.success(reportInfoListDto);
    }
}
