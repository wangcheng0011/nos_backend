package com.knd.front.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.VipEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.entity.Attach;
import com.knd.front.entity.User;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.dto.UserDetailDto;
import com.knd.front.login.mapper.ShapeAndHobbyMapper;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.request.UserDetailRequest;
import com.knd.front.login.service.IUserDetailService;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.user.entity.UserHealthEntity;
import com.knd.front.user.mapper.UserHealthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-01
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail> implements IUserDetailService {

    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final ShapeAndHobbyMapper shapeAndHobbyMapper;
    private final AttachMapper attachMapper;
    private final UserHealthMapper userHealthMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    private final AttachService attachService;


    @Override
    public UserDetail insertReturnEntity(UserDetail entity) {
        return null;
    }

    @Override
    public UserDetail updateReturnEntity(UserDetail entity) {
        return null;
    }


    @Transactional(rollbackFor = Exception.class)
    public Result addOrUpdateUserDetail(UserDetailRequest userDetailRequest) {
        log.info("addOrupd userDetailRequest:{{}}"+userDetailRequest);
        User user = null;
        if(!StringUtils.isEmpty(userDetailRequest.getPassword())) {
            user = new User();
            user.setId(userDetailRequest.getUserId());
            user.setNickName(userDetailRequest.getNickName());
            user.setPassword(Md5Utils.md5(userDetailRequest.getPassword()));
            user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
            userMapper.updateById(user);
        }else if(StringUtils.isNotEmpty(userDetailRequest.getNickName())&&StringUtils.isEmpty(userDetailRequest.getPassword())) {
            user = new User();
            user.setId(userDetailRequest.getUserId());
            user.setNickName(userDetailRequest.getNickName());
            user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
            userMapper.updateById(user);
        }

        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.eq("userId",userDetailRequest.getUserId());
        queryWrapper.last("limit 1");
        UserDetail userDetail1 = userDetailMapper.selectOne(queryWrapper);

        if(StringUtils.isEmpty(userDetailRequest.getHeadPicUrlId())
                && userDetailRequest.getHeadAttachUrl()!=null
                && userDetailRequest.getHeadAttachUrl().getPicAttachSize()!=null
                && userDetailRequest.getHeadAttachUrl().getPicAttachNewName()!=null
                && userDetailRequest.getHeadAttachUrl().getPicAttachName()!=null
        ){
            //保存选中图片
            Attach attach = attachService.saveAttach(userDetailRequest.getUserId(), userDetailRequest.getHeadAttachUrl().getPicAttachName()
                    , userDetailRequest.getHeadAttachUrl().getPicAttachNewName(), userDetailRequest.getHeadAttachUrl().getPicAttachSize());
            String attachId = attach.getId();
            userDetailRequest.setHeadPicUrlId(attachId);
        }

        if (StringUtils.isNotEmpty(userDetail1)) {
            BeanUtils.copyProperties(userDetailRequest,userDetail1);
            userDetailMapper.updateById(userDetail1);
        }else {
            UserDetail userDetail = new UserDetail();
            userDetail.setBirthDay(userDetailRequest.getBirthDay());
            userDetail.setBmi(userDetailRequest.getBmi());
            userDetail.setGender(userDetailRequest.getGender());
            userDetail.setHeight(userDetailRequest.getHeight());
            userDetail.setWeight(userDetailRequest.getWeight());
            userDetail.setTarget(userDetailRequest.getTarget());
            userDetail.setTargetId(userDetailRequest.getTargetId());
            userDetail.setTrainHisFlag(userDetailRequest.getTrainHisFlag());
            userDetail.setUserId(userDetailRequest.getUserId());
            userDetail.setShapeId(userDetailRequest.getShapeId());
            userDetail.setHobbyId(userDetailRequest.getHobbyId());
            userDetail.setSportId(userDetailRequest.getSportId());
            log.info("sportId:{{}}"+userDetailRequest.getSportId());
            userDetail.setFrequencyId(userDetailRequest.getFrequencyId());
            log.info("frequencyId:{{}}"+userDetailRequest.getFrequencyId());
            System.out.println("sportId:"+userDetail.getSportId());
            System.out.println("frequencyId:"+userDetail.getFrequencyId());
            userDetail.setPerSign(userDetailRequest.getPerSign());
            userDetail.setBmi(new BigDecimal(userDetailRequest.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userDetail.setHeadPicUrlId(userDetailRequest.getHeadPicUrlId());
            userDetailMapper.insert(userDetail);
        }

        LocalDate now = LocalDate.now();
        QueryWrapper<UserHealthEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("date", now);
        wrapper.eq("userId",userDetailRequest.getUserId());
        int count = userHealthMapper.selectCount(wrapper);
        UserHealthEntity userHealthEntity = new UserHealthEntity();
        if (count > 0) {
            userHealthEntity = userHealthMapper.selectOne(wrapper);
            userHealthEntity.setCurrentWeight(userDetailRequest.getWeight());
            userHealthEntity.setHeight(userDetailRequest.getHeight());
            userHealthEntity.setBmi(new BigDecimal(userDetailRequest.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userHealthMapper.updateById(userHealthEntity);
        }else{
            userHealthEntity.setId(UUIDUtil.getShortUUID());
            userHealthEntity.setUserId(userDetailRequest.getUserId());
            userHealthEntity.setCurrentWeight(userDetailRequest.getWeight());
            userHealthEntity.setHeight(userDetailRequest.getHeight());
            userHealthEntity.setBmi(new BigDecimal(userDetailRequest.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userHealthEntity.setDate(LocalDate.now());
            userHealthEntity.setCreateBy(userDetailRequest.getUserId());
            userHealthEntity.setCreateDate(LocalDateTime.now());
            userHealthEntity.setDeleted("0");
            userHealthEntity.setLastModifiedBy(userDetailRequest.getUserId());
            userHealthEntity.setLastModifiedDate(LocalDateTime.now());
            userHealthMapper.insert(userHealthEntity);
        }

        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateUserDetail(UserDetailRequest userDetailRequest) {
        log.info("addOrupd userDetailRequest:{{}}"+userDetailRequest);
        User user = null;
        if(!StringUtils.isEmpty(userDetailRequest.getPassword())) {
            user = new User();
            user.setId(userDetailRequest.getUserId());
            user.setNickName(userDetailRequest.getNickName());
            user.setPassword(Md5Utils.md5(userDetailRequest.getPassword()));
           // user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
            userMapper.updateById(user);
        }else if(StringUtils.isNotEmpty(userDetailRequest.getNickName())&&StringUtils.isEmpty(userDetailRequest.getPassword())) {
            user = new User();
            user.setId(userDetailRequest.getUserId());
            user.setNickName(userDetailRequest.getNickName());
           // user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
            userMapper.updateById(user);
        }

        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.eq("userId",userDetailRequest.getUserId());
        queryWrapper.last("limit 1");
        UserDetail userDetail1 = userDetailMapper.selectOne(queryWrapper);

        if(StringUtils.isEmpty(userDetailRequest.getHeadPicUrlId())
                && userDetailRequest.getHeadAttachUrl()!=null
                && userDetailRequest.getHeadAttachUrl().getPicAttachSize()!=null
                && userDetailRequest.getHeadAttachUrl().getPicAttachNewName()!=null
                && userDetailRequest.getHeadAttachUrl().getPicAttachName()!=null
        ){
            //保存选中图片
            Attach attach = attachService.saveAttach(userDetailRequest.getUserId(), userDetailRequest.getHeadAttachUrl().getPicAttachName()
                    , userDetailRequest.getHeadAttachUrl().getPicAttachNewName(), userDetailRequest.getHeadAttachUrl().getPicAttachSize());
            String attachId = attach.getId();
            userDetailRequest.setHeadPicUrlId(attachId);
        }

        if (StringUtils.isNotEmpty(userDetail1)) {
            BeanUtils.copyProperties(userDetailRequest,userDetail1);
            userDetailMapper.updateById(userDetail1);
        }else {
            UserDetail userDetail = new UserDetail();
            userDetail.setBirthDay(userDetailRequest.getBirthDay());
            userDetail.setBmi(userDetailRequest.getBmi());
            userDetail.setGender(userDetailRequest.getGender());
            userDetail.setHeight(userDetailRequest.getHeight());
            userDetail.setWeight(userDetailRequest.getWeight());
            userDetail.setTarget(userDetailRequest.getTarget());
            userDetail.setTargetId(userDetailRequest.getTargetId());
            userDetail.setTrainHisFlag(userDetailRequest.getTrainHisFlag());
            userDetail.setUserId(userDetailRequest.getUserId());
            userDetail.setShapeId(userDetailRequest.getShapeId());
            userDetail.setHobbyId(userDetailRequest.getHobbyId());
            userDetail.setSportId(userDetailRequest.getSportId());
            log.info("sportId:{{}}"+userDetailRequest.getSportId());
            userDetail.setFrequencyId(userDetailRequest.getFrequencyId());
            log.info("frequencyId:{{}}"+userDetailRequest.getFrequencyId());
            System.out.println("sportId:"+userDetail.getSportId());
            System.out.println("frequencyId:"+userDetail.getFrequencyId());
            userDetail.setPerSign(userDetailRequest.getPerSign());
            userDetail.setBmi(new BigDecimal(userDetailRequest.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userDetail.setHeadPicUrlId(userDetailRequest.getHeadPicUrlId());
            userDetailMapper.insert(userDetail);
        }

        LocalDate now = LocalDate.now();
        QueryWrapper<UserHealthEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("date", now);
        wrapper.eq("userId",userDetailRequest.getUserId());
        int count = userHealthMapper.selectCount(wrapper);
        UserHealthEntity userHealthEntity = new UserHealthEntity();
        if (count > 0) {
            userHealthEntity = userHealthMapper.selectOne(wrapper);
            userHealthEntity.setCurrentWeight(userDetailRequest.getWeight());
            userHealthEntity.setHeight(userDetailRequest.getHeight());
            userHealthEntity.setBmi(new BigDecimal(userDetailRequest.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userHealthMapper.updateById(userHealthEntity);
        }else{
            userHealthEntity.setId(UUIDUtil.getShortUUID());
            userHealthEntity.setUserId(userDetailRequest.getUserId());
            userHealthEntity.setCurrentWeight(userDetailRequest.getWeight());
            userHealthEntity.setHeight(userDetailRequest.getHeight());
            userHealthEntity.setBmi(new BigDecimal(userDetailRequest.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            userHealthEntity.setDate(LocalDate.now());
            userHealthEntity.setCreateBy(userDetailRequest.getUserId());
            userHealthEntity.setCreateDate(LocalDateTime.now());
            userHealthEntity.setDeleted("0");
            userHealthEntity.setLastModifiedBy(userDetailRequest.getUserId());
            userHealthEntity.setLastModifiedDate(LocalDateTime.now());
            userHealthMapper.insert(userHealthEntity);
        }

        return ResultUtil.success();
    }

    @Override
    public Result getUserDetail(String userId) {
        log.info("getUserDetail userId:{{}}",userId);
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("deleted",0);
        queryWrapper.select("gender", "birthDay", "bmi", "height", "weight", "trainHisFlag", "targetId", "target", "shapeId", "hobbyId", "perSign", "headPicUrlId", "hobbyId", "sportId", "frequencyId");
        queryWrapper.orderByAsc("hobbyId");
        queryWrapper.orderByAsc("sportId");
        queryWrapper.orderByAsc("frequencyId");
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        userQueryWrapper.eq("deleted",0);
        userQueryWrapper.select("mobile","nickName",
                "vipStatus","masterId","vipBeginDate","vipEndDate");
        User user = userMapper.selectOne(userQueryWrapper);
        log.info("getUserDetail user:{{}}",user);
        if (user==null){
            user = new User();
        }
        UserDetail userDetail = baseMapper.selectOne(queryWrapper);
        log.info("getUserDetail userDetail:{{}}",userDetail);
        if (userDetail==null){
            userDetail = new UserDetail();
        }
        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setMobile(user.getMobile());
        userDetailDto.setNickName(user.getNickName());
        userDetailDto.setVipStatus(user.getVipStatus());
        userDetailDto.setVipBeginDate(user.getVipBeginDate());
        userDetailDto.setVipEndDate(user.getVipEndDate());
        if (StringUtils.isNotEmpty(userDetail)){
            userDetailDto.setGender(userDetail.getGender());
            userDetailDto.setBirthDay(userDetail.getBirthDay());
            userDetailDto.setHeight(userDetail.getHeight());
            userDetailDto.setWeight(userDetail.getWeight());
            if(StringUtils.isNotEmpty(userDetail.getBmi())){
                userDetailDto.setBmi(new BigDecimal(userDetail.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            userDetailDto.setTrainHisFlag(userDetail.getTrainHisFlag());
            userDetailDto.setTargetId(userDetail.getTargetId());
            userDetailDto.setTarget(userDetail.getTarget());
            userDetailDto.setShapeId(userDetail.getShapeId());
            log.info("getUserDetail Shape:{{}}",shapeAndHobbyMapper.getShape(userDetail.getShapeId()));
            userDetailDto.setShape(shapeAndHobbyMapper.getShape(userDetail.getShapeId()));
            userDetailDto.setSportId(userDetail.getSportId());
            userDetailDto.setFrequencyId(userDetail.getFrequencyId());
            if(StringUtils.isNotEmpty(userDetail.getHobbyId())){
                String hobbyIds = userDetail.getHobbyId();
                String[] split = hobbyIds.split(",");
                List<String> hobbyList = Arrays.asList(split);
                userDetailDto.setHobbyId(hobbyList);
            }
            userDetailDto.setPerSign(userDetail.getPerSign());
            if(StringUtils.isNotEmpty(userDetail.getHeadPicUrlId())){
                userDetailDto.setHeadPicUrlId(userDetail.getHeadPicUrlId());
                Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
                log.info("getUserDetail attach:{{}}",attach);
                if(attach!=null){
                    userDetailDto.setHeadPicUrl(fileImagesPath+attach.getFilePath());
                    log.info("getUserDetail HeadPicUrl:{{}}",fileImagesPath+attach.getFilePath());
                }
            }

        }
        log.info("getUserDetail userDetailDto:{{}}",userDetailDto);
        return ResultUtil.success(userDetailDto);
    }

    @Override
    public String getHeadUrl(String userId) {
        String headPicUr = "";
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("deleted",0);
        queryWrapper.select("headPicUrlId");
        UserDetail userDetail = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotEmpty(userDetail)){
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            if(attach!=null){
                headPicUr = fileImagesPath+attach.getFilePath();
            }
        }
        return headPicUr;
    }
}
