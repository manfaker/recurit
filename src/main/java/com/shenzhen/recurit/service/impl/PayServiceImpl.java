package com.shenzhen.recurit.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.service.PayService;
import com.shenzhen.recurit.utils.VaribaleUtils;
import com.shenzhen.recurit.vo.ResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {

    @Resource
    private VaribaleUtils varibaleUtils;

    @Override
    public ResultVO alipayCallBackReturn(HttpServletRequest request) {
        Map<String,String> paramsMap = new HashMap<String, String>();
        Map parameterMap = request.getParameterMap();
        for(Iterator<String> iter = parameterMap.keySet().iterator(); iter.hasNext();){
            String name = iter.next();
            String[] values = (String [])parameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1 ) ? valueStr + values [i] : valueStr + values[i] + ",";
            }
            paramsMap.put(name,valueStr);
        }
        String outTradeNo = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");
        String sign = request.getParameter("sign");
        boolean flag = false;
        try {
            flag  = AlipaySignature.rsaCheckV1(paramsMap, varibaleUtils.getPublicKey(), InformationConstant.UTF_8,varibaleUtils.getSignType());// 对支付宝回调签名的校验
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ResultVO.error("订单异常");
        }
        if(!flag){
            return ResultVO.error("订单查询失败，请刷新页面重新操作");
        }
        return ResultVO.success("支付成功");
    }

    @Override
    public ResultVO alipayAsyncReturn() {
        return null;
    }

    @Override
    public ResultVO getInfoByOrderNo() {
        return null;
    }
}
