package com.knd.front.pay.service;

import com.knd.common.response.Result;
import com.knd.front.entity.TbOrder;
import com.knd.front.entity.VipMenu;
import com.knd.front.pay.request.CreateWebsiteOrderRequest;
import com.knd.front.pay.request.IosInAppPurchaseRequest;
import com.knd.front.pay.request.OrderConsultingRequest;
import com.knd.front.pay.request.QueryOrderConsultingRequest;
import com.knd.front.user.request.ObligationRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IOrderService extends SuperService<TbOrder> {


    Result createOrderFromWebsite(CreateWebsiteOrderRequest createOrderRequest);

    Result iosInAppPurchase(VipMenu vipMenu,IosInAppPurchaseRequest iosInAppPurchaseRequest);

    Result obligation(ObligationRequest obligationRequest);

    Result getOrderById(String orderId,String orderNo, String userId);

    Result addOrUpdateOrderConsulting(OrderConsultingRequest orderConsultingRequest);

    Result queryOrderConsulting(QueryOrderConsultingRequest queryOrderConsultingRequest);

    Result updateOrderStatus(String userId, String status, String id,String trackingNumber,String logisticsCompanies,String deliveryDate);


//    Result createOrderFromOffline(CreateWebsiteOrderRequest createWebsiteOrderRequest);
}
