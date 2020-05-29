package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.OrderInfoMapper;
import com.shenzhen.recurit.pojo.OrderInfoPojo;
import com.shenzhen.recurit.service.OrderInfoService;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;


    @Override
    public OrderInfoPojo saveOrderInfo(OrderInfoVO orderInfoVO) {
        setOrderInfo(orderInfoVO,true);
        orderInfoMapper.saveOrderInfo(orderInfoVO);
        return getOrderInfo(orderInfoVO.getId());
    }

    private void setOrderInfo(OrderInfoVO orderInfoVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            orderInfoVO.setCreateDate(new Date());
            orderInfoVO.setCreater(user.getUserName());
        }
        orderInfoVO.setUpdateDate(new Date());
        orderInfoVO.setUpdater(user.getUserName());
    }

    @Override
    public int updateOrderInfo(OrderInfoVO orderInfoVO) {
        setOrderInfo(orderInfoVO,true);
        return orderInfoMapper.updateOrderInfo(orderInfoVO);
    }

    @Override
    public List<OrderInfoPojo> getAllOrderInfo() {
        List<OrderInfoPojo> listOrderInfo = orderInfoMapper.getAllOrderInfo();
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
