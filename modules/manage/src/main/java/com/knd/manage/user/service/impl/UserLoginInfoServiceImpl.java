package com.knd.manage.user.service.impl;

import cn.jiguang.common.utils.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.em.VipEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.utils.HttpUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.mall.mapper.TbOrderMapper;
import com.knd.manage.mall.mapper.UserReceiveAddressMapper;
import com.knd.manage.mall.mapper.VerifyCodeMapper;
import com.knd.manage.user.dto.LoginInfoDto;
import com.knd.manage.user.dto.UserDto;
import com.knd.manage.user.dto.UserLoginInfoListDto;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.entity.UserLoginInfo;
import com.knd.manage.user.entity.VerifyCode;
import com.knd.manage.user.handler.LoginHandler;
import com.knd.manage.user.mapper.UserLoginInfoMapper;
import com.knd.manage.user.mapper.UserMapper;
import com.knd.manage.user.request.LoginRequest;
import com.knd.manage.user.request.RegisterRequest;
import com.knd.manage.user.service.IUserDetailService;
import com.knd.manage.user.service.IUserLoginInfoService;
import com.knd.manage.user.service.IUserService;
import com.knd.manage.user.service.IVerifyCodeService;
import com.knd.manage.user.util.AESUtil;
import com.knd.manage.user.util.QQHttpClient;
import com.knd.manage.user.util.WeChatUtil;
import com.knd.manage.user.vo.VoWeChatUserLoginInfo;
import com.knd.permission.bean.Token;
import com.knd.permission.util.RedisClientTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Slf4j
@Service
public class UserLoginInfoServiceImpl extends ServiceImpl<UserLoginInfoMapper, UserLoginInfo> implements IUserLoginInfoService {

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
    @Resource
    private IUserService iUserService;
    @Resource
    private TbOrderMapper tbOrderMapper;
    @Resource
    private UserReceiveAddressMapper userReceiveAddressMapper;
    @Resource
    public UserMapper userMapper;
    @Resource
    public LoginHandler loginHandler;
    @Value("${smallRoutine.appId}")
    private String smallRoutineAppId;
    @Value("${smallRoutine.appSecret}")
    private String smallRoutineAppSecret;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Value("${wx.appid}")
    private String wxAppid;

    @Value("${wx.secret}")
    private String wxSecret;

    @Value("${wx.redirectUrl}")
    private String wxRedirectUrl;

    @Value("${qq.appid}")
    private String qqAppid;

    @Value("${qq.secret}")
    private String qqSecret;

    @Value("${qq.redirectUrl}")
    private String qqRedirectUrl;

    @Value("${domain}")
    private String domain;

    private static final String TOKEN_PREFIX = "token:";


    // ????????????????????????
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";


