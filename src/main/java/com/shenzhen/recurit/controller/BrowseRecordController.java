package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.BrowseRecordService;
import com.shenzhen.recurit.vo.BrowseRecordVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "browse/record")
@Api(tags = {"浏览记录"})
public class BrowseRecordController {
    
    @Resource
    private BrowseRecordService browseRecordService;
    
    @PostMapping(value = "saveBrowseRecord")
    @PermissionVerification
    @ApiOperation(value = "保存记录")
    public ResultVO saveBrowseRecord(@RequestBody @ApiParam BrowseRecordVO browseRecordVO){
        BrowseRecordVO browseRecord = browseRecordService.saveBrowseRecord(browseRecordVO);
        if(browseRecord.getId()> NumberEnum.ZERO.getValue()){
            return ResultVO.success();
        }
        return ResultVO.error();
    };
}
