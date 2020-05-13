package com.shenzhen.recurit.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;

public class ApplyConfigUtils {

        private static VaribaleUtils varibaleUtils=null;

        private static void init(){
            if(EmptyUtils.isEmpty(varibaleUtils)){
                varibaleUtils=SpringUtils.getBean(VaribaleUtils.class);
            }
        }

    /**
     * 支付接口
     * @param category
     * @param orderInfoVO
     * @return
     */
        public static ResultVO payOrderNo(String category, OrderInfoVO orderInfoVO){
            init();
            if(InformationConstant.ALIPAY.equals(category)){
                return alipayPay(orderInfoVO);
            }else{
                return null;
            }
        }

    /**
     * 生成二维码
     * @param category
     * @param orderInfoVO
     * @return
     */
        public static ResultVO preCreate(String category, OrderInfoVO orderInfoVO){
            init();
            if(InformationConstant.ALIPAY.equals(category)){
                return alipyPreCreate(orderInfoVO);
            }else{
                return null;
            }
        }
    /**
     * 关单
     * @param category
     * @param orderInfoVO
     * @return
     */
    public static ResultVO closeTrade(String category, OrderInfoVO orderInfoVO){
        init();
        if(InformationConstant.ALIPAY.equals(category)){
            return closeTrade(orderInfoVO);
        }else{
            return null;
        }
    }

    public static Object refund(String category, OrderInfoVO orderInfoVO) {
        init();
        if(InformationConstant.ALIPAY.equals(category)){
            return refund(orderInfoVO);
        }else{
            return null;
        }
    }
    /**
     * 查询交易信息
     * @param category
     * @param orderInfoVO
     * @return
     */
        public static ResultVO alipayQuery(String category, OrderInfoVO orderInfoVO){
            init();
            if(InformationConstant.ALIPAY.equals(category)){
                return alipayQuery(orderInfoVO);
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


    public static ResultVO refund(OrderInfoVO orderInfoVO){
        AlipayClient alipayClient = applipayClient();
        AlipayTradeCloseRequest  request = new AlipayTradeCloseRequest();
        request.setBizContent(getBizContent(orderInfoVO));
        AlipayTradeCloseResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultVO.success(response);
    }

    public static ResultVO closeTrade(OrderInfoVO orderInfoVO){ AlipayClient alipayClient = applipayClient();
    AlipayTradeCloseRequest  request = new AlipayTradeCloseRequest();
    request.setBizContent(getBizContent(orderInfoVO));
    AlipayTradeCloseResponse response = null;
    try {
        response = alipayClient.execute(request);
    } catch (AlipayApiException e) {
        e.printStackTrace();
    }
    return ResultVO.success(response); }

        public static ResultVO alipyPreCreate(OrderInfoVO orderInfoVO){
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
            payRequest.setNotifyUrl(varibaleUtils.getNotifyUrl());
            payRequest.setReturnUrl(varibaleUtils.getReturnUrl());
            payRequest.setBizContent(getBizContent(orderInfoVO));
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
            return ResultVO.success(response);
        }else{
            return ResultVO.error();
        }
    }

        private static String getBizContent(OrderInfoVO orderInfoVO){
            JSONObject bizContentJson=JSONAndEntityConvertUtils.entityToIndexJSONObject(orderInfoVO,true);
            if(bizContentJson.containsKey("status")){
                bizContentJson.remove("status");
            }
            return JSON.toJSONString(bizContentJson);
        }


}
