package com.knd.manage.common.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.vo.VoUploadInfo;
import com.knd.mybatis.SuperService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-10
 */
public interface IAttachService extends SuperService<Attach> {

    //根据文件id获取文件信息
    Attach getInfoById(String id);

    //上传文件
    Result uploadAttach(MultipartFile file, String folderType);

    //获取obs上传信息(post方式)
    Result getUploadInfo(VoUploadInfo vo) throws Exception;

      //将原文件标识设为删除
    void deleteFile(String id,String userid);

    Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize);

     ImgDto getImgDto(String urlId);
}
