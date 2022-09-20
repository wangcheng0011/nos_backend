package com.knd.manage.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.mall.mapper.UserHealthMapper;
import com.knd.manage.user.dto.UserHealthEntity;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.entity.UserDetail;
import com.knd.manage.user.mapper.UserDetailMapper;
import com.knd.manage.user.mapper.UserMapper;
import com.knd.manage.user.request.UserDetailRequest;
import com.knd.manage.user.service.IUserDetailService;
import com.knd.manage.user.service.feignInterface.GetUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Service
@Slf4j
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail> implements IUserDetailService {

     @Resource
    private GetUserDetailService getUserDetailService;
     @Resource
    private AttachMapper attachMapper;

     @Resource
    private UserMapper userMapper;

     @Resource
    private UserDetailMapper userDetailMapper;

     @Resource
    private UserHealthMapper userHealthMapper;

     @Resource
    private IAttachService attachService;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;



    @Override
    public UserDetail insertReturnEntity(UserDetail entity) {
        return null;
    }

    @Override
    public UserDetail updateReturnEntity(UserDetail entity) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addOrUpdateUserDetail(UserDetailRequest userDetailRequest) {
        log.info("addOrupd userDetailRequest:{{}}"+userDetailRequest);
        User user = null;
        if(!StringUtils.isEmpty(userDetailRequest.getPassword())) {
            user = new User();
            user.setId(userDetailRequest.getUserId());
            user.setNickName(userDetailRequest.getNickName());
            user.setPassword(Md5Utils.md5(userDetailRequest.getPassword()));
            userMapper.updateById(user);
        }else if(StringUtils.isNotEmpty(userDetailRequest.getNickName())&&StringUtils.isEmpty(userDetailRequest.getPassword())) {
            user = new User();
            user.setId(userDetailRequest.getUserId());
            user.setNickName(userDetailRequest.getNickName());
            userMapper.updateById(user);
        }

        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.eq("userId",userDetailRequest.getUserId());
        queryWrapper.last("limit 1");
        UserDetail userDetail1 = userDetailMapper.selectOne(queryWrapper);

        if(StringUtils.isEmpty(userDetailRequest.getHeadPicUrlId())
                && StringUtils.isNotEmpty(userDetailRequest.getHeadAttachUrl())){
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
    //查询注册会员详情
    @Override
    public Result getUserDetail(String userId) {
        return getUserDetailService.getUserDetail(userId);
    }

    @Override
    public String getHeadUrl(String userId) {
        String headPicUr = "";
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("deleted",0);
        queryWrapper.select("headPicUrlId");
        queryWrapper.last("limit 1");
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
