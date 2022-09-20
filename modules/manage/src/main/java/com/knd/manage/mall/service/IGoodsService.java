package com.knd.manage.mall.service;

import com.knd.common.response.Result;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.mall.entity.GoodsEntity;
import com.knd.manage.mall.request.CreateGoodsRequest;
import com.knd.manage.mall.request.GetOrderInfoRequest;
import com.knd.manage.mall.request.GoodsListRequest;
import com.knd.mybatis.SuperService;
import com.knd.pay.request.ParseOrderNotifyRequest;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IGoodsService extends SuperService<GoodsEntity> {
    //新增动作
   Result add(CreateGoodsRequest createGoodsRequest );

    //新增动作
    Result update(CreateGoodsRequest createGoodsRequest );
//
//    //更新动作
//    Result edit(VoSaveAction vo );
//
//    //删除动作
//    Result deleteAction(VoId vo);
//
//    //获取动作
//    Result getAction(String actionId);
//
//    //获取动作列表
//    Result getActionList(String actionType,String target, String part, String action, String currentPage);

    Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize);

    Result getGoodsList(String goodsType,String goodsName, String current,String platform);

    Result getGoodsAttrValueList(String goodsId, String categoryId);

    Result getGoods(String goodsId);

    Result updateGoodsPublishStatus(String userId, String status, String id);

    Result deleteGoods(String userId, String id);

    Result getPayInfo(HttpServletResponse response, GetOrderInfoRequest getOrderInfoRequest);

    Result tradeQuery(String outTradeNo, String tradeNo);

    Result getGoodsListByType(GoodsListRequest request);

    Result tradeStatusQuery(String current,String userId,String status,String platform);

    Result createOfficialAccountUnifiedOrder(String openid, String orderNo, BigDecimal amount);

    Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest);

    Result alipayCallback(String outBizNo, String orderId);
}
