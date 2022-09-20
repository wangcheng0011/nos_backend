package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseLabelService;
import com.knd.manage.basedata.vo.VoGetLabelList;
import com.knd.manage.basedata.vo.VoSaveLabel;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 标签管理
 */
@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseLabelController {

    private final IBaseLabelService baseLabelService;

    @Log("获取标签列表")
    @ApiOperation(value = "获取标签列表")
    @GetMapping("/getLabelList")
    public Result getLabelList(@Validated VoGetLabelList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseLabelService.getLabelList(vo);
    }

    @Log("维护标签信息")
    @ApiOperation(value = "维护标签信息")
    @PostMapping("/saveLabel")
    public Result saveLabel(@RequestBody @Validated VoSaveLabel vo, BindingResult bindingResult) {
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
            return baseLabelService.add(vo.getUserId(), vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getLabelId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseLabelService.edit(vo.getUserId(), vo);
        }

    }

    @Log("I211-删除标签")
    @ApiOperation(value = "I211-删除标签")
    @PostMapping("/deleteLabel")
    public Result deleteLabel(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseLabelService.delete(vo.getUserId(), vo.getId());
    }


    @Log("I213-获取标签")
    @ApiOperation(value = "I213-获取标签")
    @GetMapping("/getLabel")
    public Result getLabel(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseLabelService.getLabel(id);
    }


}
