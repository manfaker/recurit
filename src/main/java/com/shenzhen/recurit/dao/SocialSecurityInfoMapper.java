package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;

public interface SocialSecurityInfoMapper {
    
    void saveSocialSecurityInfo(SocialSecurityInfoVO socialSecurityInfoVO);

    int updateSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);

    SocialSecurityInfoPojo getSocialSecuritInfoById(int id);
}
