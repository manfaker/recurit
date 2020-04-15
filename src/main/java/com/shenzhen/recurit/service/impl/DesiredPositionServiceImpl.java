package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.dao.DesiredPositionMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.DesiredPositionPojo;
import com.shenzhen.recurit.service.DesiredPositionService;
import com.shenzhen.recurit.service.DictionaryService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DesiredPositionVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DesiredPositionServiceImpl implements DesiredPositionService {
    @Resource
    private DesiredPositionMapper desiredPositionMapper;
    @Resource
    private DictionaryService dictionaryService;

    @Override
    public ResultVO saveDesiredPosition(DesiredPositionVO desiredPositionVO) {
        setDesiredPositionInfo(desiredPositionVO,true);
        desiredPositionMapper.saveDesiredPosition(desiredPositionVO);
        if(desiredPositionVO.getId()> NumberEnum.ZERO.getValue()){
            DesiredPositionPojo desiredPositionPojo=getDesiredPosition(desiredPositionVO.getId());
            return ResultVO.success(desiredPositionPojo);
        }
        return ResultVO.error(desiredPositionVO);
    }

    private void setDesiredPosition(DesiredPositionPojo desiredPositionPojo){
        if(EmptyUtils.isNotEmpty(desiredPositionPojo)){
            if(EmptyUtils.isNotEmpty(desiredPositionPojo.getSalary())){
                desiredPositionPojo.setSalaryDict(dictionaryService.getSignleByDictNumber(InformationConstant.SALARY,desiredPositionPojo.getSalary()));
            }
        }
    }

    public void setDesiredPositionInfo(DesiredPositionVO desiredPositionVO,boolean flag){
        if(EmptyUtils.isEmpty(desiredPositionVO)){
            return;
        }
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            desiredPositionVO.setUserCode(user.getUserCode());
            desiredPositionVO.setCreater(user.getUserName());
            desiredPositionVO.setCreateDate(new Date());
        }
        desiredPositionVO.setUpdateDate(new Date());
        desiredPositionVO.setUpdater(user.getUserName());
    }
    @Override
    public int deleteDesiredPositionById(int id) {
        return desiredPositionMapper.deleteDesiredPositionById(id);
    }

    @Override
    public int updateDesiredPosition(DesiredPositionVO desiredPositionVO) {
        setDesiredPositionInfo(desiredPositionVO,false);
        return desiredPositionMapper.updateDesiredPosition(desiredPositionVO);
    }

    @Override
    public DesiredPositionPojo getDesiredPosition(int id) {
        DesiredPositionPojo desiredPositionPojo=desiredPositionMapper.getDesiredPosition(id);
        setDesiredPosition(desiredPositionPojo);
        return desiredPositionPojo;
    }
}
