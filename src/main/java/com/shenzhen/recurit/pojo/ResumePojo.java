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
    @ApiModelProperty(value = "主键id")
    private int id ;
    @ApiModelProperty(value = "用户编码")
    private String userCode;
    @ApiModelProperty(value = "户籍省")
    private String domicileProvince;
    @ApiModelProperty(value = "户籍市")
    private String domicileCity;
    @ApiModelProperty(value = "户籍县")
    private String domicileCounty;
    @ApiModelProperty(value = "户籍详细地址")
    private String domicileAddr;
    @ApiModelProperty(value = "居住省")
    private String residenceProvince;
    @ApiModelProperty(value = "居住市")
    private String residenceCity;
    @ApiModelProperty(value = "居住县")
    private String residenceCounty;
    @ApiModelProperty(value = "居住详细地址")
    private String residenceAddr;
    @ApiModelProperty(value = "个人经历code")
    private String experience;
    @ApiModelProperty(value = "个人经历")
    private DictionaryVO experienceDict;
    @ApiModelProperty(value = "学历code")
    private String education;
    @ApiModelProperty(value = "学历")
    private DictionaryVO educationDict;
    @ApiModelProperty(value = "在职状态")
    private String jobStatus;
    @ApiModelProperty(value = "期望薪资code")
    private String salary;
    @ApiModelProperty(value = "期望薪资")
    private DictionaryVO salaryDict;
    @ApiModelProperty(value = "心仪城市")
    private String city;
    @ApiModelProperty(value = "专业技能")
    private String profession;
    @ApiModelProperty(value = "入学时间")
    private Date admissionTime;
    @ApiModelProperty(value = "毕业时间")
    private Date graduationTtime;
    @ApiModelProperty(value = "介绍")
    private String introduce;
    @ApiModelProperty(value = "工作年限")
    private int workingLife;
    @ApiModelProperty(value = "用户名")
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
