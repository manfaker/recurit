package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.pojo.SocialStandardPojo;
import com.shenzhen.recurit.service.SocialStandardService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;
import com.shenzhen.recurit.vo.SocialStandardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = {"社保标准"})
@RequestMapping(value = "social/standard")
public class SocialStandardController {

    @Resource
    private SocialStandardService socialStandardService;

    @PostMapping(value = "saveSocialStandard")
    @PermissionVerification
    @ApiOperation(value = "保存区域社保比例")
    public Object saveSocialStandard(@RequestBody @ApiParam SocialStandardVO socialStandardVO){
        SocialStandardPojo socialStandardPojo = socialStandardService.saveSocialStandard(socialStandardVO);
        if(EmptyUtils.isNotEmpty(socialStandardPojo)){
            return ResultVO.success(socialStandardPojo);
        }
        return ResultVO.error("保存失败");

    }

    @GetMapping (value = "getAllSocialStandard")
    @ApiOperation(value = "获取所有的区域社保比例")
    public Object getAllSocialStandard(){
        List<SocialStandardPojo> listSocialStand = socialStandardService.getAllSocialStandard();
        return ResultVO.success(listSocialStand);
    }
}
