package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.CompanyVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.util.List;

/**
 * 2020/3/29
 * LiXiaoWei
 */
public interface CompanyService {
    /**
     * 添加公司（注册公司）
     * @param companyVO
     */
    ResultVO saveCompany(CompanyVO companyVO);

//    /**
//     * 根据公司id隐藏（删除）公司（删除后还有公司信息，但没有招聘信息）
//     * @param id
//     */
//    ResultVO deletePositionById(int id);
    /**
     * 修改公司信息
     * @param companyVO
     * @return
     */
    ResultVO updateCompany(CompanyVO companyVO);

    /**
     * 根据公司名字获取公司信息
     * @param companyName
     * @return
     */
    ResultVO getCompanyByName(String companyName);

    /**
     * 根据公司code获取公司信息
     * @param companyCode
     * @return
     */
    ResultVO getCompanyByCode(String companyCode);

    /**
     * 根据公司id获取公司信息
     * @param CompanyId
     * @return
     */
    ResultVO getCompanyById(int CompanyId);

    /**
     *根据公司内的任意信息查询公司信息
     * @param companyVO
     * @return
     */
    CompanyVO getByCompany(CompanyVO companyVO);
}
