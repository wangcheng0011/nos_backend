package com.knd.manage.mall.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author will
 */
@Data
public class CreateGoodsRequest {
    //userId从token获取
    private String userId;

    @NotBlank(message = "操作类型不可空")
    @Pattern(regexp = "^(1|2)$", message = "操作类型错误")
    private String postType;
    @Size(max = 64, message = "最大长度为64")
    private String id;
    @NotBlank(message = "动作名称不可空")
    @Size(max = 32, message = "最大长度为32")
    private String goodsName;

    @NotBlank(message = "商品类型 0-虚拟 1-实物")
    @Pattern(regexp = "^(0|1)$", message = "商品类型只能是 0-虚拟 1-实物")
    private String goodsType;
    //非必传
    @Size(max = 256, message = "商品描述最大长度为256")
    private String goodsDesc;
    @NotBlank(message = "分类id不可空")
    @Size(max = 64, message = "分类id最大长度为64")
    private String categoryId;

    @NotBlank(message = "是否需要配送安装:0-否1-是")
    private String  installFlag;

    @Size(max = 256, message = "最大长度为256")
    //封面图片原名称
    private String picAttachName;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //封面图片新名称
    private String picAttachNewName;
    //    @NotBlank
    @Size(max = 32, message = "最大长度为32")
    //封面图片大小
    private String picAttachSize;

   // @Pattern(regexp = "^(([1-9]\\\\d*)|([0]))(\\\\.(\\\\d){2})?$", message = "价格格式不正确")
    private String price;

    @Valid
    private List<GoodsAttrRequest> attrList;

    @NotEmpty(message="商品介绍图不能为空")
    private List<GoodsHeadImageRequest> headImgList;

    @NotEmpty(message="商品详情图不能为空")
    private List<GoodsInfoImageRequest> infoImgList;





}
