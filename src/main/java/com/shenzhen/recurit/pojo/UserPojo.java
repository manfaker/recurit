package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserPojo extends BaseVO{
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
    private ResumePojo resumePojo;//简历信息
    private List<JobExperiencePojo> listJobExperiences;//简历信息
    private List<DesiredPositionPojo> listDesiredPosition;//期望职位
    private List<EducationExperiencesPojo> listEducationExperience;//个人教育
    private String image;//头像信息
    private String likeJob; //意向职位


}
