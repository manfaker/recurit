package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SocialSecurityInfoMapper {
    
    void saveSocialSecurityInfo(SocialSecurityInfoVO socialSecurityInfoVO);

    int updateSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);

    SocialSecurityInfoPojo getSocialSecuritInfoById(int id);

    int deleteSecuritInfoById(int id);

    List<SocialSecurityInfoPojo> getAllSocialSecuritInfoByIds(@Param("ids") List<Integer> ids);

    int batchUpdateSocialSecuritInfo(@Param("ids") List<Integer> ids,@Param("ids") int orderInfoId);

    int deleteByOrderInfoId(int orderInfoId);

    List<SocialSecurityInfoPojo> getAllSecuritInfo(String userCode);

    int batchRemoveOrderInfoIds(@Param("ids") List<Integer> ids);

    List<SocialSecurityInfoPojo> getAllSecuritInfoByOrderInfoId(int orderInfoId);

    List<SocialSecurityInfoPojo> getAllSecuritInfoByIdCard(String idCard);
}
