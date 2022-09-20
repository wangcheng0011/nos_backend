package com.knd.manage.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.user.entity.VerifyCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Repository
public interface VerifyCodeMapper extends BaseMapper<VerifyCode> {
        int updateByPrimaryKeyList(@Param("verifyCodes") List<VerifyCode> verifyCodes);
        /**
         * 验证码验证后删除
         * @return
         */
        int updateCode(@Param("mobile") String mobile, @Param("id") String id);
}
