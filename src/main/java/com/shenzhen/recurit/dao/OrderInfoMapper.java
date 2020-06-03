package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.OrderInfoPojo;
import com.shenzhen.recurit.vo.OrderInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper {

    void saveOrderInfo(OrderInfoVO orderInfoVO);

    int updateOrderInfo(OrderInfoVO orderInfoVO);

    List<OrderInfoPojo> getAllOrderInfo(@Param("payStatus") int payStatus);

    OrderInfoPojo getOrderInfo(int id);

    int deleteOrderInfo(int id);
}
