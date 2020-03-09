package com.shenzhen.recurit.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BaseVO {

    private String creater;
    private String updater;
    private Date createDate;  //创建时间
    private Date updateDate;  //修改时间

}
