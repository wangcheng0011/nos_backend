package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.entity.Attach;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.train.dto.FreeTrainDetailDto;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.train.service.ICourseDetailService;
import com.knd.front.user.dto.PowerTestDto;
import com.knd.front.user.dto.PowerTestItemDto;
import com.knd.front.user.entity.PowerTestEntity;
import com.knd.front.user.entity.PowerTestItemEntity;
import com.knd.front.user.entity.PowerTestRecommendEntity;
import com.knd.front.user.entity.PowerTestResultEntity;
import com.knd.front.user.mapper.PowerTestItemMapper;
import com.knd.front.user.mapper.PowerTestMapper;
import com.knd.front.user.mapper.PowerTestRecommendMapper;
import com.knd.front.user.mapper.PowerTestResultMapper;
import com.knd.front.user.request.PowerTestResultDetailRequest;
import com.knd.front.user.request.PowerTestResultRequest;
import com.knd.front.user.service.IPowerTestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author will
 * @date 2021年08月11日 11:15
 */
@Service
@Transactional
public class PowerTestServiceImpl extends ServiceImpl<PowerTestMapper, PowerTestEntity> implements IPowerTestService {
    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private PowerTestItemMapper powerTestItemMapper;

    @Autowired
    private PowerTestResultMapper powerTestResultMapper;

    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private PowerTestRecommendMapper powerTestRecommendMapper;


    @Autowired
    private ICourseDetailService iCourseDetailService;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String FileImagesPath;


    @Override
    public PowerTestEntity insertReturnEntity(PowerTestEntity entity) {
        return null;
    }

    @Override
    public PowerTestEntity updateReturnEntity(PowerTestEntity entity) {
        return null;
    }

    @Override
    public List<PowerTestDto> getPowerTest() {
        String userId = UserUtils.getUserId();
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId",userId).eq("deleted", "0"));
        String gender = "1";
        if(userDetail != null&& !StringUtils.isEmpty(userDetail.getGender())) {
            gender = userDetail.getGender();
        }
        List<PowerTestEntity> powerTestEntities = baseMapper.selectList(
                new QueryWrapper<PowerTestEntity>()
                        .eq("deleted","0")
                        .eq("gender", gender).orderByAsc("length(sort)","sort"));
        List<PowerTestDto> powerTestDtos = new ArrayList<>();
        powerTestEntities.forEach(e->{
            PowerTestDto powerTestDto = new PowerTestDto();
            BeanUtils.copyProperties(e,powerTestDto);
            //根据id获取图片信息
            Attach aPi = attachMapper.selectById(e.getPicAttachId());
            if (aPi != null) {
                powerTestDto.setPicUrl(FileImagesPath + aPi.getFilePath());
            }
            ArrayList<PowerTestItemDto> powerTestItemDtos = new ArrayList<>();
            List<PowerTestItemEntity> powerTestItemEntities = powerTestItemMapper.selectList(new QueryWrapper<PowerTestItemEntity>()
                    .eq("deleted", "0")
                    .eq("powerTestId", e.getId()).orderByAsc("length(sort)","sort"));
            powerTestItemEntities.forEach(g->{
                PowerTestItemDto powerTestItemDto = new PowerTestItemDto();
                BeanUtils.copyProperties(g,powerTestItemDto);
                Result actionResult = iCourseDetailService.getFreeTrainDetail(g.getActionId(), UserUtils.getUserId());
                FreeTrainDetailDto freeTrainDetailDto = (FreeTrainDetailDto)actionResult.getData();
                powerTestItemDto.setFreeTrainDetailDto(freeTrainDetailDto);
                powerTestItemDtos.add(powerTestItemDto);
            });
            powerTestDto.setItems(powerTestItemDtos);
            powerTestDtos.add(powerTestDto);
        });
        return powerTestDtos;
    }

