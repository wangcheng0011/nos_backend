//package com.knd.manage.common.service.feignInterface.fallbackFactory;
//
//import com.knd.common.response.Result;
//import com.knd.common.response.ResultEnum;
//import com.knd.common.response.ResultUtil;
//import com.knd.manage.common.service.feignInterface.UploadAttachService;
//import feign.hystrix.FallbackFactory;
//import lombok.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//@Component
//public class UploadAttachServiceFallback implements FallbackFactory<UploadAttachService> {
//
//
//    @Override
//    public UploadAttachService create(Throwable throwable) {
//        return new UploadAttachService() {
//            @Override
//            public Result uploadAttach(MultipartFile file, @NonNull String folderType) {
//                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
//            }
//        };
//    }
//}
