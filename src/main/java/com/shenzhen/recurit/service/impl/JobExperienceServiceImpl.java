package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.JobExperienceMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.JobExperiencePojo;
import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.service.JobExperienceService;
import com.shenzhen.recurit.service.LabelService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.JobExperienceVO;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class JobExperienceServiceImpl implements JobExperienceService {

    @Resource
    private JobExperienceMapper jobExperienceMapper;
    @Resource
    private LabelService labelService;


    @Override
    @Transactional
    public void addJobExperience(JobExperienceVO jobExperienceVO) {
        setJobExperienceBaseInfo(jobExperienceVO,true);
        jobExperienceMapper.addJobExperience(jobExperienceVO);
        JobExperiencePojo jobExperiencePojo = getJobExperienceById(jobExperienceVO.getId());
        savePositions(jobExperienceVO.getLabels(),jobExperiencePojo);
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
        setInfoToJobExperience(jobExperiencePojo);
        return jobExperiencePojo;
    }


    @Override
    public int deleteJobExperienceById(int id) {
        return jobExperienceMapper.deleteJobExperienceById(id);
    }

    @Override
    public List<JobExperiencePojo> getJobExperienceByUserCode(String userCode) {
        List<JobExperiencePojo> listJobExperience = jobExperienceMapper.getJobExperienceByUserCode(userCode);
        setListInfoToJobExperience(listJobExperience);
        return listJobExperience;
    }

    private void setListInfoToJobExperience(List<JobExperiencePojo> listInfoToJobExperience){
        if(EmptyUtils.isNotEmpty(listInfoToJobExperience)&&listInfoToJobExperience.size()>NumberEnum.ZERO.getValue()){
            listInfoToJobExperience.forEach(jobExperiencePojo->{
                setInfoToJobExperience(jobExperiencePojo);
            });
        }
    }

    private void setInfoToJobExperience(JobExperiencePojo jobExperiencePojo){
          if(EmptyUtils.isEmpty(jobExperiencePojo))  {
              return;
          }
        jobExperiencePojo.setListLabel(labelService.queryByRelationId(InformationConstant.JOB_EXPERIENCE,jobExperiencePojo.getId()));
    }

    @Override
    @Transactional
    public int updateJobExperience(JobExperienceVO jobExperienceVO) {
        int result = jobExperienceMapper.updateJobExperience(jobExperienceVO);
        if(result>NumberEnum.ZERO.getValue()){
            JobExperiencePojo jobExperiencePojo = jobExperienceMapper.getJobExperienceById(jobExperienceVO.getId());
            savePositions(jobExperienceVO.getLabels(),jobExperiencePojo);
        }
        return result;
    }

    private void savePositions(String labels,JobExperiencePojo jobExperiencePojo){
        if(EmptyUtils.isNotEmpty(labels)){
            List<LabelVO> listLabel = new ArrayList<>();
            List<String> listStr = Arrays.asList(labels.split(OrdinaryConstant.SYMBOL_4));
            getAssembleLabels(listStr,listLabel,jobExperiencePojo);
            labelService.saveBatchLabel(listLabel);
        }
    }
    private void getAssembleLabels(List<String> listStr, List<LabelVO> listLabel, JobExperiencePojo jobExperiencePojo){
        if(EmptyUtils.isEmpty(listStr)||listStr.isEmpty()){
            return;
        }
        for(String str : listStr){
            if(EmptyUtils.isEmpty(str)){
                continue;
            }
            LabelVO labelVO = new LabelVO();
            labelVO.setLabelName(str);
            labelVO.setCategory(InformationConstant.JOB_EXPERIENCE);
            labelVO.setRelationId(jobExperiencePojo.getId());
            listLabel.add(labelVO);
        }

    }
}
