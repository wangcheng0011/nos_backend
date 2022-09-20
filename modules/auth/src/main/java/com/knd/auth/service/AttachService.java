package com.knd.auth.service;


import com.knd.auth.entity.Attach;

public interface AttachService {
    Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize);
}
