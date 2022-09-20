
package com.knd.manage.logistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.utils.HttpUtils;
import com.knd.manage.logistics.service.IAneLogisticsService;
import com.knd.manage.logistics.vo.VoAneLogistics;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AneLogisticsServiceImpl implements IAneLogisticsService {

    @Value("${ane.code}")
    private String aneCode;
    @Value("${ane.secretKey}")
    private String aneSecretKey;
    @Value("${ane.trajectoryUrl}")
    private String aneTrajectoryUrl;

    @Override
    public Result


    trajectoryQuery(VoAneLogistics vo)  {
        JSONObject json = new JSONObject();
        String params = JSONObject.toJSONString(vo);
        //code与secretKey为双方约定
        String timeStamp = System.currentTimeMillis()+"";
        //时间戳 SDK提供SecurityUtil获取时间戳
        //摘要 SDK提供SecurityUtil生成摘要
        String digest = getDigest(params+aneCode+aneSecretKey);
        //请求参数
        json.put("code", aneCode);
        json.put("timestamp", timeStamp);
        json.put("params", params);
        json.put("digest", digest);
        //请求url
        String response = null;
        try {
            //返回结果
            response = HttpUtils.httpPost(aneTrajectoryUrl, json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.replaceAll("\\\\", "");
        JSONObject jsonObject = JSONObject.parseObject(response);
        String resultInfo = jsonObject.getString("resultInfo");
        resultInfo.replaceAll("\\\\", "");
        Map maps = (Map) JSON.parse(resultInfo);
        //根据返回结果进行后续操作
        System.out.println(maps);
        return ResultUtil.success(maps);
    }



    private static String getDigest(String plainText) {
        return  Base64.encodeBase64String(DigestUtils.md5Hex(plainText).getBytes());
    }



}
