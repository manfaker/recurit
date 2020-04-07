package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.JobExperiencePojo;
import com.shenzhen.recurit.vo.JobExperienceVO;

import java.util.List;

public interface JobExperienceMapper {
    /**
     * 新增
     * @param jobExperienceVO
     */
    void addJobExperience(JobExperienceVO jobExperienceVO);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    JobExperiencePojo getJobExperienceById(int id);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteJobExperienceById(int id);

    /**
     * 根据userCode获取个人经历
     * @param userCode
     * @return
     */
    List<JobExperiencePojo> getJobExperienceByUserCode(String userCode);

    /**
     * 根据id修改个人经历
     * @param jobExperienceVO
     * @return
     */
    int updateJobExperience(JobExperienceVO jobExperienceVO);
}
