package com.knd.batch.mapper;

import com.knd.batch.entity.UserTrainLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-08-13
 */
public interface UserTrainLevelMapper extends BaseMapper<UserTrainLevel> {

    //根据用户id,获取最新的训练周期信息
    UserTrainLevel selectNewestByUserid(String userId);
}
