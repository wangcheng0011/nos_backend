package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.entity.CourseTrainningBlock;
import com.knd.manage.course.vo.BlockSort;
import com.knd.manage.course.vo.VoSaveCourseBlockInfo;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-06
 */
public interface ICourseTrainningBlockService extends SuperService<CourseTrainningBlock> {
    //查询训练课Block列表,返回list类型
    List<CourseTrainningBlock> getCourseBlockInfoToList(String id);

    //查询训练课Block列表
    Result getCourseBlockInfo(String id);

    //维护训练课Block信息
    Result saveCourseBlockInfo(VoSaveCourseBlockInfo vo);
    //编辑
    Result editCourseBlockInfo(VoSaveCourseBlockInfo vo);

    //批量更新训练课Block排序号
    Result updateCourseBlocksSort(String userId, List<BlockSort> blockSortList);

    //删除训练课Block信息
    Result deleteCourseBlock(String userId, String blockId);

}
