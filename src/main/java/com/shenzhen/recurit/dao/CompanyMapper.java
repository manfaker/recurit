package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.CompanyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 2020/3/29
 * LiXiaoWei
 */
public interface CompanyMapper {

    int saveCompany(CompanyVO companyVO);

    CompanyVO getCompanyByCode(String companyCode);

    CompanyVO getCompanyByLegalRepresentative(String legalRepresentative);

    List<CompanyVO> getCompanyByName(String companyByName);

    int updateCompany(CompanyVO companyVO);
}
