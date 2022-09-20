package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.OrderTypeEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.entity.TbOrder;
import com.knd.front.entity.UserPay;
import com.knd.front.login.mapper.TbOrderMapper;
import com.knd.front.user.dto.UserPayCheckDto;
import com.knd.front.user.mapper.UserPayMapper;
import com.knd.front.user.request.*;
import com.knd.front.user.service.IUserPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserPayServiceImpl extends ServiceImpl<UserPayMapper, UserPay> implements IUserPayService {

    private final UserPayMapper userPayMapper;

    @Override
    public Result add(UserPayAddRequest request) {
        QueryWrapper<UserPay> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        List<String> statusList = new ArrayList<>();
        statusList.add("0");
        statusList.add("1");
        qw.in("payStatus",statusList);
        qw.eq("orderId",request.getOrderId());
        int count = baseMapper.selectCount(qw);
        if(count > 0){
            return ResultUtil.error("U0999","该订单已支付");
        }

        for(UserPayDetailRequest detail : request.getDetailRequests()){
            qw.clear();
            List<String> list = new ArrayList<>();
            list.add("0");
            list.add("1");
            qw.in("payStatus",list);
            qw.eq("deleted","0");
            qw.eq("type",detail.getType());
            qw.eq("payId",detail.getPayId());
            qw.eq("userId",request.getUserId());
            int count2 = baseMapper.selectCount(qw);
            if(count2 > 0){
                return ResultUtil.error("U0999","用户已购买该课程");
            }
            UserPay pay = new UserPay();
            pay.setId(UUIDUtil.getShortUUID());
            pay.setOrderId(request.getOrderId());
            pay.setType(String.valueOf(detail.getType()));
            pay.setPayId(detail.getPayId());
            pay.setPayStatus("1");
            pay.setUserId(request.getUserId());
            pay.setCreateBy(request.getUserId());
            pay.setCreateDate(LocalDateTime.now());
            pay.setDeleted("0");
            pay.setLastModifiedBy(request.getUserId());
            pay.setLastModifiedDate(LocalDateTime.now());
            baseMapper.insert(pay);
        }
        return ResultUtil.success();
    }




    @Override
    public Result edit(UserPayEditRequest request) {
        QueryWrapper<UserPay> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        qw.ne("payStatus","1");
        qw.eq("orderId",request.getOrderId());
        int count = baseMapper.selectCount(qw);
        if(count == 0){
            return ResultUtil.error("U0999","订单不存在");
        }
        List<UserPay> userPays = baseMapper.selectList(qw);
        for(UserPay u : userPays){
            u.setPayStatus(request.getPayStatus());
            userPayMapper.updateById(u);
        }
        return ResultUtil.success();
    }

    @Override
    public Result checkList(UserPayCheckRequest request) {
        List<UserPayCheckDto> list = new ArrayList<>();
        UserPayCheckDto dto = new UserPayCheckDto();
        QueryWrapper<UserPay> qw = new QueryWrapper<>();
        for(UserPayDetailRequest detail : request.getDetailRequests()){
            dto.setType(detail.getType());
            dto.setPayId(detail.getPayId());

            qw.clear();
            qw.eq("deleted","0");
            List<String> statusList = new ArrayList<>();
            statusList.add("0");
            statusList.add("1");
            qw.in("payStatus",statusList);
            qw.eq("userId",request.getUserId());
            qw.eq("type",detail.getType());
            qw.eq("payId",detail.getPayId());
            int count = baseMapper.selectCount(qw);
            if(count>0){
                dto.setResult(true);
            }else{
                dto.setResult(false);
            }
            list.add(dto);
        }
        return ResultUtil.success(list);
    }

    @Override
    public Result check(String userId, Integer type, String payId) {
        QueryWrapper<UserPay> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        List<String> statusList = new ArrayList<>();
        statusList.add("0");
        statusList.add("1");
        qw.in("payStatus",statusList);
        qw.eq("userId",userId);
        qw.eq("type",type);
        qw.eq("payId",payId);
        int count = baseMapper.selectCount(qw);
        if(count>0){
            return ResultUtil.success(true);
        }else{
            return ResultUtil.success(false);
        }
    }

    @Override
    public UserPay insertReturnEntity(UserPay entity) {
        return null;
    }

    @Override
    public UserPay updateReturnEntity(UserPay entity) {
        return null;
    }
}
