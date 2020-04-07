package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import lombok.Data;

@Data
public class JobExperiencePojo extends BaseVO {
    private int id;
    private String userCode;
    private String companyCode;
    private String companyName;
    private String profession;
    private String department;
    private String professionName;
    private String professionCategory;
    private String startTime;
    private String endTime;
    private String content;
    private String achievement;
}
