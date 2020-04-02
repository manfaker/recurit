package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.shenzhen.recurit.service.CompanyService;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.CompanyVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = {"公司信息"})
@RestController
@RequestMapping(value = "company")
public class CompanyController {

    @Resource
    private CompanyService companyService;


    @ApiOperation("保存公司信息")
    @PostMapping(value = "saveCompany")
    public Object savePosition(@RequestBody  @ApiParam(required=true) CompanyVO companyVO){
        return companyService.saveCompany(companyVO);
    }

//    @RequestMapping(value = "deleteCompany",method = RequestMethod.DELETE)
//    public Object deletePosition(int id){
//        return companyService.deletePositionById(id);
//    }

    @RequestMapping(value = "updateCompany",method = RequestMethod.POST)
    public Object updatePosition(@RequestBody String jsonData){
        CompanyVO companyVO = JSON.parseObject(jsonData, CompanyVO.class);
        return companyService.updateCompany(companyVO);
    }

    @PostMapping(value = "getByCompany")
    public Object getCompany(@RequestBody String jsonData){
        CompanyVO companyVO = JSON.parseObject(jsonData, CompanyVO.class);
        return ResultVO.success(companyService.getByCompany(companyVO));
    }

    @ApiOperation(value = "根据公司名称查询公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name="companyName",value = "公司名称",required = false)
    })
    @GetMapping(value = "getCompanyByCompanyName")
    public Object getByPositionId(String companyName){
        return ResultVO.success(companyService.getCompanyByName(companyName));
    }

}
