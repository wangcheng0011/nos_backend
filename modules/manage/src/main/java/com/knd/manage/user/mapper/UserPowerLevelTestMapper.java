package com.knd.manage.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.knd.manage.user.entity.UserPowerLevelTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.user.dto.PowerLevelTestDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-27
 */
public interface UserPowerLevelTestMapper extends BaseMapper<UserPowerLevelTest> {

    //分页查询注册会员力量等级测试列表
    IPage<PowerLevelTestDto> selectPowerLevelTestPageBySome(IPage<PowerLevelTestDto> page, @Param("nickName") String nickName, @Param("mobile") String mobile,
                                                            @Param("action") String action, @Param("trainTimeBegin") Date trainTimeBegin,
                                                            @Param("trainTimeEnd") Date trainTimeEnd);
}
