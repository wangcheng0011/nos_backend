package com.knd.front.login.request;

import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/1
 * @Version 1.0
 */
@Data
public class UserDetailRequest {

    private String password;

    private String nickName;

    private String userId;
    @NotBlank(message = "性别不能为空")
    private String gender;
    @NotBlank(message = "生日不能为空")
    private String birthDay;
    @NotBlank(message = "身高不能为空")
    @Size(max = 8,message = "身高输入过长,请重新输入")
    private String height;
    @NotBlank(message = "体重不能为空")
    @Size(max = 8,message = "体重输入过长,请重新输入")
    private String weight;
    @NotBlank(message = "bmi不能为空")
    private String bmi;
    //@NotBlank(message = "健身基础不能为空")
    private String trainHisFlag = "0";
    //@NotBlank(message = "健身目标Id不能为空")
    private String targetId;
    //@NotBlank(message = "健身目标不能为空")
    private String target;
    //@NotBlank(message = "体型id不能为空")
    private String shapeId;

   // @NotBlank(message = "爱好id不能为空")
    //支持多选，中间用逗号隔开
    private String hobbyId;
    private String sportId;
    private String frequencyId;
    @Length(max=20,message = "个性签名不能超过20个字")
    private String perSign;
    private String headPicUrlId;

   // @NotBlank(message = "头像图片不能为空")
    @ApiModelProperty(value = "头像图片")
    private VoUrl headAttachUrl;
}