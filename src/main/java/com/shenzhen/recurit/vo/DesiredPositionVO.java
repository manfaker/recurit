package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(value = "期望职位")
public class DesiredPositionVO extends BaseVO{

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "用户code")
    private String userCode;
    @ApiModelProperty(value = "期望职位")
    private String desiredPosition;
    @ApiModelProperty(value = "期望薪资")
    private String salary;
    @ApiModelProperty(value = "行业")
    private String profession;
    @ApiModelProperty(value = "城市")
    private String  city;
}
