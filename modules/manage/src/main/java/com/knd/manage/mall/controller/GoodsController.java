package com.knd.manage.mall.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.mall.request.CreateGoodsRequest;
import com.knd.manage.mall.request.GetOrderInfoRequest;
import com.knd.manage.mall.request.GoodsListRequest;
import com.knd.manage.mall.request.VoChangeGoodsPublishStatus;
import com.knd.manage.mall.service.IGoodsService;
import com.knd.pay.request.ParseOrderNotifyRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * @author will
 */
@Log4j2
@Api(tags = "云端管理-商城-商品管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/mall/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final IGoodsService iGoodsService;



    @Log("创建商品")
    @ApiOperation(value = "创建商品")
    @PostMapping("/createGoods")
    public Result createGoods(@RequestBody @Validated CreateGoodsRequest createGoodsRequest, BindingResult bindingResult) {
        if(StringUtils.isEmpty(createGoodsRequest.getUserId())){
            //userId从token获取
            createGoodsRequest.setUserId(UserUtils.getUserId());
        }

        //判断结果
        if (bindingResult.hasErrors()) {
            //参数校验失败
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        //商品必须上传封面图片
        if (StringUtils.isEmpty(createGoodsRequest.getPicAttachName(),createGoodsRequest.getPicAttachNewName())){
            //参数校验失败
            return ResultUtil.error("U0995", "商品必须上传封面图片");
        }
        if("1".equals(createGoodsRequest.getPostType())){
            return iGoodsService.add(createGoodsRequest);
        }else{
            return iGoodsService.update(createGoodsRequest);
        }

    }


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

    @Log("获取商品属性列表")
    @ApiOperation(value = "获取商品属性列表")
    @GetMapping("/getGoodsAttrValueList")
    public Result getGoodsAttrValueList(String goodsId,String categoryId) {
        //校验参数
        if (StringUtils.isEmpty(goodsId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iGoodsService.getGoodsAttrValueList(goodsId,categoryId);
    }


    @Log("获取商品列表")
    @ApiOperation(value = "获取商品列表")
    @GetMapping("/getGoodsList")
    public Result getGoodsList(String goodsType,String goodsName, String current,HttpServletRequest httpServletRequest) {
        String platform = httpServletRequest.getHeader("platform");
        return iGoodsService.getGoodsList(goodsType,goodsName, current,platform);
    }

    @Log("根据分类获取商品列表")
    @ApiOperation(value = "根据分类获取商品列表")
    @PostMapping("/getGoodsListByType")
    public Result getGoodsListByType(@Valid @RequestBody GoodsListRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iGoodsService.getGoodsListByType(request);
    }






    @Log("更新商品发布状态")
    @ApiOperation(value = "更新商品发布状态")
    @PostMapping("/updateGoodsPublishStatus")
    public Result updateGoodsPublishStatus(@RequestBody @Validated VoChangeGoodsPublishStatus vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        return iGoodsService.updateGoodsPublishStatus(vo.getUserId(),vo.getStatus(),vo.getId());
    }

    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @PostMapping("/deleteGoods")
    public Result deleteGoods(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iGoodsService.deleteGoods(vo.getUserId(), vo.getId());
    }



    @Log("I11X-查询订单支付状态")
    @ApiOperation(value = "I11X-查询订单支付状态",notes = "I11X-查询订单支付状态")
    @GetMapping("/tradeQuery")
    public Result tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(name = "tradeNo") String tradeNo){

        return iGoodsService.tradeQuery(outTradeNo,tradeNo);
    }


    @Log("I11X-JS 调起支付签名")
    @ApiOperation(value = "I11X-JS 调起支付签名",notes = "I11X-JS 调起支付签名")
    @GetMapping("/createOfficialAccountUnifiedOrder")
    public Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                                    @RequestParam(required = false, name = "orderNo") String orderNo,
                                                    @RequestParam(required = false, name = "amount") BigDecimal amount){

        return iGoodsService.createOfficialAccountUnifiedOrder(openid,orderNo,amount);
    }


    @Log("I11X-查询用户订单支付状态")
    @ApiOperation(value = "I11X-查询用户订单支付状态",notes = "I11X-查询用户订单支付状态 状态：1未付款,2已付款,3已发货,4已完成,5已取消,6已退款")
    @GetMapping("/tradeStatusQuery")
    public Result tradeStatusQuery(String current,String userId,String status,String platform){
        log.info("tradeStatusQuery current:{{}}",current);
        log.info("tradeStatusQuery userId:{{}}",userId);
        log.info("tradeStatusQuery status:{{}}",status);
        log.info("tradeStatusQuery platform:{{}}",platform);
        if(StringUtils.isEmpty(userId)){
            //userId从token获取
            userId=UserUtils.getUserId();
        }
        return iGoodsService.tradeStatusQuery(current,userId,status,platform);
    }

    @Log("I11X-获取支付订单")
    @ApiOperation(value = "I11X-获取支付订单",notes = "I11X-获取支付订单")
    @PostMapping("/getPayInfo")
    public Result getPayInfo(HttpServletResponse response, @Valid @RequestBody GetOrderInfoRequest getOrderInfoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if(StringUtils.isEmpty(getOrderInfoRequest.getUserId())){
            //userId从token获取
            getOrderInfoRequest.setUserId(UserUtils.getUserId());
        }
            return iGoodsService.getPayInfo(response, getOrderInfoRequest);
    }


    /**
     * 微信回调接口
     */
    @PostMapping(value = "/parseOrderNotifyResult")
    @Log("I11X-微信回调接口")
    @ApiOperation(value = "I11X-微信回调接口", notes = "I11X-微信回调接口")
    public Result parseOrderNotifyResult(@RequestBody ParseOrderNotifyRequest parseOrderNotifyRequest) throws ParseException {
        return  iGoodsService.parseOrderNotifyResult(parseOrderNotifyRequest);
    }

    /**
     * 支付宝回调接口
     */
    @PostMapping(value = "/alipayCallback")
    @Log("I11X-支付宝回调接口")
    @ApiOperation(value = "I11X-支付宝回调接口", notes = "I11X-支付宝回调接口")
    public Result alipayCallback(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                 @RequestParam(required = false, name = "orderId") String orderId) {
        return  iGoodsService.alipayCallback(outBizNo,orderId);
    }






}

