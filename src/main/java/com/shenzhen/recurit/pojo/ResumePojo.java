package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import com.shenzhen.recurit.vo.DictionaryVO;
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
    private String experience;
    private DictionaryVO experienceDict;
    private String education;
    private DictionaryVO educationDict;
    private String jobStatus;
    private String salary;
    private DictionaryVO salaryDict;
    private String city;
    private String profession;
    private Date admissionTime;
    private Date graduationTtime;
    private String introduce;
    @ApiModelProperty(value = "工作年限")
    private int workingLife;
    private String userName;
    private int positionId;
    private String phone;
    private String positionName;
    private String email;
    private  int isChecked;
    private int isAccept;
    @ApiModelProperty(value = "总简历数")
    private  int resumeSum;
    @ApiModelProperty(value = "未查看简历数")
    private  int noCheckedSum;



}
