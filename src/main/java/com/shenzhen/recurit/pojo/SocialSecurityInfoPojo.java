package com.shenzhen.recurit.pojo;

import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "个人社保")
public class SocialSecurityInfoPojo extends BaseVO {

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "社保所在城市id")
    private int cityId;
    @ApiModelProperty(value = "社保所在城市")
    private SocialStandardPojo socialStandardPojo;
    @ApiModelProperty(value = "申请人姓名")
    private String applyName;
    @ApiModelProperty(value = "身份证号码")
    private String idCard;
    @ApiModelProperty(value = "身份证正面")
    private int positiveIdCard;
    @ApiModelProperty(value = "身份证正面Base64")
    private String positiveIdCardInfo;
    @ApiModelProperty(value = "身份证反面")
    private int reverseIdCard;
    @ApiModelProperty(value = "身份证反面Base64")
    private String reverseIdCardInfo;
    @ApiModelProperty(value = "手机号码")
    private String phone;
    @ApiModelProperty(value = "户籍所在地")
    private String domicile;
    @ApiModelProperty(value = "户籍性质")
    private String natureRegist;
    @ApiModelProperty(value = "基数")
    private int cardinality;
    @ApiModelProperty(value = "参保日期")
    private Date socialSecurityDate;
    @ApiModelProperty(value = "套餐Object")
    private List<ActivityPackagePojo> listPackage;
    @ApiModelProperty(value = "1 正常 2 删除")
    private int status;
    @ApiModelProperty(value = "订单id")
    private int orderInfoId;
    @ApiModelProperty(value = "用户CODE")
    private String userCode;
    @ApiModelProperty(value = "参保结束日期")
    private Date socialSecurityEndDate;
    @ApiModelProperty(value = "套餐")
    private String feePackageIds;
    @ApiModelProperty(value = "计算所得总金额")
    private int allCountMoney;
    @ApiModelProperty(value = "计算统计")
    private JSONObject caculatePrice;
    @ApiModelProperty(value = "总金额")
    private int countMoney;
    @ApiModelProperty(value = "单月社保金额")
    private int socialSecurityPrice;
}
