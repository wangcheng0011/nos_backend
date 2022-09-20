package com.knd.front.pay.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.entity.VipMenu;
import com.knd.front.login.mapper.VipMenuMapper;
import com.knd.front.pay.request.*;
import com.knd.front.pay.service.IOrderService;
import com.knd.front.user.request.ObligationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author will
 */
@Api(tags = "订单")
@RestController
@CrossOrigin
@RequestMapping("/front/order/")
@Slf4j
public class OrderController {


    @Resource
    private IOrderService iOrderService;

    @Resource
    private VipMenuMapper vipMenuMapper;


   /**
    * ios 会员内购
    * @author will
    * @date 2021/7/15 15:52
    * @param iosInAppPurchaseRequest
    * @return com.knd.common.response.Result
    */
    @PostMapping(value = "/iosInAppPurchase")
    @Log("I11X-ios内购会员订单")
    @ApiOperation(value = "I11X-ios内购会员订单", notes = "I11X-ios内购会员订单")
    public Result iosInAppPurchase(@Valid @RequestBody IosInAppPurchaseRequest iosInAppPurchaseRequest) {
        if (StringUtils.isEmpty(iosInAppPurchaseRequest.getReceipt())){
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(),"receipt不能为空");
        }
        if (StringUtils.isEmpty(iosInAppPurchaseRequest.getGoodsId())){
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(),"productId不能为空");
        }
        VipMenu vipMenu = vipMenuMapper.selectById(iosInAppPurchaseRequest.getGoodsId());
        if(vipMenu == null) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(),"productId不存在");
        }
        return iOrderService.iosInAppPurchase(vipMenu,iosInAppPurchaseRequest);
    }


    /**
     * 待付款接口
     */
    @PostMapping(value = "/obligation")
    @ResponseBody
    @Log("待付款 下单")
    @ApiOperation(value = "待付款 下单", notes = "待付款 下单")
    public Result obligation(@Valid @RequestBody ObligationRequest obligationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iOrderService.obligation(obligationRequest);
    }

    @Log("创建网站订单")
    @ApiOperation(value = "创建网站订单")
    @PostMapping("/createOrderFromWebsite")
    public Result createOrderFromWebsite(@Valid @RequestBody
                                 CreateWebsiteOrderRequest createWebsiteOrderRequest
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
//        try {
//            String key = "3aed1da3ce642a9f7523f8ca701d6023";
//            String s = HttpUtils.httpGet("http://restapi.amap.com/v3/geocode/geo?key="+key+"&address=" + "江苏省常州市武进区凤林南路199号(新誉集团2号楼7楼)");
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return iOrderService.createOrderFromWebsite(createWebsiteOrderRequest);
    }


    @Log("获取订单详情")
    @ApiOperation(value = "获取订单详情")
    @GetMapping("/getOrder")
    public Result getOrder(String orderId,String orderNo) {
        //校验参数
        if (StringUtils.isEmpty(orderNo)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        String userId =UserUtils.getUserId();

       log.info("getOrder userId:{{}}",userId);
        return iOrderService.getOrderById(orderId,orderNo,userId);
    }

    @Log("新增或更新订单咨询")
    @ApiOperation(value = "新增或更新订单咨询")
    @PostMapping("/addOrUpdateOrderCousulting")
    public Result addOrUpdateOrderCousulting(@Valid @RequestBody OrderConsultingRequest orderConsultingRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        String userId = UserUtils.getUserId();
        orderConsultingRequest.setUserId(userId);
        log.info("getOrder userId:{{}}",userId);
        return iOrderService.addOrUpdateOrderConsulting(orderConsultingRequest);
    }


    @Log("查询订单咨询")
    @ApiOperation(value = "查询订单咨询")
    @GetMapping("/queryOrderCousulting")
    public Result queryOrderCousulting(@Valid QueryOrderConsultingRequest queryOrderConsultingRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iOrderService.queryOrderConsulting(queryOrderConsultingRequest);
    }

    @Log("更新订单状态")
    @ApiOperation(value = "更新订单状态")
    @PostMapping("/updateOrderStatus")
    public Result updateOrderStatus(@RequestBody @Validated ChangeOrderStatusRequest vo, BindingResult bindingResult) {
        //userId从token获取
        vo.setUserId(UserUtils.getUserId());
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        return iOrderService.updateOrderStatus(vo.getUserId(), vo.getStatus(), vo.getId(),vo.getTrackingNumber(),vo.getLogisticsCompanies(),vo.getDeliveryDate());
    }

}

