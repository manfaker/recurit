package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "简历")
public class ResumeVO extends BaseVO{

    private int id ;
    @ApiModelProperty(value = "用户code")
    private String userCode;
    @ApiModelProperty(value = "户籍 省")
    private String domicileProvince;
    @ApiModelProperty(value = "户籍 市")
    private String domicileCity;
    @ApiModelProperty(value = "户籍 县")
    private String domicileCounty;
    @ApiModelProperty(value = "户籍 详细地址")
    private String domicileAddr;
    @ApiModelProperty(value = "居住地 省")
    private String residenceProvince;
    @ApiModelProperty(value = "居住地 市")
    private String residenceCity;
    @ApiModelProperty(value = "居住地 县")
    private String residenceCounty;
    @ApiModelProperty(value = "居住地 详细地址")
    private String residenceAddr;
    @ApiModelProperty(value = "工作经验")
    private int experience;
    @ApiModelProperty(value = "学历")
    private String education;
    @ApiModelProperty(value = "工作状态")
    private String jobStatus;
    @ApiModelProperty(value = "薪资要求")
    private String salary;
    @ApiModelProperty(value = "所在城市")
    private String city;
    @ApiModelProperty(value = "行业")
    private String profession;
    @ApiModelProperty(value = "入学时间")
    private Date admissionTime;
    @ApiModelProperty(value = "毕业时间")
    private Date graduationTime;
    @ApiModelProperty(value = "自我介绍")
    private String introduce;
    @ApiModelProperty(value = "工作年限")
    private int workingLife;
}
