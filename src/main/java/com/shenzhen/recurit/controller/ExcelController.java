package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.pojo.FilePojo;
import com.shenzhen.recurit.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "excel")
@Api(tags = "文档管理")
public class ExcelController {

    @Resource
    private ExcelService excelService;


    @ApiOperation("下载模板")
    @GetMapping(value = "download/template")
    public void downloadTemplate( String name, String suffix, String pelativePath){
        FilePojo filePojo = new FilePojo().setName(name).setSuffix(suffix).setPelativePath(pelativePath);
        excelService.downloadTemplate(filePojo);
    }
}
