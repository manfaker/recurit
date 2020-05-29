package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("套餐折扣")
public class ActivityPackagePojo extends BaseVO {
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
    @ApiModelProperty(value = "企业价格")
    private int enterprisePrice;
    @ApiModelProperty(value = "推广广价")
    private int promotePrice;
    @ApiModelProperty(value = "期数")
    private int amount;
}
