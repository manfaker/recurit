package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;

public interface SocialSecurityInfoService {

    Object calculatePrice(String jsonData);

    SocialSecurityInfoPojo saveSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);

    int updateSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);


    SocialSecurityInfoPojo getSocialSecuritInfoById(int id);
}
