package com.knd.manage.user.controller;


import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.user.entity.UserLoginInfo;
import com.knd.manage.user.request.LoginOutRequest;
import com.knd.manage.user.request.LoginRequest;
import com.knd.manage.user.service.IUserLoginInfoService;
import com.knd.manage.user.vo.VoWxQQLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;


@Slf4j
@Api(tags = "云端管理-user")
@RestController
@CrossOrigin
@RequestMapping("/admin/user")
public class UserLoginInfoController {

    @Resource
    private IUserLoginInfoService iUserLoginInfoService;




    @Log("I311-查询注册会员登录列表")
    @ApiOperation(value = "I311-查询注册会员登录列表")
    @GetMapping("/queryUserLoginInfoList")
    public Result queryUserLoginInfoList(String nickName, String mobile, String equipmentNo, String loginInTimeBegin,
                                        String loginInTimeEnd, String current) throws ParseException {
        //数据检查
        if (StringUtils.isEmpty(current)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserLoginInfoService.queryUserLoginInfoList(nickName, mobile, equipmentNo, loginInTimeBegin, loginInTimeEnd, current);
    }

    @Log("I041-登录")
    @ApiOperation(value = "I041-登录", notes = "I041-登录")
    @PostMapping("/loginAccount")
    public Result loginAccount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody LoginRequest loginRequest) throws Exception {
        log.info("loginAccount loginRequest:{{}}",loginRequest);
        String sid = httpServletRequest.getHeader("sid");
        loginRequest.setSid(sid);
        log.info("loginAccount sid:{{}}",sid);
        String platform = httpServletRequest.getHeader("platform");
        loginRequest.setPlatform(platform);
        log.info("loginAccount platform:{{}}",platform);
        //Result result = loginHandlerFactory.excuteHandler(loginRequest);
        if(StringUtils.isEmpty(loginRequest.getWxNickname())){
            loginRequest.setWxNickname(UUIDUtil.getShortUUID().toUpperCase());
            log.info("loginAccount loginRequest:{{}}",loginRequest.getWxNickname());
        }

        Result result = null;

        //手机号+手机验证码
        if(!StringUtils.isEmpty(loginRequest.getMobile())&&!StringUtils.isEmpty(loginRequest.getVerifyCode())) {
            result = iUserLoginInfoService.verifyCodeLogin(loginRequest);
            log.info("loginAccount result:{{}}"+result);
            if(null!=result){
                if ("U0001".equals(result.getCode())){
                    iUserLoginInfoService.loginAccount(loginRequest);
                }
                return result;
            }
        }
        //小程序登录
        if(!StringUtils.isEmpty(loginRequest.getOpenIdcode())&&!StringUtils.isEmpty(loginRequest.getIv())&&!StringUtils.isEmpty(loginRequest.getEncryptedData())) {
            result = iUserLoginInfoService.smallRoutineLogin(httpServletResponse,loginRequest);
            return result;
        }

        //微信QQ登录
        if(!StringUtils.isEmpty(loginRequest.getWxNickname())&&!StringUtils.isEmpty(loginRequest.getWxAvatar())&&!StringUtils.isEmpty(loginRequest.getUnionId())) {
            result = iUserLoginInfoService.weiXinlogin(loginRequest);
            return result;
        }

        return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(),"参数有误，登陆失败");
    }



    @Log("I041-微信登录")
    @ApiOperation(value = "I041-微信登录", notes = "I041-微信登录")
    @PostMapping("/wxLogin")
    public Result wxLogin(@RequestBody VoWxQQLogin vo, BindingResult bindingResult) {

        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserLoginInfoService.wxLogin(vo.getState());
    }
    @Log("I041-微信回调")
    @ApiOperation(value = "I041-微信回调", notes = "I041-微信QQ回调")
    @GetMapping("/wxcallback")
    public void wxcallback(String code, String state,HttpServletResponse httpServletResponse) throws Exception {
        log.info("wxcallback code:{{}}", code);
        log.info("wxcallback state:{{}}", state);
        iUserLoginInfoService.wxcallback(code,state,httpServletResponse);
    }

    @Log("I041-QQ登录")
    @ApiOperation(value = "I041-QQ登录", notes = "I041-QQ登录")
    @PostMapping("/qqLogin")
    public Result qqLogin(@RequestBody VoWxQQLogin vo, BindingResult bindingResult) throws UnsupportedEncodingException {
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserLoginInfoService.qqLogin(vo.getState());
    }



    @Log("I041-QQ登录重定向")
    @ApiOperation(value = "I041-QQ登录重定向", notes = "I041-QQ登录重定向")
    @GetMapping("/authqq")
    public void authqq(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // QQ登录有点特殊，参数放在#后面，后台无法获取#后面的参数，只能用JS做中间转换
        String html =   "<!DOCTYPE html>" +
                "<html lang=\"zh-cn\">" +
                "<head>" +
                "   <title>QQ登录重定向页</title>" +
                "   <meta charset=\"utf-8\"/>" +
                "</head>" +
                "<body>" +
                "   <script type=\"text/javascript\">" +
                "   location.href = location.href.replace('#', '').replace('authqq', 'qqcallback');" +
                " </script> " +
                "</body>" +
                "</html>";
        log.info("authqq html:{{}}",html);
        response.getWriter().print(html);
    }

    @Log("I041-QQ登录回调")
    @ApiOperation(value = "I041-QQ登录回调", notes = "I041-QQ登录回调")
    @GetMapping(value="/qqcallback")
    public void qqcallback(String code, String state,HttpServletResponse httpServletResponse) throws Exception {
        iUserLoginInfoService.qqcallback(code,state,httpServletResponse);
    }



    /**
     * 前端轮询判断用户是否登录了
     */
    @Log("I041-前端轮询判断用户是否登录了")
    @ApiOperation(value = "I041-前端轮询判断用户是否登录了", notes = "I041-前端轮询判断用户是否登录了")
    @GetMapping(value = "/wxQQLoginPolling")
    public Result wxQQLoginPolling(@RequestParam String state) throws Exception {
        return iUserLoginInfoService.wxQQLoginPolling(state);
    }


    @Log("I045-退出登录")
    @ApiOperation(value = "I045-退出登录", notes = "I045-退出登录")
    @PostMapping("/loginOut")
    public Result loginOut(@RequestBody LoginOutRequest loginHisId) {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setId(loginHisId.getLoginHisId());
        userLoginInfo.setLoginOutTime(DateUtils.getCurrentDateTimeStr());
        iUserLoginInfoService.saveOrUpdate(userLoginInfo);
        return ResultUtil.success();
    }
}

