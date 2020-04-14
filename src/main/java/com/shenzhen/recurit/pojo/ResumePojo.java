package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "简历详细信息")
public class ResumePojo extends BaseVO {
    private int id ;
    private String userCode;
    private String domicileProvince;
    private String domicileCity;
    private String domicileCounty;
    private String domicileAddr;
    private String residenceProvince;
    private String residenceCity;
    private String residenceCounty;
    private String residenceAddr;
    private int experience;
    private String education;
    private String jobStatus;
    private String salary;
    private String city;
    private String profession;
    private Date admissionTime;
    private Date graduationTtime;
    private String introduce;
    @ApiModelProperty(value = "工作年限")
    private int workingLife;
}
