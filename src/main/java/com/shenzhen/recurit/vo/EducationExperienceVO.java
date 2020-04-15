package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "教育经历")
public class EducationExperienceVO extends BaseVO{

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "主键")
    private String userCode;
    @ApiModelProperty(value = "学校名称")
    private String schoolName;
    @ApiModelProperty(value = "全日制类型")
    private String category;
    @ApiModelProperty(value = "专业")
    private String major;
    @ApiModelProperty(value = "学校经历")
    private String associationActivity;
    @ApiModelProperty(value = "开学时间")
    private Date startTime;
    @ApiModelProperty(value = "毕业时间")
    private Date endTime;
    @ApiModelProperty(value = "学历")
    private String experience;
}
