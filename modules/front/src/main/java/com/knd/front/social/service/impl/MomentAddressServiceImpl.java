package com.knd.front.social.service.impl;

import com.knd.common.uuid.UUIDUtil;
import com.knd.front.social.dto.MomentAddressDto;
import com.knd.front.social.entity.UserSocialMomentAddressEntity;
import com.knd.front.social.mapper.UserSocialMomentAddressMapper;
import com.knd.front.social.service.MomentAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MomentAddressServiceImpl implements MomentAddressService {
    private final UserSocialMomentAddressMapper addressMapper;
    @Value("${amap.web.key}")
    private String key ;

    @Override
    public String add(MomentAddressDto request, String momentId) throws Exception {
        String longitude = "0";
        String latitude = "0";
        log.info("逆向地理 longitude:{{}}",longitude);
        log.info("逆向地理 latitude:{{}}",latitude);
      //  try {
         /*   String addressStr = request.getProvince()
                    +request.getCity()+request.getRegion()
                    +request.getDetailAddress()+request.getRoomNo();
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
            }*/
      //  } catch (Exception e) {
       //     e.printStackTrace();
        //    return "-1";
      //  }
        UserSocialMomentAddressEntity entity = new UserSocialMomentAddressEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setMomentId(momentId);
        entity.setLongitude(request.getLongitude());
        entity.setLatitude(request.getLatitude());
        entity.setId(UUIDUtil.getShortUUID());
        entity.setCreateDate(LocalDateTime.now());
        entity.setDeleted("0");
        addressMapper.insert(entity);
        return "0";
    }
}
