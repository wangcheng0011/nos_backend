package com.knd.manage.equip.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.equip.dto.EquipmentListDto;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.manage.equip.mapper.EquipmentInfoMapper;
import com.knd.manage.equip.service.IEquipmentInfoService;
import com.knd.redis.jedis.RedisClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
@Service
@Transactional
public class EquipmentInfoServiceImpl extends ServiceImpl<EquipmentInfoMapper, EquipmentInfo> implements IEquipmentInfoService {
     @Resource
    private RedisClient redisClient;

    @Override
    public EquipmentInfo insertReturnEntity(EquipmentInfo entity) {
        return null;
    }

    @Override
    public EquipmentInfo updateReturnEntity(EquipmentInfo entity) {
        return null;
    }

    //新增
    @Override
    public Result add(String userId, String equipmentNo, String remark,String courseHeadId) {
        //查重
        QueryWrapper<EquipmentInfo> qw = new QueryWrapper<>();
        qw.eq("equipmentNo", equipmentNo);
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        EquipmentInfo e = new EquipmentInfo();
        e.setId(UUIDUtil.getShortUUID());
        e.setEquipmentNo(equipmentNo);
        e.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
        e.setRemark(remark);
        e.setCreateBy(userId);
        e.setCreateDate(LocalDateTime.now());
        e.setLastModifiedBy(userId);
        e.setLastModifiedDate(LocalDateTime.now());
        e.setDeleted("0");
        baseMapper.insert(e);
        //成功
        return ResultUtil.success();
    }


    //更新
    @Override
    public Result edit(String userId, String equipmentNo, String remark, String id,String courseHeadId) {
        //根据id获取名称
        QueryWrapper<EquipmentInfo> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        qw.select("equipmentNo");
        EquipmentInfo eq = baseMapper.selectOne(qw);
        if (eq ==null){
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!eq.getEquipmentNo().equals(equipmentNo)) {
            //查重
            qw.clear();
            qw.eq("equipmentNo", equipmentNo);
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        //
        EquipmentInfo e = new EquipmentInfo();
        e.setId(id);
        e.setEquipmentNo(equipmentNo);
        e.setRemark(remark);
        e.setLastModifiedBy(userId);
        e.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(e);
        redisClient.del(eq.getEquipmentNo());
        redisClient.set(e.getEquipmentNo(), JSONObject.toJSONString(e));
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result changeStatus(String userId, String status, String id,String equipmentNo) {
        //根据id获取名称
        QueryWrapper<EquipmentInfo> qw = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(id)){
            qw.eq("id", id);
        }
        if(StringUtils.isNotEmpty(equipmentNo)){
            qw.eq("equipmentNo", equipmentNo);
        }
        qw.eq("deleted", "0");
        //qw.select("status");
        EquipmentInfo eq = baseMapper.selectOne(qw);
        if (eq ==null){
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }


        eq.setStatus(status);
        eq.setLastModifiedBy(userId);
        eq.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(eq);
        if("0".equals(eq.getStatus())) {
            redisClient.del(eq.getEquipmentNo());
        }

        //成功
        return ResultUtil.success();
    }

    //删除
    @Override
    public Result deleteEquipment(String userId, String id) {
        EquipmentInfo b = baseMapper.selectById(id);
        if(b != null) {
            b.setId(id);
            b.setDeleted("1");
            b.setLastModifiedBy(userId);
            b.setLastModifiedDate(LocalDateTime.now());
            baseMapper.deleteById(id);
            redisClient.del(b.getEquipmentNo());
            //成功
            return ResultUtil.success();
        }else{
            //成功
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该资源不存在，无法删除！");
        }

    }

    //获取设备信息
    @Override
    public Result getEquipment(String id) {
        QueryWrapper<EquipmentInfo> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.select("id", "equipmentNo","status", "remark");
        qw.eq("deleted", "0");
        EquipmentInfo b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    //获取设备信息列表
    @Override
    public Result getEquipmentList(String equipmentNo, String status,String currentPage) {
        QueryWrapper<EquipmentInfo> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(equipmentNo)) {
            qw.like("equipmentNo", equipmentNo);
        }
        if (StringUtils.isNotEmpty(status)) {
            qw.like("status", status);
        }
        qw.select("id", "equipmentNo", "status","remark", "createDate");
        qw.eq("deleted", "0");
        qw.orderByDesc("createDate");
        List<EquipmentInfo> equipmentList;
        EquipmentListDto equipmentListDto = new EquipmentListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            equipmentList = baseMapper.selectList(qw);
            equipmentListDto.setTotal(equipmentList.size());
        } else {
            //分页
            Page<EquipmentInfo> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            equipmentList = partPage.getRecords();
            equipmentListDto.setTotal((int)partPage.getTotal());
        }
        equipmentListDto.setEquipmentList(equipmentList);
        //成功
        return ResultUtil.success(equipmentListDto);
    }

    @Override
    public List<EquipmentInfo> getEquipmentListByCourseId(String courseId) {
        QueryWrapper<EquipmentInfo> ei = new QueryWrapper<>();
        ei.eq("deleted","0");
        ei.eq("courseHeadId",courseId);
        List<EquipmentInfo> equipmentInfos = baseMapper.selectList(ei);
        return equipmentInfos;
    }

    @Transactional
    @Override
    public Result saveEquipmentByBatch(InputStream inputStream, String originalFilename) {

        try {
            List<EquipmentInfo> list = getListByExcel(inputStream,originalFilename);
            for(EquipmentInfo equipmentInfo : list) {
                QueryWrapper<EquipmentInfo> objectQueryWrapper = new QueryWrapper<>();
                objectQueryWrapper.eq("equipmentNo",Md5Utils.md5(equipmentInfo.getEquipmentNo()));
                List<EquipmentInfo> equipmentInfo1 = baseMapper.selectList(objectQueryWrapper);
                if(equipmentInfo1.size()>0){
                    continue;
                }
                equipmentInfo.setEquipmentNo(Md5Utils.md5(equipmentInfo.getEquipmentNo()));
                equipmentInfo.setStatus(OrderStatusEnum.WAIT_FOR_PAY.getCode());
                equipmentInfo.setId(UUIDUtil.getShortUUID());
                equipmentInfo.setCreateBy(UserUtils.getUserId());
                equipmentInfo.setCreateDate(LocalDateTime.now());
                equipmentInfo.setLastModifiedBy(UserUtils.getUserId());
                equipmentInfo.setLastModifiedDate(LocalDateTime.now());
                equipmentInfo.setDeleted("0");
                baseMapper.insert(equipmentInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.FILE_NONE_ERROR);
        }
        return ResultUtil.success();
    }

    /**
     * 处理上传的文件
     *
     * @param in
     * @param fileName
     * @return
     * @throws Exception
     */
    public List getListByExcel(InputStream in, String fileName) throws Exception {
        List<EquipmentInfo> list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                //List<EquipmentInfo> li = new ArrayList<>();
                EquipmentInfo equipmentInfo = new EquipmentInfo();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    cell.setCellType(CellType.STRING);
                    if(y == 0) {
                        equipmentInfo.setEquipmentNo(cell.getStringCellValue());
                    }else{
                        equipmentInfo.setRemark(cell.getStringCellValue());
                    }
                }
                list.add(equipmentInfo);
            }
        }
        work.close();
        return list;
    }

    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }


}
