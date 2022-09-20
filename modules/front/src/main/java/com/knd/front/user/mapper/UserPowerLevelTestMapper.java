package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.UserPowerLevelTest;
import com.knd.front.user.dto.UserPowerLevelDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
public interface UserPowerLevelTestMapper extends BaseMapper<UserPowerLevelTest> {
    UserPowerLevelDto getUserPowerLevels(@Param("userId") String userId);
}
