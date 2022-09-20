package com.knd.front.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.userutil.UserUtils;
import com.knd.common.utils.HttpUtils;
import com.knd.front.entity.AmapAdcodeEntity;
import com.knd.front.entity.UserReceiveAddressEntity;
import com.knd.front.pay.request.UserReceiveAddressRequest;
import com.knd.front.train.mapper.AmapAdcodeMapper;
import com.knd.front.user.mapper.UserReceiveAddressMapper;
import com.knd.front.user.service.IUserReceiveAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author will
 */
@Service
@Slf4j
public class UserReceiveAddressServiceImpl extends ServiceImpl<UserReceiveAddressMapper, UserReceiveAddressEntity> implements IUserReceiveAddressService {
    @Value("${amap.web.key}")
    private String key;
    @Resource
    private AmapAdcodeMapper amapAdcodeMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(UserReceiveAddressEntity userReceiveAddressEntity) {
        baseMapper.delete(new QueryWrapper<UserReceiveAddressEntity>()
                .eq("phone", userReceiveAddressEntity.getPhone()).isNull("userId"));
        userReceiveAddressEntity.setId(null);
        baseMapper.insert(userReceiveAddressEntity);
        return userReceiveAddressEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(UserReceiveAddressRequest userReceiveAddressRequest) {
        String longitude = "0";
        String latitude = "0";
        try {
            String addressStr = userReceiveAddressRequest.getProvince()
                    +userReceiveAddressRequest.getCity()+userReceiveAddressRequest.getRegion()
                    +userReceiveAddressRequest.getDetailAddress()+userReceiveAddressRequest.getRoomNo();
            String s = HttpUtils.httpGet("http://restapi.amap.com/v3/geocode/geo?key="
                    + key + "&address=" + addressStr);
            log.info("高德逆向地理编码结果为："+s);
            JSONObject jsonResult = JSONObject.parseObject(s);
            if("1".equals(jsonResult.getString("status"))&&Integer.parseInt(jsonResult.getString("count"))>0) {
                JSONArray geocodes = jsonResult.getJSONArray("geocodes");
                JSONObject jsonObject = geocodes.getJSONObject(0);
                String location = jsonObject.getString("location");

                String[] split = location.split(",");
                longitude = split[0];
                latitude = split[1];

            }else{
                return "-1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
        UserReceiveAddressEntity userReceiveAddressEntity = new UserReceiveAddressEntity();
        BeanUtils.copyProperties(userReceiveAddressRequest, userReceiveAddressEntity);
        userReceiveAddressEntity.setUserId(UserUtils.getUserId());
        userReceiveAddressEntity.setLongitude(longitude);
        userReceiveAddressEntity.setLatitude(latitude);
        Integer count = baseMapper.selectCount(new QueryWrapper<UserReceiveAddressEntity>()
                .eq("userId", UserUtils.getUserId()));
        if (count <= 0) {
            userReceiveAddressEntity.setDefaultStatus("1");
        } else {
            userReceiveAddressEntity.setDefaultStatus("0");
        }
        userReceiveAddressEntity.setId(null);
        userReceiveAddressEntity.setCreateDate(LocalDateTime.now());
        userReceiveAddressEntity.setDeleted("0");
            baseMapper.insert(userReceiveAddressEntity);
        return userReceiveAddressEntity.getId();
    }

    @Override
    public void edit(UserReceiveAddressEntity userReceiveAddressEntity) {
        List<UserReceiveAddressEntity> list = baseMapper.selectList(
                        new QueryWrapper<UserReceiveAddressEntity>()
                                .eq("userId", userReceiveAddressEntity.getUserId()).eq("defaultStatus", 1).or(e -> e.eq("phone", userReceiveAddressEntity.getPhone())).eq("defaultStatus", 1));
        list.stream().forEach(address->{address.setDefaultStatus("0");
        baseMapper.updateById(address);});
        //省截取
        QueryWrapper<AmapAdcodeEntity> provinceQueryWrapper = new QueryWrapper<>();
        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
        provinceQueryWrapper.last("limit 1");
        AmapAdcodeEntity provinceAmapAdCodeEntity = amapAdcodeMapper.selectOne(provinceQueryWrapper);
        if(StringUtils.isEmpty(provinceAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getProvince().split(",");
            userReceiveAddressEntity.setProvince(split[0]);
        }
        //市截取
        QueryWrapper<AmapAdcodeEntity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
        cityQueryWrapper.last("limit 1");
        AmapAdcodeEntity cityAmapAdCodeEntity = amapAdcodeMapper.selectOne(cityQueryWrapper);
        if(StringUtils.isEmpty(cityAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getCity().split(",");
            userReceiveAddressEntity.setCity(split[0]);
        }
        //区截取
        QueryWrapper<AmapAdcodeEntity> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("name",userReceiveAddressEntity.getRegion());
        regionQueryWrapper.last("limit 1");
        AmapAdcodeEntity regionAmapAdCodeEntity = amapAdcodeMapper.selectOne(regionQueryWrapper);
        if(StringUtils.isEmpty(regionAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getRegion().split(",");
            userReceiveAddressEntity.setRegion(split[0]);
        }
        baseMapper.updateById(userReceiveAddressEntity);

    }

}