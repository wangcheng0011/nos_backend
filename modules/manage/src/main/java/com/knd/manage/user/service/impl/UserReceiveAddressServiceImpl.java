package com.knd.manage.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.mall.entity.UserReceiveAddressEntity;
import com.knd.manage.mall.mapper.UserReceiveAddressMapper;
import com.knd.manage.mall.request.UserReceiveAddressRequest;
import com.knd.manage.user.service.IUserReceiveAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * @author will
 */
@Service
public class UserReceiveAddressServiceImpl extends ServiceImpl<UserReceiveAddressMapper, UserReceiveAddressEntity> implements IUserReceiveAddressService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(UserReceiveAddressRequest userReceiveAddressRequest) {
        UserReceiveAddressEntity userReceiveAddressEntity = new UserReceiveAddressEntity();
        BeanUtils.copyProperties(userReceiveAddressRequest,userReceiveAddressEntity);
        Integer count = baseMapper.selectCount(new QueryWrapper<UserReceiveAddressEntity>()
                .eq("phone", userReceiveAddressRequest.getPhone()).isNull("userId"));
        if(count<=0) {
            userReceiveAddressEntity.setDefaultStatus("1");
        }else{
            userReceiveAddressEntity.setDefaultStatus("0");
        }
        userReceiveAddressEntity.setId(UUIDUtil.getShortUUID());
        userReceiveAddressEntity.setCreateBy(userReceiveAddressRequest.getUserId());
        userReceiveAddressEntity.setCreateDate(LocalDateTime.now());
        userReceiveAddressEntity.setDeleted("0");
        baseMapper.insert(userReceiveAddressEntity);
        return userReceiveAddressEntity.getId();
    }
}