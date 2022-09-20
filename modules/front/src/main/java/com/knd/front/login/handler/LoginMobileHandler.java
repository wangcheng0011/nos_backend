package com.knd.front.login.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.utils.HttpUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.entity.TbOrder;
import com.knd.front.entity.User;
import com.knd.front.entity.UserLoginInfo;
import com.knd.front.entity.UserReceiveAddressEntity;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author will
 * @date 2021年04月30日 14:51
 */
@Component
@Slf4j
public class LoginMobileHandler implements ILoginHandler{
    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisClientTokenUtil redisClient;

    @Autowired
    private IUserLoginInfoService iUserLoginInfoService;

    @Autowired
    private IUserDetailService iUserDetailService;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Autowired
    private UserCoachMapper userCoachMapper;

    @Value("${appId}")
    private String appId;

    /**
     * app终端token有效期
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;

    private static final String TOKEN_PREFIX = "token:";
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(LoginRequest loginRequest) throws Exception {
        log.info("我要开始登录啦！！！！！！！！！！！ 手机登录");
        log.info("login loginRequest:{{}}",loginRequest);
        String url = "https://my.wlwx.com:6016/req/api/server/Onekey/mobileQuery";
        String master_secret = "E3AF1B5731C12EEBD0CFEEF6451C8AD1";
        String paramStr = "";
        Map<String,String> paramMap = new HashMap<>();
        String timeStamp = System.currentTimeMillis()+"";
        String signKey = Md5Utils.md5(master_secret+timeStamp);
        paramMap.put("sign",signKey);
        paramMap.put("app_id",appId);
        paramMap.put("time_stamp",timeStamp);
        paramMap.put("access_token",loginRequest.getToken());
        log.info("login paramMap:{{}}",paramMap);
        paramStr = JSONObject.toJSONString(paramMap);
       // try {
            String s = HttpUtils.httpPost(url, paramStr);
            JSONObject jsonResult = JSONObject.parseObject(s);
            log.info("login jsonResult:{{}}",jsonResult);
            String phone = jsonResult.getJSONObject("object").getString("tel");
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("mobile", phone);
            queryWrapper.eq("deleted", 0);
            queryWrapper.select("mobile", "deleted",
                    "password", "id", "nickName","frozenFlag",
                    "vipStatus","masterId","vipBeginDate","vipEndDate");
            User login = iUserService.getOne(queryWrapper);
            log.info("login login:{{}}",login);
            boolean isFirstFlag = false;
            if (StringUtils.isEmpty(login)) {
                User user = new User();
                user.setMobile(phone);
                user.setRegistTime(DateUtils.getCurrentDateTimeStr());
               // user.setNickName(UUIDUtil.getShortUUID());
                user.setNickName(loginRequest.getNickname());
                user.setFrozenFlag("0");
                iUserService.save(user);
                log.info("login user:{{}}",user);
                //关联该手机号未注册前的订单及收货地址
                List<TbOrder> tbOrderList = tbOrderMapper.selectList(new QueryWrapper<TbOrder>()
                        .eq("mobile", user.getMobile()).isNull("userId"));
                log.info("login tbOrderList:{{}}",tbOrderList);
                if(tbOrderList.size()>0) {
                    Set<String> orderIdSet = new HashSet<>() ;
                    for (TbOrder tbOrder : tbOrderList) {
                        tbOrder.setUserId(user.getId());
                        tbOrderMapper.updateById(tbOrder);

                    }
                    List<UserReceiveAddressEntity> userReceiveAddressEntities = userReceiveAddressMapper.selectList(new QueryWrapper<UserReceiveAddressEntity>()
                            .eq("phone", user.getMobile()).isNull("userId"));
                    log.info("login userReceiveAddressEntities",userReceiveAddressEntities);
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
            iUserLoginInfoService.save(userLoginInfo);
            log.info("login userLoginInfo:{{}}",userLoginInfo);
            Map<String, Object> maps = new HashMap<>();
            //创建token
            String tokenstr = UserUtils.createToken(login.getId(), PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
            maps.put("token", tokenstr);
            maps.put("userId", login.getId());
            maps.put("loginHisId", userLoginInfo.getId());
            maps.put("mobile", login.getMobile());
       //     if(isFirstFlag){
          //      maps.put("nickName", "");
        //    }else{
            maps.put("nickName", login.getNickName());
        //    }

            maps.put("vipStatus", login.getVipStatus());
            maps.put("vipBeginDate", login.getVipBeginDate());
            maps.put("vipEndDate", login.getVipEndDate());
            maps.put("headPicUrl", iUserDetailService.getHeadUrl(login.getId()));
            //检查用户是否是教练
            Integer count = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>()
                    .eq("userId", login.getId()));
            log.info("login count:{{}}",count);
            maps.put("isCoach", count);//0 false 1true
            //将token存储到redis里面
            Token token = new Token();
            token.setExpireSecond(EXPIRE_SECOND_WEB);
            token.setToken(tokenstr);
            token.setUserId(login.getId());
            //token.setPlatform("mapp");
            token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid())==null?"app":PlatformEnum.getNameByCode(loginRequest.getSid()));
            redisClient.set(TOKEN_PREFIX + tokenstr, token, token.getExpireSecond());
            log.info("我要结束登录啦！！！！！！！！！！！ 手机登录");
            log.info("login maps:{{}}",maps);
            return ResultUtil.success(maps);
      //  } catch (Exception e) {
      //      e.printStackTrace();
      //      return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
      //  }
    }
}
