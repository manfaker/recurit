package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.SocialStandardMapper;
import com.shenzhen.recurit.pojo.SocialStandardPojo;
import com.shenzhen.recurit.service.SocialStandardService;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.SocialStandardVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SocialStandardServiceImpl implements SocialStandardService {

    @Resource
    private SocialStandardMapper socialStandardMapper;

    @Override
    public SocialStandardPojo saveSocialStandard(SocialStandardVO socialStandardVO) {
        setSocialStandardVO(socialStandardVO,true);
        socialStandardMapper.saveSocialStandard(socialStandardVO);
        return getSocialStandardById(socialStandardVO.getId());
    }

    private void setSocialStandardVO(SocialStandardVO socialStandardVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            socialStandardVO.setCreateDate(new Date());
            socialStandardVO.setCreater(user.getUserName());
        }
        socialStandardVO.setUpdateDate(new Date());
        socialStandardVO.setUpdater(user.getUserName());
    }
    
    
    @Override
    public List<SocialStandardPojo> getAllSocialStandard() {
        return socialStandardMapper.getAllSocialStandard();
    }

    @Override
    public SocialStandardPojo getSocialStandardById(int id) {
        return socialStandardMapper.getSocialStandardById(id);
    }
}
