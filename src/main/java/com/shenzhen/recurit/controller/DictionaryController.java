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
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "dictionary")
public class DictionaryController {

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

    @RequestMapping(value="getAllDictByCategory",method = RequestMethod.GET)
    public Object getAllDictByCategory(String category){
        List<DictionaryVO> listDict = dictionaryService.getAllDictByCategory(category);
        return ResultVO.success(listDict);

    }

    @RequestMapping(value="getAllDictByCateAndNumber",method = RequestMethod.GET)
    public Object getAllDictByCateAndNumber(@Param("category") String category,@Param("category")  String dictNum){
        DictionaryVO dictionary = dictionaryService.getAllDictByCateAndNumber(category,dictNum);
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

}
