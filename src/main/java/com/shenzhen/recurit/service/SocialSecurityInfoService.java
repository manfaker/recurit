package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;

import java.util.List;

public interface SocialSecurityInfoService {

    /**
     * 计算社保价格
     * @param jsonData
     * @return
     */
    Object calculatePrice(String jsonData);

    /**
     * 保存单个社保信息
     * @param socialSecurityInfoVO
     * @return
     */
    SocialSecurityInfoPojo saveSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);

    /**
     * 根据社保id修改社保信息
     * @param socialSecurityInfoVO
     * @return
     */
    int updateSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);


    /**
     * 根据社保id查询社保信息
     * @param id
     * @return
     */
    SocialSecurityInfoPojo getSocialSecuritInfoById(int id);

    /**
     * 根据id删除社保信息
     * @param id
     * @return
     */
    int deleteSecuritInfoById(int id);

    /**
     * 根据社保id批量查询社保信息
     * @param ids
     * @return
     */
    List<SocialSecurityInfoPojo> getAllSocialSecuritInfoByIds(List<Integer> ids);

    /**
     * 批量修改绑定社保订单
     * @param ids
     * @return
     */
    int batchUpdateSocialSecuritInfo(List<Integer> ids,int orderInfoId);

    /**
     * 根据订单批量删除商品信息
     * @param orderInfoId
     * @return
     */
    int deleteByOrderInfoId(int orderInfoId);

    /**
     * 批量移除购物车中订单
     * @param ids
     * @return
     */
    int batchRemoveOrderInfoIds(List<Integer> ids);

    /**
     * 获取购物车中所社保单
     * @return
     */
    List<SocialSecurityInfoPojo> getAllSecuritInfo();

    /**
     * 根据订单获取所有绑定的社保信息
     * @param orderInfoId
     * @return
     */
    List<SocialSecurityInfoPojo> getAllSecuritInfoByOrderInfoId(int orderInfoId);

    /**
     * 根据订单获取所有绑定的社保信息
     * @param idCard
     * @return
     */
    List<SocialSecurityInfoPojo> getAllSecuritInfoByIdCard(String idCard);

    /**
     * 获取套餐所交社保月数
     * @return
     */
    int totalMonth(SocialSecurityInfoVO socialSecurityInfoVO);

    SocialSecurityInfoPojo saveDirectSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO);
}
