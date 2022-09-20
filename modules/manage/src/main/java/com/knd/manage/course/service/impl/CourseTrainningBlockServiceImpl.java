package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.BaseBodyPart;
import com.knd.manage.course.dto.CourseBlockInfoDto;
import com.knd.manage.course.entity.CourseTrainningBlock;
import com.knd.manage.course.entity.CourseTrainningNodeInfo;
import com.knd.manage.course.mapper.CourseTrainningBlockMapper;
import com.knd.manage.course.mapper.CourseTrainningNodeInfoMapper;
import com.knd.manage.course.service.ICourseTrainningBlockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.manage.course.vo.BlockSort;
import com.knd.manage.course.vo.VoSaveCourseBlockInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-06
 */
@Service
@Transactional
public class CourseTrainningBlockServiceImpl extends ServiceImpl<CourseTrainningBlockMapper, CourseTrainningBlock> implements ICourseTrainningBlockService {

    @Resource
    private CourseTrainningNodeInfoMapper courseTrainningNodeInfoMapper;

    @Override
    public CourseTrainningBlock insertReturnEntity(CourseTrainningBlock entity) {
        return null;
    }

    @Override
    public CourseTrainningBlock updateReturnEntity(CourseTrainningBlock entity) {
        return null;
    }

    //查询训练课Block列表,返回list类型
    @Override
    public List<CourseTrainningBlock> getCourseBlockInfoToList(String id) {
        QueryWrapper<CourseTrainningBlock> qw = new QueryWrapper<>();
        qw.eq("courseId", id);
        qw.eq("deleted", "0");
        qw.orderByAsc("sort + 0");
        return baseMapper.selectList(qw);
    }

    //查询训练课Block列表
    @Override
    public Result getCourseBlockInfo(String id) {
        List<CourseTrainningBlock> list = this.getCourseBlockInfoToList(id);
        //拼接出参格式
        List<CourseBlockInfoDto> lr = new ArrayList<>();
        if (!list.isEmpty()) {
            for (CourseTrainningBlock c : list) {
                CourseBlockInfoDto courseBlockInfoDto = new CourseBlockInfoDto();
                courseBlockInfoDto.setId(c.getId());
                courseBlockInfoDto.setBlock(c.getBlock());
                courseBlockInfoDto.setAimSetNum(c.getAimSetNum());
                courseBlockInfoDto.setSort(c.getSort());
                lr.add(courseBlockInfoDto);
            }
        }
        return ResultUtil.success(lr);
    }

    //维护训练课Block信息
    @Override
    public Result saveCourseBlockInfo(VoSaveCourseBlockInfo vo) {
        //查重
        QueryWrapper<CourseTrainningBlock> qw = new QueryWrapper<>();
        qw.eq("courseId", vo.getId());
        qw.eq("block", vo.getBlock());
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        //检查序号
        qw.clear();
        qw.eq("courseId", vo.getId());
        qw.eq("sort", vo.getSort());
        qw.eq("deleted", "0");
        int s2 = baseMapper.selectCount(qw);
        if (s2 != 0) {
            //业务主键重复
            return ResultUtil.error("U0999", "序号不可重复");
        }
        //新增
        CourseTrainningBlock c = new CourseTrainningBlock();
        c.setId(UUIDUtil.getShortUUID());
        c.setCreateBy(vo.getUserId());
        c.setCreateDate(LocalDateTime.now());
        c.setLastModifiedBy(vo.getUserId());
        c.setLastModifiedDate(LocalDateTime.now());
        c.setDeleted("0");
        c.setSort(vo.getSort());
        c.setAimSetNum(vo.getAimSetNum());
        c.setBlock(vo.getBlock());
        c.setCourseId(vo.getId());
        baseMapper.insert(c);
        //成功
        return ResultUtil.success();
    }

    //编辑
    @Override
    public Result editCourseBlockInfo(VoSaveCourseBlockInfo vo) {
        //根据id获取名称
        QueryWrapper<CourseTrainningBlock> qw = new QueryWrapper<>();
        qw.eq("id", vo.getBlockId());
        qw.eq("deleted", "0");
        qw.select("courseId","block");
        CourseTrainningBlock ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getBlock().equals(vo.getBlock())){
            //查重
            qw.clear();
            //查重
            qw.eq("courseId", vo.getId());
            qw.eq("block", vo.getBlock());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        //更新
        CourseTrainningBlock b = new CourseTrainningBlock();
        b.setId(vo.getBlockId());
        b.setBlock(vo.getBlock());
        b.setAimSetNum(vo.getAimSetNum());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //批量更新训练课Block排序号
    @Override
    public Result updateCourseBlocksSort(String userId, List<BlockSort> blockSortList) {
        CourseTrainningBlock c = new CourseTrainningBlock();
        c.setLastModifiedDate(LocalDateTime.now());
        c.setLastModifiedBy(userId);
        for (BlockSort b : blockSortList) {
            c.setId(b.getId());
            c.setSort(b.getSort());
            baseMapper.updateById(c);
        }
        //成功
        return ResultUtil.success();
    }

    //删除训练课Block信息
    @Override
    public Result deleteCourseBlock(String userId, String blockId) {
        //根据 block id 查询 课程id
        QueryWrapper<CourseTrainningBlock> qw = new QueryWrapper<>();
        qw.select("courseId");
        qw.eq("deleted", "0");
        qw.eq("id", blockId);
        CourseTrainningBlock b = baseMapper.selectOne(qw);
        if (b == null) {
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //根据block id 、课程id查询是否有未删除的视频小节
        QueryWrapper<CourseTrainningNodeInfo> qw2 = new QueryWrapper<>();
        qw2.eq("deleted", "0");
        qw2.eq("courseId", b.getCourseId());
        qw2.eq("blockId", blockId);
        int count = courseTrainningNodeInfoMapper.selectCount(qw2);
        if (count != 0) {
            //有关联的视频小节，不可删除
            return ResultUtil.error(ResultEnum.BLOCK_NODE_ERROR);
        }
        //删除
        CourseTrainningBlock c = new CourseTrainningBlock();
        c.setLastModifiedDate(LocalDateTime.now());
        c.setLastModifiedBy(userId);
        c.setId(blockId);
        c.setDeleted("1");
        baseMapper.updateById(c);
        //成功
        return ResultUtil.success();
    }

}
