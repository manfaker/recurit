package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.PositionMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.service.DictionaryService;
import com.shenzhen.recurit.service.LabelService;
import com.shenzhen.recurit.service.PositionService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.PositionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionMapper positionMapper;
    @Resource
    private LabelService labelService;
    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Resource
    private DictionaryService dictionaryService;

    @Override
    public List<PositionVO> getAllPosition() {

        return null;
    }

    @Override
    public List<PositionVO> getAllPositionByCondition(PositionVO position) {
        return null;
    }

    @Transactional
    @Override
    public PositionVO savePosition(PositionVO position) {
        position.setUpdateDate(new Date());
        position.setCreateDate(new Date());
        positionMapper.savePosition(position);
        if(position.getId()> NumberEnum.ZERO.getValue()){
            String labels = position.getLabels();
            if(EmptyUtils.isNotEmpty(labels)){
                List<LabelVO> listLabel = new ArrayList<>();
                List<String> listStr = Arrays.asList(labels.split(OrdinaryConstant.SYMBOL_4));
                getAssembleLabels(listStr,listLabel,position);
                List<LabelVO> labelList = labelService.saveBatchLabel(listLabel);
            }
        }
        return position;
    }

    private void getAssembleLabels(List<String> listStr,List<LabelVO> listLabel,PositionVO position){
        if(EmptyUtils.isEmpty(listStr)||listStr.isEmpty()){
            return;
        }
        for(String str : listStr){
            LabelVO labelVO = new LabelVO();
            labelVO.setLabelName(str);
            labelVO.setCategory(InformationConstant.COMPANY);
            labelVO.setRelationId(position.getId());
            listLabel.add(labelVO);
        }

    }

    @Override
    public List<PositionPojo> getByCompanyCode(String companyCode) {
        List<PositionPojo> listPosition = positionMapper.getByCompanyCode(companyCode);
        if(EmptyUtils.isNotEmpty(listPosition)&&!listPosition.isEmpty()){
            listPosition.forEach(position->{
                setInfoToPosition(position);
            });
        }
        return listPosition;
    }

    private void setInfoToPosition(PositionPojo position){
        if(EmptyUtils.isNotEmpty(position)){
            if(EmptyUtils.isNotEmpty(position.getSalary())){
                position.setSalaryDict(dictionaryService.getSignleByDictNumber(InformationConstant.SALARY,position.getSalary()));
            }
            if(EmptyUtils.isNotEmpty(position.getAcademicDegree())){
                position.setAcademicDegreeDict(dictionaryService.getSignleByDictNumber(InformationConstant.EDUCATION,position.getAcademicDegree()));
            }
            if(EmptyUtils.isNotEmpty(position.getExperience())){
                position.setExperienceDict(dictionaryService.getSignleByDictNumber(InformationConstant.EXPERIENCE,position.getExperience()));
            }
            position.setListLabel(labelService.queryByRelationId(InformationConstant.COMPANY,position.getId()));
        }
    }

    @Override
    public int updatePosition(PositionVO position){
       return 0;
    }

    @Override
    public PositionPojo getByPositionId(int id) {
        PositionPojo position = positionMapper.getByPositionId(id);
        setInfoToPosition(position);
        return position;
    }
}
