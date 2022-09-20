package com.knd.manage.user.mapper;

import com.knd.manage.user.entity.UserTrainLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-15
 */
public interface UserTrainLevelMapper extends BaseMapper<UserTrainLevel> {
    //根据用户id,获取最新的训练周期信息
    UserTrainLevel selectNewestByUserid(String userId);

}
