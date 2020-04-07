package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.JobExperiencePojo;
import com.shenzhen.recurit.vo.JobExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.util.List;

public interface JobExperienceService {

    /**
     * 新增个人经历
     * @param jobExperienceVO
     */
    void addJobExperience(JobExperienceVO jobExperienceVO);

    /**
     * 新增个人经历带校验
     * @param jobExperienceVO
     */
    ResultVO saveJobExperience(JobExperienceVO jobExperienceVO);

    /**
     * 通过id获取个人经历信息
     * @param id
     * @return
     */
    JobExperiencePojo getJobExperienceById(int id);

    /**
     * 删除个人经历信息
     * @param id
     * @return
     */
    int deleteJobExperienceById(int id);

    /**
     * 通过id获取个人经历信息
     * @param userCode
     * @return
     */
    List<JobExperiencePojo> getJobExperienceByUserCode(String userCode);

    /**
     * 根据经历修改个人信息
     * @param jobExperienceVO
     */
    int updateJobExperience(JobExperienceVO jobExperienceVO);
}
