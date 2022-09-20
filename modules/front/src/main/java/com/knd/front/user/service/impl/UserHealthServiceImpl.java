package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.user.dto.HealthByDateDto;
import com.knd.front.user.dto.UserHealthDto;
import com.knd.front.user.dto.UserHealthListDto;
import com.knd.front.user.entity.UserHealthEntity;
import com.knd.front.user.mapper.UserHealthMapper;
import com.knd.front.user.request.UserHealthRequest;
import com.knd.front.user.service.IUserHealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserHealthServiceImpl implements IUserHealthService {
    private final UserDetailMapper userDetailMapper;
    private final UserHealthMapper userHealthMapper;

    @Override
    public Result addOrUpdate(String userId, UserHealthRequest vo) {
        log.info("addOrUpdate userId:{{}}",userId);
        log.info("addOrUpdate UserHealthRequest:{{}}",vo);
        QueryWrapper<UserHealthEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("date",vo.getDate());
        wrapper.eq("userId",userId);
        wrapper.last("limit 1");
        UserHealthEntity entity = userHealthMapper.selectOne(wrapper);
        log.info("addOrUpdate entity:{{}}",entity);
        if (entity != null) {
            BeanUtils.copyProperties(vo,entity);
            Double.valueOf(vo.getCurrentWeight());
            Double.valueOf(vo.getHeight());
            entity.setBmi(String.format("%.2f", Double.valueOf(vo.getCurrentWeight()) / Math.pow(Double.valueOf(vo.getHeight())/100, 2)));
            log.info("addOrUpdate entity:{{}}",entity);
            log.info("addOrUpdate Bmi:{{}}",String.format("%.2f", Double.valueOf(vo.getCurrentWeight()) / Math.pow(Double.valueOf(vo.getHeight())/100, 2)));
            //更新操作
            entity.setLastModifiedBy(userId);
            entity.setLastModifiedDate(LocalDateTime.now());
            log.info("addOrUpdate 更新操作entity:{{}}",entity);
            userHealthMapper.updateById(entity);
        }else{
            entity = new UserHealthEntity();
            entity.setBmi(String.format("%.2f", Double.valueOf(vo.getCurrentWeight()) / Math.pow(Double.valueOf(vo.getHeight())/100, 2)));
            log.info("addOrUpdate entity:{{}}",entity);
            log.info("addOrUpdate Bmi:{{}}",String.format("%.2f", Double.valueOf(vo.getCurrentWeight()) / Math.pow(Double.valueOf(vo.getHeight())/100, 2)));
            BeanUtils.copyProperties(vo,entity);
            //插入操作
            entity.setId(UUIDUtil.getShortUUID());
            entity.setUserId(userId);
            entity.setDate(vo.getDate());
            entity.setCreateBy(userId);
            entity.setCreateDate(LocalDateTime.now());
            entity.setDeleted("0");
            entity.setLastModifiedBy(userId);
            entity.setLastModifiedDate(LocalDateTime.now());
            log.info("addOrUpdate 新增操作entity:{{}}",entity);
            userHealthMapper.insert(entity);
        }
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.eq("userId",userId);
        Integer integer = userDetailMapper.selectCount(queryWrapper);
        log.info("addOrUpdate userDetailCount:{{}}",integer);
        if (integer > 0) {
            UserDetail userDetail = userDetailMapper.selectOne(queryWrapper);
            userDetail.setHeight(StringUtils.isNotEmpty(vo.getHeight()) ? vo.getHeight() : userDetail.getHeight());
            userDetail.setWeight(StringUtils.isNotEmpty(vo.getCurrentWeight()) ? vo.getCurrentWeight() : userDetail.getWeight());
           //userDetail.setBmi(StringUtils.isNotEmpty(vo.getBmi()) ? new BigDecimal(vo.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() : new BigDecimal(userDetail.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userDetail.setBmi(String.format("%.2f", Double.valueOf(vo.getCurrentWeight()) / Math.pow(Double.valueOf(vo.getHeight())/100, 2)));
            log.info("addOrUpdate userDetail:{{}}",userDetail);
            userDetailMapper.updateById(userDetail);
        }
        return ResultUtil.success();
    }

    @Override
    public Result getHealth(String userId) {
        QueryWrapper<UserHealthEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        wrapper.orderByDesc("date");
        wrapper.last("limit 1");
        UserHealthEntity entity = userHealthMapper.selectOne(wrapper);
        UserHealthDto dto = new UserHealthDto();
        if(entity != null){
            BeanUtils.copyProperties(entity,dto);
            dto.setBmi(new BigDecimal(entity.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            if (StringUtils.isEmpty(dto.getTargetWeight())){
                UserHealthEntity newEntity = getEntity(userId,"targetWeight");
                dto.setTargetWeight(newEntity.getTargetWeight());
            }
            if (StringUtils.isEmpty(dto.getTargetHeight())){
                UserHealthEntity newEntity = getEntity(userId,"targetHeight");
                dto.setTargetHeight(newEntity.getTargetHeight());
            }
            if (StringUtils.isEmpty(dto.getTargetBust())){
                UserHealthEntity newEntity = getEntity(userId,"targetBust");
                dto.setTargetBust(newEntity.getTargetBust());
            }
            if (StringUtils.isEmpty(dto.getTargetWaist())){
                UserHealthEntity newEntity = getEntity(userId,"targetWaist");
                dto.setTargetWaist(newEntity.getTargetWaist());
            }
            if (StringUtils.isEmpty(dto.getTargetHipline())){
                UserHealthEntity newEntity = getEntity(userId,"targetHipline");
                dto.setTargetHipline(newEntity.getTargetHipline());
            }
            if (StringUtils.isEmpty(dto.getTargetArmCircumference())){
                UserHealthEntity newEntity = getEntity(userId,"targetArmCircumference");
                dto.setTargetArmCircumference(newEntity.getTargetArmCircumference());
            }
            if (StringUtils.isEmpty(dto.getBmi())){
                UserHealthEntity newEntity = getEntity(userId,"bmi");
                dto.setBmi(new BigDecimal(newEntity.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            if (StringUtils.isEmpty(dto.getCurrentWeight())){
                UserHealthEntity newEntity = getEntity(userId,"currentWeight");
                dto.setCurrentWeight(newEntity.getCurrentWeight());
            }
            if (StringUtils.isEmpty(dto.getHeight())){
                UserHealthEntity newEntity = getEntity(userId,"height");
                dto.setHeight(newEntity.getHeight());
            }
            if (StringUtils.isEmpty(dto.getBust())){
                UserHealthEntity newEntity = getEntity(userId,"bust");
                dto.setBust(newEntity.getBust());
            }
            if (StringUtils.isEmpty(dto.getWaist())){
                UserHealthEntity newEntity = getEntity(userId,"waist");
                dto.setWaist(newEntity.getWaist());
            }
            if (StringUtils.isEmpty(dto.getHipline())){
                UserHealthEntity newEntity = getEntity(userId,"hipline");
                dto.setHipline(newEntity.getHipline());
            }
            if (StringUtils.isEmpty(dto.getArmCircumference())){
                UserHealthEntity newEntity = getEntity(userId,"armCircumference");
                dto.setArmCircumference(newEntity.getArmCircumference());
            }
        }
        return ResultUtil.success(dto);
    }

    @Override
    public Result getHealthByDate(String userId, LocalDate beginDate, LocalDate endDate) {
        UserHealthListDto dto = new UserHealthListDto();
        List<HealthByDateDto> currentWeightList = userHealthMapper.getHealthByDateList("currentWeight", userId, beginDate, endDate);
        List<HealthByDateDto> heightList = userHealthMapper.getHealthByDateList("height", userId, beginDate, endDate);
        List<HealthByDateDto> bustList = userHealthMapper.getHealthByDateList("bust", userId, beginDate, endDate);
        List<HealthByDateDto> waistList = userHealthMapper.getHealthByDateList("waist", userId, beginDate, endDate);
        List<HealthByDateDto> hiplineList = userHealthMapper.getHealthByDateList("hipline", userId, beginDate, endDate);
        List<HealthByDateDto> armCircumferenceList = userHealthMapper.getHealthByDateList("armCircumference", userId, beginDate, endDate);
        dto.setCurrentWeightList(currentWeightList);
        dto.setHeightList(heightList);
        dto.setBustList(bustList);
        dto.setWaistList(waistList);
        dto.setHiplineList(hiplineList);
        dto.setArmCircumferenceList(armCircumferenceList);
        return ResultUtil.success(dto);
    }

    public UserHealthEntity getEntity(String userId ,String field){
        QueryWrapper<UserHealthEntity> wrapper = new QueryWrapper<>();
        wrapper.clear();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        wrapper.ne(field,"");
        wrapper.orderByDesc("date");
        wrapper.last("limit 1");
        UserHealthEntity entity = userHealthMapper.selectOne(wrapper);
        return entity!=null ? entity : new UserHealthEntity();
    }




}
