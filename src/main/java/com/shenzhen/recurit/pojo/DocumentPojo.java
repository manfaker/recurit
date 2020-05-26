package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "文档信息")
public class DocumentPojo extends BaseVO {

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "旧名称")
    private String oldName;
    @ApiModelProperty(value = "文档名称")
    private String documentName;
    @ApiModelProperty(value = "文档大小")
    private int documentSize;
    @ApiModelProperty(value = "路径")
    private String url;
    @ApiModelProperty(value = "后缀名")
    private String suffix;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "状态 1 正常 2删除")
    private String status;

}