package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "基础类")
public class BaseVO {

    @ApiModelProperty(value = "创建人")
    private String creater;
    @ApiModelProperty(value = "修改人")
    private String updater;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;  //创建时间
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;  //修改时间

}
