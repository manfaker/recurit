package com.shenzhen.recurit.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.OrderInfoService;
import com.shenzhen.recurit.service.PayService;
import com.shenzhen.recurit.utils.AMQPUtils;
import com.shenzhen.recurit.utils.EncryptBase64Utils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.utils.VaribaleUtils;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {

    private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Resource
    private VaribaleUtils varibaleUtils;
    @Resource
    private OrderInfoService orderInfoService;

    @Override
    public ResultVO alipayCallBackReturn(HttpServletRequest request) {
        logger.info("我支付成功了，我是同步接口");
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
    public ResultVO alipayAsyncReturn(String userCode) {
        logger.info("我支付成功了");
        ThreadLocalUtils.setUserCode(EncryptBase64Utils.encryptBASE64(userCode));
        AMQPUtils.producer(userCode);
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        orderInfoVO.setPayStatus(NumberEnum.TWO.getValue());
        orderInfoService.updateOrderInfo(orderInfoVO);
        return ResultVO.success("支付成功");
    }

    @Override
    public ResultVO getInfoByOrderNo() {
        return null;
    }
}
