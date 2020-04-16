package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.JobExperiencePojo;
import com.shenzhen.recurit.service.JobExperienceService;
import com.shenzhen.recurit.vo.JobExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("job/experience")
@Api(tags = {"个人经历"})
public class JobExperienceController {
    @Resource
    private JobExperienceService jobExperienceService;

    @ApiOperation(value = "保存个人经历")
    @PermissionVerification
    @PostMapping(value = "saveJobExperience",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    private ResultVO saveJobExperience(@RequestBody @ApiParam(required = true) JobExperienceVO jobExperienceVO){
        return jobExperienceService.saveJobExperience(jobExperienceVO);
    }

    @ApiOperation(value = "删除个人经历")
    @PermissionVerification
    @ApiImplicitParam(value = "主键",name = "id",required = true)
    @DeleteMapping (value = "deleteJobExperienceById", produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    private ResultVO deleteJobExperienceById(int id){
        int result = jobExperienceService.deleteJobExperienceById(id);
        if(result> NumberEnum.ZERO.getValue()){
            return  ResultVO.success("删除成功");
        }
        return  ResultVO.success("删除失败");
    }

    @ApiOperation(value = "修改个人经历")
    @PermissionVerification
    @PutMapping (value = "updateJobExperience")
    private ResultVO updateJobExperience(@RequestBody @ApiParam(required = true) JobExperienceVO jobExperienceVO){
        int result = jobExperienceService.updateJobExperience(jobExperienceVO);
        if(result> NumberEnum.ZERO.getValue()){
            return  ResultVO.success(jobExperienceService.getJobExperienceById(jobExperienceVO.getId()));
        }
        return  ResultVO.error(jobExperienceVO);
    }

}
