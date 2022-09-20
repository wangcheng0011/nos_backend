package com.knd.manage.website.service.Impl;

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
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.mall.service.IGoodsService;
import com.knd.manage.website.dto.NewsDto;
import com.knd.manage.website.entity.ClassifyEntity;
import com.knd.manage.website.entity.NewsEntity;
import com.knd.manage.website.mapper.ClassifyMapper;
import com.knd.manage.website.mapper.NewsMapper;
import com.knd.manage.website.request.ClassifyRequest;
import com.knd.manage.website.request.NewsRequest;
import com.knd.manage.website.service.INewsService;
import com.knd.redis.jedis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class INewsServiceImp extends ServiceImpl<NewsMapper, NewsEntity> implements INewsService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private ClassifyMapper classifyMapper;

    @Resource
    private AttachMapper attachMapper;
    
    @Resource
    private IAttachService iAttachService;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Resource
    private RedisClient redisClient;

    @Override
    public Result add(NewsRequest newsRequest) {
        log.info("add newsRequest:{{}}",newsRequest);
        NewsEntity newsEntity = new NewsEntity();
        BeanUtils.copyProperties(newsRequest,newsEntity);
        newsEntity.setId(UUIDUtil.getShortUUID());
        ImgDto imgUrl = newsRequest.getPicAttach();
        if(imgUrl != null && imgUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = goodsService.saveAttach(newsRequest.getUserId(), imgUrl.getPicAttachName(), imgUrl.getPicAttachNewName(), imgUrl.getPicAttachSize());
            newsEntity.setAttachId(imgAttach.getId());
        }
        newsEntity.setPublishTime(LocalDateTime.now());
        newsEntity.setCreateBy(newsRequest.getUserId());
        newsEntity.setCreateDate(LocalDateTime.now());
        newsEntity.setDeleted("0");
        baseMapper.insert(newsEntity);
        return ResultUtil.success();
    }

    @Override
    public Result edit(NewsRequest newsRequest) {
        NewsEntity newsEntity = new NewsEntity();
        BeanUtils.copyProperties(newsRequest,newsEntity);
        //搜集关联附件id,更新前清理
        List<String> attachIds = new ArrayList<>();
        attachIds.add(newsEntity.getAttachId());
        attachMapper.deleteBatchIds(attachIds);
        ImgDto imgUrl = newsRequest.getPicAttach();
        if(imgUrl != null && imgUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = goodsService.saveAttach(newsRequest.getUserId(), imgUrl.getPicAttachName(), imgUrl.getPicAttachNewName(), imgUrl.getPicAttachSize());
            newsEntity.setAttachId(imgAttach.getId());
        }
        newsEntity.setPublishTime(LocalDateTime.now());
        newsEntity.setLastModifiedBy(newsRequest.getUserId());
        newsEntity.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(newsEntity);
        return ResultUtil.success(newsEntity);
    }


    @Override
    public Result delete(NewsRequest newsRequest) {
        log.info("delete id:{{}}",newsRequest.getId());
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(newsRequest.getId());
        newsEntity.setDeleted("1");
        baseMapper.updateById(newsEntity);
        //成功
        return ResultUtil.success();
    }


    //获取新闻
    @Override
    public Result getNews(String id) {
        QueryWrapper<NewsEntity> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        NewsEntity newsEntity = baseMapper.selectOne(qw);
        NewsDto newsDto = new NewsDto();
        BeanUtils.copyProperties(newsEntity,newsDto);
        //成功
        ImgDto imgDto = getImgDto(newsEntity.getAttachId());
        newsDto.setPicAttach(imgDto);
        Integer count =1;
        if (StringUtils.isNotEmpty(newsEntity.getReadCount())){
            count = newsEntity.getReadCount();
            count ++;
        }
        newsDto.setReadCount(String.valueOf(count));
        newsEntity.setReadCount(count);
        baseMapper.updateById(newsEntity);
        return ResultUtil.success(newsDto);
    }

    //获取新闻列表
    @Override
    public Result getNewsList(String classify,String recommend,Integer size, String current) {
        QueryWrapper<NewsEntity> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(classify)) {
            qw.like("classify", classify);
        }
        if (StringUtils.isNotEmpty(recommend)) {
            qw.eq("recommend", recommend);
        }
        qw.eq("deleted", "0");
        qw.orderByDesc("createDate");
        List<NewsEntity> newsEntityList =null;
        if (StringUtils.isEmpty(current)) {
            //获取全部
            newsEntityList = baseMapper.selectList(qw);
        } else {
            if (StringUtils.isEmpty(size)){
                size= PageInfo.pageSize;
            }
            //分页
            Page<NewsEntity> partPage = new Page<>(Long.parseLong(current), size);
            partPage = baseMapper.selectPage(partPage, qw);
            newsEntityList = partPage.getRecords();
        }
        List<NewsDto> newsDtoList = new ArrayList<NewsDto>();
        newsEntityList.stream().forEach(i->{
            NewsDto newsDto = new NewsDto();
            BeanUtils.copyProperties(i,newsDto);
            if(StringUtils.isNotEmpty(i.getAttachId())){
                ImgDto imgDto = getImgDto(i.getAttachId());
                newsDto.setPicAttach(imgDto);
            }
            QueryWrapper<ClassifyEntity> eq = new QueryWrapper<ClassifyEntity>().eq("id", i.getClassify()).eq("deleted", "0");
            ClassifyEntity classifyEntity = classifyMapper.selectOne(eq);
            newsDto.setClassifyName(classifyEntity.getClassify());
            newsDtoList.add(newsDto);
        });
        //成功
        return ResultUtil.success(newsDtoList);
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


    @Override
    public Result addClassify(ClassifyRequest classifyRequest) {
        //查重
        QueryWrapper<ClassifyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("classify",classifyRequest.getClassify());
        wrapper.eq("deleted", "0");
        //获取总数
        int s = classifyMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        ClassifyEntity classifyEntity = new ClassifyEntity();
        BeanUtils.copyProperties(classifyRequest,classifyEntity);
        classifyEntity.setId(UUIDUtil.getShortUUID());
        classifyEntity.setCreateBy(classifyRequest.getUserId());
        classifyEntity.setCreateDate(LocalDateTime.now());
        classifyEntity.setDeleted("0");
        classifyMapper.insert(classifyEntity);
        return ResultUtil.success();
    }

    @Override
    public Result editClassify(ClassifyRequest classifyRequest) {
        ClassifyEntity classifyEntity = new ClassifyEntity();
        BeanUtils.copyProperties(classifyRequest,classifyEntity);
        classifyEntity.setLastModifiedBy(classifyRequest.getUserId());
        classifyEntity.setLastModifiedDate(LocalDateTime.now());
        classifyMapper.updateById(classifyEntity);
        return ResultUtil.success(classifyEntity);
    }

    @Override
    public Result deleteClassify(ClassifyRequest classifyRequest) {
        ClassifyEntity classifyEntity = new ClassifyEntity();
        classifyEntity.setId(classifyRequest.getId());
        classifyEntity.setDeleted("1");
        classifyEntity.setLastModifiedBy(classifyRequest.getUserId());
        classifyEntity.setLastModifiedDate(LocalDateTime.now());
        classifyMapper.updateById(classifyEntity);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getClassifyList() {
        QueryWrapper<ClassifyEntity> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        qw.orderByDesc("createDate");
        List<ClassifyEntity> classifyEntity = classifyMapper.selectList(qw);
        return ResultUtil.success(classifyEntity);


    }


}
