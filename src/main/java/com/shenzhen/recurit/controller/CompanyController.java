package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.shenzhen.recurit.service.CompanyService;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.CompanyVO;
import com.shenzhen.recurit.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    @RequestMapping(value = "saveCompany",method = RequestMethod.POST)
    public Object savePosition(@RequestBody String jsonData){
        CompanyVO companyVO = JSON.parseObject(jsonData, CompanyVO.class);
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

    @RequestMapping(value = "getByCompany",method = RequestMethod.POST)
    public Object getCompany(@RequestBody String jsonData){
        CompanyVO companyVO = JSON.parseObject(jsonData, CompanyVO.class);
        return ResultVO.success(companyService.getByCompany(companyVO));
    }

    @RequestMapping(value = "getCompanyByCompanyName",method = RequestMethod.GET)
    public Object getByPositionId(String companyName){
        return ResultVO.success(companyService.getCompanyByName(companyName));
    }

}
