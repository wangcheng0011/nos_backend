package com.knd.front.common.service;

import com.knd.common.response.Result;

public interface SearchService {
    Result getCourseList(String current, String userId, String name);
    Result getActionList(String current,String userId,String name);
    Result getUserList(String userId,String userName,String current);
}
