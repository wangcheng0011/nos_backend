package com.knd.manage.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knd.manage.mall.entity.UserReceiveAddressEntity;
import com.knd.manage.mall.request.UserReceiveAddressRequest;

/**
 * 会员收货地址
 *
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-08-06 16:14:34
 */
public interface IUserReceiveAddressService extends IService<UserReceiveAddressEntity> {

    String add(UserReceiveAddressRequest userReceiveAddressRequest);
}

