package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.PositionVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.text.Position;
import java.lang.reflect.InvocationTargetException;
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

    /**
     * 判断市修改还是新增
     * @param positionVO
     * @param isUpdate   yes 修改 no新增
     */
    private void setOrdinaryInfo(PositionVO positionVO,boolean isUpdate){
        if(EmptyUtils.isEmpty(positionVO)){
            return;
        }
        UserVO user = ThreadLocalUtils.getUser();
        if(!isUpdate){
            if(EmptyUtils.isNotEmpty(user)){
                positionVO.setCreater(user.getUserName());
            }
            positionVO.setCreateDate(new Date());
        }
        if(EmptyUtils.isNotEmpty(user)){
            positionVO.setUpdater(user.getUserName());
        }
        positionVO.setUpdateDate(new Date());
    }


    @Transactional
    @Override
    public ResultVO deletePositionById(int id) {
        PositionPojo positionPojo = positionMapper.getByPositionId(id);
        if(EmptyUtils.isEmpty(positionPojo)){
            return ResultVO.error("该职位信息不存在");
        }
        PositionVO positionVO = new PositionVO();
        String category = InformationConstant.COMPANY;
        int relationId = positionPojo.getId();
        positionVO.setId(relationId);
        positionVO.setStatus(NumberEnum.TWO.getValue());
        setOrdinaryInfo(positionVO,true);
        String redisKey = category +relationId;
        int result = positionMapper.updatePosition(positionVO);
        if(result>NumberEnum.ZERO.getValue()){
            if(EmptyUtils.isNotEmpty(redisKey)){
                int num = labelService.deleteLabelByRelationId(category, relationId);
                if(num>NumberEnum.ZERO.getValue()){
                    redisTempleUtils.deleteValue(redisKey);
                }
            }
            return ResultVO.success("删除成功");
        }
        return ResultVO.error("删除失败");
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
    public ResultVO updatePosition(PositionVO position){
        setOrdinaryInfo(position,true);
        if(EmptyUtils.isNotEmpty(position)&&EmptyUtils.isNotEmpty(position.getCompanyCode())&&EmptyUtils.isNotEmpty(position.getId())&&position.getStatus()==1){
            positionMapper.updatePosition(position);
            return ResultVO.success("修改成功");
        }
       return ResultVO.error("修改失败成功");
    }

    @Override
    public PositionPojo getByPositionId(int id) {
        PositionPojo position = positionMapper.getByPositionId(id);
        setInfoToPosition(position);
        return position;
    }
}
