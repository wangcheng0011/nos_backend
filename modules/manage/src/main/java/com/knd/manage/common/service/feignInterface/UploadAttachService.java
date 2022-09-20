//package com.knd.manage.common.service.feignInterface;
//
//import com.knd.common.response.Result;
//import com.knd.manage.common.service.feignInterface.fallbackFactory.UploadAttachServiceFallback;
//import com.knd.manage.config.feign.FeignMultipartSupportConfig;
//import lombok.NonNull;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.multipart.MultipartFile;
//
//////注入远程服务的应用名【不区分大小写】 ，以及熔断回调类
////@FeignClient(name = "FRONT",fallbackFactory = UploadAttachServiceFallback.class, configuration = FeignMultipartSupportConfig.class)
////public interface UploadAttachService {
////    //第一二级路径
////    String prefix = "/front/common";
////    //附件上传
////    @RequestMapping(value = prefix + "/uploadAttach", method = RequestMethod.POST,
////            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
////            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////     Result uploadAttach(@RequestPart(value = "file", required = false) MultipartFile file,
////                         @NonNull @RequestParam(value = "folderType") String folderType) ;
////}
//
//public interface UploadAttachService {
//    Result uploadAttach(MultipartFile file, String folderType);
//}
//
