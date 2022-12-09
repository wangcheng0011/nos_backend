package com.knd.front.common.service;

import com.knd.front.entity.Attach;
import com.knd.front.pay.dto.ImgDto;

public interface AttachService {
    Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize);

    Attach saveVedioAttach(String userId, String videoAttachName, String videoAttachNewName, String videoAttachSize);

    Attach getInfoById(String urlId);

    String getHeadPicUrl(String userId);

    ImgDto getImgDto(String urlId);

    void deleteFile(String attachUrlId, String userId);
}
