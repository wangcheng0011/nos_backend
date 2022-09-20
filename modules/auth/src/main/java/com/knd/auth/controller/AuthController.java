package com.knd.auth.controller;

import com.knd.auth.dto.LoginRequestDto;
import com.knd.auth.dto.OrderConsultingDTO;
import com.knd.auth.entity.EquipmentInfo;
import com.knd.auth.entity.PassUri;
import com.knd.auth.service.IAuthService;
import com.knd.auth.service.IPassUriService;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.permission.bean.Token;
import com.knd.permission.config.TokenManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "令牌管理")
@RestController
@CrossOrigin
@RequestMapping("")
public class AuthController {

    @Resource
    protected TokenManager tokenManager;

    @Resource
    private IAuthService authService;

    @Resource
    private IPassUriService passUriService;

    @Log("获取信息")
    @ApiOperation(value = "获取登录信息", notes = "获取登录信息")
    @GetMapping(value = "info")
    public Result info() throws Exception {
        String userId ="07GuoJ09";
        return authService.info(userId);
    }

    @Log("登录")
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "login")
    public Result login(@RequestBody LoginRequestDto dto) throws Exception {
        return authService.login(dto);
    }

    @Log("注销令牌")
    @ApiOperation(value = "注销令牌", notes = "注销令牌")
    @GetMapping(value = "/logout")
    public Result logoutToken() throws Exception {
        return ResultUtil.success(tokenManager.removeToken());
    }

    @Log("查看令牌")
    @ApiOperation(value = "查看令牌", notes = "查看令牌")
    @GetMapping(value = "/query")
    public Result queryToken() throws Exception {
        return authService.queryToken();
    }

    @Log("根据用户id查询权限url列表")
    @ApiOperation(value = "根据用户id查询权限url列表", notes = "根据用户id查询权限url列表")
    @GetMapping(value = "/queryAuthUrlList")
    public Result queryAuthUrlList(@RequestParam("userId") String userId) {
        return authService.queryAuthUrlList(userId);
    }

    /***
     * 内部服务调用
     */

    @Log("获取白名单列表")
    @ApiOperation(value = "获取白名单列表", notes = "获取白名单列表")
    @GetMapping(value = "/queryPassList")
    public List<PassUri> queryPassList() {
        return passUriService.list();
    }


    @Log("验证令牌")
    @ApiOperation(value = "验证令牌", notes = "验证令牌")
    @GetMapping(value = "/queryToken")
    public Token queryTokenInfo(@RequestParam("token") String token) throws Exception {
        return authService.queryTokenInfo(token);
    }

    @Log("检查用户账号状态")
    @ApiOperation(value = "检查用户账号状态", notes = "检查用户账号状态")
    @GetMapping(value = "/queryUserState")
    public String queryUserState(@RequestParam("userId") String userId) throws Exception {
        return authService.queryUserState(userId);
    }

    @Log("获取设备授权码信息")
    @ApiOperation(value = "获取设备授权码信息", notes = "获取设备授权码信息")
    @GetMapping(value = "/queryEquipmentNo")
    public EquipmentInfo queryEquipmentNo(@RequestParam("equipmentNo") String equipmentNo) throws Exception {
        return authService.queryEquipmentNo(equipmentNo);
    }


    @Log("新增或更新订单咨询")
    @ApiOperation(value = "新增或更新订单咨询")
    @PostMapping("/addOrUpdateOrderCousulting")
    public Result addOrUpdateOrderCousulting(@RequestBody OrderConsultingDTO orderConsultingRequest) {
        return authService.addOrUpdateOrderConsulting(orderConsultingRequest);
    }



}
