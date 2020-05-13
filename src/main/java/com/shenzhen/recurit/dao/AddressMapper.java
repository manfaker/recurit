package com.shenzhen.recurit.dao;


import com.shenzhen.recurit.pojo.AddressPojo;
import com.shenzhen.recurit.vo.AddressVO;

import java.util.List;

public interface AddressMapper {

    List<AddressPojo> getAddressByLevel(String addressLevel);

    List<AddressPojo> getAddressParentNum(String parentNum);

    AddressPojo getAddressById(int id);

    void addAddress(AddressVO addressVO);

    int updateAddress(AddressVO addressVO);
}
