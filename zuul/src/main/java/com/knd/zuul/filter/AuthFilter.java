package com.knd.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.userutil.UserUtils;
import com.knd.zuul.dto.AuthUrl;
import com.knd.zuul.dto.EquipmentInfo;
import com.knd.zuul.dto.PassUri;
import com.knd.zuul.dto.Token;
import com.knd.zuul.feign.AuthFeignClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Slf4j
public class AuthFilter extends ZuulFilter {

//    @Resource
//    private TokenManager tokenManager;

    @Resource
    private AuthFeignClient authFeignClient;

    @Resource
    private RouteLocator routeLocator;

    @Value("${pass.swagger-ui}")
    private String[] swaggerPathArr;

    private static final String defaultToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJzRUo0dkU0UyIsInBsYXRmb3JtIjoiYXBwIiwiaWF0IjoxNTk2MDk5MjQwfQ._LYGVTmcA1izjPUBP3Xqfx3GqhfljZkzBFLN424TVrM";


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        log.info("ZUUL请求URI:{}", requestURI);



        // swagger 过滤
        for (String swaggerPath : swaggerPathArr) {
            if (requestURI.lastIndexOf(swaggerPath) > -1) {
                StringBuilder message = new StringBuilder();
                message.append("【身份认证】 swagger无需拦截");
                message.append("IP: " + request.getRemoteAddr() + ", ");
                message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
                log.info(message.toString());
                return null;
            }
        }

        //获取所有路由信息，找到该请求对应的appName
        List<Route> routeList = routeLocator.getRoutes();
        String appName = null;  // 路由名称
        String path = "";  // 真实路径（对比数据库）
        for (Route route : routeList) {
            if (requestURI.startsWith(route.getPrefix())) {
                //取到该请求对应的微服务名字
                appName = route.getLocation();
                path = requestURI.replace(route.getPrefix(), "");
            }
        }
        if (appName == null) {
            buildUnAuth(ctx, HttpStatus.NOT_FOUND);
            return null;
        }
        if ("front".equals(appName)) {
            //判断传入的设备编号是否授权
            String equipmentNo = request.getHeader("SID");
            EquipmentInfo equipmentInfo = null;
            if (!StringUtils.isEmpty(equipmentNo)) {
                equipmentInfo = authFeignClient.queryEquipmentNo(equipmentNo);
            }
            if (equipmentInfo == null) {
                //拦截
                StringBuilder message = new StringBuilder();
                message.append("【身份认证】 设备未授权！");
                message.append("IP: " + request.getRemoteAddr() + ", ");
                message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
                log.warn(message.toString());
                buildUnAuth(ctx, HttpStatus.UNAUTHORIZED);
                return null;
            }
        }

        // 白名单过滤 (不需要token的url)
        List<PassUri> passUriList = authFeignClient.queryPassList();
        if (passUriList.stream().filter(t -> t.getType() == 1)
                .anyMatch(t -> requestURI.equals(t.getUri()) && requestMethod.equalsIgnoreCase(t.getMethod()))) {

            StringBuilder message = new StringBuilder();
            message.append("【身份认证】 白名单无需拦截");
            message.append("IP: " + request.getRemoteAddr() + ", ");
            message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.info(message.toString());
            return null;

        }


