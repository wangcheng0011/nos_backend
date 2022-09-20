package com.knd.front.diagnosis.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.log.Log;
import com.knd.common.password.PasswordUtil;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.diagnosis.mapper.AdminLoginMapper;
import com.knd.front.diagnosis.mapper.AdminMapper;
import com.knd.front.diagnosis.mapper.FaultTemplateMapper;
import com.knd.front.diagnosis.request.RepairmanLoginRequest;
import com.knd.front.diagnosis.request.TestingReportRequest;
import com.knd.front.diagnosis.service.IDiagnosisService;
import com.knd.front.entity.Admin;
import com.knd.front.entity.AdminLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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
@RequestMapping("/front/diagnosis")
@Api(tags = "diagnosis")
@Transactional
public class DiagnosisController {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private FaultTemplateMapper faultTemplateMapper;

    @Autowired
    private IDiagnosisService iDiagnosisService;
    @Resource
    private AdminLoginMapper adminLoginMapper;




    @PostMapping("/login")
    @Log("I11X-维修员登录")
    @ApiOperation(value = "I11X-维修员登录", notes = "I11X-维修员登录")
    public Result login(@RequestBody @Valid RepairmanLoginRequest repairmanLoginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
//        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userName", repairmanLoginRequest.getUserName());
//        queryWrapper.eq("password", repairmanLoginRequest.getPassword());
//        queryWrapper.eq("deleted", "0");
//        Admin admin = adminMapper.selectOne(queryWrapper);
//        if (admin == null) {
//            return ResultUtil.error(ResultEnum.PWD_MOBILE_ERROR.getCode(), ResultEnum.PWD_MOBILE_ERROR.getMessage());
//        }
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", repairmanLoginRequest.getUserName());
        queryWrapper.eq("deleted", "0");
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null) {
            return ResultUtil.error("0001", "用户不存在");
        } else if (!PasswordUtil.checkPassword(repairmanLoginRequest.getPassword(), admin.getSalt(), admin.getPassword())) {
            return ResultUtil.error("0002", "帐号或密码错误");
        } else if (admin.getFrozenFlag().equals("1")) {
            return ResultUtil.error("0003", "帐号冻结");
        } else {
            List<String> roleNameList = adminMapper.getRoleName(repairmanLoginRequest.getUserName());
            for (String roleName : roleNameList){
                if ("临时技师".equals(roleName)){
                    //登陆表插入数据
                    AdminLogin adminLogin = new AdminLogin();
                    adminLogin.setId(UUIDUtil.getShortUUID());
                    adminLogin.setUserId(admin.getId());
                    adminLogin.setLoginTime(LocalDateTime.now());
                    adminLogin.setLoginTime(LocalDateTime.now());
                    adminLoginMapper.insert(adminLogin);
                    //登陆完成之后冻结用户，此操作主要是用户临时技工登陆
                    admin.setFrozenFlag("1");
                    break;
                }
            }
            return ResultUtil.success(admin.getId());
        }
    }

    @GetMapping("/getFaultTemplate")
    @Log("I11X-获取故障模板数据")
    @ApiOperation(value = "I11X-获取故障模板数据", notes = "I11X-获取故障模板数据")
    public Result getFaultTemplate() {
        return ResultUtil.success(faultTemplateMapper.selectList(null));
    }

    @PostMapping("/testingReport")
    @Log("I11X-上报检测")
    @ApiOperation(value = "I11X-上报检测", notes = "I11X-上报检测")
    public Result testingReport(@RequestBody @Valid TestingReportRequest testingReportRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        iDiagnosisService.testingReport(testingReportRequest);
        return ResultUtil.success();
    }
}

