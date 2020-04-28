package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("数据字典")
public class DictionaryVO extends BaseVO{

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "编号")
    private String dictNum;   //编号
    @ApiModelProperty(value = "类别")
    private String category;  //类别
    @ApiModelProperty(value = "名称")
    private String dictName;  //名称
    @ApiModelProperty(value = "内容")
    private String content;   //内容
    @ApiModelProperty(value = "上级编码")
    private String superNum;  //上级编码
    @ApiModelProperty(value = "子集")
    private List<DictionaryVO> children;

}
