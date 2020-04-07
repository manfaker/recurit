package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户职位信息关联关系表")
@Data
public class PositionUserRelationVO extends BaseVO{

    @ApiModelProperty(value = "职位信息id")
    private int positionId;
    @ApiModelProperty(value = "用户唯一编码")
    private String userCode;
    @ApiModelProperty(value = "1 关注 2 投递申请")
    private int relationStatus;
    @ApiModelProperty(value = "1 新增 2 删除")
    private int status;
    @ApiModelProperty(value = "多个职位信息id,以逗号分隔")
    private String positionIds;
}
