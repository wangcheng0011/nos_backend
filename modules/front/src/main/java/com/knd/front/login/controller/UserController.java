package com.knd.front.login.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.LoginTypeEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.IVerifyCodeService;
import com.knd.front.common.util.JPushUtil;
import com.knd.front.entity.*;
import com.knd.front.login.handler.LoginHandlerFactory;
import com.knd.front.login.handler.LoginHandlerFactoryV2;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.request.*;
import com.knd.front.login.service.IUserLoginInfoService;
import com.knd.front.login.service.IUserService;
import com.knd.front.login.service.IVipMenuService;
import com.knd.front.pay.request.ParseOrderNotifyRequest;
import com.knd.front.pay.service.IGoodsService;
import com.knd.front.train.mapper.ProgramPlanGenerationDao;
import com.knd.front.train.mapper.TrainProgramMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@RestController
@CrossOrigin
@RequestMapping("/front/login")
@Api(tags = "login")
@Slf4j
public class UserController {
    @Resource
    private IUserService iUserService;
    @Resource
    private IUserLoginInfoService iUserLoginInfoService;
    @Resource
    private IVerifyCodeService iVerifyCodeService;
    @Resource
    private IVipMenuService iVipMenuService;
    @Resource
    private IGoodsService iGoodsService;
    @Resource
    private LoginHandlerFactoryV2 loginHandlerFactoryV2;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private TrainProgramMapper trainProgramMapper;
    @Autowired
    private ProgramPlanGenerationDao programPlanGenerationDao;
    @Autowired
    private JPushUtil jPushUtil;

    @PostMapping("/registerUser")
    @Log("I040-用户注册 ")
    @ApiOperation(value = "I040-用户注册 ", notes = "I040-用户注册 ")
    @Deprecated
    public Result registerUser(HttpServletRequest httpServletRequest,@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        String sid = httpServletRequest.getHeader("sid");
        log.info("registerUser sid:"+sid);
        String platform = httpServletRequest.getHeader("platform");
        log.info("registerUser platform:"+platform);
        registerRequest.setSid(sid);
        registerRequest.setPlatform(platform);
        return iUserService.registerUser(registerRequest);

    }

    @PostMapping("/createVirtualAccount")
    @Log("I045-创建虚拟账号")
    @ApiOperation(value = "I045-创建虚拟账号", notes = "I045-创建虚拟账号")
    public Result createVirtualAccount() {
        String userId = UserUtils.getUserId();
        return ResultUtil.success(iUserService.createVirtualAccount(userId));
    }


