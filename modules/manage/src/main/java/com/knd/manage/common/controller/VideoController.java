package com.knd.manage.common.controller;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.mall.service.ITbOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/common")
@Api(tags = "common")
@Slf4j
public class VideoController {

     @Resource
     private ITbOrderService iTbOrderService;


    /**
     * 上传视频
     *
     * */

    @PostMapping("addVideo")
    @ResponseBody
    public Result add( @RequestParam( value="file",required=false) MultipartFile multipartFile, HttpServletRequest request) {
        //视频上传
        //获取原文件名
        String name=multipartFile.getOriginalFilename();
        log.info("");
        //获取文件后缀
        String subffix=name.substring(name.lastIndexOf(".")+1,name.length());
        //控制格式
        if(subffix.equals("")&&!subffix.equals("mp4")&&!subffix.equals("mov")&&!subffix.equals("avi")&&!subffix.equals("wmv")&&!subffix.equals("m4v")&&!subffix.equals("dat")&&!subffix.equals("flv")&&!subffix.equals("mkv"))
        {
            return ResultUtil.error(ResultEnum.FAIL);
        }
        //新的文件名以日期命名
        //String fileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName="video";
        //获取项目路径到webapp
        String filepath=request.getServletContext().getRealPath("/")+"files\\";
        System.out.println(filepath);
        //获取项目根路径并转到static/videos
     //   String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/videos/";
        String path = "/usr/local/vita/web/installVideo";
        System.out.println(path);
        File file=new File(path);
        if(!file.exists())//文件夹不存在就创建
        {
            file.mkdirs();
        }
        //保存文件
        try {
            multipartFile.transferTo(new File(file+"/"+fileName+"."+subffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String realPath=file+"\\"+fileName+"."+subffix;
        String simulationVideo="/videos/"+fileName+"."+subffix;
         return ResultUtil.success(true);
    }




}

