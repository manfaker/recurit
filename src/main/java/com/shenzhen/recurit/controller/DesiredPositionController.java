package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.DesiredPositionService;
import com.shenzhen.recurit.service.ResumeService;
import com.shenzhen.recurit.vo.DesiredPositionVO;
import com.shenzhen.recurit.vo.JobExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping(value = "desired/position")
@RestController
@Api(tags = {"期望职位信息"})
public class DesiredPositionController {

    @Resource
    private DesiredPositionService desiredPositionService;

    @Resource
    private ResumeService resumeService;

    @ApiOperation(value = "修改期望职位信息")
    @PermissionVerification
    @PostMapping(value = "saveDesiredPosition",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    private ResultVO saveDesiredPosition(@RequestBody @ApiParam(required = true) DesiredPositionVO desiredPositionVO){
        return desiredPositionService.saveDesiredPosition(desiredPositionVO);
    }



    @ApiOperation(value = "删除期望职位信息")
    @PermissionVerification
    @ApiImplicitParam(value = "主键",name = "id",required = true)
    @DeleteMapping(value = "deleteJobExperienceById", produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    private ResultVO deleteDesiredPositionById(int id){
        int result = desiredPositionService.deleteDesiredPositionById(id);
        if(result> NumberEnum.ZERO.getValue()){
            return  ResultVO.success("删除成功");
        }
        return  ResultVO.success("删除失败");
    }

    @ApiOperation(value = "修改期望职位信息")
    @PermissionVerification
    @PutMapping (value = "updateDesiredPosition")
    private ResultVO updateDesiredPosition(@RequestBody @ApiParam(required = true)  DesiredPositionVO desiredPositionVO){
        int result = desiredPositionService.updateDesiredPosition(desiredPositionVO);
        if(result> NumberEnum.ZERO.getValue()){
            return  ResultVO.success(desiredPositionService.getDesiredPosition(desiredPositionVO.getId()));
        }
        return  ResultVO.error(desiredPositionVO);
    }
}
