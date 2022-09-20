package com.knd.front.social.service;

import com.knd.front.social.dto.MomentAddressDto;

public interface MomentAddressService {

    String add(MomentAddressDto request, String momentId);
}
