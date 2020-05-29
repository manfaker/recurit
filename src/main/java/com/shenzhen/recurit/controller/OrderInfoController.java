package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.service.OrderInfoService;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("orderInfo")
@RestController
@Api(tags = {"订单模块"})
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;

    @PostMapping(value = "saveOrderInfo")
    public ResultVO saveOrderInfo(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        return null;
    }


    
}
