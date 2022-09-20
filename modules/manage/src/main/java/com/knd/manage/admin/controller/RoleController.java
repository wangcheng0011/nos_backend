package com.knd.manage.admin.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.admin.service.IRoleService;
import com.knd.manage.admin.vo.VoSaveRole;
import com.knd.manage.admin.vo.VoSaveRolePower;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@Api(tags = "云端管理-admin")
@RestController
@CrossOrigin
@RequestMapping("/admin/admin")
public class RoleController {
     @Resource
    private IRoleService iRoleService;

    @Log("I351-维护角色信息")
    @ApiOperation(value = "I351-维护角色信息")
    @PostMapping("/saveRole")
    public Result saveRole(@Validated @RequestBody VoSaveRole vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (vo.getPostType().equals("1")) {
            //新增
            return iRoleService.add(vo);
        } else {
            //数据检查
            if (StringUtils.isEmpty(vo.getRoleId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iRoleService.edit(vo);
        }
    }


    @Log("I354-删除角色")
    @ApiOperation(value = "I354-删除角色")
    @PostMapping("/deleteRole")
    public Result deleteRole(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iRoleService.deleteRole(vo);
    }


    @Log("I352-查询角色信息")
    @ApiOperation(value = "I352-查询角色信息")
    @GetMapping("/getRole")
    public Result getRole(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iRoleService.getRole(id);
    }


    @Log("I355-查询角色列表")
    @ApiOperation(value = "I355-查询角色列表")
    @GetMapping("/getRoleList")
    public Result getRoleList(String name, String current) {
        return iRoleService.getRoleList(name, current);
    }

    @Log("I356-维护角色权限")
    @ApiOperation(value = "I356-维护角色权限")
    @PostMapping("/saveRolePower")
    public Result saveRolePower(@Validated @RequestBody VoSaveRolePower vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (vo.getPowerList() == null) {
            vo.setPowerList(new ArrayList<>());
        }
        return iRoleService.saveRolePower(vo);
    }

}

