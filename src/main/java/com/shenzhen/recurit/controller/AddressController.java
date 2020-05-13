package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.AddressPojo;
import com.shenzhen.recurit.service.AddressService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.AddressVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value="address")
@Api(tags = {"地址表查询"})
public class AddressController {

    @Resource
    private AddressService addressService;

    @GetMapping(value = "getAddressByLevel")
    @ApiOperation(value = "根据地级级别查找所有地址")
    @ApiImplicitParam(value = "级别",name="addressLevel",required = true)
    public ResultVO getAddressByLevel(String addressLevel){
        List<AddressPojo> listAddress = addressService.getAddressByLevel(addressLevel);
        return ResultVO.success(listAddress);
    }



    @GetMapping(value = "getAddressParentNum")
    @ApiOperation(value = "根据父级编码查找子集")
    @ApiImplicitParam(value = "父级编码",name="parentNum",required = true)
    public ResultVO getAddressParentNum(String parentNum){
        List<AddressPojo> listAddress = addressService.getAddressParentNum(parentNum);
        return ResultVO.success(listAddress);
    }

    @PutMapping(value = "updateAddress")
    @ApiOperation(value = "根据ID修改地址信息")
    public ResultVO updateAddress(@RequestBody @ApiParam AddressVO addressVO){
        int result = addressService.updateAddress(addressVO);
        if(result> NumberEnum.ZERO.getValue()){
            return ResultVO.success(addressService.getAddressById(addressVO.getId()));
        }else{
            return ResultVO.error(addressVO);
        }
    }

    @PostMapping(value = "addAddress")
    @ApiOperation(value = "根据ID修改地址信息")
    public ResultVO addAddress(@RequestBody @ApiParam AddressVO addressVO){
        AddressPojo addressPojo = addressService.addAddress(addressVO);
        if(EmptyUtils.isNotEmpty(addressPojo)){
            return ResultVO.success(addressService.getAddressById(addressVO.getId()));
        }else{
            return ResultVO.error(addressVO);
        }
    }




}
