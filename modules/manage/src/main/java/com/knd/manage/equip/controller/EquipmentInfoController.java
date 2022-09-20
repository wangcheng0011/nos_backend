package com.knd.manage.equip.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.equip.service.IEquipmentInfoService;
import com.knd.manage.equip.vo.VoChangeEquipmentStatus;
import com.knd.manage.equip.vo.VoSaveEquipment;
import com.knd.manage.equip.vo.VoScanEquipmentStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "云端管理-equip")
@RestController
@CrossOrigin
@RequestMapping("/admin/equip")
public class EquipmentInfoController {

    @Resource
    private IEquipmentInfoService iEquipmentInfoService;

    @Log("I262-维护设备")
    @ApiOperation(value = "I262-维护设备")
    @PostMapping("/saveEquipment")
    public Result saveEquipment(@RequestBody @Validated VoSaveEquipment vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //判断操作类型
        if (vo.getPostType().equals("1")) {
            //新增
            return iEquipmentInfoService.add(vo.getUserId(), vo.getEquipmentNo(), vo.getRemark(),null);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iEquipmentInfoService.edit(vo.getUserId(), vo.getEquipmentNo(), vo.getRemark(), vo.getId(),null);
        }

    }

    @Log("I26X-excel批量导入设备")
    @ApiOperation(value = "I26X-excel批量导入设备")
    @PostMapping("/saveEquipmentByBatch")
    public Result saveEquipmentByBatch(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        MultipartFile file = multipartRequest.getFile("file");
        if (file.isEmpty()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.FILE_NULL_ERROR);
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            return iEquipmentInfoService.saveEquipmentByBatch(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FILE_NONE_ERROR);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                inputStream = null;
            }
        }
    }

    @Log("I261-删除设备")
    @ApiOperation(value = "I261-删除设备")
    @PostMapping("/deleteEquipment")
    public Result deleteEquipment(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iEquipmentInfoService.deleteEquipment(vo.getUserId(), vo.getId());
    }

    @Log("I263-获取设备")
    @ApiOperation(value = "I263-获取设备")
    @GetMapping("/getEquipment")
    public Result getEquipment(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iEquipmentInfoService.getEquipment(id);
    }


    @Log("I260-获取设备列表")
    @ApiOperation(value = "I260-获取设备列表")
    @GetMapping("/getEquipmentList")
    public Result getEquipmentList(String equipmentNo, String status,String current) {
        return iEquipmentInfoService.getEquipmentList(equipmentNo,status, current);
    }

    @Log("I26X-更新设备激活状态")
    @ApiOperation(value = "I26X-更新设备激活状态")
    @PostMapping("/updateEquipmentStatus")
    public Result updateEquipmentStatus(@RequestBody @Validated VoChangeEquipmentStatus vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        return iEquipmentInfoService.changeStatus(vo.getUserId(),vo.getStatus(),vo.getId(),"");
    }

    @Log("I26X-扫码更新设备激活状态")
    @ApiOperation(value = "I26X-扫码更新设备激活状态")
    @PostMapping("/scanUpdateEquipmentStatus")
    public Result scanUpdateEquipmentStatus(@RequestBody @Validated VoScanEquipmentStatus vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        return iEquipmentInfoService.changeStatus(vo.getUserId(),"1","",vo.getEquipmentNo());
    }





}

