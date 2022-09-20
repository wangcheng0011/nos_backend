package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseElementService;
import com.knd.manage.basedata.service.IbaseFloorService;
import com.knd.manage.basedata.vo.VoGetFloorList;
import com.knd.manage.basedata.vo.VoSaveElement;
import com.knd.manage.basedata.vo.VoSaveFloor;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseFloorController {

    private final IbaseFloorService baseFloorService;
    private final IBaseElementService baseElementService;


    @Log("I210-获取楼层列表")
    @ApiOperation(value = "I210-获取楼层列表")
    @GetMapping("/getFloorList")
    public Result getFloorList(@Validated VoGetFloorList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseFloorService.getFloorList(vo);
    }

    @Log("I213-获取楼层详情")
    @ApiOperation(value = "I213-获取楼层详情")
    @GetMapping("/GetFloor")
    public Result GetFloor(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseFloorService.getFloor(id);
    }

    @Log("I211-删除楼层")
    @ApiOperation(value = "I211-删除楼层")
    @PostMapping("/deleteFloor")
    public Result deleteFloor(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseFloorService.deleteFloor(vo.getUserId(), vo.getId());
    }

    @Log("I212-维护楼层")
    @ApiOperation(value = "I212-维护楼层")
    @PostMapping("/saveFloor")
    public Result saveFloor(@RequestBody @Validated VoSaveFloor vo, BindingResult bindingResult) {
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
            return baseFloorService.addFloor(vo.getUserId(), vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getFloorId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseFloorService.editFloor(vo.getUserId(), vo);
        }
    }

    @Log("I212-维护元素")
    @ApiOperation(value = "I212-维护元素")
    @PostMapping("/saveElement")
    public Result saveElement(@RequestBody @Validated VoSaveElement vo, BindingResult bindingResult) {
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
            return baseElementService.addElement(vo.getUserId(), vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getElementId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseElementService.editElement(vo.getUserId(), vo);
        }
    }

    @Log("I211-删除元素")
    @ApiOperation(value = "I211-删除元素")
    @PostMapping("/deleteElement")
    public Result deleteElement(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseElementService.deleteElement(vo.getUserId(), vo.getId());
    }

    @Log("I213-获取元素详情")
    @ApiOperation(value = "I213-获取元素详情")
    @GetMapping("/getElement")
    public Result getElement(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseElementService.getElement(id);
    }


}
