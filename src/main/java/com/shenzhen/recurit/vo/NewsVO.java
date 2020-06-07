package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.pojo.DocumentPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "新闻板块")
public class NewsVO {

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "图片路径")
    private String newsImagePath;
    @ApiModelProperty(value = "新闻标题")
    private String  newsTitle;
    @ApiModelProperty(value = "图片关联id")
    private int documentId;
    @ApiModelProperty(value = "新闻路径")
    private String newsPath;
    @ApiModelProperty(value = "来源")
    private String source;

    private DocumentPojo documentPojo;

}