    @Override
    public Result addTestResult(PowerTestResultRequest request) {
        List<PowerTestResultEntity> resultEntityList = new ArrayList<>();
        for (PowerTestResultDetailRequest detail : request.getDetailRequestList()){
            QueryWrapper<PowerTestResultEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted","0");
            wrapper.eq("userId",request.getUserId());
            wrapper.eq("powerTestItemId",detail.getPowerTestItemId());
            PowerTestResultEntity resultEntity = powerTestResultMapper.selectOne(wrapper);
            if (StringUtils.isNotEmpty(resultEntity)){
                BeanUtils.copyProperties(detail,resultEntity);
                resultEntity.setLastModifiedBy(request.getUserId());
                resultEntity.setLastModifiedDate(LocalDateTime.now());
                powerTestResultMapper.updateById(resultEntity);
            }else {
                resultEntity = new PowerTestResultEntity();
                BeanUtils.copyProperties(detail,resultEntity);
                resultEntity.setId(UUIDUtil.getShortUUID());
                resultEntity.setUserId(request.getUserId());
                resultEntity.setCreateBy(request.getUserId());
                resultEntity.setCreateDate(LocalDateTime.now());
                resultEntity.setDeleted("0");
                resultEntity.setLastModifiedBy(request.getUserId());
                resultEntity.setLastModifiedDate(LocalDateTime.now());
                powerTestResultMapper.insert(resultEntity);
            }
            resultEntityList.add(resultEntity);
        }
        // 测试结果计算等级并保存
        calculationPowerTestLevel(resultEntityList);
        return ResultUtil.success();
    }

    private void calculationPowerTestLevel(List<PowerTestResultEntity> resultEntityList) {
        for(PowerTestResultEntity resultEntity : resultEntityList) {
            PowerTestItemEntity powerTestItemEntity = powerTestItemMapper.selectById(resultEntity.getPowerTestItemId());
            PowerTestEntity powerTestEntity = baseMapper.selectById(powerTestItemEntity.getPowerTestId());
            String difficultyId = powerTestEntity.getDifficultyId();
            PowerTestRecommendEntity powerTestRecommendEntity = new PowerTestRecommendEntity();
            powerTestRecommendEntity.setUserId(UserUtils.getUserId());
            switch (difficultyId) {
                case "1":
                    if(Integer.parseInt(resultEntity.getFinishedKpi())<Integer.parseInt(powerTestItemEntity.getKpa())) {
                        powerTestRecommendEntity.setRecommendDifficulty("1");
                    }
                    break;
                case "2":
                    if(Integer.parseInt(resultEntity.getFinishedKpi())<Integer.parseInt(powerTestItemEntity.getKpa())) {
                        powerTestRecommendEntity.setRecommendDifficulty("1");
                    }else{
                        powerTestRecommendEntity.setRecommendDifficulty("2");
                    }
                    break;
                case "3":
                    if(Integer.parseInt(resultEntity.getFinishedKpi())<Integer.parseInt(powerTestItemEntity.getKpa())) {
                        powerTestRecommendEntity.setRecommendDifficulty("2");
                    }else{
                        powerTestRecommendEntity.setRecommendDifficulty("3");
                    }
                    break;
                case "4":
                    if(Integer.parseInt(resultEntity.getFinishedKpi())<Integer.parseInt(powerTestItemEntity.getKpa())) {
                        powerTestRecommendEntity.setRecommendDifficulty("3");
                    }else{
                        powerTestRecommendEntity.setRecommendDifficulty("4");
                    }
                    break;
                default:
                    break;
            }
            if(!StringUtils.isEmpty(powerTestItemEntity.getBodyPartId())){
                powerTestRecommendEntity.setRecommendBodyPart(powerTestItemEntity.getBodyPartId());
            }
            PowerTestRecommendEntity recommendEntity = powerTestRecommendMapper.selectOne(new QueryWrapper<PowerTestRecommendEntity>().eq("userId", powerTestRecommendEntity.getUserId()).eq("deleted", "0"));
            if (StringUtils.isNotEmpty(recommendEntity)){
                recommendEntity.setRecommendBodyPart(powerTestRecommendEntity.getRecommendBodyPart());
                recommendEntity.setRecommendDifficulty(powerTestRecommendEntity.getRecommendDifficulty());
                powerTestRecommendMapper.updateById(recommendEntity);
            }else {
                powerTestRecommendMapper.insert(powerTestRecommendEntity);
            }
        }


    }


}
