package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.ResumePojo;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.ResumeVO;
import com.shenzhen.recurit.vo.UserVO;

public interface ResumeService {

    /**
     * 根据id 删除
     * @param id
     * @return
     */
    int deleteById(int id);

    /**
     * 根据用户code 查询招聘信息
     * @param userCode
     * @return
     */
    ResumePojo getByUserCode(String userCode);

    /**
     * 添加简历信息
     * @param resumeVO
     */
    void addResume(ResumeVO resumeVO);

    /**
     * 修改简历信息，根据简历id
     * @param resumeVO
     */
    int updateResume(ResumeVO resumeVO);

    /**
     * 添加简历信息检查校验
     * @param resumeVO
     */
    ResultVO saveResume(ResumeVO resumeVO);


    /**
     * 根据当前用户获取简历信息
     */
    UserPojo getByCurrUser();


    /**
     * 根据id 获取
     * @param id
     * @return
     */
    ResumePojo getById(int id);

}
