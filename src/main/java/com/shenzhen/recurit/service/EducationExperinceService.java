package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.vo.EducationExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;

public interface EducationExperinceService {
    /**
     * 保存教育经历
     * @param educationExperienceVO
     * @return
     */
    ResultVO saveEducationExperince(EducationExperienceVO educationExperienceVO);

    /**
     * 删除教育经历
     * @param id
     * @return
     */
    int deleteEducationExperinceById(int id);

    /**
     * 修改教育经历
     * @param educationExperienceVO
     * @return
     */
    int updateEducationExperince(EducationExperienceVO educationExperienceVO);

    /**
     * 根据id查询个人经历
     * @param id
     * @return
     */
    EducationExperiencesPojo getEducationExperinceById(int id);
}
