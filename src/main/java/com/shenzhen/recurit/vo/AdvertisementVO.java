package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.pojo.DocumentPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api(value = "公告")
public class AdvertisementVO extends BaseVO {

    @ApiModelProperty("主键")
    private Integer id = 0;
    @ApiModelProperty("状态")
    private Integer status = 0;
    @ApiModelProperty("广告名称")
    private String advertisementName;
    @ApiModelProperty("图片ID")
    private Integer documentId = 0;

}