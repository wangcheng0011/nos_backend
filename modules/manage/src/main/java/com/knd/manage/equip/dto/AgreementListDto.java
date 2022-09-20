package com.knd.manage.equip.dto;


import com.knd.manage.equip.entity.Agreement;
import lombok.Data;

import java.util.List;

@Data
public class AgreementListDto {
    private int total;
    private List<Agreement> agreementList;

}
