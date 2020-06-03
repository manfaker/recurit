package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.service.SocialSecurityInfoService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = {"社保"})
    @RequestMapping(value = "social/security")
public class SocialSecurityInfoController {

    @Resource
    private SocialSecurityInfoService socialSecurityInfoService;

    @PostMapping(value = "calculatePrice")
    @PermissionVerification
    @ApiOperation(value = "计算价格")
    public Object calculatePrice(@RequestBody @ApiParam String jsonData){
        return socialSecurityInfoService.calculatePrice(jsonData);
    }

    @PostMapping(value = "saveSocialSecuritInfo")
    @PermissionVerification
    @ApiOperation(value = "保存社保信息")
    public ResultVO saveSocialSecuritInfo(@RequestBody @ApiParam SocialSecurityInfoVO socialSecurityInfoVO){
        String idCard = socialSecurityInfoVO.getIdCard();
        List<SocialSecurityInfoPojo> listSocialInfo = socialSecurityInfoService.getAllSecuritInfoByIdCard(idCard);
        if(EmptyUtils.isNotEmpty(listSocialInfo)){
            for(SocialSecurityInfoPojo socialInfo: listSocialInfo){
                if(socialInfo.getSocialSecurityDate().compareTo(socialSecurityInfoVO.getSocialSecurityDate())!=-1&&socialSecurityInfoVO.getSocialSecurityDate().compareTo(socialInfo.getSocialSecurityEndDate())!=-1){
                    return ResultVO.error("当前时间段已有添加社保，请重新选择社保代缴开始日期");
                }
            }
        }
        SocialSecurityInfoPojo socialSecurityInfoPojo = socialSecurityInfoService.saveSocialSecuritInfo(socialSecurityInfoVO);
        return ResultVO.success(socialSecurityInfoPojo);
    }

    @PutMapping(value = "saveSocialSecuritInfo")
    @PermissionVerification
    @ApiOperation(value = "修改社保信息")
    public ResultVO updateSocialSecuritInfo(@RequestBody @ApiParam SocialSecurityInfoVO socialSecurityInfoVO){
        int result = socialSecurityInfoService.updateSocialSecuritInfo(socialSecurityInfoVO);
        return ResultVO.success(result);
    }

    @GetMapping(value = "getSocialSecuritInfoById")
    @PermissionVerification
    @ApiImplicitParam(value = "id" ,name = "id",required = true)
    @ApiOperation(value = "修改社保信息")
    public ResultVO getSocialSecuritInfoById( int id){
        SocialSecurityInfoPojo socialSecurityInfoPojo = socialSecurityInfoService.getSocialSecuritInfoById(id);
        return ResultVO.success(socialSecurityInfoPojo);
    }

    @DeleteMapping(value = "deleteSecuritInfoById")
    @PermissionVerification
    @ApiImplicitParam(value = "id" ,name = "id",required = true)
    @ApiOperation(value = "删除社保信息")
    public ResultVO deleteSecuritInfoById( int id){
        int result = socialSecurityInfoService.deleteSecuritInfoById(id);
        return ResultVO.success(result);
    }

    @GetMapping(value = "getAllSecuritInfo")
    @PermissionVerification
    @ApiOperation(value = "获取购物车中所有的社保信息")
    public ResultVO getAllSecuritInfo( ){
        List<SocialSecurityInfoPojo> listSocialSecurit = socialSecurityInfoService.getAllSecuritInfo();
        return ResultVO.success(listSocialSecurit);
    }

    @PostMapping(value = "saveDirectSecuritInfo")
    @PermissionVerification
    @ApiOperation(value = "确认并支付")
    public ResultVO saveDirectSecuritInfo( @RequestBody @ApiParam SocialSecurityInfoVO socialSecurityInfoVO){
        SocialSecurityInfoPojo socialSecurit = socialSecurityInfoService.saveDirectSecuritInfo(socialSecurityInfoVO);
        return ResultVO.success(socialSecurit);
    }

}
