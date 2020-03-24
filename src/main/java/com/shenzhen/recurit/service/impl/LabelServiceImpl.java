package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.LabelMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.enums.SymbolEnum;
import com.shenzhen.recurit.service.LabelService;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.ResultVO;
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
            String key = listLabel.get(NumberEnum.ZERO.getValue()).getCategory()+ OrdinaryConstant.SYMBOL_2+listLabel.get(NumberEnum.ZERO.getValue()).getRelationId();
            redisTempleUtils.setValue(key, JSON.toJSONString(listLabel));
            return listLabel;
        }
        return null;
    }

    @Override
    public LabelVO saveLabel(LabelVO labelVO) {
        return null;
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
