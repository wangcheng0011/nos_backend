package com.knd.front.common.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.common.service.SearchService;
import com.knd.front.pay.request.GoodsListRequest;
import com.knd.front.pay.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@RestController
@CrossOrigin
@RequestMapping("/front/common")
@Api(tags = "common")
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final IGoodsService iGoodsService;


    @Log("查询动作，课程，商城，用户")
    @ApiOperation(value = "查询动作，课程，商城，用户")
    @GetMapping("/getActionCourseGoodsUser")
    public Result getActionCourseGoodsUser(@ApiParam("查询类型：0动作 1课程 2商城 3用户") @RequestParam(required = true) String type,
                                   @ApiParam("名称") @RequestParam(required = false) String name,
                                   @ApiParam("当前页")  @RequestParam(required = true) String current,
                                   @ApiParam("用户id") @RequestParam(required = true) String userId){
        if ("0".equals(type)){
            return searchService.getActionList(current,userId,name);
        }else if ("1".equals(type)){
            return searchService.getCourseList(current,userId,name);
        }else if ("2".equals(type)){
            GoodsListRequest request = new GoodsListRequest();
            request.setCurrent(current);
            if (StringUtils.isNotEmpty(name)){
                request.setGoodName(name);
            }
            return iGoodsService.getGoodsList(request);
        }else if ("3".equals(type)){
            return searchService.getUserList(userId,name,current);
        }else{
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"查询类型错误");
        }
    }
}
