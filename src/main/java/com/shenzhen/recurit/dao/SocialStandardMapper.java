package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.SocialStandardPojo;
import com.shenzhen.recurit.vo.SocialStandardVO;

import java.util.List;

public interface SocialStandardMapper {


    void saveSocialStandard(SocialStandardVO socialStandardVO);

    List<SocialStandardPojo> getAllSocialStandard();

    SocialStandardPojo getSocialStandardById(int id);
}
