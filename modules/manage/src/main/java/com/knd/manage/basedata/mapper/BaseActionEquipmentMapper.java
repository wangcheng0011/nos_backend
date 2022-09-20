package com.knd.manage.basedata.mapper;

import com.knd.manage.basedata.dto.EquipmentDto;
import com.knd.manage.basedata.entity.BaseActionEquipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.basedata.vo.VoEquipment;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface BaseActionEquipmentMapper extends BaseMapper<BaseActionEquipment> {

    //根据动作id获取器材id和器材名称
    List<EquipmentDto> selectIDAndNameByActionId(String actionId);

    //检查是否有未删除的动作使用该器材
    int selectActionCountByEquipId(String id);

}
