package com.knd.manage.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.admin.entity.AdminLogin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface AdminLoginMapper extends BaseMapper<AdminLogin> {

    @Select("select max(loginTime) FROM admin_login where userId =#{userId}")
    LocalDateTime getMaxLoginTime(@Param("userId") String userId);
}
