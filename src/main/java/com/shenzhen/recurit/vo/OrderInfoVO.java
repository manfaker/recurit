package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "订单信息")
@Data
public class OrderInfoVO  extends BaseVO{
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "支付订单编号")
    private String outTradeNo;
    @ApiModelProperty(value = "支付宝交易号")
    private String tradeNo;
    @ApiModelProperty(value = "支付金额")
    private int   price;
    @ApiModelProperty(value = "支付状态 1 未支付，2已支付，3订单流程已走完，4退款")
    private int payStatus;
    @ApiModelProperty(value = "总金额")
    private int totalAmount;
    @ApiModelProperty(value = "打折金额")
    private int discountablemount;
    @ApiModelProperty(value = "折扣")
    private int discount;
    @ApiModelProperty(value = "1.正常 2 删除")
    private int status;
    @ApiModelProperty(value = "主题")
    private String subject;
    @ApiModelProperty(value = "描述")
    private String body;
    @ApiModelProperty(value = "退款金额")
    private long refundAmount;
    @ApiModelProperty(value = "退款场景")
    private String bizType;
    @ApiModelProperty(value = "用户信息")
    private String userCode;
    @ApiModelProperty(value = "社保ids以逗号隔开")
    private String socialInfoIds;


}
