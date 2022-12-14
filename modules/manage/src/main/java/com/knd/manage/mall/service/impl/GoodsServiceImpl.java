package com.knd.manage.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.OrderStatusEnum;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.BaseActionEquipment;
import com.knd.manage.basedata.mapper.BaseActionEquipmentMapper;
import com.knd.manage.basedata.mapper.BaseActionMapper;
import com.knd.manage.basedata.service.IBaseActionAimService;
import com.knd.manage.basedata.service.IBaseActionEquipmentService;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.mapper.CourseTrainningNodeInfoMapper;
import com.knd.manage.mall.dto.GoodsDto;
import com.knd.manage.mall.dto.GoodsInfoDto;
import com.knd.manage.mall.dto.ImgDto;
import com.knd.manage.mall.dto.OrderDto;
import com.knd.manage.mall.entity.*;
import com.knd.manage.mall.mapper.*;
import com.knd.manage.mall.request.*;
import com.knd.manage.mall.service.ICategoryService;
import com.knd.manage.mall.service.IGoodsService;
import com.knd.manage.mall.service.feignInterface.FrontInfoFeignClient;
import com.knd.pay.request.ParseOrderNotifyRequest;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
@Service
@Transactional
@Log4j2
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsEntity> implements IGoodsService {

     @Resource
    private IAttachService iAttachService;

     @Resource
    private IBaseActionAimService iBaseActionAimService;
     @Resource
    private IBaseActionEquipmentService iBaseActionEquipmentService;

    @Resource
    private BaseActionMapper baseActionMapper;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private BaseActionEquipmentMapper baseActionEquipmentMapper;

    @Resource
    private CourseTrainningNodeInfoMapper courseTrainningNodeInfoMapper;

    @Resource
    private FrontInfoFeignClient frontInfoFeignClient;

    @Value("${pay.order.timeOut}")
    private Integer payTimeOut;


    @Resource
    private AttachMapper attachMapper;

    //????????????
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //????????????????????????????????????????????????
    @Value("${OBS.endPoint}")
    private String endPoint;
    //??????
    @Value("${OBS.ak}")
    private String ak;
    //??????
    @Value("${OBS.sk}")
    private String sk;
    //?????????????????????
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;
    //?????????????????????
    @Value("${OBS.videoFoldername}")
    private String videoFoldername;
    //apk???????????????
    @Value("${OBS.appFoldername}")
    private String appFoldername;
    //??????
    @Value("${OBS.bucketname}")
    private String bucketname;

    @Resource
    private GoodsImgMapper goodsImgMapper;

    @Resource
    private GoodsAttrValueMapper goodsAttrValueMapper;

    @Resource
    private ICategoryService iCategoryService;

    @Resource
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Override
    public Attach saveAttach(String userId,String picAttachName,String picAttachNewName,String picAttachSize) {
        log.info("---------------------------??????????????????-------------------------------");
        log.info("saveAttach userId:{{}}",userId);
        log.info("saveAttach picAttachName:{{}}",picAttachName);
        log.info("saveAttach picAttachNewName:{{}}",picAttachNewName);
        log.info("saveAttach picAttachSize:{{}}",picAttachSize);
        //??????????????????
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        log.info("saveAttach endPoint:{{}}",endPoint);
        // ??????ObsClient??????
        ObsClient obsClient = new ObsClient(ak, sk, config);
        log.info("saveAttach ak:{{}}",ak);
        log.info("saveAttach sk:{{}}",sk);
        // URL????????????1???
        long expireSeconds =  365 * 24 * 3600L;
        Attach aPi = null;
        if (StringUtils.isNotEmpty(picAttachNewName)) {
            //???????????????????????????
            log.info("saveAttach obsClient:{{}}",obsClient);
            log.info("saveAttach picAttachNewName:{{}}",picAttachNewName);
            log.info("saveAttach bucketname:{{}}",bucketname);
            log.info("saveAttach expireSeconds:{{}}",expireSeconds);
            String picUrl = ObsObjectUtil.getUrl(obsClient, picAttachNewName, bucketname, expireSeconds);
            String[] strs2 = picUrl.split("\\/");
            picUrl = strs2[strs2.length - 1];
            //??????????????????
            aPi = new Attach();
            aPi.setId(UUIDUtil.getShortUUID());
            aPi.setFileName(picAttachNewName);
            aPi.setFilePath(picUrl);
            aPi.setFileSize(picAttachSize);
            aPi.setFileType(picAttachName.substring(picAttachName.lastIndexOf(".") + 1));
            aPi.setCreateDate(LocalDateTime.now());
            aPi.setCreateBy(userId);
            aPi.setLastModifiedDate(LocalDateTime.now());
            aPi.setLastModifiedBy(userId);
            aPi.setDeleted("0");
            attachMapper.insert(aPi);
            log.info("saveAttach aPi:{{}}",aPi);
        }
        return aPi;
    }

    @Override
    public Result getGoodsList(String goodsType,String goodsName, String current,String platform) {

        if ("ANDROID".equals(platform) || "ios".equals(platform)){
            platform = "1";
        } else {
            platform = "2";
        }
        //??????
        Page<GoodsDto> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<GoodsDto> p = this.baseMapper.selectPageByLike(partPage,"%" + (goodsName == null ? "" : goodsName) + "%",goodsType,platform);

        return ResultUtil.success(p);
    }

    @Override
    public Result getGoodsListByType(GoodsListRequest request) {
        //??????
        Page<GoodsDto> partPage = new Page<>(Integer.parseInt(request.getCurrent()), PageInfo.pageSize);
        Page<GoodsDto> p = this.baseMapper.selectPageByTypeLike(partPage, request.getTypeList(), request.getGoodName());
        List<GoodsDto> records = p.getRecords();
        for (GoodsDto goodsDto : records) {
            //??????id??????????????????
            Attach aPi = attachMapper.selectById(goodsDto.getCoverUrl());
            if (aPi != null) {
                goodsDto.setCoverUrl(fileImagesPath + aPi.getFilePath());
            }
        }

        return ResultUtil.success(p);
    }

    @Override
    public Result tradeStatusQuery(String current,String userId,String status,String platform) {
        log.info("tradeStatusQuery current:{{}}",current);
        log.info("tradeStatusQuery userId:{{}}",userId);
        log.info("tradeStatusQuery current:{{}}",current);
        log.info("tradeStatusQuery platform:{{}}",platform);
        QueryWrapper<TbOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("createBy", userId);
        if(StringUtils.isNotEmpty(status)){
            queryWrapper.eq("status", status);
        }
        queryWrapper.eq("deleted", "0");
        queryWrapper.orderByDesc("createDate");
        if(StringUtils.isNotEmpty(platform)){
            queryWrapper.notInSql("id","select b.id from tb_order b where b.userId ='"+userId+"'and b.platform <>'"+platform+"' and b.platform <>'' and b.status = '1'");
        }
        Page<TbOrder> tbOrderPage = new Page<>();
       if (StringUtils.isEmpty(current)) {
            //????????????
            List<TbOrder> tbOrders = tbOrderMapper.selectList(queryWrapper);
            tbOrderPage.setRecords(tbOrders);
            tbOrderPage.setTotal(tbOrders.size());
            tbOrderPage.setSize(tbOrders.size());
       } else {
            Page<TbOrder> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
            tbOrderPage = tbOrderMapper.selectPage(partPage, queryWrapper);
        }
        log.info("tradeStatusQuery tbOrderPage:{{}}",tbOrderPage);
       // List<TbOrder> tbOrders = tbOrderMapper.selectList(queryWrapper);
        if(tbOrderPage.getRecords() == null) {
            ResultUtil.error(ResultEnum.VALID_ERROR.getCode(),"???????????????");
        }
        ArrayList<OrderDto> orderDtos = new ArrayList<>();
        Page<OrderDto> orderDtoPage = new Page<>(tbOrderPage.getSize(), PageInfo.pageSize);
        tbOrderPage.getRecords().stream().forEach(tbOrder->{
            OrderDto orderDto = new OrderDto();
            long remainingTime = ChronoUnit.SECONDS.between(DateUtils.getCurrentLocalDateTime(), tbOrder.getCreateDate().plusMinutes(payTimeOut));
            String remainingTimeStr = (remainingTime>=0?remainingTime:-1)+"";
            log.info("tradeQuery remainingTime:{{}}",remainingTime);
            log.info("tradeStatusQuery remainingTimeStr:{{}}",remainingTimeStr);
            if(remainingTime<=0&&OrderStatusEnum.WAIT_FOR_PAY.getCode().equals(tbOrder.getStatus())){
                tbOrder.setStatus(OrderStatusEnum.ORDER_CLOSED.getCode());
                tbOrder.setLastModifiedDate(DateUtils.getCurrentLocalDateTime());
                tbOrderMapper.updateById(tbOrder);
            }
            log.info("tradeQuery tbOrder:{{}}",tbOrder);
            tbOrder.setRemainingTime(remainingTimeStr);
            BeanUtils.copyProperties(tbOrder,orderDto);
            log.info("tradeStatusQuery tbOrder:{{}}",tbOrder);
            log.info("tradeStatusQuery GoodsId:{{}}",tbOrder.getGoodsId());
            if(StringUtils.isNotEmpty(tbOrder.getGoodsId())){
                QueryWrapper<GoodsEntity> goodsEntityQueryWrapper = new QueryWrapper<>();
                goodsEntityQueryWrapper.eq("id",tbOrder.getGoodsId());
                goodsEntityQueryWrapper.eq("deleted","0");
                GoodsEntity goodsEntity = goodsMapper.selectOne(goodsEntityQueryWrapper);
                log.info("tradeStatusQuery goodsEntity:{{}}",goodsEntity);
                GoodsDto goodsDto = new GoodsDto();
                if(StringUtils.isNotEmpty(goodsEntity)){
                    BeanUtils.copyProperties(goodsEntity,goodsDto);
                    if(StringUtils.isNotEmpty(goodsEntity.getCoverAttachId())){
                        //??????id??????????????????
                        Attach aPi = attachMapper.selectById(goodsEntity.getCoverAttachId());
                        log.info("tradeStatusQuery aPi:{{}}",aPi);
                        if (aPi != null) {
                            goodsDto.setCoverUrl(fileImagesPath + aPi.getFilePath());
                        }
                    }
                }
                orderDto.setGoodsDto(goodsDto);
            }
            log.info("tradeStatusQuery UserReceiveAddressId:{{}}",tbOrder.getUserReceiveAddressId());
            if(StringUtils.isNotEmpty(tbOrder.getUserReceiveAddressId())){
                UserReceiveAddressEntity addressEntity = userReceiveAddressMapper.selectById(tbOrder.getUserReceiveAddressId());
                orderDto.setUserReceiveAddressEntity(addressEntity);
            }
            orderDtos.add(orderDto);
        });
        orderDtoPage.setRecords(orderDtos);
        orderDtoPage.setTotal(tbOrderPage.getTotal());
        orderDtoPage.setCurrent(tbOrderPage.getCurrent());
        log.info("tradeStatusQuery orderDtoPage:{{}}",orderDtoPage);
        return ResultUtil.success(orderDtoPage);
    }



    @Override
    public Result getGoodsAttrValueList(String goodsId, String categoryId) {
        Result attrByCategoryId = iCategoryService.getAttrByCategoryId(categoryId);
        List<AttrEntity> attrEntities = (List<AttrEntity>) attrByCategoryId.getData();
        List<GoodsAttrValueEntity> goodsAttrValueEntitys = goodsAttrValueMapper.selectList(new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", goodsId).orderByAsc("length(sort)","sort"));
        List<GoodsAttrRequest> attrList = new ArrayList<>();
        for (AttrEntity e: attrEntities
             ) {
            GoodsAttrRequest goodsAttrRequest = new GoodsAttrRequest();
            BeanUtils.copyProperties(e,goodsAttrRequest);
            attrList.add(goodsAttrRequest);
        }
        if(attrList.size()<=0) {
            for (GoodsAttrRequest e:attrList
            ) {
                e.setAttrValue("");
            }
        }else{
            for (GoodsAttrRequest e:attrList
            ) {
                for(GoodsAttrValueEntity i: goodsAttrValueEntitys){
                    if(i.getAttrId().equals(e.getId())){
                        e.setAttrValue(i.getAttrValue());
                    }
                }
            }
        }

        return ResultUtil.success(attrList);
    }

    @Override
    public Result getGoods(String goodsId) {
        QueryWrapper<GoodsEntity> qw = new QueryWrapper<>();
        qw.eq("id", goodsId);
        qw.eq("deleted", "0");
        GoodsEntity goodsEntity = baseMapper.selectOne(qw);
        GoodsInfoDto goodsInfoDto = new GoodsInfoDto();
        BeanUtils.copyProperties(goodsEntity,goodsInfoDto);
        if (goodsEntity != null) {
            List<GoodsAttrValueEntity> goodsAttrValueEntitys = goodsAttrValueMapper.selectList(new QueryWrapper<GoodsAttrValueEntity>().eq("goodsId", goodsId).orderByAsc("length(sort)","sort"));
            goodsInfoDto.setAttrList(goodsAttrValueEntitys);
            List<GoodsImgEntity> goodsHeadImgEntities = goodsImgMapper.selectList(new QueryWrapper<GoodsImgEntity>()
                    .eq("goodsId", goodsId).eq("imgType", "0"));
            List<ImgDto> headImgList = new ArrayList<>();
            for (GoodsImgEntity goodsHeadImgEntity : goodsHeadImgEntities) {
                //??????id??????????????????
                Attach aPi = iAttachService.getInfoById(goodsHeadImgEntity.getAttachId());
                if (aPi != null) {
                    ImgDto imgDto = new ImgDto();
                    imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
                    imgDto.setPicAttachSize(aPi.getFileSize());
                    String[] strs = (aPi.getFilePath()).split("\\?");
                    imgDto.setPicAttachNewName(imageFoldername + strs[0]);
                    imgDto.setPicAttachName(aPi.getFileName());
                    headImgList.add(imgDto);
                }
            }
            goodsInfoDto.setHeadImgList(headImgList);

            List<GoodsImgEntity> goodsInfoImgEntities = goodsImgMapper.selectList(new QueryWrapper<GoodsImgEntity>()
                    .eq("goodsId", goodsId).eq("imgType", "1"));

            List<ImgDto> infoImgList = new ArrayList<>();
            for (GoodsImgEntity goodsInfoImgEntity : goodsInfoImgEntities) {
                //??????id??????????????????
                Attach aPi = iAttachService.getInfoById(goodsInfoImgEntity.getAttachId());
                if (aPi != null) {
                    ImgDto imgDto = new ImgDto();
                    imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
                    imgDto.setPicAttachSize(aPi.getFileSize());
                    String[] strs = (aPi.getFilePath()).split("\\?");
                    imgDto.setPicAttachNewName(imageFoldername + strs[0]);
                    imgDto.setPicAttachName(aPi.getFileName());
                    infoImgList.add(imgDto);
                }
            }
            goodsInfoDto.setInfoImgList(infoImgList);

            //??????id??????????????????
            Attach aPi = iAttachService.getInfoById(goodsEntity.getCoverAttachId());
            ImgDto imgDto = null;
            if (aPi != null) {
                imgDto = new ImgDto();
                imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
                imgDto.setPicAttachSize(aPi.getFileSize());
                String[] strs = (aPi.getFilePath()).split("\\?");
                imgDto.setPicAttachNewName(imageFoldername + strs[0]);
                imgDto.setPicAttachName(aPi.getFileName());
            }
            goodsInfoDto.setCoverImg(imgDto);

        }
        return ResultUtil.success(goodsInfoDto);
    }

    @Override
    public Result updateGoodsPublishStatus(String userId, String status, String id) {
        //??????id????????????
        QueryWrapper<GoodsEntity> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        //qw.select("status");
        GoodsEntity eq = baseMapper.selectOne(qw);
        if (eq ==null){
            //?????????id?????????
            //???????????????
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        eq.setPublishStatus(status);
        eq.setLastModifiedBy(userId);
        eq.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(eq);

        //??????
        return ResultUtil.success();
    }

    @Override
    public Result deleteGoods(String userId, String id) {
        GoodsEntity goodsEntity = baseMapper.selectById(id);
        if(goodsEntity != null) {
            if("2".equals(goodsEntity.getCategoryId())){
                BaseActionEquipment baseActionEquipment = new BaseActionEquipment();
                baseActionEquipment.setDeleted("1");
                baseActionEquipment.setLastModifiedBy(userId);
                baseActionEquipment.setLastModifiedDate(LocalDateTime.now());
                QueryWrapper<BaseActionEquipment> updateWrapper = new QueryWrapper<>();
                updateWrapper.eq("deleted","0");
                updateWrapper.eq("equipmentId",id);
                baseActionEquipmentMapper.update(baseActionEquipment,updateWrapper);
            }

            goodsEntity.setDeleted("1");
            goodsEntity.setLastModifiedBy(userId);
            goodsEntity.setLastModifiedDate(LocalDateTime.now());
            baseMapper.updateById(goodsEntity);
            //??????????????????

            QueryWrapper<GoodsAttrValueEntity> goodsAttrValueQuery = new QueryWrapper<GoodsAttrValueEntity>()
                    .eq("goodsId", goodsEntity.getId());
            //??????????????????
            List<GoodsAttrValueEntity> goodsAttrValueEntities = goodsAttrValueMapper.selectList(goodsAttrValueQuery);
            for (GoodsAttrValueEntity g:goodsAttrValueEntities
                 ) {
                g.setDeleted("1");
                g.setLastModifiedBy(userId);
                g.setLastModifiedDate(LocalDateTime.now());
                goodsAttrValueMapper.updateById(g);
            }

            QueryWrapper<GoodsImgEntity> goodsImgQuery = new QueryWrapper<GoodsImgEntity>()
                    .eq("goodsId", goodsEntity.getId());
            List<GoodsImgEntity> goodsImgEntities = goodsImgMapper.selectList(goodsImgQuery);
            for(GoodsImgEntity g : goodsImgEntities){
                g.setDeleted("1");
                g.setLastModifiedBy(userId);
                g.setLastModifiedDate(LocalDateTime.now());
                goodsImgMapper.updateById(g);
            }

            //??????
            return ResultUtil.success();
        }else{
            //??????
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"????????????????????????????????????");
        }
    }




    //??????
    @Override
    public Result add(CreateGoodsRequest createGoodsRequest) {
        IGoodsService proxy =(IGoodsService) AopContext.currentProxy();
        //????????????????????????
        Attach aPi;
        aPi = proxy.saveAttach(createGoodsRequest.getUserId(), createGoodsRequest.getPicAttachName()
                , createGoodsRequest.getPicAttachNewName(), createGoodsRequest.getPicAttachSize());
        //????????????
        GoodsEntity g = new GoodsEntity();
        //g.setId(UUIDUtil.getShortUUID());
        g.setCategoryId(createGoodsRequest.getCategoryId());
       // g.setCoverAttachId(aPi.getId());
        g.setGoodsName(createGoodsRequest.getGoodsName());
        g.setGoodsDesc(createGoodsRequest.getGoodsDesc());
        g.setGoodsType(createGoodsRequest.getGoodsType());
        g.setPublishStatus("0");
        g.setPrice(new BigDecimal(createGoodsRequest.getPrice()));
        g.setCreateDate(LocalDateTime.now());
        g.setCreateBy(createGoodsRequest.getUserId());
        g.setLastModifiedDate(LocalDateTime.now());
        g.setLastModifiedBy(createGoodsRequest.getUserId());
        g.setInstallFlag(createGoodsRequest.getInstallFlag());
        g.setDeleted("0");
        baseMapper.insert(g);
        //?????????????????????
        for (int i = 0; i < createGoodsRequest.getAttrList().size(); i++) {
            GoodsAttrValueEntity goodsAttrValueEntity = new GoodsAttrValueEntity();
            goodsAttrValueEntity.setAttrId(createGoodsRequest.getAttrList().get(i).getId());
            goodsAttrValueEntity.setAttrName(createGoodsRequest.getAttrList().get(i).getAttrName());
            goodsAttrValueEntity.setAttrValue(createGoodsRequest.getAttrList().get(i).getAttrValue());
            goodsAttrValueEntity.setGoodsId(g.getId());
            goodsAttrValueEntity.setSort((i + 1) + "");
            goodsAttrValueEntity.setCreateDate(LocalDateTime.now());
            goodsAttrValueEntity.setCreateBy(createGoodsRequest.getUserId());
            goodsAttrValueEntity.setLastModifiedDate(LocalDateTime.now());
            goodsAttrValueEntity.setLastModifiedBy(createGoodsRequest.getUserId());
            goodsAttrValueEntity.setDeleted("0");
            goodsAttrValueMapper.insert(goodsAttrValueEntity);
        }
        //?????????????????????
        for (int i = 0; i < createGoodsRequest.getHeadImgList().size(); i++) {
            GoodsHeadImageRequest e = createGoodsRequest.getHeadImgList().get(i);
            Attach attach = proxy.saveAttach(createGoodsRequest.getUserId(),
                    e.getPicAttachName(),e.getPicAttachNewName(),e.getPicAttachSize());
            if(attach != null) {
                GoodsImgEntity goodsImgEntity = new GoodsImgEntity();
                goodsImgEntity.setGoodsId(g.getId());
                goodsImgEntity.setAttachId(attach.getId());
                goodsImgEntity.setImgType("0");
                goodsImgEntity.setSort((i + 1) + "");
                goodsImgEntity.setCreateDate(LocalDateTime.now());
                goodsImgEntity.setCreateBy(createGoodsRequest.getUserId());
                goodsImgEntity.setLastModifiedDate(LocalDateTime.now());
                goodsImgEntity.setLastModifiedBy(createGoodsRequest.getUserId());
                goodsImgEntity.setDeleted("0");
                goodsImgMapper.insert(goodsImgEntity);
            }
//            if (StringUtils.isNotEmpty(e.getPicAttachNewName())) {
//                //???????????????????????????
//                String picUrl = ObsObjectUtil.getUrl(obsClient, e.getPicAttachNewName(), bucketname, expireSeconds);
//                String[] strs2 = picUrl.split("\\/");
//                picUrl = strs2[strs2.length - 1];
//                //??????????????????
//                attach = new Attach();
//                attach.setId(UUIDUtil.getShortUUID());
//                attach.setFileName(e.getPicAttachName());
//                attach.setFilePath(picUrl);
//                attach.setFileSize(e.getPicAttachSize());
//                attach.setFileType(e.getPicAttachName().substring(e.getPicAttachName().lastIndexOf(".") + 1));
//                attach.setCreateDate(LocalDateTime.now());
//                attach.setCreateBy(createGoodsRequest.getUserId());
//                attach.setLastModifiedDate(LocalDateTime.now());
//                attach.setLastModifiedBy(createGoodsRequest.getUserId());
//                attach.setDeleted("0");
//                attachMapper.insert(attach);
//            }
        }

        //?????????????????????
        for (int i = 0; i < createGoodsRequest.getInfoImgList().size(); i++) {
            GoodsInfoImageRequest e = createGoodsRequest.getInfoImgList().get(i);
            Attach attach = proxy.saveAttach(createGoodsRequest.getUserId(),
                    e.getPicAttachName(),e.getPicAttachNewName(),e.getPicAttachSize());
            if(attach != null) {
                GoodsImgEntity goodsImgEntity = new GoodsImgEntity();
                goodsImgEntity.setGoodsId(g.getId());
                goodsImgEntity.setAttachId(attach.getId());
                goodsImgEntity.setImgType("1");
                goodsImgEntity.setSort((i + 1) + "");
                goodsImgEntity.setCreateDate(LocalDateTime.now());
                goodsImgEntity.setCreateBy(createGoodsRequest.getUserId());
                goodsImgEntity.setLastModifiedDate(LocalDateTime.now());
                goodsImgEntity.setLastModifiedBy(createGoodsRequest.getUserId());
                goodsImgEntity.setDeleted("0");
                goodsImgMapper.insert(goodsImgEntity);
            }
//            if (StringUtils.isNotEmpty(e.getPicAttachNewName())) {
//                //???????????????????????????
//                String picUrl = ObsObjectUtil.getUrl(obsClient, e.getPicAttachNewName(), bucketname, expireSeconds);
//                String[] strs2 = picUrl.split("\\/");
//                picUrl = strs2[strs2.length - 1];
//                //??????????????????
//                attach = new Attach();
//                attach.setId(UUIDUtil.getShortUUID());
//                attach.setFileName(e.getPicAttachName());
//                attach.setFilePath(picUrl);
//                attach.setFileSize(e.getPicAttachSize());
//                attach.setFileType(e.getPicAttachName().substring(e.getPicAttachName().lastIndexOf(".") + 1));
//                attach.setCreateDate(LocalDateTime.now());
//                attach.setCreateBy(createGoodsRequest.getUserId());
//                attach.setLastModifiedDate(LocalDateTime.now());
//                attach.setLastModifiedBy(createGoodsRequest.getUserId());
//                attach.setDeleted("0");
//                attachMapper.insert(attach);
//            }
        }
        //??????
        return ResultUtil.success();
    }

    @Override
    public Result update(CreateGoodsRequest createGoodsRequest) {

        if(StringUtils.isEmpty(createGoodsRequest.getId())) {
            return ResultUtil.error("U0995", "??????????????????id");
        }

        IGoodsService proxy =(IGoodsService) AopContext.currentProxy();

        GoodsEntity goodsEntity = baseMapper.selectById(createGoodsRequest.getId());
        if(goodsEntity == null || !"0".equals(goodsEntity.getDeleted())) {
            return ResultUtil.error("U0995", "???????????????????????????");
        }
        //??????????????????id,???????????????
        List<String> attachIds = new ArrayList<>();
        attachIds.add(goodsEntity.getCoverAttachId());

        //BeanUtils.copyProperties(createGoodsRequest,goodsEntity);

        QueryWrapper<GoodsAttrValueEntity> goodsAttrValueQuery = new QueryWrapper<GoodsAttrValueEntity>()
                .eq("goodsId", goodsEntity.getId());
        //??????????????????
        goodsAttrValueMapper.delete(goodsAttrValueQuery);

        QueryWrapper<GoodsImgEntity> goodsImgQuery = new QueryWrapper<GoodsImgEntity>()
                .eq("goodsId", goodsEntity.getId());
        List<GoodsImgEntity> goodsImgEntities = goodsImgMapper.selectList(goodsImgQuery);
        for(GoodsImgEntity g : goodsImgEntities){
            attachIds.add(g.getAttachId());
        }
        attachMapper.deleteBatchIds(attachIds);
        goodsImgMapper.delete(goodsImgQuery);

        //????????????????????????
        Attach aPi;
        aPi = proxy.saveAttach(createGoodsRequest.getUserId(), createGoodsRequest.getPicAttachName()
                , createGoodsRequest.getPicAttachNewName(), createGoodsRequest.getPicAttachSize());
        //????????????
        //GoodsEntity g = new GoodsEntity();

        goodsEntity.setCategoryId(createGoodsRequest.getCategoryId());
        goodsEntity.setCoverAttachId(aPi.getId());
        goodsEntity.setGoodsName(createGoodsRequest.getGoodsName());
        goodsEntity.setGoodsDesc(createGoodsRequest.getGoodsDesc());
        goodsEntity.setGoodsType(createGoodsRequest.getGoodsType());
        goodsEntity.setPublishStatus("0");
        goodsEntity.setPrice(new BigDecimal(createGoodsRequest.getPrice()));
        goodsEntity.setInstallFlag(createGoodsRequest.getInstallFlag());
//        g.setCreateDate(LocalDateTime.now());
//        g.setCreateBy(createGoodsRequest.getUserId());
        goodsEntity.setLastModifiedDate(LocalDateTime.now());
        goodsEntity.setLastModifiedBy(createGoodsRequest.getUserId());
        goodsEntity.setDeleted("0");
        baseMapper.updateById(goodsEntity);
        //?????????????????????
        for (int i = 0; i < createGoodsRequest.getAttrList().size(); i++) {
            GoodsAttrValueEntity goodsAttrValueEntity = new GoodsAttrValueEntity();
            goodsAttrValueEntity.setAttrId(createGoodsRequest.getAttrList().get(i).getId());
            goodsAttrValueEntity.setAttrName(createGoodsRequest.getAttrList().get(i).getAttrName());
            goodsAttrValueEntity.setAttrValue(createGoodsRequest.getAttrList().get(i).getAttrValue());
            goodsAttrValueEntity.setGoodsId(goodsEntity.getId());
            goodsAttrValueEntity.setSort((i + 1) + "");
            goodsAttrValueEntity.setCreateDate(LocalDateTime.now());
            goodsAttrValueEntity.setCreateBy(createGoodsRequest.getUserId());
            goodsAttrValueEntity.setLastModifiedDate(LocalDateTime.now());
            goodsAttrValueEntity.setLastModifiedBy(createGoodsRequest.getUserId());
            goodsAttrValueEntity.setDeleted("0");
            goodsAttrValueMapper.insert(goodsAttrValueEntity);
        }
        //?????????????????????
        for (int i = 0; i < createGoodsRequest.getHeadImgList().size(); i++) {
            GoodsHeadImageRequest e = createGoodsRequest.getHeadImgList().get(i);
            Attach attach = proxy.saveAttach(createGoodsRequest.getUserId(),
                    e.getPicAttachName(),e.getPicAttachNewName(),e.getPicAttachSize());
            if(attach != null) {
                GoodsImgEntity goodsImgEntity = new GoodsImgEntity();
                goodsImgEntity.setGoodsId(goodsEntity.getId());
                goodsImgEntity.setAttachId(attach.getId());
                goodsImgEntity.setImgType("0");
                goodsImgEntity.setSort((i + 1) + "");
                goodsImgEntity.setCreateDate(LocalDateTime.now());
                goodsImgEntity.setCreateBy(createGoodsRequest.getUserId());
                goodsImgEntity.setLastModifiedDate(LocalDateTime.now());
                goodsImgEntity.setLastModifiedBy(createGoodsRequest.getUserId());
                goodsImgEntity.setDeleted("0");
                goodsImgMapper.insert(goodsImgEntity);
            }
        }

        //?????????????????????
        for (int i = 0; i < createGoodsRequest.getInfoImgList().size(); i++) {
            GoodsInfoImageRequest e = createGoodsRequest.getInfoImgList().get(i);
            Attach attach = proxy.saveAttach(createGoodsRequest.getUserId(),
                    e.getPicAttachName(),e.getPicAttachNewName(),e.getPicAttachSize());
            if(attach != null) {
                GoodsImgEntity goodsImgEntity = new GoodsImgEntity();
                goodsImgEntity.setGoodsId(goodsEntity.getId());
                goodsImgEntity.setAttachId(attach.getId());
                goodsImgEntity.setImgType("1");
                goodsImgEntity.setSort((i + 1) + "");
                goodsImgEntity.setCreateDate(LocalDateTime.now());
                goodsImgEntity.setCreateBy(createGoodsRequest.getUserId());
                goodsImgEntity.setLastModifiedDate(LocalDateTime.now());
                goodsImgEntity.setLastModifiedBy(createGoodsRequest.getUserId());
                goodsImgEntity.setDeleted("0");
                goodsImgMapper.insert(goodsImgEntity);
            }
        }
        //??????
        return ResultUtil.success();
    }

    /**
    //??????
    @Override
    public Result edit(VoSaveAction vo) {
        //??????id??????????????????
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("id", vo.getActionId());
        qw.eq("deleted", "0");
        qw.select("action", "videoAttachId", "picAttachId");
        BaseAction ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //?????????id?????????
            //???????????????
            return ResultUtil.error("U0995", "??????id?????????");
        }
        if (!ta.getAction().equals(vo.getAction())) {
            //??????
            qw.clear();
            qw.eq("action", vo.getAction());
            qw.eq("deleted", "0");
            //????????????
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //??????????????????
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }

        BaseAction b = new BaseAction();
        b.setId(vo.getActionId());
        b.setAction(vo.getAction());
        b.setActionType(vo.getActionType());
        b.setRemark(vo.getRemark());
        b.setTargetId(vo.getTargetId());
        b.setPartId(vo.getPartId());
        b.setCountMode(vo.getCountMode());
        b.setPowerLevel(vo.getPowerLevel());
        b.setAimDuration(vo.getAimDuration());
        b.setAimTimes(vo.getAimTimes());
        b.setBasePower(vo.getBasePower());
        b.setFreeTrainFlag(vo.getFreeTrainFlag());
        //
        // URL????????????1???
        long expireSeconds =  365 * 24 * 3600L;

        if (StringUtils.isNotEmpty(vo.getVideoAttachNewName())) {
            //?????????????????????????????????
            Attach aVi = iAttachService.getInfoById(ta.getVideoAttachId());
            //????????????????????????
            boolean vFlag = false;
            if (aVi != null) {
                String[] strs1 = aVi.getFilePath().split("\\?");
                if (!strs1[0].equals(vo.getVideoAttachNewName())) {
                    //??????
                    vFlag = true;
                }
                //???????????????
            } else {
                //??????
                vFlag = true;
            }
            if (vFlag) {
                //????????????
                //??????????????????????????????
                iAttachService.deleteFile(ta.getVideoAttachId(),vo.getUserId());

                //??????????????????
                ObsConfiguration config = new ObsConfiguration();
                config.setSocketTimeout(30000);
                config.setConnectionTimeout(10000);
                config.setEndPoint(endPoint);
                // ??????ObsClient??????
                ObsClient obsClient = new ObsClient(ak, sk, config);
                //???????????????????????????
                String videoUrl = ObsObjectUtil.getUrl(obsClient, vo.getVideoAttachNewName(), bucketname, expireSeconds);
                String[] strs11 = videoUrl.split("\\/");
                videoUrl = strs11[strs11.length - 1];
                //??????????????????
                Attach aVi2 = new Attach();
                aVi2.setId(UUIDUtil.getShortUUID());
                aVi2.setFileName(vo.getVideoAttachName());
                aVi2.setFilePath(videoUrl);
                aVi2.setFileSize(vo.getVideoAttachSize());
                aVi2.setFileType(vo.getVideoAttachName().substring(vo.getVideoAttachName().lastIndexOf(".") + 1));
                aVi2.setCreateDate(LocalDateTime.now());
                aVi2.setCreateBy(vo.getUserId());
                aVi2.setLastModifiedDate(LocalDateTime.now());
                aVi2.setLastModifiedBy(vo.getUserId());
                aVi2.setDeleted("0");
                attachMapper.insert(aVi2);
                //
                b.setVideoAttachId(aVi2.getId());
            }
        }else {
            //?????????????????????
            //?????????????????????
            b.setVideoAttachId("");
        }

        if (StringUtils.isNotEmpty(vo.getPicAttachNewName())) {
            //?????????????????????????????????
            Attach aPi = iAttachService.getInfoById(ta.getPicAttachId());
            //????????????????????????
            boolean pFlag = false;
            if (aPi != null) {
                String[] strs2 = aPi.getFilePath().split("\\?");
                if (!strs2[0].equals(vo.getPicAttachNewName())) {
                    //??????
                    pFlag = true;
                }
                //???????????????
            } else {
                //??????
                pFlag = true;
            }
            if (pFlag) {
                //????????????
                //??????????????????????????????
                iAttachService.deleteFile(ta.getPicAttachId(),vo.getUserId());
                //??????????????????
                ObsConfiguration config = new ObsConfiguration();
                config.setSocketTimeout(30000);
                config.setConnectionTimeout(10000);
                config.setEndPoint(endPoint);
                // ??????ObsClient??????
                ObsClient obsClient = new ObsClient(ak, sk, config);
                //???????????????????????????
                String picUrl = ObsObjectUtil.getUrl(obsClient, vo.getPicAttachNewName(), bucketname, expireSeconds);
                String[] strs22 = picUrl.split("\\/");
                picUrl = strs22[strs22.length - 1];
                //??????????????????
                Attach aPi2 = new Attach();
                aPi2.setId(UUIDUtil.getShortUUID());
                aPi2.setFileName(vo.getPicAttachName());
                aPi2.setFilePath(picUrl);
                aPi2.setFileSize(vo.getPicAttachSize());
                aPi2.setFileType(vo.getPicAttachName().substring(vo.getPicAttachName().lastIndexOf(".") + 1));
                aPi2.setCreateDate(LocalDateTime.now());
                aPi2.setCreateBy(vo.getUserId());
                aPi2.setLastModifiedDate(LocalDateTime.now());
                aPi2.setLastModifiedBy(vo.getUserId());
                aPi2.setDeleted("0");
                attachMapper.insert(aPi2);
                //
                b.setPicAttachId(aPi2.getId());
            }
        }else {
            b.setPicAttachId("");
        }
        b.setStrengthTestFlag(vo.getStrengthTestFlag());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());

        //????????????????????????????????????????????????????????????
        if("1".equals(vo.getStrengthTestFlag()) && "0".equals(vo.getActionType())){
            b.setStrengthTestFlag("0");
        }else{
            b.setStrengthTestFlag(vo.getStrengthTestFlag());
        }
        baseMapper.updateById(b);
        //???????????? ??? ???????????????????????????
        iBaseActionAimService.edit(vo.getUserId(), vo.getActionId(), vo.getAimSettingList());
        //???????????? ??? ???????????????????????????
        iBaseActionEquipmentService.edit(vo.getUserId(), vo.getActionId(), vo.getEquipmentList());

        //?????????????????????1 ????????????????????????????????????????????????
        if("1".equals(b.getStrengthTestFlag())) {
            //??????
            QueryWrapper<BaseAction> qw1 = new QueryWrapper<>();
            qw1.eq("strengthTestFlag", "1");
            qw1.eq("actionType", b.getActionType());
            qw1.ne("id", b.getId());
            qw1.select("id","strengthTestFlag");
            BaseAction baseAction = baseMapper.selectOne(qw1);
            if(baseAction != null && !StringUtils.isEmpty(baseAction.getId())) {
                baseAction.setStrengthTestFlag("0");
                baseAction.setLastModifiedBy(vo.getUserId());
                baseAction.setLastModifiedDate(LocalDateTime.now());
                baseMapper.updateById(baseAction);
            }
        }

        //??????
        return ResultUtil.success();
    }

    //??????
    @Override
    public Result deleteAction(VoId vo) {
        //????????????????????????????????????????????????
        int aCount = courseTrainningNodeInfoMapper.selectCourseCountByActionId(vo.getId());
        if (aCount != 0) {
            return ResultUtil.error(ResultEnum.Action_ERROR);
        }
        //??????id????????????
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        qw.select("videoAttachId", "picAttachId");
        BaseAction ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //?????????id?????????
            //???????????????
            return ResultUtil.error("U0995", "??????id?????????");
        }
        //??????????????????????????????
        iAttachService.deleteFile(ta.getVideoAttachId(),vo.getUserId());
        iAttachService.deleteFile(ta.getPicAttachId(),vo.getUserId());
        BaseAction b = new BaseAction();
        b.setDeleted("1");
        b.setId(vo.getId());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //?????? ?????????????????????????????????
        iBaseActionAimService.delete(vo.getUserId(), vo.getId());
        //?????? ?????????????????????????????????
        iBaseActionEquipmentService.delete(vo.getUserId(), vo.getId());
        //??????
        return ResultUtil.success();
    }

    //????????????
    @Override
    public Result getAction(String actionId) {
        QueryWrapper<BaseAction> qw = new QueryWrapper<>();
        qw.eq("id", actionId);
        qw.eq("deleted", "0");
        qw.select("action","actionType", "remark", "targetId", "partId", "countMode", "powerLevel", "aimDuration",
                "aimTimes", "basePower", "videoAttachId", "picAttachId", "strengthTestFlag", "freeTrainFlag");
        BaseAction b = baseMapper.selectOne(qw);
        ActionDto actionDto = new ActionDto();
        if (b != null) {
            //????????????id?????? ????????????????????????
            List<AimSettingDto> voAimSettingList = iBaseActionAimService.getList(actionId);
            //????????????id?????? ??????????????????????????????
            List<EquipmentDto> voEquipmentList = iBaseActionEquipmentService.getList(actionId);
            //??????????????????
            actionDto.setId(actionId);
            actionDto.setAimSettingList(voAimSettingList);
            actionDto.setEquipmentList(voEquipmentList);
            actionDto.setAction(b.getAction());
            actionDto.setActionType(b.getActionType());
            actionDto.setRemark(b.getRemark());
            actionDto.setTargetId(b.getTargetId());
            actionDto.setPartId(b.getPartId());
            actionDto.setCountMode(b.getCountMode());
            actionDto.setPowerLevel(b.getPowerLevel());
            actionDto.setAimDuration(b.getAimDuration());
            actionDto.setAimTimes(b.getAimTimes());
            actionDto.setBasePower(b.getBasePower());
            actionDto.setStrengthTestFlag(b.getStrengthTestFlag());
            actionDto.setFreeTrainFlag(b.getFreeTrainFlag());
            //??????id??????????????????
            Attach aVi = iAttachService.getInfoById(b.getVideoAttachId());
            if (aVi != null) {
                actionDto.setVideoAttachUrl(FileVideoPath + aVi.getFilePath());
                actionDto.setVideoAttachSize(aVi.getFileSize());
                actionDto.setVideoAttachName(aVi.getFileName());
                String[] strs = (aVi.getFilePath()).split("\\?");
                actionDto.setVideoAttachNewName(videoFoldername + strs[0]);

            }
            //??????id??????????????????
            Attach aPi = iAttachService.getInfoById(b.getPicAttachId());
            if (aPi != null) {
                actionDto.setPicAttachUrl(FileImagesPath + aPi.getFilePath());
                actionDto.setPicAttachSize(aPi.getFileSize());
                String[] strs = (aPi.getFilePath()).split("\\?");
                actionDto.setPicAttachNewName(imageFoldername + strs[0]);
                actionDto.setPicAttachName(aPi.getFileName());
            }

        }
        return ResultUtil.success(actionDto);
    }

    //??????????????????
    @Override
    public Result getActionList(String actionType,String target, String part, String action, String currentPage) {
        ActionListDto actionListDto = new ActionListDto();
        List<ActionInfoDto> actionInfoDtos;
        //??????????????????
        if (StringUtils.isEmpty(currentPage)) {
            //????????????
            actionInfoDtos = baseActionMapper.selectAllByLike("%" + (actionType == null ? "" : actionType) + "%","%" + (target == null ? "" : target) + "%",
                    "%" + (part == null ? "" : part) + "%", "%" + (action == null ? "" : action) + "%");
            actionListDto.setTotal(actionInfoDtos.size());
        } else {
            //??????
            Page<ActionInfoDto> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            Page<ActionInfoDto> p = this.baseMapper.selectPageByLike(partPage,"%" + (actionType == null ? "" : actionType) + "%", "%" + (target == null ? "" : target) + "%",
                    "%" + (part == null ? "" : part) + "%", "%" + (action == null ? "" : action) + "%");
            actionInfoDtos = p.getRecords();
            actionListDto.setTotal((int) p.getTotal());
        }
        //??????????????????
        for (ActionInfoDto a : actionInfoDtos) {
            //????????????id?????? ??????????????????????????????
            List<EquipmentDto> voEquipmentList = iBaseActionEquipmentService.getList(a.getId());
            StringBuilder s = new StringBuilder();
            if (!voEquipmentList.isEmpty()) {
                for (EquipmentDto e : voEquipmentList) {
                    if (e != null) {
                        s.append(e.getEquipment()).append(",");
                    }
                }
            }
            //???????????????????????????
            if (s.length() > 0) {
                String str = s.substring(0, s.length() - 1);
                a.setEquipments(str);
            }
        }
        actionListDto.setActionList(actionInfoDtos);
        return ResultUtil.success(actionListDto);
    }
    */

    @Override
    public Result getPayInfo(HttpServletResponse response, GetOrderInfoRequest getOrderInfoRequest) {
        try {
            log.info("getPayInfo getOrderInfoRequest:{{}}",getOrderInfoRequest);
            return frontInfoFeignClient.getPayInfo(response,getOrderInfoRequest);
        } catch (Exception e) {
            System.err.println("??????????????????????????????" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Result tradeQuery(String outTradeNo, String tradeNo) {
        try {
            return frontInfoFeignClient.tradeQuery(outTradeNo,tradeNo);
        } catch (Exception e) {
            System.err.println("??????????????????????????????" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Result createOfficialAccountUnifiedOrder(String openid,String orderNo,BigDecimal amount) {
        try {
            log.info("createOfficialAccountUnifiedOrder openid:{{}}",openid);
            log.info("createOfficialAccountUnifiedOrder orderNo:{{}}",orderNo);
            log.info("createOfficialAccountUnifiedOrder amount:{{}}",amount);
            return frontInfoFeignClient.createOfficialAccountUnifiedOrder(openid,orderNo,amount);
        } catch (Exception e) {
            System.err.println("??????????????????????????????" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest) {
        return frontInfoFeignClient.parseOrderNotifyResult(parseOrderNotifyRequest);
    }

    @Override
    public Result alipayCallback(String outBizNo, String orderId) {
        return frontInfoFeignClient.alipayCallback(outBizNo,orderId);
    }


    @Override
    public GoodsEntity insertReturnEntity(GoodsEntity entity) {
        return null;
    }

    @Override
    public GoodsEntity updateReturnEntity(GoodsEntity entity) {
        return null;
    }
}
