package com.knd.manage.mall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.homePage.entity.AmapAdCodeEntity;
import com.knd.manage.homePage.mapper.AmapAdCodeMapper;
import com.knd.manage.mall.entity.UserReceiveAddressEntity;
import com.knd.manage.mall.request.*;
import com.knd.manage.mall.service.IOrderInstallService;
import com.knd.manage.mall.service.ITbOrderService;
import com.knd.manage.user.service.IUserReceiveAddressService;
import com.knd.permission.bean.Token;
import com.knd.permission.config.TokenManager;
import com.knd.permission.util.RedisClientTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author will
 */
@Api(tags = "云端管理-商城-订单管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/mall/order")
@Log4j2
public class OrderController {

    @Resource
    private ITbOrderService iTbOrderService;

    @Resource
    private IOrderInstallService iOrderInstallService;

    @Resource
    private IUserReceiveAddressService userReceiveAddressService;

    @Resource
    private AmapAdCodeMapper amapAdCodeMapper;

    @Resource
    private TokenManager tokenManager;

    @Resource
    private RedisClientTokenUtil redisClient;

    private static final String TOKEN_PREFIX = "token:";

    @Log("获取订单详情")
    @ApiOperation(value = "获取订单详情")
    @GetMapping("/getOrder")
    public Result getOrder(String orderId) {
        //校验参数
        if (StringUtils.isEmpty(orderId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iTbOrderService.getOrderById(orderId);
    }

    @Log("获取后管订单列表")
    @ApiOperation(value = "获取后管订单列表 状态：1未付款,2已付款,3已发货,4已完成,5已取消,6已退款")
    @GetMapping("/getOrderList")
    public Result getOrderList(@Valid OrderListQueryParam orderListQueryParam, BindingResult bindingResult) {
        Token token = null;
        try {
            token = redisClient.get(TOKEN_PREFIX + UserUtils.getTokenCode());
        } catch (Exception e) {
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
        //校验传参
        if (bindingResult.hasErrors()) {
            //参数校验失败
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        //Set<String> roleList = token.getRoleList();

        return iTbOrderService.getOrderList(orderListQueryParam);

    }

    @Log("获取员工APP订单列表")
    @ApiOperation(value = "获取员工APP订单列表")
    @GetMapping("/getOrderList4App")
    public Result getOrderList4App(@Valid OrderQueryParam orderQueryParam, BindingResult bindingResult) {

        //校验传参
        if (bindingResult.hasErrors()) {
            //参数校验失败
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }

        Token token = null;
        String tokenCode = UserUtils.getTokenCode();
        String str = TOKEN_PREFIX + tokenCode;
        log.info("getOrderList4App tokenCode:{{}}", tokenCode);
        log.info("getOrderList4App str:{{}}", str);
        log.info("---------------------------------------------");
        try {
            // token = redisClient.get(str);
            token = tokenManager.getTokenByKey(str);
            log.info("getOrderList4App token:{{}}", token);
        } catch (Exception e) {
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
        if (null == token) {
            return ResultUtil.error(ResultEnum.UNAUTHORIZED);
        }
        log.info("getOrderList4App token:{{}}", token);
        log.info("---------------------------------------------");
//        Set<String> roleList = token.getRoleList();

        return iTbOrderService.getOrderList4App(orderQueryParam, token);

    }


    @Log("更新订单状态")
    @ApiOperation(value = "更新订单状态")
    @PostMapping("/updateOrderStatus")
    public Result updateOrderStatus(@RequestBody @Validated VoChangeOrderStatus vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        return iTbOrderService.updateOrderStatus(vo.getUserId(), vo.getStatus(), vo.getId(), vo.getTrackingNumber(),vo.getLogisticsCompanies(),vo.getSerialNo());
    }

    @Log("获取订单安装信息")
    @ApiOperation(value = "获取订单安装信息")
    @GetMapping("/getOrderInstall")
    public Result getOrderInstall(String orderId) {
        //校验参数
        if (StringUtils.isEmpty(orderId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iOrderInstallService.getInstall(orderId);
    }

    @Log("维护订单安装信息")
    @ApiOperation(value = "维护订单安装信息")
    @PostMapping("/addOrUpdateInstall")
    public Result addOrUpdateInstall(@RequestBody @Validated OrderInstallRequest request, BindingResult bindingResult) {
        //userId从token获取
        //   request.setUserId(UserUtils.getUserId());


        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iOrderInstallService.addOrUpdate(request);
    }


    @Log("获取安装人员信息")
    @ApiOperation(value = "获取安装人员信息")
    @GetMapping("/getInstallPersonList")
    public Result getInstallPersonList(String areaId) {
        //校验参数
        if (StringUtils.isEmpty(areaId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iOrderInstallService.getInstallPersonList(areaId);
    }

    @Log("处理安装流程")
    @ApiOperation(value = "处理安装流程")
    @PostMapping("/editInstall")
    public Result editInstall(@RequestBody @Validated EditInstallRequest request, BindingResult bindingResult) {
        //userId从token获取
//        request.setUserId(UserUtils.getUserId());
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iOrderInstallService.editInstall(request);
    }

    @Log("创建线下订单")
    @ApiOperation(value = "创建线下订单")
    @PostMapping("/createOrderFromOffline")
    public Result createOrderFromOffline(@Valid @RequestBody
                                                 CreateOfflineOrderRequest createOfflineOrderRequest
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTbOrderService.createOrderFromOffline(createOfflineOrderRequest);
    }

    @Log("新增或更新订单咨询")
    @ApiOperation(value = "新增或更新订单咨询")
    @PostMapping("/addOrUpdateOrderCousulting")
    public Result addOrUpdateOrderCousulting(@Valid @RequestBody OrderConsultingRequest orderConsultingRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTbOrderService.addOrUpdateOrderConsulting(orderConsultingRequest);
    }

    @Log("查询订单咨询")
    @ApiOperation(value = "查询订单咨询")
    @GetMapping("/queryCousulting")
    public Result queryCousulting(@Valid QueryOrderConsultingRequest queryOrderConsultingRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTbOrderService.queryOrderConsulting(queryOrderConsultingRequest);
    }


    @Log("I26X-excel批量导入订单咨询")
    @ApiOperation(value = "I26X-excel批量导入订单咨询")
    @PostMapping("/saveOrderCousultingByBatch")
    public Result saveOrderCousultingByBatch(HttpServletRequest request) throws Exception {
        log.info("saveOrderCousultingByBatch request:", request);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        log.info("saveOrderCousultingByBatch multipartRequest:", multipartRequest);
        MultipartFile file = multipartRequest.getFile("file");
        log.info("saveOrderCousultingByBatch file:", file);
        if (file.isEmpty()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.FILE_NULL_ERROR);
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            log.info("saveOrderCousultingByBatch inputStream:", inputStream);
            log.info("saveOrderCousultingByBatch originalFilename:", file.getOriginalFilename());
            return iTbOrderService.saveOrderCousultingByBatch(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FILE_NONE_ERROR);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                inputStream = null;
            }
        }
    }

    /**
     * 获取会员收货地址详情
     */
    @ApiOperation(value = "获取会员收货地址详情", notes = "获取会员收货地址详情")
    @Log("获取会员收货地址详情")
    @GetMapping("/getUserReceiveAddress")
    public Result getUserReceiveAddress(@RequestParam("id") String id){
        return ResultUtil.success(iTbOrderService.getUserReceiveAddressById(id));
    }


    /**
     * 列表
     */
    @ApiOperation(value = "获取收货地址列表", notes = "获取收货地址列表")
    @Log("获取收货地址列表")
    @GetMapping("/receiveAddresslist")
    public Result receiveAddresslist(String userId){
        log.info("receiveAddresslist userId",userId);
        if(StringUtils.isEmpty(userId)){
            userId = UserUtils.getUserId();
        }
        log.info("-------------------receiveAddresslist-------------");
        List<UserReceiveAddressEntity> list = userReceiveAddressService
                .list(
                        new QueryWrapper<UserReceiveAddressEntity>()
                                .eq("userId", userId).eq("deleted", "0"));
        //.orderByDesc("defaultStatus")
        list.stream().forEach(userReceiveAddressEntity->{
            //省拼接
            QueryWrapper<AmapAdCodeEntity> provinceQueryWrapper = new QueryWrapper<>();
            provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
            provinceQueryWrapper.eq("deleted", "0");
            provinceQueryWrapper.last("limit 1");
            AmapAdCodeEntity provinceAmapAdCodeEntity = amapAdCodeMapper.selectOne(provinceQueryWrapper);
            if(StringUtils.isNotEmpty(provinceAmapAdCodeEntity)){
                String provinceAdCode =provinceAmapAdCodeEntity.getAdcode();
                userReceiveAddressEntity.setProvince(userReceiveAddressEntity.getProvince()+","+provinceAdCode);
            }

            //市拼接
            QueryWrapper<AmapAdCodeEntity> cityQueryWrapper = new QueryWrapper<>();
            cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
            cityQueryWrapper.eq("deleted", "0");
            cityQueryWrapper.last("limit 1");
            AmapAdCodeEntity cityAmapAdCodeEntity = amapAdCodeMapper.selectOne(cityQueryWrapper);
            if(StringUtils.isNotEmpty(cityAmapAdCodeEntity)) {
                String cityAdCode = cityAmapAdCodeEntity.getAdcode();
                userReceiveAddressEntity.setCity(userReceiveAddressEntity.getCity() + "," + cityAdCode);
            }
            //区拼接
            QueryWrapper<AmapAdCodeEntity> regionWrapper = new QueryWrapper<>();
            regionWrapper.eq("name",userReceiveAddressEntity.getRegion());
            regionWrapper.eq("deleted", "0");
            regionWrapper.last("limit 1");
            AmapAdCodeEntity regionAmapAdCodeEntity = amapAdCodeMapper.selectOne(regionWrapper);
            if(StringUtils.isNotEmpty(regionAmapAdCodeEntity)) {
                String regionAdCode = regionAmapAdCodeEntity.getAdcode();
                userReceiveAddressEntity.setRegion(userReceiveAddressEntity.getRegion() + "," + regionAdCode);
            }
        });
        return ResultUtil.success(list);
    }

    /**
     * 保存更新收货地址
     */
    @ApiOperation(value = "保存更新收货地址", notes = "保存更新收货地址")
    @Log("保存更新收货地址")
    @PostMapping("/save")
    public Result save(@RequestBody UserReceiveAddressRequest userReceiveAddressRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if(StringUtils.isEmpty(userReceiveAddressRequest.getUserId())){
            userReceiveAddressRequest.setUserId(UserUtils.getUserId());
        }
        return ResultUtil.success(iTbOrderService.saveUserReceiveAddressEntity(userReceiveAddressRequest));
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @Log("删除收货地址")
    @PostMapping("/delete")
    public Result delete(@RequestBody String[] ids){
        log.info("ids:{{}}",ids);
        UserReceiveAddressEntity byId = userReceiveAddressService.getById(ids[0]);
        log.info("byId:{{}}",byId);
        if("1".equals(byId.getDefaultStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该地址是默认地址，请先更换默认收货地址");
        }
        userReceiveAddressService.removeByIds(Arrays.asList(ids));
        return  ResultUtil.success();
    }



}

