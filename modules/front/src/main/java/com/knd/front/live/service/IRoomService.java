package com.knd.front.live.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.response.Result;
import com.knd.front.live.dto.RoomListDto;
import com.knd.front.live.entity.RoomEntity;
import com.knd.front.live.request.CreateOrUpdateRoomRequest;
import com.knd.front.live.request.QueryLiveRoomRequest;
import com.knd.front.live.request.ReportRoomRequest;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface IRoomService extends SuperService<RoomEntity> {
    Result createRoom(CreateOrUpdateRoomRequest createOrUpdateRoomRequest);

    Result<Page<RoomListDto>> roomList(QueryLiveRoomRequest queryLiveRoomRequest);

    Result editRoom(CreateOrUpdateRoomRequest createOrUpdateRoomRequest);

    Result changeRoomStatus(String id, String status);

    Result getRoomToken(String roomId,String invitationCode) throws Exception;

    Result deleteRoom(String id);

    Result closeRoom(String roomId);

    Result closeRoomForManage(String id);

    void kickUser(List<RoomEntity> rooms, String userId);

    Result reserve(String id);

    Result<Page<RoomListDto>> roomReserveList(String currentPage);

    Result reportRoom(ReportRoomRequest request);

   // Result coachTimeCloseRoom(String coachId) ;

    Result scheduledCloseRoom(String id);

    Result closeCourse(String id);

    Result userCoachCloseRoom(String courseId);
}
