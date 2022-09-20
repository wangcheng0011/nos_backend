package com.knd.manage.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.mall.dto.GoodsDto;
import com.knd.manage.mall.entity.AttrEntity;
import com.knd.manage.mall.entity.CategoryEntity;
import com.knd.manage.mall.mapper.AttrMapper;
import com.knd.manage.mall.mapper.CategoryMapper;
import com.knd.manage.mall.request.CreateAttrRequest;
import com.knd.manage.mall.request.UpdateAttrRequest;
import com.knd.manage.mall.service.ICategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
@Service
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements ICategoryService {

    @Resource
    private AttrMapper attrMapper;

//    @Override
//    public Result add(CreateAttrRequest createAttrRequest) {
//        List<String> attrNameList = createAttrRequest.getAttrNameList();
//        int i = 1;
//        for(String e : attrNameList){
//            AttrEntity attrEntity = new AttrEntity();
//            attrEntity.setAttrName(e);
//            attrEntity.setCategoryId(createAttrRequest.getCategoryId());
//            attrEntity.setSort(i+"");
//            attrEntity.setCreateDate(LocalDateTime.now());
//            attrEntity.setCreateBy(createAttrRequest.getUserId());
//            attrEntity.setLastModifiedDate(LocalDateTime.now());
//            attrEntity.setLastModifiedBy(createAttrRequest.getUserId());
//            attrEntity.setDeleted("0");
//            baseMapper.insert(attrEntity);
//            i++;
//        }
//        return null;
//    }



    @Override
    public Result getAttrByCategoryId(String categoryId) {
        List<AttrEntity> attrEntities = attrMapper.selectList(new QueryWrapper<AttrEntity>().eq("categoryId", categoryId)
                .eq("deleted", "0").orderByAsc("sort"));
        return ResultUtil.success(attrEntities);
    }

    @Override
    public Result getCategoryList(String categoryName, String current) {
        if(StringUtils.isEmpty(categoryName)&&StringUtils.isEmpty(current)) {
            return ResultUtil.success(baseMapper.selectList(null));
        }else{
            //分页
            Page<CategoryEntity> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
            Page<CategoryEntity> p = this.baseMapper.selectPage(partPage,
                    new QueryWrapper<CategoryEntity>().like("categoryName",(categoryName == null ? "" : categoryName)));

            return ResultUtil.success(p);
        }

    }

    @Override
    public Result addAttr(UpdateAttrRequest vo) {

//        attrMapper.delete(new QueryWrapper<AttrEntity>().eq("categoryId", vo.getCategoryId())
//                .eq("deleted", "0"));
//        int i = 1;
//        for(String s : vo.getAttrNameList()) {
//            AttrEntity e = new AttrEntity();
//            e.setCategoryId(vo.getCategoryId());
//            e.setAttrName(s);
//            e.setSort(i+"");
//            e.setCreateDate(LocalDateTime.now());
//            e.setCreateBy(vo.getUserId());
//            e.setLastModifiedDate(LocalDateTime.now());
//            e.setLastModifiedBy(vo.getUserId());
//            e.setDeleted("0");
//            attrMapper.insert(e);
//            i++;
//        }
            AttrEntity e = new AttrEntity();
            e.setCategoryId(vo.getCategoryId());
            e.setAttrName(vo.getAttrName());
            e.setSort(vo.getSort());
            e.setCreateDate(LocalDateTime.now());
            e.setCreateBy(vo.getUserId());
            e.setLastModifiedDate(LocalDateTime.now());
            e.setLastModifiedBy(vo.getUserId());
            e.setDeleted("0");
            attrMapper.insert(e);
        return ResultUtil.success();
    }

    @Override
    public Result updateAttr(UpdateAttrRequest vo) {
        AttrEntity attrEntity = attrMapper.selectById(vo.getId());
        if(attrEntity != null) {
            attrEntity.setAttrName(vo.getAttrName());
            attrEntity.setSort(vo.getSort());
            attrEntity.setLastModifiedDate(LocalDateTime.now());
            attrEntity.setLastModifiedBy(vo.getUserId());
            attrMapper.updateById(attrEntity);
        }
        return ResultUtil.success();

    }

    @Override
    public Result deleteAttr(String id) {
        attrMapper.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public CategoryEntity insertReturnEntity(CategoryEntity entity) {
        return null;
    }

    @Override
    public CategoryEntity updateReturnEntity(CategoryEntity entity) {
        return null;
    }
}
