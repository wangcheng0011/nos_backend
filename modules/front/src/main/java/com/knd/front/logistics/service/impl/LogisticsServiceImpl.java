
package com.knd.front.logistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deppon.dop.module.sdk.shared.domain.result.ResultDO;
import com.deppon.dop.module.sdk.shared.util.FastJsonUtil;
import com.deppon.dop.module.sdk.shared.util.HttpUtils;
import com.knd.common.em.LogisticsCompanyEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.logistics.domain.DepponQueryOrder;
import com.knd.front.logistics.entity.LogisticsEntity;
import com.knd.front.logistics.mapper.LogisticsMapper;
import com.knd.front.logistics.service.ILogisticsService;
import com.knd.front.logistics.vo.VoAneLogistics;
import com.knd.front.logistics.vo.VoDepponLogistics;
import com.knd.front.logistics.vo.VoLogistics;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class LogisticsServiceImpl extends ServiceImpl<LogisticsMapper, LogisticsEntity> implements ILogisticsService {

    @Value("${ane.code}")
    private String aneCode;
    @Value("${ane.secretKey}")
    private String aneSecretKey;
    @Value("${ane.trajectoryUrl}")
    private String aneTrajectoryUrl;
    @Value("${deppon.sign}")
    private String depponsign;
    @Value("${deppon.companyCode}")
    private String depponCompanyCode;
    @Value("${deppon.appKey}")
    private String depponAppKey;
    @Value("${deppon.trajectoryUrl}")
    private String depponTrajectoryUrl;
    @Value("${deppon.queryOrderUrl}")
    private String depponQueryOrderUrl;
    @Override
    public Result trajectoryQuery(VoLogistics vo)  {
        List<LogisticsEntity> logisticsEntities = null;
        if(LogisticsCompanyEnum.DEPPON.getCode().equals(vo.getLogisticsCompanies())){
            logisticsEntities = depponTrajectory(vo);
        }else if(LogisticsCompanyEnum.ANE.getCode().equals(vo.getLogisticsCompanies())){
            logisticsEntities = aneTrajectory(vo);
        }
        return ResultUtil.success(logisticsEntities);
    }

    public List<LogisticsEntity> depponTrajectory(VoLogistics vo){
        VoDepponLogistics voDepponLogistics = new VoDepponLogistics();
        voDepponLogistics.setMailNo(vo.getTrackingNumber());
        //订单内容 json字符串，SDK提供FastJsonUtil转Json
        String params= FastJsonUtil.toJSONString(voDepponLogistics);
        //companyCode与appkey为双方约定
        //时间戳 SDK提供SecurityUtil获取时间戳
        String timeStamp = System.currentTimeMillis()+"";
        //摘要 SDK提供SecurityUtil生成摘要
        String plainText =getDigest(params+depponAppKey+timeStamp);
        //post请求参数
        NameValuePair[] data = new NameValuePair[4];
        data[0] = new NameValuePair("companyCode", depponCompanyCode);
        data[1] = new NameValuePair("digest", plainText);
        data[2] = new NameValuePair("timestamp", timeStamp);
        data[3] = new NameValuePair("params", params);
        //请求url
        ResultDO<String> response=null;
        //返回结果
        response= HttpUtils.sendRequest(depponTrajectoryUrl, data, "UTF-8", 5000);
        System.out.print(response);
        Object model = response.getModel();
        String resp = JSONObject.toJSONString(model).replaceAll("\\\\n", "").replaceAll("\\\\t", "").replaceAll("\\\\", "");
        String substring = resp.substring(1, resp.length()-1);
        JSONObject modelJsonObject = JSONObject.parseObject(substring);
        String responseParam = modelJsonObject.getString("responseParam");
        JSONObject responseParamObject = JSONObject.parseObject(responseParam);
        String tracking_number = responseParamObject.getString("tracking_number");
        String trace_list = responseParamObject.getString("trace_list");
        JSONArray arrays = JSONArray.parseArray(trace_list);
        List<LogisticsEntity> logisticsEntities = new ArrayList<>();
        QueryWrapper<LogisticsEntity> logisticsEntityQueryWrapper = new QueryWrapper<>();
        logisticsEntityQueryWrapper.eq("trackingNumber",vo.getTrackingNumber());
        logisticsEntityQueryWrapper.eq("deleted","0");
        baseMapper.delete(logisticsEntityQueryWrapper);
        for (int i =0;i<arrays.size();i++){
            LogisticsEntity logisticsEntity = new LogisticsEntity();
            JSONObject jsonObject = arrays.getJSONObject(i);
            logisticsEntity.setId(UUIDUtil.getShortUUID());
            logisticsEntity.setSite(jsonObject.getString("site"));
            logisticsEntity.setCity(jsonObject.getString("city"));
            logisticsEntity.setDescription(jsonObject.getString("description"));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            logisticsEntity.setTime(LocalDateTime.parse(jsonObject.getString("time"),df));
            logisticsEntity.setLogisticsCompanies(vo.getLogisticsCompanies());
            logisticsEntity.setTrackingNumber(vo.getTrackingNumber());
            logisticsEntity.setSort(String.valueOf(i));
            logisticsEntity.setDeleted("0");
            logisticsEntity.setCreateBy(vo.getUserId());
            logisticsEntity.setCreateDate(LocalDateTime.now());
            baseMapper.insert(logisticsEntity);
            logisticsEntities.add(logisticsEntity);
        }
        //根据返回结果进行后续操作
        System.out.println(logisticsEntities);
        return logisticsEntities;
    };


    public List<LogisticsEntity> aneTrajectory(VoLogistics vo) {
        VoAneLogistics voAneLogistics = new VoAneLogistics();
        voAneLogistics.setEwbNo(vo.getTrackingNumber());
        JSONObject json = new JSONObject();
        String params = JSONObject.toJSONString(voAneLogistics);
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
        String response = null;
        try {
            //返回结果
            response = com.knd.common.utils.HttpUtils.httpPost(aneTrajectoryUrl, json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.replaceAll("\\\\", "");
        JSONObject jsonObject = JSONObject.parseObject(response);
        String resultInfo = jsonObject.getString("resultInfo");
        resultInfo.replaceAll("\\\\", "");
        JSONObject resultInfoJsonObject = JSONObject.parseObject(resultInfo);
        String traces = resultInfoJsonObject.getString("traces");
        List<JSONObject> tracesJsonObjects = JSONArray.parseArray(traces, JSONObject.class);
        Collections.reverse(tracesJsonObjects);
        List<LogisticsEntity> logisticsEntities = new ArrayList<>();
        QueryWrapper<LogisticsEntity> logisticsEntityQueryWrapper = new QueryWrapper<>();
        logisticsEntityQueryWrapper.eq("trackingNumber",vo.getTrackingNumber());
        logisticsEntityQueryWrapper.eq("deleted","0");
        baseMapper.delete(logisticsEntityQueryWrapper);
        for (int i =0;i<tracesJsonObjects.size();i++){
            LogisticsEntity logisticsEntity = new LogisticsEntity();
            JSONObject tracesJsonObject = tracesJsonObjects.get(i);
            logisticsEntity.setId(UUIDUtil.getShortUUID());
            logisticsEntity.setSite(tracesJsonObject.getString("siteName"));
            logisticsEntity.setCity(tracesJsonObject.getString("city"));
            logisticsEntity.setDescription(tracesJsonObject.getString("desc"));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            logisticsEntity.setTime(LocalDateTime.parse(tracesJsonObject.getString("operateTime"),df));
            logisticsEntity.setLogisticsCompanies(vo.getLogisticsCompanies());
            logisticsEntity.setTrackingNumber(vo.getTrackingNumber());
            logisticsEntity.setSort(String.valueOf(i));
            logisticsEntity.setDeleted("0");
            logisticsEntity.setCreateBy(vo.getUserId());
            logisticsEntity.setCreateDate(LocalDateTime.now());
            baseMapper.insert(logisticsEntity);
            logisticsEntities.add(logisticsEntity);
        }
        //根据返回结果进行后续操作
        System.out.println(logisticsEntities);
        return logisticsEntities;

    }



    @Override
    public Result queryOrder(String logisticID,String logisticCompanyID){
        DepponQueryOrder depponQueryOrder = new DepponQueryOrder();
        depponQueryOrder.setLogisticID(logisticID);
        depponQueryOrder.setLogisticCompanyID(logisticCompanyID);
        //订单内容 json字符串，SDK提供FastJsonUtil转Json
        String params= FastJsonUtil.toJSONString(depponQueryOrder);
        //companyCode与appkey为双方约定
        //时间戳 SDK提供SecurityUtil获取时间戳
        String timeStamp = System.currentTimeMillis()+"";
        //摘要 SDK提供SecurityUtil生成摘要
        String plainText =getDigest(params+depponAppKey+timeStamp);
        //post请求参数
        NameValuePair[] data = new NameValuePair[4];
        data[0] = new NameValuePair("companyCode", depponCompanyCode);
        data[1] = new NameValuePair("digest", plainText);
        data[2] = new NameValuePair("timestamp", timeStamp);
        data[3] = new NameValuePair("params", params);
        //请求url
        ResultDO<String> response=null;
        //返回结果
        response= HttpUtils.sendRequest(depponQueryOrderUrl, data, "UTF-8", 5000);
        Object model = response.getModel();
        String resp = JSONObject.toJSONString(model).replaceAll("\\\\n", "").replaceAll("\\\\t", "").replaceAll("\\\\", "");
        String substring = resp.substring(1, resp.length()-1);
        JSONObject modelJsonObject = JSONObject.parseObject(substring);
        String responseParam = modelJsonObject.getString("responseParam");
        Map maps = (Map) JSON.parse(responseParam);
        //根据返回结果进行后续操作
        System.out.println(maps);
        return ResultUtil.success(maps);
    }

    private static String getDigest(String plainText) {
        return  Base64.encodeBase64String(DigestUtils.md5Hex(plainText).getBytes());
    }


    @Override
    public LogisticsEntity insertReturnEntity(LogisticsEntity entity) {
        return null;
    }

    @Override
    public LogisticsEntity updateReturnEntity(LogisticsEntity entity) {
        return null;
    }
}
