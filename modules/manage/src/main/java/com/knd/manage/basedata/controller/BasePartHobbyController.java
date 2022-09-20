package com.knd.manage.basedata.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBasePartHobbyService;
import com.knd.manage.basedata.vo.VoGetHobbyList;
import com.knd.manage.basedata.vo.VoSaveHobby;
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
public class BasePartHobbyController {

    private final IBasePartHobbyService iBasePartHobbyService;

    @Log("I212-维护爱好")
    @ApiOperation(value = "I212-维护爱好")
    @PostMapping("/saveHobby")
    public Result saveHobby(@RequestBody @Validated VoSaveHobby vo, BindingResult bindingResult) {
        //userId从token获取
        String userId = UserUtils.getUserId();
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
            return iBasePartHobbyService.add(userId, vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getHobbyId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iBasePartHobbyService.edit(userId, vo);
        }

    }

    @Log("I211-删除爱好")
    @ApiOperation(value = "I211-删除爱好")
    @PostMapping("/deleteHobby")
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
        return iBasePartHobbyService.deleteHobby(vo.getUserId(), vo.getId());
    }


    @Log("I213-获取爱好")
    @ApiOperation(value = "I213-获取爱好")
    @GetMapping("/GetHobby")
    public Result getHobby(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBasePartHobbyService.getHobby(id);
    }

    @Log("I210-获取爱好列表")
    @ApiOperation(value = "I210-获取爱好列表")
    @GetMapping("/getHobbyList")
    public Result getHobbyList(@Validated VoGetHobbyList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBasePartHobbyService.getHobbyList(vo.getHobby(), vo.getCurrent());
    }


}