    @Log("I041-登录")
    @ApiOperation(value = "I041-登录", notes = "I041-登录")
    @PostMapping("/loginAccount")
    public Result loginAccount(HttpServletRequest httpServletRequest, @RequestBody LoginRequest loginRequest) throws Exception {
        log.info("loginAccount loginRequest:{{}}",loginRequest);
        String sid = httpServletRequest.getHeader("sid");
        loginRequest.setSid(sid);
        log.info("loginAccount sid:{{}}",sid);
        String platform = httpServletRequest.getHeader("platform");
        loginRequest.setPlatform(platform);
        log.info("loginAccount platform:{{}}",platform);
        //Result result = loginHandlerFactory.excuteHandler(loginRequest);
        if(StringUtils.isEmpty(loginRequest.getNickname())){
            loginRequest.setNickname(UUIDUtil.getShortUUID().toUpperCase());
            log.info("loginAccount nickname:{{}}",loginRequest.getNickname());
        }
        Result result = null;
        if(StringUtils.isNotEmpty(loginRequest.getVirtualAccount())&&StringUtils.isNotEmpty(loginRequest.getVirtualPassword())){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("virtualAccount",loginRequest.getVirtualAccount());
            userQueryWrapper.eq("virtualPassword",loginRequest.getVirtualPassword());
            User user = userMapper.selectOne(userQueryWrapper);
            if(StringUtils.isEmpty(user)){
                return  ResultUtil.error(ResultEnum.USER_NOT_FOUND_ERROR);
            }
            loginRequest.setMobile(user.getMobile());
            loginRequest.setPassword(user.getPassword());
        }
        //手机号+密码
        if(!StringUtils.isEmpty(loginRequest.getMobile())&&!StringUtils.isEmpty(loginRequest.getPassword())) {
            log.info("loginAccount loginRequest:{{}}",loginRequest);
             result = LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_PASSWORD, loginRequest);
            return result;
        }
        //token登陆
        if(!StringUtils.isEmpty(loginRequest.getToken())) {
            log.info("loginAccount Token:{{}}",loginRequest.getToken());
            result = LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_MOBILE, loginRequest);
            return result;
        }
        //苹果id登陆
        if(!StringUtils.isEmpty(loginRequest.getAppleId())) {
            log.info("loginAccount AppleId:{{}}",loginRequest.getAppleId());
            result = LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_APPLE, loginRequest);
            return result;
        }
        //手机号+手机验证码
        if(!StringUtils.isEmpty(loginRequest.getMobile())&&!StringUtils.isEmpty(loginRequest.getVerifyCode())) {
            log.info("loginAccount Mobile:{{}}",loginRequest.getMobile());
            log.info("loginAccount VerifyCode:{{}}",loginRequest.getVerifyCode());
            result = LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_VERIFYCODE, loginRequest);
            return result;
        }


        log.info("loginAccount result:{{}}"+result);
      //  return result;
        //if(null!=result){
        //  if ("U0001".equals(result.getCode())){
         //     iUserService.loginAccount(loginRequest);
       //   }
        //    return result;
     //  }
      return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(),"参数有误，登陆失败");
    }

    @Log("I042-重置密码")
    @ApiOperation(value = "I042-重置密码", notes = "I042-重置密码")
    @PostMapping("/resetPassword")
    @Transactional(rollbackFor = Exception.class)
    public Result resetPassword(@RequestBody @Valid ResetRequest resetRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", resetRequest.getMobile());
        queryWrapper.eq("deleted", 0);
        queryWrapper.select("mobile", "deleted", "password", "id");
        User reset = iUserService.getOne(queryWrapper);
        if (StringUtils.isEmpty(reset)) {
            return ResultUtil.error(ResultEnum.NOT_PHONE_ERROR);
        }

        //查询验证码是否有效
        QueryWrapper<VerifyCode> wrapper = new QueryWrapper();
//        wrapper.gt("expireTime", DateUtils.getCurrentLocalDateTime());
        wrapper.eq("mobile", resetRequest.getMobile());
        wrapper.eq("id", resetRequest.getVerifyCodeId());
        wrapper.eq("code", resetRequest.getCode());
        wrapper.eq("codeType", 20);
        wrapper.eq("deleted", 0);
        List<VerifyCode> getByMobile = iVerifyCodeService.list(wrapper);
        for (VerifyCode verifyCode :getByMobile){
            if (verifyCode.getExpireTime().isBefore(DateUtils.getCurrentLocalDateTime())){
                return ResultUtil.error(ResultEnum.CODE_TIME_OUT);
            }
        }

        if (StringUtils.isNotEmpty(getByMobile)) {
            if (Md5Utils.md5(resetRequest.getPassword()).equals(reset.getPassword())){
                return ResultUtil.error(ResultEnum.PWD_REPEAT_ERROR);
            }
            resetRequest.setPassword(Md5Utils.md5(resetRequest.getPassword()));
            int i = iUserService.resetPassword(resetRequest, getByMobile);
            if (i == 0) {
                return ResultUtil.error("U1901","重置密码失败");
            } else {
                return ResultUtil.success();
            }
        } else {
            return ResultUtil.error(ResultEnum.VERIFY_CODE_ERROR);
        }

    }

    @PostMapping("/logout")
    @Log("I045-注销账号")
    @ApiOperation(value = "I045-注销账号", notes = "I045-注销账号")
    public Result logout(@RequestBody LogoutRequest logoutRequest) {
        if(StringUtils.isEmpty(logoutRequest.getUserId())){
            //userId从token获取
            logoutRequest.setUserId(UserUtils.getUserId());
        }
        return ResultUtil.success(iUserService.logout(logoutRequest));
    }

    @PostMapping("/trainProgramPush")
    @Log("I045-训练计划推送")
    @ApiOperation(value = "I045-训练计划推送", notes = "I045-训练计划推送")
    public Result trainProgramPush() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        log.info("定时任务 trainProgramPush--------------------执行静态定时任务--------------------------------");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String formatDate = dtf.format(now);
        System.out.println("trainProgramPush:"+formatDate);
        QueryWrapper<ProgramPlanGenerationEntity> programPlanGenerationEntityQueryWrapper = new QueryWrapper<>();
        programPlanGenerationEntityQueryWrapper.eq("date_format(trainDate ,'%Y-%m-%d')",formatDate);
        programPlanGenerationEntityQueryWrapper.eq("trainFinishFlag","0");
        programPlanGenerationEntityQueryWrapper.eq("deleted","0");
        List<ProgramPlanGenerationEntity> programPlanGenerationEntities = programPlanGenerationDao.selectList(programPlanGenerationEntityQueryWrapper);

        programPlanGenerationEntities.stream().forEach(i->{
            log.info("定时任务 trainProgramPush programPlanGenerationEntity:{{}}",i);
            QueryWrapper<ProgramEntity> programEntityQueryWrapper = new QueryWrapper<>();
            programEntityQueryWrapper.eq("id",i.getTrainProgramId());
            programEntityQueryWrapper.eq("deleted",0);
            ProgramEntity programEntity = trainProgramMapper.selectOne(programEntityQueryWrapper);
            log.info("定时任务 trainProgramPush programEntity:{{}}",programEntity);
            // 设置推送参数
            // 这里可以自定义推送参数了
            Map<String, String> parm = new HashMap<>();
            // 设置提示信息,内容是文章标题
            parm.put("title","待接受任务");
            parm.put("alias",programEntity.getUserId());
            parm.put("msg",programEntity.getProgramName());
            parm.put("trainProgramId",programEntity.getId());
            log.info("定时任务 trainProgramPush parm:{{}}",parm);
            jPushUtil.jpushAll(parm);
            System.out.println("定时任务 trainProgramPush parm"+parm);
        });
        return ResultUtil.success();
    }

    @PostMapping("/loginOut")
    @Log("I045-退出登录")
    @ApiOperation(value = "I045-退出登录", notes = "I045-退出登录")
    public Result loginOut(@RequestBody LoginOutRequest loginHisId) {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setId(loginHisId.getLoginHisId());
        userLoginInfo.setLoginOutTime(DateUtils.getCurrentDateTimeStr());
        iUserLoginInfoService.saveOrUpdate(userLoginInfo);
        return ResultUtil.success();
    }


    @Log("I11X-变更会员vip类型")
    @ApiOperation(value = "I11X-变更会员vip类型( 0普通会员 1个人会员 2家庭会员-主)",notes = "I11X-变更会员vip类型")
    @PostMapping("/changeVipType")
    public Result changeVipType(@Valid @RequestBody ChangeVipTypeRequest changeVipTypeRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }

        return iUserService.changeVipType(changeVipTypeRequest);
    }


    @Log("I11X-家庭会员绑定副号")
    @ApiOperation(value = "I11X-家庭会员绑定副号",notes = "I11X-家庭会员绑定副号")
    @PostMapping("/bindingSecondary")
    public Result bindingSecondary(@Valid @RequestBody BindingSecondaryRequest bindingSecondaryRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iUserService.bindingSecondary(bindingSecondaryRequest);
    }

    @Log("I11X-家庭会员解绑副号")
    @ApiOperation(value = "I11X-家庭会员解绑副号",notes = "I11X-家庭会员解绑副号")
    @PostMapping("/unBindingSecondary")
    public Result unBindingSecondary(@Valid @RequestBody BindingSecondaryRequest bindingSecondaryRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iUserService.unBindingSecondary(bindingSecondaryRequest);
    }
    @Log("I11X-家庭会员副号是否可绑定校验")
    @ApiOperation(value = "I11X-家庭会员副号是否可绑定校验",notes = "I11X-家庭会员副号是否可绑定校验")
    @GetMapping("/checkBeforeBindingSecondary")
    public Result checkBeforeBindingSecondary(@Valid BindingSecondaryRequest bindingSecondaryRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return  iUserService.checkBeforeBindingSecondary(bindingSecondaryRequest);
    }

    @Log("I11X-获取家庭会员主号关联副号列表")
    @ApiOperation(value = "I11X-获取家庭会员主号关联副号列表",notes = "I11X-获取家庭会员主号关联副号列表")
    @GetMapping("/getBindingSecondaryList")
    public Result getBindingSecondaryList(@Valid getBindingSecondaryListRequest getBindingSecondaryListRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iUserService.getBindingSecondaryList(getBindingSecondaryListRequest);
    }

    @Log("I11X-获取支付订单")
    @ApiOperation(value = "I11X-获取支付订单",notes = "I11X-获取支付订单")
    @PostMapping("/getPayInfo")
    public Result getPayInfo(HttpServletResponse response, @Valid @RequestBody GetOrderInfoRequest getOrderInfoRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        log.info("getPayInfo getOrderInfoRequest:{{}}",getOrderInfoRequest);
        if(OrderTypeEnum.VIP.getCode().equals(getOrderInfoRequest.getOrderType())) {
            log.info("iVipMenuService getOrderInfoRequest:{{}}",getOrderInfoRequest);
            return iVipMenuService.getPayInfo(getOrderInfoRequest);
        }
//        else if("2".equals(getOrderInfoRequest.getOrderType())){
//            return iGoodsService.getPayInfo(getOrderInfoRequest);
//        }
        else{
            log.info("iGoodsService getOrderInfoRequest:{{}}",getOrderInfoRequest);
            return iGoodsService.getPayInfo(response,getOrderInfoRequest);
        }
    }

    /**
     * 微信回调接口
     */
    @PostMapping(value = "/parseOrderNotifyResult")
    @Log("I11X-微信回调接口")
    @ApiOperation(value = "I11X-微信回调接口", notes = "I11X-微信回调接口")
    public Result parseOrderNotifyResult(@RequestBody ParseOrderNotifyRequest parseOrderNotifyRequest) throws ParseException {
        return  iGoodsService.parseOrderNotifyResult(parseOrderNotifyRequest);
    }

    /**
     * 支付宝回调接口
     */
    @PostMapping(value = "/alipayCallback")
    @Log("I11X-支付宝回调接口")
    @ApiOperation(value = "I11X-支付宝回调接口", notes = "I11X-支付宝回调接口")
    public Result alipayCallback(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                 @RequestParam(required = false, name = "orderId") String orderId) {
        return  iGoodsService.alipayCallback(outBizNo,orderId);
    }




    @Log("I11X-取消订单")
    @ApiOperation(value = "I11X-取消订单",notes = "I11X-取消订单")
    @PostMapping("/cancelOrder")
    public Result cancelOrder(@RequestParam(required = false, name = "orderNo") String orderNo ){
        return iGoodsService.cancelOrder(orderNo);
    }

    @Log("I11X-获取物流信息")
    @ApiOperation(value = "I11X-获取物流信息",notes = "I11X-获取物流信息")
    @GetMapping("/getLogisticsInformation")
    public Result getLogisticsInformation(@RequestParam(required = false, name = "orderNo") String orderNo ){
        return iGoodsService.cancelOrder(orderNo);
    }


    @Log("I11X-查询订单支付状态")
    @ApiOperation(value = "I11X-查询订单支付状态",notes = "I11X-查询订单支付状态")
    @GetMapping("/tradeQuery")
    public Result tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo){

        return iVipMenuService.tradeQuery(outTradeNo,tradeNo);
    }

    @Log("I11X-JS 调起支付签名")
    @ApiOperation(value = "I11X-JS 调起支付签名",notes = "I11X-JS 调起支付签名")
    @GetMapping("/createOfficialAccountUnifiedOrder")
    public Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                                    @RequestParam(required = false, name = "orderNo") String orderNo,
                                                    @RequestParam(required = false, name = "amount") BigDecimal amount){

        return iVipMenuService.createOfficialAccountUnifiedOrder(openid,orderNo,amount);
    }



    @Log("I11X-获取订单列表")
    @ApiOperation(value = "I11X-获取订单列表",notes = "I11X-获取订单列表")
    @GetMapping("/getOrderList")
    public Result getOrderList(String userId,String status,String current,String queryParam,String platform){
        userId = StringUtils.isEmpty(userId)?UserUtils.getUserId():userId;
        return iVipMenuService.getOrderList(userId,status,current,queryParam,platform);
    }

    @Log("I11X-获取会员套餐信息")
    @ApiOperation(value = "I11X-获取会员套餐信息",notes = "I11X-获取会员套餐信息")
    @GetMapping("/getVipMenu")
    public Result getVipMenu(@RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        return iVipMenuService.getVipMenu(userId);
    }

    @Log("I11X-获取运动爱好与体型")
    @ApiOperation(value = "I11X-获取爱好与体型",notes = "I11X-获取爱好与体型")
    @GetMapping("/getShareAndHobby")
    public Result getShareAndHobby(){
        return iUserService.getShareAndHobby();
    }

    @Log("I11X-获取运动方式与运动频率")
    @ApiOperation(value = "I11X-获取运动方式与运动频率",notes = "I11X-获取运动方式与运动频率")
    @GetMapping("/getSportAndFrequency")
    public Result getSportAndFrequency(){
        return iUserService.getSportAndFrequency();
    }


    @Log("I11X-获取器材")
    @ApiOperation(value = "I11X-获取配件",notes = "I11X-获取配件")
    @GetMapping("/getEquipment")
    public Result getEquipment(){
        return iUserService.getEquipmentList();
    }

     /*@Log("根据token查出登录信息")
    @ApiOperation(value = "根据token查出所有信息", notes="根据token查询登录信息")
    @PostMapping("/querylogin")
    public Result queryLogin(@RequestBody @Valid LoginRequest LoginRequest,BindingResult bindingResult){
        if(!StringUtils.isEmpty(LoginRequest.getMobile())&&!StringUtils.isEmpty(LoginRequest.getPassword())) {
            return LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_PASSWORD,LoginRequest);
        }
        if(!StringUtils.isEmpty(LoginRequest.getToken())) {
            return LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_MOBILE,LoginRequest);
        }
        if(!StringUtils.isEmpty(LoginRequest.getAppleId())) {
            return LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_APPLE,LoginRequest);
        }
        if(!StringUtils.isEmpty(LoginRequest.getMobile())&&!StringUtils.isEmpty(LoginRequest.getVerifyCode())) {
            return LoginHandlerFactory.excuteHandler(LoginTypeEnum.LOGIN_VERIFYCODE,LoginRequest);
        }
    }*/


}

