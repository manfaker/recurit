package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "地址表")
public class AddressVO extends BaseVO{
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "地址坐标")
    private String location;
    @ApiModelProperty(value = "地址级别 province省，city 市  ，county 县/区,street 街道")
    private String addressLevel;
    @ApiModelProperty(value = "名称")
    private String addressName;
    @ApiModelProperty(value = "行政区代码")
    private String addressNum;
    @ApiModelProperty(value = "父级行政区")
    private String parentNum;
    @ApiModelProperty(value = "邮编")
    private String addressPostal;
    @ApiModelProperty(value = "简称")
    private String abbreviation;

}
