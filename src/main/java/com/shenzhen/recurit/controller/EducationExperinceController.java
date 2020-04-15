package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.DesiredPositionService;
import com.shenzhen.recurit.service.EducationExperinceService;
import com.shenzhen.recurit.vo.DesiredPositionVO;
import com.shenzhen.recurit.vo.EducationExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping(value = "education/experiences")
@RestController
@Api(tags = {"教育经历"})
public class EducationExperinceController {

    @Resource
    private EducationExperinceService educationExperinceService;

    @ApiOperation(value = "修改期望教育经历")
    @PermissionVerification
    @PostMapping(value = "saveEducationExperince",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    private ResultVO saveEducationExperince(@RequestBody @ApiParam(required = true) EducationExperienceVO educationExperienceVO){
        return educationExperinceService.saveEducationExperince(educationExperienceVO);
    }

    @ApiOperation(value = "删除期望教育经历")
    @PermissionVerification
    @ApiImplicitParam(value = "主键",name = "id",required = true)
    @DeleteMapping(value = "deleteEducationExperinceById", produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    private ResultVO deleteEducationExperinceById(int id){
        int result = educationExperinceService.deleteEducationExperinceById(id);
        if(result> NumberEnum.ZERO.getValue()){
            return  ResultVO.success("删除成功");
        }
        return  ResultVO.success("删除失败");
    }

    @ApiOperation(value = "修改期望教育经历")
    @PermissionVerification
    @PutMapping (value = "updateEducationExperince")
    private ResultVO updateEducationExperince(@RequestBody @ApiParam(required = true) EducationExperienceVO educationExperienceVO){
        int result = educationExperinceService.updateEducationExperince(educationExperienceVO);
        if(result> NumberEnum.ZERO.getValue()){
            return  ResultVO.success(educationExperinceService.getEducationExperinceById(educationExperienceVO.getId()));
        }
        return  ResultVO.error(educationExperienceVO);
    }
}
