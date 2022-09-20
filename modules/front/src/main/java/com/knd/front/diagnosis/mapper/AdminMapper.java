package com.knd.front.diagnosis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("select c.name from role c " +
            "join admin_role a on c.id = a.roleId and a.deleted=0 " +
            "join admin b on a.userId = b.id and b.deleted=0 " +
            "where b.userName = #{userName} and c.deleted=0")
    List<String> getRoleName(@Param("userName") String userName);
}
