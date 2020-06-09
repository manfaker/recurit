package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.SensitiveWordMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.SensitiveWordService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.SensitiveWordVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {
    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public void importSensitiveWord(List<String> listWord) {
        List<SensitiveWordVO> listSensitiveWord = new ArrayList<>();
        for(String word :listWord){
            if(EmptyUtils.isNotEmpty(word)){
                SensitiveWordVO sensitiveWordVO = new SensitiveWordVO();
                sensitiveWordVO.setSensitiveWord(word.trim());
                listSensitiveWord.add(sensitiveWordVO);
            }
        }
        int num = listSensitiveWord.size()% NumberEnum.ONE_HUNDRED.getValue()>NumberEnum.ZERO.getValue()?listSensitiveWord.size()/NumberEnum.ONE_HUNDRED.getValue()+1:listSensitiveWord.size()/NumberEnum.ONE_HUNDRED.getValue();
        for(int index=0;index<num;index++){
            List<SensitiveWordVO> listSensitive = new ArrayList<>();
            if((index+1)*NumberEnum.ONE_HUNDRED.getValue()>listSensitiveWord.size()){
                listSensitive=listSensitiveWord.subList(index*NumberEnum.ONE_HUNDRED.getValue(),listSensitiveWord.size());
            }else{
                listSensitive=listSensitiveWord.subList(index*NumberEnum.ONE_HUNDRED.getValue(),(index+1)*NumberEnum.ONE_HUNDRED.getValue());
            }
            saveBatchSensitiveWord(listSensitive);
        }

    }

    private void setSensitiveWordInfo(SensitiveWordVO sensitiveWordVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            sensitiveWordVO.setCreateDate(new Date());
            sensitiveWordVO.setCreater(user.getUserName());
        }
        sensitiveWordVO.setUpdateDate(new Date());
        sensitiveWordVO.setUpdater(user.getUserName());
    }

    @Override
    public void saveBatchSensitiveWord(List<SensitiveWordVO> listSensitiveWord) {
        if(EmptyUtils.isNotEmpty(listSensitiveWord)){
            for(SensitiveWordVO sensitiveWord:listSensitiveWord){
                setSensitiveWordInfo(sensitiveWord,true);
            }
            sensitiveWordMapper.saveBatchSensitiveWord(listSensitiveWord);
        }
    }

    @Override
    public ResultVO checkSensitiveWord(String jsonData) {
        if(EmptyUtils.isEmpty(jsonData)){
            return ResultVO.success();
        }
        JSONObject jsonObject  = JSON.parseObject(jsonData);
        Set<String> setKey = jsonObject.keySet();
        List<String> listStr = new ArrayList<>();
        for(String key:setKey){
            if(!"creater".equals(key)&&
                    !"updater".equals(key)&&
                    !"createDate".equals(key)&&
                    !"updateDate".equals(key)){
                String value = jsonObject.getString(key);
                if(EmptyUtils.isNotEmpty(value)){
                    listStr.add(value);
                }
            }
        }
        if(listStr.size()==NumberEnum.ZERO.getValue()){
            return ResultVO.success();
        }
        List<SensitiveWordVO> listSensitive= sensitiveWordMapper.checkSensitiveWord(listStr);
        List<String> listWord = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(listSensitive)&&listSensitive.size()>NumberEnum.ZERO.getValue()){
            for(SensitiveWordVO sensitiveWordVO:listSensitive){
                listWord.add(sensitiveWordVO.getSensitiveWord());
            }
            return ResultVO.error("内容含有"+String.join(OrdinaryConstant.SYMBOL_4,listWord)+"等敏感字符，请修改后再做处理");
        }
        return ResultVO.success();
    }
}
