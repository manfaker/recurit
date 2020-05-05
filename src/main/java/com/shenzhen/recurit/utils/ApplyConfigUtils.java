package com.shenzhen.recurit.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.text.DecimalFormat;

public class ApplyConfigUtils {

        private static VaribaleUtils varibaleUtils=null;

        private static void init(){
            if(EmptyUtils.isEmpty(varibaleUtils)){
                varibaleUtils=SpringUtils.getBean(VaribaleUtils.class);
            }
        }

        public static ResultVO payOrderNo(String category, OrderInfoVO orderInfoVO){
            init();
            if(InformationConstant.ALIPAY.equals(category)){
                return alipayPay(orderInfoVO);
            }else{
                return null;
            }
        }


        private static AlipayClient applipayClient(){
            return new DefaultAlipayClient(
                    varibaleUtils.getGeteWayUrl(),
                    varibaleUtils.getAppId(),
                    varibaleUtils.getPrivateKey(),
                    InformationConstant.DATA_TYPE,
                    InformationConstant.UTF_8,
                    varibaleUtils.getPublicKey(),
                    varibaleUtils.getSignType());
        }

        public static ResultVO preCreate(OrderInfoVO orderInfoVO){
            init();
            AlipayClient alipayClient = applipayClient();
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setBizContent(getBizContent(orderInfoVO));
            AlipayTradePrecreateResponse response = null;
            try {
                response = alipayClient.execute(request);
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            return ResultVO.success(response);
        }
        /**
         * 支付宝支付
         */
        private static ResultVO alipayPay( OrderInfoVO orderInfoVO){
            AlipayClient alipayClient = applipayClient();
            AlipayTradeCreateRequest  payRequest = new AlipayTradeCreateRequest  ();
            String bizContent = getBizContent(orderInfoVO);
            payRequest.setBizContent(bizContent);
            payRequest.setReturnUrl(varibaleUtils.getReturnUrl());
            //在公共参数中设置回跳和通知地址
            payRequest.setNotifyUrl(varibaleUtils.getNotifyUrl());
            AlipayTradeCreateResponse response= null;
            try {
                response= alipayClient.execute(payRequest);
            } catch (AlipayApiException e) {
                e.printStackTrace();
                return ResultVO.error();
            }
            if(response.isSuccess()){
                return ResultVO.success("支付成功");
            }else{
                return ResultVO.error(response.getSubMsg());
            }
        }

    /**
     * 支付宝查询订单
     */
    private static ResultVO alipayQuery(OrderInfoVO orderInfoVO){
        AlipayClient alipayClient = applipayClient();
        AlipayTradeQueryRequest payRequest = new AlipayTradeQueryRequest();
        String bizContent = getBizContent(orderInfoVO);
        payRequest.setBizContent(bizContent);
        payRequest.setReturnUrl(varibaleUtils.getReturnUrl());
        //在公共参数中设置回跳和通知地址
        payRequest.setNotifyUrl(varibaleUtils.getNotifyUrl());
        AlipayTradeQueryResponse response= null;
        try {
            response= alipayClient.execute(payRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ResultVO.error();
        }
        if(EmptyUtils.isNotEmpty(response)){
            return ResultVO.success();
        }else{
            return ResultVO.error();
        }
    }

        private static String getBizContent(OrderInfoVO orderInfoVO){
            JSONObject bizContentJson = new JSONObject();
            if(orderInfoVO.getTotalAmount()> NumberEnum.ZERO.getValue()){
                double money = orderInfoVO.getTotalAmount()*0.01;
                bizContentJson.put("total_amount",money);
            }
            bizContentJson.put("out_trade_no",orderInfoVO.getOutTradeNo());
            bizContentJson.put("subject","iphone20");
            bizContentJson.put("body","ssssiphone20ssss");
            //bizContentJson.put("passback_params","merchantBizType%3d3C%26merchantBizNo%3d2016010101111");
            return JSON.toJSONString(bizContentJson);
        }
}
