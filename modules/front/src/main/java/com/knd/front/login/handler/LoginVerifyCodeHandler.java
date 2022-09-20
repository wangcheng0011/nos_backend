package com.knd.front.login.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.IVerifyCodeService;
import com.knd.front.entity.*;
import com.knd.front.live.entity.UserCoachEntity;
import com.knd.front.live.mapper.UserCoachMapper;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.login.request.LoginRequest;
import com.knd.front.login.service.IUserDetailService;
import com.knd.front.login.service.IUserLoginInfoService;
import com.knd.front.login.service.IUserService;
import com.knd.front.user.mapper.UserReceiveAddressMapper;
import com.knd.permission.bean.Token;
import com.knd.permission.util.RedisClientTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author will
 * @date 2021年04月30日 14:51
 */
@Component
@Slf4j
public class LoginVerifyCodeHandler implements ILoginHandler{
    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisClientTokenUtil redisClient;

    @Autowired
    private IUserLoginInfoService iUserLoginInfoService;

    @Autowired
    private IUserDetailService iUserDetailService;

    @Autowired
    private IVerifyCodeService iVerifyCodeService;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Autowired
    private UserCoachMapper userCoachMapper;

    /**
    /**
     * app终端token有效期
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;

    private static final String TOKEN_PREFIX = "token:";
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(LoginRequest loginRequest) throws Exception {
        log.info("我要开始登录啦！！！！！！！！！！！ 验证码登录");
        log.info("login loginRequest:{{}}",loginRequest);
        //查询验证码是否有效
        QueryWrapper<VerifyCode> wrapper = new QueryWrapper();

        wrapper.eq("mobile", loginRequest.getMobile());
        wrapper.eq("id", loginRequest.getVerifyCodeId());
        wrapper.eq("code", loginRequest.getVerifyCode());
        wrapper.eq("deleted", 0);
        wrapper.eq("codeType", 40);
        List<VerifyCode> getByMobile = iVerifyCodeService.list(wrapper);
        for (VerifyCode verifyCode:getByMobile){
            if (verifyCode.getExpireTime().isBefore(DateUtils.getCurrentLocalDateTime())){
                return ResultUtil.error(ResultEnum.CODE_TIME_OUT);
            }
        }
        if (StringUtils.isNotEmpty(getByMobile)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("mobile", loginRequest.getMobile());
            queryWrapper.eq("deleted", 0);
            queryWrapper.select("mobile", "deleted",
                    "password", "id", "nickName","frozenFlag",
                    "vipStatus","masterId","vipBeginDate","vipEndDate");
            queryWrapper.last("limit 1");
            User login = iUserService.getOne(queryWrapper);
            boolean isFirstFlag = false;
            if (StringUtils.isEmpty(login)) {
                User user = new User();
                user.setMobile(loginRequest.getMobile());
                user.setRegistTime(DateUtils.getCurrentDateTimeStr());
                user.setNickName(loginRequest.getNickname());
                user.setFrozenFlag("0");
                user.setDeleted("0");
                user.setPassword(loginRequest.getPassword());
                log.info("login user:{{}}",user);
                iUserService.save(user);
                //关联该手机号未注册前的订单及收货地址
                List<TbOrder> tbOrderList = tbOrderMapper.selectList(new QueryWrapper<TbOrder>()
                        .eq("mobile", user.getMobile()).isNull("userId"));
                if(tbOrderList.size()>0) {
                    Set<String> orderIdSet = new HashSet<>() ;
                    for (TbOrder tbOrder : tbOrderList) {
                        tbOrder.setUserId(user.getId());
                        tbOrderMapper.updateById(tbOrder);

                    }
                    List<UserReceiveAddressEntity> userReceiveAddressEntities = userReceiveAddressMapper.selectList(new QueryWrapper<UserReceiveAddressEntity>()
                            .eq("phone", user.getMobile()).isNull("userId"));
                    for (UserReceiveAddressEntity userReceiveAddressEntity : userReceiveAddressEntities) {
                        userReceiveAddressEntity.setUserId(user.getId());
                        userReceiveAddressMapper.updateById(userReceiveAddressEntity);
                    }
                }

                login = user;
                isFirstFlag = true;
            }
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            userLoginInfo.setUserId(login.getId());
            userLoginInfo.setId(UUIDUtil.getShortUUID());
            userLoginInfo.setDeleted("0");
            userLoginInfo.setLoginInTime(DateUtils.getCurrentDateTimeStr() + "");
            userLoginInfo.setEquipmentNo(loginRequest.getEquipmentNo());
            log.info("login userLoginInfo:{{}}",userLoginInfo);
            iUserLoginInfoService.save(userLoginInfo);

            Map<String, Object> maps = new HashMap<>();
            //创建token
            String tokenstr = UserUtils.createToken(login.getId(), PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
            maps.put("token", tokenstr);
            maps.put("userId", login.getId());
            maps.put("loginHisId", userLoginInfo.getId());
         //   if(isFirstFlag){
          //      maps.put("nickName", "");
        //    }else{
                maps.put("nickName", login.getNickName());
         //   }
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
            //token.setPlatform("mapp");
            token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));

           // try {
                redisClient.set(TOKEN_PREFIX + tokenstr, token, token.getExpireSecond());
           // } catch (Exception e) {
           //     e.printStackTrace();
           //     return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
          //  }
            log.info("我要开始登录啦！！！！！！！！！！！ 验证码登录");
            return ResultUtil.success(maps);
        } else {
            return ResultUtil.error(ResultEnum.VERIFY_CODE_ERROR);
        }
    }
}
