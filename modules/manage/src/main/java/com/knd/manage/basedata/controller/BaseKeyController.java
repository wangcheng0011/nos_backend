package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.BaseKeyService;
import com.knd.manage.basedata.vo.VoSaveKey;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseKeyController {

    private final BaseKeyService baseKeyService;

    @Log("I230-获取Key列表")
    @ApiOperation(value = "I230-获取Key列表")
    @GetMapping("/getKeyKeyList")
    public Result getKeyList(@ApiParam(value = "key名称") @RequestParam(required = false) String keyValue,
                                   @ApiParam(value = "当前页") @RequestParam(required = false) String current) {
        return baseKeyService.getKeyList(keyValue,current);
    }

    @Log("I230-获取Key")
    @ApiOperation(value = "I230-获取Key")
    @GetMapping("/getKey")
    public Result getKey(@ApiParam(value = "id") @RequestParam(required = true) String id){
        return baseKeyService.getKey(id);
    }

    @Log("I230-删除Key")
    @ApiOperation(value = "I230-删除Key")
    @PostMapping("/deleteKey")
    public Result deleteKey(@RequestBody  @Validated VoId vo, BindingResult bindingResult){
        //userId从token获取
        if (StringUtils.isEmpty(vo.getUserId())){
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseKeyService.deleteKey(vo.getUserId(),vo.getId());
    }

    @Log("I232-维护Key")
    @ApiOperation(value = "I232-维护Key")
    @PostMapping("/saveKey")
    public Result saveKey(@RequestBody  @Validated VoSaveKey vo,BindingResult bindingResult){
        //userId从token获取
        if (StringUtils.isEmpty(vo.getUserId())){
            vo.setUserId(UserUtils.getUserId());
        }

        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (vo.getPostType().equals("1")) {
            //新增
            return baseKeyService.add(vo);
        }else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getKeyId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseKeyService.edit(vo);
        }
    }

}
