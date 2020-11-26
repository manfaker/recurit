package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.utils.EncryptBase64Utils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "用户信息")
public class UserVO extends BaseVO{
    private int id;
    private String userName; //用户登录名
    private String realName;//真实姓名
    private String phone;//手机
    private String fixedPhone;//座机
    private String email;//邮件
    private String password;//密码
    private String contactser;//联系人
    private String roleNum; //关联角色
    private String roleContent;//角色描述
    private String roleName; //角色名称
    private String sexNum; //性别00未知 01 男 02 女
    private String sex;  //性别
    private int age; //年龄
    private Date birth; //生日
    private String nickName; //昵称
    private String entryCode;//动态识别码
    private int status;//动态识别码
    private String companyCode;//公司编码
    private String userCode;//用户编码
    @ApiModelProperty(value = "用户信息")
    private String image;//头像信息
    @ApiModelProperty(value = "意向职位")
    private String likeJob;
    @ApiModelProperty(value = "工作经验")
    private String jobExperience;
    @ApiModelProperty(value = "现居住地址")
    private String residenceAddress;
    @ApiModelProperty(value = "学历Code")
    private String education;
    @ApiModelProperty(value = "学校名称")
    private String schoolName;
    @ApiModelProperty(value = "薪资code")
    private String salary;
    @ApiModelProperty(value = "职位")
    private String profession;
    @ApiModelProperty(value = "职位/简介")
    private String desiredPosition;



}
