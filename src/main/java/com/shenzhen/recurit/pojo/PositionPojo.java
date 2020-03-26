package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.LabelVO;
import lombok.Data;

import java.util.List;

@Data
public class PositionPojo extends BaseVO {
    private int  id;
    private String  positionName;
    private String  positionCity;
    private String  positionAddress;
    private String  salary;
    private String  academicDegree;
    private String  experience;
    private DictionaryVO salaryDict;
    private DictionaryVO  academicDegreeDict;
    private DictionaryVO  experienceDict;
    private String  companyCode;
    private String  sexReq;
    private String  status;
    private List<LabelVO> listLabel;
}

