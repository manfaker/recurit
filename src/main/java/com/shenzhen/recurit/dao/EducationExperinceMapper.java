package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.vo.EducationExperienceVO;

public interface EducationExperinceMapper {
    void saveEducationExperince(EducationExperienceVO educationExperienceVO);

    int deleteEducationExperinceById(int id);

    int updateEducationExperince(EducationExperienceVO educationExperienceVO);

    EducationExperiencesPojo getEducationExperinceById(int id);
}
