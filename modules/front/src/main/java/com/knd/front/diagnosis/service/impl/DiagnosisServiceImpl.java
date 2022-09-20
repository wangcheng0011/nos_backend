package com.knd.front.diagnosis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.diagnosis.mapper.TestingReportDetailMapper;
import com.knd.front.diagnosis.mapper.TestingReportHeadMapper;
import com.knd.front.diagnosis.request.TestingReportDetailRequest;
import com.knd.front.diagnosis.request.TestingReportRequest;
import com.knd.front.diagnosis.service.IDiagnosisService;
import com.knd.front.entity.Attach;
import com.knd.front.entity.TestingReportDetail;
import com.knd.front.entity.TestingReportHead;
import com.knd.front.train.mapper.AttachMapper;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-01
 */
@Service
public class DiagnosisServiceImpl extends ServiceImpl<TestingReportHeadMapper, TestingReportHead> implements IDiagnosisService {

    @Autowired
    private TestingReportHeadMapper testingReportHeadMapper;

    @Autowired
    private TestingReportDetailMapper testingReportDetailMapper;

    @Autowired
    private AttachMapper attachMapper;


    //终端，可以不带协议、也可以带协议
    @Value("${OBS.endPoint}")
    private String endPoint;
    //公钥
    @Value("${OBS.ak}")
    private String ak;
    //私钥
    @Value("${OBS.sk}")
    private String sk;
    //桶名
    @Value("${OBS.bucketname}")
    private String bucketname;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result testingReport(TestingReportRequest testingReportRequest) {
        //存储文件信息
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, config);
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        Attach aPi = new Attach();
        //获取图片访问的路径
        String picUrl = ObsObjectUtil.getUrl(obsClient, testingReportRequest.getAutographFilePathName(), bucketname, expireSeconds);
        String[] strs2 = picUrl.split("\\/");
        picUrl = strs2[strs2.length - 1];
        //存储图片信息
        aPi = new Attach();
        aPi.setId(UUIDUtil.getShortUUID());
        aPi.setFileName(testingReportRequest.getAutographFileName());
        aPi.setFilePath(picUrl);
        aPi.setFileSize(testingReportRequest.getAutographFileSize());
        aPi.setFileType(testingReportRequest.getAutographFileName().substring(testingReportRequest.getAutographFileName().lastIndexOf(".") + 1));
        aPi.setCreateDate(LocalDateTime.now());
        aPi.setCreateBy(testingReportRequest.getRepairmanId());
        aPi.setLastModifiedDate(LocalDateTime.now());
        aPi.setLastModifiedBy(testingReportRequest.getRepairmanId());
        aPi.setDeleted("0");
        attachMapper.insert(aPi);
        TestingReportHead testingReportHead = new TestingReportHead();
        BeanUtils.copyProperties(testingReportRequest,testingReportHead);
        testingReportHead.setAttachId(aPi.getId());
        testingReportHead.setCreateDate(LocalDateTime.now());
        testingReportHead.setCreateBy(testingReportRequest.getRepairmanId());
        testingReportHead.setLastModifiedDate(LocalDateTime.now());
        testingReportHead.setLastModifiedBy(testingReportRequest.getRepairmanId());
        testingReportHead.setReportTime(testingReportHead.getCreateDate());
        testingReportHead.setDeleted("0");
        testingReportHeadMapper.insert(testingReportHead);

        for(TestingReportDetailRequest testingReportDetailRequest : testingReportRequest.getTestingReportDetailRequestList()){
            TestingReportDetail testingReportDetail = new TestingReportDetail();
            BeanUtils.copyProperties(testingReportDetailRequest,testingReportDetail);
            testingReportDetail.setTestingReportHeadId(testingReportHead.getId());
            testingReportDetail.setCreateDate(LocalDateTime.now());
            testingReportDetail.setCreateBy(testingReportRequest.getRepairmanId());
            testingReportDetail.setLastModifiedDate(LocalDateTime.now());
            testingReportDetail.setLastModifiedBy(testingReportRequest.getRepairmanId());
            testingReportDetail.setDeleted("0");
            testingReportDetailMapper.insert(testingReportDetail);
        }
        return ResultUtil.success();
    }

    @Override
    public TestingReportHead insertReturnEntity(TestingReportHead entity) {
        return null;
    }

    @Override
    public TestingReportHead updateReturnEntity(TestingReportHead entity) {
        return null;
    }
}
