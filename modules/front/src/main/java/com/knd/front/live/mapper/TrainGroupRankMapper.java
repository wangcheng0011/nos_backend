package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.knd.front.user.dto.UserTrainDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainGroupRankMapper {

    /**
     * 爆发力
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl,  " +
            "   IFNULL(SUM(total.maxExplosiveness),'0') as trainNum,     " +
            "   IFNULL(praise.praise,'1') as praise,  " +
            "   IFNULL(praiseNum.num,'0') as praiseNum     " +
            "  from lb_train_group_user gp   " +
            "  JOIN user u on gp.userId = u.id and u.deleted=0   " +
            "  LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0   " +
            "  LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0   " +
            "  LEFT JOIN(     " +
            "   select userId,maxExplosiveness from train_action_array_head      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "  union all     " +
            "   select userId,maxExplosiveness from train_course_head_info      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "  union all     " +
            "   select userId,maxExplosiveness from train_free_head      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "  ) total  on total.userId = u.id     " +
            "  left join train_user_praise praise      " +
            "   on praise.UserId = #{userId}      " +
            "   and praise.praiseUserId = u.id     " +
            "   and praise.praiseType='2'     " +
            "   and praise.createDate BETWEEN #{beginDate} and #{endDate}     " +
            "  left join (     " +
            "    select praiseUserId,count(1) as num from train_user_praise     " +
            "    where createDate BETWEEN #{beginDate} and #{endDate}     " +
            "      and praise = '0' and praiseType='2' and deleted='0'     " +
            "    GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId     " +
            "   where gp.trainGroupId = #{groupId} and gp.joinStatus='1' GROUP BY u.id")
    List<UserTrainDto> getMaxPower(@Param("userId") String userId,
                                   @Param("groupId") String groupId,
                                   @Param("beginDate") LocalDateTime beginDate,
                                   @Param("endDate") LocalDateTime endDate);
    /**
     * 总力量
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl,  " +
            "   IFNULL(SUM(total.finishTotalPower),'0') as trainNum,     " +
            "   IFNULL(praise.praise,'1') as praise,  " +
            "   IFNULL(praiseNum.num,'0') as praiseNum    " +
            "  from lb_train_group_user gp  " +
            "  JOIN user u on gp.userId = u.id and u.deleted=0    " +
            "  LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0   " +
            "  LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0   " +
            "  LEFT JOIN(     " +
            "   select userId,finishTotalPower from train_action_array_head      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   union all     " +
            "   select userId,finishTotalPower from train_course_head_info      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   union all     " +
            "   select userId,finishTotalPower from train_free_head      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   ) total on total.userId = u.id     " +
            "   left join train_user_praise praise      " +
            "   on praise.UserId = #{userId}    " +
            "   and praise.praiseUserId = u.id     " +
            "   and praise.praiseType='0'    " +
            "   and praise.createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   left join (     " +
            "    select praiseUserId,count(1) as num from train_user_praise     " +
            "    where createDate BETWEEN #{beginDate} and #{endDate}     " +
            "     and praise = '0' and praiseType='0' and deleted='0'     " +
            "    GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId     " +
            "   where gp.trainGroupId = #{groupId} and gp.joinStatus='1' GROUP BY u.id")
    List<UserTrainDto> getPower(@Param("userId") String userId,
                                @Param("groupId") String groupId,
                                @Param("beginDate") LocalDateTime beginDate,
                                @Param("endDate") LocalDateTime endDate);


    /**
     * 毅力
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl,  " +
            "   IFNULL(SUM(total.actTrainSeconds),'0') as trainNum,     " +
            "   IFNULL(praise.praise,'1') as praise,  " +
            "   IFNULL(praiseNum.num,'0') as praiseNum   " +
            "  from lb_train_group_user gp   " +
            "  JOIN user u on gp.userId = u.id and u.deleted=0   " +
            "  LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0   " +
            "  LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0   " +
            "  LEFT JOIN(     " +
            "   select userId,actTrainSeconds from train_action_array_head      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   union all     " +
            "   select userId,actualTrainSeconds from train_course_head_info      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   union all     " +
            "   select userId,actTrainSeconds from train_free_head      " +
            "   where deleted = 0      " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   ) total on total.userId = u.id     " +
            "   left join train_user_praise praise      " +
            "   on praise.UserId = #{userId}     " +
            "   and praise.praiseUserId = u.id     " +
            "   and praise.praiseType='1'    " +
            "   and praise.createDate BETWEEN #{beginDate} and #{endDate}     " +
            "   left join (     " +
            "    select praiseUserId,count(1) as num from train_user_praise     " +
            "    where createDate BETWEEN #{beginDate} and #{endDate}     " +
            "     and praise = '0' and praiseType='1' and deleted='0'     " +
            "    GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId     " +
            "   where gp.trainGroupId = #{groupId} and gp.joinStatus='1' GROUP BY u.id ORDER BY trainNum desc,praiseNum desc")
    List<UserTrainDto> getWill(@Param("userId") String userId,
                         @Param("groupId") String groupId,
                         @Param("beginDate") LocalDateTime beginDate,
                         @Param("endDate") LocalDateTime endDate);

    /**
     * 拼接参数获取小组排行榜
     * @param userId
     * @param paramType
     * @param praiseType
     * @param beginDate
     * @param endDate
     * @param wrapper
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl,     " +
            "   IFNULL(SUM(total.${paramType}),'0') as trainNum,        " +
            "   IFNULL(praise.praise,'1') as praise,     " +
            "   IFNULL(praiseNum.num,'0') as praiseNum       " +
            "   from lb_train_group_user gp     " +
            "   JOIN user u on gp.userId = u.id and u.deleted=0       " +
            "   LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0      " +
            "   LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0      " +
            "   LEFT JOIN(        " +
            "   select userId,${paramType} from train_action_array_head         " +
            "   where deleted = 0         " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}        " +
            "   union all        " +
            "   select userId,${paramType} from train_course_head_info         " +
            "   where deleted = 0         " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}        " +
            "   union all        " +
            "   select userId,${paramType} from train_free_head         " +
            "   where deleted = 0         " +
            "   and createDate BETWEEN #{beginDate} and #{endDate}        " +
            "   ) total on total.userId = u.id        " +
            "   left join train_user_praise praise         " +
            "   on praise.UserId = #{userId}       " +
            "   and praise.praiseUserId = u.id        " +
            "   and praise.praiseType=#{praiseType}       " +
            "   and praise.createDate BETWEEN #{beginDate} and #{endDate}        " +
            "   left join (        " +
            "      select praiseUserId,count(1) as num from train_user_praise        " +
            "      where createDate BETWEEN #{beginDate} and #{endDate}        " +
            "      and praise = '0' and praiseType=#{praiseType} and deleted='0'        " +
            "      GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId " +
            "   where gp.trainGroupId = #{groupId} and gp.joinStatus='1' GROUP BY u.id ORDER BY trainNum desc,praiseNum desc")
    List<UserTrainDto> getGroupTrainByParam(@Param("userId") String userId,
                                            @Param("groupId") String groupId,
                                            @Param("paramType") String paramType,
                                            @Param("praiseType") String praiseType,
                                            @Param("beginDate") LocalDateTime beginDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param(Constants.WRAPPER) Wrapper wrapper);
}
