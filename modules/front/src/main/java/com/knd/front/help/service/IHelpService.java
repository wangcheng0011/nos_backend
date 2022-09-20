package com.knd.front.help.service;

import com.knd.common.response.Result;
import com.knd.front.help.request.HelpRequest;


public interface IHelpService {
    Result add(HelpRequest newsRequest);

    Result edit(HelpRequest newsRequest);

    Result delete(HelpRequest newsRequest);

    Result getHelp(String id);

    Result getHelpList(String title,Integer size, String current);

}
