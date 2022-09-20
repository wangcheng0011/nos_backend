package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoSaveMusic;

public interface IBaseMusicService {

    //获取页面列表
    Result getMusicList(String name,String current,String size);

    //删除页面
    Result deleteMusic(String userId, String id);

    //新增页面
    Result addMusic(VoSaveMusic vo);
}
