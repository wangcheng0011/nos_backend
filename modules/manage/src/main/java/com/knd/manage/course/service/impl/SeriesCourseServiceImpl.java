package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.entity.BaseBodyPart;
import com.knd.manage.basedata.entity.BaseTarget;
import com.knd.manage.basedata.mapper.BaseBodyPartMapper;
import com.knd.manage.basedata.mapper.BaseTargetMapper;
import com.knd.manage.basedata.vo.VoUrl;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.domain.SeriesDifficultyEnum;
import com.knd.manage.course.dto.CourseDto;
import com.knd.manage.course.dto.SeriesCourseDto;
import com.knd.manage.course.entity.*;
import com.knd.manage.course.mapper.*;
import com.knd.manage.course.service.SeriesCourseService;
import com.knd.manage.course.vo.VoSaveSeries;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SeriesCourseServiceImpl extends ServiceImpl<SeriesCourseHeadMapper, SeriesCourseHead> implements SeriesCourseService {

    private final BaseCourseTypeMapper baseCourseTypeMapper;
    private final CourseTypeMapper courseTypeMapper;
    private final BaseTargetMapper baseTargetMapper;
    private final CourseTargetMapper courseTargetMapper;
    private final BaseBodyPartMapper baseBodyPartMapper;
    private final CourseBodyPartMapper courseBodyPartMapper;
    private final CourseHeadMapper courseHeadMapper;
    private final SeriesCourseAttachMapper seriesCourseAttachMapper;
    private final SeriesCourseRelationMapper seriesCourseRelationMapper;
    private final AttachMapper attachMapper;
    private final IAttachService iAttachService;
    private final IGoodsService goodsService;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getList(String name, String userId, String current) {
        QueryWrapper<SeriesCourseHead> qw = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(name)){
            qw.like("name",name);
        }
        qw.eq("deleted","0");
        Page<SeriesCourseHead> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        page = baseMapper.selectPage(page,qw);
        List<SeriesCourseHead> list = page.getRecords();
        list.stream().forEach(ll ->{
            ll.setDifficulty(SeriesDifficultyEnum.values()[Integer.valueOf(ll.getDifficulty())].getDisplay());
        });
        ResponseDto dto = ResponseDto.<SeriesCourseHead>builder().total((int)page.getTotal()).resList(list).build();
        return ResultUtil.success(dto);
    }

    @Override
    public Result getDetail(String id) {
        SeriesCourseDto dto = new SeriesCourseDto();
        QueryWrapper<SeriesCourseHead> headQw = new QueryWrapper<>();
        headQw.eq("deleted","0");
        headQw.eq("id",id);
        SeriesCourseHead series = baseMapper.selectOne(headQw);

        List<CourseDto> courseList = new ArrayList<>();
        List<SeriesCourseRelation> seriesCourseRelations = seriesCourseRelationMapper.selectList(new QueryWrapper<SeriesCourseRelation>().eq("deleted", "0").eq("seriesId", series.getId()));
        for (SeriesCourseRelation relation : seriesCourseRelations){
            CourseHead courseHead = courseHeadMapper.selectOne(new QueryWrapper<CourseHead>().eq("deleted", "0").eq("releaseFlag","1").eq("id",relation.getCourseId()));
            if (StringUtils.isNotEmpty(courseHead)){
                CourseDto courseDto = new CourseDto();
                String courseId = courseHead.getId();
                courseDto.setId(courseHead.getId());
                courseDto.setCourse(courseHead.getCourse());

                List<CourseBodyPart> courseBodyParts = courseBodyPartMapper.selectList(new QueryWrapper<CourseBodyPart>().eq("deleted", 0).eq("courseId", courseId));
                List<String> partList = new ArrayList<>();
                for (CourseBodyPart  part : courseBodyParts){
                    BaseBodyPart baseBodyPart = baseBodyPartMapper.selectById(part.getPartId());
                    if (StringUtils.isNotEmpty(baseBodyPart)){
                        partList.add(baseBodyPart.getPart());
                    }
                }

                List<CourseTarget> courseTargets = courseTargetMapper.selectList(new QueryWrapper<CourseTarget>().eq("deleted", 0).eq("courseId", courseId));
                List<String> targetList= new ArrayList<>();
                for (CourseTarget target : courseTargets){
                    BaseTarget baseTarget = baseTargetMapper.selectById(target.getTargetId());
                    if (StringUtils.isNotEmpty(baseTarget)){
                        targetList.add(baseTarget.getTarget());
                    }
                }

                List<CourseType> courseTypes = courseTypeMapper.selectList(new QueryWrapper<CourseType>().eq("deleted", 0).eq("courseId", courseId));
                List<String> typeList= new ArrayList<>();
                for (CourseType type : courseTypes){
                    BaseCourseType baseCourseType = baseCourseTypeMapper.selectById(type.getCourseTypeId());
                    if (StringUtils.isNotEmpty(baseCourseType)){
                        typeList.add(baseCourseType.getType());
                    }
                }
                courseDto.setPartList(partList);
                courseDto.setTargetList(targetList);
                courseDto.setTypeList(typeList);
                courseList.add(courseDto);
            }
        }
        dto.setCourseList(courseList);

        List<ImgDto> imageUrl = new ArrayList<>();
        QueryWrapper<SeriesCourseAttach> attachQw = new QueryWrapper<>();
        attachQw.eq("deleted","0");
        attachQw.eq("seriesId",id);
        List<SeriesCourseAttach> attaches = seriesCourseAttachMapper.selectList(attachQw);
        for(SeriesCourseAttach attach : attaches){
            ImgDto img = getImgDto(attach.getAttachId());
            imageUrl.add(img);
        }
        imageUrl = imageUrl.stream().sorted(Comparator.comparing(ImgDto::getPicAttachName)).collect(Collectors.toList());
        BeanUtils.copyProperties(series,dto);
        dto.setPicAttach(getImgDto(series.getPicAttachId()));
        dto.setCourseList(courseList);
        dto.setImageUrl(imageUrl);
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result delete(VoId vo) {
        SeriesCourseHead head = new SeriesCourseHead();
        head.setId(vo.getId());
        head.setDeleted("1");
        head.setLastModifiedBy(vo.getUserId());
        head.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(head);
        return ResultUtil.success();
    }

    @Override
    public Result add(VoSaveSeries vo) {
        QueryWrapper<SeriesCourseHead> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        qw.eq("name",vo.getName());
        int count = baseMapper.selectCount(qw);
        if(count > 0){
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        //保存选中图片
        Attach picAttachUrl = goodsService.saveAttach(vo.getUserId(), vo.getPicAttachUrl().getPicAttachName()
                , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());

        SeriesCourseHead head = new SeriesCourseHead();
        BeanUtils.copyProperties(vo,head);
        head.setId(UUIDUtil.getShortUUID());
        head.setCreateBy(vo.getUserId());
        head.setCreateDate(LocalDateTime.now());
        head.setDeleted("0");
        head.setLastModifiedBy(vo.getUserId());
        head.setLastModifiedDate(LocalDateTime.now());
        head.setPicAttachId(picAttachUrl.getId());
        baseMapper.insert(head);

        save(vo.getUserId(),vo,head.getId());
        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveSeries vo) {
        QueryWrapper<SeriesCourseHead> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        qw.eq("id",vo.getSeriesId());
        SeriesCourseHead seriesCourseHead = baseMapper.selectOne(qw);
        if (seriesCourseHead == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if(!seriesCourseHead.getName().equals(vo.getName())){
            //查重
            qw.clear();
            qw.eq("deleted","0");
            qw.eq("name",vo.getName());
            int count = baseMapper.selectCount(qw);
            if(count > 0){
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        QueryWrapper<SeriesCourseAttach> attachQueryWrapper = new QueryWrapper<>();
        attachQueryWrapper.eq("deleted","0");
        attachQueryWrapper.eq("seriesId",vo.getSeriesId());
        List<SeriesCourseAttach> seriesCourseAttaches = seriesCourseAttachMapper.selectList(attachQueryWrapper);

        //搜集关联附件id,更新前清理
        List<String> attachIds = new ArrayList<>();
        attachIds.add(seriesCourseHead.getPicAttachId());
        for(SeriesCourseAttach attach : seriesCourseAttaches){
            attachIds.add(attach.getAttachId());
        }
        attachMapper.deleteBatchIds(attachIds);
        seriesCourseAttachMapper.delete(attachQueryWrapper);

        QueryWrapper<SeriesCourseRelation> relationQueryWrapper = new QueryWrapper<SeriesCourseRelation>()
                .eq("deleted","0")
                .eq("seriesId",vo.getSeriesId());
        seriesCourseRelationMapper.delete(relationQueryWrapper);

        //保存选中图片
        Attach picAttachUrl = goodsService.saveAttach(vo.getUserId(), vo.getPicAttachUrl().getPicAttachName()
                , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());

        SeriesCourseHead head = new SeriesCourseHead();
        BeanUtils.copyProperties(vo,head);
        head.setId(vo.getSeriesId());
        head.setPicAttachId(picAttachUrl.getId());
        head.setLastModifiedBy(vo.getUserId());
        head.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(head);

        save(vo.getUserId(),vo,vo.getSeriesId());
        return ResultUtil.success();
    }

    @Override
    public SeriesCourseHead insertReturnEntity(SeriesCourseHead entity) {
        return null;
    }

    @Override
    public SeriesCourseHead updateReturnEntity(SeriesCourseHead entity) {
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

    public void save(String userId, VoSaveSeries vo,String seriesId){
        for(VoUrl url : vo.getAttachUrl()){
            Attach attach = goodsService.saveAttach(userId, url.getPicAttachName()
                    , url.getPicAttachNewName(), url.getPicAttachSize());
            SeriesCourseAttach courseAttach = new SeriesCourseAttach();
            courseAttach.setId(UUIDUtil.getShortUUID());
            courseAttach.setSeriesId(seriesId);
            courseAttach.setAttachId(attach.getId());
            courseAttach.setCreateBy(userId);
            courseAttach.setCreateDate(LocalDateTime.now());
            courseAttach.setDeleted("0");
            courseAttach.setLastModifiedBy(userId);
            courseAttach.setLastModifiedDate(LocalDateTime.now());
            seriesCourseAttachMapper.insert(courseAttach);
        }

        for(String courseId : vo.getCourseIdList()){
            SeriesCourseRelation courseRelation = new SeriesCourseRelation();
            courseRelation.setId(UUIDUtil.getShortUUID());
            courseRelation.setSeriesId(seriesId);
            courseRelation.setCourseId(courseId);
            courseRelation.setCreateBy(userId);
            courseRelation.setCreateDate(LocalDateTime.now());
            courseRelation.setDeleted("0");
            courseRelation.setLastModifiedBy(userId);
            courseRelation.setLastModifiedDate(LocalDateTime.now());
            seriesCourseRelationMapper.insert(courseRelation);
        }
    }

}
