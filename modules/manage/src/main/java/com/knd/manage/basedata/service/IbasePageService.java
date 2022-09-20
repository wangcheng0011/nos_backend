package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BasePage;
import com.knd.manage.basedata.vo.VoGetPageList;
import com.knd.manage.basedata.vo.VoSavePage;
import com.knd.mybatis.SuperService;

public interface IbasePageService extends SuperService<BasePage> {

    //获取页面列表
    Result getPageList(VoGetPageList vo);

    //获取页面详情
    Result getPage(String id);

    //删除页面
    Result deletePage(String userId, String id);

    //新增页面
    Result addPage(VoSavePage vo);

    //更新页面
    Result editPage(VoSavePage vo);


}
