package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.LabelMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.LabelService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.LabelVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelMapper labelMapper;
    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Override
    public List<LabelVO> saveBatchLabel(List<LabelVO> listLabel) {
        labelMapper.saveBatchLabel(listLabel);
        boolean flag = false;
        for(LabelVO label :listLabel){
            if(label.getId()> NumberEnum.ZERO.getValue()){
                flag = true;
                break;
            }
        }
        if(flag){
            JSONObject jsonObject = new JSONObject();
            String redisKey = listLabel.get(NumberEnum.ZERO.getValue()).getCategory()+listLabel.get(NumberEnum.ZERO.getValue()).getRelationId();
            for(LabelVO label : listLabel){
                String key  = label.getId()+OrdinaryConstant.IS_BLACK;
                jsonObject.put(key,JSON.toJSONString(label));
            }
            redisTempleUtils.setValue(redisKey, jsonObject);
            return listLabel;
        }
        return null;
    }

    @Override
    public LabelVO saveLabel(LabelVO labelVO) {
        labelMapper.saveLabel(labelVO);
        String redisKey = labelVO.getCategory()+ OrdinaryConstant.SYMBOL_2+labelVO.getRelationId();
        if(EmptyUtils.isNotEmpty(labelVO)&&labelVO.getId()>NumberEnum.ZERO.getValue()){
            String value = redisTempleUtils.getValue(redisKey, String.class);
            JSONObject jsonObject;
            if(EmptyUtils.isNotEmpty(value)){
                jsonObject = JSON.parseObject(value, JSONObject.class);
            }else{
                jsonObject = new JSONObject();
            }
            jsonObject.put(labelVO.getId()+OrdinaryConstant.IS_BLACK,JSON.toJSONString(labelVO));
            redisTempleUtils.setValue(redisKey,jsonObject);
        }
        return labelVO;
    }

    @Override
    public int deleteLabelById(int id) {
        return 0;
    }

    @Override
    public int deleteLabelByRelationId(String category, int relationId) {
        return 0;
    }

    @Override
    public int updateLabel(LabelVO labelVO) {
        return 0;
    }

    @Override
    public LabelVO queryById(int id, String category, int relationId) {
        return null;
    }

    @Override
    public List<LabelVO> queryByRelationId(String category, int relationId) {
        return null;
    }
}
