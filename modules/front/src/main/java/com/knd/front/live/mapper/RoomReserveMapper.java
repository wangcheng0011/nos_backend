package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.live.dto.RoomListDto;
import com.knd.front.live.entity.RoomEntity;
import com.knd.front.live.entity.RoomReserveEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface RoomReserveMapper extends BaseMapper<RoomReserveEntity> {

    @Select("SELECT ltg.*,u.id userId,u.nickName userName,ac.filePath headPicUrl from lb_room ltg " +
            "LEFT JOIN `user` u ON u.id = ltg.userId " +
            "LEFT JOIN user_detail ud ON ud.userId = u.id " +
            "LEFT JOIN attach ac ON ac.id = ud.headPicUrlId " +
            "WHERE EXISTS(select roomId from lb_room_reserve where userId = #{userId}) " +
            "AND date_format(ltg.beginDate ,'%Y-%m-%d') =date_format(NOW() ,'%Y-%m-%d') ")
    List<RoomListDto> roomList(String userId);
}
