package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.user.dto.UserDetailDto;
import com.knd.front.user.entity.PowerTestEntity;
import com.knd.front.user.entity.PowerTestItemEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author will
 * @date 2021/8/10 21:40
 */
public interface PowerTestItemMapper extends BaseMapper<PowerTestItemEntity> {

    @Select("select targetId,shapeId,hobbyId from user_detail where userId = #{userId}")
    UserDetailDto getUserDetail(@Param("userId") String userId);
}
