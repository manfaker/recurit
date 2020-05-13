package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.AddressMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.AddressPojo;
import com.shenzhen.recurit.service.AddressService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.AddressVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    public List<AddressPojo> getAddressByLevel(String addressLevel) {
        return addressMapper.getAddressByLevel(addressLevel);
    }

    @Override
    public List<AddressPojo> getAddressParentNum(String parentNum) {
        return addressMapper.getAddressParentNum(parentNum);
    }

    @Override
    public AddressPojo getAddressById(int id) {
        return addressMapper.getAddressById(id);
    }

    @Override
    public int updateAddress(AddressVO addressVO) {
        setAddressInfo(addressVO,false);
        return addressMapper.updateAddress(addressVO);
    }

    private void setAddressInfo(AddressVO addressVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            addressVO.setCreater(user.getUserName());
            addressVO.setCreateDate(new Date());
        }
        addressVO.setUpdater(user.getUserName());
        addressVO.setUpdateDate(new Date());
    }


    @Override
    public AddressPojo addAddress(AddressVO addressVO) {
        setAddressInfo(addressVO,true);
        addressMapper.addAddress(addressVO);
        if(addressVO.getId()> NumberEnum.ZERO.getValue()){
            return addressMapper.getAddressById(addressVO.getId());
        }
        return null;
    }
}
