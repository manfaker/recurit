package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import com.shenzhen.recurit.vo.LabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "工作经历")
public class JobExperiencePojo extends BaseVO {
    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "用户编码")
    private String userCode;
    @ApiModelProperty(value = "公司编码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "职位")
    private String profession;
    @ApiModelProperty(value = "部门")
    private String department;
    private String professionName;
    private String professionCategory;
    private Date startTime;
    private Date endTime;
    private String content;
    private String achievement;
    private List<LabelVO> listLabel;
    private String projectTime;
}
