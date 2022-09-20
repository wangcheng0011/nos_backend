package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.user.dto.HealthByDateDto;
import com.knd.front.user.entity.UserHealthEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

public interface UserHealthMapper extends BaseMapper<UserHealthEntity> {

    @Select("select ${field} as value,date  from user_health   " +
            "where userId = #{userId} " +
            "and ${field}!='' " +
            "and  date BETWEEN #{beginDate} and #{endDate}  " +
            "GROUP BY date  " +
            "ORDER BY date desc ")
    List<HealthByDateDto> getHealthByDateList(@Param("field") String field,
                                              @Param("userId") String userId,
                                              @Param("beginDate") LocalDate beginDate,
                                              @Param("endDate") LocalDate endDate);
}
