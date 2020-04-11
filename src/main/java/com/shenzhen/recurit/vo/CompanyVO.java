package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2020/3/29
 * LiXiaoWei
 */
@Data
public class CompanyVO {
    private int id;
    private String companyCode;   //SH+时间搓
    private String companyName;  //公司名称
    private String legalRepresentative; //法人代表
    private String companyInfo;  //公司详细信息
    private String icon;   //图片
    private String province;  //省
    private String city;  //市
    private String county;  //县/区
    private String address;  //详细地址
    private String userCode; //创建人的userCode
    private int status; //状态  1 正常，2作废，3待审核
    @ApiModelProperty(value = "融资")
    private String financing;
    @ApiModelProperty(value = "规模")
    private String scale;
}
