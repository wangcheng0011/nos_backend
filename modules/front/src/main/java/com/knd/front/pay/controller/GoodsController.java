package com.knd.front.pay.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.pay.request.GoodsListRequest;
import com.knd.front.pay.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author will
 */
@Api(tags = "商品")
@RestController
@CrossOrigin
@RequestMapping("/front/goods/")
public class GoodsController {


    @Autowired
    private IGoodsService iGoodsService;




    @Log("获取商品详情")
    @ApiOperation(value = "获取商品详情")
    @GetMapping("/getGoods")
    public Result getGoods(String goodsId) {
        //校验参数
        if (StringUtils.isEmpty(goodsId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iGoodsService.getGoods(goodsId);
    }


    @Log("获取商品列表")
    @ApiOperation(value = "获取商品列表")
    @PostMapping("/getGoodsList")
    public Result getGoodsList(@Valid @RequestBody GoodsListRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iGoodsService.getGoodsList(request);
    }


}

