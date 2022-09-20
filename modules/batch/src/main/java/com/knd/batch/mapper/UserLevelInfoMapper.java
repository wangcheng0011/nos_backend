package com.knd.batch.mapper;

import com.knd.batch.dto.StarCountAndBufferDayCountDto;
import com.knd.batch.entity.UserLevelInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-08-13
 */
public interface UserLevelInfoMapper extends BaseMapper<UserLevelInfo> {

    //根据用户id获取最新的周日期信息【user_train_level表获取】
    UserLevelInfo selectNewestById(String userId);

    //根据用户id和训练周期id获取当前星星总数
    StarCountAndBufferDayCountDto selectStarCountBySome(@Param("userId") String userId, @Param("userTrainLevelId") String userTrainLevelId);


}
