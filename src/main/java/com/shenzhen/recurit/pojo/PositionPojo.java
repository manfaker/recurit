package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "职位信息显示信息")
public class PositionPojo extends BaseVO {
    private int  id;
    private String  positionName;
    private String  positionCity;
    private String  positionAddress;
    private String  salary;
    private String  education;
    private String  experience;
    private DictionaryVO salaryDict;
    private DictionaryVO  academicDegreeDict;
    private DictionaryVO  experienceDict;
    private String  companyCode;
    private String companyName;
    private String  sexReq;
    private String  status;
    private List<LabelVO> listLabel;
    private String userCode;
    private UserVO userVO;
    private String description;
    private String skillRequirement;
    private String location;
    private int follow;
    private int apply;
    private int userId;
    private String userRealName;
    @ApiModelProperty(value = "融资")
    private String financing;
    @ApiModelProperty(value = "规模")
    private String scale;
    @ApiModelProperty(value = "融资详细信息")
    private DictionaryVO financingDict;
    @ApiModelProperty(value = "规模详细信息")
    private DictionaryVO  scaleDict;
    @ApiModelProperty(value = "当前职位投递人数")
    private Integer pisitionCount;

}

