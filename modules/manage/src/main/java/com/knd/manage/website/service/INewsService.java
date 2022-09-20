package com.knd.manage.website.service;

import com.knd.common.response.Result;
import com.knd.manage.website.request.ClassifyRequest;
import com.knd.manage.website.request.NewsRequest;

public interface INewsService {
    Result add(NewsRequest newsRequest);

    Result edit(NewsRequest newsRequest);

    Result delete(NewsRequest newsRequest);

    Result getNews(String id);

    Result getNewsList(String classify,String recommend,Integer size,String current);

    Result addClassify(ClassifyRequest classifyRequest);

    Result editClassify(ClassifyRequest classifyRequest);

    Result deleteClassify(ClassifyRequest classifyRequest);

    Result getClassifyList();
}
