package com.shenzhen.recurit.vo;

import lombok.Data;

@Data
public class PositionVO extends BaseVO{

    private int id;
    private String  positionName;
    private String  positionCity;
    private String  positionAddress;
    private String salary;
    private String experience;
    private String companyCode;
    private String sexReq;
    private int status;
    private String labels;
    private String userCode;
    private String description;
    private String skillRequirement;
    private String education;
}