    /**
     * app??????token?????????
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;


    @Override
    public UserLoginInfo insertReturnEntity(UserLoginInfo entity) {
        return null;
    }

    @Override
    public UserLoginInfo updateReturnEntity(UserLoginInfo entity) {
        return null;
    }


    //??????????????????????????????
    @Override
    public Result queryUserLoginInfoList(String nickName, String mobile, String equipmentNo, String loginInTimeBegin,
                                         String loginInTimeEnd, String current) throws ParseException {
        IPage<LoginInfoDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //????????????
        page = baseMapper.selectPageBySome(page, nickName == null ? "%%" : "%" + nickName + "%", mobile == null ? "%%" : "%" + mobile + "%",
                equipmentNo == null ? "%%" : "%" + equipmentNo + "%", loginInTimeBegin == null ? null : sdf.parse(loginInTimeBegin),
                loginInTimeEnd == null ? null : sdf.parse(loginInTimeEnd));
        //??????????????????
        UserLoginInfoListDto userLoginInfoListDto = new UserLoginInfoListDto();
        userLoginInfoListDto.setTotal((int) page.getTotal());
        userLoginInfoListDto.setLoginInfoList(page.getRecords());
        return ResultUtil.success(userLoginInfoListDto);
    }

    @Override
    public void loginAccount(LoginRequest loginRequest) {
        log.info("loginAccount platform:{{}}" + loginRequest.getPlatform());
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        //???????????????????????????
        queryWrapper.eq("sid", loginRequest.getSid());
        log.info("loginAccount sid:{{}}", loginRequest.getSid());
        queryWrapper.eq("deleted", 0);
        User sidLogin = userMapper.selectOne(queryWrapper);
        queryWrapper.clear();
        queryWrapper.eq("mobile", loginRequest.getMobile());

        queryWrapper.eq("deleted", 0);
        queryWrapper.select("mobile", "deleted",
                "password", "id", "nickName", "frozenFlag",
                "vipStatus", "masterId", "vipBeginDate", "vipEndDate");
        User mobileLogin = userMapper.selectOne(queryWrapper);
        log.info("loginAccount mobileLogin:", mobileLogin);
        if (null == sidLogin && PlatformEnum.QUINNOID.getName().equals(loginRequest.getPlatform())) {
            //???????????????????????????????????????
            if (null == mobileLogin.getVipBeginDate()) {
                mobileLogin.setVipBeginDate(LocalDate.now());
            } else {
                mobileLogin.setVipEndDate(mobileLogin.getVipBeginDate().plusYears(1));
            }
            if (null == mobileLogin.getVipEndDate()) {
                mobileLogin.setVipEndDate(LocalDate.now().plusYears(1));
            } else {
                mobileLogin.setVipEndDate(mobileLogin.getVipEndDate().plusYears(1));
            }
            //2????????????-???
            mobileLogin.setVipStatus("2");
            mobileLogin.setSid(loginRequest.getSid());
            userMapper.update(mobileLogin, queryWrapper);
        }
        // mobileLogin.setUnionId(loginRequest.getUnionId());
        // mobileLogin.setQqOpenId(loginRequest.getQqOpenId());
        // userMapper.update(mobileLogin, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result verifyCodeLogin(LoginRequest loginRequest) throws Exception {
        log.info("verifyCodeLogin loginRequest:{{}}", loginRequest);
        // try {
        if (StringUtils.isEmpty(loginRequest.getMobile())
                || StringUtils.isEmpty(loginRequest.getVerifyCode())
                || StringUtils.isEmpty(loginRequest.getVerifyCodeId())) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "?????????|code|codeId??????");
        }

        //???????????????????????????
        Result checkResult = checkVerifyCode(loginRequest);
        log.info("verifyCodeLogin checkResult:{{}}", checkResult);
        if (!ResultEnum.SUCCESS.getCode().equals(checkResult.getCode())) {
            return checkResult;
        }
        //???????????????????????????????????????????????????
        User user = iUserService.getUserByMobile(loginRequest.getMobile());
        log.info("verifyCodeLogin user:{{}}", user);
        if (StringUtils.isEmpty(user)) {

            //????????????
            RegisterRequest registerRequest = new RegisterRequest();
            BeanUtils.copyProperties(loginRequest, registerRequest);
            user = iUserService.registerUser2(registerRequest);
            log.info("verifyCodeLogin user:{{}}", user);
        } else {
            if (StringUtils.isNotEmpty(loginRequest.getUnionId())) {
                user.setUnionId(loginRequest.getUnionId());
            }
            if (StringUtils.isNotEmpty(loginRequest.getQqOpenId())) {
                user.setQqOpenId(loginRequest.getQqOpenId());
            }
            userMapper.updateById(user);
            if ("1".equals(user.getFrozenFlag())) {
                return ResultUtil.error(ResultEnum.ACCOUNT_FREEZE_ERROR);
            }
        }
        if (StringUtils.isNotEmpty(loginRequest.getLoginState())) {
            String tokenstr = UserUtils.createToken(user != null ? user.getId() : UUIDUtil.getShortUUID(), PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
            Token token = new Token();
            token.setExpireSecond(EXPIRE_SECOND_WEB);
            token.setToken(tokenstr);
            token.setUserId(user != null ? user.getId() : UUIDUtil.getShortUUID());
            token.setMobile(loginRequest.getMobile());
            token.setUnionid(loginRequest.getUnionId());
            token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
            log.info("verifyCodeLogin token:{{}}", token);
            log.info("verifyCodeLogin", "login:code:" + loginRequest.getLoginState());
            redisClient.set("login:code:" + loginRequest.getLoginState(), token, token.getExpireSecond());
        }
        Map<String, Object> maps = getResponse(loginRequest, user, null);
        return ResultUtil.success(maps);
        //   } catch (Exception e) {
        //       e.printStackTrace();
        //       return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        //   }
    }

    @Override
    public Result smallRoutineLogin(HttpServletResponse httpServletResponse, LoginRequest loginRequest) throws Exception {
        log.info("-----------------------------????????????????????????----------------------------------");
        log.info("smallRoutineLogin loginRequest:{{}}", loginRequest);
        User user = new User();
        // ???????????????
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        // ????????????
        Map<String, Object> datas = new HashMap<String, Object>();
        // ??????????????????
        //??????????????????id
        //??????access_token
        log.info("smallRoutineLogin appId:{{}}", smallRoutineAppId);
        log.info("smallRoutineLogin appSecret:{{}}", smallRoutineAppSecret);
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" + "&appid=" + smallRoutineAppId + "&secret=" + smallRoutineAppSecret;
        String accessTokenResult = HttpUtils.httpGet(accessTokenUrl);
        log.info("smallRoutineLogin accessTokenResult:{{}}", accessTokenResult);
        JSONObject accessTokenJson = JSONObject.parseObject(accessTokenResult);
        log.info("smallRoutineLogin accessTokenJson:{{}}", accessTokenJson);
        String access_token = accessTokenJson.getString("access_token");
        //???????????????
        String phoneUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + access_token;
        Map<String, Object> phoneMap = new HashMap<String, Object>();
        phoneMap.put("code", loginRequest.getMobileCode());
        log.info("smallRoutineLogin access_token:{{}}", access_token);
        String phoneJsonString = JSONObject.toJSON(phoneMap).toString();
        log.info("smallRoutineLogin phoneMap:{{}}", phoneMap);
        String phoneResult = HttpUtils.httpPost(phoneUrl, phoneJsonString);
        log.info("smallRoutineLogin phoneResult:{{}}", phoneResult);
        JSONObject phoneJson = (JSONObject) JSONObject.parseObject(phoneResult).get("phone_info");
        log.info("smallRoutineLogin phoneJson:{{}}", phoneJson);
        String phoneNumber = phoneJson.getString("phoneNumber");
        log.info("smallRoutineLogin phoneNumber:{{}}", phoneNumber);
        log.info("smallRoutineLogin phoneResult:{{}}", phoneResult);
        log.info("smallRoutineLogin phoneJson:{{}}", phoneJson);
        //???????????????
        byte[] dataByte = Base64.decode(loginRequest.getEncryptedData().toCharArray());
        Map<String, String> stringStringMap = oauth2GetUnionId(loginRequest.getOpenIdcode(), loginRequest.getEncryptedData(), loginRequest.getIv());
        log.info("smallRoutineLogin stringStringMap:{{}}", stringStringMap);
        log.info("smallRoutineLogin unionid:{{}}", stringStringMap.get("unionid"));
        String unionid = stringStringMap.get("unionid");
        log.info("smallRoutineLogin session_key:{{}}", stringStringMap.get("session_key"));
        byte[] keyByte = Base64.decode(stringStringMap.get("session_key").toCharArray());
        byte[] ivByte = Base64.decode(loginRequest.getIv().toCharArray());
        UserDto userDto = new UserDto();
        //   try {
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base
                    + (keyByte.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters
                .getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// ?????????
        byte[] resultByte = cipher.doFinal(dataByte);
        Map<String, Object> maps = null;
        if (null != resultByte && resultByte.length > 0) {
            String dec = new String(resultByte, "UTF-8");
            System.out.println(dec);
            // ?????????????????????
            Map<String, Object> wxinfo = (Map) JSON.parse(dec);
            log.info("smallRoutineLogin wxinfo:{{}}", wxinfo);
            // ===???????????????openid???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // 3.???????????????????????? ?????????????????????
            String openId = wxinfo.get("openId").toString();
            log.info("smallRoutineLogin openId:{{}}", openId);
            System.out.println("openId" + openId + "=====");
            // ??????
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(phoneNumber)) {
                userQueryWrapper.eq("mobile", phoneNumber);
            } else {
                userQueryWrapper.eq("unionId", unionid);
            }
            log.info("smallRoutineLogin unionid:{{}}", unionid);
            userQueryWrapper.eq("deleted", 0);
            user = userMapper.selectOne(userQueryWrapper);
            log.info("smallRoutineLogin user:{{}}", user);
            String userId = UUIDUtil.getShortUUID();
            // ??????????????????????????????
            if (StringUtils.isEmpty(user)) {
                user = new User();

                log.info("smallRoutineLogin userId:{{}}", userId);
                // ????????????????????????
                user.setId(userId);
                user.setDeleted("0");
                log.info("smallRoutineLogin openId:{{}}", openId);
                user.setSmallRoutineOpenId(openId);
                user.setMobile(phoneNumber);
                log.info("smallRoutineLogin phoneNumber:{{}}", phoneNumber);
                user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
                user.setRegistTime("");
                user.setTrainPeriodBeginTime("");
                user.setFrozenFlag("0");
                user.setRecommendBodyPart("");
                user.setUnionId(unionid);
                if(StringUtils.isNotEmpty(loginRequest.getWxNickname())) {
                    user.setWxNickname(loginRequest.getWxNickname());
                }
                if(StringUtils.isNotEmpty(loginRequest.getWxAvatar())) {
                    user.setWxAvatar(loginRequest.getWxAvatar());
                }
                if(StringUtils.isNotEmpty(loginRequest.getSmallRoutineHeadPic())){
                    user.setSmallRoutineHeadPic(loginRequest.getSmallRoutineHeadPic());
                }
                user.setCreateBy(userId);
                user.setCreateDate(LocalDateTime.now());
                log.info("smallRoutineLogin CreateDate:{{}}", LocalDateTime.now());
                user.setWxNickname(loginRequest.getWxNickname());
                log.info("smallRoutineLogin WxNickname:{{}}", loginRequest.getWxNickname());
                user.setWxAvatar(loginRequest.getWxAvatar());
                log.info("smallRoutineLogin WxAvatar:{{}}", loginRequest.getWxAvatar());
                log.info("smallRoutineLogin user:{{}}", user);
                userMapper.insert(user);
            } else {
                //       user.setWxOpenId(wxinfo.get("openId").toString());
                log.info("smallRoutineLogin phoneNumber:{{}}", phoneNumber);
                user.setMobile(phoneNumber);
                user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
                if(StringUtils.isNotEmpty(loginRequest.getWxNickname())) {
                    user.setWxNickname(loginRequest.getWxNickname());
                }
                if(StringUtils.isNotEmpty(loginRequest.getWxAvatar())) {
                    user.setWxAvatar(loginRequest.getWxAvatar());
                }
                user.setUnionId(unionid);
                user.setSmallRoutineOpenId(openId);
                if(StringUtils.isNotEmpty(loginRequest.getSmallRoutineHeadPic())){
                    user.setSmallRoutineHeadPic(loginRequest.getSmallRoutineHeadPic());
                }
                user.setLastModifiedBy(userId);
                user.setLastModifiedDate(LocalDateTime.now());
                log.info("smallRoutineLogin user:{{}}", user);
                userMapper.updateById(user);
            }
        }
        //  } catch (Exception e) {
        //     e.printStackTrace();
        //  }
        maps = getResponse(loginRequest, user, access_token);
        log.info("smallRoutineLogin loginRequest:{{}}", loginRequest);
        log.info("smallRoutineLogin user:{{}}", user);
        log.info("smallRoutineLogin access_token:{{}}", access_token);
        log.info("smallRoutineLogin maps:{{}}", maps);
        log.info("-----------------------------????????????????????????----------------------------------");
        return ResultUtil.success(maps);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result weiXinlogin(LoginRequest loginRequest) {
        if (StringUtils.isEmpty(loginRequest.getOpenId())
                || StringUtils.isEmpty(loginRequest.getUnionId())
                || StringUtils.isEmpty(loginRequest.getNickname())
                || StringUtils.isEmpty(loginRequest.getAvatar())) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "openId|unionId|??????|????????????");
        }

        log.info("???????????? loginRequest:{{}}", loginRequest);
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("wxOpenId", loginRequest.getOpenId());
            queryWrapper.eq("unionId", loginRequest.getUnionId());
            queryWrapper.eq("deleted", 0);
            User user = userMapper.selectOne(queryWrapper);
            log.info("???????????? login:{{}}", user);
            if (user == null) {
                if (StringUtils.isEmpty(loginRequest.getMobile())
                        || StringUtils.isEmpty(loginRequest.getVerifyCode())
                        || StringUtils.isEmpty(loginRequest.getVerifyCodeId())) {
                    return ResultUtil.success(0);
                }
                log.info("??????????????????,????????????????????????????????????");

                //???????????????
                Result checkResult = loginHandler.checkVerifyCode(loginRequest);
                if (!ResultEnum.SUCCESS.getCode().equals(checkResult.getCode())) {
                    return checkResult;
                }

                //????????????????????????????????????
                User mobileUser = iUserService.getUserByMobile(loginRequest.getMobile());
                if (StringUtils.isNotEmpty(mobileUser)) {
                    log.info("??????????????????,?????????????????????,???????????????????????????????????????");
                    if ("1".equals(mobileUser.getFrozenFlag())) {
                        return ResultUtil.error(ResultEnum.ACCOUNT_FREEZE_ERROR);
                    }
                    mobileUser.setWxOpenId(loginRequest.getOpenId());
                    mobileUser.setUnionId(loginRequest.getUnionId());
                    mobileUser.setWxNickname(loginRequest.getNickname());
                    mobileUser.setWxAvatar(loginRequest.getAvatar());
                    if (StringUtils.isEmpty(mobileUser.getNickName())) {
                        mobileUser.setNickName(loginRequest.getNickname());
                    }
                    userMapper.updateById(mobileUser);
                    user = mobileUser;
                } else {
                    log.info("??????????????????,?????????????????????");
                    //????????????
                    RegisterRequest registerRequest = new RegisterRequest();
                    BeanUtils.copyProperties(loginRequest, registerRequest);
                    registerRequest.setWxOpenId(loginRequest.getOpenId());
                    registerRequest.setUnionId(loginRequest.getUnionId());
                    registerRequest.setWxNickname(loginRequest.getNickname());
                    registerRequest.setWxAvatar(loginRequest.getAvatar());
                    user = iUserService.registerUser2(registerRequest);
                }
            } else {
                if ("1".equals(user.getFrozenFlag())) {
                    return ResultUtil.error(ResultEnum.ACCOUNT_FREEZE_ERROR);
                }
                //???????????????????????????????????????????????????????????????????????????
                user.setWxNickname(loginRequest.getNickname());
                user.setWxAvatar(loginRequest.getAvatar());
                if (StringUtils.isEmpty(user.getNickName())) {
                    user.setNickName(loginRequest.getNickname());
                }
                userMapper.updateById(user);
            }
            Map<String, Object> maps = loginHandler.getResponse(loginRequest, user);
            return ResultUtil.success(maps);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    @Override
    public void wxcallback(String code, String state, HttpServletResponse httpServletResponse) throws Exception {
        log.info("wxcallback code:{{}}", code);
        log.info("wxcallback state:{{}}", state);
        VoWeChatUserLoginInfo userInfo = WeChatUtil.getWeChatUserInfo(wxAppid, wxSecret, code);
        String nickname = userInfo.getNickname();
        log.info("wxcallback state:{{}}", state);
        String unionid = userInfo.getUnionid();
        log.info("wxcallback unionid:{{}}", unionid);
        String headimgurl = userInfo.getHeadimgurl();
        log.info("wxcallback headimgurl:{{}}", headimgurl);
        String openid = userInfo.getOpenid();
        log.info("wxcallback openid:{{}}", openid);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("unionId", unionid);
        userQueryWrapper.eq("deleted", "0");
        User user = userMapper.selectOne(userQueryWrapper);
        log.info("wxcallback user:{{}}", user);
        if (StringUtils.isNotEmpty(user)) {
            user.setWxNickname(nickname);
            user.setWxAvatar(headimgurl);
            user.setUnionId(unionid);
            user.setWxOpenId(openid);
            user.setLastModifiedDate(LocalDateTime.now());
            log.info("wxcallback user:{{}}", user);
            userMapper.updateById(user);
        }
        //??????token
        LoginRequest loginRequest = new LoginRequest();
        String tokenstr = UserUtils.createToken(user != null ? user.getId() : UUIDUtil.getShortUUID(), PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
        Token token = new Token();
        token.setExpireSecond(EXPIRE_SECOND_WEB);
        token.setToken(tokenstr);
        token.setUserId(user != null ? user.getId() : UUIDUtil.getShortUUID());
        token.setMobile(user != null ? user.getMobile() : "");
        token.setUnionid(unionid);
        token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
        log.info("wxcallback token:{{}}", token);
        log.info("wxcallback", "login:code:" + state);
        redisClient.set("login:code:" + state, token, token.getExpireSecond());
        httpServletResponse.sendRedirect(wxRedirectUrl);
        //   } catch (Exception e) {
        //        throw new CustomResultException("????????????");
        // }


    }

    @Override
    public void qqcallback(String code, String state, HttpServletResponse httpServletResponse) throws Exception {
        log.info("qqcallback code:{{}}", code);
        log.info("qqcallback state:{{}}", state);
        String accessTokenUrl = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code" +
                "&client_id=" + qqAppid +
                "&client_secret=" + qqSecret +
                "&code=" + code +
                "&redirect_uri=" + qqRedirectUrl;

        String accessToken = QQHttpClient.getAccessToken(accessTokenUrl);
        log.info("qqcallback accessToken:{{}}", accessToken);
        //Step3: ?????????????????? openid ???
        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
        String openid = QQHttpClient.getOpenID(url);
        log.info("qqcallback openid:{{}}", openid);
        //Step4?????????QQ????????????
        url = "https://graph.qq.com/user/get_user_info?access_token=" + accessToken +
                "&oauth_consumer_key=" + qqAppid +
                "&openid=" + openid;

        JSONObject jsonObject = QQHttpClient.getUserInfo(url);
        log.info("qqcallback jsonObject:{{}}", jsonObject);
        String nickname = jsonObject.get("nickname").toString();
        log.info("qqcallback nickname:{{}}", nickname);
        String figureurl_qq_2 = jsonObject.get("figureurl_qq_2").toString();
        log.info("qqcallback figureurl_qq_2:{{}}", figureurl_qq_2);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("qqOpenId", openid);
        userQueryWrapper.eq("deleted", "0");
        User user = userMapper.selectOne(userQueryWrapper);
        log.info("qqcallback user:{{}}", user);
        if (StringUtils.isNotEmpty(user)) {
            user.setQqNickname(nickname);
            user.setQqAvatar(figureurl_qq_2);
            user.setQqOpenId(openid);
            user.setLastModifiedDate(LocalDateTime.now());
            log.info("qqcallback user:{{}}", user);
            userMapper.updateById(user);
        }

        //??????token
        LoginRequest loginRequest = new LoginRequest();
        String tokenstr = UserUtils.createToken(user != null ? user.getId() : UUIDUtil.getShortUUID(), PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
        Token token = new Token();
        token.setExpireSecond(EXPIRE_SECOND_WEB);
        token.setToken(tokenstr);
        token.setUserId(user != null ? user.getId() : UUIDUtil.getShortUUID());
        token.setMobile(user != null ? user.getMobile() : "");
        token.setQqOpenId(openid);
        token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
        log.info("qqcallback token:{{}}", token);
        log.info("qqcallback", "login:code:" + state);
        redisClient.set("login:code:" + state, token, token.getExpireSecond());
        httpServletResponse.sendRedirect(qqRedirectUrl);
    }


    @Override
    public Result wxLogin(String state) {
        log.info("wxLogin state:{{}}", state);
        //?????????????????????????????????
//        String url = "https://open.weixin.qq.com/" +
//                "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";

        // ????????????????????????baseUrl  %s?????????????????????????
           /* String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                    "?appid=%s" +
                    "&redirect_uri=%s" +
                    "&response_type=code" +
                    "&scope=snsapi_login" +
                    "&state=%s" +
                    "#wechat_redirect";
            //???redirect_uri??????URLEncoder??????
            String redirectUrl = wxRedirectUrl;
            System.out.println(redirectUrl + "++++++++++++++++++++++++++++++++++++++++++++");
            try {
                redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //??????%s????????????
            String url = String.format(
                    baseUrl,
                    wxAppid,
                    redirectUrl,
                    "atguigu"
            );
*/
        String callBackMethod = wxRedirectUrl + "/manage/admin/user/wxcallback";
        // String state=  "atguigu";
        String url = WeChatUtil.getWeChatUrl(wxAppid, state, callBackMethod);
        log.info("wxLogin:{{}}", url);

