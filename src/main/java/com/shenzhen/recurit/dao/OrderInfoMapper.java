package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.OrderInfoPojo;
import com.shenzhen.recurit.vo.OrderInfoVO;

import java.util.List;

public interface OrderInfoMapper {

    void saveOrderInfo(OrderInfoVO orderInfoVO);

    int updateOrderInfo(OrderInfoVO orderInfoVO);

    List<OrderInfoPojo> getAllOrderInfo();

    OrderInfoPojo getOrderInfo(int id);

    int deleteOrderInfo(int id);
}
