package com.knd.manage.course.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.course.service.ICourseTrainningBlockService;
import com.knd.manage.course.vo.VoDeleteCourseBlock;
import com.knd.manage.course.vo.VoSaveCourseBlockInfo;
import com.knd.manage.course.vo.VoUpdateCourseBlocksSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "云端管理-course")
@RestController
@CrossOrigin
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class CourseTrainningBlockController {


    private final ICourseTrainningBlockService iCourseTrainningBlockService;


    @Log("I282-查询训练课Block列表")
    @ApiOperation(value = "I282-查询训练课Block列表")
    @GetMapping("/getCourseBlockInfo")
    public Result getCourseBlockInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseTrainningBlockService.getCourseBlockInfo(id);
    }


    @Log("I283-维护训练课Block信息")
    @ApiOperation(value = "I283-维护训练课Block信息")
    @PostMapping("/saveCourseBlockInfo")
    public Result saveCourseBlockInfo(@RequestBody @Validated VoSaveCourseBlockInfo vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (vo.getPostType().equals("1")) {
            //新增
            return iCourseTrainningBlockService.saveCourseBlockInfo(vo);
        } else {
            //编辑
            if (StringUtils.isEmpty(vo.getBlockId())) {
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iCourseTrainningBlockService.editCourseBlockInfo(vo);
        }


    }


    @Log("I284-批量更新训练课Block排序号")
    @ApiOperation(value = "I284-批量更新训练课Block排序号")
    @PostMapping("/updateCourseBlocksSort")
    public Result updateCourseBlocksSort(@RequestBody @Validated VoUpdateCourseBlocksSort vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseTrainningBlockService.updateCourseBlocksSort(vo.getUserId(), vo.getBlockSortList());
    }


    @Log(" 285-删除训练课Block信息")
    @ApiOperation(value = " 285-删除训练课Block信息")
    @PostMapping("/deleteCourseBlock")
    public Result deleteCourseBlock(@Validated @RequestBody VoDeleteCourseBlock vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseTrainningBlockService.deleteCourseBlock(vo.getUserId(), vo.getBlockId());
    }


}

