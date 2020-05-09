package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.service.PayService;
import com.shenzhen.recurit.utils.ApplyConfigUtils;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.PositionUserRelationVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "pay")
@Api(tags = {"支付"})
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping(value ="payByAlipay" )
    @ApiOperation(value="支付订单")
    public Object payByAlipay(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
       return ApplyConfigUtils.payOrderNo(InformationConstant.ALIPAY, orderInfoVO);
    }

    @ApiOperation("支付成功后同步回调转支付成功页面")
    @GetMapping(value = "/alipay/callback/return")
    public Object alipayCallBackReturn(HttpServletRequest request){
        return payService.alipayCallBackReturn(request);
    }

    @ApiOperation("支付成功后异步回调支付信息")
    @PostMapping(value = "/alipay/async/return")
    public Object alipayAsyncReturn(){
        return payService.alipayAsyncReturn();
    }

    @ApiOperation("查询支付信息情况")
    @PostMapping(value = "query/getInfoByOrderNo")
    public Object getInfoByOrderNo(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        return ApplyConfigUtils.alipayQuery(InformationConstant.ALIPAY, orderInfoVO);
    }

    @ApiOperation("支付宝生成二维码")
    @PostMapping(value = "create/qRcode")
    public Object preCreateQRcode(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        return ApplyConfigUtils.preCreate( InformationConstant.ALIPAY,orderInfoVO);
    }

    @ApiOperation("支付宝关单")
    @PutMapping(value = "closeTrade")
    public Object closeTrade(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        return ApplyConfigUtils.closeTrade( InformationConstant.ALIPAY,orderInfoVO);
    }

    @ApiOperation("支付宝退款")
    @PutMapping(value = "refund")
    public Object refund(@RequestBody @ApiParam OrderInfoVO orderInfoVO){
        return ApplyConfigUtils.refund( InformationConstant.ALIPAY,orderInfoVO);
    }




}
