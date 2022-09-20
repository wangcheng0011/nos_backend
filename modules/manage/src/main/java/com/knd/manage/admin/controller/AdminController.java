package com.knd.manage.admin.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.admin.service.IAdminService;
import com.knd.manage.admin.vo.VoSaveAdmin;
import com.knd.manage.admin.vo.VoSaveFrozenFlag;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Api(tags = "云端管理-admin")
@RestController
@CrossOrigin
@RequestMapping("/admin/admin")
public class AdminController {
    
     @Resource
    private IAdminService iAdminService;


    @Log("I341-维护用户")
    @ApiOperation(value = "I341-维护用户")
    @PostMapping("/saveAdmin")
    public Result saveAdmin(@Validated @RequestBody VoSaveAdmin vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (vo.getPostType().equals("1")) {
            //数据检查
            if (StringUtils.isEmpty(vo.getPassword())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            //新增
            return iAdminService.add(vo);
        } else {
            //数据检查
            if (StringUtils.isEmpty(vo.getAdminId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iAdminService.edit(vo);
        }
    }


    @Log("I344-更新用户冻结状态")
    @ApiOperation(value = "I344-更新用户冻结状态")
    @PostMapping("/saveFrozenFlag")
    public Result saveFrozenFlag(@Validated @RequestBody VoSaveFrozenFlag vo, BindingResult bindingResult) {
        //userId从token获取
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAdminService.saveFrozenFlag(vo);
    }

    @Log("I343-删除用户")
    @ApiOperation(value = "I343-删除用户")
    @PostMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAdminService.deleteAdmin(vo);
    }

    @Log("I342-查询用户")
    @ApiOperation(value = "I342-查询用户")
    @GetMapping("/getAdmin")
    public Result getAdmin(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAdminService.getAdmin(id);
    }

    @Log("I345-查询用户列表")
    @ApiOperation(value = "I345-查询用户列表")
    @GetMapping("/getAdminList")
    public Result getAdminList(String userName, String nickName, String mobile, String frozenFlag, String current) {
        if (frozenFlag != null && !frozenFlag.equals("0") && !frozenFlag.equals("1")) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAdminService.getAdminList(userName, nickName, mobile, frozenFlag, current);
    }


    @Log("I34X-更新维修人员类型用户密码")
    @ApiOperation(value = "I34X-更新维修人员类型用户密码")
    @PostMapping("/updateMaintenanceWorkerPassword")
    public Result updateMaintenanceWorkerPassword(String userId) {

        return iAdminService.updateMaintenanceWorkerPassword(userId);
    }

//    @Log("I34X-查看维修人员类型用户密码")
//    @ApiOperation(value = "I34X-更新维修人员类型用户密码")
//    @GetMapping("/updateMaintenanceWorkerPassword")
//    public Result updateMaintenanceWorkerPassword() {
//        return iAdminService.updateMaintenanceWorkerPassword();
//    }


}
