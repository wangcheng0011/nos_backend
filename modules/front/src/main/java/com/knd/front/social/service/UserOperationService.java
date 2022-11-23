package com.knd.front.social.service;

import com.knd.common.response.Result;
import com.knd.front.social.request.*;

public interface UserOperationService {

    /**
     * 发布动态
     * @param request
     * @return
     */
    Result moment(OperationMomentRequest request) throws Exception;

    /**
     * 给动态点赞
     * @param request
     * @return
     */
    Result praise(OperationPraiseRequest request);

    /**
     * 给动态回复或者@
     * @param request
     * @return
     */
    Result comment(OperationCommentRequest request);

    /**
     * 关注，取关，回关，移除
     * @return
     */
    Result followOrPass(OperationFollowRequest request);

    /**
     * 删除照片
     * @param request
     * @return
     */
    Result deletePic(OperationDeletePicRequest request);

    /**
     * 上传照片
     * @param request
     * @return
     */
    Result upPic(OperationUpPicRequest request);
}
