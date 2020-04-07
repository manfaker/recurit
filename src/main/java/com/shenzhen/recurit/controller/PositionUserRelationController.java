package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.service.PositionUserRelationService;
import com.shenzhen.recurit.vo.PositionUserRelationVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("relation")
@Api(tags = {"用户职位关联关系表"})
public class PositionUserRelationController {
    @Resource
    private PositionUserRelationService positionUserRelationService;

    @ApiOperation(value = "批量保存信息")
    @PermissionVerification
    @PostMapping(value = "saveBatchRelation")
    public ResultVO saveBatchRelation(@RequestBody @ApiParam PositionUserRelationVO positionUserRelationVO){
        return positionUserRelationService.saveBatchRelation(positionUserRelationVO.getPositionIds(),positionUserRelationVO.getRelationStatus());
    }

    @ApiOperation(value = "批量删除信息")
    @PermissionVerification
    @DeleteMapping (value = "deleteBatchRelation")
    public ResultVO deleteBatchRelation(@RequestBody @ApiParam PositionUserRelationVO positionUserRelationVO){
        return positionUserRelationService.deleteBatchRelation(positionUserRelationVO.getPositionIds(),positionUserRelationVO.getRelationStatus());
    }
}
