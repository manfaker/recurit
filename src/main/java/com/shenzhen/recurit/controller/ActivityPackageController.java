package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.ActivityPackagePojo;
import com.shenzhen.recurit.service.ActivityPackageService;
import com.shenzhen.recurit.vo.ActivityPackageVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "活动套餐")
@RequestMapping(value = "activity/package")
public class ActivityPackageController {

    @Resource
    private ActivityPackageService activityPackageService;

    @PostMapping(value = "saveActivityPackage")
    @PermissionVerification
    @ApiOperation(value = "保存活动套餐")
    public Object saveActivityPackage(@RequestBody @ApiParam ActivityPackageVO activityPackageVO){
        ActivityPackagePojo activityPackagePojo = activityPackageService.saveActivityPackage(activityPackageVO);
        return ResultVO.success(activityPackagePojo);
    }

    @GetMapping(value = "getAllActivityPackage")
    @PermissionVerification
    @ApiOperation(value = "获取所有的活动套餐")
    public Object getAllActivityPackage(){
        List<ActivityPackagePojo> listActivityPackage = activityPackageService.getAllActivityPackage();
        return ResultVO.success(listActivityPackage);
    }

    @PutMapping(value = "updateActivityPackage")
    @PermissionVerification
    @ApiOperation(value = "保存活动套餐")
    public Object updateActivityPackage(@RequestBody @ApiParam ActivityPackageVO activityPackageVO){
        int result = activityPackageService.updateActivityPackage(activityPackageVO);
        if(result> NumberEnum.ZERO.getValue()){
            return ResultVO.success();
        }
        return ResultVO.error();
    }

}
