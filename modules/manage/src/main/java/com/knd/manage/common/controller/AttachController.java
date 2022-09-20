package com.knd.manage.common.controller;

import com.knd.manage.basedata.mapper.BaseActionMapper;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.mapper.CourseHeadMapper;
import com.knd.manage.equip.mapper.AppReleaseVersionMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

//import com.knd.manage.common.service.feignInterface.UploadAttachService;

@Slf4j
@Api(tags = "云端管理-common")
@RestController
@CrossOrigin
@RequestMapping("/admin/common")
public class AttachController {
//     @Resource
//    private UploadAttachService uploadAttachService;



    @Value("${upload.AttachFileMaxSize}")
    private String attachFileMaxSize;

//    @Value("${upload.AttachTypeList}")
//    private String[] attachTypeList;

    @Value("${upload.imagesTypeList}")
    private String[] imagesTypeList;

    @Value("${upload.videoTypeList}")
    private String[] videoTypeList;

    @Value("${upload.appTypeList}")
    private String[] appTypeList;
     @Resource
    private IAttachService iAttachService;
//
//    @Log("I326-附件上传")
//    @ApiOperation(value = "I326-附件上传 ")
//    @PostMapping("/uploadAttach")
//    @Transactional(rollbackFor = Exception.class)
//    public Result uploadAttach(@RequestParam(value = "file", required = false) MultipartFile file,
//                               @RequestParam(value = "folderType") String folderType) {
//        Integer m = 1024;
//        try {
//            //文件非空检查
//            if (!file.isEmpty()) {
//                //文件大小检查
//                if (file.getSize() / m / m > Integer.parseInt(attachFileMaxSize)) {
//                    return ResultUtil.error(ResultEnum.FILE_SIZE_ERROR);
//                }
//                //获取文件名
//                String fileName = file.getOriginalFilename();
//                //获取后缀名
//                String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//                String[] args;
//                switch (folderType) {
//                    case "10":
//                        args = videoTypeList;
//                        break;
//                    case "20":
//                        args = imagesTypeList;
//                        break;
//                    case "30":
//                        args = appTypeList;
//                        break;
//                    default:
//                        return ResultUtil.error(ResultEnum.PARAM_ERROR);
//                }
//                //文件类型检查
//                for (String arg : args) {
//                    if (suffixName.equalsIgnoreCase(arg)) {
//                        return iAttachService.uploadAttach(file, folderType);
//                    }
//                }
//                return ResultUtil.error(ResultEnum.FILE_NONE_ERROR);
//            }
//            return ResultUtil.error(ResultEnum.FILE_NULL_ERROR);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("异常：" + e.getMessage());
//            return ResultUtil.error(ResultEnum.FAIL);
//        }
//    }

//    @Log("I361-获取obs上传信息")
//    @ApiOperation(value = "I361-获取obs上传信息")
//    @PostMapping("/getUploadInfo")
//    public Result getUploadInfo(@Validated VoUploadInfo vo, BindingResult bindingResult) throws Exception {
//        if (bindingResult.hasErrors()) {
//            return ResultUtil.error(ResultEnum.PARAM_ERROR);
//        }
//        //获取后缀名
//        String suffixName = vo.getNewName().substring(vo.getNewName().lastIndexOf(".") + 1).toLowerCase();
//        String[] args;
//        switch (vo.getFolderType()) {
//            case "10":
//                args = videoTypeList;
//                break;
//            case "20":
//                args = imagesTypeList;
//                break;
//            case "30":
//                args = appTypeList;
//                break;
//            default:
//                return ResultUtil.error(ResultEnum.PARAM_ERROR);
//        }
//        //文件类型检查
//        for (String arg : args) {
//            if (suffixName.equalsIgnoreCase(arg)) {
//                return iAttachService.getUploadInfo(vo);
//            }
//        }
//        return ResultUtil.error(ResultEnum.FILE_NONE_ERROR);
//    }


    @Resource
    private BaseActionMapper baseActionMapper;
    @Resource
    private CourseHeadMapper courseHeadMapper;
    @Resource
    private AppReleaseVersionMapper appReleaseVersionMapper;
    @Resource
    private AttachMapper attachMapper;


//    @Log("同步删除文件")
//    @ApiOperation(value = "同步删除文件")
//    @PostMapping("/tDfile")
//    @Transactional(rollbackFor = Exception.class)
//    public void tDfile(){
//        List<String> ldb = new ArrayList<>();
//
//        //获取未删除的文件id
//        QueryWrapper<CourseHead> q1 = new QueryWrapper<>();
//        q1.eq("deleted","0");
//        List<CourseHead> co = courseHeadMapper.selectList(q1);
//        for (CourseHead c : co){
//            ldb.add(c.getVideoAttachId());
//            ldb.add(c.getPicAttachId());
//        }
//
//        QueryWrapper<BaseAction> q2 = new QueryWrapper<>();
//        q2.eq("deleted","0");
//        List<BaseAction> co2 = baseActionMapper.selectList(q2);
//        for (BaseAction c : co2){
//            ldb.add(c.getVideoAttachId());
//            ldb.add(c.getPicAttachId());
//        }
//        QueryWrapper<AppReleaseVersion> q23 = new QueryWrapper<>();
//        q23.eq("deleted","0");
//        List<AppReleaseVersion> co23 = appReleaseVersionMapper.selectList(q23);
//        for (AppReleaseVersion c : co23){
//            ldb.add(c.getAttachId());
//        }
//        //获取所有未删除的数据id
//        //从数据库获取标识为0的文件
//        QueryWrapper<Attach> a = new QueryWrapper<>();
//        a.select("id");
//        a.eq("deleted", "0");
//        List<Attach> la = attachMapper.selectList(a);
//        List<String> lastr = new ArrayList<>();
//        for (Attach t : la){
//            lastr.add(t.getId());
//        }
//        //差集
//        lastr.removeAll(ldb);
//
//        //将剩余的都删除了
//        for (String t :lastr){
//            Attach ta = new Attach();
//            ta.setId(t);
//            ta.setDeleted("1");
//            ta.setLastModifiedBy("程序校验删除");
//            ta.setLastModifiedDate(LocalDateTime.now());
//            attachMapper.updateById(ta);
//        }
//    }


}
