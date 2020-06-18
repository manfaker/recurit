package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.pojo.OrderInfoPojo;
import com.shenzhen.recurit.service.OrderInfoService;
import com.shenzhen.recurit.utils.ApplyConfigUtils;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("orderInfo")
@RestController
@Api(tags = {"订单模块"})
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;

    @PostMapping(value = "saveOrderInfo")
    @PermissionVerification
    @ApiOperation(value = "保存订单信息")
    public ResultVO saveOrderInfo(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        OrderInfoPojo orderInfoPojo= orderInfoService.saveOrderInfo(orderInfoVO);
        if(EmptyUtils.isNotEmpty(orderInfoPojo)){
            return ApplyConfigUtils.preCreate( InformationConstant.ALIPAY,orderInfoVO);
        }
        return ResultVO.success(orderInfoPojo);
    }

    @PutMapping(value = "updateOrderInfo")
    @PermissionVerification
    @ApiOperation(value = "修改订单信息")
    public ResultVO updateOrderInfo(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        int result = orderInfoService.updateOrderInfo(orderInfoVO);
        return ResultVO.success(result);
    }

    @GetMapping(value = "getOrderInfoById")
    @PermissionVerification
    @ApiOperation(value = "根据id查询订单信息")
    public ResultVO getOrderInfo(int id){
        OrderInfoPojo orderInfoPojo= orderInfoService.getOrderInfo(id);
        return ResultVO.success(orderInfoPojo);
    }

    @GetMapping(value = "getAllOrderInfo")
    @PermissionVerification
    @ApiOperation(value = "查询所有的订单信息")
    public ResultVO getAllOrderInfo(int payStatus){
        List<OrderInfoPojo> listAllOrderInfo = orderInfoService.getAllOrderInfo(payStatus);
        return ResultVO.success(listAllOrderInfo);
    }

    @DeleteMapping(value = "deleteOrderInfo")
    @PermissionVerification
    @ApiOperation(value = "删除订单信息")
    public ResultVO deleteOrderInfo(int id){
         int result = orderInfoService.deleteOrderInfo(id);
        return ResultVO.success(result);
    }

    
}
