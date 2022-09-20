package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.entity.SeriesCourseHead;
import com.knd.manage.course.vo.VoSaveSeries;
import com.knd.mybatis.SuperService;

/**
 * @author Lenovo
 */
public interface SeriesCourseService extends SuperService<SeriesCourseHead> {

    Result getList(String name,String userId,String current);

    Result getDetail(String id);

    Result delete(VoId vo);

    Result add(VoSaveSeries vo);

    Result edit(VoSaveSeries vo);

}
