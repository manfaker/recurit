package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.OrderInfoPojo;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.util.List;

public interface OrderInfoService {
    OrderInfoPojo saveOrderInfo(OrderInfoVO orderInfoVO);

    int updateOrderInfo(OrderInfoVO orderInfoVO);

    List<OrderInfoPojo> getAllOrderInfo(String payStatus);

    OrderInfoPojo getOrderInfo(int id);

    int deleteOrderInfo(int id);
}
