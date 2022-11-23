package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.MusicListDto;
import com.knd.manage.basedata.entity.BaseMusic;
import com.knd.manage.basedata.mapper.BaseMusicMapper;
import com.knd.manage.basedata.service.IBaseMusicService;
import com.knd.manage.basedata.vo.VoSaveMusic;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
public class IBaseMusicServiceImpl implements IBaseMusicService {

    private final BaseMusicMapper baseMusicMapper;
    private final IGoodsService goodsService;
    private final IAttachService iAttachService;


    @Override
    public Result getMusicList(String name,String current,String size) {
        //分页
        Page<BaseMusic> page = new Page<>(Integer.parseInt(current), Integer.parseInt(size));
        QueryWrapper<BaseMusic> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        if (StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        Page<BaseMusic> baseMusicPage = baseMusicMapper.selectPage(page, wrapper);
        List<BaseMusic> records = baseMusicPage.getRecords();
        List<MusicListDto> dtoList = new ArrayList<>();
        records.stream().forEach(record->{
            MusicListDto dto = new MusicListDto();
            BeanUtils.copyProperties(record,dto);
            dto.setMusicUrl(iAttachService.getImgDto(record.getMusicUrlId()));
            dtoList.add(dto);
        });

        ResponseDto dto = ResponseDto.<MusicListDto>builder().total((int)page.getTotal()).resList(dtoList).build();
        return ResultUtil.success(dto);
    }

    @Override
    public Result deleteMusic(String userId, String id) {
        BaseMusic baseMusic = new BaseMusic();
        baseMusic.setId(id);
        baseMusic.setDeleted("1");
        baseMusic.setLastModifiedBy(userId);
        baseMusic.setLastModifiedDate(LocalDateTime.now());
        baseMusicMapper.updateById(baseMusic);
        return ResultUtil.success();
    }

    @Override
    public Result addMusic(VoSaveMusic vo) {
        QueryWrapper<BaseMusic> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("name",vo.getName());
        wrapper.eq("type",vo.getType());
        //获取总数
        int s = baseMusicMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        String musicUrlId = "";
        if(StringUtils.isNotEmpty(vo.getMusicUrl())
                && StringUtils.isNotEmpty(vo.getMusicUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = iAttachService.saveAttach(vo.getUserId(), vo.getMusicUrl().getPicAttachName()
                    , vo.getMusicUrl().getPicAttachNewName(), vo.getMusicUrl().getPicAttachSize());
            musicUrlId = imgAPi.getId();
        }
        BaseMusic music =  new BaseMusic();
        BeanUtils.copyProperties(vo,music);
        music.setId(UUIDUtil.getShortUUID());
        music.setMusicUrlId(musicUrlId);
        music.setCreateBy(vo.getUserId());
        music.setCreateDate(LocalDateTime.now());
        music.setDeleted("0");
        music.setLastModifiedBy(vo.getUserId());
        music.setLastModifiedDate(LocalDateTime.now());
        baseMusicMapper.insert(music);
        return ResultUtil.success();
    }


}
