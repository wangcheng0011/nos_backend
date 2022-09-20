package com.knd.front.social.dto;

import com.knd.front.pay.dto.ImgDto;
import lombok.Data;

import java.util.List;

@Data
public class UserAttachDto {

    private List<ImgDto> attachList;
}
