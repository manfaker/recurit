package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import lombok.Data;

@Data
public class PositionPojo extends BaseVO{

    private int id;
    private String positionName;
    private int salary;
    private String academicDegree;
    private int experience;
    private String companyId;
    private int level;
    private int yealSal;
    private int smallint;
    private int status;

}
