package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import lombok.Data;

@Data
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
    private String admissionTime;
    private String graduationTtime;
    private String introduce;
}
