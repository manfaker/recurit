package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.utils.EmptyUtils;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "浏览记录")
public class BrowseRecordVO extends BaseVO{
    private int id;
    private int positionId ;
    private String userCode;
    private String  companyCode;

}
