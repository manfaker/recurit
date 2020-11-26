package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api(value = "公告")
public class NoticePojo extends BaseVO {
    @ApiModelProperty("主键")
    private Integer id = 0;
    @ApiModelProperty("公告内容")
    private String noticeContent;
    @ApiModelProperty("公告顺序")
    private Integer noticeOrder = 0;
    @ApiModelProperty("状态")
    private Integer status = 0;
}
