package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.ResumeMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.ResumePojo;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.service.ResumeService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.ResumeVO;
import com.shenzhen.recurit.vo.UserVO;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Resource
    private ResumeMapper resumeMapper;

    @Override
    public int deleteById(int id) {
        return resumeMapper.deleteById(id);
    }

    /**
     *
     * @param resumeVO
     * @param flag true 保存 false 修改
     */
    private void setResumentBaseInfo(ResumeVO resumeVO,boolean flag){
        if(EmptyUtils.isEmpty(resumeVO)){
            return;
        }
        UserVO userVO = ThreadLocalUtils.getUser();
        if(flag){
            if(EmptyUtils.isEmpty(resumeVO.getUserCode())){
                resumeVO.setUserCode(userVO.getUserCode());
            }
            resumeVO.setCreater(userVO.getUserName());
            resumeVO.setCreateDate(new Date());
        }
        resumeVO.setUpdater(userVO.getUserName());
        resumeVO.setUpdateDate(new Date());
    }

    @Override
    public ResumePojo getByUserCode(String userCode) {
        return resumeMapper.getByUserCode(userCode);
    }

    @Override
    public void addResume(ResumeVO resumeVO) {
        setResumentBaseInfo(resumeVO,true);
        resumeMapper.addResume(resumeVO);
    }

    @Override
    public int updateResume(ResumeVO resumeVO) {
        setResumentBaseInfo(resumeVO,false);
         return resumeMapper.updateResume(resumeVO);
    }

    @Override
    public ResultVO saveResume(ResumeVO resumeVO) {
        addResume(resumeVO);
        if(resumeVO.getId()> NumberEnum.ZERO.getValue()){
            return ResultVO.success(getById(resumeVO.getId()));
        }else{
            return ResultVO.error(resumeVO);
        }
    }

    @Override
    public UserPojo getByCurrUser() {
        UserVO userVO = ThreadLocalUtils.getUser();
        UserPojo userPojo = new UserPojo();
        try {
            PropertyUtils.copyProperties(userPojo,userVO);
            userPojo.setResumePojo(getByUserCode(userVO.getUserCode()));
            return userPojo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPojo;
    }

    @Override
    public ResumePojo getById(int id) {
        return resumeMapper.getById(id);
    }
}
