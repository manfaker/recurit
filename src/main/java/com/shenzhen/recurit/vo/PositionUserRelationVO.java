package com.shenzhen.recurit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户职位信息关联关系表")
@Data
public class PositionUserRelationVO extends BaseVO{

    @ApiModelProperty(value = "主键")
    private int id;
    @ApiModelProperty(value = "职位信息id")
    private int positionId;
    @ApiModelProperty(value = "用户唯一编码")
    private String userCode;
    @ApiModelProperty(value = "1 取消关注 2 关注")
    private int follow;
    @ApiModelProperty(value = "1 未申请 2 已申请")
    private int apply;
    @ApiModelProperty(value = "1 未查看 2已查看")
    private int isChecked;
    @ApiModelProperty(value = "1 新增 2 删除")
    private int status;
    @ApiModelProperty(value = "多个职位信息id,以逗号分隔")
    private String positionIds;
    @ApiModelProperty(value = "是否接受")
    private int isAccept;
}
