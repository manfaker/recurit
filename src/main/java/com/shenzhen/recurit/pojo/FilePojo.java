package com.shenzhen.recurit.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FilePojo {

    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件大小")
    private int size;
    @ApiModelProperty(value = "后缀名")
    private String suffix;
    @ApiModelProperty(value = "相对路径")
    private String pelativePath;
    @ApiModelProperty(value = "绝对路径")
    private String realPath;
    @ApiModelProperty(value = "旧名称")
    private String oldName;
}
