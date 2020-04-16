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
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelMapper labelMapper;
    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Override
    public List<LabelVO> saveBatchLabel(List<LabelVO> listLabel) {
        if(EmptyUtils.isEmpty(listLabel)){
            return null;
        }
        String category = listLabel.get(NumberEnum.ZERO.getValue()).getCategory();
        int relationId = listLabel.get(NumberEnum.ZERO.getValue()).getRelationId();
        List<LabelVO> listSame=new ArrayList<>();
        String redisKey = category+relationId;
        List<LabelVO> currLabels = labelMapper.getLabelByCategory(category, relationId);
        if(EmptyUtils.isNotEmpty(currLabels)){
            for(int index=listLabel.size()-NumberEnum.ONE.getValue();index>=NumberEnum.ZERO.getValue();index--){
                if(currLabels.contains(listLabel.get(index))){
                    listSame.add(listLabel.get(index));
                    listLabel.remove(index);
                }
            }
            List<Integer> listIds = new ArrayList<>();
            for(LabelVO labelVO :currLabels){
                if(!listSame.contains(labelVO)){
                    listIds.add(labelVO.getId());
                }
            }
            deleteBatchIds(listIds);
        }else{
            deleteLabelByRelationId(category,relationId);
        }
        setAllOperaterAndDate(listLabel,true);
        List<LabelVO> labels = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(listLabel)&&listLabel.size()>NumberEnum.ZERO.getValue()){
            labelMapper.saveBatchLabel(listLabel);
            labels.addAll(labelMapper.getLabelByCategory(category, relationId));
            setLabelToResis(labels,redisKey);
        }
        return labels;
    }

    @Override
    public int deleteBatchIds(List<Integer> listIds) {
        List<LabelVO> listLabel = getLabelsByIds(listIds);
        if(EmptyUtils.isNotEmpty(listLabel)){
            listLabel.forEach(labelVO -> {
                removeRedisLabel(labelVO);
            });
        }
        if(EmptyUtils.isNotEmpty(listIds)&&listIds.size()>NumberEnum.ZERO.getValue()){
            return labelMapper.deleteBatchIds(listIds);
        }
        return NumberEnum.ZERO.getValue();
    }

    @Override
    public List<LabelVO> getLabelsByIds(List<Integer> listIds) {
        if(EmptyUtils.isEmpty(listIds)||listIds.size()==NumberEnum.ZERO.getValue()){
            return new ArrayList<>();
        }
        return labelMapper.getLabelsByIds(listIds);
    }

    @Override
    public void saveLabelToRedis(String category) {
        List<LabelVO> labels = getLabelsByCategory(category);
        if(EmptyUtils.isNotEmpty(labels)){
            labels.forEach(labelVO -> {
                setLabelToRedis(labelVO);
            });
        }
    }

    @Override
    public List<LabelVO> getLabelsByCategory(String category) {
        return labelMapper.getLabelsByCategory(category);
    }

    private void setLabelToRedis(LabelVO labelVO){
        if(EmptyUtils.isEmpty(labelVO)){
            return;
        }
        String redisKey = labelVO.getCategory()+labelVO.getRelationId();
        JSONObject jsonObject=redisTempleUtils.getValue(redisKey,JSONObject.class);
        if(EmptyUtils.isEmpty(jsonObject)){
            jsonObject = new JSONObject();
        }
        jsonObject.put(labelVO.getId()+OrdinaryConstant.IS_BLACK,JSON.toJSONString(labelVO));
        redisTempleUtils.setValue(redisKey,jsonObject);
    }

    private void setLabelToResis(List<LabelVO> labels,String redisKey){
        if(EmptyUtils.isEmpty(labels)||labels.size()==NumberEnum.ZERO.getValue()){
            return;
        }
        if(EmptyUtils.isEmpty(redisKey)){
            return;
        }
        JSONObject jsonObject=redisTempleUtils.getValue(redisKey,JSONObject.class);
        if(EmptyUtils.isEmpty(jsonObject)){
            jsonObject = new JSONObject();
        }
        for(LabelVO label:labels){
            if(EmptyUtils.isNotEmpty(label)){
                jsonObject.put(label.getId()+OrdinaryConstant.IS_BLACK,JSON.toJSONString(label));
            }
        }
        redisTempleUtils.setValue(redisKey,jsonObject);
    }

    private void setAllOperaterAndDate(List<LabelVO> listLabel,boolean flag){
        if(EmptyUtils.isNotEmpty(listLabel)||!listLabel.isEmpty()){
            for(LabelVO labelVO:listLabel){
                setSingleOperaterAndDate(labelVO,flag);
            }
        }
    }

    private void setSingleOperaterAndDate(LabelVO labelVO,boolean flag){
        if(EmptyUtils.isEmpty(labelVO)){
            labelVO = new LabelVO();
        }
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            labelVO.setCreateDate(new Date());
            labelVO.setCreater(user.getUserName());
        }
        labelVO.setUpdater(user.getUserName());
        labelVO.setUpdateDate(new Date());
    }

    @Override
    public LabelVO saveLabel(LabelVO labelVO) {
        setSingleOperaterAndDate(labelVO,true);
        labelMapper.saveLabel(labelVO);
        String redisKey = labelVO.getCategory()+labelVO.getRelationId();
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
        LabelVO labelVO=labelMapper.getLabelById(id);
        removeRedisLabel(labelVO);
        return labelMapper.deleteLabelById(id);
    }

    private void removeRedisLabel(LabelVO labelVO){
        if(EmptyUtils.isNotEmpty(labelVO)){
            String redisKey = labelVO.getCategory()+labelVO.getRelationId();
            if(EmptyUtils.isNotEmpty(redisKey)){
                JSONObject redisJson = redisTempleUtils.getValue(redisKey, JSONObject.class);
                if(EmptyUtils.isNotEmpty(redisJson)){
                    String key = labelVO.getId()+OrdinaryConstant.IS_BLACK;
                    if(redisJson.containsKey(key)){
                        redisJson.remove(key);
                        redisTempleUtils.setValue(redisKey,redisJson);
                    }
                }
            }
        }
    }

    @Override
    public int deleteLabelByRelationId(String category, int relationId) {
        String redisKey = category+relationId;
        if(EmptyUtils.isNotEmpty(redisKey)){
            redisTempleUtils.deleteValue(redisKey);
        }
        return labelMapper.deleteLabelByCategory(category,relationId);
    }

    @Override
    public int updateLabel(LabelVO labelVO) {
        setSingleOperaterAndDate(labelVO,false);
        int result = labelMapper.updateLabel(labelVO);
        if(result>NumberEnum.ZERO.getValue()&&EmptyUtils.isNotEmpty(labelVO)){
            labelVO=labelMapper.getLabelById(labelVO.getId());
            String redisKey = labelVO.getCategory()+labelVO.getRelationId();
            if(EmptyUtils.isNotEmpty(redisKey)){
                JSONObject redisJson = redisTempleUtils.getValue(redisKey, JSONObject.class);
                if(EmptyUtils.isNotEmpty(redisJson)){
                    String key = labelVO.getId()+OrdinaryConstant.IS_BLACK;
                    redisJson.put(key,JSON.toJSONString(labelVO));
                    redisTempleUtils.setValue(redisKey,redisJson);
                }
            }
        }
        return result;
    }

    @Override
    public LabelVO queryById(int id, String category, int relationId) {
        String redisKey = category+relationId;
        LabelVO labelVO=null;
        if(EmptyUtils.isNotEmpty(redisKey)){
            String value = redisTempleUtils.getValue(redisKey, String.class);
            if(EmptyUtils.isNotEmpty(value)){
                JSONObject labelJson = JSON.parseObject(value,JSONObject.class);
                String key = id+OrdinaryConstant.IS_BLACK;
                if(labelJson.containsKey(key)){
                    labelVO= JSON.parseObject(labelJson.getString(key), LabelVO.class);
                }
            }
        }
        if(EmptyUtils.isEmpty(labelVO)){
            labelVO=labelMapper.getLabelById(id);
        }
        return labelVO;
    }

    @Override
    public List<LabelVO> queryByRelationId(String category, int relationId) {
        String redisKey = category+relationId;
        List<LabelVO> listLabelVO=new ArrayList<>();
        if(EmptyUtils.isNotEmpty(redisKey)){
            String value = redisTempleUtils.getValue(redisKey, String.class);
            if(EmptyUtils.isNotEmpty(value)){
                JSONObject labelJson = JSON.parseObject(value,JSONObject.class);
                if(EmptyUtils.isNotEmpty(labelJson)){
                    for(String key : labelJson.keySet()){
                        LabelVO labelVO = JSON.parseObject(labelJson.getString(key), LabelVO.class);
                        if(EmptyUtils.isNotEmpty(labelVO)){
                            listLabelVO.add(labelVO);
                        }
                    }
                }
            }
        }
        if(listLabelVO.isEmpty()){
            listLabelVO = labelMapper.getLabelByCategory(category,relationId);
        }
        return listLabelVO;
    }
}
