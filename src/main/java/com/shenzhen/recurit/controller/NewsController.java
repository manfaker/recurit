package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.DocumentService;
import com.shenzhen.recurit.service.NewsService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.NewsVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = {"新闻"})
@RequestMapping(value = "news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @PostMapping(value = "saveNews")
    @ApiOperation(value = "新增新闻")
    public ResultVO saveNews(@RequestBody @ApiParam NewsVO newsVO){
        if(newsVO.getDocumentId()== NumberEnum.ZERO.getValue()&& EmptyUtils.isEmpty(newsVO.getNewsImagePath())){
            ResultVO.success("必须选择图片");
        }
        return ResultVO.success(newsService.saveNews(newsVO));
    }

    @PutMapping(value = "updateNews")
    @ApiOperation(value = "修改新闻")
    public ResultVO updateNews(@RequestBody @ApiParam NewsVO newsVO){
        return ResultVO.success(newsService.updateNews(newsVO));
    }

    @GetMapping(value = "getAllNews")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "每页大小",name="pageSize",required = false),
            @ApiImplicitParam(value = "起始页",name="pageNo",required = false)
    })
    @ApiOperation(value = "修改新闻")
    public ResultVO getAllNews(Integer pageSize,Integer pageNo){
        return ResultVO.success(newsService.getAllNews(pageSize,pageNo));
    }

    @DeleteMapping(value = "deleteNewsById")
    @ApiOperation(value = "删除新闻信息")
    public ResultVO deleteNewsById(int id){
        return ResultVO.success(newsService.deleteNewsById(id));
    }
}
