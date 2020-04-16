package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import com.shenzhen.recurit.vo.LabelVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "工作经历")
public class JobExperiencePojo extends BaseVO {
    private int id;
    private String userCode;
    private String companyCode;
    private String companyName;
    private String profession;
    private String department;
    private String professionName;
    private String professionCategory;
    private Date startTime;
    private Date endTime;
    private String content;
    private String achievement;
    private List<LabelVO> listLabel;
    private String projectTime;
}
