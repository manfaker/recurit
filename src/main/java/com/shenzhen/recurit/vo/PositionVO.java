package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "职位信息")
public class PositionVO extends BaseVO{

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "职位名称")
    private String  positionName;
    @ApiModelProperty(value = "所在城市")
    private String  positionCity;
    @ApiModelProperty(value = "公司地址")
    private String  positionAddress;
    @ApiModelProperty(value = "薪资水平")
    private String salary;
    @ApiModelProperty(value = "工作经验")
    private String experience;
    @ApiModelProperty(value = "公司编码")
    private String companyCode;
    @ApiModelProperty(value = "性别要求")
    private String sexReq;
    @ApiModelProperty(value = "状态")
    private int status;
    @ApiModelProperty(value = "标签名称")
    private String labels;
    @ApiModelProperty(value = "发布者信息")
    private String userCode;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "技能需求")
    private String skillRequirement;
    @ApiModelProperty(value = "学历")
    private String education;
    @ApiModelProperty(value = "是否关注")
    private int follow;
    @ApiModelProperty(value = "是否申请")
    private int apply;
    @ApiModelProperty(value = "定位")
    private String location;
    @ApiModelProperty(value = "融资")
    private String financing;
    @ApiModelProperty(value = "规模")
    private String scale;
}
