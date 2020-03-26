package com.shenzhen.recurit.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.service.DictionaryService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.DictionaryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class SchedulerTimer {

    private static Logger logger = LoggerFactory.getLogger(SchedulerTimer.class);

    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Resource
    private DictionaryService dictionaryService;
    /**
     * 每隔60秒刷新一次，看是否有需要添加的缓存
     */
    @Scheduled(fixedRate = 60000)
    public void testTasks() {
        String dictionaryTime = redisTempleUtils.getValue(InformationConstant.KEY_DICTIONARY_TIME, String.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OrdinaryConstant.YYYY_MM_DD_H24_MM_SS);
        Date startTime;
        String currTime;
        Date endTime;
        if(EmptyUtils.isEmpty(dictionaryTime)){
            endTime = dictionaryService.getLatestTime();
            //如果缓存无时间则设置开始时间时间为2000-01-01
            startTime = new Date(InformationConstant.INITIAL_TIME);
            List<DictionaryVO> listAll = dictionaryService.getListAll(startTime, endTime);
            setSaveDictory(listAll);
            currTime= simpleDateFormat.format(endTime);
            redisTempleUtils.setValue(InformationConstant.KEY_DICTIONARY_TIME,currTime);
        }else{
            endTime= new Date();
            currTime= simpleDateFormat.format(endTime);
            if(EmptyUtils.isNotEmpty(endTime)&&!dictionaryTime.equals(simpleDateFormat.format(endTime))){
                try {
                    startTime = simpleDateFormat.parse(dictionaryTime);
                    List<DictionaryVO> listAll = dictionaryService.getListAll(startTime, endTime);
                    setSaveDictory(listAll);
                    redisTempleUtils.setValue(InformationConstant.KEY_DICTIONARY_TIME,currTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将数据字典放入缓存
    private void setSaveDictory(List<DictionaryVO> listAll){
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
