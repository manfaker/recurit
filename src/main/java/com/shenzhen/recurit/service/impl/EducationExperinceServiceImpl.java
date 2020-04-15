package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.EducationExperinceMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.service.EducationExperinceService;
import com.shenzhen.recurit.service.ResumeService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.EducationExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class EducationExperinceServiceImpl implements EducationExperinceService {

    @Resource
    private EducationExperinceMapper educationExperinceMapper;
    @Resource
    private ResumeService resumeService;

    @Override
    @Transactional
    public ResultVO saveEducationExperince(EducationExperienceVO educationExperienceVO) {
        setEducationExperince(educationExperienceVO,true);
        educationExperinceMapper.saveEducationExperince(educationExperienceVO);
        setResumeRecord();
        if(educationExperienceVO.getId()> NumberEnum.ZERO.getValue()){
            EducationExperiencesPojo educationExperiencesPojo =getEducationExperinceById(educationExperienceVO.getId());
            return ResultVO.success(educationExperiencesPojo);
        }
        return ResultVO.error(educationExperienceVO);
    }

    private void setResumeRecord(){
        UserVO user = ThreadLocalUtils.getUser();
        if(EmptyUtils.isNotEmpty(user)&&EmptyUtils.isNotEmpty(user.getUserCode())){
            resumeService.updateRecentTimeByUserCode(user.getUserCode());
        }
    }

    private void setEducationExperince(EducationExperienceVO educationExperienceVO,boolean flag){
        if(EmptyUtils.isNotEmpty(educationExperienceVO)){
            UserVO user = ThreadLocalUtils.getUser();
            if(flag){
                educationExperienceVO.setUserCode(user.getUserCode());
                educationExperienceVO.setCreater(user.getUserName());
                educationExperienceVO.setCreateDate(new Date());
            }
            educationExperienceVO.setUpdater(user.getUserName());
            educationExperienceVO.setUpdateDate(new Date());
        }
    }
    @Override
    @Transactional
    public int deleteEducationExperinceById(int id) {
        setResumeRecord();
        return educationExperinceMapper.deleteEducationExperinceById(id);
    }

    @Override
    @Transactional
    public int updateEducationExperince(EducationExperienceVO educationExperienceVO) {
        setResumeRecord();
        return educationExperinceMapper.updateEducationExperince(educationExperienceVO);
    }

    @Override
    public EducationExperiencesPojo getEducationExperinceById(int id) {
        return educationExperinceMapper.getEducationExperinceById(id);
    }
}
