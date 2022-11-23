package com.knd.manage.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.InstallOperationEnum;
import com.knd.common.em.InstallStatusEnum;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.em.RoleEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.admin.entity.Admin;
import com.knd.manage.admin.mapper.AdminMapper;
import com.knd.manage.basedata.vo.VoUrl;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.util.JPushUtil;
import com.knd.manage.mall.dto.InstallPersonDto;
import com.knd.manage.mall.dto.OrderIcDto;
import com.knd.manage.mall.entity.OrderIcEntity;
import com.knd.manage.mall.entity.TbOrder;
import com.knd.manage.mall.entity.TbOrderOperateHistoryEntity;
import com.knd.manage.mall.entity.UserReceiveAddressEntity;
import com.knd.manage.mall.mapper.OrderIcMapper;
import com.knd.manage.mall.mapper.TbOrderMapper;
import com.knd.manage.mall.mapper.TbOrderOperateHistoryMapper;
import com.knd.manage.mall.mapper.UserReceiveAddressMapper;
import com.knd.manage.mall.request.EditInstallRequest;
import com.knd.manage.mall.request.OrderInstallRequest;
import com.knd.manage.mall.service.IOrderInstallService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderInstallServiceImpl implements IOrderInstallService {

    private final OrderIcMapper orderIcMapper;
    private final TbOrderMapper tbOrderMapper;
    private final UserReceiveAddressMapper userReceiveAddressMapper;
    private final TbOrderOperateHistoryMapper historyMapper;
    private final AdminMapper adminMapper;
    private final IAttachService iAttachService;
    private final JPushUtil jPushUtil;

    @Override
    public Result addOrUpdate(OrderInstallRequest request) {
        String note = "";
        boolean updateFlag = false;
        QueryWrapper<OrderIcEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tbOrderId",request.getTbOrderId());
        wrapper.eq("deleted", "0");
        int num = orderIcMapper.selectCount(wrapper);
        if(num>1){
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        Admin admin = adminMapper.selectById(request.getPersonId());
        if(StringUtils.isNotEmpty(admin) && !(admin.getAreaId()+"").equals(request.getAreaId())){
            return ResultUtil.error("U0999","安装区域变更,请重新选择安装人员");
        }

        TbOrder tbOrder = tbOrderMapper.selectById(request.getTbOrderId());

         if(RoleEnum.DOMESTIC_CONSUMER.getCode().equals(request.getRoleId())){
             tbOrder.setRoleType(RoleEnum.DOMESTIC_CONSUMER.getName());
         }else if(RoleEnum.CUSTOMER_SERVICE.getCode().equals(request.getRoleId())){
             tbOrder.setRoleType(RoleEnum.CUSTOMER_SERVICE.getName());
         }else if(RoleEnum.SALE.getCode().equals(request.getRoleId())){
             tbOrder.setRoleType(RoleEnum.SALE.getName());
         }else if(RoleEnum.ERECTOR.getCode().equals(request.getRoleId())){
             tbOrder.setRoleType(RoleEnum.ERECTOR.getName());
         }

        if(StringUtils.isEmpty(tbOrder)){
            return ResultUtil.error("U0999","订单id无数据");
        }
        if(tbOrder.getStatus().equals(InstallStatusEnum.CONFIRM_INSTALL.getCode())){
            return ResultUtil.error("U0999","订单已完成安装");
        }

        if(num == 0){
            UserReceiveAddressEntity addressEntity = new UserReceiveAddressEntity();
            addressEntity.setId(UUIDUtil.getShortUUID());
            addressEntity.setName(request.getUserName());
            addressEntity.setPhone(request.getUserPhone());
            addressEntity.setProvince(request.getProvince());
            addressEntity.setCity(request.getCity());
            addressEntity.setRegion(request.getRegion());
            addressEntity.setDetailAddress(request.getDetailAddress());
            addressEntity.setRoomNo(request.getRoomNo());
            addressEntity.setDeleted("0");
            userReceiveAddressMapper.insert(addressEntity);
            tbOrder.setUserReceiveAddressId(addressEntity.getId());
            updateFlag = true;

            OrderIcEntity entity = new OrderIcEntity();
            BeanUtils.copyProperties(request,entity);
            entity.setId(UUIDUtil.getShortUUID());
            orderIcMapper.insert(entity);
            note = "分配任务";

        }else if(num==1){
            OrderIcEntity entity = orderIcMapper.selectOne(wrapper);
            if(StringUtils.isNotEmpty(entity.getPersonId())){
                note = "客服重新分配安装人员";
            }else{
                note = "分配任务";
            }
            BeanUtils.copyProperties(request,entity);
            orderIcMapper.updateById(entity);

            UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(tbOrder.getUserReceiveAddressId());
            if(!userReceiveAddressEntity.getLongitude().equals(request.getLongitude()+"")
                || !userReceiveAddressEntity.getLatitude().equals(request.getLatitude()+"")
                || !userReceiveAddressEntity.getName().equals(request.getUserName()+"")
                || !userReceiveAddressEntity.getPhone().equals(request.getUserPhone()+"")){
                userReceiveAddressEntity.setProvince(request.getProvince());
                userReceiveAddressEntity.setCity(request.getCity());
                userReceiveAddressEntity.setRegion(request.getRegion());
                userReceiveAddressEntity.setDetailAddress(request.getDetailAddress());
                userReceiveAddressEntity.setRoomNo(request.getRoomNo());
                userReceiveAddressEntity.setLongitude(request.getLongitude());
                userReceiveAddressEntity.setLatitude(request.getLatitude());
                userReceiveAddressEntity.setName(request.getUserName());
                userReceiveAddressEntity.setPhone(request.getUserPhone());
                userReceiveAddressMapper.updateById(userReceiveAddressEntity);
            }
        }

        if(StringUtils.isNotEmpty(request.getPersonId())){
            TbOrderOperateHistoryEntity historyEntity = new TbOrderOperateHistoryEntity();
            historyEntity.setId(UUIDUtil.getShortUUID());
            historyEntity.setOrderId(request.getTbOrderId());
            historyEntity.setCreateTime(LocalDateTime.now());
            historyEntity.setOperateType("2");
            historyEntity.setNote(note);
            historyEntity.setOperateId(request.getPersonId());
            historyEntity.setOrderStatus("7");
            historyMapper.insert(historyEntity);

            // 设置推送参数
            // 这里可以自定义推送参数了
            Map<String, String> parm = new HashMap<>();
            // 设置提示信息,内容是文章标题
            parm.put("msg","待接受任务");
            parm.put("title","待接受任务");
            parm.put("alias",request.getPersonId());
            parm.put("orderId",request.getTbOrderId());
            jPushUtil.jpushAll(parm);

            tbOrder.setInstallStatus("1");
            updateFlag = true;
        }

        if(updateFlag){
            tbOrderMapper.updateById(tbOrder);
        }
        return ResultUtil.success();
    }

    @Override
    public Result getInstall(String orderId) {
        QueryWrapper<OrderIcEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tbOrderId",orderId);
        wrapper.eq("deleted", "0");
        OrderIcEntity orderIcEntity = orderIcMapper.selectOne(wrapper);
        if(StringUtils.isEmpty(orderIcEntity)){
            orderIcEntity = new OrderIcEntity();
            orderIcEntity.setTbOrderId(orderId);
        }

        OrderIcDto dto = new OrderIcDto();
        BeanUtils.copyProperties(orderIcEntity,dto);

        TbOrder tbOrder = tbOrderMapper.selectById(orderIcEntity.getTbOrderId());
        if(StringUtils.isNotEmpty(tbOrder)){
            UserReceiveAddressEntity userReceiveAddressEntity = userReceiveAddressMapper.selectById(tbOrder.getUserReceiveAddressId());
            dto.setProvince(userReceiveAddressEntity.getProvince());
            dto.setCity(userReceiveAddressEntity.getCity());
            dto.setRegion(userReceiveAddressEntity.getRegion());
            dto.setDetailAddress(userReceiveAddressEntity.getDetailAddress());
            dto.setRoomNo(userReceiveAddressEntity.getRoomNo());
            dto.setLongitude(userReceiveAddressEntity.getLongitude());
            dto.setLatitude(userReceiveAddressEntity.getLatitude());
            dto.setUserName(userReceiveAddressEntity.getName());
            dto.setUserPhone(userReceiveAddressEntity.getPhone());
        }
        if(StringUtils.isNotEmpty(orderIcEntity.getLocationAddressId())){
            UserReceiveAddressEntity addressEntity = userReceiveAddressMapper.selectById(orderIcEntity.getLocationAddressId());
            dto.setLocationAddress(addressEntity.getProvince()+addressEntity.getCity()+addressEntity.getRegion()+addressEntity.getDetailAddress()+addressEntity.getRoomNo());
            dto.setLocationStatus(orderIcEntity.getLocationStatus());
            dto.setLocationTime(orderIcEntity.getLocationTime());
        }
        return ResultUtil.success(dto);
    }

    @Override
    public Result getInstallPersonList(String areaId) {
        List<InstallPersonDto> installPerson = orderIcMapper.getInstallPersonList(areaId);
        for(InstallPersonDto dto : installPerson){
            LocalDateTime dateTime = orderIcMapper.getTime(dto.getId());
            dto.setDateTime(dateTime);
        }
        return ResultUtil.success(installPerson);
    }

    @Override
    public Result editInstall(EditInstallRequest request) {
        TbOrderOperateHistoryEntity historyEntity = new TbOrderOperateHistoryEntity();
        historyEntity.setId(UUIDUtil.getShortUUID());
        historyEntity.setOrderId(request.getTbOrderId());
        historyEntity.setCreateTime(LocalDateTime.now());
        historyEntity.setOperateType("2");
        historyEntity.setOperateId(request.getPersonId());

        TbOrder tbOrder = tbOrderMapper.selectById(request.getTbOrderId());
        tbOrder.setLastModifiedBy(request.getPersonId());
        tbOrder.setLastModifiedDate(LocalDateTime.now());
        //接受任务
        if(InstallOperationEnum.ACCEPT_TASK.getCode().equals(request.getFlag().getCode())){
            if(StringUtils.isNotEmpty(tbOrder)
                    && InstallStatusEnum.WAIT_ACCEPTED.getCode().equals(tbOrder.getInstallStatus())){
                tbOrder.setInstallStatus(InstallStatusEnum.WAIT_INSTALLED.getCode());
                tbOrderMapper.updateById(tbOrder);

                historyEntity.setNote(InstallOperationEnum.ACCEPT_TASK.getMessage());
                historyEntity.setOrderStatus("8");
                historyMapper.insert(historyEntity);
            }else{
                return ResultUtil.error("U0999", "当前订单状态不可进行此操作");
            }
        //开始安装
        }else if(InstallOperationEnum.START_INSTALL.getCode().equals(request.getFlag().getCode())){
            if(StringUtils.isEmpty(request.getAddressRequest())){
                return ResultUtil.error("U0999","未签到");
            }
            UserReceiveAddressEntity addressEntity = new UserReceiveAddressEntity();
            BeanUtils.copyProperties(request.getAddressRequest(),addressEntity);
            addressEntity.setId(UUIDUtil.getShortUUID());
            addressEntity.setDeleted("0");
            userReceiveAddressMapper.insert(addressEntity);
            QueryWrapper<OrderIcEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("tbOrderId",request.getTbOrderId());
            wrapper.eq("deleted", "0");
            OrderIcEntity entity = orderIcMapper.selectOne(wrapper);
            entity.setLocationAddressId(addressEntity.getId());
            entity.setLocationStatus(request.getAddressRequest().getLocationStatus());
            entity.setLocationTime(LocalDateTime.now());
            orderIcMapper.updateById(entity);
            if(StringUtils.isNotEmpty(tbOrder)
                    && InstallStatusEnum.WAIT_INSTALLED.getCode().equals(tbOrder.getInstallStatus())){
                tbOrder.setInstallStatus(InstallStatusEnum.INSTALLATION.getCode());
                tbOrderMapper.updateById(tbOrder);

                historyEntity.setNote(InstallOperationEnum.START_INSTALL.getMessage());
                historyEntity.setOrderStatus("9");
                historyMapper.insert(historyEntity);
            }else{
                return ResultUtil.error("U0999", "当前订单状态不可进行此操作");
            }
        //确认安装完成
        }else if(InstallOperationEnum.CONFIRM_INSTALL.getCode().equals(request.getFlag().getCode())){
            if(StringUtils.isNotEmpty(tbOrder)
                    && InstallStatusEnum.INSTALLATION.getCode().equals(tbOrder.getInstallStatus())){
                tbOrder.setInstallStatus(InstallStatusEnum.CONFIRM_INSTALL.getCode());
                tbOrder.setStatus(OrderStatusEnum.ORDER_FINISHED.getCode());
                tbOrderMapper.updateById(tbOrder);

                //保存签名图片
                Attach signatureUrl = iAttachService.saveAttach(request.getPersonId(), request.getSignatureUrl().getPicAttachName()
                        , request.getSignatureUrl().getPicAttachNewName(), request.getSignatureUrl().getPicAttachSize());
                //保存安装多图
                String installUrlIds = "";
                for(VoUrl url : request.getInstallUrl()){
                    Attach installUrl = iAttachService.saveAttach(request.getPersonId(), url.getPicAttachName()
                            , url.getPicAttachNewName(), url.getPicAttachSize());
                    installUrlIds = StringUtils.isNotEmpty(installUrlIds) ? installUrlIds+","+installUrl.getId() : installUrl.getId();
                }
                QueryWrapper<OrderIcEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("tbOrderId",request.getTbOrderId());
                OrderIcEntity entity = orderIcMapper.selectOne(wrapper);
                entity.setSignatureUrlId(signatureUrl.getId());
                entity.setInstallUrlIds(installUrlIds);
                orderIcMapper.updateById(entity);
                historyEntity.setNote(InstallOperationEnum.CONFIRM_INSTALL.getMessage());
                historyEntity.setOrderStatus("10");
                historyMapper.insert(historyEntity);
            }else{
                return ResultUtil.error("U0999", "当前订单状态不可进行此操作");
            }
        }else{
            return ResultUtil.error(ResultEnum.FAIL);
        }
        return ResultUtil.success();
    }

}
