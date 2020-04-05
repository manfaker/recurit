package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.ResumeService;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.ResumeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("resume")
@Api( tags= "简历模块")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    @ApiOperation(value = "新增简历信息")
    @PostMapping(value = "saveResume",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO saveResume(@RequestBody @ApiParam(required = true) ResumeVO resumeVO){
        return resumeService.saveResume(resumeVO);
    }

    @ApiOperation(value = "修改简历信息")
    @PutMapping(value = "updateResume",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO updateResume(@RequestBody @ApiParam(required = true) ResumeVO resumeVO){
        int result = resumeService.updateResume(resumeVO);
        if(result> NumberEnum.ZERO.getValue()){
            return ResultVO.success(resumeService.getById(resumeVO.getId()));
        }
        return ResultVO.error(resumeVO);
    }

    @ApiOperation(value ="删除简历信息")
    @ApiImplicitParam(value = "ID",name = "id",required = true)
    @DeleteMapping(value = "deleteById",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO deleteById(int id){
        int result = resumeService.deleteById(id);
        if(result> NumberEnum.ZERO.getValue()){
            return ResultVO.success("删除成功");
        }
        return ResultVO.error("用户已删除，请勿重复删除");
    }

    @ApiOperation(value = "根据当前用户获取用户和简历信息")
    @GetMapping(value = "getByCurrUser",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO getByCurrUser(){
         return ResultVO.success(resumeService.getByCurrUser());
    }
}
