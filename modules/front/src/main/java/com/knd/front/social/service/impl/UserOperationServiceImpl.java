package com.knd.front.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.FollowOperationEnum;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.dto.VoUrl;
import com.knd.front.entity.Attach;
import com.knd.front.social.dto.MomentAddressDto;
import com.knd.front.social.entity.*;
import com.knd.front.social.mapper.*;
import com.knd.front.social.request.*;
import com.knd.front.social.service.MomentAddressService;
import com.knd.front.social.service.UserOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserOperationServiceImpl implements UserOperationService {
    private final UserSocialMomentPraiseMapper userSocialMomentPraiseMapper;
    private final UserSocialMomentMapper userSocialMomentMapper;
    private final UserSocialMomentCommentMapper userSocialMomentCommentMapper;
    private final UserSocialMomentAttachMapper userSocialMomentAttachMapper;
    private final UserSocialRelationMapper userSocialRelationMapper;
    private final UserSocialFollowMapper userSocialFollowMapper;
    private final UserSocialAttachMapper userSocialAttachMapper;
    private final AttachService attachService;
    private final MomentAddressService addressService;

    @Override
    public Result moment(OperationMomentRequest request) {
        UserSocialMomentEntity entity = new UserSocialMomentEntity();
        BeanUtils.copyProperties(request,entity);
        entity.setId(UUIDUtil.getShortUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setDeleted("0");
        userSocialMomentMapper.insert(entity);

        List<VoUrl> imageList = request.getImageList();
        if(imageList!=null){
            for (VoUrl url : imageList){
                if(StringUtils.isNotEmpty(url.getPicAttachName()) &&
                        StringUtils.isNotEmpty(url.getPicAttachNewName()) &&
                        StringUtils.isNotEmpty(url.getPicAttachSize())){
                    //保存选中图片
                    Attach attach = attachService.saveAttach(request.getUserId(), url.getPicAttachName()
                            , url.getPicAttachNewName(), url.getPicAttachSize());
                    UserSocialMomentAttachEntity attachEntity = new UserSocialMomentAttachEntity();
                    attachEntity.setId(UUIDUtil.getShortUUID());
                    attachEntity.setMomentId(entity.getId());
                    attachEntity.setAttachId(attach.getId());
                    entity.setDeleted("0");
                    userSocialMomentAttachMapper.insert(attachEntity);
                }
            }
        }
        MomentAddressDto addressDto = request.getAddressDto();
        if(addressDto!=null){
            addressService.add(request.getAddressDto(),entity.getId());
        }
        return ResultUtil.success();
    }

    @Override
    public Result praise(OperationPraiseRequest request) {
        UserSocialMomentEntity momentEntity = userSocialMomentMapper.selectById(request.getMomentId());
        if(momentEntity == null){
            return ResultUtil.error("U0999", "动态不存在");
        }
        long thumbup = momentEntity.getThumbup();
        QueryWrapper<UserSocialMomentPraiseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId",request.getUserId());
        wrapper.eq("momentId",request.getMomentId());
        wrapper.eq("deleted","0");
        int num = userSocialMomentPraiseMapper.selectCount(wrapper);
        if(num==0){
            UserSocialMomentPraiseEntity entity = new UserSocialMomentPraiseEntity();
            BeanUtils.copyProperties(request,entity);
            entity.setDeleted("0");
            entity.setCreateBy(request.getUserId());
            entity.setLastModifiedBy(request.getUserId());
            userSocialMomentPraiseMapper.insert(entity);
            thumbup = thumbup+1;
        }else{
            UserSocialMomentPraiseEntity entity = userSocialMomentPraiseMapper.selectOne(wrapper);
            String praise = entity.getPraise();
            //已点赞，执行取消点赞操作
            if("0".equals(praise)){
                entity.setPraise("1");
                thumbup = thumbup-1;
            }else  if("1".equals(praise)){
                //已取消，执行点赞操作
                entity.setPraise("0");
                thumbup = thumbup+1;
            }
            entity.setLastModifiedBy(request.getUserId());
            userSocialMomentPraiseMapper.updateById(entity);
        }
        momentEntity.setThumbup(thumbup);
        momentEntity.setUpdateTime(LocalDateTime.now());
        userSocialMomentMapper.updateById(momentEntity);
        return ResultUtil.success();
    }

    @Override
    public Result comment(OperationCommentRequest request) {
        UserSocialMomentEntity momentEntity = userSocialMomentMapper.selectById(request.getMomentId());
        if(momentEntity == null){
            return ResultUtil.error("U0999", "动态不存在");
        }
        long comments = momentEntity.getComments();
        UserSocialMomentCommentEntity entity = new UserSocialMomentCommentEntity();
        BeanUtils.copyProperties(request,entity);
        entity.setId(UUIDUtil.getShortUUID());
        entity.setPublishTime(LocalDateTime.now());
        entity.setDeleted("0");
        userSocialMomentCommentMapper.insert(entity);
        comments = comments +1;
        momentEntity.setComments(comments);
        momentEntity.setUpdateTime(LocalDateTime.now());
        userSocialMomentMapper.updateById(momentEntity);
        return ResultUtil.success();
    }

    @Override
    public Result followOrPass(OperationFollowRequest request) {
        QueryWrapper<UserSocialRelationEntity> relationWrapper1 = new QueryWrapper<>();
        relationWrapper1.eq("deleted","0");
        relationWrapper1.eq("userId",request.getUserId());
        relationWrapper1.eq("friendId",request.getTargetUserId());
        UserSocialRelationEntity relation1 = userSocialRelationMapper.selectOne(relationWrapper1);

        QueryWrapper<UserSocialRelationEntity> relationWrapper2 = new QueryWrapper<>();
        relationWrapper2.eq("deleted","0");
        relationWrapper2.eq("friendId",request.getUserId());
        relationWrapper2.eq("userId",request.getTargetUserId());
        UserSocialRelationEntity relation2 = userSocialRelationMapper.selectOne(relationWrapper2);

        QueryWrapper<UserSocialFollowEntity> followWrapper = new QueryWrapper<>();
        followWrapper.eq("deleted","0");
        followWrapper.eq("userId",request.getUserId());
        followWrapper.eq("targetUserId",request.getTargetUserId());
        UserSocialFollowEntity follow = userSocialFollowMapper.selectOne(followWrapper);

        QueryWrapper<UserSocialFollowEntity> beFollowWrapper = new QueryWrapper<>();
        beFollowWrapper.eq("deleted","0");
        beFollowWrapper.eq("targetUserId",request.getUserId());
        beFollowWrapper.eq("userId",request.getTargetUserId());
        UserSocialFollowEntity beFollow = userSocialFollowMapper.selectOne(beFollowWrapper);

        FollowOperationEnum followOperation = request.getFollowOperation();
        if(FollowOperationEnum.FOLLOW.equals(followOperation)){
            //关注
           /* if(beFollow !=null){
                return ResultUtil.error(ResultEnum.FAIL);
            }*/
            if(follow == null){
                UserSocialFollowEntity entity = new UserSocialFollowEntity();
                entity.setId(UUIDUtil.getShortUUID());
                entity.setUserId(request.getUserId());
                entity.setTargetUserId(request.getTargetUserId());
                entity.setDeleted("0");
                userSocialFollowMapper.insert(entity);
            }else{
                return ResultUtil.error(ResultEnum.FAIL);
            }
        }else if (FollowOperationEnum.PASS.equals(followOperation)){
            //取关
            if(follow == null){
                return ResultUtil.error(ResultEnum.FAIL);
            }else{
                follow.setDeleted("1");
                userSocialFollowMapper.updateById(follow);
                //判断是否为好友
                if(relation1!=null){
                    relation1.setDeleted("1");
                    userSocialRelationMapper.updateById(relation1);
                }else{
                    if(relation2!=null){
                        relation2.setDeleted("1");
                        userSocialRelationMapper.updateById(relation2);
                    }
                }
            }
        }else if (FollowOperationEnum.BACK_FOLLOW.equals(followOperation)){
            //回关
            if(follow==null && beFollow!=null){
                UserSocialFollowEntity entity = new UserSocialFollowEntity();
                entity.setId(UUIDUtil.getShortUUID());
                entity.setUserId(request.getUserId());
                entity.setTargetUserId(request.getTargetUserId());
                entity.setDeleted("0");
                userSocialFollowMapper.insert(entity);

                UserSocialRelationEntity relationEntity = new UserSocialRelationEntity();
                relationEntity.setId(UUIDUtil.getShortUUID());
                relationEntity.setUserId(request.getUserId());
                relationEntity.setFriendId(request.getTargetUserId());
                relationEntity.setDeleted("0");
                userSocialRelationMapper.insert(relationEntity);
            }else{
                return ResultUtil.error(ResultEnum.FAIL);
            }
        }else if (FollowOperationEnum.DELETE.equals(followOperation)){
            if(follow!=null || beFollow!=null){
                //移除好友
                if(relation1 != null){
                    relation1.setDeleted("1");
                    userSocialRelationMapper.updateById(relation1);
                }
                if (relation2!=null){
                    relation2.setDeleted("1");
                    userSocialRelationMapper.updateById(relation2);
                }
                if (beFollow!=null){
                    beFollow.setDeleted("1");
                    userSocialFollowMapper.updateById(beFollow);
                }
                if (follow!=null){
                    follow.setDeleted("1");
                    userSocialFollowMapper.updateById(follow);
                }
            }else{
                return ResultUtil.error(ResultEnum.FAIL);
            }
        }
        return ResultUtil.success();
    }

    @Override
    public Result deletePic(OperationDeletePicRequest request) {
        if(request.getPicIdList() !=null && request.getPicIdList().size()>0){
            QueryWrapper<UserSocialAttachEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted","0");
            wrapper.in("attachId",request.getPicIdList());
            userSocialAttachMapper.delete(wrapper);
        }
        return ResultUtil.success();
    }

    @Override
    public Result upPic(OperationUpPicRequest request) {
        List<VoUrl> picList = request.getPicList();
        if(picList!=null && picList.size()>0){
            QueryWrapper<UserSocialAttachEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted","0");
            wrapper.eq("userId",request.getUserId());
            int num = userSocialAttachMapper.selectCount(wrapper);
            if((num + picList.size())>9){
                return ResultUtil.error("U0999","照片不能超过9张");
            }
            for (VoUrl url : picList){
                if(StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                        && StringUtils.isNotEmpty(url.getPicAttachSize())){
                    //保存选中图片
                    Attach attach = attachService.saveAttach(request.getUserId(), url.getPicAttachName()
                            , url.getPicAttachNewName(), url.getPicAttachSize());
                    UserSocialAttachEntity entity = new UserSocialAttachEntity();
                    entity.setId(UUIDUtil.getShortUUID());
                    entity.setUserId(request.getUserId());
                    entity.setAttachId(attach.getId());
                    entity.setDeleted("0");
                    userSocialAttachMapper.insert(entity);
                }
            }
        }
        return ResultUtil.success();
    }
}
