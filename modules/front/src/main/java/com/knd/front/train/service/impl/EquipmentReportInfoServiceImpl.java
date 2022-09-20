package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.entity.AmapAdcodeEntity;
import com.knd.front.entity.AmapDataEntity;
import com.knd.front.entity.EquipmentReportInfo;
import com.knd.front.train.mapper.AmapAdcodeMapper;
import com.knd.front.train.mapper.AmapDataMapper;
import com.knd.front.train.mapper.EquipmentReportInfoMapper;
import com.knd.front.train.request.EquipmentReportInfoRequest;
import com.knd.front.train.service.IEquipmentReportInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-08
 */
@Service
@Log4j2
public class EquipmentReportInfoServiceImpl extends ServiceImpl<EquipmentReportInfoMapper, EquipmentReportInfo> implements IEquipmentReportInfoService {
    @Resource
    private EquipmentReportInfoMapper equipmentReportInfoMapper;
    @Resource
    private AmapDataMapper amapDataMapper;
    @Resource
    private AmapAdcodeMapper amapAdcodeMapper;


    @Override

    public Result commitEquipmentReportInfo(EquipmentReportInfoRequest equipmentReportInfoRequest) {
        EquipmentReportInfo equipmentReportInfo = new EquipmentReportInfo();
        equipmentReportInfo.setId(UUIDUtil.getShortUUID());
        equipmentReportInfo.setEquipmentNo(equipmentReportInfoRequest.getEquipmentNo());
//        equipmentReportInfo.setTurnOnTime(equipmentReportInfoRequest.getTurnOnTime());
        equipmentReportInfo.setTurnOnTime(DateUtils.getCurrentDateTimeStr());
        if (StringUtils.isNotEmpty(equipmentReportInfo.getTurnOffTime())){
            equipmentReportInfo.setTurnOffTime(equipmentReportInfoRequest.getTurnOffTime());
        }else{
            equipmentReportInfo.setTurnOffTime(null);
        }

        equipmentReportInfo.setHardVersion(equipmentReportInfoRequest.getHardVersion());
        equipmentReportInfo.setMainboardVersion(equipmentReportInfoRequest.getMainboardVersion());
        equipmentReportInfo.setAppVersion(equipmentReportInfoRequest.getAppVersion());
        equipmentReportInfo.setPositionInfo(equipmentReportInfoRequest.getPositionInfo());
        equipmentReportInfo.setCreateTime(DateUtils.getCurrentLocalDateTime());
        equipmentReportInfo.setCreateUser("user");
        equipmentReportInfo.setDeleted("0");
        equipmentReportInfoMapper.insert(equipmentReportInfo);

        AmapDataEntity amapDataEntity = amapDataMapper.selectOne(new QueryWrapper<AmapDataEntity>().eq("equipmentNo", equipmentReportInfoRequest.getEquipmentNo()).eq("deleted", 0));
        log.info("commitEquipmentReportInfo amapDataEntity:{{}}",amapDataEntity);
        if (StringUtils.isNotEmpty(amapDataEntity)){
            log.info("-----------------------------------设备信息上报 更新--------------------------------");
            BeanUtils.copyProperties(equipmentReportInfoRequest, amapDataEntity);
            //前端获取得是省名称，所以这里需要转成省代码
            AmapAdcodeEntity amapAdcodeEntity = amapAdcodeMapper.selectOne(new QueryWrapper<AmapAdcodeEntity>().eq("name", equipmentReportInfoRequest.getProvinceAdcode()).eq("deleted", 0).last(" limit 0,1"));
            log.info("commitEquipmentReportInfo amapAdcodeEntity:{{}}",amapAdcodeEntity);
            amapDataEntity.setProvinceAdcode(amapAdcodeEntity.getAdcode());
            //前端获取得是市邮政编码，所以这里需要转成市行政编码
            AmapAdcodeEntity amapAdCityCodeEntity= amapAdcodeMapper.selectOne(new QueryWrapper<AmapAdcodeEntity>().eq("citycode", equipmentReportInfoRequest.getCityAdcode()).eq("deleted", 0).last(" limit 0,1"));
            log.info("commitEquipmentReportInfo amapAdCityCodeEntity:{{}}",amapAdCityCodeEntity);
            amapDataEntity.setCityAdcode(amapAdCityCodeEntity.getAdcode());
            amapDataEntity.setLastModifiedBy("userId");
            amapDataEntity.setLastModifiedDate(LocalDateTime.now());
            log.info("commitEquipmentReportInfo amapDataEntity:{{}}",amapDataEntity);
            amapDataMapper.updateById(amapDataEntity);
        }else {
            log.info("-----------------------------------设备信息上报 新增--------------------------------");
            amapDataEntity = new AmapDataEntity();
            amapDataEntity.setId(UUIDUtil.getShortUUID());
            BeanUtils.copyProperties(equipmentReportInfoRequest, amapDataEntity);

            //前端获取得是省名称，所以这里需要转成省代码
            AmapAdcodeEntity amapAdcodeEntity = amapAdcodeMapper.selectOne(new QueryWrapper<AmapAdcodeEntity>().eq("name", equipmentReportInfoRequest.getProvinceAdcode()).eq("deleted", 0).last(" limit 0,1"));
            log.info("commitEquipmentReportInfo amapAdcodeEntity:{{}}",amapAdcodeEntity);
            amapDataEntity.setProvinceAdcode(amapAdcodeEntity.getAdcode());
            //前端获取得是市邮政编码，所以这里需要转成市行政编码
            AmapAdcodeEntity amapAdCityCodeEntity= amapAdcodeMapper.selectOne(new QueryWrapper<AmapAdcodeEntity>().eq("citycode", equipmentReportInfoRequest.getCityAdcode()).eq("deleted", 0).last(" limit 0,1"));
            log.info("commitEquipmentReportInfo amapAdCityCodeEntity:{{}}",amapAdCityCodeEntity);
            amapDataEntity.setCityAdcode(amapAdCityCodeEntity.getAdcode());
            // TODO: 2021/9/10 数据未插入，需要调试
            amapDataEntity.setCreateBy("userId");
            amapDataEntity.setCreateDate(LocalDateTime.now());
            amapDataEntity.setDeleted("0");
            amapDataEntity.setLastModifiedBy("userId");
            amapDataEntity.setLastModifiedDate(LocalDateTime.now());
            log.info("commitEquipmentReportInfo amapDataEntity:{{}}",amapDataEntity);
            amapDataMapper.insert(amapDataEntity);
        }
        return ResultUtil.success();
    }
}
