package com.knd.manage.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.live.dto.TrainGroupDto;
import com.knd.manage.live.dto.TrainGroupListDto;
import com.knd.manage.live.entity.TrainGroupEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface TrainGroupMapper extends BaseMapper<TrainGroupEntity> {
    @Select("select ltg.*,u.nickName userName,bt.target targetName,ac.filePath headPicUrl from lb_train_group ltg " +
            "LEFT JOIN `user` u ON u.id = ltg.userId " +
            "LEFT JOIN user_detail ud ON ud.userId = u.id " +
            "LEFT JOIN attach ac ON ac.id = ud.headPicUrlId " +
            "LEFT JOIN base_target bt ON bt.id = ltg.targetId " +
           // "LEFT JOIN lb_train_group_apply ltgu ON ltg.id = ltgu.trainGroupId AND u.id = ltgu.userId" +
            "${ew.customSqlSegment}")
    Page<TrainGroupListDto> groupList(Page<TrainGroupListDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select ltg.*,u.nickName userName,bt.target targetName,ac.filePath headPicUrl from lb_train_group ltg " +
            "LEFT JOIN `user` u ON u.id = ltg.userId " +
            "LEFT JOIN user_detail ud ON ud.userId = u.id " +
            "LEFT JOIN attach ac ON ac.id = ud.headPicUrlId " +
            "LEFT JOIN base_target bt ON bt.id = ltg.targetId " +
            "${ew.customSqlSegment}")
    TrainGroupDto groupById(@Param(Constants.WRAPPER) Wrapper wrapper);

}
