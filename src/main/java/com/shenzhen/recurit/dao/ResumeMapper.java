package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.ResumePojo;
import com.shenzhen.recurit.vo.ResumeVO;

public interface ResumeMapper {

    /**
     * 根据id 删除简历信息
     * @param id
     * @return
     */
    int deleteById(int id);

    /**
     * 根据userCode获取简历信息
     * @param userCode
     * @return
     */
    ResumePojo getByUserCode(String userCode);

    /**
     * 根据id 获取
     * @param id
     * @return
     */
    ResumePojo getById(int id);

    /**
     * 新增简历信息
     * @param resumeVO
     */
    void addResume(ResumeVO resumeVO);

    /**
     * 修改简历信息
     * @param resumeVO
     * @return
     */
    int updateResume(ResumeVO resumeVO);
}
