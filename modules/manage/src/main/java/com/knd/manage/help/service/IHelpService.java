package com.knd.manage.help.service;

import com.knd.common.response.Result;
import com.knd.manage.help.request.HelpRequest;


public interface IHelpService {
    Result add(HelpRequest helpRequest);

    Result edit(HelpRequest helpRequest);

    Result delete(HelpRequest helpRequest);

    Result getHelp(String id);

    Result getHelpList(String title,Integer size, String current);

}
