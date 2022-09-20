package com.knd.manage.mall.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.mall.request.UpdateAttrRequest;
import com.knd.manage.mall.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author will
 */
@Api(tags = "云端管理-商城-分类管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/mall/category")
public class CategoryController {

    @Resource
    private ICategoryService iCategoryService;


//    @Log("新增或编辑属性")
//    @ApiOperation(value = "新增或编辑属性")
//    @PostMapping("/addOrUpdate")
//    public Result addOrUpdate(@RequestBody @Validated CreateAttrRequest createAttrRequest, BindingResult bindingResult) {
//        //userId从token获取
//        createAttrRequest.setUserId(UserUtils.getUserId());
//
//        //判断结果
//        if (bindingResult.hasErrors()) {
//            //参数校验失败
//            String error = bindingResult.getFieldError().getDefaultMessage();
//            return ResultUtil.error("U0995", error);
//        }
//
//        if("1".equals(createAttrRequest.getPostType())){
//            return iAttrService.add(createAttrRequest);
//        }else{
//            return iAttrService.update(createAttrRequest);
//        }
//
//    }




    @Log("获取分类列表")
    @ApiOperation(value = "获取分类列表")
    @GetMapping("/getCategoryList")
    public Result getCategoryList(String categoryName, String current) {
        return iCategoryService.getCategoryList(categoryName, current);
    }

    @Log("获取分类属性列表")
    @ApiOperation(value = "获取分类属性列表")
    @GetMapping("/getAttrByCategoryId")
    public Result getAttrByCategoryId(String categoryId) {
        return iCategoryService.getAttrByCategoryId(categoryId);
    }


    @Log("更新分类属性")
    @ApiOperation(value = "更新分类属性")
    @PostMapping("/updateAttr")
    public Result updateAttr(@RequestBody @Validated UpdateAttrRequest vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if("0".equals(vo.getPostType())) {
            return iCategoryService.addAttr(vo);
        }else if("1".equals(vo.getPostType())) {
            return iCategoryService.updateAttr(vo);
        }else{
            return iCategoryService.deleteAttr(vo.getId());
        }

    }


}

