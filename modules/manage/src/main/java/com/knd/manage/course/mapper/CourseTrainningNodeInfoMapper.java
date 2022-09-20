package com.knd.manage.course.mapper;

import com.knd.manage.course.dto.BlockDataDto;
import com.knd.manage.course.entity.CourseTrainningNodeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.course.dto.NodeDto;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-06
 */
public interface CourseTrainningNodeInfoMapper extends BaseMapper<CourseTrainningNodeInfo> {

    //获取一组动作里的不重复的动作总数（去重）
    int selectActionCountForDistinct(@Param("courseId") String courseId, @Param("blockId") String blockId);

    ///根据block id 查询小节信息
    List<NodeDto> selectSomeByBlockid(String id);

    ///根据 课程 id 查询小节信息
    List<NodeDto> selectSomeByCourseId(String id);

    //检查是否有未删除的课程使用该动作
    int selectCourseCountByActionId(String id);

    //获取block范围数据
//    BlockDataDto getBlockDataDto(@Param("nodeSort") String nodeSort, @Param("blockId") String blockId, @Param("courseId") String courseId, @Param("nodeId") String nodeId);

    //
    //上一个block序号
    String getLastBlockSort(@Param("nodeSort") String nodeSort, @Param("courseId") String courseId, @Param("nodeId") String nodeId);

    //下一个block序号
    String getNextBlockSort(@Param("nodeSort") String nodeSort, @Param("courseId") String courseId, @Param("nodeId") String nodeId);

    //当前block序号
    String getNowBlockSort(@Param("blockId") String blockId);

    //小节绑定block的数量检查
    int selectBNCountByCourseId(String id);

}
