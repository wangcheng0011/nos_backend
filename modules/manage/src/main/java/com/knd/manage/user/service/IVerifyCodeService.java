package com.knd.manage.user.service;

import com.knd.common.response.Result;
import com.knd.manage.mall.dto.CodeDto;
import com.knd.manage.user.entity.VerifyCode;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface IVerifyCodeService extends SuperService<VerifyCode> {
   Result getVerificationCode(CodeDto codeDto);
}
