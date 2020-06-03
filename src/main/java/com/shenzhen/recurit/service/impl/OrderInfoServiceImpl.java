package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.OrderInfoMapper;
import com.shenzhen.recurit.pojo.OrderInfoPojo;
import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.service.OrderInfoService;
import com.shenzhen.recurit.service.SocialSecurityInfoService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private SocialSecurityInfoService socialSecurityInfoService;


    @Override
    @Transactional
    public OrderInfoPojo saveOrderInfo(OrderInfoVO orderInfoVO) {
        setOrderInfo(orderInfoVO,true);
        orderInfoMapper.saveOrderInfo(orderInfoVO);
        List<Integer> listSocialInfo = strToList(orderInfoVO.getSocialInfoIds());
        socialSecurityInfoService.batchUpdateSocialSecuritInfo(listSocialInfo,orderInfoVO.getId());
        return getOrderInfo(orderInfoVO.getId());
    }



    private List<Integer> strToList(String socialInfoIds){
        if(EmptyUtils.isEmpty(socialInfoIds)){
            return null;
        }
        List<Integer> listSocialInfo= new ArrayList<>();
        for(String str : socialInfoIds.split(OrdinaryConstant.SYMBOL_4)){
            if(EmptyUtils.isNotEmpty(str)){
                listSocialInfo.add(Integer.valueOf(str));
            }
        }
        return listSocialInfo;
    }


    private void setOrderInfo(OrderInfoVO orderInfoVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            orderInfoVO.setTradeNo(getTradeNo("ORDER",user));
            orderInfoVO.setOutTradeNo(getTradeNo("ZFB",user));
            orderInfoVO.setCreateDate(new Date());
            orderInfoVO.setCreater(user.getUserName());
        }
        orderInfoVO.setUpdateDate(new Date());
        orderInfoVO.setUpdater(user.getUserName());
    }

    private String getTradeNo(String pre,UserVO userVO){
        return  pre+"_"+userVO.getUserCode()+"_"+System.currentTimeMillis();
    }

    @Override
    @Transactional
    public int updateOrderInfo(OrderInfoVO orderInfoVO) {
        setOrderInfo(orderInfoVO,true);
        List<SocialSecurityInfoPojo> listBindingSocailInfo = socialSecurityInfoService.getAllSecuritInfoByOrderInfoId(orderInfoVO.getId());
        List<Integer> listSocialInfo = strToList(orderInfoVO.getSocialInfoIds());
        List<Integer> saveSocial = new ArrayList<>();
        List<Integer> deleteSocial = new ArrayList<>();
        getDeleteAndSaveSocialIds(listBindingSocailInfo,listSocialInfo,saveSocial,deleteSocial);
        socialSecurityInfoService.batchUpdateSocialSecuritInfo(saveSocial,orderInfoVO.getId());
        socialSecurityInfoService.batchRemoveOrderInfoIds(deleteSocial);
        return orderInfoMapper.updateOrderInfo(orderInfoVO);
    }

    private void getDeleteAndSaveSocialIds(List<SocialSecurityInfoPojo> listBindingSocailInfo,List<Integer> listSocialInfo,List<Integer> saveSocial,List<Integer> deleteSocial){
        if(EmptyUtils.isEmpty(listBindingSocailInfo)){
            saveSocial.addAll(listSocialInfo);
        }
        Set<Integer> oldSocailInfos = new HashSet<>();
        for(SocialSecurityInfoPojo socialSecurityInfoPojo:listBindingSocailInfo){
            oldSocailInfos.add(socialSecurityInfoPojo.getId());
            if(!listSocialInfo.contains(socialSecurityInfoPojo.getId())&&!deleteSocial.contains(socialSecurityInfoPojo.getId())){
                deleteSocial.add(socialSecurityInfoPojo.getId());
            }
        }
        if(EmptyUtils.isNotEmpty(listSocialInfo)){
            for(Integer socialInfoId : listSocialInfo){
                if(!oldSocailInfos.contains(socialInfoId)&&!saveSocial.contains(socialInfoId)){
                    saveSocial.add(socialInfoId);
                }
            }
        }
    }



    @Override
    public List<OrderInfoPojo> getAllOrderInfo(int payStatus) {
        List<OrderInfoPojo> listOrderInfo = orderInfoMapper.getAllOrderInfo(payStatus);
        return listOrderInfo;
    }

    @Override
    public OrderInfoPojo getOrderInfo(int id) {
        return  orderInfoMapper.getOrderInfo(id);
    }

    @Override
    public int deleteOrderInfo(int id) {
        return orderInfoMapper.deleteOrderInfo(id);
    }
}
