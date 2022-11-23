package com.knd.front.wallpaper.service;

import com.knd.common.response.Result;
import com.knd.front.wallpaper.request.SaveUserWallPaperRequest;
import com.knd.front.wallpaper.request.SaveWallPaperRequest;
import com.knd.front.wallpaper.request.UpdateUserWallPaperRequest;
import com.knd.front.wallpaper.request.UpdateWallPaperRequest;


public interface IWallPaperService {
    Result add(SaveWallPaperRequest newsRequest);

    Result edit(UpdateWallPaperRequest newsRequest);

    Result delete(String ids);

    Result getWallPaper(String id);

    Result getWallPaperList(String type, Integer size, String current);

    Result saveUserWallPaper(SaveUserWallPaperRequest saveUserWallPaperRequest);

    Result updateUserWallPaper(UpdateUserWallPaperRequest updateUserWallPaperRequest);

    Result deleteUserWallPaper(String ids);

    Result getUserWallPaperList(String type, Integer size, String current);


}
