package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "导出")
public class ExportsVO {

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "导出字段")
    private String columnField;
    @ApiModelProperty(value = "导出名称")
    private String columnName;
    @ApiModelProperty(value = "顺序")
    private int gradation;
    @ApiModelProperty(value = "高")
    private int hight;
    @ApiModelProperty(value = "宽")
    private int width;
    @ApiModelProperty(value = "颜色")
    private int colour;
    @ApiModelProperty(value = "实列名")
    private String instanceName;
}
