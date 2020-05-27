package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "社保比例")
public class SocialStandardPojo extends BaseVO {
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "所在城市")
    private String city;
    @ApiModelProperty(value = "最低基数")
    private int lowCardinality;
    @ApiModelProperty(value = "最高基数")
    private int hightCardinality;
    @ApiModelProperty(value = "企业养老保险比例")
    private int enterprisePension;
    @ApiModelProperty(value = "个人养老保险比例")
    private int personPension;
    @ApiModelProperty(value = "企业医疗保险比列")
    private int enterpriseMedical;
    @ApiModelProperty(value = "个人医疗保险比列")
    private int personMedical;
    @ApiModelProperty(value = "企业失业保险比例")
    private int enterpriseUnemployment;
    @ApiModelProperty(value = "个人失业保险比例")
    private int personUnemployment;
    @ApiModelProperty(value = "企业工伤保险比例")
    private int enterpriseInjury;
    @ApiModelProperty(value = "个人工伤保险比例")
    private int personInjury;
    @ApiModelProperty(value = "企业生育保险比例")
    private int enterpriseChildbirth;
    @ApiModelProperty(value = "个人生育保险比例")
    private int personChildbirth;
    @ApiModelProperty(value = "残保金")
    private int disabilityInsurance;

}