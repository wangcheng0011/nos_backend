package com.knd.front.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.Attach;
import com.knd.front.entity.User;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.social.dto.MessageListDto;
import com.knd.front.social.entity.UserSocialMomentCommentEntity;
import com.knd.front.social.entity.UserSocialMomentEntity;
import com.knd.front.social.mapper.UserSocialMomentCommentMapper;
import com.knd.front.social.mapper.UserSocialMomentMapper;
import com.knd.front.social.service.UserMessageService;
import com.knd.front.train.mapper.AttachMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMessageServiceImpl implements UserMessageService {
    private final UserSocialMomentMapper momentMapper;
    private final UserSocialMomentCommentMapper commentMapper;
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final AttachMapper attachMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result getAllMessage(String current, String userId) {
        //分页
        Page<MessageListDto> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        List<MessageListDto> messageList = momentMapper.getAllMessageList(partPage, userId);
        for(MessageListDto dto : messageList){
            User user = userMapper.selectById(dto.getUserId());
            dto.setUserName(user.getNickName());
            dto.setUserHeadPicUrl(getHeadPicUrl(dto.getUserId()));
        }
        partPage.setRecords(messageList);
        return ResultUtil.success(partPage);
    }

    @Override
    public Result getComment(String current,String userId) {
        //分页
        Page<MessageListDto> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        List<MessageListDto> commentMessageList = momentMapper.getCommentMessageList(partPage, userId);
        for(MessageListDto dto : commentMessageList){
            User user = userMapper.selectById(dto.getUserId());
            dto.setUserName(user.getNickName());
            dto.setUserHeadPicUrl(getHeadPicUrl(dto.getUserId()));
        }
        partPage.setRecords(commentMessageList);
        return ResultUtil.success(partPage);
    }

    @Override
    public Result getBeCall(String current,String userId) {
        List<MessageListDto> dtoList = new ArrayList<>();
        QueryWrapper<UserSocialMomentCommentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("callUserId",userId);
        wrapper.eq("deleted","0");
        wrapper.eq("type","1");
        //分页
        Page<UserSocialMomentCommentEntity> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<UserSocialMomentCommentEntity> userSocialMomentCommentEntityPage = commentMapper.selectPage(partPage, wrapper);
        List<UserSocialMomentCommentEntity> userSocialMomentCommentList = userSocialMomentCommentEntityPage.getRecords();
        for(UserSocialMomentCommentEntity entity : userSocialMomentCommentList){
            MessageListDto dto = new MessageListDto();
            dto.setType("2");
            dto.setMomentId(entity.getMomentId());
            dto.setUserId(entity.getUserId());

            User user = userMapper.selectById(entity.getUserId());
            dto.setUserName(user.getNickName());
            dto.setUserHeadPicUrl(getHeadPicUrl(entity.getUserId()));

            UserSocialMomentEntity momentEntity = momentMapper.selectById(entity.getMomentId());
            dto.setTitle(momentEntity.getTitle());
            dto.setContent(entity.getContent());
            dto.setTime(entity.getPublishTime());
            dtoList.add(dto);
        }
        Page<MessageListDto> dto = new Page<>();
        dto.setTotal(partPage.getTotal());
        dto.setCurrent(partPage.getCurrent());
        dto.setSize(partPage.getSize());
        dto.setRecords(dtoList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getBePraised(String current,String userId) {
        //分页
        Page<MessageListDto> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        List<MessageListDto> praiseMessageList = momentMapper.getPraiseMessageList(partPage, userId);
        for(MessageListDto dto : praiseMessageList){
            User user = userMapper.selectById(dto.getUserId());
            dto.setUserName(user.getNickName());
            dto.setUserHeadPicUrl(getHeadPicUrl(dto.getUserId()));
        }
        partPage.setRecords(praiseMessageList);
        return ResultUtil.success(partPage);
    }

    private String getHeadPicUrl(String userId){
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if(userDetail!=null){
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            return attach!=null ? fileImagesPath+attach.getFilePath() : "";
        }else{
            return "";
        }
    }
}
