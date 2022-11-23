package com.knd.manage.wallpaper.service;

import com.knd.common.response.Result;
import com.knd.manage.wallpaper.request.WallPaperRequest;


public interface IWallPaperService {
    Result add(WallPaperRequest newsRequest);

    Result edit(WallPaperRequest newsRequest);

    Result delete(String id);

    Result getWallPaper(String id);

    Result getWallPaperList(String name,String type,Integer size, String current);

}
