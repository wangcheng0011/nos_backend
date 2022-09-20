package com.knd.manage.mall.service;

import com.knd.common.response.Result;
import com.knd.manage.mall.entity.TbOrder;
import com.knd.manage.mall.request.*;
import com.knd.mybatis.SuperService;
import com.knd.permission.bean.Token;

import java.io.InputStream;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface ITbOrderService extends SuperService<TbOrder> {
    Result getOrderById(String orderId);

    Result getOrderList(OrderListQueryParam orderListQueryParam);

    Result getOrderInfo(String goodsId);
    //改变订单状态
    Result updateOrderStatus(String userId, String status,String id,String trackingNumber,String logisticsCompanies,String serialNo);

    Result createOrderFromOffline(CreateOfflineOrderRequest createOfflineOrderRequest);

    Result getOrderList4App(OrderQueryParam orderQueryParam, Token token);

    Result addOrUpdateOrderConsulting(OrderConsultingRequest orderConsultingRequest);

    Result queryOrderConsulting(QueryOrderConsultingRequest queryOrderConsultingRequest);

    Result saveOrderCousultingByBatch(InputStream inputStream, String originalFilename) throws Exception;

    Result getUserReceiveAddressById(String id);

    Result saveUserReceiveAddressEntity(UserReceiveAddressRequest userReceiveAddressRequest);


}
