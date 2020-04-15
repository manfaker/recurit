package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.EducationExperiencesPojo;
import com.shenzhen.recurit.vo.EducationExperienceVO;

import java.util.List;

public interface EducationExperienceMapper {
    void saveEducationExperince(EducationExperienceVO educationExperienceVO);

    int deleteEducationExperinceById(int id);

    int updateEducationExperince(EducationExperienceVO educationExperienceVO);

    EducationExperiencesPojo getEducationExperinceById(int id);

    List<EducationExperiencesPojo> getEducationExperinceUserCode(String userCode);
}
