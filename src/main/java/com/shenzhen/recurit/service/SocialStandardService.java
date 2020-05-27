package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.SocialStandardPojo;
import com.shenzhen.recurit.vo.SocialStandardVO;

import java.util.List;

public interface SocialStandardService {
    SocialStandardPojo saveSocialStandard(SocialStandardVO socialStandardVO);

    List<SocialStandardPojo> getAllSocialStandard();

    SocialStandardPojo getSocialStandardById(int id);
}
