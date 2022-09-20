package com.knd.front.common.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.common.service.ICodeService;
import com.knd.front.entity.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/front/common")
@Api(tags = "common")
@Slf4j
public class CodeController {


    @Autowired
    private ICodeService iCodeService;

    @GetMapping("/queryCodeList")
    @Log("I010-获取Code小分类列表")
    @ApiOperation(value = "I010-获取Code小分类列表", notes = "I010-获取Code小分类列表")
    public Result queryCodeList(@RequestParam  String bigClassCode) {
        QueryWrapper<Code> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bigClassCode", bigClassCode);
        queryWrapper.eq("deleted","0");
        queryWrapper.select("smallClassCode","smallClassName");

        List<Code> list = iCodeService.list(queryWrapper);
        return ResultUtil.success(list);
    }


}

