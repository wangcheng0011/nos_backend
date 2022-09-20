package com.knd.front.login.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.login.service.IPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 */
@RestController
@CrossOrigin
@RequestMapping("/front/login")
@Api(tags = "login")
@RequiredArgsConstructor
public class PageController {

    private final IPageService pageService;

    @Log("I11X-查询页面")
    @ApiOperation(value = "I11X-查询页面",notes = "I11X-查询页面")
    @GetMapping("/getPageMessage")
    public Result getPageMessage(HttpServletRequest httpServletRequest,
                                 @RequestParam(required = true, name = "key") String key,
                                 @RequestParam(required = true, name = "version") String version
                                 ){

        String platform = httpServletRequest.getHeader("platform");
        return pageService.getPage(key,version,platform);
    }
}
