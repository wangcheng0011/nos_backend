package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.BaseAction;
import com.knd.front.user.dto.UserTestActionDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface BaseActionMapper extends BaseMapper<BaseAction> {

    UserTestActionDto getUserTestActionDto(@Param("actionTypeId") String actionTypeId);

}
