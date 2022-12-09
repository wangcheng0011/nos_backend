package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.course.entity.CourseHead;
import com.knd.manage.course.entity.CourseTrainningNodeInfo;
import com.knd.manage.course.mapper.CourseHeadMapper;
import com.knd.manage.course.mapper.CourseTrainningNodeInfoMapper;
import com.knd.manage.course.service.ICourseTrainningNodeInfoService;
import com.knd.manage.course.vo.VoSaveCourseNodeInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
public class CourseTrainningNodeInfoServiceImpl extends ServiceImpl<CourseTrainningNodeInfoMapper, CourseTrainningNodeInfo> implements ICourseTrainningNodeInfoService {


    @Resource
    private CourseHeadMapper courseHeadMapper;

    @Resource
    private CourseTrainningNodeInfoMapper courseTrainningNodeInfoMapper;

    @Override
    public CourseTrainningNodeInfo insertReturnEntity(CourseTrainningNodeInfo entity) {
        return null;
    }

    @Override
    public CourseTrainningNodeInfo updateReturnEntity(CourseTrainningNodeInfo entity) {
        return null;
    }

    //维护训练课小节信息
    @Override
    public Result saveCourseNodeInfo(VoSaveCourseNodeInfo vo) {
        //获取课程视频信息
        QueryWrapper<CourseHead> qw2 = new QueryWrapper<>();
        qw2.eq("id", vo.getCourseId());
        qw2.select("videoDurationMinutes", "videoDurationSeconds");
        qw2.eq("deleted", "0");
        //获取课程信息
        CourseHead courseHead = courseHeadMapper.selectOne(qw2);
        if (StringUtils.isEmpty(courseHead.getVideoDurationMinutes()) || StringUtils.isEmpty(courseHead.getVideoDurationSeconds())) {
            return ResultUtil.error("U0995", "失败,未设置视频总时长");
        }
        //总视频时长
        long countTime = (Integer.parseInt(courseHead.getVideoDurationMinutes())) * 60 + Integer.parseInt(courseHead.getVideoDurationSeconds());
        //检查视频最大时长不能超过课程主表总视频时长，，，已经存储的则说明不会超出，所以不需要比较，仅需要比较新增的即可
        if ((Integer.parseInt(vo.getNodeEndMinutes()) * 60 + Integer.parseInt(vo.getNodeEndSeconds())) > countTime) {
            //超出总视频时长
            return ResultUtil.error("U0995", "超出总视频时长");
        }
        //小节序号
        int sort;
        try {
            sort = Integer.parseInt(vo.getNodeSort());
        } catch (Exception e) {
            //小节序号请输入非负整数
            return ResultUtil.error("U0995", "小节序号请输入非负整数");
        }
        QueryWrapper<CourseTrainningNodeInfo> qw = new QueryWrapper<>();
        //获取该课程的所有视频小节序号
        qw.select("nodeSort", "blockId");
        qw.eq("deleted", "0");
        qw.eq("courseId", vo.getCourseId());
        qw.orderByAsc("length(nodeSort + 0)","nodeSort + 0");
        if (vo.getPostType().equals("2")) {
            //更新
            //去掉自己的小节序号
            qw.ne("id", vo.getId());
        }
        List<CourseTrainningNodeInfo> ln = baseMapper.selectList(qw);
        //判断小节序号是否重复
        for (CourseTrainningNodeInfo n : ln) {
            if (vo.getNodeSort().equals(n.getNodeSort())) {
                return ResultUtil.error("U0995", "小节序号重复，请输入正确的小节序号");
            }
        }

        if (vo.getActionFlag().equals("1")) {
            //上一个block序号
            String lastBlockSort = baseMapper.getLastBlockSort(vo.getNodeSort(), vo.getCourseId(), vo.getPostType().equals("1") ? null : vo.getId());
            //下一个block序号
            String nextBlockSort = baseMapper.getNextBlockSort(vo.getNodeSort(), vo.getCourseId(), vo.getPostType().equals("1") ? null : vo.getId());
            //当前block序号
            String nowBlockSort = baseMapper.getNowBlockSort(vo.getBlockId());

            //
            if (nowBlockSort == null) {
                return ResultUtil.error("U0995", "绑定错误的block");
            }
            if (StringUtils.isNotEmpty(lastBlockSort)) {
                if (Integer.parseInt(nowBlockSort) < Integer.parseInt(lastBlockSort)) {
                    return ResultUtil.error("U0995", "请按block顺序绑定");
                }
            }
            if (StringUtils.isNotEmpty(nextBlockSort)) {
                if (Integer.parseInt(nowBlockSort) > Integer.parseInt(nextBlockSort)) {
                    return ResultUtil.error("U0995", "请按block顺序绑定");
                }
            }

        }

        //获取该小节信息上一个序号小节 和下一小节的 区间 ( sl , sn)
        int sl = -1, sn = -1;
        //如果是空的，则可以直接存储
        if (!ln.isEmpty()) {
            if (sort < Integer.parseInt(ln.get(0).getNodeSort())) {
                //在开头
                sn = Integer.parseInt(ln.get(0).getNodeSort());
            } else if (Integer.parseInt(ln.get(ln.size() - 1).getNodeSort()) < sort) {
                //在末尾
                sl = Integer.parseInt(ln.get(ln.size() - 1).getNodeSort());
            } else {
                //在中间
                for (int i = 0; i < ln.size() - 1; i++) {
                    if (Integer.parseInt(ln.get(i).getNodeSort()) < sort && sort < Integer.parseInt(ln.get(i + 1).getNodeSort())) {
                        sl = Integer.parseInt(ln.get(i).getNodeSort());
                        sn = Integer.parseInt(ln.get(i + 1).getNodeSort());
                    }
                }
            }
            if (sl != -1) {
                //说明有值
                //获取上一小节的结束时间
                //TODO
                qw.clear();
                qw.eq("deleted", "0");
                qw.eq("courseId", vo.getCourseId());
                qw.eq("nodeSort", sl + "");
                qw.select("*");
                CourseTrainningNodeInfo n = baseMapper.selectOne(qw);
                int end = Integer.parseInt(n.getNodeEndMinutes()) * 60 + Integer.parseInt(n.getNodeEndSeconds());
                //新小节开始时间
                int nbegin = Integer.parseInt(vo.getNodeBeginMinutes()) * 60 + Integer.parseInt(vo.getNodeBeginSeconds());
                if (nbegin <= end) {
                    //视频开始和结束时间有交叉，返回参数异常
                    return ResultUtil.error("U0995", "视频开始和结束时间有交叉");
                }
            }
            if (sn != -1) {
                //说明有值
                //获取下一小节的开始时间
                qw.clear();
                qw.eq("deleted", "0");
                qw.eq("courseId", vo.getCourseId());
                qw.eq("nodeSort", sn + "");
                qw.last("limit 1");
                qw.select("nodeBeginMinutes", "nodeBeginSeconds");
                CourseTrainningNodeInfo n = baseMapper.selectOne(qw);
                int begin = Integer.parseInt(n.getNodeBeginMinutes()) * 60 + Integer.parseInt(n.getNodeBeginSeconds());
                //新小节结束时间
                int nend = Integer.parseInt(vo.getNodeEndMinutes()) * 60 + Integer.parseInt(vo.getNodeEndSeconds());
                if (nend >= begin) {
                    //视频开始和结束时间有交叉，返回参数异常
                    return ResultUtil.error("U0995", "视频开始和结束时间有交叉");
                }
            }
            //判断动作组数
            if (vo.getActionFlag().equals("1")) {
                //是动作
                //查重，判断新增的动作是否已经存在
                qw.clear();
                qw.eq("blockId", vo.getBlockId());
                qw.eq("courseId", vo.getCourseId());
                qw.eq("deleted", "0");
                qw.eq("actionId", vo.getActionId());
                qw.eq("actionFlag", "1");
                int num = baseMapper.selectCount(qw);
                if (num == 0) {
                    //是新的动作
                    //验证课程每个block下面动作数量不能超过5个。超过5个时返回错误信息
                    //获取一组动作里的不重复的动作总数（去重）
                    int actionCount = baseMapper.selectActionCountForDistinct(vo.getCourseId(), vo.getBlockId());
                    if (actionCount >= 5) {
                        //分组下维护动作数量太多
                        return ResultUtil.error(ResultEnum.BLOCK_ACTION_TOOMORE);
                    }
                }
            }
        }
        //存储
        CourseTrainningNodeInfo c = new CourseTrainningNodeInfo();
        c.setCourseId(vo.getCourseId());
        c.setNodeSort(vo.getNodeSort());
        c.setNodeName(vo.getNodeName());
        c.setActionFlag(vo.getActionFlag());
        c.setNodeBeginMinutes(vo.getNodeBeginMinutes());
        c.setNodeBeginSeconds(vo.getNodeBeginSeconds());
        c.setNodeEndMinutes(vo.getNodeEndMinutes());
        c.setNodeEndSeconds(vo.getNodeEndSeconds());
        c.setCountDownFlag(vo.getCountDownFlag());
        c.setMusicId(vo.getMusicId());
        c.setCountdownLength(vo.getCountdownLength());
        c.setBlockRests(vo.getBlockRests());
        //只有是动作才有 维护训练动作目标值
        if (vo.getActionFlag().equals("1")) {
            c.setBlockId(vo.getBlockId());
            c.setActionId(vo.getActionId());
            c.setTrainingFlag(vo.getTrainingFlag());
            if (vo.getTrainingFlag().equals("1")) {
                c.setAimDuration(vo.getAimDuration());
                c.setAimTimes(vo.getAimTimes());
                c.setEndNodePeriod(vo.getEndNodePeriod());
            } else {
                c.setAimDuration("");
                c.setAimTimes("");
                c.setEndNodePeriod("0");
            }
        } else {
            c.setBlockId(vo.getBlockId());
            c.setActionId(vo.getActionId());
            //清空
            c.setTrainingFlag("");
            c.setAimDuration("");
            c.setAimTimes("");
            c.setEndNodePeriod("0");
        }
        c.setLastModifiedBy(vo.getUserId());
        c.setLastModifiedDate(LocalDateTime.now());
        //
        if (vo.getPostType().equals("1")) {
            //新增
            c.setId(UUIDUtil.getShortUUID());
            c.setCreateBy(vo.getUserId());
            c.setCreateDate(LocalDateTime.now());
            c.setDeleted("0");
            baseMapper.insert(c);
        } else {
            //更新
            c.setId(vo.getId());
            baseMapper.updateById(c);
        }
        return ResultUtil.success();
    }

