package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.ResumePojo;
import com.shenzhen.recurit.vo.ResumeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查看所有发布简历的人员
     * @param resumeVO
     * @return
     */
    ResumePojo getResumeAllByCondition(ResumeVO resumeVO);

    int updateRecentTimeByUserCode(ResumeVO resumeVO);

    /**
     * 获取当前hr下面的所有简历信息
     *
     * @param userCode
     * @param positionId 不为空，当前hr发布职位下的所有投递人员简历信息
     * @return 所有简历信息
     */
    List<ResumePojo> getApplyResume(@Param("userCode") String userCode,@Param("positionId") Integer positionId);

    ResumePojo getCheckedResumes(String userCode);
}