        //????????????????????????????????????
        return ResultUtil.success(url);
    }


    @Override
    public Result wxQQLoginPolling(String state) throws Exception {
        //WxResultVo ????????????????????????????????????
        log.info("wxLoginPolling state:{{}}", state);
        Token token = null;
        try {
            token = redisClient.get("login:code:" + state);
            log.info("wxLoginPolling token:{{}}", token);
            if (StringUtils.isEmpty(token)) {
                return ResultUtil.error(ResultEnum.FAIL);
            }
            log.info("wxLoginPolling token:{{}}", token);
        } catch (Exception e) {
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
        return ResultUtil.success(token);
    }


    @Override
    public Result qqLogin(String state) throws UnsupportedEncodingException {
        //QQ????????????????????????
        String backUrl = qqRedirectUrl + "/manage/admin/user/authqq";
        //???????????????????????????CSRF??????
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //Step1?????????Authorization Code
        String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code" +
                "&client_id=" + qqAppid +
                "&redirect_uri=" + URLEncoder.encode(backUrl, "utf-8") +
                "&state=" + state;
        return ResultUtil.success(url);

    }


    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param loginRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result checkVerifyCode(LoginRequest loginRequest) {
        log.info("checkVerifyCode loginRequest:{{}}", loginRequest);
        //???????????????????????????
        QueryWrapper<VerifyCode> verifyCodeWrapper = new QueryWrapper();
        verifyCodeWrapper.eq("mobile", loginRequest.getMobile());
        verifyCodeWrapper.eq("id", loginRequest.getVerifyCodeId());
        verifyCodeWrapper.eq("code", loginRequest.getVerifyCode());
        verifyCodeWrapper.eq("deleted", 0);
        verifyCodeWrapper.eq("codeType", 40);
        List<VerifyCode> verifyCodeList = iVerifyCodeService.list(verifyCodeWrapper);
        log.info("checkVerifyCode verifyCodeList:{{}}", verifyCodeList);
        if (verifyCodeList.size() <= 0) {
            return ResultUtil.error(ResultEnum.VERIFY_CODE_ERROR);
        }
        for (VerifyCode verifyCode : verifyCodeList) {
            if (verifyCode.getExpireTime().isBefore(DateUtils.getCurrentLocalDateTime())) {
                return ResultUtil.error(ResultEnum.CODE_TIME_OUT);
            }
        }
        //???????????????
        verifyCodeMapper.updateByPrimaryKeyList(verifyCodeList);
        return ResultUtil.success();
    }

    /**
     * ?????????????????????????????????
     *
     * @param loginRequest
     * @param user
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getResponse(LoginRequest loginRequest, User user, String access_token) throws Exception {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUserId(user.getId());
        userLoginInfo.setId(UUIDUtil.getShortUUID());
        userLoginInfo.setDeleted("0");
        userLoginInfo.setLoginInTime(DateUtils.getCurrentDateTimeStr() + "");
        userLoginInfo.setEquipmentNo(loginRequest.getEquipmentNo());
        log.info("getResponse getResponse:{{}}", userLoginInfo);
        iUserLoginInfoService.save(userLoginInfo);

        Map<String, Object> maps = new HashMap<>();
        //??????token
        String tokenstr = UserUtils.createToken(user.getId(), PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
        maps.put("token", tokenstr);
        maps.put("userId", user.getId());
        maps.put("loginHisId", userLoginInfo.getId());
        maps.put("nickName", user.getNickName());
        maps.put("vipStatus", user.getVipStatus());
        maps.put("vipBeginDate", user.getVipBeginDate());
        maps.put("vipEndDate", user.getVipEndDate());
        maps.put("mobile", user.getMobile());
        String headUrl = iUserDetailService.getHeadUrl(user.getId());
        log.info("getResponse headUrl:{{}}", headUrl);
        maps.put("headPicUrl", headUrl);
        maps.put("smallRoutineHeadPic", user.getSmallRoutineHeadPic());
        maps.put("access_token", access_token);

        //???????????????????????????
        Integer count = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>()
                .eq("userId", user.getId()));
        log.info("getResponse count:{{}}", count);
        maps.put("isCoach", count);//0 false 1true        //???token?????????redis??????
        Token token = new Token();
        token.setExpireSecond(EXPIRE_SECOND_WEB);
        token.setToken(tokenstr);
        token.setUserId(user.getId());
        token.setMobile(loginRequest.getMobile());
        token.setPlatform(PlatformEnum.getNameByCode(loginRequest.getSid()) == null ? "app" : PlatformEnum.getNameByCode(loginRequest.getSid()));
        log.info("getResponse token:{{}}", token);
        redisClient.set(TOKEN_PREFIX + tokenstr, token, token.getExpireSecond());
        redisClient.set("token_" + loginRequest.getSid() + "_" + user.getId(), TOKEN_PREFIX + tokenstr, token.getExpireSecond());
        log.info("getResponse maps:{{}}", maps);
        return maps;
    }

    /**
     * ???????????????
     *
     * @param code          WxCode
     * @param encryptedData ????????????
     * @param iv            ?????????iv
     * @return
     */
    public Map<String, String> oauth2GetUnionId(String code, String encryptedData, String iv) {
        String requestUrl = GetPageAccessTokenUrl.replace("APPID", smallRoutineAppId).replace("SECRET", smallRoutineAppSecret).replace("CODE", code);
        Map<String, String> result = new HashMap<>();
        try {
            String response = HttpUtils.httpGet(requestUrl);
            JSONObject jsonObject = JSONObject.parseObject(response);
            log.info("oauth2GetUnionId jsonObject:{{}}", jsonObject);
            String openid = String.valueOf(jsonObject.get("openid"));
            // ?????????????????????session_key
            String session_key = String.valueOf(jsonObject.get("session_key"));
            String unionid = String.valueOf(jsonObject.get("unionid"));
            // ??????AES??????encryptedData ??????union_id?????????????????????
            String encryptedResult = AESUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            /**
             * ???????????????????????????????????????
             * openid	 string	??????????????????
             * nickName  string ??????
             * gender    string ??????
             * city      string	??????
             * province  string ??????
             * country   string ??????
             * avatarUrl string	??????
             * unionId   string	???????????????????????????????????????
             * watermark JSON	?????????????????????appid???timestamp?????? ??????????????????????????????
             */
            JSONObject parseObject = JSONObject.parseObject(encryptedResult);
            log.info("oauth2GetUnionId parseObject:{{}}", parseObject);
            // ps:??????????????????????????????????????????????????????????????????unionId,openId????????????????????????unionid

            result.put("openid", openid);
            result.put("unionid", unionid);
            result.put("session_key", session_key);
        } catch (Exception e) {
            log.info("????????????unionid????????????");
            e.printStackTrace();
        }
        return result;
    }


}
