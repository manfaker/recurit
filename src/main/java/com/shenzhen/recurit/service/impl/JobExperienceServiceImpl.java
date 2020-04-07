package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.JobExperienceMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.JobExperiencePojo;
import com.shenzhen.recurit.service.JobExperienceService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.JobExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class JobExperienceServiceImpl implements JobExperienceService {

    @Resource
    private JobExperienceMapper jobExperienceMapper;


    @Override
    public void addJobExperience(JobExperienceVO jobExperienceVO) {
        setJobExperienceBaseInfo(jobExperienceVO,true);
        jobExperienceMapper.addJobExperience(jobExperienceVO);
    }

    /**
     *
     * @param jobExperienceVO
     * @param flag true 新增 false 修改
     */
    private void setJobExperienceBaseInfo(JobExperienceVO jobExperienceVO,boolean flag){
        if(EmptyUtils.isEmpty(jobExperienceVO)){
            return;
        }
        UserVO userVO = ThreadLocalUtils.getUser();
        if(flag){
            if(EmptyUtils.isEmpty(jobExperienceVO.getUserCode())){
                jobExperienceVO.setUserCode(userVO.getUserCode());
            }
            jobExperienceVO.setCreater(userVO.getUserName());
            jobExperienceVO.setCreateDate(new Date());
        }
        jobExperienceVO.setUpdater(userVO.getUserName());
        jobExperienceVO.setUpdateDate(new Date());
    }

    @Override
    public ResultVO saveJobExperience(JobExperienceVO jobExperienceVO) {
        addJobExperience(jobExperienceVO);
        if(jobExperienceVO.getId()> NumberEnum.ZERO.getValue()){
            return ResultVO.success(getJobExperienceById(jobExperienceVO.getId()));
        }else{
            return ResultVO.error(jobExperienceVO);
        }
    }

    @Override
    public JobExperiencePojo getJobExperienceById(int id) {
        JobExperiencePojo jobExperiencePojo=jobExperienceMapper.getJobExperienceById(id);
        return jobExperiencePojo;
    }

    @Override
    public int deleteJobExperienceById(int id) {
        return jobExperienceMapper.deleteJobExperienceById(id);
    }

    @Override
    public List<JobExperiencePojo> getJobExperienceByUserCode(String userCode) {
        return jobExperienceMapper.getJobExperienceByUserCode(userCode);
    }

    @Override
    public int updateJobExperience(JobExperienceVO jobExperienceVO) {
        return jobExperienceMapper.updateJobExperience(jobExperienceVO);
    }
}
