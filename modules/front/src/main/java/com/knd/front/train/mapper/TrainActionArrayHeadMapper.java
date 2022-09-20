package com.knd.front.train.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.TrainActionArrayHead;
import com.knd.front.train.dto.ActionArrayTrainListDto;
import com.knd.front.train.dto.TrainActionArrayHeadDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author will
 */
public interface TrainActionArrayHeadMapper extends BaseMapper<TrainActionArrayHead> {
    List<ActionArrayTrainListDto> getActionArrayTrainList(@Param("page")Page<ActionArrayTrainListDto> page, @Param("userId")String userId);
     TrainActionArrayHeadDto getTrainActionArrayHeadDetail( @Param("id")String id);
}
