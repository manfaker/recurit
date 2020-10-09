package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.DictionaryService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.BaseVO;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "dictionary")
@Api(tags = {"数据字典"})
public class    DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @RequestMapping(value="save",method = RequestMethod.POST)
    public Object saveDictionary(@RequestBody String jsonData){
        DictionaryVO dictionary = JSONObject.parseObject(jsonData, DictionaryVO.class);
        dictionaryService.saveDictionary(dictionary);
        if(EmptyUtils.isNotEmpty(dictionary)&&dictionary.getId()> NumberEnum.ZERO.getValue()){
            return ResultVO.success(dictionary);
        }else{
            return ResultVO.error("网络异常。。。。");
        }
    }

    @PostMapping(value="saveBatch")
    @ApiOperation(value = "批量保存职位信息")
    public Object saveBatch(@RequestBody @ApiParam List<DictionaryVO> listDict){
        dictionaryService.saveBatch(listDict);
        return ResultVO.success();
    }

    @DeleteMapping(value="removeDict")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类型",name = "category",required = true),
            @ApiImplicitParam(value = "编码",name = "dictNum",required = false)
    })
    @ApiOperation(value = "根据类型或者编码删除数据字典")
    public Object removeDict(String category,String dictNum){
        dictionaryService.removeDict(category,dictNum);
        return ResultVO.success();
    }

    @RequestMapping(value="getAllDictByCategory",method = RequestMethod.GET)
    public Object getAllDictByCategory(String category){
        List<DictionaryVO> listDict = dictionaryService.getAllDictByCategory(category);
        return ResultVO.success(listDict);

    }

    @RequestMapping(value="getSignleByDictNumber",method = RequestMethod.GET)
    public Object getSignleByDictNumber(@Param("category") String category,@Param("dictNum")  String dictNum){
        DictionaryVO dictionary = dictionaryService.getSignleByDictNumber(category,dictNum);
        return ResultVO.success(dictionary);
    }

    @RequestMapping(value="getAllChildrenByDictNum",method = RequestMethod.GET)
    public Object getAllChildrenByDictNum(String dictNum){
        List<DictionaryVO> listDict = dictionaryService.getAllChildrenByDictNum(dictNum);
        return ResultVO.success(listDict);

    }

    @RequestMapping(value="update",method = RequestMethod.POST)
    public Object updateDictionary(@RequestBody String jsonData){
        DictionaryVO dictionary = JSONObject.parseObject(jsonData, DictionaryVO.class);
        DictionaryVO resultVO = dictionaryService.updateDictionary(dictionary);
        return ResultVO.success(resultVO);
    }

    @GetMapping(value="refreshAllDict")
    @ApiOperation(value = "刷新所有数据字典")
    public Object refreshAllDict(){
        dictionaryService.refreshAllDict();
        return ResultVO.success("刷新完毕");
    }

    @GetMapping(value="getTreeByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类型",name = "category",required = true),
    })
    @ApiOperation(value = "获取树形数据字典")
    public Object getTreeByCategory(String dictNum){
        return ResultVO.success(dictionaryService.getTreeByCategory(dictNum));
    }

}
