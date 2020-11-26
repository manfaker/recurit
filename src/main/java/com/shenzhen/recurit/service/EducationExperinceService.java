package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.vo.EducationExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.util.List;

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

    /**
     * 根据id查询个人经历
     * @param userCode
     * @return
     */
    List<EducationExperiencesPojo> getEducationExperinceUserCode(String  userCode);

    /**
     * 根据用户编码删除用户信息
     * @param userCode
     * @return
     */
    int deleteByUserCode(String  userCode);
}
