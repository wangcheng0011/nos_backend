package com.knd.front.live.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.response.Result;
import com.knd.front.domain.RankingTypeEnum;
import com.knd.front.live.dto.ApplyListDto;
import com.knd.front.live.dto.GroupUserListDto;
import com.knd.front.live.dto.TrainGroupDto;
import com.knd.front.live.dto.TrainGroupListDto;
import com.knd.front.live.entity.TrainGroupEntity;
import com.knd.front.live.request.CreateOrUpdateTrainGroupRequest;
import com.knd.front.live.request.QueryTrainGroupRequest;
import com.knd.mybatis.SuperService;

import java.time.LocalDate;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface ITrainGroupService extends SuperService<TrainGroupEntity> {
    Result createGroup(CreateOrUpdateTrainGroupRequest createOrUpdateTrainGroupRequest);

    Result<Page<TrainGroupListDto>> groupList(QueryTrainGroupRequest queryTrainGroupRequest);

    Result editGroup(CreateOrUpdateTrainGroupRequest createOrUpdateTrainGroupRequest);

    Result joinGroup(String groupId);

    Result signOutGroup(String groupId);

    Result kickOutGroup(String Id);

    Result kickOutGroupForManage(String id);

    Result examine(String id,String status);

    Result changeUserRole(String id,String role) throws Exception;

    Result<Page<ApplyListDto>> applyList(String groupId,String applyStatus,String currentPage);

    Result<Page<GroupUserListDto>> groupUserList(String groupId,String currentPage);

    Result<GroupUserListDto> groupUserList(String groupId);

    Result deleteGroup(String id);

    TrainGroupEntity getGroup(String groupId);

    Result<String> getGroupRole(String groupId);

    /**
     * 获取小组排行榜
     * @param type
     * @param date
     * @param userId
     * @param groupId
     * @return
     */
    Result getRankingList(RankingTypeEnum type, LocalDate date, String userId, String groupId);

    Result<TrainGroupDto> groupById(String groupId);
}
