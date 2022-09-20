package com.knd.manage.equip.controller;

import com.knd.common.response.Result;
import com.knd.manage.equip.service.TestReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author zm
 * @since 2021-01-07
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/diagnosis")
@Api(tags = "检修报告")
@RequiredArgsConstructor
public class DiagnosisController {

    private final TestReportService testReportService;

    @GetMapping("/getTestReportList")
    @ApiOperation(value = "检修列表", notes = "检修列表")
    public Result getTestReportList(@RequestParam(required = false) String equipmentNo,
                                    @RequestParam("current") @NotNull String current){
        return testReportService.getTestReportList(equipmentNo,current);
    }


    @GetMapping("/getTestReportDetail")
    @ApiOperation(value = "检修详情", notes = "检修详情")
    public Result getTestReportDetail(@RequestParam(required = true) String id){
        return testReportService.getTestReportDetail(id);
    }

}
