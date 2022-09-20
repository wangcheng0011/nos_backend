package com.knd.front.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.entity.AmapAdcodeEntity;
import com.knd.front.entity.UserReceiveAddressEntity;
import com.knd.front.train.mapper.AmapAdcodeMapper;
import com.knd.front.user.service.IUserReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


/**
 * 会员收货地址
 *
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-08-06 16:14:34
 */
@RestController
@CrossOrigin
@RequestMapping("front/userReceiveAddress")
@Api(tags = "userReceiveAddress")
@Log4j2
public class UserReceiveAddressController {
    @Resource
    private IUserReceiveAddressService iUserReceiveAddressService;

    @Resource
    private AmapAdcodeMapper amapAdcodeMapper;

    /**
     * 列表
     */
    @ApiOperation(value = "获取会员收货地址列表", notes = "获取会员收货地址列表")
    @Log("获取会员收货地址列表")
    @GetMapping("/list")
    public Result list(@RequestParam(required =false) String userId
            ,@RequestParam(required =false) String phone,@RequestParam(required =false) String defaultStatus){
        log.info("list userId:{{}}",userId);
        log.info("list phone:{{}}",phone);
        log.info("list defaultStatus:{{}}",defaultStatus);
        log.info("list UserId:{{}}",UserUtils.getUserId());
        if(null==UserUtils.getUserId()){
            return ResultUtil.error(ResultEnum.UNAUTHORIZED);
        }
        if(null==defaultStatus){
            List<UserReceiveAddressEntity> list = iUserReceiveAddressService
                    .list(
                            new QueryWrapper<UserReceiveAddressEntity>()
                                    .eq("userId", userId).or(e -> e.eq("phone", phone)));
            log.info("list list:{{}}",list);
            list.stream().forEach(userReceiveAddressEntity->
                    {
                        log.info("list userReceiveAddressEntity:{{}}",userReceiveAddressEntity);
                        //省截取
                        QueryWrapper<AmapAdcodeEntity> provinceQueryWrapper = new QueryWrapper<>();
                        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
                        provinceQueryWrapper.eq("deleted", "0");
                        provinceQueryWrapper.last("limit 1");
                        AmapAdcodeEntity provinceAmapAdCodeEntity = amapAdcodeMapper.selectOne(provinceQueryWrapper);
                        log.info("list provinceAmapAdCodeEntity:{{}}",provinceAmapAdCodeEntity);
                        if(StringUtils.isEmpty(provinceAmapAdCodeEntity)){
                            String[] split = userReceiveAddressEntity.getProvince().split(",");
                            userReceiveAddressEntity.setProvince(split[0]);
                        }
                        //市截取
                        QueryWrapper<AmapAdcodeEntity> cityQueryWrapper = new QueryWrapper<>();
                        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
                        cityQueryWrapper.eq("deleted", "0");
                        cityQueryWrapper.last("limit 1");
                        AmapAdcodeEntity cityAmapAdCodeEntity = amapAdcodeMapper.selectOne(cityQueryWrapper);
                        log.info("list cityAmapAdCodeEntity:{{}}",cityAmapAdCodeEntity);
                        if(StringUtils.isEmpty(cityAmapAdCodeEntity)){
                            String[] split = userReceiveAddressEntity.getCity().split(",");
                            userReceiveAddressEntity.setCity(split[0]);
                        }
                        //区截取
                        QueryWrapper<AmapAdcodeEntity> regionQueryWrapper = new QueryWrapper<>();
                        regionQueryWrapper.eq("name",userReceiveAddressEntity.getRegion());
                        regionQueryWrapper.eq("deleted", "0");
                        regionQueryWrapper.last("limit 1");
                        AmapAdcodeEntity regionAmapAdCodeEntity = amapAdcodeMapper.selectOne(regionQueryWrapper);
                        log.info("list regionAmapAdCodeEntity:{{}}",regionAmapAdCodeEntity);
                        if(StringUtils.isEmpty(regionAmapAdCodeEntity)){
                            String[] split = userReceiveAddressEntity.getRegion().split(",");
                            userReceiveAddressEntity.setRegion(split[0]);
                        }
                    }
                    );

            return ResultUtil.success(list);
        }else {
            List<UserReceiveAddressEntity> list = iUserReceiveAddressService
                    .list(
                            new QueryWrapper<UserReceiveAddressEntity>()
                                    .eq("userId", userId).eq("defaultStatus", defaultStatus).or(e -> e.eq("phone", phone).eq("defaultStatus", defaultStatus)));
            log.info("list list:{{}}",list);
            list.stream().forEach(userReceiveAddressEntity->
                    {
                        //省截取
                        QueryWrapper<AmapAdcodeEntity> provinceQueryWrapper = new QueryWrapper<>();
                        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
                        provinceQueryWrapper.eq("deleted", "0");
                        provinceQueryWrapper.last("limit 1");
                        AmapAdcodeEntity provinceAmapAdCodeEntity = amapAdcodeMapper.selectOne(provinceQueryWrapper);
                        log.info("list provinceAmapAdCodeEntity:{{}}",provinceAmapAdCodeEntity);
                        if(StringUtils.isEmpty(provinceAmapAdCodeEntity)){
                            String[] split = userReceiveAddressEntity.getProvince().split(",");
                            userReceiveAddressEntity.setProvince(split[0]);
                        }
                        //市截取
                        QueryWrapper<AmapAdcodeEntity> cityQueryWrapper = new QueryWrapper<>();
                        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
                        cityQueryWrapper.eq("deleted", "0");
                        cityQueryWrapper.last("limit 1");
                        AmapAdcodeEntity cityAmapAdCodeEntity = amapAdcodeMapper.selectOne(cityQueryWrapper);
                        log.info("list cityAmapAdCodeEntity:{{}}",cityAmapAdCodeEntity);
                        if(StringUtils.isEmpty(cityAmapAdCodeEntity)){
                            String[] split = userReceiveAddressEntity.getCity().split(",");
                            userReceiveAddressEntity.setCity(split[0]);
                        }
                        //区截取
                        QueryWrapper<AmapAdcodeEntity> regionQueryWrapper = new QueryWrapper<>();
                        regionQueryWrapper.eq("name",userReceiveAddressEntity.getRegion());
                        regionQueryWrapper.eq("deleted", "0");
                        regionQueryWrapper.last("limit 1");
                        AmapAdcodeEntity regionAmapAdCodeEntity = amapAdcodeMapper.selectOne(regionQueryWrapper);
                        log.info("list regionAmapAdCodeEntity:{{}}",regionAmapAdCodeEntity);
                        if(StringUtils.isEmpty(regionAmapAdCodeEntity)){
                            String[] split = userReceiveAddressEntity.getRegion().split(",");
                            userReceiveAddressEntity.setRegion(split[0]);
                        }
                    }
            );
            return ResultUtil.success(list);
        }

    }



