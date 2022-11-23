package com.knd.front.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.live.dto.UserRecordListDto;
import com.knd.front.live.entity.UserOrderRecordEntity;
import com.knd.front.live.mapper.UserOrderRecordMapper;
import com.knd.front.live.service.UserOrderRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class UserOrderRecordServiceImpl implements UserOrderRecordService {
    private final UserOrderRecordMapper userOrderRecordMapper;

    @Override
    public Result getRecordList(String userId,String current) {
        List<UserRecordListDto> dtoList = new ArrayList<>();
        Page<UserOrderRecordEntity> page = new Page(Integer.parseInt(current), PageInfo.pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        wrapper.orderBy(true,false,"isRead","orderTime");
        Page pageList = userOrderRecordMapper.selectPage(page, wrapper);
        List<UserOrderRecordEntity> list = pageList.getRecords();
        for (UserOrderRecordEntity entity: list){
            UserRecordListDto dto = new UserRecordListDto();
            BeanUtils.copyProperties(entity,dto);
            dtoList.add(dto);
        }
        Page<UserRecordListDto> pageDto = new Page<>();
        pageDto.setTotal(page.getTotal());
        pageDto.setCurrent(page.getCurrent());
        pageDto.setSize(page.getSize());
        pageDto.setRecords(dtoList);
        return ResultUtil.success(pageDto);

        /*Page<UserRecordListDto> page = new Page(Integer.parseInt(current), PageInfo.pageSize);
        QueryWrapper<UserRecordListDto> wrapper = new QueryWrapper();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        wrapper.orderBy(true,false,"isRead","orderTime");
        List<UserRecordListDto> dtoList = userOrderRecordMapper.getUserRecordList(page,wrapper);
        page.setRecords(dtoList);
        return ResultUtil.success(page);*/
    }

    @Override
    public Result save(String userId, String orderType, String orderName,String content, LocalDateTime orderTime, String relevancyId) {
        UserOrderRecordEntity recordEntity = new UserOrderRecordEntity();
        recordEntity.setId(UUIDUtil.getShortUUID());
        recordEntity.setUserId(userId);
        recordEntity.setIsRead("0");
        recordEntity.setOrderType(orderType);
        recordEntity.setOrderName(orderName);
        recordEntity.setContent(content);
        recordEntity.setOrderTime(orderTime);
        recordEntity.setRelevancyId(relevancyId);
        recordEntity.setCreateBy(userId);
        recordEntity.setCreateDate(LocalDateTime.now());
        recordEntity.setDeleted("0");
        recordEntity.setLastModifiedBy(userId);
        recordEntity.setLastModifiedDate(LocalDateTime.now());
        log.info("save recordEntity:{{}}",recordEntity);
        userOrderRecordMapper.insert(recordEntity);
        return ResultUtil.success();
    }

    @Override
    public Result remove(String userId,String relevancyId) {
        List<UserOrderRecordEntity> userOrderRecordEntities = userOrderRecordMapper.selectList(new QueryWrapper<UserOrderRecordEntity>()
                .eq("deleted", "0").eq("userId", userId).eq("relevancyId", relevancyId));
        /*if (StringUtils.isEmpty(userOrderRecordEntities)){
            return ResultUtil.error("U0999", "记录数据不存在");
        }*/
        for (UserOrderRecordEntity recordEntity : userOrderRecordEntities){
            recordEntity.setDeleted("1");
            userOrderRecordMapper.updateById(recordEntity);
        }
        return ResultUtil.success();
    }

    @Override
    public Result read(String id,String userId) {
        UserOrderRecordEntity recordEntity = userOrderRecordMapper.selectById(id);
        if (StringUtils.isNotEmpty(recordEntity)){
            if (!userId.equals(recordEntity.getUserId())){
                return ResultUtil.error("U0999", "该用户并无此消息");
            }
            recordEntity.setIsRead("1");
            userOrderRecordMapper.updateById(recordEntity);
        }
        return ResultUtil.success();
    }
}
