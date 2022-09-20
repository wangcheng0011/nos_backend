package com.knd.pay.common;

import com.knd.common.basic.DateUtils;
import com.knd.common.constant.Constant;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.pay.entity.AliPayBean;
import com.knd.pay.entity.TbOrder;
import com.knd.pay.entity.TbOrderItem;
import com.knd.pay.entity.WxPayBean;
import com.knd.pay.mapper.TbOrderItemMapper;
import com.knd.pay.mapper.TbOrderMapper;
import com.knd.pay.request.CreateOrderRequest;
import com.knd.pay.request.GoodsRequest;
import com.knd.pay.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author will
 * @date 2021/8/5 18:38
 */
@Component
@Log4j2
public abstract class TradeCreatePayTemplate {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Value("${domain}")
    private String domain;

    @Resource
    private AliPayBean aliPayBean;

    @Resource
    private WxPayBean wxPayBean;
    //创建预订单模板方法
    @Transactional(rollbackFor = Exception.class)
    Result tradeCreatePay(CreateOrderRequest createOrderRequest){
        try {
            log.info("tradeCreatePay createOrderRequest:{{}}",createOrderRequest);
            //初始化订单对象
            TbOrder tbOrder = init(createOrderRequest);

            //调用微信/支付宝创建预订单信息
            log.info("tradeCreatePay tbOrder:{{}}",tbOrder);
            tradeCreatePayCore(tbOrder,createOrderRequest);

            //商品购买需组装地址信息
            if(OrderTypeEnum.GOODS.getCode().equals(createOrderRequest.getOrderType())) {
                assembleReceivingAddress(tbOrder,createOrderRequest);
            }

            //生成本地预订单
            saveOrder(tbOrder);

            //生成订单关联子订单信息
            saveOrderItem(tbOrder,createOrderRequest);
            return ResultUtil.success(tbOrder);
        }catch (CustomResultException e){
            return ResultUtil.error(e);
        }catch (Exception e){
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }

    }

    protected void saveOrderItem(TbOrder tbOrder,CreateOrderRequest createOrderRequest){
        List<TbOrderItem> tbOrderItemList = new ArrayList<>();
        for(GoodsRequest goodsRequest: createOrderRequest.getGoodsRequestList()) {
            TbOrderItem tbOrderItem = new TbOrderItem();
            BeanUtils.copyProperties(goodsRequest,tbOrderItem);
            tbOrderItem.setTbOrderId(tbOrder.getId());
            tbOrderItemMapper.insert(tbOrderItem);
            tbOrderItemList.add(tbOrderItem);
        }
        tbOrder.setTbOrderItemList(tbOrderItemList);
    };

    protected void saveOrder(TbOrder tbOrder){
        LocalDateTime currentLocalDateTime = DateUtils.getCurrentLocalDateTime();
        tbOrder.setCreateDate(currentLocalDateTime);
        tbOrder.setLastModifiedDate(currentLocalDateTime);
        tbOrderMapper.insert(tbOrder);
    };

    protected void assembleReceivingAddress(TbOrder tbOrder, CreateOrderRequest createOrderRequest){
        tbOrder.setUserReceiveAddressId(createOrderRequest.getUserReceiveAddressId());
    };

    protected abstract void tradeCreatePayCore(TbOrder tbOrder, CreateOrderRequest createOrderRequest);

    TbOrder init(CreateOrderRequest createOrderRequest){
        TbOrder tbOrder = new TbOrder();
        BeanUtils.copyProperties(createOrderRequest,tbOrder);
        tbOrder.setId(UUIDUtil.getShortUUID());
        tbOrder.setOrderNo(StringUtils.getOutTradeNo());
        //设置订单为未付款状态
        tbOrder.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
        tbOrder.setConfirmStatus("0");
        tbOrder.setDeleted("0");
        tbOrder.setCreateBy(createOrderRequest.getUserId());
        tbOrder.setLastModifiedBy(createOrderRequest.getUserId());

        //填充所需的其他数据
        createOrderRequest.setAliPayNotifyUrl(domain + Constant.ALI_NOTIFY_URL);
        createOrderRequest.setWxPayNotifyUrl(domain + Constant.WX_NOTIFY_URL);
        return tbOrder;

     }
}
