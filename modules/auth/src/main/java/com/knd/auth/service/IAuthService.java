package com.knd.auth.service;

import com.knd.auth.dto.LoginRequestDto;
import com.knd.auth.dto.OrderConsultingDTO;
import com.knd.auth.entity.EquipmentInfo;
import com.knd.common.response.Result;
import com.knd.permission.bean.Token;

public interface IAuthService {
    /**
     * 登录
     *
     * @param dto
     * @return
     */
    Result login(LoginRequestDto dto) throws Exception;

    /**
     * 验证token信息
     *
     * @param token
     * @return
     */
    Result checkToken(String token) throws Exception;

    /**
     * 查看token信息
     *
     * @return
     */
    Result queryToken() throws Exception;

    /**
     * 根据用户id查询权限url列表
     *
     * @return
     */
    Result queryAuthUrlList(String userId);

    /**
     * 查看token信息
     *
     * @return
     */
    Token queryTokenInfo(String token) throws Exception;

    /**
     * 检查用户账号状态
     */
    String queryUserState(String userId);

    /**
     * 获取设备授权码信息
     */
    EquipmentInfo queryEquipmentNo(String equipmentNo) throws Exception;

    /**
     * 查询登录信息
     */
    Result info(String userId)throws Exception;

    Result addOrUpdateOrderConsulting(OrderConsultingDTO orderConsultingRequest);
}
