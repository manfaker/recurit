package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.dao.ResumeMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.pojo.ResumePojo;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.service.*;
import com.shenzhen.recurit.utils.EmailUtils;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.ResumeVO;
import com.shenzhen.recurit.vo.UserVO;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Resource
    private ResumeMapper resumeMapper;
    @Resource
    private UserService userService;
    @Resource
    private DictionaryService dictionaryService;
    @Resource
    private JobExperienceService jobExperienceService;
    @Resource
    private DesiredPositionService desiredPositionService;
    @Resource
    private EducationExperinceService educationExperinceService;

    @Override
    public int deleteById(int id) {
        return resumeMapper.deleteById(id);
    }

    /**
     *
     * @param resumeVO
     * @param flag true 保存 false 修改
     */
    private void setResumentBaseInfo(ResumeVO resumeVO,boolean flag){
        if(EmptyUtils.isEmpty(resumeVO)){
            return;
        }
        UserVO userVO = ThreadLocalUtils.getUser();
        if(flag){
            if(EmptyUtils.isEmpty(resumeVO.getUserCode())){
                resumeVO.setUserCode(userVO.getUserCode());
            }
            resumeVO.setCreater(userVO.getUserName());
            resumeVO.setCreateDate(new Date());
        }
        //resumeVO.setWorkingLife(getCalculationAge(resumeVO.getGraduationTime()));
        resumeVO.setUpdater(userVO.getUserName());
        resumeVO.setUpdateDate(new Date());
    }

    /**
     * 计算工作年限
     * @param endTime
     * @return
     */
    private int getCalculationAge(Date endTime){
        if(EmptyUtils.isEmpty(endTime)){
            return NumberEnum.ZERO.getValue();
        }
        int workingLife = NumberEnum.ZERO.getValue();
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(endTime);
        Calendar newCalendar = Calendar.getInstance();
        workingLife = newCalendar.get(Calendar.YEAR)-birthCalendar.get(Calendar.YEAR);
        if(newCalendar.get(Calendar.MONTH)>birthCalendar.get(Calendar.MONTH)){
            workingLife++;
        }else if (newCalendar.get(Calendar.MONTH)==birthCalendar.get(Calendar.MONTH)
                &&newCalendar.get(Calendar.DAY_OF_MONTH)>birthCalendar.get(Calendar.DAY_OF_MONTH)){
            workingLife++;
        }else{
        }
        return workingLife;
    }

    @Override
    public ResumePojo getByUserCode(String userCode) {
        return resumeMapper.getByUserCode(userCode);
    }

    @Override
    public void addResume(ResumeVO resumeVO) {
        setResumentBaseInfo(resumeVO,true);
        resumeMapper.addResume(resumeVO);
    }

    @Override
    public void initResumenTemplate(ResumeVO resumeVO) {
        //setResumentBaseInfo(resumeVO,false);
        resumeVO.setCreateDate(new Date());
        resumeVO.setUpdateDate(new Date());
        resumeMapper.addResume(resumeVO);
    }

    @Override
    public int updateResume(ResumeVO resumeVO) {
        setResumentBaseInfo(resumeVO,false);
         return resumeMapper.updateResume(resumeVO);
    }

    @Override
    public ResultVO saveResume(ResumeVO resumeVO) {
        addResume(resumeVO);
        if(resumeVO.getId()> NumberEnum.ZERO.getValue()){
            return ResultVO.success(getById(resumeVO.getId()));
        }else{
            return ResultVO.error(resumeVO);
        }
    }

    @Override
    public UserPojo getByCurrUser() {
        UserVO userVO = ThreadLocalUtils.getUser();
        UserPojo userPojo = new UserPojo();
        if(EmptyUtils.isNotEmpty(userVO)){
            setUserPojo(userVO,userPojo);
        }
        return userPojo;
    }

    private void setUserPojo(UserVO userVO,UserPojo userPojo){
        try {
            PropertyUtils.copyProperties(userPojo,userVO);
            userPojo.setResumePojo(getByUserCode(userVO.getUserCode()));
            userPojo.setListJobExperiences(jobExperienceService.getJobExperienceByUserCode(userVO.getUserCode()));
            userPojo.setListDesiredPosition(desiredPositionService.getDesiredPositionuserCode(userVO.getUserCode()));
            userPojo.setListEducationExperience(educationExperinceService.getEducationExperinceUserCode(userVO.getUserCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserPojo getResumeInfoByUserCode(String userCode){
        UserVO userVO = userService.getUserCode(userCode);
        UserPojo userPojo = new UserPojo();
        setUserPojo(userVO,userPojo);
        return userPojo;
    }

    @Override
    public ResumePojo getById(int id) {
        return resumeMapper.getById(id);
    }

    @Override
    public ResumePojo getResumeAllByCondition(ResumeVO resumeVO) {
        return resumeMapper.getResumeAllByCondition(resumeVO);
    }

    @Override
    public int updateRecentTimeByUserCode(String userCode) {
        ResumeVO resumeVO = new ResumeVO();
        resumeVO.setUserCode(userCode);
        setResumentBaseInfo(resumeVO,false);
        return resumeMapper.updateRecentTimeByUserCode(resumeVO);
    }

    @Override
    public List<ResumePojo> getApplyResume(Integer positionId) {
        UserVO user = ThreadLocalUtils.getUser();
        List<ResumePojo> listResume = resumeMapper.getApplyResume(user.getUserCode(), positionId);
        if(EmptyUtils.isNotEmpty(listResume)){
            listResume.forEach(resumePojo -> {
                setResume(resumePojo);
            });
        }
        return listResume;
    }

    private void setResume(ResumePojo resume){
        if(EmptyUtils.isNotEmpty(resume)){
            if(EmptyUtils.isNotEmpty(resume.getEducation())){
                resume.setEducationDict(dictionaryService.getSignleByDictNumber(InformationConstant.EDUCATION,resume.getEducation()));
            }
            if(EmptyUtils.isNotEmpty(resume.getExperience())){
                resume.setExperienceDict(dictionaryService.getSignleByDictNumber(InformationConstant.EXPERIENCE,resume.getExperience()));
            }
            if(EmptyUtils.isNotEmpty(resume.getSalary()
            )){
                resume.setSalaryDict(dictionaryService.getSignleByDictNumber(InformationConstant.EDUCATION,resume.getSalary()));
            }
        }
    }


    public ResultVO sendResumeEmail(String userCode){
        UserVO userVO = ThreadLocalUtils.getUser();
        UserPojo userPojo = getResumeInfoByUserCode(userCode);
        if(EmptyUtils.isNotEmpty(userVO.getEmail())){
            EmailUtils.sendResume(userVO.getEmail(),userPojo);
            return ResultVO.success("邮件已发送，请注意查收");
        }else{
            return ResultVO.success("邮件发送失败，请跟新邮箱");
        }

    }

    @Override
    public ResumePojo getCheckedResumes(String userCode) {
        if(EmptyUtils.isEmpty(userCode)){
            return new ResumePojo();
        }
        ResumePojo resumePojo = resumeMapper.getCheckedResumes(userCode);
        return resumePojo;
    }
}
