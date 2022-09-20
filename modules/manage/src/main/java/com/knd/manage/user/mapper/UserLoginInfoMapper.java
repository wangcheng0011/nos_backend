package com.knd.manage.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.knd.manage.user.entity.UserLoginInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.user.dto.LoginInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
public interface UserLoginInfoMapper extends BaseMapper<UserLoginInfo> {
    //查询注册会员登录列表
    IPage<LoginInfoDto> selectPageBySome(IPage<LoginInfoDto> page, @Param("nickName") String nickName,
                                         @Param("mobile") String mobile, @Param("equipmentNo") String equipmentNo,
                                         @Param("loginInTimeBegin") Date loginInTimeBegin, @Param("loginInTimeEnd") Date loginInTimeEnd);

}
