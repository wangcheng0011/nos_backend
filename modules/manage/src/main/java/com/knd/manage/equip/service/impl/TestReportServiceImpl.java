package com.knd.manage.equip.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.equip.dto.TestReportDetailDto;
import com.knd.manage.equip.dto.TestReportFaultDto;
import com.knd.manage.equip.dto.TestReportListDto;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.manage.equip.entity.TestReportEntity;
import com.knd.manage.equip.entity.TestingReportDetail;
import com.knd.manage.equip.mapper.TestReportMapper;
import com.knd.manage.equip.service.TestReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 协议
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestReportServiceImpl implements TestReportService {

    private final TestReportMapper testReportMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result getTestReportList(String equipmentNo,String currentPage) {
        Page<TestReportEntity> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        QueryWrapper<TestReportEntity> wrapper = Wrappers.query();
        wrapper.eq("a.deleted","0");
        if(equipmentNo!=null){
            wrapper.like("a.equipmentNo",equipmentNo);
        }
        List<TestReportEntity> list = testReportMapper.getList(tPage, wrapper);
        tPage.setRecords(list);
        list.stream().forEach(item -> {
            item.setFilePath(fileImagesPath+item.getFilePath());
        });
        TestReportListDto dto = TestReportListDto.builder().total((int)tPage.getTotal()).testReportEntityList(list).build();
        return ResultUtil.success(dto);
    }

    @Override
    public Result getTestReportDetail(String id) {
        QueryWrapper<TestingReportDetail> qw = new QueryWrapper<>();
        qw.eq("testingReportHeadId",id);
        qw.eq("deleted","0");
        List<TestingReportDetail> testingReportDetail = testReportMapper.selectList(qw);

        //正常项目集合
        List<String> normalList = new ArrayList<>();
        //故障集合
        List<TestReportFaultDto> faultList = new ArrayList<>();
        for(TestingReportDetail detail : testingReportDetail){
            String testingReportStatus = detail.getTestingReportStatus();
            if("0".equals(testingReportStatus)){
                normalList.add(detail.getFaultDesc());
            }else{
                TestReportFaultDto dto = TestReportFaultDto.builder().build();
                BeanUtils.copyProperties(detail, dto);
                faultList.add(dto);
            }
        }
        TestReportDetailDto detailDto = TestReportDetailDto.builder()
                .normalList(normalList).faultList(faultList).build();
        return ResultUtil.success(detailDto);
    }
}
