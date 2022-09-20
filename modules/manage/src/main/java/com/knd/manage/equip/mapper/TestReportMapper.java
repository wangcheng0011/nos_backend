package com.knd.manage.equip.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.equip.entity.TestReportEntity;
import com.knd.manage.equip.entity.TestingReportDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 检修
 * @author zm
 */
public interface TestReportMapper extends BaseMapper<TestingReportDetail> {

    /**
     * 获取检修列表
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select a.id,a.equipmentNo,a.reportTime,c.nickName,b.filePath  " +
            " from testing_report_head a  " +
            " LEFT JOIN attach b on a.attachId = b.id " +
            " left join admin c on a.repairmanId = c.id " +
            " ${ew.customSqlSegment}")
    List<TestReportEntity> getList(Page<TestReportEntity> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
