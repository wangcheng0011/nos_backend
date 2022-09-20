package com.knd.front.common.service;

import com.knd.front.entity.Attach;

public interface AttachService {
    Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize);

    Attach getInfoById(String urlId);
}
