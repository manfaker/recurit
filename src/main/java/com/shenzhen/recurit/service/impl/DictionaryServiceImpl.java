package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.DictionaryMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.DictionaryService;
import com.shenzhen.recurit.timer.SchedulerTimer;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;
    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Resource
    private SchedulerTimer schedulerTimer;

    @Override
    public Date getLatestTime() {
        return dictionaryMapper.getLatestTime();
    }

    @Override
    public List<DictionaryVO> getListAll(Date startTime, Date endTime) {
        return dictionaryMapper.getListAll(startTime,endTime);
    }

    @Override
    public void saveDictionary(DictionaryVO dictionary) {
        String dictNum = dictionary.getDictNum();
        DictionaryVO dictByNum = getDictByNum(dictNum);
        if(EmptyUtils.isEmpty(dictByNum)){
            dictionary.setCreateDate(new Date());
            dictionary.setUpdateDate(new Date());
            dictionaryMapper.saveDictionary(dictionary);
            String category = dictionary.getCategory();
            JSONObject dictJson = redisTempleUtils.getValue(category,JSONObject.class);
            if(EmptyUtils.isEmpty(dictJson)){
                dictJson = new JSONObject();
            }
            String str = JSON.toJSONString(dictionary);
            dictJson.put(dictNum, str);
            redisTempleUtils.setValue(category,dictJson);
        }
    }

    @Override
    public DictionaryVO getDictByNum(String dictNum) {
        return dictionaryMapper.getDictByNum(dictNum);
    }

    @Override
    public List<DictionaryVO> getAllDictByCategory(String category) {
        JSONObject dictJson = redisTempleUtils.getValue(category, JSONObject.class);
        List<DictionaryVO> dictList = new ArrayList<>();
        if(EmptyUtils.isEmpty(dictJson)||dictJson.size()== NumberEnum.ZERO.getValue()){
            dictList = dictionaryMapper.getAllDictByCategory(category);
            saveToRedisDictionarys(dictList);
            return dictList;
        }
        for(String key: dictJson.keySet()){
            DictionaryVO dictionary = JSONObject.parseObject(dictJson.getString(key),DictionaryVO.class);
            dictList.add(dictionary);
        }
        return dictList;
    }

    @Override
    public DictionaryVO getSignleByDictNumber(String category, String dictNum) {
        JSONObject dictJson = redisTempleUtils.getValue(category, JSONObject.class);
        if(EmptyUtils.isNotEmpty(dictJson)&&dictJson.size()>NumberEnum.ZERO.getValue()&&dictJson.containsKey(dictNum)){
            DictionaryVO dictionary = JSONObject.parseObject(dictJson.getString(dictNum),DictionaryVO.class);
            return dictionary;
        }
        DictionaryVO dictionaryVO = dictionaryMapper.getSignleByDictNumber(dictNum);
        saveSingleRedisDictionary(dictionaryVO);
        return dictionaryVO;
    }

    private void saveToRedisDictionarys(List<DictionaryVO> listDict){
        if(EmptyUtils.isEmpty(listDict)){
            return;
        }
        for(DictionaryVO dictionaryVO : listDict){
            saveSingleRedisDictionary(dictionaryVO);
        }
    }

    private void saveSingleRedisDictionary(DictionaryVO dictionaryVO){
        JSONObject dictJson = redisTempleUtils.getValue(dictionaryVO.getCategory(), JSONObject.class);
        if(EmptyUtils.isEmpty(dictJson)){
            dictJson = new JSONObject();
        }
        dictJson.put(dictionaryVO.getDictNum(),JSON.toJSONString(dictionaryVO));
        redisTempleUtils.setValue(dictionaryVO.getCategory(),dictJson);
    }

    @Override
    public List<DictionaryVO> getAllChildrenByDictNum(String dictNum) {
        List<DictionaryVO> listDict = dictionaryMapper.getAllChildrenByDictNum(dictNum);
        return listDict ;
    }

    @Override
    public DictionaryVO updateDictionary(DictionaryVO dictionaryVO) {
        String dictNum = dictionaryVO.getDictNum();
        dictionaryVO.setUpdateDate(new Date());
        int result = dictionaryMapper.updateDictionary(dictionaryVO);
        DictionaryVO dictByNum;
        if(result>NumberEnum.ZERO.getValue()){
            String category = dictionaryVO.getCategory();
            dictByNum = getDictByNum(dictNum);
            JSONObject dictJson = redisTempleUtils.getValue(category,JSONObject.class);
            if(EmptyUtils.isEmpty(dictJson)){
                dictJson = new JSONObject();
            }
            dictJson.put(dictNum, JSON.toJSONString(dictByNum));
            redisTempleUtils.setValue(category,dictJson);
            return dictByNum;
        }else{
            return dictionaryVO;
        }

    }

    @Override
    public void refreshAllDict() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000,1,1);
        List<DictionaryVO> listAll = dictionaryMapper.getListAll(calendar.getTime(), new Date());
        if(EmptyUtils.isNotEmpty(listAll)&&!listAll.isEmpty()){
            listAll.forEach(item->{
                String category = item.getCategory();
                if(EmptyUtils.isNotEmpty(category)){
                    JSONObject dictJson = redisTempleUtils.getValue(category, JSONObject.class);
                    if(EmptyUtils.isEmpty(dictJson)){
                        dictJson = new JSONObject();
                    }
                    dictJson.put(item.getDictNum(),JSON.toJSONString(item));
                    redisTempleUtils.setValue(category, dictJson);
                }
            });
        }
    }
}