    //查询训练课小节信息
    @Override
    public Result getCourseNodeInfo(String id) {
        QueryWrapper<CourseTrainningNodeInfo> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.select("id", "courseId", "nodeSort", "nodeName", "actionFlag", "nodeBeginMinutes", "nodeBeginSeconds",
                "nodeEndMinutes", "nodeEndSeconds", "countDownFlag", "blockId", "actionId", "aimDuration", "aimTimes", "trainingFlag", "endNodePeriod");
        qw.eq("deleted", "0");
        CourseTrainningNodeInfo c = baseMapper.selectOne(qw);
        return ResultUtil.success(c);
    }

    //删除训练课小节信息
    @Override
    public Result deleteCourseNodeInfo(String userId, String id) {
        //根据视频小节id查询课程id
        QueryWrapper<CourseTrainningNodeInfo> qw = new QueryWrapper();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        qw.select("courseId","blockId");
        CourseTrainningNodeInfo n = baseMapper.selectOne(qw);
        System.out.println(n.toString());
        if (n == null) {
            return ResultUtil.error("U995", "小节id不存在");
        }
        //检查当前的绑定block的视频小节总数
        int bnCount = courseTrainningNodeInfoMapper.selectBNCountByCourseId(n.getCourseId());
        System.out.println(bnCount);
        if (bnCount <= 1) {
            System.out.println("2222222222222222222222");
            //查询当前视频小节是否绑定block
            if (StringUtils.isNotEmpty(n.getBlockId())) {
                System.out.println("33333333333333333333333333");
                //检查是否为发布状态
                QueryWrapper<CourseHead> qw2 = new QueryWrapper();
                qw2.eq("id", n.getCourseId());
                qw2.eq("deleted", "0");
                qw2.select("releaseFlag");
                CourseHead h = courseHeadMapper.selectOne(qw2);
                System.out.println(h.toString());
                if (h.getReleaseFlag().equals("1")) {
                    System.out.println("44444444444444444444444444444");
                    return ResultUtil.error("U995", "发布状态不可删除最后一个绑定block的视频小节");
                }
            }
        }
        CourseTrainningNodeInfo c = new CourseTrainningNodeInfo();
        c.setId(id);
        c.setDeleted("1");
        c.setLastModifiedBy(userId);
        c.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(c);
        //成功
        return ResultUtil.success();
    }

}