        //获取传来的参数accessToken
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(accessToken)) {
            StringBuilder message = new StringBuilder();
            message.append("【身份认证】 无token信息");
            message.append("IP: " + request.getRemoteAddr() + ", ");
            message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.warn(message.toString());
            buildUnAuth(ctx, HttpStatus.UNAUTHORIZED);
            return null;
        }

        if (UserUtils.getToken() == null) {
            StringBuilder message = new StringBuilder();
            message.append("【身份认证】 token无法解析");
            message.append("IP: " + request.getRemoteAddr() + ", ");
            message.append("Token: " + accessToken + ", ");
            message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.warn(message.toString());
            buildUnAuth(ctx, HttpStatus.UNAUTHORIZED);
            return null;
        }

        /**
         * 手机接口验证(Platform: (0- 手机， 1-网页))
         */
        if (!"1".equals(UserUtils.getPlatform())) {
            StringBuilder mess = new StringBuilder();
            mess.append("【身份认证】 手机端接口验证是否需要登录才可以访问");
            mess.append("IP: " + request.getRemoteAddr() + ", ");
            mess.append("Token: " + accessToken + ", ");
            mess.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.info(mess.toString());

            //验证当前接口是否是管理端
            if (!"front".equals(appName)) {
                StringBuilder message = new StringBuilder();
                message.append("【身份认证】 手机接口不允许访问管理端");
                message.append("IP: " + request.getRemoteAddr() + ", ");
                message.append("Token: " + accessToken + ", ");
                message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
                log.warn(message.toString());
                buildUnAuth(ctx, HttpStatus.UNAUTHORIZED);
            }

            //判断传入的设备编号是否授权
//            String equipmentNo = request.getHeader("SID");
//            EquipmentInfo equipmentInfo = null;
//            if(!StringUtils.isEmpty(equipmentNo)) {
//                equipmentInfo= authFeignClient.queryEquipmentNo(equipmentNo);
//            }
//            if(equipmentInfo == null ) {
//                //拦截
//                StringBuilder message = new StringBuilder();
//                message.append("【身份认证】 设备未授权！");
//                message.append("IP: " + request.getRemoteAddr() + ", ");
//                message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
//                log.warn(message.toString());
//                buildUnAuth(ctx, HttpStatus.UNAUTHORIZED);
//                return null;
//            }

            //检查这个请求是否是这里白名单的路径之一【需要登录才可以访问】
            if (passUriList.stream().filter(t -> t.getType() == 2)
                    .anyMatch(t -> requestURI.equals(t.getUri()) && requestMethod.equalsIgnoreCase(t.getMethod()))) {
                //是
                //如果是，redis检查是否已经登录，未登录则拦截
                log.info("-----------------如果是，redis检查是否已经登录，未登录则拦截---------------------");
                Token token = authFeignClient.queryToken(accessToken);
                log.info("run token:{{}}",token);
                if (token == null) {
                    //未登录
                    //拦截
                    StringBuilder message = new StringBuilder();
                    message.append("【身份认证】 未登录，没有权限，服务器拒绝请求");
                    message.append("IP: " + request.getRemoteAddr() + ", ");
                    message.append("Token: " + accessToken + ", ");
                    message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
                    log.warn(message.toString());
                    buildUnAuth(ctx, HttpStatus.FORBIDDEN);
                }
            }
            //检查用户账号状态
            if (!accessToken.equals(defaultToken)) {
                //不是默认token
                //微服务查询用户状态，1:冻结，2：删除，3：正常
                log.info("-----------------微服务查询用户状态，1:冻结，2：删除，3：正常---------------------");
                String state = authFeignClient.queryUserState(UserUtils.getUserId());
                log.info("run state:{{}}",state);
                if (!state.equals("3")) {
                    //异常操作
                    return doException(state, ctx, request, accessToken);
                }
            }
            log.info("-----------------都不是，则直接通过---------------------");
            //都不是，则直接通过
            return null;
        }


        Token token = authFeignClient.queryToken(accessToken);
        if (token == null) {
            StringBuilder message = new StringBuilder();
            message.append("【身份认证】未找到token信息");
            message.append("IP: " + request.getRemoteAddr() + ", ");
            message.append("Token: " + accessToken + ", ");
            message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.warn(message.toString());
            buildUnAuth(ctx, HttpStatus.UNAUTHORIZED);
            return null;
        }


        if (passUriList.stream().filter(t -> t.getType() == 2)
                .anyMatch(t -> requestURI.equals(t.getUri()) && requestMethod.equalsIgnoreCase(t.getMethod()))) {
            StringBuilder message = new StringBuilder();
            message.append("【身份认证】 白名单只需要token无需验证权限");
            message.append("IP: " + request.getRemoteAddr() + ", ");
            message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.info(message.toString());
            return null;
        }


        // 用户权限验证
        Set<AuthUrl> permissionList = token.getAuthUrlList();
        String finalPath = path;
        boolean flag = permissionList.stream().anyMatch(t ->
                finalPath.equals(t.getUrl())
                        && requestMethod.equalsIgnoreCase(t.getMethod()));
        if (!flag) {
            StringBuilder message = new StringBuilder();
            message.append("【身份认证】 没有权限，服务器拒绝请求");
            message.append("IP: " + request.getRemoteAddr() + ", ");
            message.append("Token: " + accessToken + ", ");
            message.append("Api: " + request.getMethod() + ":" + request.getRequestURI() + ", ");
            log.warn(message.toString());
            buildUnAuth(ctx, HttpStatus.FORBIDDEN);
            return null;
        }


        return null;
    }

    private void buildUnAuth(RequestContext ctx, HttpStatus status) throws ZuulException {
        HttpServletResponse response = ctx.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ctx.setSendZuulResponse(false);
        response.setStatus(status.value());

    }

    private Object doException(String state, RequestContext ctx, HttpServletRequest request, String accessToken) {
        log.info("doException state:{{}}",state);
        StringBuilder message = new StringBuilder();
        HttpServletResponse response = ctx.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ctx.setSendZuulResponse(false);
        ResultEnum resultEnum;
        if (state.equals("1")) {
            log.info("---------------------doException 【身份认证】 用户已被冻结--------------------");
            message.append("【身份认证】 用户已被冻结");
            message.append("IP: ").append(request.getRemoteAddr()).append(", ");
            message.append("Token: ").append(accessToken).append(", ");
            message.append("Api: ").append(request.getMethod()).append(":").append(request.getRequestURI()).append(", ");
            log.warn(message.toString());
            log.info(message.toString());
            resultEnum = ResultEnum.USER_FROZEN_ERROR;
        } else {
            log.info("---------------------doException 【身份认证】 用户已被删除--------------------");
            message.append("state:"+state+"【身份认证】 用户已被删除");
            message.append("IP: ").append(request.getRemoteAddr()).append(", ");
            message.append("Token: ").append(accessToken).append(", ");
            message.append("Api: ").append(request.getMethod()).append(":").append(request.getRequestURI()).append(", ");
            log.warn("state:"+state+"message:"+message.toString());
            log.info("state:"+state+"message:"+message.toString());
            resultEnum = ResultEnum.USER_DELETE_ERROR;
        }
//        //200状态码
//        response.setStatus(HttpStatus.OK.value());
//        return ResultUtil.error(resultEnum);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        try {
            //200状态码
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            //401状态码
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            e.printStackTrace();
        }
        return null;
    }


}
