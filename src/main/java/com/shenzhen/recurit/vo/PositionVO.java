package com.shenzhen.recurit.vo;

import lombok.Data;

@Data
public class PositionVO extends BaseVO{

    private int id;
    private String  positionName;
    private String  positionCity;
    private String salary;
    private String academicDegree;
    private String experience;
    private String companyCode;
    private int sexReq;
    private int status;
    private String labels;

}
