package com.knd.manage.basedata.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBasePartShapeService;
import com.knd.manage.basedata.vo.VoGetShapeList;
import com.knd.manage.basedata.vo.VoSaveShape;
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
public class BasePartShapeController {

    private final IBasePartShapeService iBasePartShapeService;

    @Log("I212-维护体型")
    @ApiOperation(value = "I212-维护体型")
    @PostMapping("/saveShape")
    public Result saveHobby(@RequestBody @Validated VoSaveShape vo, BindingResult bindingResult) {
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
            if(StringUtils.isEmpty(vo.getPartId())){
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            //新增
            return iBasePartShapeService.add(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getShapeId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iBasePartShapeService.edit(vo);
        }

    }

    @Log("I211-删除体型")
    @ApiOperation(value = "I211-删除体型")
    @PostMapping("/deleteShape")
    public Result deleteHobby(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBasePartShapeService.deleteShape(vo.getUserId(), vo.getId());
    }


    @Log("I213-获取体型")
    @ApiOperation(value = "I213-获取体型")
    @GetMapping("/GetShape")
    public Result GetHobby(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBasePartShapeService.GetShape(id);
    }

    @Log("I210-获取体型列表")
    @ApiOperation(value = "I210-获取体型列表")
    @GetMapping("/getShapeList")
    public Result getHobbyList(@Validated VoGetShapeList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBasePartShapeService.getShapeList(vo.getShape(), vo.getCurrent());
    }


}
