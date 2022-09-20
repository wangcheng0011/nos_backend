package com.knd.front.login.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.IVerifyCodeService;
import com.knd.front.entity.User;
import com.knd.front.entity.UserLoginInfo;
import com.knd.front.live.entity.UserCoachEntity;
import com.knd.front.live.mapper.UserCoachMapper;
import com.knd.front.login.request.LoginRequest;
import com.knd.front.login.service.IUserDetailService;
import com.knd.front.login.service.IUserLoginInfoService;
import com.knd.front.login.service.IUserService;
import com.knd.front.login.service.IVipMenuService;
import com.knd.front.login.service.impl.UserServiceImpl;
import com.knd.permission.bean.Token;
import com.knd.permission.util.RedisClientTokenUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author will
 * @date 2021年04月30日 14:51
 */
@Component
@Log4j2
public class LoginPasswordHandler implements ILoginHandler{

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IUserLoginInfoService iUserLoginInfoService;
    @Autowired
    private IVerifyCodeService iVerifyCodeService;
    @Autowired
    private RedisClientTokenUtil redisClient;


    @Autowired
    private UserCoachMapper userCoachMapper;


    @Autowired
    private IVipMenuService iVipMenuService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private IUserDetailService iUserDetailService;

    /**
     * app终端token有效期
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;

    private static final String TOKEN_PREFIX = "token:";
    @Override
    public Result login(LoginRequest loginRequest) throws Exception {
        log.info("-------------------------------我要开始登录啦！！！！！！！！！！！密码登录---------------------------------------");
        if (StringUtils.isEmpty(loginRequest.getMobile())
                ||StringUtils.isEmpty(loginRequest.getPassword())
        ||StringUtils.isEmpty(loginRequest.getEquipmentNo())) {
            return ResultUtil.error("U0995","用户名|密码|设备号不能为空");
        }
        log.info("login loginRequest:{{}}",loginRequest);
        log.info("login mobile:{{}}",loginRequest.getMobile());
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", loginRequest.getMobile());
        queryWrapper.eq("deleted", 0);
        queryWrapper.select("mobile", "deleted",
                "password", "id", "nickName","frozenFlag",
                "vipStatus","masterId","vipBeginDate","vipEndDate");
        User login = iUserService.getOne(queryWrapper);
        log.info("login user:{{}}",login);
        if (StringUtils.isEmpty(login)) {
            return ResultUtil.error(ResultEnum.DELETE_PHONE_ERROR);
        }
        log.info("login login.getPassword():{{}}",login.getPassword());
        log.info("------------------------------------------------");
        String pwd = Md5Utils.md5(loginRequest.getPassword());
        log.info("login loginRequest.getPassword():{{}}",loginRequest.getPassword());
        log.info("login pwd:{{}}",pwd);
        if (!pwd.equals(login.getPassword())) {
            return ResultUtil.error(ResultEnum.PWD_MOBILE_ERROR);
        }
        if ("1".equals(login.getFrozenFlag())){
            return ResultUtil.error(ResultEnum.ACCOUNT_FREEZE_ERROR);
        }

        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUserId(login.getId());
        userLoginInfo.setId(UUIDUtil.getShortUUID());
        userLoginInfo.setDeleted("0");
        userLoginInfo.setLoginInTime(DateUtils.getCurrentDateTimeStr() + "");
        userLoginInfo.setEquipmentNo(loginRequest.getEquipmentNo());
        iUserLoginInfoService.save(userLoginInfo);
        //大屏送一年活动
       // User user = userServiceImpl.sendToMembers(loginRequest);
        log.info("login login:{{}}",login);
        Map<String, Object> maps = new HashMap<>();
        //创建token
        String tokenstr = UserUtils.createToken(login.getId(), PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
        maps.put("token", tokenstr);
        maps.put("userId", login.getId());
        maps.put("loginHisId", userLoginInfo.getId());
        maps.put("nickName", login.getNickName());
        maps.put("vipStatus", login.getVipStatus());
        maps.put("vipBeginDate", login.getVipBeginDate());
        maps.put("vipEndDate", login.getVipEndDate());
        maps.put("headPicUrl", iUserDetailService.getHeadUrl(login.getId()));

        //检查用户是否是教练
        Integer count = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>()
                .eq("userId", login.getId()));
        maps.put("isCoach", count);//0 false 1true

        //将token存储到redis里面
        Token token = new Token();
        token.setExpireSecond(EXPIRE_SECOND_WEB);
        token.setToken(tokenstr);
        token.setUserId(login.getId());
        token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
        log.info("login token:{{}}",token);
        try {
            redisClient.set(TOKEN_PREFIX + tokenstr, token, token.getExpireSecond());
            redisClient.set( "token_"+loginRequest.getSid()+"_"+ login.getId(), TOKEN_PREFIX + tokenstr, token.getExpireSecond());
        } catch (Exception e) {
            e.printStackTrace();
           return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
        log.info("login maps:{{}}",maps);
        log.info("我要结束登录啦！！！！！！！！！！！ 密码登录");
        return ResultUtil.success(maps);
    }
}
