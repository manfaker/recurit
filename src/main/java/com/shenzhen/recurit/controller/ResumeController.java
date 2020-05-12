package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.service.ResumeService;
import com.shenzhen.recurit.utils.word.WordUtil;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.ResumeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("resume")
@Api( tags= "简历模块")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    @ApiOperation(value = "新增简历信息")
    @PermissionVerification
    @PostMapping(value = "saveResume",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO saveResume(@RequestBody @ApiParam(required = true) ResumeVO resumeVO){
        return resumeService.saveResume(resumeVO);
    }

    @ApiOperation(value = "修改简历信息")
    @PermissionVerification
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
    @PermissionVerification
    @DeleteMapping(value = "deleteById",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO deleteById(int id){
        int result = resumeService.deleteById(id);
        if(result> NumberEnum.ZERO.getValue()){
            return ResultVO.success("删除成功");
        }
        return ResultVO.error("用户已删除，请勿重复删除");
    }

    @ApiOperation(value = "根据当前用户获取用户和简历信息")
    @PermissionVerification
    @GetMapping(value = "getByCurrUser",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO getByCurrUser(){
         return ResultVO.success(resumeService.getByCurrUser());
    }

    @ApiOperation(value = "查看已经所有发布简历的人员")
    @PermissionVerification
    @PostMapping(value = "getResumeAllByCondition",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO getResumeAllByCondition(@RequestBody @ApiParam ResumeVO resumeVO){
        return ResultVO.success(resumeService.getResumeAllByCondition(resumeVO));
    }

    @ApiOperation(value = "查看已经所有投递简历的人员")
    @PermissionVerification
    @GetMapping (value = "getApplyResume",produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResultVO getApplyResume(){
        return ResultVO.success(resumeService.getApplyResume());
    }

    @GetMapping (value = "downloadResume")
    @ApiOperation(value = "下载简历")
    public void downloadResume(HttpServletResponse response, String userCode){
        UserPojo userPojo = resumeService.getResumeInfoByUserCode(userCode);
        WordUtil.downloadResume(response,userPojo);
    }

    @GetMapping (value = "sendResumeEmail")
    @ApiOperation(value = "投递简历到hr邮箱")
    public ResultVO sendResumeEmail(String userCode){
        return resumeService.sendResumeEmail(userCode);
    }

    @GetMapping (value = "getResumeByUserCode")
    @ApiOperation(value = "根据userCode获取简历信息")
    public ResultVO getResumeByUserCode(String userCode){
        UserPojo userPojo = resumeService.getResumeInfoByUserCode(userCode);
        return ResultVO.success(userPojo);
    }

}
