package com.knd.manage.user.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.mall.mapper.VerifyCodeMapper;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.entity.UserLoginInfo;
import com.knd.manage.user.entity.VerifyCode;
import com.knd.manage.user.request.LoginRequest;
import com.knd.manage.user.service.IUserDetailService;
import com.knd.manage.user.service.IUserLoginInfoService;
import com.knd.manage.user.service.IVerifyCodeService;
import com.knd.permission.bean.Token;
import com.knd.permission.util.RedisClientTokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zm
 */
@Component
public class LoginHandler {

    @Resource
    private IUserLoginInfoService iUserLoginInfoService;
    @Resource
    private IUserDetailService iUserDetailService;
    @Resource
    private RedisClientTokenUtil redisClient;
    @Resource
    private UserCoachMapper userCoachMapper;
    @Resource
    private IVerifyCodeService iVerifyCodeService;
    @Resource
    private VerifyCodeMapper verifyCodeMapper;

    /**
     * app终端token有效期
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;

    private static final String TOKEN_PREFIX = "token:";

    /**
     * 针对手机验证码进行校验，校验通过之后删除验证码
     * @param loginRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result  checkVerifyCode(LoginRequest loginRequest) {
        //查询验证码是否有效
        QueryWrapper<VerifyCode> verifyCodeWrapper = new QueryWrapper();
        verifyCodeWrapper.eq("mobile", loginRequest.getMobile());
        verifyCodeWrapper.eq("id", loginRequest.getVerifyCodeId());
        verifyCodeWrapper.eq("code", loginRequest.getVerifyCode());
        verifyCodeWrapper.eq("deleted", 0);
        verifyCodeWrapper.eq("codeType", 40);
        List<VerifyCode> verifyCodeList = iVerifyCodeService.list(verifyCodeWrapper);
        if(verifyCodeList.size()<=0) {
            return ResultUtil.error(ResultEnum.VERIFY_CODE_ERROR);
        }
        for (VerifyCode verifyCode:verifyCodeList){
            if (verifyCode.getExpireTime().isBefore(DateUtils.getCurrentLocalDateTime())){
                return ResultUtil.error(ResultEnum.CODE_TIME_OUT);
            }
        }
        //删除验证码
        verifyCodeMapper.updateByPrimaryKeyList(verifyCodeList);
        return ResultUtil.success();
    }

    /**
     * 组装用户登陆的返回数据
     * @param loginRequest
     * @param user
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getResponse(LoginRequest loginRequest, User user) throws Exception{
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUserId(user.getId());
        userLoginInfo.setId(UUIDUtil.getShortUUID());
        userLoginInfo.setDeleted("0");
        userLoginInfo.setLoginInTime(DateUtils.getCurrentDateTimeStr() + "");
        if(StringUtils.isNotEmpty(loginRequest.getEquipmentNo())){
            userLoginInfo.setEquipmentNo(loginRequest.getEquipmentNo());
        }
        iUserLoginInfoService.save(userLoginInfo);

        Map<String, Object> maps = new HashMap<>();
        //创建token
        String tokenstr = UserUtils.createToken(user.getId(), PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
        maps.put("token", tokenstr);
        maps.put("userId", user.getId());
        maps.put("mobile", user.getMobile());
        maps.put("loginHisId", userLoginInfo.getId());
        maps.put("nickName", user.getNickName());
        maps.put("vipStatus", user.getVipStatus());
        maps.put("vipBeginDate", user.getVipBeginDate());
        maps.put("vipEndDate", user.getVipEndDate());
        maps.put("headPicUrl", iUserDetailService.getHeadUrl(user.getId()));

        //检查用户是否是教练
        Integer count = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>()
                .eq("userId", user.getId()));
        maps.put("isCoach", count);//0 false 1true
        //将token存储到redis里面
        Token token = new Token();
        token.setExpireSecond(EXPIRE_SECOND_WEB);
        token.setToken(tokenstr);
        token.setUserId(user.getId());
        token.setMobile(user.getMobile());
        token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
        redisClient.set(TOKEN_PREFIX + tokenstr, token, token.getExpireSecond());
        redisClient.set( "token_"+loginRequest.getSid()+"_"+ user.getId(), TOKEN_PREFIX + tokenstr, token.getExpireSecond());
        return maps;
    }
}
