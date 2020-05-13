package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.AddressPojo;
import com.shenzhen.recurit.vo.AddressVO;

import java.util.List;

public interface AddressService {

    /**
     * 根据地级级别查找地址
     * @param addressLevel
     * @return
     */
    List<AddressPojo> getAddressByLevel(String addressLevel);
    /**
     * 根据父级编码查找子集
     * @param parentNum
     * @return
     */
    List<AddressPojo> getAddressParentNum(String parentNum);

    /**
     * 根据id查找地址信息
     * @param id
     * @return
     */
    AddressPojo getAddressById(int id);

    /**
     * 根据id修改地址信息
     * @param addressVO
     * @return
     */
    int updateAddress(AddressVO addressVO);

    /**
     * 新增地址信息
     * @param addressVO
     * @return
     */
    AddressPojo addAddress(AddressVO addressVO);
}