    /**
     * 信息
     */
    @ApiOperation(value = "获取会员收货地址详情", notes = "获取会员收货地址详情")
    @Log("获取会员收货地址详情")
    @GetMapping("/info")
    public Result info(@RequestParam("id") String id){
        UserReceiveAddressEntity userReceiveAddressEntity = iUserReceiveAddressService.getById(id);
        //省截取
        QueryWrapper<AmapAdcodeEntity> provinceQueryWrapper = new QueryWrapper<>();
        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
        provinceQueryWrapper.last("limit 1");
        AmapAdcodeEntity provinceAmapAdCodeEntity = amapAdcodeMapper.selectOne(provinceQueryWrapper);
        if(StringUtils.isEmpty(provinceAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getProvince().split(",");
            userReceiveAddressEntity.setProvince(split[0]);
        }
        //市截取
        QueryWrapper<AmapAdcodeEntity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
        cityQueryWrapper.last("limit 1");
        AmapAdcodeEntity cityAmapAdCodeEntity = amapAdcodeMapper.selectOne(cityQueryWrapper);
        if(StringUtils.isEmpty(cityAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getCity().split(",");
            userReceiveAddressEntity.setCity(split[0]);
        }
        //区截取
        QueryWrapper<AmapAdcodeEntity> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("name",userReceiveAddressEntity.getRegion());
        regionQueryWrapper.last("limit 1");
        AmapAdcodeEntity regionAmapAdCodeEntity = amapAdcodeMapper.selectOne(regionQueryWrapper);
        if(StringUtils.isEmpty(regionAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getRegion().split(",");
            userReceiveAddressEntity.setRegion(split[0]);
        }
        return ResultUtil.success(userReceiveAddressEntity);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增会员收货地址", notes = "新增会员收货地址")
    @Log("新增会员收货地址")
    @PostMapping("/save")
    public Result save(@RequestBody UserReceiveAddressEntity userReceiveAddressEntity){

        userReceiveAddressEntity.setUserId(UserUtils.getUserId());
        if("1".equals(userReceiveAddressEntity.getDefaultStatus())) {
            cleanOldDefaultAddress(userReceiveAddressEntity.getUserId());
        }
        if(!"1".equals(userReceiveAddressEntity.getDefaultStatus())&&iUserReceiveAddressService.count(new QueryWrapper<UserReceiveAddressEntity>()
                .eq("userId", userReceiveAddressEntity.getUserId())
                .eq("deleted", "0"))<=0) {
            userReceiveAddressEntity.setDefaultStatus("1");
        }
        //省截取
        QueryWrapper<AmapAdcodeEntity> provinceQueryWrapper = new QueryWrapper<>();
        provinceQueryWrapper.eq("name",userReceiveAddressEntity.getProvince());
        provinceQueryWrapper.last("limit 1");
        AmapAdcodeEntity provinceAmapAdCodeEntity = amapAdcodeMapper.selectOne(provinceQueryWrapper);
        if(StringUtils.isEmpty(provinceAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getProvince().split(",");
            userReceiveAddressEntity.setProvince(split[0]);
        }
        //市截取
        QueryWrapper<AmapAdcodeEntity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("name",userReceiveAddressEntity.getCity());
        cityQueryWrapper.last("limit 1");
        AmapAdcodeEntity cityAmapAdCodeEntity = amapAdcodeMapper.selectOne(cityQueryWrapper);
        if(StringUtils.isEmpty(cityAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getCity().split(",");
            userReceiveAddressEntity.setCity(split[0]);
        }
        //区截取
        QueryWrapper<AmapAdcodeEntity> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("name",userReceiveAddressEntity.getRegion());
        regionQueryWrapper.last("limit 1");
        AmapAdcodeEntity regionAmapAdCodeEntity = amapAdcodeMapper.selectOne(regionQueryWrapper);
        if(StringUtils.isEmpty(regionAmapAdCodeEntity)){
            String[] split = userReceiveAddressEntity.getRegion().split(",");
            userReceiveAddressEntity.setRegion(split[0]);
        }
        iUserReceiveAddressService.save(userReceiveAddressEntity);
        return ResultUtil.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "更新会员收货地址", notes = "更新会员收货地址")
    @Log("更新会员收货地址")
    @PostMapping("/update")
    public Result update(@RequestBody UserReceiveAddressEntity userReceiveAddressEntity){
        userReceiveAddressEntity.setUserId(UserUtils.getUserId());
        UserReceiveAddressEntity old = iUserReceiveAddressService.getById(userReceiveAddressEntity.getId());
        if("0".equals(userReceiveAddressEntity.getDefaultStatus())
                &&"1".equals(old.getDefaultStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该地址是默认地址，请先更换默认收货地址");
        }
        if("0".equals(old.getDefaultStatus())&&"1".equals(userReceiveAddressEntity.getDefaultStatus())) {
            cleanOldDefaultAddress(userReceiveAddressEntity.getUserId());
        }
        iUserReceiveAddressService.edit(userReceiveAddressEntity);
        return ResultUtil.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除会员收货地址", notes = "删除会员收货地址")
    @Log("删除会员收货地址")
    @PostMapping("/delete")
    public Result delete(@RequestBody String[] ids){
        UserReceiveAddressEntity byId = iUserReceiveAddressService.getById(ids[0]);
        if("1".equals(byId.getDefaultStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该地址是默认地址，请先更换默认收货地址");
        }
        iUserReceiveAddressService.removeByIds(Arrays.asList(ids));
        return  ResultUtil.success();
    }

    private void cleanOldDefaultAddress(String userId) {
        UserReceiveAddressEntity one = iUserReceiveAddressService
                .getOne(new QueryWrapper<UserReceiveAddressEntity>()
                        .eq("userId", userId)
                        .eq("defaultStatus", "1")
                        .eq("deleted", "0")
                );

        if(one != null ){
            one.setDefaultStatus("0");
            iUserReceiveAddressService.updateById(one);
        }


    }


}
