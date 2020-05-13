package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        setOrdinaryInfo(position,false);
        positionMapper.savePosition(position);
        PositionPojo positionPojo = new PositionPojo();
        if(position.getId()> NumberEnum.ZERO.getValue()){
            positionPojo = positionMapper.getByPositionId(position.getId());
            String labels = position.getLabels();
            savePositions(labels,positionPojo);
        }
        setInfoToPosition(positionPojo);
        return position;
    }

    private void savePositions(String labels,PositionPojo position){
        if(EmptyUtils.isNotEmpty(labels)){
            List<LabelVO> listLabel = new ArrayList<>();
            List<String> listStr = Arrays.asList(labels.split(OrdinaryConstant.SYMBOL_4));
            getAssembleLabels(listStr,listLabel,position);
            List<LabelVO> labelList = labelService.saveBatchLabel(listLabel);
        }else{
            labelService.deleteLabelByRelationId(InformationConstant.POSITION,position.getId());
        }
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
            positionVO.setCompanyCode(user.getCompanyCode());
            positionVO.setUserCode(user.getUserCode());
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

    private void getAssembleLabels(List<String> listStr,List<LabelVO> listLabel,PositionPojo position){
        if(EmptyUtils.isEmpty(listStr)||listStr.isEmpty()){
            return;
        }
        for(String str : listStr){
            if(EmptyUtils.isEmpty(str)){
                continue;
            }
            LabelVO labelVO = new LabelVO();
            labelVO.setLabelName(str);
            labelVO.setCategory(InformationConstant.POSITION);
            labelVO.setRelationId(position.getId());
            listLabel.add(labelVO);
        }

    }

    @Override
    public PageInfo<PositionPojo> getByCompanyCode(String companyCode,String userCode,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<PositionPojo> listPosition = positionMapper.getByCompanyCode(companyCode,userCode);
        if(EmptyUtils.isNotEmpty(listPosition)&&!listPosition.isEmpty()){
            listPosition.forEach(position->{
                setInfoToPosition(position);
            });
        }
        PageInfo<PositionPojo> pageInfo = new PageInfo<>(listPosition);
        return pageInfo;
    }

    private void setInfoToPosition(PositionPojo position){
        if(EmptyUtils.isNotEmpty(position)){
            if(EmptyUtils.isNotEmpty(position.getSalary())){
                position.setSalaryDict(dictionaryService.getSignleByDictNumber(InformationConstant.SALARY,position.getSalary()));
            }
            if(EmptyUtils.isNotEmpty(position.getEducation())){
                position.setAcademicDegreeDict(dictionaryService.getSignleByDictNumber(InformationConstant.EDUCATION,position.getEducation()));
            }
            if(EmptyUtils.isNotEmpty(position.getExperience())){
                position.setExperienceDict(dictionaryService.getSignleByDictNumber(InformationConstant.EXPERIENCE,position.getExperience()));
            }
            if(EmptyUtils.isNotEmpty(position.getFinancing())){
                position.setFinancingDict(dictionaryService.getSignleByDictNumber(InformationConstant.FINANCING,position.getFinancing()));
            }
            if(EmptyUtils.isNotEmpty(position.getScale())){
                position.setScaleDict(dictionaryService.getSignleByDictNumber(InformationConstant.SCALE,position.getScale()));
            }
            if(EmptyUtils.isNotEmpty(position.getUserCode())){
                UserVO user = ThreadLocalUtils.getUser();
                if(position.getUserCode().equals(user.getUserCode())){
                    position.setUserVO(user);
                }
            }
            position.setListLabel(labelService.queryByRelationId(InformationConstant.POSITION,position.getId()));
        }
    }

    private void setNewInfoToPosition(PositionPojo position){
        if(EmptyUtils.isNotEmpty(position)){
            if(EmptyUtils.isNotEmpty(position.getSalary())){
                position.setSalaryDict(dictionaryService.getSignleByDictNumber(InformationConstant.SALARY,position.getSalary()));
            }
            if(EmptyUtils.isNotEmpty(position.getEducation())){
                position.setAcademicDegreeDict(dictionaryService.getSignleByDictNumber(InformationConstant.EDUCATION,position.getEducation()));
            }
            if(EmptyUtils.isNotEmpty(position.getExperience())){
                position.setExperienceDict(dictionaryService.getSignleByDictNumber(InformationConstant.EXPERIENCE,position.getExperience()));
            }
            if(EmptyUtils.isNotEmpty(position.getFinancing())){
                position.setFinancingDict(dictionaryService.getSignleByDictNumber(InformationConstant.FINANCING,position.getFinancing()));
            }
            if(EmptyUtils.isNotEmpty(position.getScale())){
                position.setScaleDict(dictionaryService.getSignleByDictNumber(InformationConstant.SCALE,position.getScale()));
            }
            position.setListLabel(labelService.queryByRelationId(InformationConstant.POSITION,position.getId()));
        }
    }

    @Transactional
    @Override
    public ResultVO updatePosition(PositionVO position){
        setOrdinaryInfo(position,true);
        if(EmptyUtils.isNotEmpty(position)
                &&position.getId()>NumberEnum.ZERO.getValue()){
            positionMapper.updatePosition(position);
            PositionPojo positionPojo = positionMapper.getByPositionId(position.getId());
            savePositions(position.getLabels(),positionPojo);
            setInfoToPosition(positionPojo);
            return ResultVO.success("修改成功",positionPojo);
        }
       return ResultVO.error("修改失败",position);
    }

    @Override
    public PositionPojo getByPositionId(int id) {
        PositionPojo position = positionMapper.getByPositionId(id);
        setInfoToPosition(position);
        return position;
    }

    @Override
    public PageInfo<PositionPojo> getAllPositions(PositionVO positionVO,int pageNum,int pageSize){
        String userCode = null;
        positionVO.setUserCode(ThreadLocalUtils.getUser().getUserCode());
        PageHelper.offsetPage((pageNum-NumberEnum.ONE.getValue())*pageSize,pageSize);
        List<PositionPojo> listPostion = positionMapper.getAllPositions(positionVO);
        if(EmptyUtils.isNotEmpty(listPostion)&&listPostion.size()>NumberEnum.ZERO.getValue()){
            listPostion.forEach(position->{
                setInfoToPosition(position);
            });
        }
        PageInfo<PositionPojo> pageInfo = new PageInfo<>(listPostion);
        return pageInfo;
    }

    @Override
    public PageInfo<PositionPojo> getNewAllPositions(PositionVO positionVO, Integer pageNum, Integer pageSize) {
        PageHelper.offsetPage((pageNum-NumberEnum.ONE.getValue())*pageSize,pageSize);
        List<PositionPojo> listPostion = positionMapper.getNewAllPositions(positionVO);
        if(EmptyUtils.isNotEmpty(listPostion)&&listPostion.size()>NumberEnum.ZERO.getValue()){
            listPostion.forEach(position->{
                setNewInfoToPosition(position);
            });
        }
        PageInfo<PositionPojo> pageInfo = new PageInfo<>(listPostion);
        return pageInfo;
    }

    @Override
    public List<PositionPojo> getPopularPositions() {
        List<PositionPojo> listPosition = positionMapper.getPopularPositions();
        if(EmptyUtils.isNotEmpty(listPosition)){
            listPosition.forEach(position->{
                setInfoToPosition(position);
            });
        }
        return listPosition;
    }

    @Override
    public List<PositionPojo> getRecentlyPositions(String userCode) {
        List<PositionPojo> recentlyPositions = positionMapper.getRecentlyPositions(userCode);
        if(EmptyUtils.isNotEmpty(recentlyPositions)){
            recentlyPositions.forEach(position->{
                setInfoToPosition(position);
            });
        }
        return recentlyPositions;
    }

    @Override
    public List<PositionPojo> getBulletinBoardPosition() {
        int sumPosition = statisticsAllPositions();
        int pageNo = NumberEnum.ZERO.getValue();
        int pageSize = NumberEnum.ZERO.getValue();
        if(sumPosition>=5){
            int mathRandon = (int)Math.random()*100+1;
            pageSize =4;
            if(mathRandon+NumberEnum.FOUR.getValue()>sumPosition){
                pageNo = sumPosition-4;
            }else if(mathRandon<4){
                pageNo=0;
            }else{
                pageNo=mathRandon;
            }
        }
        return positionMapper.getBulletinBoardPosition(pageNo,pageSize);
    }

    @Override
    public int statisticsAllPositions() {
        return positionMapper.statisticsAllPositions();
    }




}
