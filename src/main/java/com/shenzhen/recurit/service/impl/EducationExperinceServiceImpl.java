package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.dao.EducationExperienceMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.service.DictionaryService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EducationExperinceServiceImpl implements EducationExperinceService {

    @Resource
    private EducationExperienceMapper educationExperinceMapper;
    @Resource
    private ResumeService resumeService;
    @Resource
    private DictionaryService dictionaryService;

    @Override
    @Transactional
    public ResultVO saveEducationExperince(EducationExperienceVO educationExperienceVO) {
        setEducationExperinceInfo(educationExperienceVO,true);
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

    private void setEducationExperinceInfo(EducationExperienceVO educationExperienceVO,boolean flag){
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
        setEducationExperinceInfo(educationExperienceVO,false);
        return educationExperinceMapper.updateEducationExperince(educationExperienceVO);
    }

    private void setEducationExperince(EducationExperiencesPojo educationExperiencesPojo){
        if(EmptyUtils.isNotEmpty(educationExperiencesPojo)){
            if(EmptyUtils.isNotEmpty(educationExperiencesPojo.getExperience())){
                educationExperiencesPojo.setExperienceDict(dictionaryService.getSignleByDictNumber(InformationConstant.EXPERIENCE,educationExperiencesPojo.getExperience()));
            }
        }
    }

    @Override
    public EducationExperiencesPojo getEducationExperinceById(int id) {
        EducationExperiencesPojo educationExperiencesPojo = educationExperinceMapper.getEducationExperinceById(id);
        setEducationExperince(educationExperiencesPojo);
        return educationExperiencesPojo;
    }

    @Override
    public List<EducationExperiencesPojo> getEducationExperinceUserCode(String userCode) {
        List<EducationExperiencesPojo> listEducation =educationExperinceMapper.getEducationExperinceUserCode(userCode);
        if(EmptyUtils.isNotEmpty(listEducation)){
            listEducation.forEach(educationExperiencesPojo -> {
                setEducationExperince(educationExperiencesPojo);
            });
        }else{
            listEducation=new ArrayList<>();
        }
        return listEducation;
    }
}
