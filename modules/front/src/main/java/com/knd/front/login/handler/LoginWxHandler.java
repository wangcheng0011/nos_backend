package com.knd.front.login.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.User;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.request.LoginRequest;
import com.knd.front.login.request.RegisterRequest;
import com.knd.front.login.service.IUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zm
 */
@Component
@Log4j2
public class LoginWxHandler extends LoginHandler implements ILoginHandler{

    @Resource
    private IUserService iUserService;
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(LoginRequest loginRequest) {
        if (StringUtils.isEmpty(loginRequest.getOpenId())
                || StringUtils.isEmpty(loginRequest.getUnionId())
                || StringUtils.isEmpty(loginRequest.getNickname())
                || StringUtils.isEmpty(loginRequest.getAvatar())){
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(),"openId|unionId|昵称|头像为空");
        }

        log.info("微信登陆 loginRequest:{{}}",loginRequest);
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("wxOpenId", loginRequest.getOpenId());
            queryWrapper.eq("unionId", loginRequest.getUnionId());
            queryWrapper.eq("deleted", 0);
            User user = userMapper.selectOne(queryWrapper);
            log.info("微信登陆 login:{{}}",user);
            if(user == null) {
                if (StringUtils.isEmpty(loginRequest.getMobile())
                        || StringUtils.isEmpty(loginRequest.getVerifyCode())
                        || StringUtils.isEmpty(loginRequest.getVerifyCodeId())){
                    return ResultUtil.success(0);
                }
                log.info("微信首次登陆,需要判断是否已有手机账号");

                //校验验证码
                Result checkResult = super.checkVerifyCode(loginRequest);
                if (!ResultEnum.SUCCESS.getCode().equals(checkResult.getCode())){
                    return checkResult;
                }

                //查询该手机号是否已注册过
                User mobileUser = iUserService.getUserByMobile(loginRequest.getMobile());
                if (StringUtils.isNotEmpty(mobileUser)){
                    log.info("微信首次登陆,该手机号已注册,同步该手机号的微信登陆信息");
                    if ("1".equals(mobileUser.getFrozenFlag())){
                        return ResultUtil.error(ResultEnum.ACCOUNT_FREEZE_ERROR);
                    }
                    mobileUser.setWxOpenId(loginRequest.getOpenId());
                    mobileUser.setUnionId(loginRequest.getUnionId());
                    mobileUser.setWxNickname(loginRequest.getNickname());
                    mobileUser.setWxAvatar(loginRequest.getAvatar());
                    if (StringUtils.isEmpty(mobileUser.getNickName())){
                        mobileUser.setNickName(loginRequest.getNickname());
                    }
                    iUserService.updateUser(mobileUser);
                    user = mobileUser;
                }else {
                    log.info("微信首次登陆,手机号尚未注册");
                    //注册用户
                    RegisterRequest registerRequest = new RegisterRequest();
                    BeanUtils.copyProperties(loginRequest,registerRequest);
                    registerRequest.setWxOpenId(loginRequest.getOpenId());
                    registerRequest.setUnionId(loginRequest.getUnionId());
                    registerRequest.setWxNickname(loginRequest.getNickname());
                    registerRequest.setWxAvatar(loginRequest.getAvatar());
                    user = iUserService.registerUser2(registerRequest);
                }
            }else {
                if ("1".equals(user.getFrozenFlag())){
                    return ResultUtil.error(ResultEnum.ACCOUNT_FREEZE_ERROR);
                }
                //该微信已存在用户的时候，针对该用户微信数据进行更新
                user.setWxNickname(loginRequest.getNickname());
                user.setWxAvatar(loginRequest.getAvatar());
                if (StringUtils.isEmpty(user.getNickName())){
                    user.setNickName(loginRequest.getNickname());
                }
                iUserService.updateUser(user);
            }
            Map<String, Object> maps = super.getResponse(loginRequest,user);
            return ResultUtil.success(maps);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }
}
