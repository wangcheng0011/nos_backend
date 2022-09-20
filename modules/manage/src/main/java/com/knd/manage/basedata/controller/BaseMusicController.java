package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseMusicService;
import com.knd.manage.basedata.vo.VoSaveMusic;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseMusicController {

    private final IBaseMusicService baseMusicService;

    @Log("获取音乐列表")
    @ApiOperation(value = "获取音乐列表")
    @GetMapping("/getMusicList")
    public Result getMusicList(@ApiParam("音乐名称") @RequestParam(required = false) String name,
                               @ApiParam("当前页") @RequestParam(required = true) String current,
                               @ApiParam("总数量") @RequestParam(required = true) String size){
        return baseMusicService.getMusicList(name,current,size);
    }

    @Log("保存音乐")
    @ApiOperation(value = "保存音乐")
    @PostMapping("/saveMusic")
    public Result saveMusic(@RequestBody @Validated VoSaveMusic vo,BindingResult bindingResult){
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseMusicService.addMusic(vo);
    }

    @Log("删除音乐")
    @ApiOperation(value = "删除音乐")
    @PostMapping("/deleteMusic")
    public Result deleteMusic(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseMusicService.deleteMusic(vo.getUserId(), vo.getId());
    }

}
