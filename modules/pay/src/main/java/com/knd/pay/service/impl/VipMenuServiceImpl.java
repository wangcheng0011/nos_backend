package com.knd.pay.service.impl;

import com.knd.pay.entity.VipMenu;
import com.knd.pay.mapper.VipMenuMapper;
import com.knd.pay.service.IVipMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员套餐表  服务实现类
 * </p>
 *
 * @author will
 * @since 2021-01-13
 */
@Service
public class VipMenuServiceImpl extends ServiceImpl<VipMenuMapper, VipMenu> implements IVipMenuService {

    @Override
    public VipMenu insertReturnEntity(VipMenu entity) {
        return null;
    }

    @Override
    public VipMenu updateReturnEntity(VipMenu entity) {
        return null;
    }
}
