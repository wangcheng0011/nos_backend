package com.knd.front.user.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.RankingTypeEnum;
import com.knd.front.user.dto.RankingListDto;
import com.knd.front.user.request.UserTrainPraiseRequest;
import com.knd.front.user.service.UserTrainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RestController
@CrossOrigin
@RequestMapping("/front/userTrain")
@Slf4j
@Api(tags = "排行榜userTrain")
@RequiredArgsConstructor
public class UserTrainController {

    private final UserTrainService userTrainService;

    @Log("获取用户爆发力,总力量,毅力,卡路里排行榜")
    @ApiOperation(value = "获取用户爆发力,总力量,毅力,卡路里排行榜",notes = "获取用户爆发力,总力量,毅力,卡路里排行榜")
    @GetMapping("/getMaxExplosivenessList")
    public Result<RankingListDto> getMaxExplosivenessList(@RequestParam(required = true) String userId,
                                                          @RequestParam RankingTypeEnum type,
                                                          @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
                                                          @RequestParam("size") @NotNull Long size){
        return userTrainService.getRankingList(type,date,userId,size);

    }

    @Log("点赞功能")
    @ApiOperation(value = "点赞功能",notes = "点赞功能")
    @PostMapping("/praise")
    public Result praise(@RequestBody @Valid UserTrainPraiseRequest userTrainPraiseRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userTrainService.praise(userTrainPraiseRequest);
    }

}
