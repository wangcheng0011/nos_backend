package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.TrainUserPraiseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface UserTrainPraiseMapper extends BaseMapper<TrainUserPraiseEntity> {

    @Select(" select * from train_user_praise where " +
            " UserId = #{userId} and praiseUserId = #{praiseUserId} and praiseType = #{praiseType}" +
            " and createDate BETWEEN #{beginDate} and #{endDate}")
    TrainUserPraiseEntity getPraise(@Param("userId") String userId,
                                    @Param("praiseUserId") String praiseUserId,
                                    @Param("praiseType") String praiseType,
                                    @Param("beginDate")LocalDateTime beginDate,@Param("endDate") LocalDateTime endDate);



}
