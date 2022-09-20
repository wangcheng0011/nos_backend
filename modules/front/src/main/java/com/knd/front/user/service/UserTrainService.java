package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.domain.RankingTypeEnum;
import com.knd.front.user.request.UserTrainPraiseRequest;

import java.time.LocalDate;

/**
 * @author zm
 */
public interface UserTrainService {

    /**
     * 获取排行榜
     * @return
     */
    Result getRankingList(RankingTypeEnum type, LocalDate date,String userId, Long size);

    /**
     * 点赞功能
     * @param userTrainPraiseRequest
     * @return
     */
    Result praise(UserTrainPraiseRequest userTrainPraiseRequest);
}
