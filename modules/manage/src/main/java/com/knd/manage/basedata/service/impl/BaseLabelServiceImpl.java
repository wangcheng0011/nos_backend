package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.LabelTypeEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.dto.LabelDto;
import com.knd.manage.basedata.dto.UserModelDto;
import com.knd.manage.basedata.entity.BaseLabel;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.entity.UserLabel;
import com.knd.manage.basedata.mapper.BaseLabelMapper;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.basedata.mapper.UserLabelMapper;
import com.knd.manage.basedata.service.IBaseLabelService;
import com.knd.manage.basedata.vo.VoGetLabelList;
import com.knd.manage.basedata.vo.VoSaveLabel;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.entity.UserCoachCourseOrderEntity;
import com.knd.manage.course.mapper.UserCoachCourseOrderMapper;
import com.knd.manage.mall.service.IGoodsService;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class BaseLabelServiceImpl implements IBaseLabelService {
    private final BaseLabelMapper baseLabelMapper;
    private final UserLabelMapper userLabelMapper;
    private final UserMapper userMapper;
    private final UserCoachMapper userCoachMapper;
    private final UserCoachCourseOrderMapper userCoachCourseOrderMapper;
    private final IGoodsService goodsService;
    private final IAttachService iAttachService;
    private final AttachMapper attachMapper;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getLabelList(VoGetLabelList vo) {
        Page<BaseLabel> page = new Page<>(Long.parseLong(vo.getCurrent()), PageInfo.pageSize);
        QueryWrapper<BaseLabel> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        if(StringUtils.isNotEmpty(vo.getType())){
            wrapper.eq("type",vo.getType());
        }
        if(StringUtils.isNotEmpty(vo.getLabel())){
            wrapper.eq("label",vo.getLabel());
        }
        Page<BaseLabel> baseLabelPage = baseLabelMapper.selectPage(page, wrapper);
        List<BaseLabel> labelList = baseLabelPage.getRecords();
        for (BaseLabel b : labelList){
            b.setType(LabelTypeEnum.values()[Integer.valueOf(b.getType())].getDisplay());
        }
        ResponseDto dto = ResponseDto.<BaseLabel>builder().total((int) baseLabelPage.getTotal()).resList(labelList).build();

        return ResultUtil.success(dto);
    }

    @Override
    public Result getLabel(String id) {
        BaseLabel baseLabel = baseLabelMapper.selectById(id);
        QueryWrapper<UserLabel> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("labelId",id);

        List<UserModelDto> userList = new ArrayList<>();
        List<UserLabel> userLabels = userLabelMapper.selectList(wrapper);
        for (UserLabel userLabel : userLabels){
            User user = userMapper.selectById(userLabel.getUserId());
            if(StringUtils.isNotEmpty(user)){
                UserModelDto userModelDto = new UserModelDto();
                userModelDto.setMobile(user.getMobile());
                userModelDto.setNickName(user.getNickName());
                userModelDto.setUserId(user.getId());
                userList.add(userModelDto);
            }

        }

        ImgDto imgUrlDto = getImgDto(baseLabel.getImageUrlId());

        LabelDto dto = new LabelDto();
        BeanUtils.copyProperties(baseLabel,dto);
        dto.setUserList(userList);
        dto.setImageUrl(imgUrlDto);
        return ResultUtil.success(dto);
    }

    @Override
    @Transactional
    public Result add(String userId, VoSaveLabel vo) {
        log.info("addLabel userId:{{}}",userId);
        log.info("addLabel vo:{{}}",vo);
        QueryWrapper<BaseLabel> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("label",vo.getLabel());
        wrapper.eq("type",vo.getType());
        //获取总数
        int s = baseLabelMapper.selectCount(wrapper);
        log.info("addLabel s:{{}}",s);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        String imageUrlId = "";
        if(StringUtils.isNotEmpty(vo.getImageUrl())
                && StringUtils.isNotEmpty(vo.getImageUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = goodsService.saveAttach(userId, vo.getImageUrl().getPicAttachName()
                    , vo.getImageUrl().getPicAttachNewName(), vo.getImageUrl().getPicAttachSize());
            imageUrlId = imgAPi.getId();
        }

        BaseLabel baseLabel = new BaseLabel();
        BeanUtils.copyProperties(vo,baseLabel);
        baseLabel.setId(UUIDUtil.getShortUUID());
        baseLabel.setImageUrlId(imageUrlId);
        baseLabel.setCreateBy(userId);
        baseLabel.setCreateDate(LocalDateTime.now());
        baseLabel.setDeleted("0");
        baseLabel.setLastModifiedBy(userId);
        baseLabel.setLastModifiedDate(LocalDateTime.now());
        log.info("addLabel baseLabelId:{{}}",baseLabel.getId());
        log.info("addLabel baseLabel:{{}}",baseLabel);
        baseLabelMapper.insert(baseLabel);
        if(StringUtils.isNotEmpty(vo.getUserModelList())){
            vo.getUserModelList().stream().distinct().forEach(i->{
                QueryWrapper<UserLabel> userLabelQueryWrapper = new QueryWrapper<UserLabel>().eq("userId", i.getUserId()).eq("labelId",baseLabel.getId()).eq("deleted", "0");
                int userLabelNum = userLabelMapper.selectCount(userLabelQueryWrapper);
                if(userLabelNum == 0){
                    UserLabel userLabel = new UserLabel();
                    userLabel.setId(UUIDUtil.getShortUUID());
                    userLabel.setLabelId(baseLabel.getId());
                    userLabel.setUserId(i.getUserId());
                    userLabel.setCreateBy(userId);
                    userLabel.setCreateDate(LocalDateTime.now());
                    userLabel.setDeleted("0");
                    userLabel.setLastModifiedBy(userId);
                    userLabel.setLastModifiedDate(LocalDateTime.now());
                    log.info("addLabel labelId:{{}}",baseLabel.getId());
                    log.info("addLabel userLabel:{{}}",userLabel);
                    userLabelMapper.insert(userLabel);
                }
                List<String> typeList = new ArrayList<>();
                typeList.add(LabelTypeEnum.DANCE.ordinal()+"");
                typeList.add(LabelTypeEnum.BODYBUILDING.ordinal()+"");
                typeList.add(LabelTypeEnum.POWER.ordinal()+"");
                typeList.add(LabelTypeEnum.PILATES.ordinal()+"");
                typeList.add(LabelTypeEnum.YOGA.ordinal()+"");
                typeList.add(LabelTypeEnum.STRENGTH.ordinal()+"");
                log.info("addLabel typeList:{{}}",typeList);
                log.info("addLabel type:{{}}",vo.getType());
                int num = userCoachMapper.selectCount(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", i.getUserId()));
                log.info("addLabel num:{{}}",num);
                if (typeList.contains(vo.getType()) && num == 0){
                        UserCoachEntity coachEntity = new UserCoachEntity();
                        coachEntity.setId(UUIDUtil.getShortUUID());
                        coachEntity.setUserId(i.getUserId());
                        coachEntity.setCreateBy(userId);
                        coachEntity.setCreateDate(LocalDateTime.now());
                        coachEntity.setDeleted("0");
                        coachEntity.setLastModifiedBy(userId);
                        coachEntity.setLastModifiedDate(LocalDateTime.now());
                        log.info("addLabel coachEntity:{{}}",coachEntity);
                        userCoachMapper.insert(coachEntity);
                }
            });
        }
        return ResultUtil.success();
    }

    @Override
    public Result edit(String userId, VoSaveLabel vo) {
        log.info("editLabel userId:{{}}",userId);
        log.info("editLabel voSaveLabel:{{}}",vo);
        //根据id获取名称
        QueryWrapper<BaseLabel> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getLabelId());
        wrapper.eq("deleted","0");
        BaseLabel ho = baseLabelMapper.selectOne(wrapper);
        log.info("editLabel baseLabel:{{}}",ho);
        if (ho == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ho.getLabel().equals(vo.getLabel())) {
            //查重
            wrapper.clear();
            wrapper.eq("label",vo.getLabel());
            wrapper.eq("type",vo.getType());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = baseLabelMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        if(StringUtils.isNotEmpty(ho.getImageUrlId())){
            attachMapper.deleteById(ho.getImageUrlId());
        }
        String imageUrlId = "";
        if(StringUtils.isNotEmpty(vo.getImageUrl())
                && StringUtils.isNotEmpty(vo.getImageUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = goodsService.saveAttach(userId, vo.getImageUrl().getPicAttachName()
                    , vo.getImageUrl().getPicAttachNewName(), vo.getImageUrl().getPicAttachSize());
            imageUrlId = imgAPi.getId();
        }
        ho.setType(vo.getType());
        ho.setLabel(vo.getLabel());
        ho.setRemark(vo.getRemark());
        ho.setImageUrlId(imageUrlId);
        ho.setLastModifiedBy(userId);
        ho.setLastModifiedDate(LocalDateTime.now());
        log.info("editLabel baseLabel:{{}}",ho);
        baseLabelMapper.updateById(ho);

        List<String> newUserIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(vo.getUserModelList())) {
            for (UserModelDto u : vo.getUserModelList()) {
                newUserIdList.add(u.getUserId());
            }
        }

        List<String> typeList = new ArrayList<>();
        typeList.add(LabelTypeEnum.DANCE.ordinal()+"");
        typeList.add(LabelTypeEnum.BODYBUILDING.ordinal()+"");
        typeList.add(LabelTypeEnum.POWER.ordinal()+"");
        typeList.add(LabelTypeEnum.PILATES.ordinal()+"");
        typeList.add(LabelTypeEnum.YOGA.ordinal()+"");
        typeList.add(LabelTypeEnum.STRENGTH.ordinal()+"");

        List<BaseLabel> labelList = baseLabelMapper.selectList(new QueryWrapper<BaseLabel>().eq("deleted", "0").in("type", typeList));
        List<String> labelIdList = new ArrayList<>();
        for (BaseLabel baseLabel : labelList){
            labelIdList.add(baseLabel.getId());
        }
        labelIdList.remove(vo.getLabelId());
        log.info("editLabel labelIdList:{{}}",labelIdList);

        //查询该标签下所有的人
        List<UserLabel> userLabels = userLabelMapper.selectList(new QueryWrapper<UserLabel>().eq("deleted", "0").in("labelId", vo.getLabelId()));
        for(int i=0;i<userLabels.size();i++){
            String uId = userLabels.get(i).getUserId();
            //校验老用户与新用户是否重叠，重叠则两边都消除，不做处理
            if (newUserIdList.contains(uId)){
                newUserIdList.remove(uId);
                userLabels.remove(userLabels.get(i));
                i--;
            }
        }

        //老用户中得数据执行删除操作，
        for (UserLabel userLabel : userLabels){
            String uId = userLabel.getUserId();
            //判断该教练是否有课程被预约，被预约之后不能移除标签
            int orderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>().eq("deleted", "0").eq("coachUserId", uId));
            //TODO
           /* if (orderNum>0){
                return ResultUtil.error("U0999","该教练存在发布的课程被预约,无法移除标签");
            }*/
            //判断每个人是否有其他得教练标签
            int num = userLabelMapper.selectCount(new QueryWrapper<UserLabel>().eq("deleted", "0").in("labelId", labelIdList).eq("userId", uId));
            //若该用户无其他教练标签，则删除教练表数据，
            if (num==0){
                userCoachMapper.delete(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId",uId));
            }
            userLabelMapper.delete(new QueryWrapper<UserLabel>().eq("deleted", "0").eq("labelId", vo.getLabelId()).eq("userId", uId));
        }

        //查询新加的用户
        newUserIdList.stream().distinct().forEach(uId->{
            log.info("-------------------------查询新加的用户--------------------------");
            log.info("editLabel labelId:{{}}",vo.getLabelId());
            log.info("editLabel uId:{{}}",uId);
            QueryWrapper<UserLabel> userLabelQueryWrapper = new QueryWrapper<UserLabel>().eq("userId", uId).eq("labelId",vo.getLabelId()).eq("deleted", "0");
            int userLabelNum = userLabelMapper.selectCount(userLabelQueryWrapper);
            log.info("editLabel userLabelNum:{{}}",userLabelNum);
            if(userLabelNum == 0) {
                UserLabel userLabel = new UserLabel();
                userLabel.setId(UUIDUtil.getShortUUID());
                userLabel.setLabelId(vo.getLabelId());
                userLabel.setUserId(uId);
                userLabel.setCreateBy(userId);
                userLabel.setCreateDate(LocalDateTime.now());
                userLabel.setDeleted("0");
                userLabel.setLastModifiedBy(userId);
                userLabel.setLastModifiedDate(LocalDateTime.now());
                log.info("editLabel userLabel:{{}}",userLabel);
                userLabelMapper.insert(userLabel);
            }

            //该标签是教练标签
            if (typeList.contains(vo.getType())){
                log.info("------------------------该标签是教练标签----------------------");
                //判断该用户是否有其他教练标签
                int num = userLabelMapper.selectCount(new QueryWrapper<UserLabel>().eq("deleted", "0").in("labelId", labelIdList).eq("userId", uId));
                log.info("editLabel num:{{}}",num);
                if (num == 0){
                    log.info("------------------------该用户没有其他教练标签----------------------");
                    QueryWrapper<UserCoachEntity> coachEntityQueryWrapper = new QueryWrapper<>();
                    coachEntityQueryWrapper.eq("userId",uId);
                    coachEntityQueryWrapper.eq("deleted",0);
                    List<UserCoachEntity> userCoachEntities = userCoachMapper.selectList(coachEntityQueryWrapper);
                    log.info("editLabel userCoachEntities:{{}}",userCoachEntities);
                    if(StringUtils.isEmpty(userCoachEntities)){
                        log.info("------------------------教练表里面没有这个用户 新增教练----------------------");
                        UserCoachEntity coachEntity = new UserCoachEntity();
                        coachEntity.setId(UUIDUtil.getShortUUID());
                        coachEntity.setUserId(uId);
                        coachEntity.setCreateBy(userId);
                        coachEntity.setCreateDate(LocalDateTime.now());
                        coachEntity.setDeleted("0");
                        coachEntity.setLastModifiedBy(userId);
                        coachEntity.setLastModifiedDate(LocalDateTime.now());
                        log.info("editLabel coachEntity:{{}}",coachEntity);
                        userCoachMapper.insert(coachEntity);
                    }
                }
            }
        });
        return ResultUtil.success();
    }

    @Override
    public Result delete(String userId, String id) {
        return null;
    }

    public ImgDto getImgDto(String urlId){
        //根据id获取图片信息
        Attach aPi = iAttachService.getInfoById(urlId);
        ImgDto imgDto = new ImgDto();
        if (aPi != null) {
            imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
            imgDto.setPicAttachSize(aPi.getFileSize());
            String[] strs = (aPi.getFilePath()).split("\\?");
            imgDto.setPicAttachNewName(imageFoldername + strs[0]);
            imgDto.setPicAttachName(aPi.getFileName());
        }
        return imgDto;
    }
}
