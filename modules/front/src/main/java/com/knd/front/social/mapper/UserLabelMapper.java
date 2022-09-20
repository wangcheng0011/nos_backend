package com.knd.front.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.social.dto.LabelDto;
import com.knd.front.social.entity.UserLabelEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserLabelMapper extends BaseMapper<UserLabelEntity> {

    @Select("select a.label,a.type from base_label a join user_label b on a.id = b.labelId where b.userId=#{userId}")
    List<LabelDto> getLabelList(@Param("userId") String userId);
}
