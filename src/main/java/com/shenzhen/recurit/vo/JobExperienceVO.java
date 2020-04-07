package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作经历")
public class JobExperienceVO extends BaseVO{
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "用户")
    private String userCode;
    @ApiModelProperty(value = "公司")
    private String companyCode;
    @ApiModelProperty(value = "行业")
    private String profession;
    @ApiModelProperty(value = "部门")
    private String department;
    @ApiModelProperty(value = "职位名称")
    private String professionName;
    @ApiModelProperty(value = "职位类型")
    private String professionCategory;
    @ApiModelProperty(value = "工作开始时间")
    private String startTime;
    @ApiModelProperty(value = "工作结束时间")
    private String endTime;
    @ApiModelProperty(value = "工作内容")
    private String content;
    @ApiModelProperty(value = "业绩")
    private String achievement;
}
