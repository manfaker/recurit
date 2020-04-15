package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.dao.DesiredPositionMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.DesiredPositionPojo;
import com.shenzhen.recurit.service.DesiredPositionService;
import com.shenzhen.recurit.service.DictionaryService;
import com.shenzhen.recurit.service.ResumeService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DesiredPositionVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DesiredPositionServiceImpl implements DesiredPositionService {
    @Resource
    private DesiredPositionMapper desiredPositionMapper;
    @Resource
    private DictionaryService dictionaryService;
    @Resource
    private ResumeService resumeService;

    @Override
    @Transactional
    public ResultVO saveDesiredPosition(DesiredPositionVO desiredPositionVO) {
        setDesiredPositionInfo(desiredPositionVO,true);
        desiredPositionMapper.saveDesiredPosition(desiredPositionVO);
        setResumeRecord();
        if(desiredPositionVO.getId()> NumberEnum.ZERO.getValue()){
            DesiredPositionPojo desiredPositionPojo=getDesiredPosition(desiredPositionVO.getId());
            return ResultVO.success(desiredPositionPojo);
        }
        return ResultVO.error(desiredPositionVO);
    }

    private void setResumeRecord(){
        UserVO user = ThreadLocalUtils.getUser();
        if(EmptyUtils.isNotEmpty(user)&&EmptyUtils.isNotEmpty(user.getUserCode())){
            resumeService.updateRecentTimeByUserCode(user.getUserCode());
        }
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
    @Transactional
    public int deleteDesiredPositionById(int id) {
        setResumeRecord();
        return desiredPositionMapper.deleteDesiredPositionById(id);
    }

    @Override
    public List<DesiredPositionPojo> getDesiredPositionuserCode(String userCode) {
        List<DesiredPositionPojo> listDesiredPosition = desiredPositionMapper.getDesiredPositionuserCode(userCode);
        if(EmptyUtils.isNotEmpty(listDesiredPosition)){
            listDesiredPosition.forEach(disired->{
                setDesiredPosition(disired);
            });
        }
        return listDesiredPosition;
    }

    @Override
    @Transactional
    public int updateDesiredPosition(DesiredPositionVO desiredPositionVO) {
        setResumeRecord();
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
