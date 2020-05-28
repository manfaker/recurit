package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("套餐折扣")
public class ActivityPackageVO extends BaseVO{
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "服务期数")
    private String servicePeriod;
    @ApiModelProperty(value = "单价")
    private int price;
    @ApiModelProperty(value = "折扣")
    private int discount;
    @ApiModelProperty(value = "团体价格")
    private int groupPrice;
    @ApiModelProperty(value = "公司价格+")
    private int enterprisePrice;
    @ApiModelProperty(value = "广价")
    private int promotePrice;
}
